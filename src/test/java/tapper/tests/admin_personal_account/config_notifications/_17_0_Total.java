package tapper.tests.admin_personal_account.config_notifications;

import admin_personal_account.config_notifications.ConfigNotifications;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.AdminPersonalAccount.ConfigNotifications.*;

@Order(170)
@Epic("Личный кабинет администратора ресторана")
@Feature("Настройка уведомлений")
@DisplayName("Проверка блока уведомлений, добавление\\удаление группы")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _17_0_Total extends AdminBaseTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    ConfigNotifications configNotifications = new ConfigNotifications();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

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
    @DisplayName("1.4. Добавляем группу")
    public void addTelegramGroup() {

        configNotifications.addTelegramGroup(TELEGRAM_AUTO_CHANNEL_LOGIN);

    }

    @Test
    @DisplayName("1.5. Проверяем удаление группы")
    public void deleteTelegramGroup() {

        configNotifications.deleteTelegramGroup(TELEGRAM_AUTO_CHANNEL_LOGIN);

    }


    @Test
    @DisplayName("1.6. Проверяем форму редактирования группы")
    public void isSettingCorrect() {

        configNotifications.isElementNotificationSettingCorrect();

    }

    @Test
    @DisplayName("1.7. Проверяем все опции выбора типа сообщений")
    public void isTypesCorrect() {

        configNotifications.isTypeOptionCorrect(typeCallWaiterNotificationOption);
        configNotifications.isTypeOptionCorrect(typeRatingNotificationOption);
        configNotifications.isTypeOptionCorrect(typePaymentNotificationOption);

    }

}
