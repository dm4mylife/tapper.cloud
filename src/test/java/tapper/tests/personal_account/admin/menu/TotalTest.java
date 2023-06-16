package tapper.tests.personal_account.admin.menu;

import admin_personal_account.menu.Menu;
import common.BaseActions;
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
import static data.selectors.AdminPersonalAccount.Menu.enableMenuForVisitorsButton;
import static data.selectors.TapperTable.RootPage.DishList.orderContainer;
import static data.selectors.TapperTable.RootPage.Menu.menuDishContainer;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;

@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Проверка что включенное меню отображается")
@DisplayName("Проверка что включенное меню отображается")

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
    @DisplayName("Включаем меню для гостей")
    void activateMenuForGuests() {

        menu.activateFirstCategoryAndDishInMenu();
        menu.isDishItemCorrectInList();

    }

    @Test
    @Order(4)
    @DisplayName("Переходим на стол и проверяем что иконка меню есть и само меню отображается")
    void checkMenuIconAndContainerInTable() {

        rootPage.openNewTabAndSwitchTo(tableUrl);

        rootPage.isElementVisibleDuringLongTime(appFooterMenuIcon,10);
        BaseActions.click(appFooterMenuIcon);
        rootPage.isElementVisible(menuDishContainer);

    }

    @Test
    @Order(5)
    @DisplayName("Отключаем отображение меню для гостей по кнопке и проверяем стол")
    public void deactivateMenuForGuest() {

        rootPage.switchBrowserTab(adminTab);
        BaseActions.click(enableMenuForVisitorsButton);

        rootPage.switchBrowserTab(tapperTableTab);
        rootPage.refreshPage();

        BaseActions.click(appFooterMenuIcon);
        rootPage.isElementInvisible(orderContainer);

    }

    @Test
    @Order(6)
    @DisplayName("В админке отключаем все категории но кнопка показывать гостям активна")
    void deactivateCategoryButShowGuestButtonIsActive() {

        rootPage.switchBrowserTab(adminTab);

        menu.deactivateAllMenuCategory();
        BaseActions.click(enableMenuForVisitorsButton);

        rootPage.switchBrowserTab(tapperTableTab);
        rootPage.refreshPage();

        BaseActions.click(appFooterMenuIcon);
        rootPage.isElementInvisible(orderContainer);

    }

    @Test
    @Order(7)
    @DisplayName("Проверяем кнопку 'Обновить меню'")
    void isRefreshMenuCorrect() {

        rootPage.switchBrowserTab(adminTab);
        menu.isRefreshMenuCorrect();

    }

    @Test
    @Order(8)
    @DisplayName("Проверяем отображения списка блюд плиткой")
    void isDishListTileViewCorrectIsDishListTileViewCorrect() {

        menu.isDishListTileViewCorrect();

    }

}
