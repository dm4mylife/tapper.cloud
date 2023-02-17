package tapper.tests.waiter_personal_account;


import admin_personal_account.waiters.Waiters;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.TapperTable.WRONG_EMAIL_PATTERN;

@Order(142)
@Epic("Личный кабинет официант ресторана")
@Feature("Приглашение официанта на некорректный емейл")
@DisplayName("Приглашение официанта на некорректный емейл")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _14_2_WrongEmailInvitationTest extends BaseTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();

    Waiters waiters = new Waiters();

    @Test
    @DisplayName("1.0. Авторизуемся под админом")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        waiters.goToWaiterCategory();

    }

    @Test
    @DisplayName("1.1. Отправляем приглашение и проверяем что будет ошибка при некорректном емейле")
    public void sendInviteWaiter() {

        waiters.sendInviteToWaiterWithWrongEmail(IRONHIDE_WAITER, WRONG_EMAIL_PATTERN);

    }


}
