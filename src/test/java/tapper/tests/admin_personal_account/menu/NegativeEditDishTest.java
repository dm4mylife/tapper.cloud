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


@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Негативные сценарии")
@DisplayName("Редактирование полей и проверка что данные сохраняются\\не сохраняются при особых условиях")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NegativeEditDishTest extends PersonalAccountTest {

    static int categoryIndex = 0;
    static int adminBrowserTab = 0;
    static int dishIndex = 0;

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

        menu.activateCategoryAndDishAndActivateShowGuestMenuByIndex(categoryIndex,dishIndex);

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
    @DisplayName("Смена имени и закрытие формы редактирования не сохранит результат в блюде")
    void deleteDishImage() {

        menu.isDishEditNameByGuestCorrect_Negative(dishIndex);

    }

    @Test
    @Order(6)
    @DisplayName("Проверка закрытия окна редактирования блюда без внесения изменений")
    void uploadImageFile() {

        menu.isPopupClosedWithoutConfirmationAfterNotEditingData(dishIndex);

    }

    @Test
    @Order(7)
    @DisplayName("Проверка на сохранения изменений в позиции после обновления страницы")
    void isChangingAppliedOnTable() {

        menu.isChangingSavedAfterPageReload(dishIndex);

    }
    @Test
    @Order(8)
    @DisplayName("Проверка что вес и единица измерения выдают корректные ошибки при пустых полях")
    void isAmountAndMeasureUnitInputsCorrectError() {

        menu.isAmountAndMeasureUnitInputsCorrectError(dishIndex);

    }

}