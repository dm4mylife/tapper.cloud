package tapper.tests.personal_account.admin.integrations;

import admin_personal_account.integrations.Integrations;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;


@Epic("Личный кабинет администратора ресторана")
@Feature("Интеграции")
@DisplayName("Проверка что все элементы отображаются корректно")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Integrations integrations = new Integrations();
    RootPage rootPage = new RootPage();


    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу интеграций, проверка всех элементов")
    void goToIntegrationCategory() {

        integrations.goToIntegrationsCategory();
        integrations.isIntegrationsCategoryCorrect();

    }

}
