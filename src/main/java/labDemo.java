
import org.sikuli.script.Screen;

import java.io.*;


public class labDemo {

    // Set the screen to use for the tests
    // important when you have multiple screens (e.g. an external monitor in addition to the laptop monitor)
    private static final Screen myScreen = new Screen(Screen.getPrimaryId());

    public static Screen getMyScreen() {
        return myScreen;
    }

    public static Process openSUT() throws IOException {

        return Runtime.getRuntime().exec("java -jar C:/Users/duquet/Documents/SWT22/Lab7-Sikuli/LabSikuliX-Demo/lab08-Lab.jar");
    }

    /*
    public static void closesSUT() throws IOException {
        Runtime.getRuntime().exec("java -jar C:/Users/duquet/Documents/SWT22/Lab7-Sikuli/LabSikuliX-Demo/lab08-Lab.jar").destroy();
    }


    public static void messages(Process proc) throws IOException {
        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        String s = stdError.readLine();
        if ((s ) != null) {
            System.out.println(s);
            System.out.println("Check the path");
        }
    }
     */

}
