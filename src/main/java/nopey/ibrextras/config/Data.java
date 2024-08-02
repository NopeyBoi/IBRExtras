package nopey.ibrextras.config;

import net.minecraft.client.option.KeyBinding;
import nopey.ibrextras.objects.HudElement;

import java.util.ArrayList;

public class Data {
    public static boolean enabled = true;
    public static boolean speedometerBackground = false;

    public static boolean isRidingBoat;
    public static double speedVal;
    public static double oldSpeedVal;
    public static double displayedSpeedVal;
    public static double gVal;
    public static double angleVal;
    public static int pingVal;
    public static int fpsVal;
    public static double inertiaVal;
    public static double oldInertiaVal;
    public static double displayedInertiaVal;

    public static ArrayList<HudElement> hudElements = new ArrayList<>();

    public static KeyBinding keyEnable;
}
