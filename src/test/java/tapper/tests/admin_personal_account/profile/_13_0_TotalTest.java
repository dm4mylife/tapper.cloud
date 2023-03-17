package tapper.tests.admin_personal_account.profile;

import admin_personal_account.profile.Profile;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.AdminBaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.*;

@Order(130)
@Epic("Личный кабинет администратора ресторана")
@Feature("Профиль")
@DisplayName("Проверка профиля, редактирование полей, смена пароля")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _13_0_TotalTest extends AdminBaseTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Profile profile = new Profile();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу профиля, проверка всех элементов")
    public void goToMenu() {

        profile.goToProfileCategory();
        profile.isProfileCategoryCorrect();

    }

    @Test
    @DisplayName("1.3. Проверяем редактирование названия заведения, имя, телефон")
    public void isPrivateDateChangedCorrect() {

        profile.isPrivateDateChangedCorrect();

    }

    @Test
    @DisplayName("1.4. Добавление логина телеграмма и добавление")
    public void addTelegramLogin() {

        profile.addTelegramLogin();

    }

    @Test
    @DisplayName("1.5. Проверяем функционал управляющего")
    public void isMasterCorrect() {

        profile.isMasterCorrect(TELEGRAM_AUTO_LOGIN);

    }

    @Test
    @DisplayName("1.6. Удаление логина телеграмма и добавление")
    public void deleteTelegramLogin() {

        profile.deleteTelegramLogin();

    }

    @Test
    @DisplayName("1.7. Смена пароля админа")
    public void changeAdminPassword() {

        profile.changeAdminPassword();

    }

}
