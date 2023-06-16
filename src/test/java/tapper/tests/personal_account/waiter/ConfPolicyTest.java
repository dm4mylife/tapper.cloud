package tapper.tests.personal_account.waiter;


import admin_personal_account.AdminAccount;
import admin_personal_account.waiters.Waiters;
import api.MailByApi;
import data.Constants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
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
@Feature("Политика конфиденциальности")
@DisplayName("Политика конфиденциальности")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConfPolicyTest extends PersonalAccountTest {

    static HashMap<String,String> waiterData = new HashMap<>();
    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiters waiters = new Waiters();
    Waiter waiter = new Waiter();

    @Test
    @Order(1)
    @DisplayName("Авторизуемся под админом")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        waiters.goToWaiterCategory();

    }

    @Test
    @Order(2)
    @DisplayName("Отправляем приглашение официанту")
    void yandexAuthorization() {

        waiters.sendInviteToWaiterEmail(IRONHIDE_WAITER, Constants.TestData.Yandex.WAITER_LOGIN_EMAIL);
        backToPreviousPage.click();
        waiters.isWaiterStatusCorrectInPreviewAndCard(IRONHIDE_WAITER, INVITED_IN_SERVICE_TEXT);
        adminAccount.logOut();

    }

    @Test
    @Order(3)
    @DisplayName("Получаем данные из письма по приглашению")
    void sendInviteWaiter() throws MessagingException, IOException {

        waiterData = MailByApi.getMailData
                ("waiter.yandex.mail", "waiter.yandex.password", WAITER_REGISTRATION_EMAIL);

        Assertions.assertEquals(waiterData.get("url"), PERSONAL_ACCOUNT_PROFILE_STAGE_URL);

    }

    @Test
    @Order(4)
    @DisplayName("Авторизация под данными из письма")
    void authorizeFromMailUrl() {

        authorizationPage.authorizeNewWaiter
                (waiterData.get("login"), waiterData.get("password"),waiterData.get("url"));



    }

    @Test
    @Order(5)
    @DisplayName("Проверка формы политики конфиденциальности")
    void isConfPolicyCorrectModal() {

        waiter.isConfPolicyCorrectModal();

    }

    @Test
    @Order(6)
    @DisplayName("Отказываемся от принятия документа и проверяем корректностью форм")
    void donTAgreeWithConfPolicy() {

        waiter.donTAgreeWithConfPolicy();

    }

    @Test
    @Order(7)
    @DisplayName("Снова авторизовываемся в учетной записи")
    void authorizeFromMailUrlAgain() {

        authorizeFromMailUrl();

    }

    @Test
    @Order(8)
    @DisplayName("Принимаем политику соглашений и проверяем корректностью форм")
    void agreeWithConfPolicy() {

        waiter.agreeWithConfPolicy();
        adminAccount.logOut();

    }

    @Test
    @Order(9)
    @DisplayName("Проверка что политики нет если соглашались и заново авторизовались")
    void authorizationWaiterThatAgreedConfPolicy() {

        authorizationPage.authorizationWaiterThatAgreedConfPolicy(waiterData.get("login"), waiterData.get("password"));

    }


    @Test
    @Order(10)
    @DisplayName("Отвязываем почту от учетной записи")
    void unlinkMailWaiter() {

        waiters.unlinkMailWaiter(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD,IRONHIDE_WAITER);

    }

    @Test
    @Order(11)
    @DisplayName("Удаляем письмо на почте Яндекса")
    void deleteYandexInviteMail() {

        MailByApi.deleteMailsByApi
                ("waiter.yandex.mail", "waiter.yandex.password", WAITER_REGISTRATION_EMAIL);

    }

}
