package tapper.tests.screenshots_comparison.mobile.tapper_table;


import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
import data.ScreenLayout;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static data.Constants.TestData.TapperTable.COOKIE_GUEST_SECOND_USER;
import static data.Constants.TestData.TapperTable.COOKIE_SESSION_SECOND_USER;
import static data.selectors.TapperTable.Common.wiFiIconBy;
import static data.selectors.TapperTable.RootPage.DishList.allDishesInOrder;
import static data.selectors.TapperTable.RootPage.DishList.dishPreloaderSpinnerSelectorBy;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Проверка статусов \"Ожидается\",\"Оплачено\"")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EveryDishStatusTest extends ScreenMobileTest {

    protected final String restaurantName = TableData.Keeper.Table_666.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_666.tableCode;
    protected final String waiter = TableData.Keeper.Table_666.waiter;
    protected final String apiUri = TableData.Keeper.Table_666.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_666.tableUrl;
    protected final String tableId = TableData.Keeper.Table_666.tableId;
    Set<By> ignoredElements =
            ScreenshotComparison.setIgnoredElements
                    (new ArrayList<>(List.of(wiFiIconBy,dishPreloaderSpinnerSelectorBy)));

    boolean isScreenShot = getClass().getAnnotation(TakeOrCompareScreenshots.class).isTakeScreenshot();
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();

    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Test
    @Order(1)
    @DisplayName("Статус 'Ожидается' и 'Оплачено'")
    void statuses() throws IOException {

        String guid;
        double totalPay;
        String orderType = "part";
        int amountDishesForFillingOrder = 3;

        guid = nestedTests.createAndFillOrder(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableId);

        rootPageNestedTests.openTableAndSetGuest(tableUrl,COOKIE_GUEST_SECOND_USER,COOKIE_SESSION_SECOND_USER);
        rootPage.ignoreAllDynamicsElements();
        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPageNestedTests.chooseFirstDish(allDishesInOrder);

        Selenide.clearBrowserLocalStorage();
        Selenide.clearBrowserCookies();
        Selenide.refresh();
        rootPage.isTableHasOrder();

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPageNestedTests.chooseLastDish(allDishesInOrder);

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.tapperTableDisabledDish,diffPercent,imagePixelSize,ignoredElements);

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        nestedTests.acquiringPayment(totalPay);
        reviewPageNestedTests.paymentCorrect(orderType);

        reviewPage.isReviewBlockCorrect();
        reviewPage.clickOnFinishButton();
        rootPage.isTableHasOrder();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.tapperTableDisabledAndPayedDish,diffPercent,imagePixelSize,ignoredElements);

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
