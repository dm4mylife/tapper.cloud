package tapper.tests.admin_personal_account.customization;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import total_personal_account_actions.AuthorizationPage;
import admin_personal_account.customization.Customization;
import tapper_table.RootPage;
import tests.BaseTest;

import static data.Constants.TestData.*;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.selectors.AdminPersonalAccount.Customization.wifiTab;
import static data.selectors.TapperTable.Common.wiFiIcon;

@Order(150)
@Epic("Личный кабинет администратора ресторана")
@Feature("Кастомизация")
@Story("Проверка вайфая")
@DisplayName("Проверка вайфая")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _15_1_wiFiTest extends BaseTest {

    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Customization customization = new Customization();


    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

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
        customization.activateWifiIfDeactivated();
        customization.isWiFiTabCorrect();
        customization.setWifiConfiguration(AdminPersonalAccount.TEST_WIFI_NETWORK_NAME, AdminPersonalAccount.TEST_WIFI_NETWORK_PASSWORD);

    }

    @Test
    @DisplayName("1.4. Активируем вайфай, прописываем данные")
    public void setWifiConfiguration() {

        customization.setWifiConfiguration(AdminPersonalAccount.TEST_WIFI_NETWORK_NAME, AdminPersonalAccount.TEST_WIFI_NETWORK_PASSWORD);

    }

    @Test
    @DisplayName("1.5. Переходим на стол и проверяем что он активировался")
    public void setMsgAsTextPattern() {

        rootPage.openNewTabAndSwitchTo(TapperTable.STAGE_RKEEPER_TABLE_333);
        rootPage.closeHintModal();
        rootPage.checkWiFiOnTapperTable(AdminPersonalAccount.TEST_WIFI_NETWORK_NAME, AdminPersonalAccount.TEST_WIFI_NETWORK_PASSWORD);

    }

    @Test
    @DisplayName("1.6. Возвращаемся в админку, отключаем его, проверяем что на столе корректно")
    public void isChangedTextPatternCorrectOnTable() {

        Selenide.switchTo().window(0);
        customization.deactivateWifiIfActivated();

        Selenide.switchTo().window(1);
        rootPage.refreshPage();

        wiFiIcon.shouldBe(Condition.hidden);

    }

}
