package tapper.tests.admin_personal_account.customization;

import admin_personal_account.customization.Customization;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_333;

@Order(150)
@Epic("Личный кабинет администратора ресторана")
@Feature("Кастомизация")
@Story("Проверка переключения типу получателя, замена шаблона, проверка на столе")
@DisplayName("Проверка переключения типу получателя, замена шаблона, проверка на столе")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _15_0_TotalTest extends BaseTest {

    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Customization customization = new Customization();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу кастомизации, проверка всех элементов")
    public void goToMenu() {

        customization.goToCustomizationCategory();
        customization.isCustomizationCategoryCorrect();

    }

    @Test
    @DisplayName("1.3. Меняем тип получателя на менеджера и проверяем что на столе сменилась кнопка")
    public void checkCallManager() {

        customization.choseOnlyManager();

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_333);
        customization.checkCallWaiterButtonTypeOnTable("Написать\nменеджеру","Вызов менеджера");


    }

    @Test
    @DisplayName("1.4. Меняем тип получателя на официанта и менеджерам и проверяем что на столе сменилась кнопка")
    public void checkCallWaiter() {

        rootPage.switchBrowserTab(0);
        customization.choseWaiterAndManager();

        rootPage.switchBrowserTab(1);
        rootPage.refreshPage();
        customization.checkCallWaiterButtonTypeOnTable("Написать\nофицианту","Вызов официанта");

        rootPage.switchBrowserTab(0);

    }


}
