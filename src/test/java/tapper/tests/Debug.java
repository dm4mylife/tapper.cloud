package tapper.tests;


import admin_personal_account.AdminAccount;
import admin_personal_account.waiters.Waiters;
import api.ApiRKeeper;
import common.BaseActions;
import api.MailByApi;
import data.Constants;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import junit.framework.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import support_personal_account.lock.Lock;
import support_personal_account.logs_and_permissions.LogsAndPermissions;
import tapper_table.*;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import total_personal_account_actions.AuthorizationPage;
import waiter_personal_account.Waiter;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;

import static data.Constants.*;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.AdminPersonalAccount.Waiters.backToPreviousPage;


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

    AdminAccount adminAccount = new AdminAccount();
    YandexPage yandexPage = new YandexPage();

    Waiter waiter = new Waiter();
    Waiters waiters = new Waiters();


    //  <---------- Tests ---------->


    @Test
    public void screenCompare() throws IOException, MessagingException {




    }

}
