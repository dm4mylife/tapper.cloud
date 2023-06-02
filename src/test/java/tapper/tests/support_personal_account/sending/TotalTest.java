package tapper.tests.support_personal_account.sending;

import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import support_personal_account.sending.Sending;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static api.ApiData.OrderData.TABLE_AUTO_555_ID;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;
import static data.Constants.TestData.TapperTable.TEST_COMMENT_IN_SUPPORT_SENDING_TO_ADMINS;
import static data.Constants.TestData.TapperTable.TEST_COMMENT_IN_SUPPORT_SENDING_TO_ALL;
import static data.Constants.WAIT_FOR_TELEGRAM_SUPPORT_SENDING;
import static data.selectors.SupportPersonalAccount.Sending.sendToAllContainer;
import static data.selectors.SupportPersonalAccount.Sending.sendToManagerContainer;


@Epic("Личный кабинет техподдержки")
@Feature("Рассылка")
@DisplayName("Проверка категории Рассылка")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiterName = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Sending sending = new Sending();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на категорию рассылка, проверка всех элементов")
    void goToSendingCategory() {

        sending.goToSending();
        sending.isSendingCategoryCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Отправляем письмо всем")
    void sendMessageToAll() {

        sending.sendMessageToCertainTypeRecipient(sendToAllContainer, TEST_COMMENT_IN_SUPPORT_SENDING_TO_ALL);

    }

    @Disabled("Пока не починят баг с тем что уведомления не приходят в группу")
    @Test
    @Order(4)
    @DisplayName("Проверяем сообщение в телеграмме")
    void checkMsgInTgToAll() {

        rootPage.getAdminSendingMsgData
                (tableId, TEST_COMMENT_IN_SUPPORT_SENDING_TO_ALL, WAIT_FOR_TELEGRAM_SUPPORT_SENDING);

    }

    @Test
    @Order(5)
    @DisplayName("Отправляем письмо админам")
    void sendMessageToAdmins() {

        sending.sendMessageToCertainTypeRecipient(sendToManagerContainer, TEST_COMMENT_IN_SUPPORT_SENDING_TO_ADMINS);

    }
    @Disabled("Пока не починят баг с тем что уведомления не приходят в группу")
    @Test
    @Order(6)
    @DisplayName("Проверяем сообщение в телеграмме")
    public void checkMsgInTgToAdmins() {

        rootPage.getAdminSendingMsgData
                (tableId, TEST_COMMENT_IN_SUPPORT_SENDING_TO_ADMINS, WAIT_FOR_TELEGRAM_SUPPORT_SENDING);

    }

}
