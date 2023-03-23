package tapper.tests;


import api.ApiRKeeper;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import support_personal_account.lock.Lock;
import support_personal_account.logsAndPermissions.LogsAndPermissions;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.Telegram;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.AdminBaseTest;
import layout_screen_compare.ScreenShotComparison;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static data.Constants.MOBILE_IMAGE_PIXEL_SIZE;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.*;


@Epic("Debug")
@DisplayName("E2E")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class Debug extends AdminBaseTest {

    static Response rsGetOrder;
    static Response rsFillingOrder;
    static String visit;
    static String guid;
    static String uni;
    static String orderType;
    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Lock lock = new Lock();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    Telegram telegram = new Telegram();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    LogsAndPermissions logsAndPermissions = new LogsAndPermissions();

    ScreenShotComparison screenShotComparison = new ScreenShotComparison();


    //  <---------- Tests ---------->


    @Test
    public void screenCompare() throws IOException {


        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        rootPage.forceWait(10000);
        ScreenShotComparison.makeOriginalScreenshot("personal_account");

        rootPage.openPage(STAGE_RKEEPER_TABLE_333);
        rootPage.forceWait(10000);
        ScreenShotComparison.makeImageDiff("personal_account",5, MOBILE_IMAGE_PIXEL_SIZE);


    }


}
