package tapper.tests.support_personal_account.profile;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import support_personal_account.profile.Profile;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;

@Order(130)
@Epic("Личный кабинет техподдержки")
@Feature("Профиль")
@DisplayName("Проверка профиля, редактирование полей, смена пароля")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _13_0_TotalTest extends BaseTest {


    AuthorizationPage authorizationPage = new AuthorizationPage();
    Profile profile = new Profile();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу профиля, проверка всех элементов")
    public void goToMenu() {

        profile.goToProfileCategory();
        profile.isProfileCategoryCorrect();

    }

}
