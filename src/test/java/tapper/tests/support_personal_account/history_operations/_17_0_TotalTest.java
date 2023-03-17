package tapper.tests.support_personal_account.history_operations;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import support_personal_account.history_operations.HistoryOperations;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;


@Order(130)
@Epic("Личный кабинет техподдержки")
@Feature("История операций")
@DisplayName("Проверка истории операций")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _17_0_TotalTest extends AdminBaseTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    HistoryOperations historyOperations = new HistoryOperations();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на категорию история операций, проверка всех элементов")
    public void goToHistoryOperations() {

        historyOperations.goToHistoryOperationsCategory();

    }

    @Test
    @DisplayName("1.2. Проверка всех элементов")
    public void isHistoryOperationsCategoryCorrect() {

        historyOperations.isHistoryOperationsCategoryCorrect();

    }

}
