package tapper.tests.admin_personal_account.tips;

import admin_personal_account.tips.Tips;
import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
import data.AnnotationAndStepNaming;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static data.Constants.TestData.AdminPersonalAccount.*;


@Epic("Личный кабинет администратора ресторана")
@Feature("Чаевые")
@DisplayName("Проверка что все элементы отображаются корректно")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiterName = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    static String guid;
    String tapperTableTab = "tapper";
    int adminTab = 0;
    String hookahRole = "hookah";
    String kitchenRole = "kitchen";
    static String waiterAdminName;
    static String goal;

    AuthorizationPage authorizationPage = new AuthorizationPage();
    RootPage rootPage = new RootPage();
    Tips tips = new Tips();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();


    @Test
    @Order(1)
    @DisplayName("Создание заказа на кассе.Авторизация под администратором в личном кабинете")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, 3);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiterName, apiUri,
                dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу чаевых")
    void goToTipsCategory() {

        tips.goToTipsCategory();

    }
    @Test
    @Order(3)
    @DisplayName("Проверка таба Чаевые кальянщику")
    void isTipsHookahTabCorrect() {

        tips.isServiceWorkerRoleTipsTabCorrect(hookahRole);
        tips.activateTips();

    }
    @Test
    @Order(4)
    @DisplayName("Загрузка аватарки кальянщику")
    void downloadAvatar() {

        tips.downloadAvatar();

    }
    @Test
    @Order(5)
    @DisplayName("Проверка аватарки на столе таппера")
    void isServiceWorkerAvatarCorrect() {

        rootPage.openNewTabAndSwitchTo(tableUrl);
        rootPage.isTableHasOrder();
        rootPage.isServiceWorkerAvatarCorrect(hookahRole,true);

    }
    @Test
    @Order(6)
    @DisplayName("Удаляем аватарку ")
    void deleteAvatar() {

        rootPage.switchBrowserTab(adminTab);
        tips.deleteAvatar();

    }
    @Test
    @Order(7)
    @DisplayName("Проверка что аватарка удалилась и её нет на столе")
    void isServiceWorkerAvatarCorrectAfterDelete() {

        rootPage.switchAndRefreshBrowserTabWithOrder(tapperTableTab);
        rootPage.isServiceWorkerAvatarCorrect(hookahRole,false);

    }

    @Test
    @Order(8)
    @DisplayName("Смена имени кальянщика")
    void setServiceWorkerName() {

        rootPage.switchBrowserTab(adminTab);
        waiterAdminName = tips.setServiceWorkerName();

    }

    @Test
    @Order(9)
    @DisplayName("Проверка изменения имени на столе")
    void isServiceWorkerNameCorrect() {

        rootPage.switchAndRefreshBrowserTabWithOrder(tapperTableTab);
        rootPage.isServiceWorkerNameCorrect(waiterAdminName);

    }

    @Test
    @Order(10)
    @DisplayName("Проверка ошибки при сохранении пустого имени в админке")
    void checkServiceWorkerName() {

        rootPage.switchBrowserTab(adminTab);
        tips.isEmptyHookahNameIncorrect();

    }

    @Test
    @Order(11)
    @DisplayName("Установка цели кальянщика")
    void setGoal() {

        Selenide.refresh(); // toDo убрать это когда пофиксят баг, что нельзя два раза сохранить на странице без рефреша
        goal = tips.setGoal(hookahRole);

    }

    @Test
    @Order(12)
    @DisplayName("Проверка цели на столе")
    void isGoalCorrect() {

        rootPage.switchAndRefreshBrowserTabWithOrder(tapperTableTab);
        rootPage.isGoalCorrect(hookahRole,goal);

    }

    @Test
    @Order(13)
    @DisplayName("Отключаем чаевые кальянщику в админке")
    void deactivateTips() {

        rootPage.switchBrowserTab(adminTab);
        tips.deactivateTips();

    }

    @Test
    @Order(14)
    @DisplayName("Проверяем что чаевые кальянщика отключены на столе")
    void resetGoal() {

        rootPage.switchAndRefreshBrowserTabWithOrder(tapperTableTab);
        rootPage.isTipsDeactivated(hookahRole);

    }

    @Test
    @Order(15)
    @DisplayName("Проверка таба Чаевые кухне")
    void isKitchenHookahTabCorrect() {

        rootPage.switchBrowserTab(adminTab);
        tips.goToKitchenTipsCategory();
        tips.isServiceWorkerRoleTipsTabCorrect(kitchenRole);
        tips.activateTips();

    }
    @Test
    @Order(16)
    @DisplayName("Загрузка аватарки кухне")
    void downloadKitchenAvatar() {

        tips.downloadAvatar();

    }
    @Test
    @Order(17)
    @DisplayName("Проверка аватарки на столе таппера")
    void isKitchenAvatarCorrect() {

        rootPage.switchAndRefreshBrowserTabWithOrder(tapperTableTab);
        rootPage.isServiceWorkerAvatarCorrect(kitchenRole,true);

    }
    @Test
    @Order(18)
    @DisplayName("Удаляем аватарку")
    void deleteKitchenAvatar() {

        rootPage.switchBrowserTab(adminTab);
        tips.deleteAvatar();

    }
    @Test
    @Order(19)
    @DisplayName("Проверка что аватарка кухни удалилась на столе")
    void isKitchenAvatarCorrectAfterDelete() {

        rootPage.switchAndRefreshBrowserTabWithOrder(tapperTableTab);
        rootPage.isServiceWorkerAvatarCorrect(kitchenRole,false);

    }

    @Test
    @Order(20)
    @DisplayName("Установка цели кухни")
    void setKitchenGoal() {

        rootPage.switchTab(adminTab);// toDo убрать это когда пофиксят баг, что нельзя два раза сохранить на странице без рефреша
        goal = tips.setGoal(kitchenRole);

    }

    @Test
    @Order(21)
    @DisplayName("Проверка цели на столе")
    void isKitchenGoalCorrect() {

        rootPage.switchAndRefreshBrowserTabWithOrder(tapperTableTab);
        rootPage.isGoalCorrect(kitchenRole,goal);

    }

    @Test
    @Order(22)
    @DisplayName("Отключаем чаевые кухне в админке")
    void deactivateKitchenTips() {

        rootPage.switchBrowserTab(adminTab);
        tips.deactivateTips();

    }
    @Test
    @Order(23)
    @DisplayName("Проверяем что чаевые кальянщика отключены на столе")
    void resetKitchenGoal() {

        rootPage.switchAndRefreshBrowserTabWithOrder(tapperTableTab);
        rootPage.isTipsDeactivated(kitchenRole);

    }

    @Test
    @Order(24)
    @DisplayName("Проверка привязки карты в табе Чаевые кальянщику")
    void changeCreditCard() {

        rootPage.switchBrowserTab(adminTab);
        rootPage.refreshPage();
        tips.changeCreditCard(hookahRole);

    }
    @Test
    @Order(25)
    @DisplayName("Проверка привязки карты в табе Чаевые кухне")
    void changeCreditCardKitchenTips() {

        tips.goToKitchenTipsCategory();
        tips.changeCreditCard(kitchenRole);

    }

    @Test
    @Order(26)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.closedOrder)
    void closedOrderByApi() {

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
