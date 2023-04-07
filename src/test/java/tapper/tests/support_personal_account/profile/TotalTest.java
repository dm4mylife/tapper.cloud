package tapper.tests.support_personal_account.profile;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import support_personal_account.profile.Profile;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;


@Epic("Личный кабинет техподдержки")
@Feature("Профиль")
@DisplayName("Проверка категории Профиль")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Profile profile = new Profile();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу профиля, проверка всех элементов")
    void goToProfileCategory() {

        profile.goToProfileCategory();
        profile.isProfileCategoryCorrect();

    }

}
