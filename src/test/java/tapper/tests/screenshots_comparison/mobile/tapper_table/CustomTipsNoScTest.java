package tapper.tests.screenshots_comparison.mobile.tapper_table;


import api.ApiRKeeper;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.selectors.TapperTable;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.RegexPattern.TapperTable.tableNumberRegex;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.RootPage.DishList.tableNumber;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Отмена сервисного сбора и ручной ввод чаевых")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomTipsNoScTest extends ScreenMobileTest {
    static TakeOrCompareScreenshots annotation =
            CustomTipsNoScTest.class.getAnnotation(TakeOrCompareScreenshots.class);
    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_111;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_111;
    protected final String tableId = TABLE_AUTO_111_ID;

    public static boolean isScreenShot = annotation.isTakeScreenshot();
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();
    static String guid;
    static int amountDishesForFillingOrder = 2;
    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();
    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Test
    @Order(1)
    @DisplayName("Отмена сервисного сбора и ручной ввод чаевых")
    void createAndFillOrder() throws IOException {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);

        rootPage.setCustomTips("155");
        rootPage.scrollTillBottom();
        rootPage.deactivateServiceChargeIfActivated();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.tapperTableCustomTipsAndNoSc,diffPercent,imagePixelSize);

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid,apiUri);

    }


}
