package tapper.tests.waiter_personal_account;


import admin_personal_account.waiters.Waiters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.TapperTable.WRONG_EMAIL_PATTERN;


@Epic("Личный кабинет официант ресторана")
@Feature("Приглашение официанта на некорректный емейл")
@DisplayName("Приглашение официанта на некорректный емейл")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WrongEmailInvitationTest extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiters waiters = new Waiters();

    @Test
    @Order(1)
    @DisplayName("Авторизуемся под админом")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        waiters.goToWaiterCategory();

    }

    @Test
    @Order(2)
    @DisplayName("Отправляем приглашение и проверяем что будет ошибка при некорректном емейле")
    void sendInviteWaiter() {

        waiters.sendInviteToWaiterWithWrongEmail(IRONHIDE_WAITER, WRONG_EMAIL_PATTERN);

    }

}
