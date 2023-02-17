package tapper.tests.support_personal_account.sending;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import support_personal_account.sending.Sending;
import tapper_table.RootPage;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static api.ApiData.orderData.TABLE_AUTO_111_ID;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_REVIEW;
import static data.Constants.WAIT_FOR_TELEGRAM_SUPPORT_SENDING;
import static data.selectors.SupportPersonalAccount.Sending.*;

@Order(210)
@Epic("Личный кабинет техподдержки")
@Feature("Рассылка")
@DisplayName("Проверка категории Рассылка")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _21_0_TotalTest extends BaseTest {

    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Sending sending = new Sending();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на категорию рассылка, проверка всех элементов")
    public void goToSendingCategory() {

        sending.goToSending();
        sending.isSendingCategoryCorrect();

    }

    @Test
    @DisplayName("1.3. Отправляем письмо всем")
    public void sendMessageToAll() {

        sending.sendMessageToCertainTypeRecipient(sendToAllContainer, TEST_COMMENT_IN_SUPPORT_SENDING_TO_ALL);

    }

    @Test
    @DisplayName("1.4. Проверяем сообщение в телеграмме")
    public void checkMsgInTgToAll() {

        rootPage.getAdminSendingMsgData
                (TABLE_AUTO_111_ID, TEST_COMMENT_IN_SUPPORT_SENDING_TO_ALL, WAIT_FOR_TELEGRAM_SUPPORT_SENDING);

    }

    @Test
    @DisplayName("1.5. Отправляем письмо админам")
    public void sendMessageToAdmins() {

        sending.sendMessageToCertainTypeRecipient(sendToManagerContainer, TEST_COMMENT_IN_SUPPORT_SENDING_TO_ADMINS);

    }

    @Test
    @DisplayName("1.6. Проверяем сообщение в телеграмме")
    public void checkMsgInTgToAdmins() {

        rootPage.getAdminSendingMsgData
                (TABLE_AUTO_111_ID, TEST_COMMENT_IN_SUPPORT_SENDING_TO_ADMINS, WAIT_FOR_TELEGRAM_SUPPORT_SENDING);

    }

}
