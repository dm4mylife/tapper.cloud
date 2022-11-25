package tapper.tests.e2e;


import api.ApiRKeeper;
import com.codeborne.selenide.SelenideElement;
import com.google.common.base.Stopwatch;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.RootPage;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.exist;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Selectors.RootPage.DishList.dishesStatus;

@Order(3)
@Epic("E2E - тесты (полные)")
@Feature("keeper - выбираем позиции, разделяем блюдо и проверяем что блюда заблокированы и в статусе ожидаются 4+ мин")
@DisplayName("keeper - выбираем позиции, разделяем блюдо и проверяем что блюда заблокированы и в статусе ожидаются 4+ мин")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_3_DisabledDishesWhenDividedByTimeLimitTest extends BaseTest {

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP));
        String visit = rs.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "3000"));

    }

    @Test
    @DisplayName("2. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("3. Выбираем рандомно блюда, проверяем все суммы и условия")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmountWithTipsWithSC(3);

    }

    @Test
    @DisplayName("4. Очищаем все данные, делимся чеком")
    public void clearAllSiteData() {

        rootPage.clearAllSiteData();
        rootPage.forceWait(2000);

    }

    @Test
    @DisplayName("5. Проверяем что позиции закрыты для выбора и в статусе ожидается")
    public void savePaymentDataForAcquiring() {

        Stopwatch stopwatch = Stopwatch.createStarted();

        System.out.println(dishesStatus);

        for (SelenideElement element : dishesStatus) {
            System.out.println("Элементы еще в статусе 'Оплачивается'");
            element.shouldNotBe(exist, Duration.ofSeconds(300));

        }

        stopwatch.stop();

        long millis = stopwatch.elapsed(TimeUnit.SECONDS);
        System.out.println(millis + " sec execution time");

        Assertions.assertTrue(millis >= 240, "Время ожидания разделенных позиций меньше 4 мин (" + millis + ")");
        System.out.println(millis + "Время ожидания разделенных позиций соответствует 4 мин или больше");

        Allure.addAttachment("Время ожидания разделенных позиций в секундах", "text/plain", String.valueOf(millis));

    }

    @Test
    @DisplayName("6. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        rootPageNestedTests.closeOrder();
    }

}