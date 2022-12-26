package tapper.tests.waiter_personal_account;


import api.ApiRKeeper;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_admin_personal_account.AuthorizationPage;
import tapper_table.ReviewPage;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tapper_waiter_personal_account.Waiter;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Constant.TestDataRKeeperAdmin.WAITER_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.WAITER_PASSWORD;
import static constants.selectors.TapperTableSelectors.RootPage.TipsAndCheck.waiterImage;

@Order(140)
@Epic("Личный кабинет администратора ресторана")
@Feature("Личный кабинет официанта")
@DisplayName("Проверка всех элементов, смены имени, телеграмма, пароля, загрузка изображений, сверка со столом")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _14_0_TotalTest extends BaseActions {


    ReviewPage reviewPage = new ReviewPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiter waiter = new Waiter();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1.0. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD));
        String visit = rs.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "10000"));

    }

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";
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

        openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_3);

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
        Selenide.refresh();

        isElementInvisible(waiterImage);

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

        openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

        rootPageNestedTests.clickPayment();

        best2PayPageNestedTests.typeDataAndPay();

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPage.clickOnFinishButton();

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
