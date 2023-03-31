package tapper.tests.admin_personal_account.customization;

import admin_personal_account.customization.Customization;
import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.hidden;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.AdminPersonalAccount.Customization.wifiTab;
import static data.selectors.AdminPersonalAccount.Profile.pagePreloader;
import static data.selectors.TapperTable.Common.wiFiIcon;

@Order(151)
@Epic("Личный кабинет администратора ресторана")
@Feature("Кастомизация")
@Story("Проверка вайфая")
@DisplayName("Проверка вайфая")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _15_1_wiFiTest extends PersonalAccountTest {

    static String guid;
    static int amountDishesForFillingOrder = 3;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Customization customization = new Customization();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();


    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу кастомизации, проверка всех элементов")
    public void goToCustomizationCategory() {

        customization.goToCustomizationCategory();
        customization.isCustomizationCategoryCorrect();

    }

    @Test
    @DisplayName("1.3. Переходим на вайфай, проверяем формы")
    public void isWifiTabCorrect() {

        rootPage.click(wifiTab);
        pagePreloader.shouldBe(hidden, Duration.ofSeconds(5));
        customization.activateWifiIfDeactivated();
        customization.isWiFiTabCorrect();

    }

    @Test
    @DisplayName("1.4. Активируем вайфай, прописываем имя сети и пароль")
    public void setWifiConfiguration() {

        customization.setWifiConfiguration(TEST_WIFI_NETWORK_NAME, TEST_WIFI_NETWORK_PASSWORD);

    }

    @Test
    @DisplayName("1.5. Переходим на стол, проверяем что есть функционал вайфая, его поля и что данные совпадают с админкой")
    public void setMsgAsTextPattern() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_555,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_555_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);

        rootPage.isTableHasOrder();
        rootPage.checkWiFiOnTapperTable(TEST_WIFI_NETWORK_NAME, TEST_WIFI_NETWORK_PASSWORD);

    }

    @Test
    @DisplayName("1.6. Возвращаемся в админку, отключаем его, проверяем что на столе корректно")
    public void isChangedTextPatternCorrectOnTable() {

        rootPage.switchBrowserTab(0);
        customization.deactivateWifiIfActivated();

        rootPage.switchBrowserTab(1);
        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        wiFiIcon.shouldBe(hidden);

    }

}
