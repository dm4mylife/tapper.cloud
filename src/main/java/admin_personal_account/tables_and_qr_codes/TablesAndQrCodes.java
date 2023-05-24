package admin_personal_account.tables_and_qr_codes;

import com.codeborne.selenide.SelenideElement;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import common.BaseActions;
import data.selectors.TapperTable;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import tapper_table.RootPage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Collection;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.files.FileFilters.withExtension;
import static data.Constants.WAIT_FOR_FILE_TO_BE_DOWNLOADED;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.Common.tablesAndQrCodesCategory;
import static data.selectors.AdminPersonalAccount.Profile.pagePreloader;
import static data.selectors.AdminPersonalAccount.TableAndQrCodes.*;


public class TablesAndQrCodes extends BaseActions {

    RootPage rootPage = new RootPage();

    @Step("Переход в меню столики и qr-коды")
    public void goToTablesAndQrCodesCategory() {

        click(tablesAndQrCodesCategory);
        tableListHeading.shouldHave(text("Список столиков"), Duration.ofSeconds(5));
        tablesAndQrCodesContainer.shouldBe(visible);

    }

    public void searchTableRangeWithoutReset(int min, int max) {

        if (min != 0) {

            tableSearchFrom.setValue(String.valueOf(min));

        } else {

            tableSearchFrom.setValue("");

        }

        if (max != 0) {

            tableSearchTo.setValue(String.valueOf(max));

        } else {

            tableSearchTo.setValue("");

        }

        click(findTableButton);
        resetTableButton.shouldBe(visible);

        if (paginationPages.size() == 1) {

            click(paginationPages.first());
            pagePreloader.shouldBe(visible,Duration.ofSeconds(2));

            if(!Objects.equals(tableSearchFrom.getValue(), ""))
                tableListItem.filter(matchText(String.valueOf(min))).shouldHave(sizeGreaterThanOrEqual(1));

            tableListItem.filter(matchText(String.valueOf(max))).shouldHave(sizeGreaterThanOrEqual(1));

        } else {

            click(paginationPages.first());
            pagePreloader.shouldBe(visible,Duration.ofSeconds(2));
            if(!Objects.equals(tableSearchFrom.getValue(), ""))
                tableListItem.filter(matchText(String.valueOf(min))).shouldHave(sizeGreaterThanOrEqual(1));

            click(paginationPages.last());
            pagePreloader.shouldBe(visible,Duration.ofSeconds(2));
            tableListItem.filter(matchText(String.valueOf(max))).shouldHave(sizeGreaterThanOrEqual(1));

        }

    }

    @Step("Проверка что после обновления страницы, мы остались на этой вкладке")
    public void isCorrectAfterRefresh() {

        rootPage.refreshPage();
        isTableAndQrCodesCorrect();

    }

    @Step("Поиск столов с начального и до конечного значения")
    public void searchTableRange(int min, int max) {

        if (min != 0) {

            tableSearchFrom.setValue(String.valueOf(min));

        } else {

            tableSearchFrom.setValue("");

        }

        if (max != 0) {

            tableSearchTo.setValue(String.valueOf(max));

        } else {

            tableSearchTo.setValue("");

        }

        click(findTableButton);
        tableAndQRCodesPreloader.shouldHave(cssValue("display","none"),Duration.ofSeconds(10));
        resetTableButton.shouldBe(visible);

        if (paginationPages.size() == 1) {

            click(paginationPages.first());

            if(!Objects.equals(tableSearchFrom.getValue(), ""))
                tableListItem.filter(matchText(String.valueOf(min)))
                        .shouldHave(sizeGreaterThanOrEqual(1),Duration.ofSeconds(5));

            tableListItem.filter(matchText(String.valueOf(max)))
                    .shouldHave(sizeGreaterThanOrEqual(1),Duration.ofSeconds(5));

        } else {

            click(paginationPages.first());

            if(!Objects.equals(tableSearchFrom.getValue(), ""))
                tableListItem.filter(matchText(String.valueOf(min)))
                        .shouldHave(sizeGreaterThanOrEqual(1),Duration.ofSeconds(5));

            click(paginationPages.last());

            tableListItem.filter(matchText(String.valueOf(max)))
                    .shouldHave(sizeGreaterThanOrEqual(1),Duration.ofSeconds(5));

        }

        resetTableSearch();

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
        tableListItem.shouldHave(size(10));

    }

