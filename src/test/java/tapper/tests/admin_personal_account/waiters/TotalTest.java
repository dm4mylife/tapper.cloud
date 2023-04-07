package tapper.tests.admin_personal_account.waiters;


import admin_personal_account.AdminAccount;
import admin_personal_account.waiters.Waiters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.YandexPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.Yandex.TEST_YANDEX_LOGIN_EMAIL;
import static data.Constants.TestData.Yandex.TEST_YANDEX_PASSWORD_MAIL;
import static data.selectors.AdminPersonalAccount.Waiters.backToPreviousPage;
import static data.selectors.YandexMail.tapperMail;
import static data.selectors.YandexMail.tapperMailCheckbox;


@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@Story("Авторизация администратора,проверка всех статусов приглашения официанта")
@DisplayName("Авторизация администратора,проверка всех статусов приглашения официанта")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    static String password;

    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    YandexPage yandexPage = new YandexPage();
    Waiters waiters = new Waiters();

    @Test
    @Order(0)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(1)
    @DisplayName("Проверяем что после обновления страницы мы остаёмся на этом же месте")
    void isCorrectAfterPageRefresh() {

        waiters.goToWaiterCategory();
        waiters.isCorrectAfterPageRefresh();
    }

    @Test
    @Order(2)
    @DisplayName("Проверяем сортировку в поиске")
    void isAlphabeticOrderCorrect() {

        waiters.searchWaiter("Ак");
        waiters.isAlphabeticOrderCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Проверка поиска при некорректном запросе ")
    void sendInviteWaiter() {

        waiters.searchWaiterNegative();

    }

    @Test
    @Order(4)
    @DisplayName("Отправка приглашение на почту официанту")
    void negativeSearchWaiter() {

        waiters.sendInviteToWaiterEmail(OPTIMUS_PRIME_WAITER, TEST_YANDEX_LOGIN_EMAIL);

    }

    @Test
    @Order(5)
    @DisplayName("Проверка смены статуса верификации")
    void checkWaiterStatus() {

        backToPreviousPage.click();
        waiters.isWaiterStatusCorrectInPreviewAndCard(OPTIMUS_PRIME_WAITER, INVITED_IN_SERVICE_TEXT);
        adminAccount.logOut();

    }

    @Test
    @Order(6)
    @DisplayName("Авторизация в почте яндекса")
    void yandexAuthorization() {

        yandexPage.yandexAuthorization(TEST_YANDEX_LOGIN_EMAIL, TEST_YANDEX_PASSWORD_MAIL);

    }

    @Test
    @Order(7)
    @DisplayName("Отправка приглашение на почту официанту")
    void checkInvitationMail() {

        password = yandexPage.checkTapperMail();

    }

    @Test
    @Order(8)
    @DisplayName("Переход на авторизацию из письма в приглашении с присланными данными и авторизация")
    void goToAuthTapperPage() {

        yandexPage.goToAuthPageFromMail();
        authorizationPage.authorizeUser(TEST_YANDEX_LOGIN_EMAIL, password);
        adminAccount.isRegistrationComplete();
        adminAccount.logOut();

    }

    @Test
    @Order(9)
    @DisplayName("Проверка что авторизация корректна")
    void checkIfWaiterVerifiedSuccessfully() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        waiters.goToWaiterCategory();
        waiters.isWaiterStatusCorrectInPreviewAndCard(OPTIMUS_PRIME_WAITER, VERIFIED_WAITER_TEXT);
        adminAccount.logOut();

    }

    @Test
    @Order(10)
    @DisplayName("Проверка статуса верификации, удаление привязанной почты у официанта")
    void clearMailWaiter() {

        waiters.unlinkMailWaiter(ADMIN_RESTAURANT_LOGIN_EMAIL,ADMIN_RESTAURANT_PASSWORD,OPTIMUS_PRIME_WAITER);

    }

    @Test
    @Order(11)
    @DisplayName("Удаляем письмо на почте Яндекса")
    void deleteYandexInviteMail() {

        yandexPage.deleteMail(TEST_YANDEX_LOGIN_EMAIL, TEST_YANDEX_PASSWORD_MAIL,tapperMailCheckbox,tapperMail);

    }

}
