package tapper.tests.personal_account.admin.menu;

import admin_personal_account.menu.Menu;
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
@Feature("Меню")
@Story("Проверка что если меню отключено, то на столе отображается кнопка и информация")
@DisplayName("Проверка что если меню отключено, то на столе отображается кнопка и информация")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmptyMenuTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiterName = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    int adminTab = 0;
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Menu menu = new Menu();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу меню")
    void goToMenu() {

        menu.goToMenuCategory();
        menu.isMenuCategoryCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Выключаем меню для гостей если включено")
    void deactivateMenu() {

        menu.deactivateShowGuestSliderIfActivated();

    }

    @Test
    @Order(4)
    @DisplayName("Переходим на стол и проверяем функционал меню при отключенном в админке")
    void checkMenuIconAndContainerInTable() {

        rootPage.openNewTabAndSwitchTo(tableUrl);
        rootPage.clickOnMenuInFooter();

        rootPage.emptyMenuCorrect();

    }

    @Test
    @Order(5)
    @DisplayName("Возвращаемся в админку и включаем обратно меню")
    void deactivateMenuForGuest() {

        rootPage.switchBrowserTab(adminTab);
        menu.activateShowActiveForGuest();

    }

}
