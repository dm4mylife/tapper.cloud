package tapper.tests.support_personal_account.profile;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import support_personal_account.profile.Profile;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;

@Order(160)
@Epic("Личный кабинет техподдержки")
@Feature("Профиль")
@DisplayName("Проверка категории Профиль")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _16_0_TotalTest extends AdminBaseTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Profile profile = new Profile();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу профиля, проверка всех элементов")
    public void goToProfileCategory() {

        profile.goToProfileCategory();
        profile.isProfileCategoryCorrect();

    }

}
