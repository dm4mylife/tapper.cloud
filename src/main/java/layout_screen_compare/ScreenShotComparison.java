package layout_screen_compare;

import common.BaseActions;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static data.Constants.*;

public class ScreenShotComparison {

    static BaseActions baseActions = new BaseActions();

    public static void createDirectory(String path) {

        File directory = new File(path);

        if (!directory.exists())
            Assertions.assertTrue(directory.mkdirs(),"Директория не создалась");

    }

    public static void makeOriginalScreenshot(String type,String expectedName) throws IOException {

        baseActions.forceWait(WAIT_FOR_DELETE_ARTEFACT_BEFORE_SCREEN);

        String fullPath = type.equals("desktop") ?
                DESKTOP_SCREENSHOTS_COMPARISON_ORIGINAL_PATH :
                MOBILE_SCREENSHOTS_COMPARISON_ORIGINAL_PATH;

        createDirectory(fullPath);

        String pathname = fullPath + "original_" + expectedName + ".png";
        Screenshot actual = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .takeScreenshot(getWebDriver());
        ImageIO.write(actual.getImage(), "png", new File(pathname));

    }

    public static void makeOriginalScreenshot(String type,String expectedName, Set<By> setIgnoredElements) throws IOException {

        baseActions.forceWait(WAIT_FOR_DELETE_ARTEFACT_BEFORE_SCREEN);

        String fullPath = type.equals("desktop") ?
                DESKTOP_SCREENSHOTS_COMPARISON_ORIGINAL_PATH :
                MOBILE_SCREENSHOTS_COMPARISON_ORIGINAL_PATH;

        createDirectory(fullPath);

        String pathname = fullPath + "original_" + expectedName + ".png";
        Screenshot original = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .ignoredElements(setIgnoredElements)
                .takeScreenshot(getWebDriver());
        ImageIO.write(original.getImage(), "png", new File(pathname));

    }

