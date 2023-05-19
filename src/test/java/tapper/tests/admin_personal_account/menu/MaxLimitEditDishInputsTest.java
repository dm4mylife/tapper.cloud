package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.HashMap;

import static com.codeborne.selenide.Condition.matchText;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.AdminPersonalAccount.Menu.menuDishItemsEditButtons;
import static data.selectors.TapperTable.RootPage.Menu.*;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;


@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Проверка максимальных значений в полях и их отображение на столе")
@DisplayName("Проверка максимальных значений в полях и их отображение на столе")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MaxLimitEditDishInputsTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiterName = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    int adminTab = 0;
    static int categoryIndex = 0;
    static int dishIndex = 0;


    static HashMap<String,String> previousDishData;

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
        menu.isMenuCategoryCorrect();
        menu.deactivateAllMenuCategory();
        menu.activateNonAutoCategoryAndDishAndActivateShowGuestMenuByIndex(categoryIndex,dishIndex);

    }

    @Test
    @Order(3)
    @DisplayName("Заполняем поля максимальными значениями")
    void isInputsLimitCorrect() {

        BaseActions.click(menuDishItemsEditButtons.get(dishIndex));
        previousDishData = menu.isInputsLimitCorrect();

    }

    @Test
    @Order(4)
    @DisplayName("Переходим на стол")
    void checkMenuIconAndContainerInTable() {

        rootPage.openNewTabAndSwitchTo(tableUrl);

        rootPage.isElementVisibleDuringLongTime(appFooterMenuIcon,10);
        BaseActions.click(appFooterMenuIcon);
        rootPage.isElementVisible(menuDishContainer);
        rootPage.isElementsCollectionVisible(dishMenuItemsName);

    }

    @Test
    @Order(5)
    @DisplayName("Проверяем изменение имени,веса,количества,состава")
    void isDishMenuChangingWeightAndAmountCorrect() {

        for (SelenideElement selenideElement : dishMenuItemsName) {

            if (selenideElement.getText().matches(previousDishData.get("previousNameByGuest"))) {

                BaseActions.click(selenideElement);
                break;

            }

        }

        menuDishNameInDetailCard.shouldHave(matchText(previousDishData.get("previousNameByGuest")));
        menuDishAmountDetailCard.shouldHave(matchText(previousDishData.get("previousAmount")));
        menuDishAmountDetailCard.shouldHave(matchText(previousDishData.get("previousCalories")));
        menuDishIngredientsDetailCard.shouldHave(matchText(previousDishData.get("previousIngredients")));

    }

    @Test
    @Order(6)
    @DisplayName("Удаляем новые значения в полях и подставляем старые")
    void setDefaultInputsValue() {

        rootPage.switchBrowserTab(adminTab);
        BaseActions.click(menuDishItemsEditButtons.get(dishIndex));
        menu.setDefaultInputsValue(previousDishData);

    }

}
