package tapper.tests.admin_personal_account.config_notifications;

import admin_personal_account.config_notifications.ConfigNotifications;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.AdminPersonalAccount.ConfigNotifications.*;


@Epic("Личный кабинет администратора ресторана")
@Feature("Настройка уведомлений")
@DisplayName("Проверка блока уведомлений, добавление\\удаление группы")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Total extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    ConfigNotifications configNotifications = new ConfigNotifications();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу телеграм уведомлений, проверка всех элементов")
    void goToConfigNotificationsCategory() {

        configNotifications.goToConfigNotificationsCategory();
        configNotifications.isConfigNotificationsCategoryCorrect();

    }
    @Test
    @Order(3)
    @DisplayName("Проверка что остались на этой вкладке после обноваления страницы")
    void isCorrectAfterPageRefresh() {

        configNotifications.isCorrectAfterPageRefresh();

    }


    @Test
    @Order(4)
    @DisplayName("Проверяем ошибку при вводе логина телеграмма")
    void isErrorTelegramLoginCorrect() {

        configNotifications.isErrorTelegramLoginCorrect();

    }

    @Test
    @Order(5)
    @DisplayName("Добавляем группу")
    void addTelegramGroup() {

        configNotifications.addTelegramGroup(TELEGRAM_AUTO_CHANNEL_LOGIN);

    }

    @Test
    @Order(6)
    @DisplayName("Проверяем удаление группы")
    void deleteTelegramGroup() {

        configNotifications.deleteTelegramGroup(TELEGRAM_AUTO_CHANNEL_LOGIN);

    }

    @Test
    @Order(7)
    @DisplayName("Проверяем форму редактирования группы")
    void isSettingCorrect() {

        configNotifications.isElementNotificationSettingCorrect();

    }

    @Test
    @Order(8)
    @DisplayName("Проверяем все опции выбора типа сообщений")
    void isTypesCorrect() {

        configNotifications.isTypeOptionCorrect(typeCallWaiterNotificationOption);
        configNotifications.isTypeOptionCorrect(typeRatingNotificationOption);
        configNotifications.isTypeOptionCorrect(typePaymentNotificationOption);

    }

}