    public static void attachScreenToAllureReport(String filePathName, String allureImageName) throws IOException {

        BufferedImage bImage = ImageIO.read(new File(filePathName));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos);
        byte[] data = bos.toByteArray();
        Allure.addAttachment(allureImageName, new ByteArrayInputStream(data));

    }

    public static void makeImageDiff(String type,String name, double diffPercent, int imagePixelSize)
            throws IOException {

        baseActions.forceWait(WAIT_FOR_DELETE_ARTEFACT_BEFORE_SCREEN);

        String fullPathActual;
        String fullPathOriginal;
        String fullPathDiff;

        if (type.equals("desktop")) {

            fullPathActual = DESKTOP_SCREENSHOTS_COMPARISON_ACTUAL_PATH;
            fullPathOriginal = DESKTOP_SCREENSHOTS_COMPARISON_ORIGINAL_PATH;
            fullPathDiff = DESKTOP_SCREENSHOTS_COMPARISON_DIFF_PATH;

        } else {

            fullPathActual = MOBILE_SCREENSHOTS_COMPARISON_ACTUAL_PATH;
            fullPathOriginal = MOBILE_SCREENSHOTS_COMPARISON_ORIGINAL_PATH;
            fullPathDiff = MOBILE_SCREENSHOTS_COMPARISON_DIFF_PATH;

        }

        createDirectory(fullPathActual);
        createDirectory(fullPathOriginal);
        createDirectory(fullPathDiff);

        String originalName = "original_" + name + ".png";
        String diffName = "diff_" + name + ".png";
        String fullPathNameActual= fullPathActual + name + ".png";
        String fullPathNameOriginal = fullPathOriginal + originalName;
        String fullPathNameDiff = fullPathDiff + diffName;

        Allure.label("testType", "screenshotDiff");

        Screenshot actual = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .takeScreenshot(getWebDriver());
        ImageIO.write(actual.getImage(), "png", new File(fullPathNameActual));

        Screenshot original = new Screenshot(ImageIO.read(new File(fullPathNameOriginal)));
        ImageDiff diff = new ImageDiffer().makeDiff(original, actual);
        BufferedImage diffImage = diff.getMarkedImage();
        ImageIO.write(diffImage, "png", new File(fullPathNameDiff));

        attachScreenToAllureReport(fullPathNameDiff,"diff");
        attachScreenToAllureReport(fullPathNameOriginal,"actual");
        attachScreenToAllureReport(fullPathNameActual,"expected");

        double diffPixelPercentRatio = imagePixelSize * (diffPercent / 100);

        System.out.println("Общее количество различающихся пикселей " + diff.getDiffSize() +
                "\nДопустимое количество различающихся пикселей " + diffPixelPercentRatio);

        Assertions.assertFalse(diff.getDiffSize() > diffPixelPercentRatio,
                "Скриншоты различаются в более чем " + diffPercent + "%");

    }

    public static void makeImageDiff(String type,String name, double diffPercent, int imagePixelSize,
                                     Set<By> setIgnoredElements) throws IOException {

        baseActions.forceWait(WAIT_FOR_DELETE_ARTEFACT_BEFORE_SCREEN);

        String fullPathActual;
        String fullPathOriginal;
        String fullPathDiff;

        if (type.equals("desktop")) {

            fullPathActual = DESKTOP_SCREENSHOTS_COMPARISON_ACTUAL_PATH;
            fullPathOriginal = DESKTOP_SCREENSHOTS_COMPARISON_ORIGINAL_PATH;
            fullPathDiff = DESKTOP_SCREENSHOTS_COMPARISON_DIFF_PATH;

        } else {

            fullPathActual = MOBILE_SCREENSHOTS_COMPARISON_ACTUAL_PATH;
            fullPathOriginal = MOBILE_SCREENSHOTS_COMPARISON_ORIGINAL_PATH;
            fullPathDiff = MOBILE_SCREENSHOTS_COMPARISON_DIFF_PATH;

        }

        createDirectory(fullPathActual);
        createDirectory(fullPathOriginal);
        createDirectory(fullPathDiff);

        String originalName = "original_" + name + ".png";
        String diffName = "diff_" + name + ".png";
        String fullPathNameActual= fullPathActual + name + ".png";
        String fullPathNameOriginal = fullPathOriginal + originalName;
        String fullPathNameDiff = fullPathDiff + diffName;

        Allure.label("testType", "screenshotDiff");

        Screenshot actual = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .ignoredElements(setIgnoredElements)
                .takeScreenshot(getWebDriver());
        ImageIO.write(actual.getImage(), "png", new File(fullPathNameActual));

        Screenshot original = new Screenshot(ImageIO.read(new File(fullPathNameOriginal)));
        ImageDiff diff = new ImageDiffer().makeDiff(original, actual);
        BufferedImage diffImage = diff.getMarkedImage();
        ImageIO.write(diffImage, "png", new File(fullPathNameDiff));

        attachScreenToAllureReport(fullPathNameDiff,"diff");
        attachScreenToAllureReport(fullPathNameOriginal,"actual");
        attachScreenToAllureReport(fullPathNameActual,"expected");

        double diffPixelPercentRatio = imagePixelSize * (diffPercent / 100);

        System.out.println("Общее количество различающихся пикселей " + diff.getDiffSize() +
                "\nДопустимое количество различающихся пикселей " + diffPixelPercentRatio);

        Assertions.assertFalse(diff.getDiffSize() > diffPixelPercentRatio,
                "Скриншоты различаются в более чем " + diffPercent + "%");

    }

    public static void isScreenOrDiff(String browserSizeType,boolean type,String expectedName,double diffPercent,
                                      int imagePixelSize)
            throws IOException {

        if (type) {

            makeOriginalScreenshot(browserSizeType,expectedName);

        } else {

            makeImageDiff(browserSizeType,expectedName,diffPercent, imagePixelSize);

        }

    }

    public static void isScreenOrDiff(String browserSizeType,boolean type,String expectedName,double diffPercent,
                                      int imagePixelSize,Set<By> setIgnoredElements)
            throws IOException {

        if (type) {

            makeOriginalScreenshot(browserSizeType,expectedName,setIgnoredElements);

        } else {

            makeImageDiff(browserSizeType,expectedName,diffPercent, imagePixelSize,setIgnoredElements);

        }

    }

    public static Set<By> setIgnoredElements (ArrayList<By> elements) {

        return new HashSet<>(elements);

    }

}