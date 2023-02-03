package tapper.tests.support_personal_account.cash_desk_inaccessibility;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import support_personal_account.cash_desk_inaccessibility.CashDeskInaccessibility;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import java.io.File;
import java.io.FileNotFoundException;

import static com.codeborne.selenide.FileDownloadMode.PROXY;
import static data.Constants.TIME_WAIT_FOR_FILE_TO_BE_DOWNLOADED;
import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.selectors.SupportPersonalAccount.CashDeskInaccessibility.downloadTableButton;

@Order(210)
@Epic("Личный кабинет техподдержки")
@Feature("Недоступность кассы")
@DisplayName("Проверка категории Недоступность кассы")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _22_0_TotalTest extends BaseTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    CashDeskInaccessibility cashDeskInaccessibility = new CashDeskInaccessibility();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";
        Configuration.fileDownload = PROXY;
        Configuration.proxyEnabled = true;

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на категорию недоступность кассы, проверка всех элементов")
    public void goToCashDeskInaccessibilityCategory() throws FileNotFoundException {

        cashDeskInaccessibility.goToCashDeskInaccessibility();
        cashDeskInaccessibility.isCashDeskInaccessibilityCategoryCorrect();

    }

    @Test
    @DisplayName("1.3. Выбираем определенный ресторан")
    public void choseOnlyCertainRestaurants() throws FileNotFoundException {

        cashDeskInaccessibility.choseOnlyCertainRestaurants(RESTAURANT_NAME);

    }

    @Test
    @DisplayName("1.4. Выбираем дату для выгрузки отчёта")
    public void choseDateAndDownloadTable() {

        cashDeskInaccessibility.choseDate();

    }

    @Test
    @DisplayName("1.5. Скачиваем таблицы")
    public void isDownloadCorrect() throws FileNotFoundException {

        File table = downloadTableButton.download(TIME_WAIT_FOR_FILE_TO_BE_DOWNLOADED);

        Assertions.assertNotNull(table, "Файл не может быть скачен");
        System.out.println("Таблицы успешно скачались");

    }

}
