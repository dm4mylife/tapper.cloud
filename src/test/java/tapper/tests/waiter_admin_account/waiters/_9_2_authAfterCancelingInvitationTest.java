package tapper.tests.waiter_admin_account.waiters;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_admin.AdminAccount;
import tapper_admin.AuthorizationPage;
import tapper_admin.waiters.Waiters;
import tapper_table.YandexPage;
import tests.BaseTest;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static constants.Constant.TestData.OPTIMUS_PRIME_WAITER;
import static constants.Constant.TestData.TEST_YANDEX_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.*;
import static constants.TapperAdminSelectors.AuthorizationPage.errorMsgLoginOrPassword;
import static constants.TapperAdminSelectors.RKeeperAdmin.Waiters.backToPreviousPage;


@Order(92)
@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация")
@DisplayName("Авторизация официанта, когда у него отозвали приглашение")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _9_2_authAfterCancelingInvitationTest extends BaseTest {

    static String password;

    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    YandexPage yandexPage = new YandexPage();
    Waiters waiters = new Waiters();
    BaseActions baseActions = new BaseActions();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";
        authorizationPage.authorizationUser(ADMIN_WAITER_LOGIN_EMAIL, ADMIN_WAITER_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Отправка приглашение на почту официанту")
    public void sendInviteWaiter() {

        waiters.goToWaiterCategory();
        waiters.sendInviteToWaiterEmail(OPTIMUS_PRIME_WAITER);

    }

    @Test
    @DisplayName("1.3. Отменяем приглашение")
    public void cancelInvitationByAdmin() {

        //rootPage.forceWait(2000); // toDo после отправки инвайта появляется сверху уведомление и перекрывает лейаут у формы отмены инвайта. поэтому ждём
        waiters.cancelMailWaiter();
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
        yandexPage.yandexAuthorization();
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

        baseActions.forceWait(2000); // toDo не успевает прогрузиться инпуты после смены вкладки

        authorizationPage.authorizeUser(TEST_YANDEX_LOGIN_EMAIL,password);

        errorMsgLoginOrPassword.shouldHave(Condition.text("Неверный E-mail или пароль "), Duration.ofSeconds(2));
        System.out.println("Авторизоваться не удалось, ошибка ввода данных");

    }

    @Test
    @DisplayName("1.8. Удаляем письмо на почте Яндекса")
    public void deleteYandexInviteMail() {
        yandexPage.deleteMail();
    }

}
