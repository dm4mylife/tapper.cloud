package tapper.tests.support_personal_account.logs_and_permissions;

import admin_personal_account.tables_and_qr_codes.TablesAndQrCodes;
import com.codeborne.selenide.Configuration;
import com.google.zxing.NotFoundException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import support_personal_account.logs_and_permissions.LogsAndPermissions;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.Constants.downloadFolderPath;
import static data.Constants.downloadFolderPathAdminSupport;
import static data.selectors.AdminPersonalAccount.TableAndQrCodes.*;



@Epic("Личный кабинет техподдержки")
@Feature("Логи/доступы")
@Story("Проверка таба Столы")
@DisplayName("Проверка таба Столы")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class TablesTabTest extends PersonalAccountTest {

    static int fromTableSearchValue;
    static int toTableSearchValue;
    static String tableUrlInTableItem;
    static String downloadedFileName;
    static String tableNumberInAdmin;
    AuthorizationPage authorizationPage = new AuthorizationPage();
    LogsAndPermissions logsAndPermissions = new LogsAndPermissions();
    TablesAndQrCodes tablesAndQrCodes = new TablesAndQrCodes();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.downloadsFolder = downloadFolderPathAdminSupport;

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу логов, проверка элементов на странице")
    public void goToLogsAndPermissions() {

        logsAndPermissions.goToLogsAndPermissionsCategory();

    }

    @Test
    @DisplayName("1.3. Выбор тестового ресторана")
    public void chooseRestaurant() {

        logsAndPermissions.chooseRestaurant(KEEPER_RESTAURANT_NAME);

    }

    @Test
    @DisplayName("1.4. Переход в таб, проверка всех форм, проверка поиска, сброса фильтра")
    public void sendInviteWaiter() {

        logsAndPermissions.isTablesTabCorrect();

    }

    @Test
    @DisplayName("1.5. Ввод диапазона с 1 по 5")
    void searchTable() {

        logsAndPermissions.searchTable(fromTableSearchValue = 1,toTableSearchValue = 5);

    }

    @Test
    @DisplayName("1.6. Ввод диапазона с 5 и пустого 'по' ")
    void searchTestSecond() {

        logsAndPermissions.searchTable(fromTableSearchValue = 5,toTableSearchValue = 0);

    }

    @Test
    @DisplayName("1.7. Ввод диапазона пустого 'c' и до 5")
    void searchTestThird() {

        logsAndPermissions.searchTable(fromTableSearchValue = 0,toTableSearchValue = 5);

    }

    @Test
    @DisplayName("1.8. Ввод диапазона с одинаковым значением с 5 и до 5")
    void searchTestFourth() {

        logsAndPermissions.searchTable(fromTableSearchValue = 5,toTableSearchValue = 5);

    }

    @Test
    @DisplayName("1.9. Проверка что фильтр установленный сохранился если мы проходим в карточку стола")
    public void isTableFilterSavedAfterGoingInDetailCard() {

        tablesAndQrCodes.isFilterSaved();

    }

    @Test
    @DisplayName("2.0. Переход в детальную карточку стола и проверка элементов")
    public void goToDetailCard() {

        tablesAndQrCodes.goToDetailTableCard();
        tablesAndQrCodes.areTableCardElementCorrect();

    }

    @Test
    @DisplayName("2.1. Сохраняем данные по столам")
    public void prepareTableData() {

        tableUrlInTableItem = tableItemLink.getAttribute("href");
        tableNumberInAdmin = tableItem.getText().trim().replaceAll(".*Стол (\\d+)\\n.*", "$1");
        downloadedFileName = "table" + tableNumberInAdmin + ".png";

    }

    @Test
    @DisplayName("2.2. Переход на стол по ссылке из админке")
    public void goToTapperTable() {

        tablesAndQrCodes.clickInTableLinkQR();

    }

    @Test
    @DisplayName("2.3. Проверка что ссылка на стол в админке ведёт на стол и номер стола совпадает с админкой")
    public void checkIsTableNumberCorrectInTable() {

        tablesAndQrCodes.isTableNumberCorrectInAdminAndInTable(tableUrlInTableItem, tableNumberInAdmin);

    }

    @Test
    @DisplayName("2.4. Чтение qr-кода")
    public void readQrCode() {

        tablesAndQrCodes.switchTab(0);

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
    @DisplayName("2.5. Проверка скачивания qr-кода")
    public void isDownloadQrCorrect() throws IOException {

        tablesAndQrCodes.isDownloadQrCorrect(qrDownloadImageWhite);
        tablesAndQrCodes.isDownloadedQrCorrect
                ("white",downloadFolderPathAdminSupport, downloadedFileName,tableUrlInTableItem);

    }

    @Test
    @DisplayName("2.6. Проверка что скаченный код корректен")
    public void isDownloadedQrCorrect() throws IOException {

        tablesAndQrCodes.isDownloadQrCorrect(qrDownloadImageBlack);
        tablesAndQrCodes.isDownloadedQrCorrect
                ("black",downloadFolderPathAdminSupport, downloadedFileName,tableUrlInTableItem);
    }

    @Test
    @DisplayName("2.7. Очищение окружения")
    public void deleteDirectory() throws IOException {


        Path path = Path.of(downloadFolderPathAdminSupport);
        File directory = new File(downloadFolderPathAdminSupport);

        if (directory.isDirectory() && Objects.requireNonNull(directory.list()).length == 0) {
            FileUtils.deleteDirectory(directory);
        }

        if (Files.exists(path)) {

            FileUtils.deleteDirectory(directory);

        }

    }

}
