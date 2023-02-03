package tapper.tests.admin_personal_account.menu;

import com.codeborne.selenide.Configuration;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import total_personal_account_actions.AuthorizationPage;
import admin_personal_account.menu.Menu;
import tests.BaseTest;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
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
    static int categoryIndex = 0;
    static int dishIndex = 0;

    BaseActions baseActions = new BaseActions();
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
    @DisplayName("1.4. Редактируем имя категории")
    public void editCategoryName() {
        categoryNameAfterEditing = menu.isCategoryEditNameCorrect(categoryIndex);
    }

    @Test
    @DisplayName("1.5. Редактируем имя позиции")
    public void editDishName() {
        dishNameAfterEditing = menu.isDishEditNameCorrect(dishIndex);
    }

    @Test
    @DisplayName("1.6. Проверяем изменения на столе")
    public void checkChangesInTable() {

        baseActions.openPage(STAGE_RKEEPER_TABLE_333);
        baseActions.isElementVisibleDuringLongTime(appFooterMenuIcon,20);
        baseActions.forceWait(2000);
        appFooterMenuIcon.click();

        System.out.println(categoryMenuItems.get(categoryIndex).getText());
        System.out.println(dishMenuItems.get(dishIndex).getText());

        categoryMenuItems.findBy(matchText(categoryNameAfterEditing)).shouldHave(visible);
        dishMenuItems.findBy(matchText(dishNameAfterEditing)).shouldHave(visible);
        System.out.println("На столе изменения категории и блюда отобразились");

    }

}
