package tapper.tests.admin_personal_account.menu;

import admin_personal_account.menu.Menu;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.AdminBaseTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.HashMap;

import static com.codeborne.selenide.Condition.matchText;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.AdminPersonalAccount.Menu.*;
import static data.selectors.TapperTable.RootPage.DishList.orderContainer;
import static data.selectors.TapperTable.RootPage.DishList.orderMenuContainer;
import static data.selectors.TapperTable.RootPage.Menu.*;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;

@Order(115)
@Epic("Личный кабинет администратора ресторана")
@Feature("Меню")
@Story("Проверка максимальных значений в полях и их отображение на столе")
@DisplayName("Проверка максимальных значений в полях и их отображение на столе")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _11_5_MaxLimitEditDishInputsTest extends AdminBaseTest {

    int adminTab = 0;
    int tapperTableTab = 1;
    int dishIndex = 0;
    static String dishName;

    static HashMap<String,String> previousDishData;

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
        menu.isMenuCorrect();

    }

    @Test
    @DisplayName("1.3. Включаем меню для гостей")
    public void activateMenuForGuests() {

        menu.activateFirstCategoryAndDishInMenu();


    }

    @Test
    @DisplayName("1.4. Заполняем поля максимальными значениями")
    public void isInputsLimitCorrect() {

        rootPage.click(menuDishItemsEditButtons.get(dishIndex));
        previousDishData = menu.isInputsLimitCorrect();
        dishName = menuDishNameByGuest.get(dishIndex).getText();
        System.out.println(dishName);

    }

    @Test
    @DisplayName("1.5. Переходим на стол")
    public void checkMenuIconAndContainerInTable() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);

        rootPage.isElementVisibleDuringLongTime(appFooterMenuIcon,10);
        rootPage.click(appFooterMenuIcon);
        rootPage.isElementVisible(menuDishContainer);

    }

    @Test
    @DisplayName("1.6. Проверяем изменение имени,веса,количества,состава")
    public void isDishMenuChangingWeightAndAmountCorrect() {

        dishMenuItemsName.asFixedIterable().stream().forEach(element -> {

            if (element.getText().equals(dishName)) {

                rootPage.click(element);

            }

        });

        menuDishNameInDetailCard.shouldHave(matchText(previousDishData.get("previousNameByGuest")));
        menuDishAmountDetailCard.shouldHave(matchText(previousDishData.get("previousAmount")));
        menuDishAmountDetailCard.shouldHave(matchText(previousDishData.get("previousCalories")));
        menuDishIngredientsDetailCard.shouldHave(matchText(previousDishData.get("previousIngredients")));

    }

    @Test
    @DisplayName("1.7. Удаляем новые значения в полях и подставляем старые")
    public void setDefaultInputsValue() {

        rootPage.switchBrowserTab(adminTab);
        rootPage.click(menuDishItemsEditButtons.get(dishIndex));
        menu.setDefaultInputsValue(previousDishData);

    }

}
