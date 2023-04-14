package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;


@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Смена аватарки блюда")
@DisplayName("Смена аватарки блюда")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EditImageDishTest extends PersonalAccountTest {

    static String imageUrl;
    static int categoryIndex = 0;
    static int adminBrowserTab = 0;
    static int dishIndex = 0;
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
    void goToMenu() {

        menu.goToMenuCategory();

    }
    @Test
    @Order(3)
    @DisplayName("Делаем видимым категорию, блюдо и переключатель если отключены")
    void activateFirstCategoryAndActivateShowGuestMenu() {

        menu.activateNonAutoCategoryAndDishAndActivateShowGuestMenuByIndex(categoryIndex,dishIndex);

    }
    @Test
    @Order(4)
    @DisplayName("Проверяем изменения на столе")
    void openUrl() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);
        rootPage.switchBrowserTab(adminBrowserTab);

    }
    @Test
    @Order(5)
    @DisplayName("Удаляем текущую аватарку блюда если есть")
    void deleteDishImage() {

        menu.deleteDishImage(dishIndex);

    }
    @Test
    @Order(6)
    @DisplayName("Загружаем новую аватарку блюда")
    void uploadImageFile() {

        imageUrl = menu.uploadImageFile(dishIndex);

    }
    @Test
    @Order(7)
    @DisplayName("Проверка изображения на столе")
    void isChangingAppliedOnTable() {

        menu.switchTabAndRefreshPage(tapperBrowserTab);

        rootPage.isElementVisibleDuringLongTime(appFooterMenuIcon,10);
        rootPage.click(appFooterMenuIcon);
        menu.isDownloadedImageCorrectOnTable(imageUrl);

    }

}