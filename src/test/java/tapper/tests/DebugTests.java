package tapper.tests;

import api.ApiRKeeper;

import common.BaseActions;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;
import tests.BaseTest;

import java.math.BigDecimal;

import static com.codeborne.selenide.Selenide.$;
import static constants.Constant.TestData.TEST_ROOT_URL;
import static constants.Constant.TestData.TEST_ROOT_URL2;
import static constants.Selectors.RootPage.serviceCharge;

@Disabled
@Epic("Претестовые для дебаггинга")
@DisplayName("Претестовые для дебаггинга")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DebugTests extends BaseTest {

    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();


    //  <---------- Tests ---------->


    @Disabled // toDo доделать
    @Test
    @DisplayName("тестовый для дебага сквозный")
    public void firstE2E() {

        baseActions.openPage(TEST_ROOT_URL2);
        rootPage.isDishListVisible();
        rootPage.isPriceInWalletCorrectWithTotalPay();
        double totalSum = rootPage.countAllPricesInMenu();
        System.out.println("Total sum from countAllPrices " + totalSum);
        rootPage.checkIfTotalSumInDishesMatchWithTotalPay(totalSum);
        rootPage.checkIfTipsPercentCorrectWithTotalSumWithoutServiceCharge(totalSum);


    }

    @Disabled
    @Test
    @DisplayName("свертка чаевых")
    public void api() {

        baseActions.openPage(TEST_ROOT_URL);
        rootPage.isDishListVisible();
        baseActions.forceWait(3000L);
        rootPage.checkIfAllTipsOptionsAreCorrectWithTotalSumWithServiceCharge();


    }

    @Test
    public void test() {

        double sh = 115.332;
        double sum = 6342;

        double sumsum = sh+sum;
        double newdb = Math.floor(sumsum*100) /100.0;
        System.out.println(sumsum);
        System.out.println(newdb);

    }


}
