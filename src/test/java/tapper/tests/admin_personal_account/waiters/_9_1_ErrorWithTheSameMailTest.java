package tapper.tests.admin_personal_account.waiters;


import com.codeborne.selenide.Configuration;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_admin_personal_account.AuthorizationPage;
import tapper_admin_personal_account.waiters.Waiters;
import tests.BaseTest;

import static constants.Constant.TestDataRKeeperAdmin.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_RESTAURANT_PASSWORD;
import static constants.selectors.AdminPersonalAccountSelectors.Waiters.backToPreviousPage;
import static constants.selectors.AdminPersonalAccountSelectors.Waiters.enterEmailField;


@Order(91)
@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация")
@DisplayName("Приглашение на один и тот же e-mail, который уже занят у официанта")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _9_1_ErrorWithTheSameMailTest extends BaseTest {

    static String emailFromVerifiedUser;

    BaseActions baseActions = new BaseActions();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiters waiters = new Waiters();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";
        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход в категорию и сохранение почту у авторизованного")
    public void goToCategoryAndFindVerifiedWaiter() {

        waiters.goToWaiterCategory();
        waiters.findFirstMatchByStatusAndClickInWaiterCard("Официант верифицирован");
        emailFromVerifiedUser = enterEmailField.getValue();
        backToPreviousPage.click();

    }

    @Test
    @DisplayName("1.3. Поиск официанта в статусе 'Ожидает приглашения'")
    public void findNonVerifiedWaiter() {

        waiters.findFirstMatchByStatusAndClickInWaiterCard("Ожидает приглашения");

    }

    @Test
    @DisplayName("1.4. Проверка ошибки уже привязанной почты")
    public void sendInviteWaiter() {

        baseActions.forceWait(500); //toDo слишком быстро грузится, инпут почты не успевает прогрузиться и вводит пустое значение
        waiters.isErrorMailCorrect(emailFromVerifiedUser);

    }




}
