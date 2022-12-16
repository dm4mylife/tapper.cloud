package tapper.tests.waiter_admin_account.menu;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_admin.AuthorizationPage;
import tapper_admin.menu.Menu;
import tapper_table.RootPage;
import tests.BaseTest;

import static com.codeborne.selenide.Condition.text;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_WAITER_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_WAITER_PASSWORD;
import static constants.TapperAdminSelectors.RKeeperAdmin.Menu.*;
import static constants.TapperTableSelectors.RootPage.Menu.*;
import static constants.TapperTableSelectors.RootPage.TapBar.appFooterMenuIcon;
import static constants.TapperTableSelectors.RootPage.TapBar.appFooterWalletIcon;

@Order(110)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@DisplayName("Проверка что включенное меню отображается")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_0_totalTest extends BaseTest {

    static String categoryNameBeforeEditing;
    static String dishNameBeforeEditing;
    static String categoryNameAfterEditing;
    static String dishNameAfterEditing;

    BaseActions baseActions = new BaseActions();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Menu menu = new Menu();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

        authorizationPage.authorizationUser(ADMIN_WAITER_LOGIN_EMAIL, ADMIN_WAITER_PASSWORD);

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

        baseActions.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_3);

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
        Selenide.refresh();
        baseActions.forceWait(2000);

        baseActions.isElementInvisible(appFooterMenuIcon);
        baseActions.isElementInvisible(appFooterWalletIcon);

    }

    @Test
    @DisplayName("1.6. В админке отключаем все категории но кнопка показывать гостям активна")
    public void deactivateCategoryButShowGuestButtonIsActive() {

        Selenide.switchTo().window(0);

        menu.deactivateMenuCategory();
        baseActions.click(enableMenuForVisitorsButton);

        Selenide.switchTo().window(1);
        Selenide.refresh();
        baseActions.forceWait(2000);

        baseActions.isElementInvisible(appFooterMenuIcon);
        baseActions.isElementInvisible(appFooterWalletIcon);

    }

}
