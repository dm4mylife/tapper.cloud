package tapper.tests.waiter_personal_account;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;
import waiter_personal_account.Waiter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.*;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_PASSWORD;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.waiterImage;


@Epic("Личный кабинет официант ресторана")
@Feature("Проверка всех элементов, смены имени, телеграмма, пароля, загрузка изображений, сверка со столом")
@DisplayName("Проверка всех элементов, смены имени, телеграмма, пароля, загрузка изображений, сверка со столом")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiterName = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    int adminTab = 0;
    int tapperTableTab = 1;
    static String guid;
    static int amountDishesForFillingOrder = 3;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiter waiter = new Waiter();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @Order(1)
    @DisplayName("Создание заказа на кассе")
    void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode,
                waiterName, apiUri,dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        authorizationPage.authorizationUser(WAITER_LOGIN_EMAIL, WAITER_PASSWORD);

    }
    @Test
    @Order(2)
    @DisplayName("Проверка всех элементов в профиле")
    void isWaiterProfileCorrect() {

        waiter.isWaiterProfileCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Удаление фотографии в админке")
    void deleteWaiterImage() {

        waiter.deleteWaiterImage();

    }

    @Test
    @Order(4)
    @DisplayName("Проверяем что удаленная фотография не присутствует на столе")
    void checkDownloadedWaiterImageOnTableNotExists() {

        rootPageNestedTests.openNewTabAndSwitchTo(tableUrl);
        rootPage.isElementInvisible(waiterImage);

    }

    @Test
    @Order(5)
    @DisplayName("Загрузка аватарки официанта в админке")
    void downloadWaiterImage() {

        rootPage.switchBrowserTab(adminTab);
        waiter.downloadWaiterImage();

    }

    @Test
    @Order(6)
    @DisplayName("Проверка фотографии на столе")
    void checkDownloadedWaiterImageOnTable() {

        rootPage.switchBrowserTab(tapperTableTab);
        rootPage.refreshPage();
        rootPage.isTableHasOrder();
        waiter.checkDownloadedWaiterImageOnTable();

    }

    @Test
    @Order(7)
    @DisplayName("Смена имени официанта в админке")
    void changeWaiterName() {

        rootPage.switchBrowserTab(adminTab);
        waiter.changeWaiterName();

    }

    @Test
    @Order(8)
    @DisplayName("Проверка измененного имени на столе")
    void checkChangedNameOnTable() {

        rootPage.switchBrowserTab(tapperTableTab);
        rootPage.refreshPage();
        rootPage.isTableHasOrder();
        waiter.checkChangedNameOnTable();

    }

    @Test
    @Order(9)
    @DisplayName("Возвращаем старое имя")
    void setNameToDefault() {

        rootPage.switchBrowserTab(adminTab);
        waiter.setNameToDefault();

    }

    @Test
    @Order(10)
    @DisplayName("Смена пароля официанта")
    void changeWaiterPassword() {

        waiter.changeWaiterPassword();

    }

}
