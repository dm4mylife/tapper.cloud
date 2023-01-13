package tapper_admin_personal_account.tables_and_qr_codes;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import common.BaseActions;
import constants.selectors.TapperTableSelectors;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static constants.selectors.AdminPersonalAccountSelectors.Common.pageHeading;
import static constants.selectors.AdminPersonalAccountSelectors.Common.tablesAndQrCodesCategory;
import static constants.selectors.AdminPersonalAccountSelectors.TableAndQrCodes.*;


public class TablesAndQrCodes extends BaseActions {

    @Step("Переход в меню столики и qr-коды")
    public void goToTablesAndQrCodesCategory() {

        click(tablesAndQrCodesCategory);
        tableListHeading.shouldHave(text("Список столиков"), Duration.ofSeconds(5));
        tablesAndQrCodesContainer.shouldBe(visible);

    }

    @Step("Поиск столов с начального и до конечного значения")
    public void searchTableRange(int min, int max) {

        forceWait(1000); // toDO не успевает значение проставиться, слишком быстро тест ходит

        if (min != 0) {

            tableSearchFrom.setValue(String.valueOf(min));

        } else {

            tableSearchFrom.setValue("");

        }

        if (max != 0) {

            tableSearchTo.setValue(String.valueOf(max));

        } else {

            tableSearchTo.setValue("");
            max = 300;

        }

        findTableButton.click();
        resetTableButton.shouldBe(Condition.visible);

        String logs = "";

        for (SelenideElement paginationPage : paginationPages) {

            if (paginationPages.size() != 1) {

                paginationPage.click();
                forceWait(1000);

            }

            for (SelenideElement element : tableListItem) {

                int tableNumber = Integer.parseInt(element.getText().replaceAll(".*\\s(\\d+)\\s.*", "$1"));

                if (tableNumber >= min && tableNumber <= max) {

                    logs += "Стол номер: " + tableNumber + "\n";

                }

            }

        }

        resetTableSearch();

        System.out.println(logs);

    }

    @Step("Проверка что все элементы на странице отображаются корректно")
    public void isTableAndQrCodesCorrect() {

        pageHeading.shouldBe(visible).shouldHave(text("Столики и QR-коды"));
        isElementVisible(tableHeading);
        isElementVisible(tableListHeading);
        isElementVisible(tableSearchFrom);
        isElementVisible(tableSearchTo);
        isElementVisible(findTableButton);

    }

    @Step("Сброс фильтра поиска столов")
    public void resetTableSearch() {

        resetTableButton.shouldHave(cssValue("display", "flex"));
        click(resetTableButton);
        resetTableButton.shouldHave(cssValue("display", "none"));
        tableSearchFrom.shouldHave(value(""));
        tableSearchTo.shouldHave(value(""));
        tableListItem.shouldHave(CollectionCondition.size(10));

    }

    @Step("Переход в карточку стола")
    public void goToDetailTableCard() {

        tableListItem.get(0).click();

    }

    @Step("Проверка что установленный фильтр поиска столово сохранился после перехода в детальную карточку")
    public void isFilterSaved() {

        String previousValueSearchFrom = tableSearchFrom.getValue();
        String previousValueSearchTo = tableSearchTo.getValue();
        ElementsCollection previousTableList = tableListItem;

        tableListItem.get(0).click();
        backToTableList.click();

        String currentValueSearchFrom = tableSearchFrom.getValue();
        String currentValueSearchTo = tableSearchTo.getValue();
        ElementsCollection currentTableList = tableListItem;

        Assertions.assertEquals(previousValueSearchFrom, currentValueSearchFrom,
                "Не сохранилось значение 'с' в фильтре поиска");
        Assertions.assertEquals(previousValueSearchTo, currentValueSearchTo,
                "Не сохранилось значение 'по' в фильтре поиска");
        Assertions.assertEquals(previousTableList, currentTableList,
                "Не сохранился результат поиска по фильтрам");
        System.out.println("Все данные сохранились по фильтру и результату после прохода и возврата детальной карточки стола");

    }

    @Step("Проверка что все элементы в детальной карточке корректны")
    public void areTableCardElementCorrect() {

        isElementVisible(backToTableList);
        isElementVisible(tableItem);
        isElementVisible(qrBlockWhite);
        isElementVisible(qrBlockBlack);

    }

    @Step("Клик в ссылку стола")
    public void clickInTableLinkQR() {

        tableItemLink.click();
        switchTab(1);

    }

    @Step("Проверка перехода по ссылке стола и соответствие номера стола")
    public void isTableNumberCorrectInAdminAndInTable(String tableUrlInTableItem, String tableNumber) {

        isTextContainsInURL(tableUrlInTableItem);

        TapperTableSelectors.RootPage.DishList.tableNumber.shouldBe(visible,Duration.ofSeconds(10));

        String tapperTableNumber = TapperTableSelectors.RootPage.DishList.tableNumber.getText();
        System.out.println(tapperTableNumber + " tapperTableNumber");

        Assertions.assertEquals(tableNumber, tapperTableNumber,
                "Номера столов не совпадают в админке и в таппере");

    }

    @Step("Считываем qr-код")
    public void readQrCode(String src, String tableUrlInTableItem) throws NotFoundException, IOException {

        isImageCorrect(qrWhiteImage);
        isImageCorrect(qrBlackImage);

        URL urlOfImage = new URL(src);
        BufferedImage bufferedImage = ImageIO.read(urlOfImage);

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = new MultiFormatReader().decode(bitmap);
        String textPresentInImage = result.getText();
        System.out.println("Ссылка на QR-код : " + textPresentInImage);

        Assertions.assertEquals(textPresentInImage, tableUrlInTableItem,
                "В QR формируется не корректная ссылка на стол");
        System.out.println("В QR формируется корректная ссылка на стол\n");

    }

    @Step("Проверка что qr-код скачивается")
    public void isDownloadQrCorrect() throws FileNotFoundException {

        File qrWhite = qrDownloadImageWhite.download();

        Assertions.assertNotNull(qrWhite, "Файл не может быть скачен");
        System.out.println("Файл успешно скачался (белая версия)");

        File qrBlack = qrDownloadImageBlack.download();

        Assertions.assertNotNull(qrBlack, "Файл не может быть скачен");
        System.out.println("Файл успешно скачался (черная версия)");

    }

    @Step("Проверка qr-кода после того как мы его скачали")
    public void isDownloadedQrCorrect(String downloadFolder, String tableName, String tableUrl) throws IOException {

        File root = new File(downloadFolder);

        try {

            Collection files = FileUtils.listFiles(root, null, true);

            for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                File file = (File) iterator.next();

                if (file.getName().equals(tableName)) {

                    System.out.println("Файл найден " + file.getAbsolutePath());
                    File imageFile = new File(file.getAbsolutePath());

                    String decodedText;

                    BufferedImage bufferedImage = ImageIO.read(imageFile);

                    LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    Result result = new MultiFormatReader().decode(bitmap);
                    decodedText = result.getText();

                    Assertions.assertEquals(decodedText, tableUrl,
                            "Скаченный qr код не содержит корректную ссылку на стол");
                    System.out.println("Скаченный qr код (" + tableName + ") \n" + decodedText + "\nсодержит корректную ссылку на стол \n" + tableUrl);

                }

            }

        } catch (Exception e) {

            e.printStackTrace();
            Assertions.fail();

        }

    }

}
