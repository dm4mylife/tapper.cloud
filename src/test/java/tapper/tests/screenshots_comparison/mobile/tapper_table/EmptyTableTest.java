package tapper.tests.screenshots_comparison.mobile.tapper_table;

import api.ApiRKeeper;
import common.BaseActions;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
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
import static data.Constants.WAIT_FOR_DELETE_ARTEFACT_BEFORE_SCREEN;
import static data.ScreenLayout.Tapper.tapperTableEmpty;
import static data.ScreenLayout.Tapper.tapperTableRefreshTable;
import static data.selectors.TapperTable.Common.wiFiIconBy;
import static data.selectors.TapperTable.RootPage.DishList.*;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Пустой стол")
@SixTableData
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmptyTableTest extends ScreenMobileTest {
    SixTableData data = WiFiTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            EmptyTableTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = data.restaurantName();
    protected final String tableCode = data.tableCode();
    protected final String waiter = data.waiter();
    protected final String apiUri = data.apiUri();
    protected final String tableUrl = data.tableUrl();
    protected final String tableId = data.tableId();
    Set<By> ignoredElements =
            ScreenShotComparison.setIgnoredElements(new ArrayList<>(List.of(wiFiIconBy,refreshButtonEmptyPageBy)));


    public static boolean isScreenShot = annotation.isTakeScreenshot();
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
        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableEmpty, diffPercent, imagePixelSize, ignoredElements);


    }

    @Test
    @Order(2)
    @DisplayName("Пустой стол с уведомление по кнопке 'Обновить'")
    void refreshButton() throws IOException {

        rootPage.isElementVisible(refreshButtonEmptyPage);
        BaseActions.click(refreshButtonEmptyPage);
        dishesSumChangedHeading.shouldBe(visible,matchText(REFRESH_TABLE_BUTTON_TEXT));
        rootPage.forceWait(WAIT_FOR_DELETE_ARTEFACT_BEFORE_SCREEN);

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableRefreshTable, diffPercent, imagePixelSize,ignoredElements);

    }

}
