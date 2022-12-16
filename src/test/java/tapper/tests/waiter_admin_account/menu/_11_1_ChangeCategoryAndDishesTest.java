package tapper.tests.waiter_admin_account.menu;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_admin.AuthorizationPage;
import tapper_admin.menu.Menu;
import tests.BaseTest;

import static com.codeborne.selenide.Condition.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_WAITER_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_WAITER_PASSWORD;
import static constants.TapperAdminSelectors.RKeeperAdmin.Menu.*;
import static constants.TapperTableSelectors.RootPage.Menu.*;
import static constants.TapperTableSelectors.RootPage.TapBar.appFooterMenuIcon;
import static constants.TapperTableSelectors.RootPage.TapBar.appFooterWalletIcon;

@Order(111)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@DisplayName("Смена имени категории и позиции, проверка на столе")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_1_ChangeCategoryAndDishesTest extends BaseTest {

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

    }


    @Test
    @DisplayName("1.3. Делаем видимым категорию, блюдо и переключатель если отключены")
    public void activateFirstCategoryAndActivateShowGuestMenu() {

        menu.activateFirstCategoryAndActivateShowGuestMenu();

    }


    @Test
    @DisplayName("1.4. Редактируем имя категории")
    public void editCategoryName() {

        categoryNameAfterEditing = menu.isCategoryEditNameCorrect();

    }

    @Test
    @DisplayName("1.5. Редактируем имя позиции")
    public void editDishName() {

        categoryDishItems.get(0).click();

        dishNameAfterEditing = menu.isDishEditNameCorrect();

    }

    @Test
    @DisplayName("1.6. Проверяем изменения на столе")
    public void checkChangesInTable() {

        baseActions.openPage(STAGE_RKEEPER_TABLE_3);
        baseActions.forceWait(3000);
        appFooterMenuIcon.click();

        System.out.println(categoryMenuItems);
        System.out.println(categoryMenuItems.get(0).getText());

        categoryMenuItems.findBy(matchText(categoryNameAfterEditing)).shouldHave(visible);
        dishMenuItems.findBy(matchText(dishNameAfterEditing)).shouldHave(visible);

        System.out.println("На столе изменения категории и блюда отобразились");

    }

}
