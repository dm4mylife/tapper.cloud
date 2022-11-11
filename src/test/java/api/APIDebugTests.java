package api;

import common.BaseActions;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.RootPage;

import static com.codeborne.selenide.Selenide.$;
import static constants.Constant.ApiData.R_KEEPER_RESTAURANT;
import static constants.Constant.RequestBody.rqBodyFillingOrder;


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
        String visit = apiRKeeper.createOrder();

        System.out.println("\nfilling order 1");
        apiRKeeper.fillingOrder(rqBodyFillingOrder(R_KEEPER_RESTAURANT,visit,"1000303","3000"));
         System.out.println("\nfilling order 2");
      //  apiRKeeper.fillingOrder(rqBodyFillingOrder("testrkeeper",visit,"1000264","3000"));
        System.out.println("\nfilling order 3");
       // apiRKeeper.fillingOrder(rqBodyFillingOrder("testrkeeper",visit,"1000263","3000"));

    }



}
