package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import com.codeborne.selenide.Configuration;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_333;
import static data.selectors.TapperTable.RootPage.Menu.categoryMenuItems;
import static data.selectors.TapperTable.RootPage.Menu.dishMenuItems;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;

@Order(111)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Смена имени категории и позиции, проверка на столе")
@DisplayName("Смена имени категории и позиции, проверка на столе")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_1_ChangeCategoryAndDishesTest extends BaseTest {

    static String categoryNameAfterEditing;
    static String dishNameAfterEditing;

    static String weightAndAmountAfterEditing;
    static int categoryIndex = 0;
    static int adminBrowserTab = 0;

    static int dishIndex = 0;
    static int tapperBrowserTab = 1;



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

    }

    @Test
    @DisplayName("1.3. Делаем видимым категорию, блюдо и переключатель если отключены")
    public void activateFirstCategoryAndActivateShowGuestMenu() {

        menu.activateCategoryAndDishAndActivateShowGuestMenuByIndex(categoryIndex,dishIndex);

    }

    @Test
    @DisplayName("1.4. Проверяем изменения на столе")
    public void openUrl() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_333);
        rootPage.switchBrowserTab(adminBrowserTab);

    }

    @Test
    @DisplayName("1.5. Редактируем имя категории")
    public void editCategoryName() {

        categoryNameAfterEditing = menu.isCategoryEditNameCorrect(categoryIndex);

    }

    @Test
    @DisplayName("1.6. Проверяем изменение категории блюда на столе")
    public void isCategoryMenuChangingNameCorrect() {

        menu.isChangingAppliedOnTable(tapperBrowserTab,adminBrowserTab,categoryMenuItems,categoryNameAfterEditing);

    }

    @Test
    @DisplayName("1.7. Редактируем имя позиции")
    public void editDishName() {

        dishNameAfterEditing = menu.isDishEditNameByGuestCorrect(dishIndex);

    }

    @Test
    @DisplayName("1.8. Проверяем изменение категории блюда на столе")
    public void isDishMenuChangingNameCorrect() {

        menu.isChangingAppliedOnTable(tapperBrowserTab,adminBrowserTab,dishMenuItems,dishNameAfterEditing);

    }

    @Test
    @DisplayName("1.9. Редактируем состав")
    public void isDishEditDescriptionCorrect() {

        menu.isDishEditIngredientsCorrect(dishIndex);

    }

    @Test
    @DisplayName("2.0. Редактируем вес и его количество")
    public void isDishEditWeightAndAmountCorrect() {

        weightAndAmountAfterEditing = menu.isDishEditWeightAndAmountCorrect(dishIndex);

    }

    @Test
    @DisplayName("2.1. Проверяем изменение веса блюда на столе")
    public void isDishMenuChangingWeightAndAmountCorrect() {

        menu.isChangingAppliedOnTable(tapperBrowserTab,adminBrowserTab,dishMenuItems,weightAndAmountAfterEditing);

    }


}