import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sikuli.script.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LabDemoTest {

    private static Process process;
    private static final Screen screen = LabDemo.getMyScreen();

    @BeforeEach
    void setup() throws IOException, InterruptedException {
        process = LabDemo.openSUT();
        TimeUnit.SECONDS.sleep(2);
        screen.mouseMove();
    }

    @AfterEach
    void teardown() throws InterruptedException {
        process.destroy();
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * Tab 1 (Buttons):
     * General:
     *   * The tab consists of 3 buttons (green, red, pink).
     * Functionality to test:
     *   * After clicking each button, a text will appear below the buttons.
     *     For green button it will be “Green button was clicked!”,
     *     red -> “Red button was clicked!”
     *     and pink -> “Pink button was clicked!”. [“test_buttons_text”]
     *
     * This test should pass.
     */

    @Test
    void test_button_text() throws FindFailed, InterruptedException {

        // Define the location, where the images will be searched for
        ImagePath.setBundlePath("./src/main/resources/test_button");

        Pattern green = new Pattern("green");
        Pattern red = new Pattern("red");
        Pattern pink = new Pattern("pink");
        Pattern regionText = new Pattern("regionWithText");

        int x = screen.find(regionText).getX();
        int y = screen.find(regionText).getY();
        int w = screen.find(regionText).getW();
        int h = screen.find(regionText).getH();

        // Create a new region
        Region newReg = new Screen().newRegion(x , y, w, h);
        // show the new screen
        newReg.highlight(1);
        // List of the correct lines, i.e., the lines that should appear after the click action
        List<String> correctLines = Arrays.asList("Green button was clicked!", "Red button was clicked!", "Pink button was clicked!");
        // List with all the patterns (images) that the test will need to click
        List<Pattern> images = Arrays.asList(green, red, pink);

        int count = 0;
        for (Pattern i : images) {
            newReg.wait(i.similar(0.9), 2).click();
            String text = newReg.text();
            Assertions.assertEquals(correctLines.get(count), text);
            count = count + 1;
        }
    }

    /**
     * Tab 1 (Buttons):
     *
     * General:
     *   * The tab consists of 3 buttons (green, red, pink).
     * Functionality to test:
     *   * All buttons should stay unchanged after clicking. (implicit task). [“test_buttons_unchanged”]
     *
     * This test should fail, because the pink button disappears, although it should not.
     */

    @Test
    void test_button_unchanged() throws FindFailed {

        ImagePath.setBundlePath("./src/main/resources/test_button");

        Pattern green = new Pattern("green");
        Pattern red = new Pattern("red");
        Pattern pink = new Pattern("pink");
        Pattern threeButtons = new Pattern("threeButtons").similar(0.5);

        List<Pattern> imgs = Arrays.asList(green, red, pink);

        for (Pattern i : imgs) {
            screen.wait(i.similar(0.9), 2).click();
            for (Pattern i2 : imgs) {
                //System.out.println(screen.exists(i2.similar(0.97), 2));
                Assertions.assertNotNull(screen.exists(i2.similar(0.97), 2));
            }
        }

        // Solution 2
        //for (Pattern i : imgs) {
        //    screen.wait(i.similar(0.9), 2).click();
        //    System.out.println(screen.exists(threeButtons.exact(), 2));
        //    Assertions.assertNotNull(screen.exists(threeButtons.exact(), 2));
        //}
    }


    /**
     * Tab 2 (Editor):
     *
     * General:
     *   * The tab consists of an expandable pane (it is randomly closed or open when opening the app),
     *     that includes a text editor.
     * Functionality to test:
     *   * After closing and opening the pane, text in the text editor must stay the same. [“test_editor”]
     *
     * This test should pass.
     */

    @Test
    void test_editor() throws FindFailed {

        // Specify the folder where the images for the test are located
        ImagePath.setBundlePath("./src/main/resources/test_editor");

        // Find text "Editor" and click on it
        screen.findText("Editor").click();

        // Create patterns for pane closed and pane open
        Pattern firstPaneClosed = new Pattern("firstPaneClosed");
        Pattern firstPaneOpen = new Pattern("firstPaneOpen");

        // Determine whether the tab is closed or open
        if (screen.exists(firstPaneClosed.similar(0.8)) != null) {
            // If the pane was closed then click on it to open the pane
            // Wait for Sikuli to find the place similar to the picture of closed pane and click on it to open it
            screen.wait(firstPaneClosed.similar(0.8), 2).click();
        }

        // Wait for Sikuli to find the text box and click on it to activate the writing functionality
        screen.wait(new Pattern("textField").similar(0.9), 2).click();
        // Write text to the text box
        screen.write("Tere! Hi! Hola!" + Key.ENTER + "Go little Rock Star!");
        // Wait for Sikuli to find the place similar to the picture of pane named "First" and click on it to close
        screen.wait(firstPaneOpen.similar(0.8), 2).click();
        // Click on the same place on the screen to open the tab again
        screen.click();

        // Assert if the written lines are present on the screen
        Assertions.assertNotNull(screen.existsText("Tere! Hi! Hola!", 2));
        Assertions.assertNotNull(screen.existsText("Go little Rock Star!", 2));
    }

    /**
     * Tab 3 (Copyable):
     *
     * General:
     *   * The tab consists of 5 words.
     *   * Colors of the words are generated randomly every time you run the app.
     * Functionality to test:
     *   * Each word should be copyable. [“test_copyable”]
     *
     * This test should fail, because two words are not copiable.
     */

    @Test
    void test_copyable() throws InterruptedException, FindFailed {

        // Specify the folder where the images for the test are located
        ImagePath.setBundlePath("./src/main/resources/test_copiable");


        Pattern reg = new Pattern("region");
        Pattern copy = new Pattern("copy").similar(0.7);

        TimeUnit.SECONDS.sleep(1);

        int x = screen.find(reg).getX();
        int y = screen.find(reg).getY();
        int w = screen.find(reg).getW();
        int h = screen.find(reg).getH();

        // Create a new region
        Region newReg = new Screen().newRegion(x , y, w, h);

        // Find text "Copiable" and click on it
        newReg.findText("Copiable").click();
        TimeUnit.SECONDS.sleep(1);

        // List of the words
        List<String> word = Arrays.asList("First", "Second", "Third", "Forth", "Fifth");
        List<Integer> pos = new ArrayList<>();
        List<Match> listW = newReg.findLines();

        for (Match m : listW) {
            if (word.contains(m.getText())) {
                pos.add(listW.indexOf(m));
            }
        }

        for (int i : pos) {
            screen.newRegion(x, y, w, h).doubleClick(listW.get(i));
            TimeUnit.SECONDS.sleep(2);
            screen.newRegion(x, y, w, h).rightClick(listW.get(i));
            TimeUnit.SECONDS.sleep(2);
            Assertions.assertNotNull(screen.newRegion(x, y, w, h).exists(copy, 2));
            screen.mouseMove();
            screen.click();
            TimeUnit.SECONDS.sleep(2);
        }
    }

    /**
     * Tab 4 (Folder):
     *
     * General:
     *   * Dragging an “app” (grey icon) to the folder (red icon) should move it to the
     *     folder (“app” should disappear from the scene).
     * Functionality to test:
     *   * Folders can take a maximum of two “apps”. [“test_folder”]
     *
     * This test should pass.
     */

    @Test
    void test_folder() throws InterruptedException, FindFailed {

        ImagePath.setBundlePath("./src/main/resources/test_folder");

        Pattern redFolder = new Pattern("redFolder");
        Pattern appFolder = new Pattern("app");
        Pattern folderTab = new Pattern("folderTab");

        screen.find(folderTab).click();

        List<Match> listW = screen.findAllList(appFolder);
        Match redFoldPos = screen.find(redFolder);

        int count = 0;
        for (Match m : listW) {
            screen.dragDrop(m, redFoldPos);
             if(count == 2){
                List<Match> listApp = screen.findAllList(appFolder);
                Assertions.assertEquals(listApp.size(), listW.size()-2);
                break;
             }
            count = count + 1;
        }
    }

    /**
     * Tab 5 (Resizer):
     *
     * General:
     *   * The tab consists of a “screen” with red borders. Borders are randomized
     *   each time you “resize” the screen (click on the “Desktop”/”Tablet”/”Mobile” button)
     * Functionality to test:
     *   * After “resizing” the screen folder buttons should stay in the confinements of
     *     the borders. [“test_resizer”]
     *
     * This test should pass.
     */

/*    @Test
    void test_resizer() throws InterruptedException, FindFailed {

        ImagePath.setBundlePath("./src/main/resources/test_resizer");

        Pattern redbar = new Pattern("redbar");
        Pattern desktext = new Pattern("desk_table_mobile");
        Pattern folder = new Pattern("folder");
        Pattern desktop = new Pattern("desktop").similar(0.7);
        Pattern mobile = new Pattern("mobile").similar(0.7);
        Pattern tablet = new Pattern("table").similar(0.7);
        Pattern region = new Pattern("region").similar(0.7);

        screen.find(region).highlight(1);
        List<Pattern> images = Arrays.asList(desktop, mobile, tablet);
        // Find text "Resizer" and click on it
        screen.findText("Resizer").click();
        screen.wait(desktop.similar(0.7), 2).click();
        //screen.find("desktop").click();
        //screen.find("mobile").click();
        //screen.find("tablet").click();

//        int x = screen.find(desktext).getX();
//        int y = screen.find(desktext).getY();
//        int w = screen.find(desktext).getW();
//        int h = screen.find(desktext).getH();
        List<Match> listW = new ArrayList<>();

        listW.add(screen.findText("Desktop"));
        listW.add(screen.findText("Tablet"));
        listW.add(screen.findText("Mobile"));

*//*        listW.add(screen.newRegion(x, y, w, h).findText("Desktop"));
        listW.add(screen.newRegion(x, y, w, h).findText("Tablet"));
        listW.add(screen.newRegion(x, y, w, h).findText("Mobile"));*//*

        for (Match m : listW) {
            System.out.println(m.getText());
            TimeUnit.SECONDS.sleep(1);
            screen.click(m);
            //screen.newRegion(x, y, w, h).click(m);
            TimeUnit.SECONDS.sleep(2);
            List<Match> border = screen.findAllList(redbar);
            TimeUnit.SECONDS.sleep(2);
            //screen.newRegion(border.get(0).getX() + border.get(0).getW(), border.get(0).getY(),border.get(1).getX() - border.get(0).getX(),border.get(0).getH()).highlight();
            Region s = new Screen().newRegion(border.get(0).getX() + border.get(0).getW(), border.get(0).getY(), border.get(1).getX() - border.get(0).getX(), border.get(0).getH());
            TimeUnit.SECONDS.sleep(4);
            //List<Match> folderlist = s.highlight(1).findAllList(folder);
            List<Match> folderlist = s.findAllList(folder);
            TimeUnit.SECONDS.sleep(4);

            Assertions.assertEquals(5, folderlist.size());

            TimeUnit.SECONDS.sleep(1);
        }
    }*/
}
