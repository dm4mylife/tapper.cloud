package tapper.tests.personal_account.admin.tables_and_qr_codes;

import admin_personal_account.tables_and_qr_codes.TablesAndQrCodes;
import com.google.zxing.NotFoundException;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.downloadFolderPath;
import static data.selectors.AdminPersonalAccount.TableAndQrCodes.*;


@Epic("Личный кабинет администратора ресторана")
@Feature("Столики и QR-коды")
@DisplayName("Проверка поиска по столам, детальной карточки, qr-кода")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    static int fromTableSearchValue;
    static int toTableSearchValue;
    static String tableUrlInTableItem;
    static String downloadedFileName;
    static String tableNumberInAdmin;

    TablesAndQrCodes tablesAndQrCodes = new TablesAndQrCodes();
    AuthorizationPage authorizationPage = new AuthorizationPage();


    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу столиков и qr-кодов")
    void goToTablesAndQrCodes() {

        tablesAndQrCodes.goToTablesAndQrCodesCategory();
        tablesAndQrCodes.isTableAndQrCodesCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Ввод диапазона с 1 по 5")
    void searchTest_1() {

        tablesAndQrCodes.searchTableRange(fromTableSearchValue = 1, toTableSearchValue = 5);

    }

    @Test
    @Order(4)
    @DisplayName("Ввод диапазона с 5 и пустого 'по' ")
    void searchTest_2() {

        tablesAndQrCodes.searchTableRange(fromTableSearchValue = 5, toTableSearchValue = 0);

    }

    @Test
    @Order(5)
    @DisplayName("Ввод диапазона пустого 'c' и до 5")
    void searchTest_3() {

        tablesAndQrCodes.searchTableRange(fromTableSearchValue = 0, toTableSearchValue = 5);

    }

    @Test
    @Order(6)
    @DisplayName("Ввод диапазона с одинаковым значением с 5 и до 5")
    void searchTest_4() {

        tablesAndQrCodes.searchTableRange(fromTableSearchValue = 5, toTableSearchValue = 5);

    }

    @Test
    @Order(7)
    @DisplayName("Проверка что фильтр установленный сохранился если мы проходим в карточку стола")
    void isTableFilterSavedAfterGoingInDetailCard() {

        tablesAndQrCodes.isFilterSaved();

    }

    @Test
    @Order(8)
    @DisplayName("Переход в детальную карточку стола и проверка элементов")
    void goToDetailCard() {

        tablesAndQrCodes.goToDetailTableCard();
        tablesAndQrCodes.areTableCardElementCorrect();

    }

    @Test
    @Order(9)
    @DisplayName("Сохраняем данные по столам")
    void prepareTableData() {

        tableUrlInTableItem = tableItemLink.getAttribute("href");
        tableNumberInAdmin = tableItem.getText().trim().replaceAll(".*Стол (\\d+)\\n.*","$1");
        System.out.println(tableUrlInTableItem);
        downloadedFileName = "table" + tableNumberInAdmin + ".png";

    }

    @Test
    @Order(10)
    @DisplayName("Переход на стол по ссылке из админке")
    void goToTapperTable() {

        tablesAndQrCodes.clickInTableLinkQR();

    }

    @Test
    @Order(11)
    @DisplayName("Проверка что ссылка на стол в админке ведёт на стол и номер стола совпадает с админкой")
    void checkIsTableNumberCorrectInTable() {

        tablesAndQrCodes.isTableNumberCorrectInAdminAndInTable(tableUrlInTableItem,tableNumberInAdmin);

    }

    @Test
    @Order(12)
    @DisplayName("Возврат в меню")
    void backToMenu() {

        tablesAndQrCodes.switchTab(0);

    }

    @Test
    @Order(13)
    @DisplayName("Чтение qr-кода")
    void readQrCode() {

        String srcWhite = qrBlockWhite.$("img").getAttribute("src");
        String srcBlack = qrBlockBlack.$("img").getAttribute("src");

        try {

            tablesAndQrCodes.readQrCode(srcWhite, tableUrlInTableItem);
            tablesAndQrCodes.readQrCode(srcBlack, tableUrlInTableItem);

        } catch (NotFoundException | IOException e) {

            e.printStackTrace();

        } catch (java.net.URISyntaxException e) {

            throw new RuntimeException(e);

        }

    }

    @Test
    @Order(14)
    @DisplayName("Проверка скачивания qr-кода и что сам скаченный код корректный для белого")
    void isDownloadQrCorrectWhite() throws IOException {

        tablesAndQrCodes.isDownloadQrCorrect(qrDownloadImageWhite);
        tablesAndQrCodes.isDownloadedQrCorrect("white",downloadFolderPath, downloadedFileName,tableUrlInTableItem);



    }

    @Test
    @Order(15)
    @DisplayName("Проверка скачивания qr-кода и что сам скаченный код корректный для черного")
    void isDownloadQrCorrectBlack() throws IOException {

        tablesAndQrCodes.isDownloadQrCorrect(qrDownloadImageBlack);
        tablesAndQrCodes.isDownloadedQrCorrect("black",downloadFolderPath, downloadedFileName,tableUrlInTableItem);

    }



    @Test
    @Order(16)
    @DisplayName("Очищение окружения")
    void deleteDirectory() throws IOException {

        Path path = Path.of(downloadFolderPath);
        File directory = new File(downloadFolderPath);

        if (directory.isDirectory() && Objects.requireNonNull(directory.list()).length == 0)
            FileUtils.deleteDirectory(directory);

        if (Files.exists(path))
            FileUtils.deleteDirectory(directory);

    }

    @Test
    @Order(17)
    @DisplayName("Проверяем что после обновления страницы мы остаемся в этом табе")
    void isCorrectAfterRefresh() {

        BaseActions.click(backToTableList);
        tablesAndQrCodes.isCorrectAfterRefresh();

    }





}
