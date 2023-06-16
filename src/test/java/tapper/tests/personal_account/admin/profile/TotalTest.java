package tapper.tests.personal_account.admin.profile;

import admin_personal_account.profile.Profile;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.*;


@Epic("Личный кабинет администратора ресторана")
@Feature("Профиль")
@DisplayName("Общие")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Profile profile = new Profile();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу профиля, проверка всех элементов")
    void goToMenu() {

        profile.goToProfileCategory();
        profile.isProfileCategoryCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Проверяем редактирование названия заведения, имя, телефон")
    void isPrivateDateChangedCorrect() {

        profile.isPrivateDateChangedCorrect();

    }

    @Test
    @Order(4)
    @DisplayName("Добавление логина телеграмма и добавление")
    void addTelegramLogin() {

        profile.addTelegramLogin();

    }

    @Test
    @Order(5)
    @DisplayName("Проверяем функционал управляющего")
    void isMasterCorrect() {

        profile.isMasterCorrect(TELEGRAM_AUTO_LOGIN);

    }

    @Test
    @Order(6)
    @DisplayName("Удаление логина телеграмма и добавление")
    void deleteTelegramLogin() {

        profile.deleteTelegramLogin();

    }

    @Test
    @Order(7)
    @DisplayName("Проверка на максимальное количество логинов телеграма")
    void isMaxTelegramLoginSize() {

        profile.isMaxTelegramLoginSize();

    }

    @Test
    @Order(8)
    @DisplayName("Смена пароля админа")
    void changeAdminPassword() {

        profile.changeAdminPassword();

    }

}
