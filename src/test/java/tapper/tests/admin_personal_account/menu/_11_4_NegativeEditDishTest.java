package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_333;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.AdminPersonalAccount.Menu.menuDishItemsNames;

@Order(114)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Негативные сценарии")
@DisplayName("Редактирование полей и проверка что данные сохраняются\\не сохраняются при особых условиях")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_4_NegativeEditDishTest extends AdminBaseTest {

    static int categoryIndex = 0;
    static int adminBrowserTab = 0;
    static int dishIndex = 0;

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
    @DisplayName("1.5. Смена имени и закрытие формы редактирования не сохранит результат в блюде")
    public void deleteDishImage() {

        menu.isDishEditNameByGuestCorrect_Negative(dishIndex);

    }

    @Test
    @DisplayName("1.6. Проверка закрытия окна редактирования блюда без внесения изменений")
    public void uploadImageFile() {

        menu.isPopupClosedWithoutConfirmationAfterNotEditingData(dishIndex);

    }

    @Test
    @DisplayName("1.7. Проверка на сохранения изменений в позиции после обновления страницы")
    public void isChangingAppliedOnTable() {

        menu.isChangingSavedAfterPageReload(dishIndex);

    }
    @Test
    @DisplayName("1.8. Проверка что вес и единица измерения выдают корректные ошибки при пустых полях")
    public void isAmountAndMeasureUnitInputsCorrectError() {

        menu.isAmountAndMeasureUnitInputsCorrectError(dishIndex);

    }

}