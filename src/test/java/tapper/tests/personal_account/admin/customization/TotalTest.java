package tapper.tests.personal_account.admin.customization;

import admin_personal_account.customization.Customization;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;

@Epic("Личный кабинет администратора ресторана")
@Feature("Кастомизация")
@Story("Проверка переключения типу получателя, замена шаблона, проверка на столе")
@DisplayName("Проверка переключения типу получателя, замена шаблона, проверка на столе")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiterName = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    int adminTab = 0;
    int tapperTableTab = 1;

    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Customization customization = new Customization();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу кастомизации, проверка всех элементов")
    void goToMenu() {

        customization.goToCustomizationCategory();
        customization.isCustomizationCategoryCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Проверяем что после обновления страницы мы остаемся на этой вкладке")
    void isCorrectAfterRefresh() {

        customization.isSavedPageAfterReload();

    }

    @Test
    @Order(4)
    @DisplayName("Меняем тип получателя на менеджера и проверяем что на столе сменилась кнопка")
    void checkCallManager() {

        customization.choseOnlyManager();

        rootPage.openNewTabAndSwitchTo(tableUrl);
        customization.checkCallWaiterButtonTypeOnTable("Написать\nменеджеру","Вызов менеджера");


    }

    @Test
    @Order(5)
    @DisplayName("Меняем тип получателя на официанта и менеджерам и проверяем что на столе сменилась кнопка")
    void checkCallWaiter() {

        rootPage.switchBrowserTab(adminTab);
        customization.choseWaiterAndManager();

        rootPage.switchBrowserTab(tapperTableTab);
        rootPage.refreshPage();
        customization.checkCallWaiterButtonTypeOnTable("Написать\nофицианту","Вызов официанта");

        rootPage.switchBrowserTab(adminTab);

    }


}
