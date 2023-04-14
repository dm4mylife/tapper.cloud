package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$$;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.TapperTable.RootPage.Menu.dishMenuPhotoSelector;


@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Сортировка меню, отображение всех категорий и блюд в таппере")
@DisplayName("Сортировка меню, отображение всех категорий и блюд в таппере")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SortingTest extends PersonalAccountTest {

    static ArrayList<String> dishListWithActiveCheckboxShowForGuest;
    static LinkedHashMap<String, Map<String,String>> adminMenuData;
    static LinkedHashMap<String,Map<String,String>> tapperMenuData;
    static int adminTab = 0;
    static int tapperTable = 1;
    static int categoryIndex = 0;
    static int dishIndex = 0;

    BaseActions baseActions = new BaseActions();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    RootPage rootPage = new RootPage();
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
        menu.activateNonAutoCategoryAndDishAndActivateShowGuestMenuByIndex(categoryIndex,dishIndex);

    }

    @Test
    @Order(3)
    @DisplayName("Проверяем что количество блюд в категории соответствует заголовку")
    void matchDishesItemSizeWithCategorySize() {

        menu.matchDishesItemSizeWithCategorySize();

    }

    @Test
    @Order(4)
    @DisplayName("Проверяем работу 'Показать активные для гостя'")
    void showActiveCategoryCorrect() {

        dishListWithActiveCheckboxShowForGuest = menu.showActiveCategoryCorrect();

        baseActions.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);

        rootPage.clickOnMenuInFooter();

        menu.matchCategoryListWithTapperMenu(dishListWithActiveCheckboxShowForGuest);

    }

    @Test
    @Order(5)
    @DisplayName("Выбираем рандомные категории и блюда, делаем видимыми для гостя")
    void chooseRandomCategoryAndDishes() {

        rootPage.switchBrowserTab(adminTab);
        adminMenuData = menu.saveMenuData();

    }

    @Test
    @Order(6)
    @DisplayName("Переключаемся на стол и сравниваем что категории и меню корректны'")
    void goToTapper() {

        menu.switchTabAndRefreshPage(tapperTable);

        rootPage.clickOnMenuInFooter();

        tapperMenuData = rootPage.saveTapperMenuData();
        Assertions.assertEquals(adminMenuData,tapperMenuData);

    }

    @Test
    @Order(7)
    @DisplayName("Проверяем корректность изображений")
    void checkImages() {

        if ($$(dishMenuPhotoSelector).size() != 0)
            baseActions.isImageCorrect(dishMenuPhotoSelector,
                    "Изображения блюд не корректны, или битые");

    }

}