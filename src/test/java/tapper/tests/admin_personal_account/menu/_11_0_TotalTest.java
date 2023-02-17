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
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_333;
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
public class _11_0_TotalTest extends BaseTest {

    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Menu menu = new Menu();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

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

        menu.activateMenuForGuests();

    }

    @Test
    @DisplayName("1.4. Переходим на стол и проверяем что иконка меню есть и само меню отображается")
    public void checkMenuIconAndContainerInTable() {

        baseActions.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_333);
      //  rootPage.closeHintModal();

        baseActions.isElementVisible(appFooterMenuIcon);
        baseActions.click(appFooterMenuIcon);
        baseActions.isElementVisible(menuDishContainer);

    }

    @Test
    @DisplayName("1.5. Отключаем отображение меню для гостей по кнопке и проверяем стол")
    public void deactivateMenuForGuest() {

        Selenide.switchTo().window(0);
        baseActions.click(enableMenuForVisitorsButton);

        Selenide.switchTo().window(1);
        rootPage.refreshPage();

        baseActions.click(appFooterMenuIcon);
        baseActions.isElementInvisible(orderContainer);

    }

    @Test
    @DisplayName("1.6. В админке отключаем все категории но кнопка показывать гостям активна")
    public void deactivateCategoryButShowGuestButtonIsActive() {

        Selenide.switchTo().window(0);

        menu.deactivateMenuCategory();
        baseActions.click(enableMenuForVisitorsButton);

        Selenide.switchTo().window(1);
        rootPage.refreshPage();

        baseActions.click(appFooterMenuIcon);
        baseActions.isElementInvisible(orderContainer);

    }

}
