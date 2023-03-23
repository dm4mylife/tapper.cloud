package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.attributeMatching;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_333;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.TapperTable.RootPage.DishList.orderMenuContainer;
import static data.selectors.TapperTable.RootPage.Menu.*;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;

@Order(112)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Смена аватарки блюда")
@DisplayName("Смена имени категории и позиции, проверка на столе")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_2_EditImageDishTest extends AdminBaseTest {

    static String imageUrl;
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
        rootPage.switchBrowserTab(adminBrowserTab);

    }
    @Test
    @DisplayName("1.5. Удаляем текущую аватарку блюда если есть")
    public void deleteDishImage() {

        menu.deleteDishImage(dishIndex);

    }
    @Test
    @DisplayName("1.6. Загружаем новую аватарку блюда")
    public void uploadImageFile() {

        imageUrl = menu.uploadImageFile(dishIndex);

    }
    @Test
    @DisplayName("1.7. Проверка изображения на столе")
    public void isChangingAppliedOnTable() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);

        rootPage.isElementVisibleDuringLongTime(appFooterMenuIcon,10);
        rootPage.click(appFooterMenuIcon);
        menu.isDownloadedImageCorrectOnTable(imageUrl);

    }

}