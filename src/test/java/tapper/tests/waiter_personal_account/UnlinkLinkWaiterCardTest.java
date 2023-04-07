package tapper.tests.waiter_personal_account;


import admin_personal_account.AdminAccount;
import admin_personal_account.waiters.Waiters;
import data.Constants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.YandexPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;
import waiter_personal_account.Waiter;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.Yandex.WAITER_PASSWORD_MAIL;
import static data.selectors.AdminPersonalAccount.Waiters.backToPreviousPage;
import static data.selectors.YandexMail.tapperMail;
import static data.selectors.YandexMail.tapperMailCheckbox;


@Epic("Личный кабинет официант ресторана")
@Feature("Отвязка и привязка карты официанта")
@DisplayName("Отвязка и привязка карты официанта")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UnlinkLinkWaiterCardTest extends PersonalAccountTest {

    static String password;
    AdminAccount adminAccount = new AdminAccount();
    YandexPage yandexPage = new YandexPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiter waiter = new Waiter();
    Waiters waiters = new Waiters();
    RootPage rootPage = new RootPage();

    @Test
    @Order(1)
    @DisplayName("Авторизуемся под админом")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        waiters.goToWaiterCategory();

    }

    @Test
    @Order(2)
    @DisplayName("Отправляем приглашение")
    void sendInviteWaiter() {

        waiters.sendInviteToWaiterEmail(IRONHIDE_WAITER, Constants.TestData.Yandex.WAITER_LOGIN_EMAIL);

    }

    @Test
    @Order(3)
    @DisplayName("Проверка смены статуса верификации")
    void checkWaiterStatus() {

        backToPreviousPage.click();
        waiters.isWaiterStatusCorrectInPreviewAndCard(IRONHIDE_WAITER, INVITED_IN_SERVICE_TEXT);
        adminAccount.logOut();

    }

    @Test
    @Order(4)
    @DisplayName("Авторизация в почте яндекса")
    void yandexAuthorization() {

        yandexPage.yandexAuthorization
                (Constants.TestData.Yandex.WAITER_LOGIN_EMAIL, Constants.TestData.Yandex.WAITER_PASSWORD_MAIL);

    }

    @Test
    @Order(5)
    @DisplayName("Отправка приглашение на почту официанту")
    void checkInvitationMail() {

        password = yandexPage.checkTapperMail();
        yandexPage.goToAuthPageFromMail();

    }

    @Test
    @Order(6)
    @DisplayName("Переход на авторизацию из письма в приглашении с присланными данными и авторизация")
    void goToAuthTapperPage() {


        authorizationPage.authorizeUser(Constants.TestData.Yandex.WAITER_LOGIN_EMAIL,password);
        adminAccount.isRegistrationComplete();

    }

    @Test
    @Order(7)
    @DisplayName("Привязка кредитной карты в профиле официанта")
    void linkWaiterCard() {

        waiter.linkWaiterCard();
        adminAccount.logOut();

    }

    @Test
    @Order(8)
    @DisplayName("Отвязываем почту официанта админом ресторана")
    void unlinkMailWaiter() {

        waiters.unlinkMailWaiter(ADMIN_RESTAURANT_LOGIN_EMAIL,ADMIN_RESTAURANT_PASSWORD,IRONHIDE_WAITER);

    }

    @Test
    @Order(9)
    @DisplayName("Удаляем письмо на почте Яндекса")
    void deleteYandexInviteMail() {

        yandexPage.deleteMail
                (Constants.TestData.Yandex.WAITER_LOGIN_EMAIL, Constants.TestData.Yandex.WAITER_PASSWORD_MAIL,
                        tapperMailCheckbox,tapperMail);

    }

}
