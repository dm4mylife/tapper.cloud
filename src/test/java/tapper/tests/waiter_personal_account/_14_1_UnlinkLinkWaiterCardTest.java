package tapper.tests.waiter_personal_account;


import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_admin_personal_account.AdminAccount;
import tapper_admin_personal_account.AuthorizationPage;
import tapper_admin_personal_account.waiters.Waiters;
import tapper_table.YandexPage;
import tapper_waiter_personal_account.Waiter;
import tests.BaseTest;

import static constants.Constant.TestData.*;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_RESTAURANT_PASSWORD;
import static constants.selectors.AdminPersonalAccountSelectors.Waiters.backToPreviousPage;

@Order(141)
@Epic("Личный кабинет администратора ресторана")
@Feature("Личный кабинет официанта")
@DisplayName("Отвязка и привязка карты официанта")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _14_1_UnlinkLinkWaiterCardTest extends BaseTest {


    static String password;


    AdminAccount adminAccount = new AdminAccount();
    YandexPage yandexPage = new YandexPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiter waiter = new Waiter();
    Waiters waiters = new Waiters();

    @Test
    @DisplayName("1.0. Авторизуемся под админом")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        waiters.goToWaiterCategory();

    }

    @Test
    @DisplayName("1.1. Отправляем приглашение")
    public void sendInviteWaiter() {

        waiters.sendInviteToWaiterEmail(IRONHIDE_WAITER,TEST_YANDEX2_LOGIN_EMAIL);

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
        yandexPage.yandexAuthorization(TEST_YANDEX2_LOGIN_EMAIL,TEST_YANDEX2_PASSWORD_MAIL);
    }

    @Test
    @DisplayName("1.4. Отправка приглашение на почту официанту")
    public void checkInvitationMail() {
        password = yandexPage.checkTapperMail();
    }

    @Test
    @DisplayName("1.5. Переход на авторизацию из письма в приглашении с присланными данными и авторизация")
    public void goToAuthTapperPage() {

        yandexPage.goToAuthPageFromMail();
        authorizationPage.authorizationUser(TEST_YANDEX2_LOGIN_EMAIL, password);
        adminAccount.isRegistrationComplete();

    }

    @Test
    @DisplayName("1.6. Привязка карты")
    public void checkIfWaiterVerifiedSuccessfully() {

       waiter.linkWaiterCard();

    }

    @Test
    @DisplayName("1.7. Отвязываем почту официанта админом ресторана")
    public void unlinkMailWaiter() {

        adminAccount.logOut();

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        waiters.goToWaiterCategory();

        waiters.isWaiterStatusCorrectInPreviewAndCard(IRONHIDE_WAITER, "Официант верифицирован");
        waiters.unlinkMailWaiter();

    }

    @Test
    @DisplayName("1.8. Удаляем письмо на почте Яндекса")
    public void deleteYandexInviteMail() {
        yandexPage.deleteMail(TEST_YANDEX2_LOGIN_EMAIL,TEST_YANDEX2_PASSWORD_MAIL);
    }

}
