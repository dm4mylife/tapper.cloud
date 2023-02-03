package tapper.tests.admin_personal_account.tables_and_qr_codes;

import com.codeborne.selenide.Configuration;
import com.google.zxing.NotFoundException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import total_personal_account_actions.AuthorizationPage;
import admin_personal_account.tables_and_qr_codes.TablesAndQrCodes;
import tests.BaseTest;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.FileDownloadMode.PROXY;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.downloadFolderPath;
import static data.selectors.AdminPersonalAccount.TableAndQrCodes.*;

@Order(100)
@Epic("Личный кабинет администратора ресторана")
@Feature("Столики и QR-коды")
@DisplayName("Проверка поиска по столам, детальной карточки, qr-кода")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _10_0_TotalTest extends BaseTest {

    static int fromTableSearchValue;
    static int toTableSearchValue;
    static String tableUrlInTableItem;
    static String tableNumberWhite;
    static String tableNumberInAdmin;

    TablesAndQrCodes tablesAndQrCodes = new TablesAndQrCodes();
    AuthorizationPage authorizationPage = new AuthorizationPage();

    RootPage rootPage = new RootPage();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";
        Configuration.downloadsFolder = downloadFolderPath;
        Configuration.fileDownload = PROXY;
        Configuration.proxyEnabled = true;

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу столиков и qr-кодов")
    public void goToTablesAndQrCodes() {

        tablesAndQrCodes.goToTablesAndQrCodesCategory();
        tablesAndQrCodes.isTableAndQrCodesCorrect();

    }

    @Test
    @DisplayName("1.3. Ввод диапазона с 1 по 5")
    public void searchTest_1() {

        fromTableSearchValue = 1;
        toTableSearchValue = 5;

        tablesAndQrCodes.searchTableRange(fromTableSearchValue, toTableSearchValue);

    }

    @Test
    @DisplayName("1.4. Ввод диапазона с 5 и пустого 'по' ")
    public void searchTest_2() {

        fromTableSearchValue = 5;
        toTableSearchValue = 0;

        tablesAndQrCodes.searchTableRange(fromTableSearchValue, toTableSearchValue);

    }

    @Test
    @DisplayName("1.5. Ввод диапазона пустого 'c' и до 5")
    public void searchTest_3() {

        fromTableSearchValue = 0;
        toTableSearchValue = 5;

        tablesAndQrCodes.searchTableRange(fromTableSearchValue, toTableSearchValue);

    }

    @Test
    @DisplayName("1.6. Ввод диапазона с одинаковым значением с 5 и до 5")
    public void searchTest_4() {

        fromTableSearchValue = 5;
        toTableSearchValue = 5;

        tablesAndQrCodes.searchTableRange(fromTableSearchValue, toTableSearchValue);

    }

    @Test
    @DisplayName("1.7. Проверка что фильтр установленный сохранился если мы проходим в карточку стола")
    public void isTableFilterSavedAfterGoingInDetailCard() {
        tablesAndQrCodes.isFilterSaved();
    }

    @Test
    @DisplayName("1.8. Переход в детальную карточку стола и проверка элементов")
    public void goToDetailCard() {

        tablesAndQrCodes.goToDetailTableCard();
        tablesAndQrCodes.areTableCardElementCorrect();

    }

    @Test
    @DisplayName("1.9. Сохраняем данные по столам")
    public void prepareTableData() {

        tableUrlInTableItem = tableItemLink.getAttribute("href");
        tableNumberInAdmin = tableItem.getText().trim().replaceAll(".*Стол (\\d+)\\n.*","$1");
        tableNumberWhite = "table" + tableNumberInAdmin + ".png";

        System.out.println(tableNumberWhite);

    }

    @Test
    @DisplayName("2.0. Переход на стол по ссылке из админке")
    public void goToTapperTable() {

        tablesAndQrCodes.clickInTableLinkQR();

    }

    @Test
    @DisplayName("2.1. Проверка что ссылка на стол в админке ведёт на стол и номер стола совпадает с админкой")
    public void checkIsTableNumberCorrectInTable() {

        tablesAndQrCodes.isTableNumberCorrectInAdminAndInTable(tableUrlInTableItem,tableNumberInAdmin);

    }

    @Test
    @DisplayName("2.2 Возврат в меню")
    public void backToMenu() {

        tablesAndQrCodes.switchTab(0);

    }

    @Test
    @DisplayName("2.3. Чтение qr-кода")
    public void readQrCode() {

        String srcWhite = qrBlockWhite.$("img").getAttribute("src");
        String srcBlack = qrBlockBlack.$("img").getAttribute("src");

        try {

            tablesAndQrCodes.readQrCode(srcWhite, tableUrlInTableItem);
            tablesAndQrCodes.readQrCode(srcBlack, tableUrlInTableItem);

        } catch (NotFoundException | IOException e) {

            e.printStackTrace();

        }

    }

    @Test
    @DisplayName("2.4. Проверка скачивания qr-кода")
    public void isDownloadQrCorrect() throws IOException {

        tablesAndQrCodes.isDownloadQrCorrect();

    }

    @Test
    @DisplayName("2.5. Проверка что скаченный код корректен")
    public void isDownloadedQrCorrect() throws IOException {

        tablesAndQrCodes.isDownloadedQrCorrect(downloadFolderPath,tableNumberWhite,tableUrlInTableItem);
        FileUtils.deleteDirectory(new File(downloadFolderPath));

    }

}
