package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_333;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.AdminPersonalAccount.Menu.enableMenuForVisitorsButton;
import static data.selectors.TapperTable.RootPage.DishList.orderContainer;
import static data.selectors.TapperTable.RootPage.Menu.menuDishContainer;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;

@Order(110)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Проверка что включенное меню отображается")
@DisplayName("Проверка что включенное меню отображается")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_0_TotalTest extends AdminBaseTest {

    int adminTab = 0;
    int tapperTableTab = 1;

    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Menu menu = new Menu();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу меню")
    public void goToMenu() {

        menu.goToMenuCategory();
        menu.isMenuCorrect();

    }

    @Test
    @DisplayName("1.3. Включаем меню для гостей")
    public void activateMenuForGuests() {

        menu.activateFirstCategoryAndDishInMenu();
        menu.isDishItemCorrectInList();

    }

    @Test
    @DisplayName("1.4. Переходим на стол и проверяем что иконка меню есть и само меню отображается")
    public void checkMenuIconAndContainerInTable() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);

        rootPage.isElementVisibleDuringLongTime(appFooterMenuIcon,10);
        rootPage.click(appFooterMenuIcon);
        rootPage.isElementVisible(menuDishContainer);

    }

    @Test
    @DisplayName("1.5. Отключаем отображение меню для гостей по кнопке и проверяем стол")
    public void deactivateMenuForGuest() {

        rootPage.switchBrowserTab(adminTab);
        rootPage.click(enableMenuForVisitorsButton);

        rootPage.switchBrowserTab(tapperTableTab);
        rootPage.refreshPage();

        rootPage.click(appFooterMenuIcon);
        rootPage.isElementInvisible(orderContainer);

    }

    @Test
    @DisplayName("1.6. В админке отключаем все категории но кнопка показывать гостям активна")
    public void deactivateCategoryButShowGuestButtonIsActive() {

        rootPage.switchBrowserTab(adminTab);

        menu.deactivateMenuCategory();
        rootPage.click(enableMenuForVisitorsButton);

        rootPage.switchBrowserTab(tapperTableTab);
        rootPage.refreshPage();

        rootPage.click(appFooterMenuIcon);
        rootPage.isElementInvisible(orderContainer);

    }

    @Test
    @DisplayName("1.7. Проверяем кнопку 'Обновить меню'")
    public void isRefreshMenuCorrect() {

        rootPage.switchBrowserTab(adminTab);
        menu.isRefreshMenuCorrect();

    }

    @Test
    @DisplayName("1.8. Проверяем отображения списка блюд плиткой")
    public void isDishListTileViewCorrectIsDishListTileViewCorrect() {

        menu.isDishListTileViewCorrect();

    }

}
