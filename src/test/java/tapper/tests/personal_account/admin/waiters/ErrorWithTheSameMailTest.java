package tapper.tests.personal_account.admin.waiters;


import admin_personal_account.waiters.Waiters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.AdminPersonalAccount.Waiters.backToPreviousPage;
import static data.selectors.AdminPersonalAccount.Waiters.enterEmailField;



@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@Story("Приглашение на один и тот же e-mail, который уже занят у официанта")
@DisplayName("Приглашение на один и тот же e-mail, который уже занят у официанта")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ErrorWithTheSameMailTest extends PersonalAccountTest {

    static String emailFromVerifiedUser;

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiters waiters = new Waiters();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход в категорию и сохранение почту у авторизованного")
    void goToCategoryAndFindVerifiedWaiter() {

        waiters.goToWaiterCategory();
        waiters.findFirstMatchByStatusAndClickInWaiterCard(VERIFIED_WAITER_TEXT);
        emailFromVerifiedUser = enterEmailField.getValue();
        backToPreviousPage.click();

    }

    @Test
    @Order(3)
    @DisplayName("Поиск официанта в статусе 'Ожидает приглашения'")
    void findNonVerifiedWaiter() {

        waiters.findFirstMatchByStatusAndClickInWaiterCard(IS_WAITING_WAITER_TEXT);

    }

    @Test
    @Order(4)
    @DisplayName("Проверка ошибки уже привязанной почты")
    void sendInviteWaiter() {

        waiters.isErrorMailCorrect(emailFromVerifiedUser);

    }

}
