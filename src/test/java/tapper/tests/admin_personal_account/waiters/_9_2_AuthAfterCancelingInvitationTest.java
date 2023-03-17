package tapper.tests.admin_personal_account.waiters;


import admin_personal_account.AdminAccount;
import admin_personal_account.waiters.Waiters;
import com.codeborne.selenide.Configuration;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.YandexPage;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.Yandex.TEST_YANDEX_LOGIN_EMAIL;
import static data.Constants.TestData.Yandex.TEST_YANDEX_PASSWORD_MAIL;
import static data.selectors.AdminPersonalAccount.Waiters.backToPreviousPage;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.errorMsgLoginOrPassword;


@Order(92)
@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@Story("Авторизация официанта, когда у него отозвали приглашение")
@DisplayName("Авторизация официанта, когда у него отозвали приглашение")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _9_2_AuthAfterCancelingInvitationTest extends AdminBaseTest {

    static String password;

    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    YandexPage yandexPage = new YandexPage();
    Waiters waiters = new Waiters();
    BaseActions baseActions = new BaseActions();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Отправка приглашение на почту официанту")
    public void sendInviteWaiter() {

        waiters.goToWaiterCategory();
        waiters.sendInviteToWaiterEmail(OPTIMUS_PRIME_WAITER, TEST_YANDEX_LOGIN_EMAIL);

    }

    @Test
    @DisplayName("1.3. Отменяем приглашение")
    public void cancelInvitationByAdmin() {

        waiters.cancelEMailWaiterInvitationInCard();
        backToPreviousPage.click();

    }

    @Test
    @DisplayName("1.4. Проверка смены статуса верификации")
    public void checkWaiterStatus() {

        waiters.isWaiterStatusCorrectInPreviewAndCard(OPTIMUS_PRIME_WAITER, "Ожидает приглашения");
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
    @DisplayName("1.7. Переход на авторизацию из письма в приглашении с присланными данными и авторизация. Проверка ошибки авторизации")
    public void goToAuthTapperPage() {

        yandexPage.goToAuthPageFromMail();

        authorizationPage.authorizeUser(TEST_YANDEX_LOGIN_EMAIL,password);

        errorMsgLoginOrPassword.shouldHave(text("Неверный E-mail или пароль "), Duration.ofSeconds(2));
        System.out.println("Авторизоваться не удалось, ошибка ввода данных");

    }

    @Test
    @DisplayName("1.8. Удаляем письмо на почте Яндекса")
    public void deleteYandexInviteMail() {

        yandexPage.deleteMail(TEST_YANDEX_LOGIN_EMAIL, TEST_YANDEX_PASSWORD_MAIL);

    }

}
