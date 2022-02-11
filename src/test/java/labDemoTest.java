
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sikuli.script.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class labDemoTest {

    Process p;

    @BeforeEach
    void setup() throws IOException, InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        p = labDemo.openSUT();
    }

    @AfterEach
    void teardown() throws InterruptedException {
        p.destroy();
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    void test_button_text() throws FindFailed {

        labDemo.getMyScreen().mouseMove();
        ImagePath.setBundlePath("./src/main/resources/test_button");
        Pattern green = new Pattern("green");
        Pattern red = new Pattern("red");
        Pattern pink = new Pattern("pink");
        Pattern regionText = new Pattern("regionWithText");

        List<String> correctLines = Arrays.asList("Green button was clicked!", "Red button was clicked!", "Pink button was clicked!");
        List<Pattern> imgs = Arrays.asList(green, red, pink);
        int count = 0;
        for (Pattern i : imgs) {
            labDemo.getMyScreen().wait(i.similar(0.9), 2).click();
            String text = labDemo.getMyScreen().find(regionText).text();
            System.out.println(text);
            System.out.println(correctLines.get(count));
            Assertions.assertEquals(correctLines.get(count), text);
            count = count + 1;
        }

    }

    @Test
    void test_button_unchanged() throws FindFailed {

        labDemo.getMyScreen().mouseMove();
        ImagePath.setBundlePath("./src/main/resources/test_button");

        Pattern green = new Pattern("green");
        Pattern red = new Pattern("red");
        Pattern pink = new Pattern("pink");

        List<Pattern> imgs = Arrays.asList(green, red, pink);
        Pattern threeButtons = new Pattern("threeButtons");
        for (Pattern i : imgs) {
            labDemo.getMyScreen().wait(i.similar(0.9), 2).click();
            System.out.println(labDemo.getMyScreen().exists(threeButtons.exact(), 2));
            Assertions.assertNotNull(labDemo.getMyScreen().exists(threeButtons.exact(), 2));
        }
    }

    @Test
    void test_editor() throws FindFailed {

        labDemo.getMyScreen().mouseMove();
        ImagePath.setBundlePath("./src/main/resources/test_editor");

        File directoryPath = new File("C:/Users/duquet/Documents/SWT22/Lab7-Sikuli/LabSikuliX-Demo/src/main/resources/test_editor");
        File[] imgList = directoryPath.listFiles();

        for (File file : imgList != null ? imgList : new File[0]) {

            if (file.getName().equals("6-text.png")) {
                break;
            }

            Pattern img = new Pattern(file.getPath());
            labDemo.getMyScreen().wait(img.similar(0.9), 2).click();
            labDemo.getMyScreen().mouseUp();

            if (file.getName().equals("3-text.png")) {
                labDemo.getMyScreen().write("Tere! Hi! Hola!" + Key.ENTER + "Go little Rock Star!");
            }
        }

        for (File file : imgList != null ? imgList : new File[0]) {
            if (file.getName().equals("3-text.png")) {
                Pattern text = new Pattern("6-text.png");
                System.out.println(labDemo.getMyScreen().exists(text.exact(), 2));
                Assertions.assertNotNull(labDemo.getMyScreen().exists(text.exact(), 2));
                break;
            }
            Pattern img = new Pattern(file.getPath());
            labDemo.getMyScreen().wait(img, 2).click();
        }
    }

    @Test
    void test_copyable() throws InterruptedException, FindFailed {

        labDemo.getMyScreen().mouseMove();
        ImagePath.setBundlePath("./src/main/resources/test_copiable");

        Pattern copiableTab = new Pattern("copiable_tab");
        Pattern reg = new Pattern("region");
        Pattern copy = new Pattern("copy").similar(0.7);

        // long pid = p.pid();

        TimeUnit.SECONDS.sleep(2);

        int x = labDemo.getMyScreen().find(reg).getX();
        int y = labDemo.getMyScreen().find(reg).getY();
        int w = labDemo.getMyScreen().find(reg).getW();
        int h = labDemo.getMyScreen().find(reg).getH();

        labDemo.getMyScreen().wait(copiableTab.similar(0.9), 2).click();
        TimeUnit.SECONDS.sleep(1);
        List<String> word = Arrays.asList("First", "Second", "Third", "Forth", "Fifth");
        List<Integer> pos = new ArrayList<>();
        List<Match> listW = labDemo.getMyScreen().newRegion(x, y, w, h).findLines();

        for (Match m : listW) {
            if (word.contains(m.getText())) {
                pos.add(listW.indexOf(m));
            }
        }

        for (int i : pos) {
            labDemo.getMyScreen().newRegion(x, y, w, h).doubleClick(listW.get(i));
            TimeUnit.SECONDS.sleep(2);
            labDemo.getMyScreen().newRegion(x, y, w, h).rightClick(listW.get(i));
            TimeUnit.SECONDS.sleep(2);
            Assertions.assertNotNull(labDemo.getMyScreen().newRegion(x, y, w, h).exists(copy, 2));
            labDemo.getMyScreen().mouseMove();
            labDemo.getMyScreen().click();
            TimeUnit.SECONDS.sleep(2);
        }
    }

    @Test
    void test_folder() throws InterruptedException, FindFailed {

        TimeUnit.SECONDS.sleep(5);

        labDemo.getMyScreen().mouseMove();
        ImagePath.setBundlePath("./src/main/resources/test_folder");

        Pattern redFolder = new Pattern("redFolder");
        Pattern appFolder = new Pattern("app");
        Pattern reg = new Pattern("region");
        Pattern folderTab = new Pattern("folderTab");

        labDemo.getMyScreen().find(folderTab).click();
        TimeUnit.SECONDS.sleep(2);
        int x = labDemo.getMyScreen().find(reg).getX();
        int y = labDemo.getMyScreen().find(reg).getY();
        int w = labDemo.getMyScreen().find(reg).getW();
        int h = labDemo.getMyScreen().find(reg).getH();
        TimeUnit.SECONDS.sleep(2);
        List<Match> listW = labDemo.getMyScreen().newRegion(x, y, w, h).findAllList(appFolder);
        Match redFoldPos = labDemo.getMyScreen().newRegion(x, y, w, h).find(redFolder);
        TimeUnit.SECONDS.sleep(2);
        for (Match m : listW) {
            labDemo.getMyScreen().newRegion(x, y, w, h).dragDrop(m, redFoldPos);
            TimeUnit.SECONDS.sleep(1);
        }
//        List<Match> listW2 = labDemo.getMyScreen().newRegion(x, y, w, h).findAllList("app");
//        System.out.print(listW2.size());
        Assertions.assertNull(labDemo.getMyScreen().newRegion(x, y, w, h).exists(appFolder));
    }

    @Test
    void test_resizer() throws InterruptedException, FindFailed {

        TimeUnit.SECONDS.sleep(5);

        labDemo.getMyScreen().mouseMove();
        ImagePath.setBundlePath("./src/main/resources/test_resizer");

        Pattern resizerTab = new Pattern("resizerTab");
        Pattern redbar = new Pattern("redbar");
        Pattern desktext = new Pattern("desk_table_mobile");
        Pattern folder = new Pattern("folder");

        labDemo.getMyScreen().find(resizerTab).click();

        int x = labDemo.getMyScreen().find(desktext).getX();
        int y = labDemo.getMyScreen().find(desktext).getY();
        int w = labDemo.getMyScreen().find(desktext).getW();
        int h = labDemo.getMyScreen().find(desktext).getH();

        List<Match> listW = new ArrayList<>();

        listW.add(labDemo.getMyScreen().newRegion(x, y, w, h).findText("Desktop"));
        listW.add(labDemo.getMyScreen().newRegion(x, y, w, h).findText("Tablet"));
        listW.add(labDemo.getMyScreen().newRegion(x, y, w, h).findText("Mobile"));

        for (Match m : listW) {
            System.out.println(m.getText());
            TimeUnit.SECONDS.sleep(1);
            labDemo.getMyScreen().newRegion(x, y, w, h).click(m);
            TimeUnit.SECONDS.sleep(2);
            List<Match> border = labDemo.getMyScreen().findAllList(redbar);
            TimeUnit.SECONDS.sleep(2);
            //labDemo.getMyScreen().newRegion(border.get(0).getX() + border.get(0).getW(), border.get(0).getY(),border.get(1).getX() - border.get(0).getX(),border.get(0).getH()).highlight();
            Region s = new Screen().newRegion(border.get(0).getX() + border.get(0).getW(), border.get(0).getY(), border.get(1).getX() - border.get(0).getX(), border.get(0).getH());
            TimeUnit.SECONDS.sleep(4);
            //List<Match> folderlist = s.highlight(1).findAllList(folder);
            List<Match> folderlist = s.findAllList(folder);
            TimeUnit.SECONDS.sleep(4);

            Assertions.assertEquals(5, folderlist.size());

            TimeUnit.SECONDS.sleep(1);
        }

    }
}