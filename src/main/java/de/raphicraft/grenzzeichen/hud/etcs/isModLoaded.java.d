package de.raphicraft.grenzzeichen.hud.etcs;

public class isModLoaded {

    public static boolean isTramwaysLoaded() {
        try {
            Class.forName("purplecreate.tramways.Tramways");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
