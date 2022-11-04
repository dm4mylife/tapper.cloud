package api;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.RootPage;

import static com.codeborne.selenide.Selenide.$;


@Disabled
@Epic("API")
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



        String visit = apiRKeeper.createOrder();

        apiRKeeper.fillingOrder(visit);


    }



}
