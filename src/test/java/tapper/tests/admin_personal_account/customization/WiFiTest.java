package tapper.tests.admin_personal_account.customization;

import admin_personal_account.customization.Customization;
import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.*;
import static com.codeborne.selenide.Condition.hidden;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.AdminPersonalAccount.Customization.wifiTab;
import static data.selectors.AdminPersonalAccount.Profile.pagePreloader;
import static data.selectors.TapperTable.Common.wiFiIcon;


@Epic("Личный кабинет администратора ресторана")
@Feature("Кастомизация")
@Story("Проверка вайфая")
@DisplayName("Проверка вайфая")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WiFiTest extends PersonalAccountTest {

    int adminTab = 0;
    int tappetTab = 1;
    static int amountDishesForFillingOrder = 3;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Customization customization = new Customization();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();


    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу кастомизации, проверка всех элементов")
    void goToCustomizationCategory() {

        customization.goToCustomizationCategory();
        customization.isCustomizationCategoryCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Переходим на вайфай, проверяем формы")
    void isWifiTabCorrect() {

        rootPage.click(wifiTab);
        pagePreloader.shouldBe(hidden, Duration.ofSeconds(5));
        customization.isWiFiTabCorrect();

    }

    @Test
    @Order(4)
    @DisplayName("Активируем вайфай, прописываем имя сети и пароль")
    void setWifiConfiguration() {

        customization.activateWifiIfDeactivated();
        customization.setWifiConfiguration(TEST_WIFI_NETWORK_NAME, TEST_WIFI_NETWORK_PASSWORD);

    }

    @Test
    @Order(5)
    @DisplayName("Переходим на стол, проверяем что есть функционал вайфая, его поля и что данные совпадают с админкой")
    void setMsgAsTextPattern() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_555, WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_555_ID);

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);
        rootPage.isTableHasOrder();

        rootPage.checkWiFiOnTapperTable(TEST_WIFI_NETWORK_NAME, TEST_WIFI_NETWORK_PASSWORD);

    }

    @Test
    @Order(6)
    @DisplayName("Проверяем вайфай без пароля")
    void saveWithoutPassword() {

        rootPage.switchBrowserTab(adminTab);
        customization.setWifiConfigurationWithoutPassword(TEST_WIFI_NETWORK_NAME);
        rootPage.switchBrowserTab(tappetTab);
        rootPage.refreshPage();
        rootPage.isTableHasOrder();
        rootPage.checkWiFiOnTapperTableWithoutPassword(TEST_WIFI_NETWORK_NAME);

    }

    @Test
    @Order(7)
    @DisplayName("Возвращаемся в админку, отключаем его, проверяем что на столе корректно")
    void isChangedTextPatternCorrectOnTable() {

        rootPage.switchBrowserTab(adminTab);
        customization.deactivateWifiIfActivated();

        rootPage.switchBrowserTab(tappetTab);
        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        wiFiIcon.shouldBe(hidden);

    }

}
