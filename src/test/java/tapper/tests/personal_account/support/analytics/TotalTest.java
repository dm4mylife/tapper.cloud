package tapper.tests.personal_account.support.analytics;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import support_personal_account.analytics.Analytics;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.io.FileNotFoundException;

import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;


@Epic("Личный кабинет техподдержки")
@Feature("Аналитика")
@DisplayName("Аналитика")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Analytics analytics = new Analytics();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на категорию аналитики, проверка всех элементов")
    void goToCashDeskInaccessibilityCategory() {

       analytics.goToAnalyticsCategory();
       analytics.isAnalyticsCategoryCorrect();

    }


    @Test
    @Order(4)
    @DisplayName("Выбираем дату для выгрузки таблицы")
    void choseDateAndDownloadTable() {

        analytics.choseDate();

    }

    @Test
    @Order(5)
    @DisplayName("Скачиваем таблицы")
    void isDownloadCorrect() throws FileNotFoundException {

        analytics.downloadFile();

    }


}
