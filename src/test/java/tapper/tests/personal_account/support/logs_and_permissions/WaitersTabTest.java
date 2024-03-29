package tapper.tests.personal_account.support.logs_and_permissions;

import admin_personal_account.waiters.Waiters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import support_personal_account.logs_and_permissions.LogsAndPermissions;
import support_personal_account.logs_and_permissions.WaiterTab;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.HashMap;

import static data.Constants.TestData.AdminPersonalAccount.ROBOCOP_WAITER;
import static data.Constants.TestData.SupportPersonalAccount.*;


@Epic("Личный кабинет техподдержки")
@Feature("Логи/доступы")
@Story("Проверка таба Официанты")
@DisplayName("Проверка таба Официанты")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class WaitersTabTest extends PersonalAccountTest {

    static HashMap<String, String> tgData;
    AuthorizationPage authorizationPage = new AuthorizationPage();
    LogsAndPermissions logsAndPermissions = new LogsAndPermissions();
    Waiters waiters = new Waiters();
    WaiterTab waiterTab = new WaiterTab();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу логов, выбор тестового ресторана")
    public void goToLogsAndPermissions() {

        logsAndPermissions.goToLogsAndPermissionsCategory();

    }

    @Test
    @DisplayName("1.3. Выбор тестового ресторана")
    public void chooseRestaurant() {

        logsAndPermissions.chooseRestaurant(KEEPER_RESTAURANT_NAME);

    }

    @Test
    @DisplayName("1.4. Переход в таб, проверка всех форм, проверка поиска, сброса фильтра")
    public void sendInviteWaiter() {

        logsAndPermissions.isWaiterTabCorrect();
        waiters.isWaiterCategoryCorrect();
        waiters.searchWaiterNegative();
        waiters.searchWaiter(ROBOCOP_WAITER);

    }

    @Test
    @DisplayName("1.5. Проверка карточки официанта")
    public void isWaiterDetailCardCorrect() {

        waiters.clickInFirstResult();
        waiterTab.isWaiterDetailCardCorrect();

    }

    @Test
    @DisplayName("1.6. Установка тг логина и айди")
    public void setTelegramLoginAndId() {

        tgData = waiterTab.setTelegramLoginAndId();

    }

    @Test
    @DisplayName("1.7. Проверка что данные сохранились")
    public void isChangedDataCorrect() {

        waiters.clickInFirstResult();
        waiterTab.isChangedDataCorrect(tgData);

    }

}
