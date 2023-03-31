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

@Order(116)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Проверка что если меню отключено, то на столе отображается кнопка и информация")
@DisplayName("Проверка что если меню отключено, то на столе отображается кнопка и информация")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_6_EmptyMenuTest extends PersonalAccountTest {

    int adminTab = 0;
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
        menu.isMenuCategoryCorrect();

    }

    @Test
    @DisplayName("1.3. Выключаем меню для гостей если включено")
    public void deactivateMenu() {

        menu.deactivateShowGuestSliderIfActivated();

    }

    @Test
    @DisplayName("1.4. Переходим на стол и проверяем функционал меню при отключенном в админке")
    public void checkMenuIconAndContainerInTable() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);
        rootPage.clickOnMenuInFooter();


        rootPage.emptyMenuCorrect();

    }

    @Test
    @DisplayName("1.5. Возвращаемся в админку и включаем обратно меню")
    public void deactivateMenuForGuest() {

        rootPage.switchBrowserTab(adminTab);
        menu.activateShowActiveForGuest();

    }

}
