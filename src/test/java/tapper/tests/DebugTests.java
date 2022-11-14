package tapper.tests;


import api.ApiRKeeper;
import common.ConfigDriver;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import pages.*;
import pages.nestedTestsManager.Best2PayPageNestedTests;
import pages.nestedTestsManager.ReviewPageNestedTests;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;


import static com.codeborne.selenide.Selenide.$;
import static constants.Constant.ApiData.BARNOE_PIVO;
import static constants.Constant.ApiData.R_KEEPER_RESTAURANT;
import static constants.Constant.QueryParams.rqParamsFillingOrderBasic;

@Disabled
@Epic("Debug")
@DisplayName("E2E")
@ConfigDriver(type="desktop")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DebugTests extends BaseTest {


    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();


    //  <---------- Tests ---------->




    @Test
    @Step("Debugging")
    @DisplayName("Debugging")
    public void test() {






    }




}
