package tapper.tests.personal_account.admin.waiters;


import admin_personal_account.AdminAccount;
import admin_personal_account.waiters.Waiters;
import api.MailByApi;
import data.Constants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.WAITER_REGISTRATION_EMAIL;
import static data.selectors.AdminPersonalAccount.Waiters.backToPreviousPage;


@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@Story("Авторизация официанта, когда у него отозвали приглашение")
@DisplayName("Авторизация официанта, когда у него отозвали приглашение")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthAfterCancelingInvitationTest extends PersonalAccountTest {

    static HashMap<String,String> waiterData;
    AdminAccount adminAccount = new AdminAccount();
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
    @DisplayName("Отправка приглашение на почту официанту")
    void sendInviteWaiter() {

        waiters.goToWaiterCategory();
        waiters.sendInviteToWaiterEmail(IRONHIDE_WAITER, Constants.TestData.Yandex.WAITER_LOGIN_EMAIL);

    }

    @Test
    @Order(3)
    @DisplayName("Отменяем приглашение")
    void cancelInvitationByAdmin() {

        waiters.cancelEMailWaiterInvitationInCard();
        backToPreviousPage.click();

    }

    @Test
    @Order(4)
    @DisplayName("Проверка смены статуса верификации")
    void checkWaiterStatus() {

        waiters.isWaiterStatusCorrectInPreviewAndCard(IRONHIDE_WAITER, "Ожидает приглашения");
        adminAccount.logOut();

    }

    @Test
    @Order(5)
    @DisplayName("Получение письма на почту и сохранение данных")
    void getMailData() throws MessagingException, IOException {

        waiterData = MailByApi.getMailData
                ("waiter.yandex.mail", "waiter.yandex.password", WAITER_REGISTRATION_EMAIL);

        Assertions.assertEquals(PERSONAL_ACCOUNT_PROFILE_STAGE_URL, waiterData.get("url"));

    }

    @Test
    @Order(6)
    @DisplayName("Авторизация по данным из письма")
    void authorizeFromMailUrl() {

        authorizationPage.authorizeFromMailUrl
                (waiterData.get("login"), waiterData.get("password"),waiterData.get("url"),true);

    }

    @Test
    @Order(7)
    @DisplayName("Удаляем письмо на почте Яндекса")
    void deleteYandexInviteMail() {

        MailByApi.deleteMailsByApi
                ("waiter.yandex.mail", "waiter.yandex.password", WAITER_REGISTRATION_EMAIL);

    }

}
