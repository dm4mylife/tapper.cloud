package tapper.tests.waiter_personal_account;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;
import waiter_personal_account.Waiter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_PASSWORD;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.waiterImage;

@Order(140)
@Epic("Личный кабинет официант ресторана")
@Feature("Проверка всех элементов, смены имени, телеграмма, пароля, загрузка изображений, сверка со столом")
@DisplayName("Проверка всех элементов, смены имени, телеграмма, пароля, загрузка изображений, сверка со столом")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _14_0_TotalTest extends AdminBaseTest {

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
    @DisplayName("1.0 Создание заказа на кассе")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_555,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_555_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        authorizationPage.authorizationUser(WAITER_LOGIN_EMAIL, WAITER_PASSWORD);

    }
    @Test
    @DisplayName("1.1. Проверка всех элементов в профиле")
    public void isWaiterProfileCorrect() {

        waiter.isWaiterProfileCorrect();

    }

    @Test
    @DisplayName("1.2. Удаление фотографии в админке")
    public void deleteWaiterImage() {

        waiter.deleteWaiterImage();

    }

    @Test
    @DisplayName("1.3. Проверяем что удаленная фотография не присутствует на столе")
    public void checkDownloadedWaiterImageOnTableNotExists() {

        rootPageNestedTests.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);
        rootPage.isElementInvisible(waiterImage);
        System.out.println("Изображение удалено на столе");

    }

    @Test
    @DisplayName("1.4. Загрузка аватарки официанта в админке")
    public void downloadWaiterImage() {

        rootPage.switchBrowserTab(adminTab);
        waiter.downloadWaiterImage();

    }

    @Test
    @DisplayName("1.5. Проверка фотографии на столе")
    public void checkDownloadedWaiterImageOnTable() {

        rootPage.switchBrowserTab(tapperTableTab);
        rootPage.refreshPage();
        rootPage.isDishListNotEmptyAndVisible();
        waiter.checkDownloadedWaiterImageOnTable();

    }

    @Test
    @DisplayName("1.6. Смена имени официанта в админке")
    public void changeWaiterName() {

        rootPage.switchBrowserTab(adminTab);
        waiter.changeWaiterName();

    }

    @Test
    @DisplayName("1.7. Проверка измененного имени на столе")
    public void checkChangedNameOnTable() {

        rootPage.switchBrowserTab(tapperTableTab);
        rootPage.refreshPage();
        rootPage.isDishListNotEmptyAndVisible();
        waiter.checkChangedNameOnTable();

    }

    @Test
    @DisplayName("1.8. Возвращаем старое имя")
    public void setNameToDefault() {

        rootPage.switchBrowserTab(0);
        waiter.setNameToDefault();

    }

    @Test
    @DisplayName("1.9. Смена логина телеграма официанта")
    public void changeTelegramLogin() {

        waiter.changeTelegramLogin();

    }

    @Test
    @DisplayName("2.0. Смена пароля официанта")
    public void changeWaiterPassword() {

        waiter.changeWaiterPassword();

    }

}
