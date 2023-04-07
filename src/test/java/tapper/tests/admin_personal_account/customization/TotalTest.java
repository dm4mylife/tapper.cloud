package tapper.tests.admin_personal_account.customization;

import admin_personal_account.customization.Customization;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;

@Epic("Личный кабинет администратора ресторана")
@Feature("Кастомизация")
@Story("Проверка переключения типу получателя, замена шаблона, проверка на столе")
@DisplayName("Проверка переключения типу получателя, замена шаблона, проверка на столе")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TotalTest extends PersonalAccountTest {

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

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);
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
