package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.HashMap;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.TapperTable.RootPage.Menu.*;


@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Смена имени категории и позиции, проверка на столе")
@DisplayName("Смена имени категории и позиции, проверка на столе")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EditingDishCorrectTest extends PersonalAccountTest {

    static String categoryNameAfterEditing;
    static String dishNameAfterEditing;
    static String weightAndAmountAfterEditing;
    static HashMap<String,String> dishNameAndDescription;
    static HashMap<String,String> dishNameAndIngredients;
    static String dishPrice;
    static String calories;
    static String mark;
    static int categoryIndex = 0;
    static int dishIndex = 0;
    static int adminBrowserTab = 0;

    static int tapperBrowserTab = 1;

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
    public void goToMenu() {

        menu.goToMenuCategory();

    }

    @Test
    @Order(3)
    @DisplayName("Делаем видимым категорию, блюдо и переключатель если отключены")
    void activateFirstCategoryAndActivateShowGuestMenu() {

        menu.activateCategoryAndDishAndActivateShowGuestMenuByIndex(categoryIndex,dishIndex);

    }

    @Test
    @Order(4)
    @DisplayName("Проверяем изменения на столе")
    void openUrl() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);

    }

    @Test
    @Order(5)
    @DisplayName("Редактируем имя категории")
    void editCategoryName() {

        rootPage.switchBrowserTab(adminBrowserTab);
        categoryNameAfterEditing = menu.isCategoryEditNameCorrect(categoryIndex);

    }

    @Test
    @Order(6)
    @DisplayName("Проверяем изменение категории блюда на столе")
    void isCategoryMenuChangingNameCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);
        menu.isChangingAppliedOnTable(categoryMenuItems,categoryNameAfterEditing);

    }

    @Test
    @Order(7)
    @DisplayName("Редактируем имя позиции")
    void isDishEditDishNameCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        dishNameAfterEditing = menu.isDishEditNameByGuestCorrect(dishIndex);

    }

    @Test
    @Order(8)
    @DisplayName("Проверяем изменение имени блюда на столе и в детальной карточке блюда")
    void isDishMenuChangingNameCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);

        menu.isChangingAppliedOnTable(dishMenuItemsName,dishNameAfterEditing);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItemsName,dishNameAfterEditing);

    }

    @Test
    @Order(9)
    @DisplayName("Редактируем описание позиции")
    void isDishEditDescriptionCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        dishNameAndDescription = menu.isDishEditDescriptionCorrect(dishIndex);

    }

    @Test
    @Order(10)
    @DisplayName("Проверяем изменение описания в детальной карточке блюда")
    void isDishMenuChangingDescriptionCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItems,dishNameAndDescription,"description");

    }

    @Test
    @Order(11)
    @DisplayName("Редактируем состав позиции")
    void isDishEditIngredientsCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        dishNameAndIngredients = menu.isDishEditIngredientsCorrect(dishIndex);

    }

    @Test
    @Order(12)
    @DisplayName("Проверяем изменение состава в детальной карточке блюда")
    void isDishMenuChangingIngredientsCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItemsName,dishNameAndIngredients,"ingredients");

    }

    @Test
    @Order(13)
    @DisplayName("Редактируем вес и его количество")
    void isDishEditWeightAndAmountCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        weightAndAmountAfterEditing = menu.isDishEditWeightAndAmountCorrect(dishIndex);

    }

    @Test
    @Order(14)
    @DisplayName("Проверяем изменение веса блюда на столе и в детальной карточке блюда")
    void isDishMenuChangingWeightAndAmountCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);

        menu.isChangingAppliedOnTable(dishMenuItemsWeightAndCalories,weightAndAmountAfterEditing);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItemsWeightAndCalories,weightAndAmountAfterEditing);

    }

    @Test
    @Order(15)
    @DisplayName("Редактируем калории")
    void isDishEditCaloriesCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        calories = menu.isDishEditCaloriesCorrect(dishIndex);

    }

    @Test
    @Order(16)
    @DisplayName("Проверяем изменение калорий на столе и в детальной карточке блюда")
    void isDishMenuChangingCaloriesCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);

        menu.isChangingAppliedOnTable(dishMenuItemsWeightAndCalories,calories);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItemsWeightAndCalories,calories);

    }
    @Test
    @Order(17)
    @DisplayName("Редактируем метку блюда")
    void isDishEditMarkCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        mark = menu.isDishEditMarkCorrect(dishIndex).toUpperCase();

    }
    @Test
    @Order(18)
    @DisplayName("Проверяем изменение метки на столе и в детальной карточке блюда")
    void isDishMenuChangingMarkCorrect() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);

        menu.isChangingAppliedOnTable(dishMenuItemsMark,mark);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItemsMark,mark);

    }

    @Test
    @Order(19)
    @DisplayName("Проверяем что цена совпадает на столе и в карточке блюда")
    void isDishPriceCorrect() {

        rootPage.switchBrowserTab(adminBrowserTab);
        dishPrice = menu.getDishPrice(dishIndex);

        rootPage.switchBrowserTab(tapperBrowserTab);

        menu.isChangingAppliedOnTable(dishMenuItems,dishPrice);
        menu.isChangingAppliedOnTableInDishCard(dishMenuItems,dishPrice);

    }

}