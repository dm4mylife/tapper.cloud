package tapper.tests;


import com.codeborne.selenide.Configuration;
import common.BaseActions;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.RootPage;
import tests.BaseTest;

import static common.ConfigDriver.IPHONE12PRO;


@Epic("Адаптив - Главная страница таппера")
@DisplayName("Главная страница таппера")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class MobileInitialAndCommonChecksTests extends BaseTest {

    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();


    //  <---------- Tests ---------->

    @Test
    @Order(1)
    @Feature("Первичные проверки элементов страницы")
    @DisplayName("Открыта страница Таппера")
    public void openTapperUrlForConcurrentExecution() {

        Configuration.browserSize = IPHONE12PRO;
        rootPage.openTapperLink();

    }

    @Test
    @Order(2)
    @Feature("Первичные проверки элементов страницы")
    @DisplayName("Отображение кнопки разделения счёта")
    @Description("Кнопка есть и она кликабельна")
    public void isDivideCheckButtonShown() { // toDo исправить

        rootPage.isDivideSliderShown();

    }

    @Test
    @Order(3)
    @Feature("Первичные проверки элементов страницы")
    @DisplayName("Отображение листинга заказа")
    @Description("Проверка что меню отображается, страница не пустая")
    public void isMenuShown() {

        rootPage.isDishListVisible();

    }

    @Test
    @Order(4)
    @Feature("Первичные проверки элементов страницы")
    @DisplayName("Кнопка Оплаты счёта")
    @Description("Кнопка отображается и кликабельна")
    public void isPaymentButtonShown() {

        rootPage.isPaymentButtonShown();

    } // toDo исправить ебучие прыжки

    @Test
    @Order(5)
    @Feature("Первичные проверки элементов страницы")
    @DisplayName("Кнопка Поделиться счётом") // toDO сделать проверку на вызов шаринга с адаптива
    @Description("Кнопка отображается и кликабельна")
    public void isShareButtonShown() {

        rootPage.isShareButtonShown();

    }

    @Test
    @Order(6)
    @Feature("Первичные проверки элементов страницы")
    @DisplayName("Чекбокс сервисного сбора") // toDO доделать
    @Description("Чекбокс отображается и кликабельный")
    public void isServiceChargeShown() {

        rootPage.isServiceChargeShown();

    }

    @Test
    @Order(7)
    @Feature("Первичные проверки элементов страницы")
    @DisplayName("Чекбокс политики конфиденциальности") // toDO доделать
    @Description("Чекбокс отображается и кликабельный")
    public void isConfPolicyShown() {

        rootPage.isConfPolicyShown();

    }

    @Test
    @Order(8)
    @Feature("Первичные проверки элементов страницы")
    @DisplayName("Отображение таббара в футере")
    @Description("Таббар есть, меню включается, стол включается")
    public void isTapBarShown() {

        rootPage.isTapBarShown();

    }


    @Test
    @Order(9)
    @Feature("Первичные проверки элементов страницы")
    @DisplayName("Проверка форм и кнопок в вызове официанта")
    @Description("Форма открывается, есть кнопки отправить\\отменить, разрешён ввод текста")
    public void isCallWaiterCorrect() {

        rootPage.isCallWaiterCorrect();

    }



}
