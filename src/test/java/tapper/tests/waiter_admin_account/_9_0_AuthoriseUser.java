package tapper.tests.waiter_admin_account;


import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import tapper_admin.AdminAccount;
import tapper_admin.AuthorizationPage;
import tapper_admin.RegistrationPage;
import tapper_table.RootPage;
import tapper_table.YandexPage;
import tests.BaseTest;

import static constants.Constant.TestData.*;
import static constants.Constant.TestDataRKeeperAdmin.*;


@Order(90)
@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация")
@DisplayName("Авторизация администратора")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _9_0_AuthoriseUser extends BaseTest {

    static String password;

    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    RegistrationPage registrationPage = new RegistrationPage();
    YandexPage yandexPage = new YandexPage();
    RootPage rootPage = new RootPage();

    @Test
    @Step("1.1. Авторизация")
    public void authorizeUser() {

        rootPage.openPage(R_KEEPER_ADMIN_AUTHORISATION_STAGE_URL);
        rootPage.forceWait(2000); //toDO не успевает форма логина прогрузиться
        authorizationPage.authorizationUser(ADMIN_WAITER_LOGIN_EMAIL,ADMIN_WAITER_PASSWORD);

    }

    @Test
    @Step("1.2. Авторизация")
    public void menu() {

        adminAccount.goToWaiterCategory();

        adminAccount.sendInviteToWaiterEmail(OPTIMUS_PRIME_WAITER);

        adminAccount.checkVerificationWaiterStatusAfterConfirmation(OPTIMUS_PRIME_WAITER);

        yandexPage.yandexAuthorization();

        password = yandexPage.checkTapperMail();

        yandexPage.goToAuthPageFromMail();

        authorizationPage.authorizationUser(TEST_YANDEX_LOGIN_EMAIL,password);

        yandexPage.deleteMail();

    }

}
