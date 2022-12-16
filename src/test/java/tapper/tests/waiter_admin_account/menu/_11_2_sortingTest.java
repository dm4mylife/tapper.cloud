package tapper.tests.waiter_admin_account.menu;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import constants.TapperTableSelectors;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_admin.AuthorizationPage;
import tapper_admin.menu.Menu;
import tapper_table.RootPage;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$$;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_WAITER_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_WAITER_PASSWORD;
import static constants.TapperTableSelectors.RootPage.Menu.menuDishPhotos;
import static constants.TapperTableSelectors.RootPage.TapBar.appFooterMenuIcon;

@Order(112)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@DisplayName("Сортировка меню, отображение всех категорий и блюд в таппере")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_2_sortingTest extends BaseTest {

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

        authorizationPage.authorizationUser(ADMIN_WAITER_LOGIN_EMAIL, ADMIN_WAITER_PASSWORD);

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

        baseActions.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_3);

        Selenide.switchTo().window(1);

        baseActions.click(appFooterMenuIcon);

        menu.matchCategoryListWithTapperMenu(dishListWithActiveCheckboxShowForGuest);

    }

    @Test
    @DisplayName("1.5. Выбираем рандомные категории и блюда, делаем видимыми для гостя")
    public void chooseRandomCategoryAndDishes() {

        adminMenuData = menu.saveMenuData();

    }

    @Test
    @DisplayName("1.6. Переключаемся на стол и сравниваем что категории и меню корректны'")
    public void goToTapper() {

        baseActions.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_3);
        Selenide.switchTo().window(1);
        baseActions.click(appFooterMenuIcon);

        tapperMenuData = rootPage.saveTapperMenuData();
        Assertions.assertEquals(adminMenuData,tapperMenuData);

    }

    @Test
    @DisplayName("1.7. Проверяем корректность изображений")
    public void checkImages() {

        if ($$(menuDishPhotos).size() != 0) {

            baseActions.isImageCorrect(menuDishPhotos);

        }

    }

}
