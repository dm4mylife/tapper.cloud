package tapper.tests.waiter_admin_account.menu;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import tapper_admin.AuthorizationPage;
import tapper_admin.menu.Menu;
import tests.BaseTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.DragAndDropOptions.usingActions;
import static com.codeborne.selenide.DragAndDropOptions.usingJavaScript;
import static com.codeborne.selenide.Selenide.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_WAITER_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_WAITER_PASSWORD;
import static constants.TapperAdminSelectors.RKeeperAdmin.Menu.categoryDishItems;
import static constants.TapperTableSelectors.RootPage.Menu.categoryMenuItems;
import static constants.TapperTableSelectors.RootPage.Menu.dishMenuItems;
import static constants.TapperTableSelectors.RootPage.TapBar.appFooterMenuIcon;

@Order(113)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@DisplayName("Перетаскивание")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_3_ChangeCategoryAndDishesTest extends BaseTest {

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

        Configuration.browserSize = "1920x1080";

        authorizationPage.authorizationUser(ADMIN_WAITER_LOGIN_EMAIL, ADMIN_WAITER_PASSWORD);
        menu.goToMenuCategory();
        baseActions.forceWait(3000);

    }

}