    @Step("Переход в карточку стола")
    public void goToDetailTableCard() {

        click(tableListItem.first());
        pagePreloader.shouldBe(hidden);

    }

    @Step("Проверка что установленный фильтр поиска столово сохранился после перехода в детальную карточку")
    public void isFilterSaved() {

        String previousValueSearchFrom = tableSearchFrom.getValue();
        String previousValueSearchTo = tableSearchTo.getValue();

        click(tableListItem.first());
        click(backToTableList);
        isElementsCollectionVisible(tableListItem);

        String currentValueSearchFrom = tableSearchFrom.getValue();
        String currentValueSearchTo = tableSearchTo.getValue();

        Assertions.assertEquals(previousValueSearchFrom, currentValueSearchFrom,
                "Не сохранилось значение 'с' в фильтре поиска");
        Assertions.assertEquals(previousValueSearchTo, currentValueSearchTo,
                "Не сохранилось значение 'по' в фильтре поиска");
        Assertions.assertEquals(tableListItem, tableListItem,
                "Не сохранился результат поиска по фильтрам");

    }

    @Step("Проверка что все элементы в детальной карточке корректны")
    public void areTableCardElementCorrect() {

        isElementVisible(backToTableList);
        isElementVisible(tableItem);

        isElementVisible(tableItem);
        isElementVisible(qrBlockBlack);
        isElementVisible(qrBlockWhite);

    }

    @Step("Клик в ссылку стола")
    public void clickInTableLinkQR() {

        click(tableItemLink);
        switchTab(1);

    }

    @Step("Проверка перехода по ссылке стола и соответствие номера стола")
    public void isTableNumberCorrectInAdminAndInTable(String tableUrlInTableItem, String tableNumber) {

        isTextContainsInURL(tableUrlInTableItem);

        rootPage.skipStartScreenLogo();
        isElementVisible(TapperTable.RootPage.DishList.tableNumber);

        System.out.println(TapperTable.RootPage.DishList.tableNumber.getText());

        String tapperTableNumber = TapperTable.RootPage.DishList.tableNumber.getText()
                .replaceAll("\\D+", "");;

        Assertions.assertEquals(tableNumber, tapperTableNumber,
                "Номера столов не совпадают в админке и в таппере");

    }

    @Step("Считываем qr-код")
    public void readQrCode(String src, String tableUrlInTableItem) throws
            NotFoundException, IOException, URISyntaxException {

        URI urlOfImage = new URI(src);

        BufferedImage bufferedImage = ImageIO.read(urlOfImage.toURL());

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = new MultiFormatReader().decode(bitmap);
        String textPresentInImage = result.getText();

        Assertions.assertEquals(textPresentInImage, tableUrlInTableItem,
                "В QR формируется не корректная ссылка на стол");

    }

    @Step("Проверка что qr-код скачивается")
    public void isDownloadQrCorrect(SelenideElement element) throws FileNotFoundException {

        element.shouldBe(interactable);

        File file = element.download(WAIT_FOR_FILE_TO_BE_DOWNLOADED,withExtension("png"));

        Assertions.assertNotNull(file, "Файл не может быть скачен");

    }

    @Step("Проверка qr-кода после того как мы его скачали")
    public void isDownloadedQrCorrect(String type,String downloadFolder, String tableName, String tableUrl) {

        File root = new File(downloadFolder);

        boolean hasMatch = false;

        try {

            Collection<File> files = FileUtils.listFiles(root, null, true);
            Assertions.assertTrue(files.size() != 0,"Скаченный файл не удалось скачать");

            for (File o : files) {

                if (o.getName().equals(tableName)) {

                    hasMatch = true;

                    File imageFile = new File(o.getAbsolutePath());
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);

                    BinaryBitmap bitmap = type.equals("white") ?
                            new BinaryBitmap(new HybridBinarizer(source)) :
                            new BinaryBitmap(new HybridBinarizer(source.invert()));

                    Result result = new MultiFormatReader().decode(bitmap);
                    String decodedText = result.getText();

                    Assertions.assertEquals(decodedText, tableUrl,
                            "Скаченный qr код не содержит корректную ссылку на стол");

                    Assertions.assertTrue(imageFile.delete(), "Файл не удалился после проверки");

                }

            }

            if (!hasMatch) {

                Assertions.fail("Скаченный файл не найден");

            }

        } catch (Exception e) {

            e.printStackTrace();
            Assertions.fail();

        }

    }

}
