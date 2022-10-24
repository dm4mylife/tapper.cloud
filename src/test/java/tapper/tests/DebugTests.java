package tapper.tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.RootPage;
import tests.BaseTest;

import static com.codeborne.selenide.Selenide.$;
import static constants.Selectors.RootPage.*;


@Epic("Debug")
@DisplayName("Debug")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DebugTests extends BaseTest {

    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();


    //  <---------- Tests ---------->
    @Disabled
    @Test
    @DisplayName("тестовый для дебага")
    public void test() {

        rootPage.openTapperLink();
        rootPage.clickOnPaymentButton();
        baseActions.forceWait(3000L);
        best2PayPage.isTestBest2PayUrl();


    }

    @Disabled
    @Test
    @DisplayName("тестовый для дебага")
    public void test2() {

        rootPage.openTapperLink();
        rootPage.isDishListVisible();
        int totalSumInMenu = rootPage.countAllPricesInMenu();

        baseActions.forceWait(1000l);




        rootPage.checkIfTipsPercentCorrectInTotalSum(totalSumInMenu);

        rootPage.isCounterInWalletCorrect(totalSumInMenu);

        int gg = 1;


    }

    @Test
    public void test3() {

        rootPage.openTapperLink();
        baseActions.forceWait(3000l);
        baseActions.scroll($("body"));
        baseActions.click(serviceCharge);

       String totalSumString = totalPay.getText();

        totalSumString = totalSumString.replaceAll("\\s₽","");

        System.out.println(totalSumString + " total sum");

        if (totalSumString.contains(".")) {

            System.out.println("its a double");
            double totalSum = Double.parseDouble(totalSumString);
            System.out.println(totalSum);

        } else {

            System.out.println("its an int");
            int totalSum = Integer.parseInt(totalSumString);
            System.out.println(totalSum);
        }

        baseActions.click(Tips10);


    }







}
