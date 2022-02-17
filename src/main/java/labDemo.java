
import org.sikuli.script.Screen;

import java.io.*;


public class LabDemo {

    // Set the screen to use for the tests
    // useful when you have multiple screens
    // (e.g. an external monitor in addition to the laptop monitor)
    private static final Screen myScreen = new Screen(Screen.getPrimaryId());
    //private static final Screen myScreen = new Screen(2);

    public static Screen getMyScreen() {
        return myScreen;
    }

    public static Process openSUT() throws IOException {
        // TODO
        // return new ProcessBuilder().command("java -jar ./lab08-Lab.jar").start();
        return Runtime.getRuntime().exec("java -jar ./lab08-Lab.jar");
    }

}
