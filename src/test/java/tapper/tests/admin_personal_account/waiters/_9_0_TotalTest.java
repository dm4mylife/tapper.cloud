package tapper.tests.admin_personal_account.waiters;


import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import admin_personal_account.AdminAccount;
import total_personal_account_actions.AuthorizationPage;
import admin_personal_account.waiters.Waiters;
import tapper_table.YandexPage;
import tests.BaseTest;

import static data.Constants.TestData.*;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.Yandex.TEST_YANDEX_LOGIN_EMAIL;
import static data.Constants.TestData.Yandex.TEST_YANDEX_PASSWORD_MAIL;
import static data.selectors.AdminPersonalAccount.Waiters.backToPreviousPage;


@Order(90)
@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@Story("Авторизация администратора,проверка всех статусов приглашения официанта")
@DisplayName("Авторизация администратора,проверка всех статусов приглашения официанта")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _9_0_TotalTest extends BaseTest {

    static String password;

    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    YandexPage yandexPage = new YandexPage();
    Waiters waiters = new Waiters();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";
        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Проверка поиска при некорректном запросе ")
    public void sendInviteWaiter() {

        waiters.goToWaiterCategory();
        waiters.searchWaiterNegative();


    }

    @Test
    @DisplayName("1.3. Отправка приглашение на почту официанту")
    public void negativeSearchWaiter() {

        waiters.sendInviteToWaiterEmail(OPTIMUS_PRIME_WAITER, TEST_YANDEX_LOGIN_EMAIL);

    }

    @Test
    @DisplayName("1.4. Проверка смены статуса верификации")
    public void checkWaiterStatus() {

        backToPreviousPage.click();
        waiters.isWaiterStatusCorrectInPreviewAndCard(OPTIMUS_PRIME_WAITER, "Приглаш(е|ё)н в систему");
        adminAccount.logOut();

    }

    @Test
    @DisplayName("1.5. Авторизация в почте яндекса")
    public void yandexAuthorization() {
        yandexPage.yandexAuthorization(TEST_YANDEX_LOGIN_EMAIL, TEST_YANDEX_PASSWORD_MAIL);
    }

    @Test
    @DisplayName("1.6. Отправка приглашение на почту официанту")
    public void checkInvitationMail() {
        password = yandexPage.checkTapperMail();
    }

    @Test
    @DisplayName("1.7. Переход на авторизацию из письма в приглашении с присланными данными и авторизация")
    public void goToAuthTapperPage() {

        yandexPage.goToAuthPageFromMail();
        authorizationPage.authorizationUser(TEST_YANDEX_LOGIN_EMAIL, password);
        adminAccount.isRegistrationComplete();
        adminAccount.logOut();

    }

    @Test
    @DisplayName("1.8. Проверка что авторизация корректна")
    public void checkIfWaiterVerifiedSuccessfully() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        waiters.goToWaiterCategory();
        waiters.isWaiterStatusCorrectInPreviewAndCard(OPTIMUS_PRIME_WAITER, "Официант верифицирован");
        adminAccount.logOut();

    }

    @Test
    @DisplayName("1.9. Проверка статуса верификации, удаление привязанной почты у официанта")
    public void clearMailWaiter() {
        waiters.unlinkMailWaiter(ADMIN_RESTAURANT_LOGIN_EMAIL,ADMIN_RESTAURANT_PASSWORD,OPTIMUS_PRIME_WAITER);
    }

    @Test
    @DisplayName("2.0. Удаляем письмо на почте Яндекса")
    public void deleteYandexInviteMail() {
        yandexPage.deleteMail(TEST_YANDEX_LOGIN_EMAIL, TEST_YANDEX_PASSWORD_MAIL);
    }

}
