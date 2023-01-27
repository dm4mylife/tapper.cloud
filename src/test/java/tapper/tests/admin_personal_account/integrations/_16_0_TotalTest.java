package tapper.tests.admin_personal_account.integrations;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import total_personal_account_actions.AuthorizationPage;
import admin_personal_account.integrations.Integrations;
import tests.BaseTest;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;

@Order(160)
@Epic("Личный кабинет администратора ресторана")
@Feature("Интеграции")
@DisplayName("Проверка что все элементы отображаются корректно")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _16_0_TotalTest extends BaseTest {


    AuthorizationPage authorizationPage = new AuthorizationPage();
    Integrations integrations = new Integrations();


    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу интеграций, проверка всех элементов")
    public void goToIntegrationCategory() {

        integrations.goToIntegrationsCategory();
        integrations.isIntegrationsCategoryCorrect();

    }

}
