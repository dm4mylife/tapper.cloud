package tapper.tests.screenshots_comparison.mobile.tapper_table;

import api.ApiRKeeper;
import common.BaseActions;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tapper_table.RootPage;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.TestData.TapperTable.REFRESH_TABLE_BUTTON_TEXT;
import static data.ScreenLayout.Tapper.tapperTableEmpty;
import static data.ScreenLayout.Tapper.tapperTableRefreshTable;
import static data.selectors.TapperTable.Common.wiFiIconBy;
import static data.selectors.TapperTable.RootPage.DishList.*;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Пустой стол")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmptyTableTest extends ScreenMobileTest {

    protected final String restaurantName = TableData.Keeper.Table_666.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_666.tableCode;
    protected final String waiter = TableData.Keeper.Table_666.waiter;
    protected final String apiUri = TableData.Keeper.Table_666.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_666.tableUrl;
    protected final String tableId = TableData.Keeper.Table_666.tableId;
    Set<By> ignoredElements =
            ScreenshotComparison.setIgnoredElements(new ArrayList<>(List.of(wiFiIconBy,refreshButtonEmptyPageBy)));

    boolean isScreenShot = getClass().getAnnotation(TakeOrCompareScreenshots.class).isTakeScreenshot();
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();

    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPage rootPage = new RootPage();

    @Test
    @Order(1)
    @DisplayName("Пустой стол")
    void emptyTable() throws IOException {

        apiRKeeper.isTableEmpty(restaurantName, tableId, apiUri);
        rootPage.openPage(tableUrl);
        rootPage.isEmptyOrderAfterClosing();
        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableEmpty, diffPercent, imagePixelSize, ignoredElements);


    }

    @Test
    @Order(2)
    @DisplayName("Пустой стол с уведомление по кнопке 'Обновить'")
    void refreshButton() throws IOException {

        rootPage.isElementVisible(refreshButtonEmptyPage);
        BaseActions.click(refreshButtonEmptyPage);
        dishesSumChangedHeading.shouldBe(visible,matchText(REFRESH_TABLE_BUTTON_TEXT));

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableRefreshTable, diffPercent, imagePixelSize,ignoredElements);

    }

}
