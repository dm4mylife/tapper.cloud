package tapper.tests.waiter_personal_account;


import admin_personal_account.AdminAccount;
import admin_personal_account.waiters.Waiters;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_table.YandexPage;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;
import waiter_personal_account.Waiter;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.Yandex.TEST_YANDEX2_LOGIN_EMAIL;
import static data.Constants.TestData.Yandex.TEST_YANDEX2_PASSWORD_MAIL;
import static data.selectors.AdminPersonalAccount.Waiters.backToPreviousPage;

@Order(141)
@Epic("Личный кабинет официант ресторана")
@Feature("Отвязка и привязка карты официанта")
@DisplayName("Отвязка и привязка карты официанта")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _14_1_UnlinkLinkWaiterCardTest extends AdminBaseTest {

    static String password;
    AdminAccount adminAccount = new AdminAccount();
    YandexPage yandexPage = new YandexPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiter waiter = new Waiter();
    Waiters waiters = new Waiters();

    @Test
    @DisplayName("1.0. Авторизуемся под админом")
    public void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        waiters.goToWaiterCategory();

    }

    @Test
    @DisplayName("1.1. Отправляем приглашение")
    public void sendInviteWaiter() {


        waiters.sendInviteToWaiterEmail(IRONHIDE_WAITER, TEST_YANDEX2_LOGIN_EMAIL);

    }

    @Test
    @DisplayName("1.2. Проверка смены статуса верификации")
    public void checkWaiterStatus() {

        backToPreviousPage.click();
        waiters.isWaiterStatusCorrectInPreviewAndCard(IRONHIDE_WAITER, "Приглаш(е|ё)н в систему");
        adminAccount.logOut();

    }

    @Test
    @DisplayName("1.3. Авторизация в почте яндекса")
    public void yandexAuthorization() {

        yandexPage.yandexAuthorization(TEST_YANDEX2_LOGIN_EMAIL, TEST_YANDEX2_PASSWORD_MAIL);

    }

    @Test
    @DisplayName("1.4. Отправка приглашение на почту официанту")
    public void checkInvitationMail() {

        password = yandexPage.checkTapperMail();
        yandexPage.goToAuthPageFromMail();

    }

    @Test
    @DisplayName("1.5. Переход на авторизацию из письма в приглашении с присланными данными и авторизация")
    public void goToAuthTapperPage() {

        authorizationPage.authorizeUser(TEST_YANDEX2_LOGIN_EMAIL,password);
        adminAccount.isRegistrationComplete();

    }

    @Test
    @DisplayName("1.6. Привязка кредитной карты в профиле официанта")
    public void linkWaiterCard() {

        waiter.linkWaiterCard();
        adminAccount.logOut();

    }

    @Test
    @DisplayName("1.7. Отвязываем почту официанта админом ресторана")
    public void unlinkMailWaiter() {

        waiters.unlinkMailWaiter(ADMIN_RESTAURANT_LOGIN_EMAIL,ADMIN_RESTAURANT_PASSWORD,IRONHIDE_WAITER);

    }

    @Test
    @DisplayName("1.8. Удаляем письмо на почте Яндекса")
    public void deleteYandexInviteMail() {

        yandexPage.deleteMail(TEST_YANDEX2_LOGIN_EMAIL, TEST_YANDEX2_PASSWORD_MAIL);

    }

}
