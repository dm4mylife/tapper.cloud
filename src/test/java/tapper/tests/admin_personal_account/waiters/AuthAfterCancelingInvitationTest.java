package tapper.tests.admin_personal_account.waiters;


import admin_personal_account.AdminAccount;
import admin_personal_account.waiters.Waiters;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.YandexPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.Yandex.TEST_YANDEX_LOGIN_EMAIL;
import static data.Constants.TestData.Yandex.TEST_YANDEX_PASSWORD_MAIL;
import static data.selectors.AdminPersonalAccount.Waiters.backToPreviousPage;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.errorMsgLoginOrPassword;
import static data.selectors.YandexMail.tapperMail;
import static data.selectors.YandexMail.tapperMailCheckbox;


@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@Story("Авторизация официанта, когда у него отозвали приглашение")
@DisplayName("Авторизация официанта, когда у него отозвали приглашение")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthAfterCancelingInvitationTest extends PersonalAccountTest {

    static String password;

    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    YandexPage yandexPage = new YandexPage();
    Waiters waiters = new Waiters();


    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Отправка приглашение на почту официанту")
    void sendInviteWaiter() {

        waiters.goToWaiterCategory();
        waiters.sendInviteToWaiterEmail(OPTIMUS_PRIME_WAITER, TEST_YANDEX_LOGIN_EMAIL);

    }

    @Test
    @Order(3)
    @DisplayName("Отменяем приглашение")
    void cancelInvitationByAdmin() {

        waiters.cancelEMailWaiterInvitationInCard();
        backToPreviousPage.click();

    }

    @Test
    @Order(4)
    @DisplayName("Проверка смены статуса верификации")
    void checkWaiterStatus() {

        waiters.isWaiterStatusCorrectInPreviewAndCard(OPTIMUS_PRIME_WAITER, "Ожидает приглашения");
        adminAccount.logOut();

    }

    @Test
    @Order(5)
    @DisplayName("Авторизация в почте яндекса")
    void yandexAuthorization() {

        yandexPage.yandexAuthorization(TEST_YANDEX_LOGIN_EMAIL, TEST_YANDEX_PASSWORD_MAIL);

    }

    @Test
    @Order(6)
    @DisplayName("Отправка приглашение на почту официанту")
    void checkInvitationMail() {

        password = yandexPage.checkTapperMail();

    }

    @Test
    @Order(7)
    @DisplayName("Переход на авторизацию из письма в приглашении с присланными данными и авторизация. Проверка ошибки авторизации")
    void goToAuthTapperPage() {

        yandexPage.goToAuthPageFromMail();

        authorizationPage.authorizeUser(TEST_YANDEX_LOGIN_EMAIL,password);

        errorMsgLoginOrPassword.shouldHave(text("Неверный E-mail или пароль "), Duration.ofSeconds(2));

    }

    @Test
    @Order(8)
    @DisplayName("Удаляем письмо на почте Яндекса")
    void deleteYandexInviteMail() {

        yandexPage.deleteMail(TEST_YANDEX_LOGIN_EMAIL, TEST_YANDEX_PASSWORD_MAIL,tapperMailCheckbox,tapperMail);

    }

}
