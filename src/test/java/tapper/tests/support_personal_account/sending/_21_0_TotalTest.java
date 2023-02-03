package tapper.tests.support_personal_account.sending;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import support_personal_account.sending.Sending;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;

@Order(210)
@Epic("Личный кабинет техподдержки")
@Feature("Рассылка")
@DisplayName("Проверка категории Рассылка")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _21_0_TotalTest extends BaseTest {

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

}
