package tapper.tests.waiter_personal_account;


import api.ApiRKeeper;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import total_personal_account_actions.AuthorizationPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import waiter_personal_account.Waiter;
import tests.BaseTest;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_PASSWORD;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.waiterImage;

@Order(140)
@Epic("Личный кабинет официант ресторана")
@Feature("Проверка всех элементов, смены имени, телеграмма, пароля, загрузка изображений, сверка со столом")
@DisplayName("Проверка всех элементов, смены имени, телеграмма, пароля, загрузка изображений, сверка со столом")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _14_0_TotalTest extends BaseTest {

    static String visit;
    static String guid;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiter waiter = new Waiter();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1.0. Создание заказа в r_keeper и авторизация в админке ресторана")
    public void createAndFillOrder() {

        Configuration.browserSize = "1920x1080";
        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), AUTO_API_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "10000"));

        authorizationPage.authorizationUser(WAITER_LOGIN_EMAIL, WAITER_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Проверка всех элементов в профиле")
    public void isWaiterProfileCorrect() {

        waiter.isWaiterProfileCorrect();

    }

    @Test
    @DisplayName("1.3. Загрузка аватарки официанта в админке")
    public void downloadWaiterImage() {

        waiter.downloadWaiterImage();

    }

    @Test
    @DisplayName("1.4. Проверка фотографии на столе")
    public void checkDownloadedWaiterImageOnTable() {

        rootPageNestedTests.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_111);

        waiter.checkDownloadedWaiterImageOnTable();

    }

    @Test
    @DisplayName("1.5. Удаление фотографии в админке")
    public void deleteWaiterImage() {

        Selenide.switchTo().window(0);

        waiter.deleteWaiterImage();

    }

    @Test
    @DisplayName("1.6. Проверка удаленной фотографии на столе")
    public void isDeletedImageCorrectOnTable() {

        Selenide.switchTo().window(1);
        rootPage.refreshPage();

        rootPageNestedTests.isElementInvisible(waiterImage);

        Selenide.closeWindow();
        Selenide.switchTo().window(0);
        System.out.println("Изображение удалено на столе");

    }

    @Test
    @DisplayName("1.7. Смена имени официанта и проверка на столе")
    public void changeWaiterName() {

        waiter.changeWaiterName();
        waiter.checkChangedNameOnTable();
        waiter.setNameToDefault();
    }

    @Test
    @DisplayName("1.8. Смена логина телеграма официанта и проверка на столе")
    public void changeTelegramLogin() {

        waiter.changeTelegramLogin();

    }

    @Test
    @DisplayName("1.9. Смена пароля официанта")
    public void changeWaiterPassword() {

        waiter.changeWaiterPassword();


    }

    @Test
    @DisplayName("2.0. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.closeOrderByAPI(guid);

    }

/*
    @Test toDO  закоменчено. проверяем что в истории операции сменилось имя официанта
    @DisplayName("2.0. Переход на эквайринг, ввод данных, полная оплата")
    public void checkFullPayInAdmin() {

        Selenide.switchTo().window(0);

        adminAccount.logOut();

        openPage(R_KEEPER_ADMIN_AUTHORISATION_STAGE_URL);
        rootPage.forceWait(2000);
        authorizationPage.authorizeUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);


        operationsHistory.goToOperationsHistoryCategory();
        operationsHistory.isHistoryOperationsCorrect();

        adminOrderData = operationsHistory.saveAdminOrderData();
        rootPage.matchTapperOrderDataWithAdminOrderData(tapperOrderData,adminOrderData);

    }*/

}
