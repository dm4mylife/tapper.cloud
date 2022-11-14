package api;

import common.BaseActions;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.RootPage;

import static com.codeborne.selenide.Selenide.$;
import static constants.Constant.ApiData.*;
import static constants.Constant.QueryParams.rqParamsCreateOrderBasic;
import static constants.Constant.QueryParams.rqParamsFillingOrderBasic;


@Disabled
@Epic("Debug")
@DisplayName("API")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class APIDebugTests extends BaseActions {

    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();



    //  <---------- Tests ---------->


    @Test
    @DisplayName("lazyCreateOrderAndFill")
    public void lazyCreateOrderAndFill() {

        System.out.println("create order");
        String visit = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT,TABLE_3, WAITER_ROBOCOP));

        System.out.println("\nfilling order 1");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT,visit,BARNOE_PIVO,"3000"));
        System.out.println("\nfilling order 2");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT,visit,GLAZUNYA,"3000"));
        System.out.println("\nfilling order 3");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT,visit,SOLYANKA,"3000"));

    }



}
