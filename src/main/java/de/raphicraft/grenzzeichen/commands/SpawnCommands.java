package de.raphicraft.grenzzeichen.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class SpawnCommands {

    private static final String STATE_KEY = "custom_spawn";
    private static final String PREF_KEY = "custom_spawn_prefs";

    /** Call from your ModInitializer.onInitialize(): SpawnCommands.register(); */
    public static void register() {
        CommandRegistrationCallback.EVENT.register(SpawnCommands::registerCommands);
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher,
                                         CommandRegistryAccess registryAccess,
                                         CommandManager.RegistrationEnvironment environment) {

        // ---------- /setspawn (and /setspawn x y z) ----------
        LiteralArgumentBuilder<ServerCommandSource> setSpawn = CommandManager.literal("setspawn")
                .requires(src -> src.hasPermissionLevel(2)) // make this global; remove to allow all players
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                    if (player == null) return 0;

                    double x = player.getX();
                    double y = player.getY();
                    double z = player.getZ();
                    float yaw = player.getYaw();
                    float pitch = player.getPitch();
                    String worldName = player.getWorld().getRegistryKey().getValue().toString();

                    setSpawn(ctx.getSource().getServer(), x, y, z, worldName, yaw, pitch);
                    ctx.getSource().sendFeedback(() -> Text.literal("Spawn set to ")
                            .append(Text.literal(fmt(x) + " " + fmt(y) + " " + fmt(z)).formatted(Formatting.YELLOW))
                            .append(Text.literal(" facing ").formatted(Formatting.GRAY))
                            .append(Text.literal(String.format(Locale.ROOT, "yaw=%.1f pitch=%.1f", yaw, pitch)).formatted(Formatting.AQUA))
                            .append(Text.literal(" in ").formatted(Formatting.GRAY))
                            .append(Text.literal(worldName).formatted(Formatting.AQUA)), true);
                    return 1;
                })
                .then(CommandManager.argument("x", DoubleArgumentType.doubleArg())
                        .then(CommandManager.argument("y", DoubleArgumentType.doubleArg())
                                .then(CommandManager.argument("z", DoubleArgumentType.doubleArg())
                                        .executes(ctx -> {
                                            ServerPlayerEntity player = ctx.getSource().getPlayer();
                                            if (player == null) return 0;

                                            double x = DoubleArgumentType.getDouble(ctx, "x");
                                            double y = DoubleArgumentType.getDouble(ctx, "y");
                                            double z = DoubleArgumentType.getDouble(ctx, "z");
                                            float yaw = player.getYaw();   // still use player's current facing
                                            float pitch = player.getPitch();
                                            String worldName = player.getWorld().getRegistryKey().getValue().toString();

                                            setSpawn(ctx.getSource().getServer(), x, y, z, worldName, yaw, pitch);
                                            ctx.getSource().sendFeedback(() -> Text.literal("Spawn set to ")
                                                    .append(Text.literal(fmt(x) + " " + fmt(y) + " " + fmt(z)).formatted(Formatting.YELLOW))
                                                    .append(Text.literal(" facing ").formatted(Formatting.GRAY))
                                                    .append(Text.literal(String.format(Locale.ROOT, "yaw=%.1f pitch=%.1f", yaw, pitch)).formatted(Formatting.AQUA))
                                                    .append(Text.literal(" in ").formatted(Formatting.GRAY))
                                                    .append(Text.literal(worldName).formatted(Formatting.AQUA)), true);
                                            return 1;
                                        }))));

        dispatcher.register(setSpawn);

        // ---------- /resetspawn ----------
        dispatcher.register(CommandManager.literal("resetspawn")
                .requires(src -> src.hasPermissionLevel(2)) // optional
                .executes(ctx -> {
                    MinecraftServer server = ctx.getSource().getServer();
                    SpawnState state = getSpawnState(server);
                    state.spawnX = 0;
                    state.spawnY = 0;
                    state.spawnZ = 0;
                    state.spawnYaw = 0;
                    state.spawnPitch = 0;
                    state.worldName = "";
                    state.markDirty();
                    ctx.getSource().sendFeedback(() -> Text.literal("Spawn position has been reset.").formatted(Formatting.RED), true);
                    return 1;
                })
        );

        // ---------- Teleport aliases (everyone can use) ----------
        registerTeleportAlias(dispatcher, "spawn");
        registerTeleportAlias(dispatcher, "hub");
        registerTeleportAlias(dispatcher, "lobby");
        registerTeleportAlias(dispatcher, "leave");
        registerTeleportAlias(dispatcher, "home");

        // ---------- Hidden confirmation handler ----------
        dispatcher.register(CommandManager.literal("spawnconfirm")
                .then(CommandManager.argument("action", StringArgumentType.word())
                        .suggests((c, b) -> {
                            b.suggest("yes");
                            b.suggest("no");
                            b.suggest("never");
                            b.suggest("reset");
                            b.suggest("status");
                            return b.buildFuture();
                        })
                        .executes(ctx -> {
                            String action = StringArgumentType.getString(ctx, "action").toLowerCase(Locale.ROOT);
                            ServerPlayerEntity player = ctx.getSource().getPlayer();
                            if (player == null) return 0;

                            switch (action) {
                                case "yes" -> {
                                    return teleportToSavedSpawn(ctx.getSource());
                                }
                                case "no" -> {
                                    ctx.getSource().sendFeedback(() -> Text.literal("Teleport cancelled.").formatted(Formatting.GRAY), false);
                                    return 1;
                                }
                                case "never" -> {
                                    setConfirmationDisabled(ctx.getSource().getServer(), player.getUuid(), true);
                                    int res = teleportToSavedSpawn(ctx.getSource());
                                    if (res > 0) {
                                        ctx.getSource().sendFeedback(() -> Text.literal("You will no longer be asked for confirmation. ")
                                                .append(Text.literal("Use ").formatted(Formatting.GRAY))
                                                .append(Text.literal("/spawnconfirm reset").formatted(Formatting.YELLOW, Formatting.BOLD))
                                                .append(Text.literal(" to re-enable it.").formatted(Formatting.GRAY)), false);
                                    }
                                    return res;
                                }
                                case "reset" -> {
                                    setConfirmationDisabled(ctx.getSource().getServer(), player.getUuid(), false);
                                    ctx.getSource().sendFeedback(() -> Text.literal("Spawn confirmation prompt re-enabled.").formatted(Formatting.GREEN), false);
                                    return 1;
                                }
                                case "status" -> {
                                    boolean disabled = isConfirmationDisabled(ctx.getSource().getServer(), player.getUuid());
                                    ctx.getSource().sendFeedback(() -> Text.literal("Spawn confirmation is ")
                                            .append(Text.literal(disabled ? "DISABLED" : "ENABLED").formatted(disabled ? Formatting.RED : Formatting.GREEN))
                                            .append(Text.literal(".").formatted(Formatting.GRAY)), false);
                                    return 1;
                                }
                                default -> {
                                    ctx.getSource().sendFeedback(() -> Text.literal("Unknown option. Use ")
                                            .append(Text.literal("/spawnconfirm yes|no|never|reset|status").formatted(Formatting.YELLOW)), false);
                                    return 0;
                                }
                            }
                        })
                )
        );
    }

    /** Adds a literal command that starts the confirmation flow (or teleports directly if disabled). */
    private static void registerTeleportAlias(CommandDispatcher<ServerCommandSource> dispatcher, String literal) {
        dispatcher.register(CommandManager.literal(literal)
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                    if (player == null) return 0;

                    MinecraftServer server = ctx.getSource().getServer();
                    SpawnState state = getSpawnState(server);

                    if (!state.hasSpawn()) {
                        ctx.getSource().sendFeedback(() -> Text.literal("No spawn has been set yet! Use ")
                                .append(Text.literal("/setspawn").formatted(Formatting.YELLOW))
                                .append(Text.literal(".")), false);
                        return 0;
                    }

                    // If player disabled confirmations, inform how to re-enable and teleport instantly
                    if (isConfirmationDisabled(server, player.getUuid())) {
                        ctx.getSource().sendFeedback(() -> Text.literal("Teleporting to spawn. ")
                                .append(Text.literal("You disabled confirmations; use ").formatted(Formatting.GRAY))
                                .append(Text.literal("/spawnconfirm reset").formatted(Formatting.YELLOW, Formatting.BOLD))
                                .append(Text.literal(" to turn them back on.").formatted(Formatting.GRAY)), false);
                        return teleportToSavedSpawn(ctx.getSource());
                    }

                    // Show colorful clickable prompt
                    sendConfirmPrompt(ctx.getSource());
                    return 1;
                })
        );
    }

    /** Colorful clickable confirmation UI */
    private static void sendConfirmPrompt(ServerCommandSource source) {
        MutableText header = Text.literal("⟫ Are you sure you want to teleport to spawn?")
                .formatted(Formatting.GOLD, Formatting.BOLD);
        source.sendFeedback(() -> header, false);

        MutableText yes = Text.literal("[ YES ]")
                .styled(s -> s.withColor(Formatting.GREEN).withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawnconfirm yes"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to teleport now").formatted(Formatting.GREEN))));

        MutableText no = Text.literal("[ NO ]")
                .styled(s -> s.withColor(Formatting.RED).withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawnconfirm no"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Cancel teleport").formatted(Formatting.RED))));

        MutableText never = Text.literal("[ NEVER ASK AGAIN ]")
                .styled(s -> s.withColor(Formatting.GOLD).withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawnconfirm never"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Text.literal("Don't ask again (re-enable with /spawnconfirm reset)").formatted(Formatting.YELLOW))));

        MutableText divider = Text.literal("─────────").formatted(Formatting.DARK_GRAY);

        MutableText line = Text.literal(" ")
                .append(divider).append(Text.literal(" "))
                .append(yes).append(Text.literal("  "))
                .append(no).append(Text.literal("  "))
                .append(never).append(Text.literal(" "))
                .append(divider);

        source.sendFeedback(() -> line, false);
    }

    /** Teleport to saved spawn (uses stored yaw/pitch) */
    private static int teleportToSavedSpawn(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) return 0;

        MinecraftServer server = source.getServer();
        SpawnState state = getSpawnState(server);

        if (!state.hasSpawn()) {
            source.sendFeedback(() -> Text.literal("No spawn has been set yet! Use ")
                    .append(Text.literal("/setspawn").formatted(Formatting.YELLOW))
                    .append(Text.literal(".")), false);
            return 0;
        }

        RegistryKey<World> key = state.getWorldKey();
        ServerWorld targetWorld = server.getWorld(key);

        if (targetWorld == null) {
            targetWorld = server.getOverworld();
            source.sendFeedback(() -> Text.literal("Stored world not found. Teleporting to Overworld fallback.")
                    .formatted(Formatting.YELLOW), false);
        }

        player.teleport(targetWorld, state.spawnX, state.spawnY, state.spawnZ, state.spawnYaw, state.spawnPitch);
        source.sendFeedback(() -> Text.literal("Teleported to spawn!").formatted(Formatting.GREEN), false);
        return 1;
    }

    // ---------- Persistent storage helpers ----------
    private static void setSpawn(MinecraftServer server, double x, double y, double z, String worldName, float yaw, float pitch) {
        SpawnState state = getSpawnState(server);
        state.spawnX = x;
        state.spawnY = y;
        state.spawnZ = z;
        state.spawnYaw = yaw;
        state.spawnPitch = pitch;
        state.worldName = worldName;
        state.markDirty();
    }

    private static SpawnState getSpawnState(MinecraftServer server) {
        PersistentStateManager manager = server.getOverworld().getPersistentStateManager();
        return manager.getOrCreate(SpawnState::fromNbt, SpawnState::new, STATE_KEY);
    }

    private static PrefState getPrefState(MinecraftServer server) {
        PersistentStateManager manager = server.getOverworld().getPersistentStateManager();
        return manager.getOrCreate(PrefState::fromNbt, PrefState::new, PREF_KEY);
    }

    private static boolean isConfirmationDisabled(MinecraftServer server, UUID playerUuid) {
        return getPrefState(server).neverAsk.contains(playerUuid);
    }

    private static void setConfirmationDisabled(MinecraftServer server, UUID playerUuid, boolean disabled) {
        PrefState ps = getPrefState(server);
        if (disabled) ps.neverAsk.add(playerUuid);
        else ps.neverAsk.remove(playerUuid);
        ps.markDirty();
    }

    private static String fmt(double d) {
        return String.format(Locale.ROOT, "%.1f", d);
    }

    // ---------- PersistentState: spawn position ----------
    private static class SpawnState extends PersistentState {
        double spawnX = 0, spawnY = 64, spawnZ = 0;
        float spawnYaw = 0, spawnPitch = 0;
        String worldName = ""; // e.g. "minecraft:overworld"

        static SpawnState fromNbt(NbtCompound nbt) {
            SpawnState state = new SpawnState();
            if (nbt.contains("world")) {
                state.spawnX = nbt.getDouble("x");
                state.spawnY = nbt.getDouble("y");
                state.spawnZ = nbt.getDouble("z");
                state.spawnYaw = nbt.getFloat("yaw");
                state.spawnPitch = nbt.getFloat("pitch");
                state.worldName = nbt.getString("world");
            }
            return state;
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt) {
            nbt.putDouble("x", spawnX);
            nbt.putDouble("y", spawnY);
            nbt.putDouble("z", spawnZ);
            nbt.putFloat("yaw", spawnYaw);
            nbt.putFloat("pitch", spawnPitch);
            nbt.putString("world", worldName);
            return nbt;
        }

        boolean hasSpawn() {
            return worldName != null && !worldName.isEmpty();
        }

        RegistryKey<World> getWorldKey() {
            Identifier id = new Identifier(worldName);
            return RegistryKey.of(RegistryKeys.WORLD, id);
        }
    }

    // ---------- PersistentState: per-player preferences ----------
    private static class PrefState extends PersistentState {
        Set<UUID> neverAsk = new HashSet<>();

        static PrefState fromNbt(NbtCompound nbt) {
            PrefState ps = new PrefState();
            if (nbt.contains("neverAsk", NbtElement.LIST_TYPE)) {
                NbtList list = nbt.getList("neverAsk", NbtElement.STRING_TYPE);
                for (int i = 0; i < list.size(); i++) {
                    try {
                        ps.neverAsk.add(UUID.fromString(list.getString(i)));
                    } catch (Exception ignored) {}
                }
            }
            return ps;
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt) {
            NbtList list = new NbtList();
            for (UUID id : neverAsk) {
                list.add(NbtString.of(id.toString()));
            }
            nbt.put("neverAsk", list);
            return nbt;
        }
    }
}
