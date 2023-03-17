package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.HashMap;

import static com.codeborne.selenide.Condition.matchText;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_333;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.TapperTable.RootPage.Menu.*;

@Order(111)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Смена имени категории и позиции, проверка на столе")
@DisplayName("Смена имени категории и позиции, проверка на столе")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_1_EditingDishCorrectTest extends AdminBaseTest {

    static String categoryNameAfterEditing;
    static String dishNameAfterEditing;
    static String weightAndAmountAfterEditing;
    static HashMap<String,String> dishNameAndDescription;
    static HashMap<String,String> dishNameAndIngredients;
    static String dishPrice;
    static String calories;
    static String mark;
    static int categoryIndex = 0;
    static int adminBrowserTab = 0;
    static int dishIndex = 0;
    static int tapperBrowserTab = 1;

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

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);

    }

    @Test
    @DisplayName("1.5. Редактируем имя категории")
    public void editCategoryName() {

        rootPage.switchBrowserTab(adminBrowserTab);
        categoryNameAfterEditing = menu.isCategoryEditNameCorrect(categoryIndex);

    }

    @Test
    @DisplayName("1.6. Проверяем изменение категории блюда на столе")
    public void isCategoryMenuChangingNameCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);
        menu.isChangingAppliedOnTable(categoryMenuItems,categoryNameAfterEditing);

    }

    @Test
    @DisplayName("1.7. Редактируем имя позиции")
    public void isDishEditDishNameCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        dishNameAfterEditing = menu.isDishEditNameByGuestCorrect(dishIndex);

    }

    @Test
    @DisplayName("1.8. Проверяем изменение имени блюда на столе и в детальной карточке блюда")
    public void isDishMenuChangingNameCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);

        menu.isChangingAppliedOnTable(dishMenuItemsName,dishNameAfterEditing);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItemsName,dishNameAfterEditing);

    }

    @Test
    @DisplayName("1.9. Редактируем описание позиции")
    public void isDishEditDescriptionCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        dishNameAndDescription = menu.isDishEditDescriptionCorrect(dishIndex);

    }

    @Test
    @DisplayName("2.0. Проверяем изменение описания в детальной карточке блюда")
    public void isDishMenuChangingDescriptionCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItems,dishNameAndDescription,"description");

    }

    @Test
    @DisplayName("2.1. Редактируем состав позиции")
    public void isDishEditIngredientsCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        dishNameAndIngredients = menu.isDishEditIngredientsCorrect(dishIndex);

    }

    @Test
    @DisplayName("2.2. Проверяем изменение состава в детальной карточке блюда")
    public void isDishMenuChangingIngredientsCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItemsName,dishNameAndIngredients,"ingredients");

    }

    @Test
    @DisplayName("2.3. Редактируем вес и его количество")
    public void isDishEditWeightAndAmountCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        weightAndAmountAfterEditing = menu.isDishEditWeightAndAmountCorrect(dishIndex);

    }

    @Test
    @DisplayName("2.4. Проверяем изменение веса блюда на столе и в детальной карточке блюда")
    public void isDishMenuChangingWeightAndAmountCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);

        menu.isChangingAppliedOnTable(dishMenuItemsWeightAndCalories,weightAndAmountAfterEditing);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItemsWeightAndCalories,weightAndAmountAfterEditing);

    }

    @Test
    @DisplayName("2.5. Редактируем калории")
    public void isDishEditCaloriesCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        calories = menu.isDishEditCaloriesCorrect(dishIndex);

    }

    @Test
    @DisplayName("2.6. Проверяем изменение калорий на столе и в детальной карточке блюда")
    public void isDishMenuChangingCaloriesCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);

        menu.isChangingAppliedOnTable(dishMenuItemsWeightAndCalories,calories);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItemsWeightAndCalories,calories);

    }
    @Test
    @DisplayName("2.7. Редактируем метку блюда")
    public void isDishEditMarkCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        mark = menu.isDishEditMarkCorrect(dishIndex).toUpperCase();

    }
    @Test
    @DisplayName("2.8. Проверяем изменение метки на столе и в детальной карточке блюда")
    public void isDishMenuChangingMarkCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);

        menu.isChangingAppliedOnTable(dishMenuItemsMark,mark);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItemsMark,mark);

    }

    @Test
    @DisplayName("2.9. Проверяем что цена совпадает на столе и в карточке блюда")
    public void isDishPriceCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        dishPrice = menu.getDishPrice(dishIndex);

        rootPage.switchBrowserTab(tapperBrowserTab);

        menu.isChangingAppliedOnTable(dishMenuItems,dishPrice);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItems,dishPrice);

    }

}