package tapper.tests.personal_account.waiter;


import admin_personal_account.AdminAccount;
import admin_personal_account.waiters.Waiters;
import api.MailByApi;
import data.Constants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import junit.framework.Assert;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;
import waiter_personal_account.Waiter;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.WAITER_REGISTRATION_EMAIL;
import static data.selectors.AdminPersonalAccount.Waiters.backToPreviousPage;


@Epic("Личный кабинет официант ресторана")
@Feature("Отвязка и привязка карты официанта")
@DisplayName("Отвязка и привязка карты официанта")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UnlinkLinkWaiterCardTest extends PersonalAccountTest {

    static HashMap<String,String> waiterData;
    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiter waiter = new Waiter();
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
    @DisplayName("Отправляем приглашение")
    void sendInviteWaiter() {

        waiters.sendInviteToWaiterEmail(IRONHIDE_WAITER, Constants.TestData.Yandex.WAITER_LOGIN_EMAIL);

    }

    @Test
    @Order(3)
    @DisplayName("Проверка смены статуса верификации")
    void checkWaiterStatus() {

        backToPreviousPage.click();
        waiters.isWaiterStatusCorrectInPreviewAndCard(IRONHIDE_WAITER, INVITED_IN_SERVICE_TEXT);
        adminAccount.logOut();

    }

    @Test
    @Order(4)
    @DisplayName("Получение письма на почту и сохранение данных")
    void getMailData() throws MessagingException, IOException {

        waiterData = MailByApi.getMailData
                ("waiter.yandex.mail", "waiter.yandex.password", WAITER_REGISTRATION_EMAIL);

        Assert.assertEquals(waiterData.get("url"), PERSONAL_ACCOUNT_PROFILE_STAGE_URL);

    }

    @Test
    @Order(5)
    @DisplayName("Авторизация по данным из письма")
    void authorizeFromMailUrl() {

        authorizationPage.authorizeFromMailUrl
                (waiterData.get("login"), waiterData.get("password"),waiterData.get("url"),false);

        adminAccount.isRegistrationComplete();
        Waiter.skipConfPolicyModal();

    }

    @Test
    @Order(6)
    @DisplayName("Привязка кредитной карты в профиле официанта")
    void linkWaiterCard() {

        waiter.linkWaiterCard();
        adminAccount.logOut();

    }

    @Test
    @Order(7)
    @DisplayName("Удаляем письмо на почте Яндекса")
    void deleteYandexInviteMail() {

        MailByApi.deleteMailsByApi
                ("waiter.yandex.mail", "waiter.yandex.password", WAITER_REGISTRATION_EMAIL);

    }

}
