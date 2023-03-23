package layout_screen_compare;

import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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
import java.util.Set;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static data.Constants.*;

public class ScreenShotComparison {

    static BaseActions baseActions = new BaseActions();

    public static void makeOriginalScreenshot(String expectedName) throws IOException {

        baseActions.forceWait(WAIT_FOR_DELETE_ARTEFACT_BEFORE_SCREEN);

        String pathname = SCREENSHOTS_COMPARISON_EXPECTED_PATH + expectedName + ".png";
        Screenshot actual = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .takeScreenshot(getWebDriver());
        ImageIO.write(actual.getImage(), "png", new File(pathname));

    }

    public static void makeOriginalScreenshot(String expectedName, Set<By> setIgnoredElements) throws IOException {

        String pathname = SCREENSHOTS_COMPARISON_EXPECTED_PATH + expectedName + ".png";
        Screenshot actual = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .ignoredElements(setIgnoredElements)
                .takeScreenshot(getWebDriver());
        ImageIO.write(actual.getImage(), "png", new File(pathname));

    }

    public static void attachScreenToAllureReport(String filePathName, String allureImageName) throws IOException {

        BufferedImage bImage = ImageIO.read(new File(filePathName));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos);
        byte[] data = bos.toByteArray();
        Allure.addAttachment(allureImageName, new ByteArrayInputStream(data));

    }


    public static void makeImageDiff(String expectedName, double diffPercent, int imagePixelSize) throws IOException {

        baseActions.forceWait(WAIT_FOR_DELETE_ARTEFACT_BEFORE_SCREEN);

        String actualName = "actual_" + expectedName + ".png";
        String diffName = "diff_" + expectedName + ".png";
        String fullPathNameExpected = SCREENSHOTS_COMPARISON_EXPECTED_PATH + expectedName + ".png";
        String fullPathNameActual = SCREENSHOTS_COMPARISON_ACTUAL_PATH + actualName;
        String fullPathNameDiff = SCREENSHOTS_COMPARISON_DIFF_PATH + diffName;

        Allure.label("testType", "screenshotDiff");

        Screenshot actual = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .takeScreenshot(getWebDriver());
        ImageIO.write(actual.getImage(), "png", new File(fullPathNameActual));

        Screenshot expected = new Screenshot(ImageIO.read(new File(fullPathNameExpected)));
        ImageDiff diff = new ImageDiffer().makeDiff(actual, expected);
        BufferedImage diffImage = diff.getMarkedImage();
        ImageIO.write(diffImage, "png", new File(fullPathNameDiff));

        attachScreenToAllureReport(fullPathNameDiff,"diff");
        attachScreenToAllureReport(fullPathNameActual,"actual");
        attachScreenToAllureReport(fullPathNameExpected,"expected");

        double diffPixelPercentRatio = imagePixelSize * (diffPercent / 100);

        System.out.println("Общее количество различающихся пикселей " + diff.getDiffSize() +
                "\nДопустимое количество различающихся пикселей " + diffPixelPercentRatio);

        Assertions.assertFalse(diff.getDiffSize() > diffPixelPercentRatio,
                "Скриншоты различаются в более чем " + diffPercent + "%");

    }

    public static void makeImageDiff(String expectedName, double diffPercent, int imagePixelSize,
                                     Set<By> setIgnoredElements) throws IOException {

        String actualName = "actual_" + expectedName + ".png";
        String diffName = "diff_" + expectedName + ".png";
        String fullPathNameExpected = SCREENSHOTS_COMPARISON_EXPECTED_PATH + expectedName + ".png";
        String fullPathNameActual = SCREENSHOTS_COMPARISON_ACTUAL_PATH + actualName;
        String fullPathNameDiff = SCREENSHOTS_COMPARISON_DIFF_PATH + diffName;

        // активируем плагин аллюра для дифф
        Allure.label("testType", "screenshotDiff");

        // делаем скриншот
        Screenshot actual = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .ignoredElements(setIgnoredElements)
                .takeScreenshot(getWebDriver());
        ImageIO.write(actual.getImage(), "png", new File(fullPathNameActual));

        // сравниваем его с примером и сохраняем дифф
        Screenshot expected = new Screenshot(ImageIO.read(new File(fullPathNameExpected)));
        ImageDiff diff = new ImageDiffer().makeDiff(actual, expected);
        BufferedImage diffImage = diff.getMarkedImage();
        ImageIO.write(diffImage, "png", new File(fullPathNameDiff));

        // прикрепляем дифф в аллюр отчёт
        BufferedImage bImage1 = ImageIO.read(new File(fullPathNameDiff));
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        ImageIO.write(bImage1, "png", bos1);
        byte[] data1 = bos1.toByteArray();
        Allure.addAttachment("diff", new ByteArrayInputStream(data1));

        // прикрепляем фактический скриншот в аллюр отчёт
        BufferedImage bImage2 = ImageIO.read(new File(fullPathNameActual));
        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
        ImageIO.write(bImage2, "png", bos2);
        byte[] data2 = bos2.toByteArray();
        Allure.addAttachment("actual", new ByteArrayInputStream(data2));

        // прикрепляем скриншот-пример в аллюр отчёт
        BufferedImage bImage3 = ImageIO.read(new File(fullPathNameExpected));
        ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
        ImageIO.write(bImage3, "png", bos3);
        byte[] data3 = bos3.toByteArray();
        Allure.addAttachment("expected", new ByteArrayInputStream(data3));

        double diffPixelPercentRatio = imagePixelSize * (diffPercent / 100);

        System.out.println("Общее количество различающихся пикселей " + diff.getDiffSize() +
                "\nДопустимое количество различающихся пикселей " + diffPixelPercentRatio);

        // фейлим тест, если при сравнении разница в пикселях больше допустимой
        Assertions.assertFalse(diff.getDiffSize() > diffPixelPercentRatio,
                "Скриншоты различаются в более чем " + diffPercent + "%");

    }

    public static void isScreenOrDiff(boolean type,String expectedName,double diffPercent,
                                      int imagePixelSize)
            throws IOException {

        if (type) {

            makeOriginalScreenshot(expectedName);

        } else {

            makeImageDiff(expectedName,diffPercent, imagePixelSize);

        }

    }

    public static void isScreenOrDiff(boolean type,String expectedName,double diffPercent,
                                      int imagePixelSize,Set<By> setIgnoredElements)
            throws IOException {

        if (type) {
            System.out.println("SCREEN");
            makeOriginalScreenshot(expectedName,setIgnoredElements);

        } else {
            System.out.println("DIFF IMAGE");
            makeImageDiff(expectedName,diffPercent, imagePixelSize,setIgnoredElements);

        }

    }

}