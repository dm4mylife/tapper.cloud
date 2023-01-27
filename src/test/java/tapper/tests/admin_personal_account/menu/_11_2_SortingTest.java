package tapper.tests.admin_personal_account.menu;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import total_personal_account_actions.AuthorizationPage;
import admin_personal_account.menu.Menu;
import tapper_table.RootPage;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$$;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.selectors.TapperTable.RootPage.Menu.menuDishPhotosNotSelenide;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;

@Order(112)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Сортировка меню, отображение всех категорий и блюд в таппере")
@DisplayName("Сортировка меню, отображение всех категорий и блюд в таппере")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_2_SortingTest extends BaseTest {

    static ArrayList<String> dishListWithActiveCheckboxShowForGuest;
    static HashMap<String, Map<String,String>> adminMenuData;
    static HashMap<String,Map<String,String>> tapperMenuData;

    BaseActions baseActions = new BaseActions();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    RootPage rootPage = new RootPage();
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
    @DisplayName("1.3. Проверяем что количество блюд в категории соответствует заголовку")
    public void matchDishesItemSizeWithCategorySize() {

        menu.matchDishesItemSizeWithCategorySize();

    }

    @Test
    @DisplayName("1.4. Проверяем работу 'Показать активные для гостя'")
    public void showActiveCategoryCorrect() {

        dishListWithActiveCheckboxShowForGuest = menu.showActiveCategoryCorrect();

        baseActions.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_111);

        Selenide.switchTo().window(1);

        baseActions.click(appFooterMenuIcon);

        menu.matchCategoryListWithTapperMenu(dishListWithActiveCheckboxShowForGuest);

    }

    @Test
    @DisplayName("1.5. Выбираем рандомные категории и блюда, делаем видимыми для гостя")
    public void chooseRandomCategoryAndDishes() {


        Selenide.switchTo().window(0);
        adminMenuData = menu.saveMenuData();

    }

    @Test
    @DisplayName("1.6. Переключаемся на стол и сравниваем что категории и меню корректны'")
    public void goToTapper() {

        Selenide.switchTo().window(1);
        rootPage.refreshPage();
        baseActions.isElementVisibleDuringLongTime(appFooterMenuIcon,20);
        baseActions.forceWait(2000);

        baseActions.click(appFooterMenuIcon);

        tapperMenuData = rootPage.saveTapperMenuData();
        Assertions.assertEquals(adminMenuData,tapperMenuData);
        System.out.println("Категории и меню корректны, списки совпадают");

    }

    @Test
    @DisplayName("1.7. Проверяем корректность изображений")
    public void checkImages() {

        if ($$(menuDishPhotosNotSelenide).size() != 0) {

            baseActions.isImageCorrect(menuDishPhotosNotSelenide,"Изображения блюд не корректны, или битые");

        }

    }

}
