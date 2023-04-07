package tapper.tests;


import admin_personal_account.AdminAccount;
import api.ApiRKeeper;
import com.codeborne.selenide.LocalStorage;
import com.codeborne.selenide.LocalStorageConditions;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import common.BaseActions;
import common.JavaxMail;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import support_personal_account.lock.Lock;
import support_personal_account.logs_and_permissions.LogsAndPermissions;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.Telegram;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static api.ApiData.EndPoints.createOrder;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_AUTHORIZATION_STAGE_URL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_PROFILE_STAGE_URL;
import static data.selectors.AuthAndRegistrationPage.RegistrationPage.allNecessaryInputsForFilling;
import static io.restassured.RestAssured.given;


@Epic("Debug")
@DisplayName("E2E")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class Debug extends AdminAccount {

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

    JavaxMail javaxMail = new JavaxMail();
    //  <---------- Tests ---------->


    @Test
    public void screenCompare() throws IOException {




    }

}
