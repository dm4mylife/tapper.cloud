package tapper.tests.keeper_e2e._5_sockets;

import org.junit.jupiter.api.Test;
import tapper_table.RootPage;
import tests.BaseTestTwoBrowsers;

import static com.codeborne.selenide.Selenide.*;
import static data.selectors.TapperTable.RootPage.DishList.divideCheckSlider;

public class Experiment extends BaseTestTwoBrowsers {

    RootPage rootPage = new RootPage();

    @Test
    public void gg() {

        using(firstBrowser, () -> {

            open("https://stage-ssr.zedform.ru/testrkeeper/1000043");

        });

        using(secondBrowser, () -> {

            open("https://stage-ssr.zedform.ru/testrkeeper/1000043");

        });

        using(firstBrowser, () -> {

            sleep(5000);
            divideCheckSlider.click();
            rootPage.chooseCertainAmountDishes(1);


        });

        using(secondBrowser, () -> {

            sleep(5000);


        });

    }



}
