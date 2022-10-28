package api;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.RootPage;

import static com.codeborne.selenide.Selenide.$;
import static common.ConfigDriver.IPHONE12PRO;


@Epic("Претестовые для дебаггинга")
@DisplayName("Претестовые для дебаггинга")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class APIDebugTests extends BaseActions {

    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();



    //  <---------- Tests ---------->



    @Test
    @DisplayName("debug - e2e with api")
    public void api() {



    }

    @Disabled
    @Test
    @DisplayName("test")
    public void lazyCreateOrderAndFill() {

     apiRKeeper.fillingOrder(apiRKeeper.createOrder());


    }



}
