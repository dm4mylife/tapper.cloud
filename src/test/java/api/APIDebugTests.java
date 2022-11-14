package api;

import common.BaseActions;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.RootPage;
import pages.nestedTestsManager.RootPageNestedTests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static api.ApiData.QueryParams.*;
import static com.codeborne.selenide.Selenide.$;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_URL;


@Disabled
@Epic("Debug")
@DisplayName("API")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class APIDebugTests extends BaseActions {

    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();


    //  <---------- Tests ---------->


    @Test
    @DisplayName("lazyCreateOrderAndFill")
    public void lazyCreateOrderAndFill() {

        System.out.println("create order");
        String visit = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP));

        System.out.println("\nfilling order");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, GOVYADINA_PORTION, "3000"));

        rootPage.openTapperLink(STAGE_RKEEPER_URL);
        rootPage.isDishListNotEmptyAndVisible();

        HashMap<String,String> cookie = rootPageNestedTests.getCookieSessionAndGuest();

        System.out.println("\nget order info");
        Response rs =
                apiRKeeper.getOrder(rqParamsOrderGet(TABLE_3_ID, R_KEEPER_RESTAURANT,
                        cookie.get("guest"), cookie.get("session") ));

        HashMap<Integer, Map<String, Double>> orderInKeeper = rootPage.getOrderInfoFromKeeperAndConvToHashMap(rs);

        rootPage.isDishListNotEmptyAndVisible();

        rootPage.matchTapperOrderWithOrderInKeeeper(orderInKeeper);

    }

}
