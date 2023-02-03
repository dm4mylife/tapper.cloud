package tapper.tests.admin_personal_account.config_notifications;

import com.codeborne.selenide.Configuration;
import data.Constants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import total_personal_account_actions.AuthorizationPage;
import admin_personal_account.config_notifications.ConfigNotifications;
import tests.BaseTest;

import static data.Constants.TestData.AdminPersonalAccount.*;

@Order(160)
@Epic("Личный кабинет администратора ресторана")
@Feature("Настройка уведомлений")
@DisplayName("Проверка блока уведомлений, добавление\\удаление группы")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _16_0_TotalTest extends BaseTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    ConfigNotifications configNotifications = new ConfigNotifications();


    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу телеграм уведомлений, проверка всех элементов")
    public void goToConfigNotificationsCategory() {

        configNotifications.goToConfigNotificationsCategory();
        configNotifications.isConfigNotificationsCategoryCorrect();

    }
    @Test
    @DisplayName("1.3. Проверяем ошибку при вводе логина телеграмма")
    public void isErrorTelegramLoginCorrect() {


        configNotifications.isErrorTelegramLoginCorrect();

    }

    @Test
    @DisplayName("1.4. Удаление и добавление канала")
    public void isChangingRecipientsCorrect() {


        configNotifications.findAutoChannel(TELEGRAM_AUTO_CHANNEL_LOGIN);

    }

}
