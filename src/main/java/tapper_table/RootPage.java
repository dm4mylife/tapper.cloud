package tapper_table;

import api.ApiRKeeper;
import com.codeborne.selenide.*;
import common.BaseActions;
import data.Constants;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Cookie;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static api.ApiData.QueryParams.rqParamsOrderPay;
import static api.ApiData.orderData.R_KEEPER_RESTAURANT;
import static api.ApiData.orderData.TABLE_AUTO_111_ID;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.screenshot;
import static data.Constants.TestData.TapperTable;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.TestData.Yandex;
import static data.selectors.TapperTable.Common.*;
import static data.selectors.TapperTable.RootPage.*;
import static data.selectors.TapperTable.RootPage.DishList.*;
import static data.selectors.TapperTable.RootPage.Menu.menuCategoryContainerName;
import static data.selectors.TapperTable.RootPage.PayBlock.*;
import static data.selectors.TapperTable.RootPage.TapBar.*;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.*;


public class RootPage extends BaseActions {

    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Telegram telegram = new Telegram();



    @Step("Проверка элементов недоступности сервиса")
    public void isServiceUnavailable() {

        isElementVisible(serviceUnavailabilityContainer);
        isElementVisible(serviceUnavailabilityInfoContainer);
        isElementVisible(serviceUnavailabilityAdviceContainer);

    }

    @Step("Проверка элементов недоступности оплаты")
    public void isPaymentUnavailable() {

        isElementVisible(techWorkContainer);
        isElementVisible(techWorkInfoContainer);
        isElementVisible(techWorkCloseButton);

    }

    @Step("Проверка всплывающей подсказки")
    public void isHintModalCorrect() {

        isElementVisible(modalHintContainer);
        isElementVisible(modalHintCloseButton);

    }

    @Step("Возвращаемся на прошлую страницу")
    public void returnToPreviousPage() {

        Selenide.back();
        forceWait(1500);

    }

    @Step("Обновляем текущую страницу")
    public void refreshPage() {

        Selenide.refresh();
        forceWait(1500);

    }

    @Step("Сбор всех блюд со страницы таппера и проверка с блюдами на кассе")
    public void matchTapperOrderWithOrderInKeeper(HashMap<Integer, Map<String, Double>> allDishesInfoFromKeeper) {

        HashMap<Integer, Map<String, Double>> tapperDishes = new HashMap<>();

        int i = 0;

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = element.$(".orderItem__name").getText();
            double price = convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price:last-child"), "\\s₽");

            temporaryMap.put(name, price);
            tapperDishes.put(i, temporaryMap);

            i++;

        }

        System.out.println("\nTapper\n" + tapperDishes + "\nCashDesk\n" + allDishesInfoFromKeeper);

        if (allDishesInfoFromKeeper.equals(tapperDishes)) {

            System.out.println("Данные с кассы и столом совпадают");

        } else {

            System.out.println("Данные с кассы и столом НЕ совпадают");

            allDishesInfoFromKeeper.entrySet().stream()
                    .filter(entry -> !tapperDishes.containsValue(entry.getValue()))
                    .forEach(entry -> {

                        System.out.println(entry.getValue() + " это не совпадает со столом.На кассе другая цена");
                        Assertions.fail(entry.getValue() + " это не совпадает со столом.На кассе другая цена");

                    });

            tapperDishes.entrySet().stream()
                    .filter(entry -> !allDishesInfoFromKeeper.containsValue(entry.getValue()))
                    .forEach(entry -> {

                        System.out.println(entry.getValue() + " это не совпадает с кассой.На столе другая цена");
                        Assertions.fail(entry.getValue() + " это не совпадает с кассой.На столе другая цена");

                    });

        }

        System.out.println("Позиции и суммы на кассе полностью совпадают с тем что на столе таппера");

    }

    @Step("Переход на страницу {url} и ждём принудительно пока не прогрузятся все скрипты\\элементы\\сокет")
    public void openUrlAndWaitAfter(String url) {

        openPage(url);
        forceWait(Constants.TIME_WAIT_FOR_FULL_LOAD);

        //closeHintModal();



    }

    @Step("Проверки что стол освободился, статусы в заголовке корректные")
    public void isEmptyOrderAfterClosing() {

        emptyOrderHeading.shouldHave(visible.because("Не появился пустой стол в течение определенного времени")
                ,Duration.ofSeconds(7));
        emptyOrderHeading.shouldHave(matchText("Ваш заказ появится здесь"), Duration.ofSeconds(10));

        double totalSumInWallet = convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");
        Assertions.assertEquals(totalSumInWallet, 0, "Сумма в кошельке не равно 0");

    }

    @Step("Первичная заставка\\лого\\анимация при открытии страницы")
    public void isStartScreenShown() {

        startScreenLogoContainer.shouldHave(appear,Duration.ofSeconds(5));

        if (startScreenLogoContainerImage.exists()) {

            isImageCorrect(startScreenLogoContainerImageNotSelenide,
                    "Изображение\\анимация загрузки стола не корректная или битая");

        }

        startScreenLogoContainer.shouldNotHave(cssValue("display", "flex"), Duration.ofSeconds(15));

    }

    @Step("Переключаем на разделение счёта")
    public void activateDivideCheckSliderIfDeactivated() {

        forceWait(1500);

        if (divideCheckSlider.isDisplayed()) {

            click(divideCheckSlider);
            forceWait(1500); // toDO если после активации раздельного меню сразу выбрать позицию, то гарантировано 422, поэтому ждём
            System.out.println("\nВключили разделение счёта\n");

        }

    }

    @Step("Отключаем разделение счёта")
    public void deactivateDivideCheckSliderIfActivated() {

        if (discountSum.isDisplayed()) { // если есть скидка\\наценка то разделять счёт нельзя

            isElementInvisible(divideCheckSlider);

        }

        isElementVisibleDuringLongTime(divideCheckSliderActive, 5);
        click(divideCheckSliderActive);
        forceWait(1000);  // toDO если после активации раздельного меню сразу выбрать позицию, то гарантировано 422, поэтому ждём
        System.out.println("\nОтключили разделение счёта\n");

    }

    @Step("Проверка функционала кнопки разделения счётом")
    public void isDivideSliderCorrect() {

        isElementVisibleDuringLongTime(divideCheckSlider, 15);
        click(divideCheckSlider);

        divideCheckSliderActive.shouldBe(visible);
        isElementsListVisible(allDishesCheckboxes);
        allDishesStatuses.filterBy(cssValue("display", "flex"))
                .shouldBe(CollectionCondition.sizeGreaterThan(0));

        click(divideCheckSliderActive);
        isElementsListInVisible(allDishesCheckboxes);
        allDishesStatuses.filterBy(cssValue("display", "none"))
                .shouldBe(CollectionCondition.sizeGreaterThan(0));

       // forceWait(2000); //toDo иначе 422

    }

    @Step("Заказ не пустой и блюда отображаются")
    public void isDishListNotEmptyAndVisible() {

        isElementVisibleDuringLongTime(orderContainer, 60);

    }

    @Step("Проверка что все элементы в блоке чаевых отображаются")
    public void isTipsContainerCorrect() {

        if (totalTipsSumInMiddle.exists()) {

            isElementVisible(tipsContainer);

            if (totalTipsSumInMiddle.exists()) {

                isImageCorrect(waiterImageNotSelenide,"Изображение официанта не корректное или битое");

                isElementVisible(tipsWaiter);

                isElementVisible(totalTipsSumInMiddle);
                isElementsListVisible(tipsListItem);
                checkCustomTipForError();

            }

        }

    }

    @Step("Очищаем локал и куки для разделения чека")
    public void clearAllSiteData() {

        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
        refreshPage();

    }

    @Step("Проверка логики суммы сервисного сбора с общей суммой и чаевыми")
    public double countServiceCharge(double totalSum) { //

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("none")) {

            activateServiceChargeIfDeactivated();

            double tapperDiscount = 0;

            if (discountSum.exists()) {

                tapperDiscount = convertSelectorTextIntoDoubleByRgx(discountSum, "[^\\.\\d]+");
                System.out.println(tapperDiscount + " tapper discount");

            }

            totalSum -= tapperDiscount;
            System.out.println(totalSum + " total sum");
            double serviceChargeSumClear =
                    convertDouble(totalSum * (SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM / 100));

            if (tipsContainer.exists()) {

                int tipsCount = Integer.parseInt(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
                double serviceChargeFromTips =
                        convertDouble(tipsCount * (SERVICE_CHARGE_PERCENT_FROM_TIPS / 100));

                serviceChargeSumClear = convertDouble(serviceChargeSumClear + serviceChargeFromTips);

            }

            System.out.println(serviceChargeSumClear + " serviceChargeSumClear");

            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽") - tapperDiscount;
            double currentSumInWallet =
                    convertSelectorTextIntoDoubleByRgx
                            (TapBar.totalSumInWalletCounter, "\\s₽") - tapperDiscount;
            double serviceChargeInField =
                    convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            serviceChargeInField = convertDouble(serviceChargeInField);


            Assertions.assertEquals(totalPaySum, currentSumInWallet, 0.1,
                    "Сумма в 'Итого к оплате' " + totalPaySum
                            + " не совпадает с суммой в иконке кошелька вместе с СБ "
                            + currentSumInWallet + "\n");
            System.out.println("Сумма в 'Итого к оплате' " + totalPaySum
                    + " совпадает с суммой в иконке кошелька вместе с СБ "
                    + currentSumInWallet + "\n");

            Assertions.assertEquals(serviceChargeSumClear, serviceChargeInField, 0.1,
                    "Сервисный сбор рассчитывается не корректно из чаевых и суммы заказа");
            System.out.println("Сервисный сбор" + serviceChargeSumClear +
                    "рассчитывается корректно из чаевых и суммы заказа и совпадает с полем СБ " + serviceChargeInField);

        }

        return convertDouble(totalSum * (SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM / 100));

    }

    @Step("Ввод кастомных чаевых")
    public void setCustomTips(String value) {

        totalTipsSumInMiddle.clear();

        click(totalTipsSumInMiddle);
        totalTipsSumInMiddle.clear();
        sendHumanKeys(totalTipsSumInMiddle, value);
        totalTipsSumInMiddle.shouldHave(value(value));

    }

    @Step("Выставляем чаевые на 0 и активируем СБ")
    public void cancelTipsAndActivateSC(double cleanTotalSum) {

        resetTips();
        countServiceCharge(cleanTotalSum);

    }

    @Step("Выставляем рандомные чаевые и активируем СБ если его нет")
    public void setRandomTipsAndActivateScIfDeactivated() {

        setRandomTipsOption();
        activateServiceChargeIfDeactivated();

    }

    @Step("Выставляем рандомные чаевые и деактивируем СБ если есть")
    public void setRandomTipsAndDeactivateScIfActivated() {

        setRandomTipsOption();
        deactivateServiceChargeIfActivated();

    }

    @Step("Активируем СБ если не активен")
    public void activateServiceChargeIfDeactivated() {

        if (serviceChargeContainer.exists() &&
                serviceChargeCheckboxSvg.getCssValue("display").equals("none")) {

            scrollTillBottom();
            click(serviceChargeCheckboxButton);
            System.out.println("Установлен сервисный сбор");

        }

    }

    @Step("Деактивируем СБ если активен")
    public void deactivateServiceChargeIfActivated() {

        scrollTillBottom();

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            click(serviceChargeCheckboxButton);
            System.out.println("Отключен сервисный сбор");

        }

    }

    @Step("Проверка что у блюд с модификатором есть подписи в позиции")
    public void isModificatorTextCorrect() {

        for (SelenideElement element : allDishesInOrder) {

            element.$(".orderItem__modificator").shouldBe(exist);

        }

        System.out.println("Текст модификатора выводится под блюдом");

    }

    @Step("Прощёлкиваем все опции чаевых и проверяем что чаевые формируются корректно от общей суммы")
    public void checkAllTipsOptions(double totalSum) {

        showPaymentOptionsAndTapBar();
        scrollTillBottom();
        StringBuilder logs = new StringBuilder();
        double cleanTips = 0;

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            activateServiceChargeIfDeactivated();

            click(tipsOption);
            tipsOption
                    .shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(2));

            double serviceChargeSum =
                    convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            serviceChargeSum = convertDouble(serviceChargeSum);

            int percent = convertSelectorTextIntoIntByRgx(tipsOption, "\\D+");
            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

            double tipsSumInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
            int totalTipsSumInMiddle = Integer.parseInt
                    (Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;
                cleanTips = totalSum * percentForCalculation;
                cleanTips = Math.round(cleanTips);

            }

            double totalSumPlusCleanTips = totalSum + cleanTips;
            double totalSumPlusCleanTipsPlusServiceChargeSum = totalSum + cleanTips + serviceChargeSum;

            logs
                .append("\nЧаевые - " + percent)
                .append(serviceChargeSum + " чистый сервисный сбор по текущей проценту чаевых: " + percent)
                .append(cleanTips + " чистые чаевые по общей сумме за блюда")
                .append(totalSum + " чистая сумма за блюда без чаевых и без сервисного сбора")
                .append(totalSumPlusCleanTips + " чистая сумма за блюда c чаевым и без сервисного сбора")
                .append(totalSumPlusCleanTipsPlusServiceChargeSum + " чистая сумма за блюда c чаевым и сервисным сбором")
                .append(totalPaySum + " сумма в поле 'Итого к оплате' c учетом чаевых и сервисного сбора\n");


            Assertions.assertEquals(totalTipsSumInMiddle, cleanTips, 0.1,
                    "Общая сумма чаевых по центру " + totalTipsSumInMiddle +
                            " не совпала с суммой чистых чаевых " + cleanTips);

            Assertions.assertEquals(tipsSumInCheck, cleanTips, 0.1,
                    "Чаевые в 'Чаевые'" + tipsSumInCheck +
                            " не совпали с суммой чистых чаевых " + cleanTips);

            Assertions.assertEquals(totalSumPlusCleanTipsPlusServiceChargeSum, totalPaySum, 0.1,
                    "Чистая сумма за блюда + чистые чаевые + СБ " + totalSumPlusCleanTipsPlusServiceChargeSum
                            + " равна сумме в 'Итого к оплате' " + totalPaySum);

            click(resetTipsButton);

        }

        printAndAttachAllureLogs(logs,"Подсчёт сумм");

    }

    @Step("Проверяем что разные проценты чаевых рассчитываются корректно во всех полях и в суммах вместе с СБ не разделяя счёт")
    public void isAllTipsOptionsAreCorrectWithTotalSumAndSC(double totalSum) {
        checkAllTipsOptions(totalSum);
    }

    @Step("Проверка что логика установленных чаевых по умолчанию от общей суммы корректна")
    public void isDefaultTipsBySumLogicCorrect() {

        scrollTillBottom();
        double totalDishSum = getClearOrderAmount();
        System.out.println(totalTipsSumInMiddle.getValue() + " чаевые\n");
        System.out.println(totalDishSum + " чистая сумма за позиции\n");

        if (totalDishSum < 490) {

            totalTipsSumInMiddle.shouldHave(value(TapperTable.MIN_SUM_TIPS_));


            if (totalDishSum < 196) {

                tips0.shouldNotHave(attributeMatching("class", ".+disabled"));

                tips10.shouldHave(attributeMatching("class", ".+disabled"));
                tips15.shouldHave(attributeMatching("class", ".+disabled"));
                tips20.shouldHave(attributeMatching("class", ".+disabled"));
                tips25.shouldHave(attributeMatching("class", ".+disabled"));

                System.out.println("Первая опция чаевых - менее 196");


            } else if (totalDishSum >= 196 && totalDishSum <= 245) {

                tips25.shouldNotHave(attributeMatching("class", ".+disabled"));

                tips10.shouldHave(attributeMatching("class", ".+disabled"));
                tips15.shouldHave(attributeMatching("class", ".+disabled"));
                tips20.shouldHave(attributeMatching("class", ".+disabled"));

                System.out.println("Вторая опция чаевых - менее 245");

            } else if (totalDishSum > 245 && totalDishSum <= 326) {

                tips20.shouldNotHave(attributeMatching("class", ".+disabled"));
                tips25.shouldNotHave(attributeMatching("class", ".+disabled"));

                tips10.shouldHave(attributeMatching("class", ".+disabled"));
                tips15.shouldHave(attributeMatching("class", ".+disabled"));

                System.out.println("Третья опция чаевых - менее 326");

            } else if (totalDishSum > 326 && totalDishSum <= 489) {

                tips15.shouldNotHave(attributeMatching("class", ".+disabled"));
                tips20.shouldNotHave(attributeMatching("class", ".+disabled"));
                tips25.shouldNotHave(attributeMatching("class", ".+disabled"));

                tips10.shouldHave(attributeMatching("class", ".+disabled"));

                System.out.println("Четвертая опция чаевых - менее 489");

            }

        } else if (totalDishSum > 490) {

            tips10.shouldNotHave(attributeMatching("class", ".+disabled"));
            tips15.shouldNotHave(attributeMatching("class", ".+disabled"));
            tips20.shouldNotHave(attributeMatching("class", ".+disabled"));
            tips25.shouldNotHave(attributeMatching("class", ".+disabled"));

            activeTipsButton.shouldHave(text("10%")
                    .because("Если сумма заказа больше 490, то должно быть 10% чаевых по умолчанию"));

            System.out.println("Пятая опция чаевых - более 490");

        }

    }

    @Step("Выбираем все не оплаченные позиции")
    public void chooseAllNonPaidDishes() {

        hidePaymentOptionsAndTapBar();
        double totalDishesSum = 0;

        System.out.println(allNonPaidAndNonDisabledDishes.size() + " общее число не оплаченных и не заблокированных позиций\n");

        for (int index = 0; index < allNonPaidAndNonDisabledDishes.size(); index++) {

            if (!allNonPaidAndNonDisabledDishesCheckbox.get(index).getCssValue("display").equals("block")) {

                double currentDishPrice = convertSelectorTextIntoDoubleByRgx
                        (allNonPaidAndNonDisabledDishesSum.get(index), "\\s₽");
                String currentDishName = allNonPaidAndNonDisabledDishesName.get(index).getText();

                totalDishesSum += currentDishPrice;

                scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
                forceWaitingForSocketChangePositions(800);
                allNonPaidAndNonDisabledDishesCheckbox.get(index)
                        .shouldBe(cssValue("display", "block"), Duration.ofSeconds(2));

                System.out.println("Блюдо - " + currentDishName +
                        " - " + currentDishPrice +
                        ". Общая цена " + totalDishesSum);

            }

        }

        showPaymentOptionsAndTapBar();

    }

    @Step("Отменяем определенное количество выбранных позиций")
    public void cancelCertainAmountChosenDishes(int count) { //

        hidePaymentOptionsAndTapBar();

        System.out.println(allNonPaidAndNonDisabledDishes.size() + " общее число не оплаченных и не заблокированных позиций\n");

        for (int index = 0; index < allNonPaidAndNonDisabledDishes.size(); index++ ) {

            if (count != 0) {

                if (allNonPaidAndNonDisabledDishesCheckbox.get(index).getAttribute("style").equals("")) {

                    scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
                    forceWait(700); // toDo иначе 422
                    System.out.println("Отменили выбранную позицию. Осталось выбрать " + count);

                    count--;

                }

            }

        }

        showPaymentOptionsAndTapBar();

    }

    @Step("Отменяем все выбранные")
    public void cancelAllChosenDishes() { //

        hidePaymentOptionsAndTapBar();

        System.out.println(allDishesInOrder.size() + " всего блюд в заказе\n");

        for (int index = 0; index < allDishesInOrder.size(); index++ ) {

            if (allNonPaidAndNonDisabledDishesCheckbox.get(index).getAttribute("style").equals("")) {

                scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
                forceWait(700); // toDo иначе 422
                System.out.println("Отменили выбранную позицию");

                if (index == allDishesInOrder.size()-1) {

                    System.out.println("Отменили все блюда");

                }

            }

        }

        showPaymentOptionsAndTapBar();

    }

    @Step("Выбираем определенное число блюд и считаем сумму выбранных рандомных позиций")
    public void chooseCertainAmountDishes(int neededDishesAmount) {

            hidePaymentOptionsAndTapBar();

            double totalDishesSum = 0;
            double currentDishPrice;
            String currentDishName;

            System.out.println("\n" + "Количество рандомных позиций для выбора: " + neededDishesAmount + "\n" +
                    allNonPaidAndNonDisabledDishes.size() + " общее число позиций");
            int chosenDishesSize = allNonPaidAndNonDisabledDishesCheckbox.filter(attribute("display","block")).size();

            for (int count = 1; count <= neededDishesAmount; count++) {

                int index;
                String flag;

                if (chosenDishesSize != allNonPaidAndNonDisabledDishes.size()) {

                    do {

                        index = generateRandomNumber(1, allNonPaidAndNonDisabledDishes.size()) - 1;
                        flag = allNonPaidAndNonDisabledDishesCheckbox.get(index).getCssValue("display");

                        currentDishPrice = convertSelectorTextIntoDoubleByRgx
                                (allNonPaidAndNonDisabledDishesSum.get(index), "\\s₽");
                        currentDishName = allNonPaidAndNonDisabledDishesName.get(index).getText();

                    } while (flag.equals("block"));

                    totalDishesSum += currentDishPrice;

                    scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
                    System.out.println("Выбрали позицию\nБлюдо - " + currentDishName + " - " + currentDishPrice +
                            ". Общая цена " + totalDishesSum);
                    forceWait(1000); // toDo иначе 422

                    allNonPaidAndNonDisabledDishesCheckbox.get(index)
                            .shouldBe(cssValue("display", "block"), Duration.ofSeconds(5));

                } else {

                    System.out.println("Все блюда выбраны, выходим что из цикла");
                    break;

                }

            }

            showPaymentOptionsAndTapBar();

    }

    @Step("Считаем сумму всех не выбранных позиций")
    public double countAllDishes() { //toDo logs Cannot invoke "java.lang.StringBuilder.append(String)" because "logs" is null

        double totalDishesSum = 0;

        //   StringBuilder logs = null;

        System.out.println(allDishesInOrder.size() + " общее число позиций");

        for (SelenideElement element : allDishesInOrder) {

            double currentDishPrice =
                    convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price:last-child"), "\\s₽");
            String currentDishName = element.$(".orderItem__name").getText();

            totalDishesSum += currentDishPrice;

            System.out.println("Блюдо - " + currentDishName + " - " + currentDishPrice +
                    ". Общая цена " + totalDishesSum);

            // logs.append("Блюдо - ").append(currentDishName).append(" - ").append(currentDishPrice)
            //    .append(". Общая цена ").append(totalDishesSum);

        }

        //  Allure.addAttachment("Блюда","text/plain", logs.toString());

        System.out.println(totalDishesSum + " Сумма за все блюда");
        return totalDishesSum;

    }

    @Step("Считаем сумму всех выбранных позиций в заказе при разделении") //
    public double countAllChosenDishesDivided() {

        double totalSumInOrder = 0;
        int counter = 0;
        String logs = "";

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            if (element.$(".iconCheck").getCssValue("display").equals("block")) {

                double cleanPrice = convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price:last-child"), "\\s₽");
                String dishName = element.$(".orderItem__name").getText();

                totalSumInOrder += cleanPrice;
                counter++;

                logs += counter + ". " + dishName + " - " + cleanPrice + ". Общая сумма: " + totalSumInOrder + "\n";

            }

        }

        System.out.printf(logs);

        double markedDishesSum = convertSelectorTextIntoDoubleByRgx(TipsAndCheck.markedDishesSum, "\\s₽");

        Assertions.assertEquals(markedDishesSum, totalSumInOrder, 0.1);
        System.out.println("Сумма в поле 'Отмеченные позиции' " +
                markedDishesSum + " совпадает с общей чистой суммой заказа " + totalSumInOrder + "\n");

        return totalSumInOrder;

    }

    @Step("Считаем сумму не оплаченных позиций и заблокированных в заказе")
    public double countAllNonPaidAndDisabledDishesInOrder() {

        double totalSumInOrder = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        System.out.println(allNonPaidAndDisabledDishes.size() + " все не оплаченные и блокированные позиции");

        for (SelenideElement element : allNonPaidAndDisabledDishes) {

            double cleanPrice = convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price:last-child"), "\\s₽");
            String dishName = element.$(".orderItem__name").getText();

            totalSumInOrder += cleanPrice;
            counter++;

            logs
                    .append("\n").append(counter).append(". ").append(dishName).append(" - ").append(cleanPrice)
                    .append(". Общая сумма: ").append(totalSumInOrder);

        }

        System.out.println(logs);
        return totalSumInOrder;

    }

    @Step("Считаем сумму не оплаченных,незаблокированных позиций в заказе")
    public double countAllNonPaidDishesInOrder() {

        double totalSumInOrder = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        System.out.println(allNonPaidAndNonDisabledDishes.size() + " все не оплаченные и не заблокированные позиции");

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            double cleanPrice = convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price:last-child"), "\\s₽");
            String dishName = element.$(".orderItem__name").getText();

            totalSumInOrder += cleanPrice;
            counter++;

            logs.append("\n" + counter + ". " + dishName + " - " + cleanPrice + ". Общая сумма: " + totalSumInOrder);

        }

        printAndAttachAllureLogs(logs, "Список не оплаченных и не заблокированных блюд");

        return totalSumInOrder;

    }

    public void printAndAttachAllureLogs(StringBuilder logs, String logsName) {

        System.out.println(logs);
        Allure.addAttachment(logsName, "text/plain" , String.valueOf(logs));

    }

    @Step("Считаем сумму не оплаченных и выбранных позиций в заказе")
    public double countAllNonPaidDAndChosenDishesInOrder() {

        double totalSumInOrder = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        System.out.println(allNonPaidAndNonDisabledDishes.size() + " все не оплаченные и не блокированные позиции");

        for (int index = 0; index < allNonPaidAndNonDisabledDishes.size(); index++) {

            if (allNonPaidAndNonDisabledDishesCheckbox.get(index).getCssValue("display").equals("block")) {

                double cleanPrice = convertSelectorTextIntoDoubleByRgx(allNonPaidAndNonDisabledDishes.get(index)
                        .$(".orderItem__price:last-child"), "\\s₽");
                String dishName = allNonPaidAndNonDisabledDishes.get(index).$(".orderItem__name").getText();

                totalSumInOrder += cleanPrice;
                counter++;

                logs
                    .append("\n").append(counter).append(". ").append(dishName).append(" - ").append(cleanPrice)
                    .append(". Общая сумма: ").append(totalSumInOrder);

            }

        }

        if (discountSum.isDisplayed()) {

            totalSumInOrder -= convertSelectorTextIntoDoubleByRgx(discountSum,"[^\\d\\.]+");

        }

        System.out.println(logs);
        return totalSumInOrder;

    }

    @Step("Забираем коллекцию всех позиций в заказе для следующего теста")
    public HashMap<Integer, Map<String, Double>> getAllDishesAndSetCollection() {

        HashMap<Integer, Map<String, Double>> tapperDishes = new HashMap<>();
        int i = 0;

        System.out.println("\n" + "Количество не оплаченных и не заблокированных блюд : " + allDishesInOrder.size());

        for (SelenideElement element : allDishesInOrder) {

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = element.$(".orderItem__name").getText();
            double price = convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price:last-child"), "\\s₽");

            System.out.println("Блюдо - " + name + " - " + price);

            temporaryMap.put(name, price);
            tapperDishes.put(i, temporaryMap);

            i++;
            System.out.println("Всего блюд: " + i);

        }

        System.out.println(tapperDishes);
        return tapperDishes;

    }

    @Step("Забираем коллекцию всех выбранных позиций в заказе для следующего теста")
    public HashMap<Integer, Map<String, Double>> getChosenDishesAndSetCollection() {

        HashMap<Integer, Map<String, Double>> tapperDishes = new HashMap<>();

        int i = 0;

        System.out.println("\n" + "Количество не оплаченных и не заблокированных блюд : " + allNonPaidAndNonDisabledDishes.size());

        for (SelenideElement element : allDishesInOrder) {

            if (element.$(".iconCheck").getAttribute("style").equals("")) {

                Map<String, Double> temporaryMap = new HashMap<>();

                String name = element.$(".orderItem__name").getText();
                double price = convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price:last-child"), "\\s₽");

                System.out.println("Блюдо - " + name + " - " + price);

                temporaryMap.put(name, price);
                tapperDishes.put(i, temporaryMap);

                i++;
                System.out.println("Выбрано количество блюд : " + i);

            }

        }

        System.out.println(tapperDishes + " выбрано гостем");
        return tapperDishes;

    }

    @Step("Проверяем что выбранные ранее блюда заблокированы и в статусе 'Ожидается'")
    public void checkIfDishesDisabledEarlier(HashMap<Integer, Map<String, Double>> chosenDishesEarlier) {

        System.out.println("\n Выбранные блюда ранее\n" + chosenDishesEarlier);

        HashMap<Integer, Map<String, Double>> chosenDishesCurrent = new HashMap<>();

        int i = 0;

        System.out.println("\n" + "Количество заблокированных блюд : " + disabledDishes.size());

        isElementVisible(separateOrderHeading);

        for (SelenideElement element : allDishesInOrder) {

            if (element.$(".orderItem__status").getText().equals("Оплачивается")) {

                Map<String, Double> temporaryMap = new HashMap<>();

                String name = element.$(".orderItem__name").getText();
                double price = convertSelectorTextIntoDoubleByRgx
                        (element.$(".orderItem__price:last-child"), "\\s₽");

                System.out.println("Блюдо - " + name + " - " + price);

                temporaryMap.put(name, price);
                chosenDishesCurrent.put(i, temporaryMap);

                i++;
                System.out.println("Выбрано количество блюд : " + i);

            }

        }

        System.out.println(chosenDishesEarlier + " ранее заказ");
        System.out.println(chosenDishesCurrent + " текущий заказ");

        Assertions.assertEquals(chosenDishesEarlier, chosenDishesCurrent, "Блюда не совпадают");
        System.out.println("Блюда совпадают");


    }

    @Step("Проверяем коллекцию всех выбранных ранее позиций что они теперь оплачены")
    public void checkIfDishesDisabledAtAnotherGuestArePaid(HashMap<Integer, Map<String, Double>> chosenDishesByAnotherGuest) {

        HashMap<Integer, Map<String, Double>> paidDishes = new HashMap<>();
        int i = 0;

        System.out.println("\n" + "Количество оплаченных и не заблокированных блюд : " + DishList.paidDishes.size());

        for (SelenideElement element : DishList.paidDishes) {

            element.$(".orderItem__status").shouldHave(exist);

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = element.$(".orderItem__name").getText();

            double price = convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price:last-child"), "\\s₽");

            System.out.println("Блюдо - " + name + " - " + price);

            temporaryMap.put(name, price);
            paidDishes.put(i, temporaryMap);

            i++;

        }

        System.out.println(chosenDishesByAnotherGuest + " заказ ранее выбранных позиций");
        System.out.println(paidDishes + " заказ оплаченных позиций");

        Assertions.assertEquals(chosenDishesByAnotherGuest, paidDishes, "Блюда не совпадают");
        System.out.println("Блюда совпадают");

    }

    @Step("Проверяем что оплаченные,заблокированные позиции нельзя снова выбрать")
    public void checkIfPaidAndDisabledDishesCantBeChosen() {

        hidePaymentOptionsAndTapBar();

        int i = 0;

        System.out.println("\n" + "Количество оплаченных и заблокированных блюд : " + disabledAndPaidDishes.size());

        for (SelenideElement element : disabledAndPaidDishes) {

            element.$(".orderItem__status").shouldHave(exist);
            scrollAndClick(element.$(".orderItem__name"));
            element.$(".iconCheck").shouldNotHave(cssValue("display","block"));

            String name = element.$(".orderItem__name").getText();
            double price = convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price:last-child"), "\\s₽");

            System.out.println("Клик в блюдо - " + name + " - " + price + "\nПозиция не сменила статус");

            i++;

        }

        showPaymentOptionsAndTapBar();

    }

    @Step("Проверяем что сумма всех блюд совпадает с итоговой без чаевых и СБ")
    public void isTotalSumInDishesMatchWithTotalPay(double totalSumByDishesInOrder) {

        isElementVisibleDuringLongTime(totalPay, 2);

        double totalPaySumInCheck = convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");

        Assertions.assertEquals(totalPaySumInCheck, totalSumByDishesInOrder, 0.01,
                "Сумма за все блюда не совпала с суммой в 'Итого к оплате' ");
        System.out.println("Сумма за все блюда " + totalSumByDishesInOrder + " совпала с суммой в 'Итого к оплате' "
                + totalPaySumInCheck);

    }

    @Step("Удаляем скидки из суммы")
    public void checkTotalSumCorrectAfterRemovingDiscount() {

        discountField.shouldNotBe(exist,Duration.ofSeconds(2));

        double totalClearOrderAmount = getClearOrderAmount();

        if (!totalTipsSumInMiddle.getValue().equals("0")) {

            resetTips();

        }

        deactivateServiceChargeIfActivated();

        double totalPaySumInCheck = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

        Assertions.assertEquals(totalClearOrderAmount, totalPaySumInCheck, 0.1,
                "Чистая сумма не совпадает с 'Итого к оплате после удаления скидки'");
        System.out.println("Чистая сумма " + totalClearOrderAmount +
                " совпадает с 'Итого к оплате после удаления скидки' " + totalPaySumInCheck);

    }

    @Step("Проверяем что общая чистая сумма совпадает с 'Итого к оплате'")
    public void checkCleanSumMatchWithTotalPay(double cleanDishesSum) {

        double currentTips = getCurrentTipsSum();
        double currentSC = getCurrentSCSum();

        double totalCleanPaySum = countTotalDishesSumWithTipsAndSC(cleanDishesSum, currentTips, currentSC);
        double totalPaySumInCheck = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        double totalPaySumInWallet = convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");
        double totalPaySumInPayButton = convertSelectorTextIntoDoubleByRgx(paymentButton,"[^\\d\\.]+");

        Assertions.assertEquals(totalCleanPaySum, totalPaySumInCheck, 0.1,
                "Чистая сумма не совпадает с суммой в 'Итого к оплате'");
        System.out.println("\nЧистая сумма совпадает с суммой в 'Итого к оплате'\n");

        Assertions.assertEquals(totalPaySumInCheck, totalPaySumInWallet,0.1,
                "Сумма 'Итого к оплате' не совпадает с суммой в счётчике кошелька");
        System.out.println("\nСумма 'Итого к оплате' совпадает с суммой в счётчике кошелька\n");

        Assertions.assertEquals(totalCleanPaySum, totalPaySumInPayButton,0.1,
                "Сумма в кнопке оплатить не совпадает с чистой суммой");
        System.out.println("\nСумма в кнопке оплатить совпадает с чистой суммой\n");

    }

    @Step("Проверяем все варианты чаевых с общей суммой с сб")
    public void checkTipsOptionWithSC(double cleanDishesSum) {

        StringBuilder logs = new StringBuilder(); // toDo вывести логи

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(1));

            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));
            double tipsSumInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");

            Assertions.assertEquals(totalTipsSumInMiddle, tipsSumInCheck,
                    "Чаевые по центру не совпадают с чаевыми в поле 'Чаевые'");
            System.out.println("Чаевые по центру " + totalTipsSumInMiddle +
                    " совпадают с чаевыми в поле 'Чаевые' " + tipsSumInCheck + "");

            double cleanTips = 0;
            int percent = convertSelectorTextIntoIntByRgx(tipsOption, "\\D+");

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;
                cleanTips = cleanDishesSum * percentForCalculation;
                cleanTips = Math.round(cleanTips);

            }

            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
            double totalPaySumInWallet = convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");
            double serviceChargeSum = getCurrentSCSum();
            double totalCleanPaySum = cleanDishesSum + serviceChargeSum + cleanTips;

            Assertions.assertEquals(totalCleanPaySum, totalPaySum, 1,
                    "Чистая общая сумма не совпадает с 'Итого к оплате'");

            Assertions.assertEquals(totalPaySum, totalPaySumInWallet,
                    "Сумма 'Итого к оплате' не совпадает с суммой в счётчике кошелька");
            System.out.println("\nСумма 'Итого к оплате' совпадает с суммой в счётчике кошелька\n");

            System.out.println("\nПроцент чаевых " + percent);
            System.out.println("Чистая общая сумма " + totalCleanPaySum +
                    " совпадает с 'Итого к оплате' " + totalPaySum + "\n");

        }

    }

    @Step("Проверяем все варианты чаевых с общей суммой без сб")
    public void checkTipsOptionWithoutSC(double cleanDishesSum) {

        StringBuilder logs = new StringBuilder(); // toDo вывести логи

        deactivateServiceChargeIfActivated();

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(2));

            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));
            double tipsSumInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");

            Assertions.assertEquals(totalTipsSumInMiddle, tipsSumInCheck,
                    "Чаевые по центру не совпадают с чаевыми в поле 'Чаевые'");
            System.out.println("Чаевые по центру " + totalTipsSumInMiddle +
                    " совпадают с чаевыми в поле 'Чаевые' " + tipsSumInCheck + "");

            double cleanTips = 0;
            int percent = convertSelectorTextIntoIntByRgx(tipsOption, "\\D+");

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;
                cleanTips = cleanDishesSum * percentForCalculation;
                cleanTips = Math.round(cleanTips);

            }

            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
            double totalPaySumInWallet = convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");
            double totalCleanPaySum = cleanDishesSum + cleanTips;

            Assertions.assertEquals(totalCleanPaySum, totalPaySum, 1,
                    "Чистая общая сумма не совпадает с 'Итого к оплате'");

            Assertions.assertEquals(totalPaySum, totalPaySumInWallet,
                    "Сумма 'Итого к оплате' не совпадает с суммой в счётчике кошелька");
            System.out.println("\nСумма 'Итого к оплате' совпадает с суммой в счётчике кошелька\n");

            System.out.println("\nПроцент чаевых " + percent);
            System.out.println("Чистая общая сумма " + totalCleanPaySum +
                    " совпадает с 'Итого к оплате' " + totalPaySum + "\n");

        }

    }

    @Step("Чаевые не должны отображаться у верифицированного официанта без привязанной карты")
    public void checkIsNoTipsElementsIfVerifiedNonCard() {

        isElementVisible(tipsContainer);
        isImageCorrect(waiterImageNotSelenide,"Изображение официанта не корректное или битое");
        isElementVisible(tipsWaiter);
        isElementInvisible(tipsInCheckSum);
        isElementInvisible(totalTipsSumInMiddle);
        isElementInvisible(activeTipsButton);
        isElementInvisible(resetTipsButton);
        tipsListItem.shouldHave(CollectionCondition.size(0));

    }

    @Step("Чаевые не должны отображаться у не верифицированного официанта без привязанной карты")
    public void checkIsNoTipsElementsIfNonVerifiedNonCard() {

        isElementInvisible(tipsContainer);

    }

    @Step("Проверяем что сбор формируется корректно по формуле со всеми чаевыми")
    public void checkScLogic(double cleanDishesSum) {

        activateServiceChargeIfDeactivated();

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", ".*active.*"));

            double tipsSumInTheMiddle = Double.parseDouble(totalTipsSumInMiddle.getValue());

            double serviceChargeInField = convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            serviceChargeInField = convertDouble(serviceChargeInField);

            double serviceChargeSumClear =
                    convertDouble(cleanDishesSum * (SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM / 100));
            System.out.println(serviceChargeSumClear + " сервисный сбор от суммы");

            double serviceChargeTipsClear =
                    convertDouble(tipsSumInTheMiddle * (SERVICE_CHARGE_PERCENT_FROM_TIPS / 100));
            System.out.println(serviceChargeTipsClear + " сервисный сбор от чаевых\n");
            double serviceChargeTotal = serviceChargeTipsClear + serviceChargeSumClear;
            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

            if (totalPaySum > SUM_AFTER_SERVICE_CHARGE_MAXIMIZE) {

                System.out.println(totalPaySum + " из-за неё будет макс СБ");
                serviceChargeTotal = SERVICE_CHARGE_MAX;

            }

            double totalDishesCleanSum =
                    cleanDishesSum + tipsSumInTheMiddle + serviceChargeTotal;

            double totalPaySumInWallet = convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");

            Assertions.assertEquals(serviceChargeInField, serviceChargeTotal, 0.1,
                    "Сервисный сбор считается не корректно по формуле "
                            + SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM + "  от суммы и "
                            + SERVICE_CHARGE_PERCENT_FROM_TIPS + " от чаевых");
            System.out.println("Сервисный сбор считается корректно по формуле "
                    + SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM + "% от суммы "
                    + totalDishesCleanSum + " и " +
                    SERVICE_CHARGE_PERCENT_FROM_TIPS + "% от чаевых " + tipsSumInTheMiddle);

            Assertions.assertEquals(totalDishesCleanSum, totalPaySum, 0.1,
                    "Чистая общая сумма не совпадает с 'Итого к оплате'");

            Assertions.assertEquals(totalDishesCleanSum, totalPaySumInWallet, 0.1,
                    "Сумма 'Итого к оплате' не совпадает с суммой в счётчике кошелька");
            System.out.println("\nСумма 'Итого к оплате' совпадает с суммой в счётчике кошелька\n");

            System.out.println("Чистая общая сумма " + totalDishesCleanSum +
                    " совпадает с 'Итого к оплате' " + totalPaySum + "\n");

        }

    }

    @Step("Проверяем что текущие чаевые в поле 'Чаевые' сходятся с чаевыми по центру. Отдаём чаевые")
    public double getCurrentTipsSum() {

        double tipsInTheMiddleSum = 0;

        if (totalTipsSumInMiddle.exists()) {

            tipsInTheMiddleSum = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
            System.out.println(tipsInTheMiddleSum + " текущие чаевые по центру");

            double tipsInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
            System.out.println(tipsInCheck + " текущие чаевые в поле 'Чаевые'");

            Assertions.assertEquals(tipsInTheMiddleSum, tipsInCheck,
                    "Чаевые по центру не совпадают с чаевыми в поле 'Чаевые'");
            System.out.println("Чаевые по центру " + tipsInTheMiddleSum +
                    " совпадают с чаевыми в поле 'Чаевые' " + tipsInCheck);

        }

        System.out.println(tipsInTheMiddleSum + " чаевые");
        return tipsInTheMiddleSum;

    }

    @Step("Забираем сумму сервисного сбора если он включен")
    public double getCurrentSCSum() {

        scrollTillBottom();
        double serviceChargeSum = 0;

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            serviceChargeSum = convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.\\-]+");
            System.out.println("СБ включены. Сервисный сбор - " + serviceChargeSum);

            Assertions.assertTrue(serviceChargeSum >= 0,
                    "Сервисный сбор имеет отрицательное значение");

            serviceChargeSum = convertDouble(serviceChargeSum);

        }

        System.out.println(serviceChargeSum + " текущий сервисный сбор");
        return serviceChargeSum;

    }

    @Step("Считаем общую сумму за все блюда + чаевые + сбор")
    public double countTotalDishesSumWithTipsAndSC(double cleanDishesSum, double currentTips, double currentSC) {

        cleanDishesSum = cleanDishesSum + currentTips + currentSC;
        System.out.println(cleanDishesSum + " сумма за блюда + чаевые + сб");
        return cleanDishesSum;

    }

    @Step("Проверка что нельзя поделиться блюдом если всего одна позиция в заказ")
    public void disableDivideCheckSliderWithOneDish() {
        divideCheckSlider.shouldNotBe(exist);
    }

    @Step("Устанавливаем рандомные чаевые")
    public void setRandomTipsOption() {

        scrollTillBottom();
        click(resetTipsButton);

        if (notDisabledAndNotZeroTipsPercentOptions.size() != 0) {

            int index = generateRandomNumber(1, notDisabledAndNotZeroTipsPercentOptions.size()) - 1;

            click(notDisabledAndNotZeroTipsPercentOptions.get(index));
            System.out.println(notDisabledAndNotZeroTipsPercentOptions.get(index).getText() + " активные чаевые");
            activeTipsButton
                    .shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(1));

        }

    }

    @Step("Устанавливаем определенные чаевые")
    public void setCertainTipsOption(SelenideElement tips) {

        scrollTillBottom();
        click(resetTipsButton);

        if (notDisabledAndNotZeroTipsPercentOptions.size() != 0) {

            click(tips);
            System.out.println(tips.getText() + " активные чаевые");
            activeTipsButton
                    .shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(1));

        }

    }

    @Step("Сброс чаевых")
    public void resetTips() {

        scrollTillBottom();
        click(resetTipsButton);
        activeTipsButton.shouldHave(text("0%"));

        totalTipsSumInMiddle.shouldHave(value("0"));
        double tipsInTheCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
        Assertions.assertEquals(tipsInTheCheck, 0);

        activeTipsButton.shouldHave(text("0%"));

        int percent = convertSelectorTextIntoIntByRgx(tips0, "\\D+");
        double tipsSumInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
        int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));

        double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        double currentSumInWallet =
                convertSelectorTextIntoDoubleByRgx(TapBar.totalSumInWalletCounter, "\\s₽");

        Assertions.assertEquals(percent, 0, "Текущий процент чаевых не равен 0");
        Assertions.assertEquals(totalTipsSumInMiddle, 0, 0.1, "Общие чаевые в центре не равны 0");
        Assertions.assertEquals(tipsSumInCheck, 0, 0.1, "Чаевые в поле 'Чаевые' не равны 0");

        Assertions.assertEquals(totalPaySum, currentSumInWallet, 0.1,
                "Сумма в 'Итого к оплате' не равна сумме в иконке кошелька");

        System.out.println("Сброс чаевых работает корректно");

    }

    @Step("Проверяем что сумма 'Итого к оплате' совпадает с суммой счетчика в иконке кошелька")
    public void isSumInWalletMatchWithTotalPay() {

        if (!appFooter.exists()) {

            showPaymentOptionsAndTapBar();

            String totalPaySum = convertSelectorTextIntoStrByRgx(totalPay, "\\s₽");
            String currentSumInWallet =
                    convertSelectorTextIntoStrByRgx(TapBar.totalSumInWalletCounter, "\\s₽");

            Assertions.assertEquals(totalPaySum, currentSumInWallet,
                    "Сумма 'Итого к оплате' не совпадает с суммой в иконке кошелька");
            System.out.println("Сумма 'Итого к оплате' совпадает с суммой в иконке кошелька");

        }

    }

    @Step("Проверяем что процент установленных по умолчанию чаевых рассчитывается корректно с СБ и без")
    public void isActiveTipPercentCorrectWithTotalSumAndSC(double totalSumWithoutTips) {

        if (totalTipsSumInMiddle.exists()) {

            deactivateServiceChargeIfActivated();

            isElementVisibleDuringLongTime(activeTipsButton, 15);

            double tipPercent = convertSelectorTextIntoDoubleByRgx(activeTipsButton, "\\D+");
            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
            double totalSumWithTips = totalSumWithoutTips + Math.round(totalSumWithoutTips * (tipPercent / 100));

            System.out.println("\nПроцент чаевых " + tipPercent +
                    "\nСумма в 'Итого к оплате' " + totalPaySum +
                    "\nСумма 'Итого к оплате' вместе с чаевыми " + totalSumWithTips
            );

            Assertions.assertEquals(totalSumWithTips, totalPaySum, 0.1,
                    "Процент чаевых установленный по умолчанию не совпадает с общей ценой за все блюда без СБ");
            System.out.println("Процент чаевых установленный по умолчанию совпадает с общей ценой за все блюда без СБ");

        }

    }

    @Step("Проверка что сумма 'Другой пользователь' совпадает с суммой оплаченных позиций")
    public void isAnotherGuestSumCorrect() {

        anotherGuestField.shouldBe(visible);

        double totalPaidSum = 0;
        double anotherGuestSum =
                convertSelectorTextIntoDoubleByRgx(TipsAndCheck.anotherGuestSum, "[^\\d\\.]+");

        for (SelenideElement element : disabledAndPaidDishes) {

            double currentElementSum =
                    convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price:last-child"), "\\s₽");
            totalPaidSum += currentElementSum;

        }

        Assertions.assertEquals(anotherGuestSum, totalPaidSum, 0.1);
        System.out.println("\nОплаченная сумма другого пользователя " + anotherGuestSum +
                " совпадает с оплаченными позициями " + totalPaidSum + "\n");

    }

    @Step("Проверка что в форме 'Итого к оплате' ")
    public void isCheckContainerShown() {

        isElementVisible(checkContainer);
        isElementVisible(totalPay);

        if (tipsContainer.exists()) {

            isElementVisible(tipsInCheckField);

        }

    }

    @Step("Проверка на ошибку чаевых при вводе значения меньше установленного минимального")
    public void checkCustomTipForError() {

        scrollTillBottom();

        String defaultTips = totalTipsSumInMiddle.getValue();

        click(totalTipsSumInMiddle);
        totalTipsSumInMiddle.clear();
        sendHumanKeys(totalTipsSumInMiddle, TapperTable.MIN_SUM_FOR_TIPS_ERROR);
        tipsErrorMsg.shouldHave(cssValue("display", "block"));
        tipsErrorMsg.shouldHave(text(TapperTable.TIPS_ERROR_MSG));
        paymentButton.shouldBe(disabled);

        totalTipsSumInMiddle.clear();

        assert defaultTips != null;
        sendHumanKeys(totalTipsSumInMiddle, defaultTips);
        tipsErrorMsg.shouldHave(cssValue("display", "none"));

    }

    @Step("Кнопка 'Оплатить' отображается корректно")
    public void isPaymentButtonShown() {

        isElementVisibleAndClickable(paymentButton);

    }

    @Step("Проверка кнопки поделиться счётом, кнопка отображается и вызывает меню шаринга")
    public void isShareButtonShown() {

        isElementVisibleAndClickable(shareButton);

        String isShareButtonCorrect = """
                function check() {
                   if (navigator.share) {
                       return true;
                   } else {
                       return false;
                   }
                }; return check();""";



      //  boolean isShareActive = Boolean.TRUE.equals(Selenide.executeJavaScript(isShareButtonCorrect));
        isImageCorrect(shareButtonSvgNotSelenide,"Иконка в кнопке поделиться счётом не корректная");

      //  Assertions.assertTrue(isShareActive, "Кнопка 'Поделиться счётом' не вызывает панель поделиться ссылкой");
      //  System.out.println("Кнопка 'Поделиться счётом' работает корректно");

    }

    @Step("Проверка что сервисный сбор отображается")
    public void isServiceChargeShown() {

        if (serviceChargeContainer.exists()) {

            isElementVisible(serviceChargeContainer);

        }

    }

    @Step("Проверка политики конфиденциальности")
    public void isConfPolicyShown() { //

        scrollTillBottom();
        isElementVisible(confPolicyContainer);
        click(confPolicyContainer);
        isElementVisible(confPolicyContent);
        confPolicyContent.shouldHave(matchText("УСЛОВИЯ ИСПОЛЬЗОВАНИЯ И ПОЛИТИКА КОНФИДЕНЦИАЛЬНОСТИ TAPPER"));
        Selenide.executeJavaScript("document.querySelector('.mainPage .privacyPolicyModal').style.display = 'none';");
        isElementInvisible(confPolicyContent);

    }

    @Step("Клик по кнопке оплаты")
    public void clickOnPaymentButton() {

        changePaymentTypeOnCard();
        click(paymentButton);

    }

    @Step("Отображается лоадер на странице")
    public void isPageLoaderShown() {

        pagePreLoader.shouldNotHave(cssValue("display", "none"));

    }

    @Step("Смена оплаты на CБП")
    public void changePaymentTypeOnSBP() {

        click(paymentOptionsContainer);
        click(paymentOptionSBP);
        click(paymentContainerSaveButton);
        paymentChosenTypeText.shouldHave(matchText("Система быстрых платежей"));

    }

    @Step("Проверка формы СБП")
    public void isPaymentSBPCorrect() {

        changePaymentTypeOnSBP();
        click(paymentButton);

        isElementVisible(paymentSBPContainer);
        isElementsListVisible(paymentBanksPriorityBanks);
        paymentBanksPriorityBanks.shouldHave(CollectionCondition.size(TapperTable.PAYMENT_BANKS_MAX_PRIORITY_BANKS));
        isElementVisible(paymentBanksAllBanksButton);
        isElementVisible(paymentBanksDescription);
        isElementVisible(paymentBanksReceipt);

        isReceiptEmailCorrect();

    }

    @Step("Проверка поля почты для чека")
    public void isReceiptEmailCorrect() {

        emailReceiptInput.shouldBe(hidden);
        click(paymentBanksReceipt);
        emailReceiptInput.shouldBe(appear);

        emailReceiptInput.sendKeys(Yandex.TEST_YANDEX_LOGIN_EMAIL);
        emailReceiptInput.shouldHave(value(Yandex.TEST_YANDEX_LOGIN_EMAIL));

        click(paymentBanksReceipt);
        click(paymentBanksReceipt);

        emailReceiptInput.shouldHave(value(Yandex.TEST_YANDEX_LOGIN_EMAIL));
        clearText(emailReceiptInput);
        emailReceiptInput.sendKeys(TapperTable.TEST_REVIEW_COMMENT);
        emailReceiptErrorMsg.shouldBe(visible);
        clearText(emailReceiptInput);

    }

    @Step("Прерывание процесса оплаты")
    public void cancelProcessPaying() {

        isCancelPaymentcontainerCorrect();

        click(cancelProcessPayingContainerSaveBtn);

        cancelProcessPayingContainer.shouldBe(disappear);

        click(paymentButton);

        click(paymentOverlay);
        click(cancelProcessPayingContainerCancelBtn);

        isElementVisible(paymentSBPContainer);
        cancelProcessPayingContainer.shouldBe(disappear);

    }

    @Step("Проверка модального окна прерывания оплаты")
    public void isCancelPaymentcontainerCorrect() {

        isElementVisible(cancelProcessPayingContainer);
        isElementVisible(cancelProcessPayingContainerCancelBtn);
        isElementVisible(cancelProcessPayingContainerSaveBtn);

    }

    @Step("Смена оплаты на кредитную карту")
    public void changePaymentTypeOnCard() {

        paymentOptionsContainer.shouldBe(visible.because("Нет выбора способа оплаты"));
        click(paymentOptionsContainer);
        click(paymentOptionCreditCard);
        click(paymentContainerSaveButton);
        paymentChosenTypeText.shouldHave(matchText("Банковская карта"));

    }

    @Step("Проверка смены оплаты, отмена оплаты")
    public void isPaymentOptionsCorrect() {

        isPaymentSBPCorrect();

        click(paymentOverlay);

        cancelProcessPaying();

        click(paymentOverlay);
        click(cancelProcessPayingContainerSaveBtn);

        changePaymentTypeOnCard();
        changePaymentTypeOnSBP();

    }

    @Step("Сохранение общей суммы в таппере для проверки суммы в б2п")
    public double saveTotalPayForMatchWithAcquiring() {
        return convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
    }

    @Step("Получаем чистую сумму за вычетом всех сб,чаевых,скидки,наценки,оплаченных ранее")
    public double getClearOrderAmount() {

        double serviceChargeSum;
        double tips;
        double anotherGuestSumPaid;
        double discount = 0;
        double orderAmount;

        if (serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            System.out.println("Для подсчёта общей суммы включён сервисный сбор");
            serviceChargeSum = convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            serviceChargeSum = convertDouble(serviceChargeSum);
            System.out.println(serviceChargeSum + " сервисный сбор");

        }

        if (totalTipsSumInMiddle.exists()) {

            System.out.println("Для подсчёта общей суммы есть чаевые");
            tips = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
            System.out.println(tips + " чаевые");

        }

        if (anotherGuestSum.isDisplayed()) {

            System.out.println(anotherGuestSum);
            System.out.println(anotherGuestSum.getText() + " текст в другом пользователе");
            anotherGuestSumPaid = convertSelectorTextIntoDoubleByRgx(anotherGuestSum,"[^\\d\\.]+");
            System.out.println(anotherGuestSumPaid);

        }

        if (discountSum.isDisplayed()) {

            discount = convertSelectorTextIntoDoubleByRgx(discountSum,"[^\\d\\.]+");

        }

        if (divideCheckSliderActive.isDisplayed()) {

            orderAmount = countAllNonPaidDAndChosenDishesInOrder();
            System.out.println("Сумма выбранных позиций считается из разделенного счёта");

        } else {

            orderAmount = countAllNonPaidDishesInOrder();
            System.out.println("Сумма всех не оплаченных и не заблокированных позиций считается если счёт не разделён");

        }

        orderAmount = orderAmount - discount;
        orderAmount = convertDouble(orderAmount);

        return orderAmount;

    }

    @Step("Получаем чистую сумму за вычетом всех сб,чаевых,скидки,наценки,оплаченных ранее")
    public double getOrderAmountForOperationHistory() {

        double serviceChargeSum = 0;
        double tips = 0;

        if (serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            System.out.println("Для подсчёта общей суммы включён сервисный сбор");
            serviceChargeSum = convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            serviceChargeSum = convertDouble(serviceChargeSum);
            System.out.println(serviceChargeSum + " сервисный сбор");

        }

        if (totalTipsSumInMiddle.exists()) {

            System.out.println("Для подсчёта общей суммы есть чаевые");
            tips = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
            System.out.println(tips + " чаевые");

        }

        double orderAmount = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

        orderAmount = orderAmount - tips - serviceChargeSum;
        orderAmount = convertDouble(orderAmount);
        System.out.println(orderAmount + " чистая сумма");

        return orderAmount;

    }

    @Step("Сохранение общей суммы, чаевых, СБ для проверки с транзакцией б2п")
    public HashMap<String, Integer> savePaymentDataTapperForB2b() {

        HashMap<String, Integer> paymentData = new HashMap<>();

        int tips = 0;
        int fee = 0;

        double order_amountD = getOrderAmountForOperationHistory();

        order_amountD  = order_amountD * 100;
        Integer order_amount = (int) order_amountD;

        paymentData.put("order_amount", order_amount);

        if (totalTipsSumInMiddle.exists()) {

            double tipsD = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));

            tips = (int) (tipsD) * 100;

        }

        paymentData.put("tips", tips);

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            double feeD = convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            feeD = convertDouble(feeD) * 100;
            fee = (int) feeD;

        }

        paymentData.put("fee", fee);
        System.out.println(paymentData + " данные для б2п");
        return paymentData;

    }

    @Step("Проверка нижнего навигационного меню. Проверки на элементы, кликабельность, переходы, открытие")
    public void isTapBarShown() {

        isElementVisible(appFooter);
        isElementVisibleAndClickable(appFooterMenuIcon);
        isElementVisibleAndClickable(totalSumInWalletCounter);

        click(appFooterMenuIcon);
        isElementVisible(orderMenuContainer);

        isElementInvisible(orderContainer);

        click(totalSumInWalletCounter);

        isElementVisible(orderContainer);
        isElementInvisible(orderMenuContainer);

    }

    @Step("Открытие формы вызова официанта")
    public void openCallWaiterForm() {

        isElementVisibleAndClickable(callWaiterButton);
        click(callWaiterButton);

        isElementVisible(callWaiterContainer);

    }

    @Step("Проверка элементов формы вызова официанта")
    public void isCallContainerWaiterCorrect() {

        isElementVisible(callWaiterFadedBackground);
        isElementVisible(callWaiterHeading);
        isElementVisible(callWaiterCommentArea);
        isElementVisible(callWaiterTypingMessagePreloader);
        callWaiterTypingMessagePreloader.shouldHave(disappear,Duration.ofSeconds(5));
        isElementVisibleDuringLongTime(callWaiterFirstGreetingsMessage,5);

    }

    @Step("Проверка меню")
    public void emptyMenu() {

        isElementVisible(emptyTableLogoClock);
        isElementVisible(emptyOrderMenuDescription);
        emptyOrderMenuButton.shouldNotBe(disabled);

        click(emptyOrderMenuButton);
        isElementVisible(thanksFeedBackAlert);

    }

    @Step("Закрытие формы по крестику закрытия")
    public void closeCallWaiterFormByCloseButton() {

        click(callWaiterCloseButton);
        isElementInvisible(callWaiterContainer);

    }

    @Step("Написать 'счет' чтобы получить специальное сообщение от таппера")
    public void typeTextToGetSpecialMessage() {

        sendHumanKeys(callWaiterCommentArea, "Счет");
        callWaiterCommentArea.shouldHave(value("Счет"));

        isElementVisible(callWaiterButtonSend);
        click(callWaiterButtonSend);
        forceWait(2000);
        isElementVisibleDuringLongTime(callWaiterSecondMessage,3);

        sendHumanKeys(callWaiterCommentArea, "Счет");
        callWaiterCommentArea.shouldHave(value("Счет"));

        isElementVisible(callWaiterButtonSend);
        click(callWaiterButtonSend);
        forceWait(2000);
        isElementVisibleDuringLongTime(callWaiterUniversalTextMessage,3);

    }

    @Step("Отправка текста в комментарий официанта")
    public void sendWaiterComment() {

        callWaiterCommentArea.shouldHave(attribute("placeholder", "Cообщение"));
        sendHumanKeys(callWaiterCommentArea, TapperTable.TEST_WAITER_COMMENT);
        callWaiterCommentArea.shouldHave(value(TapperTable.TEST_WAITER_COMMENT));

        isElementVisible(callWaiterButtonSend);
        click(callWaiterButtonSend);

    }

    @Step("Ввод текста в комментарий официанта")
    public void isSendSuccessful() {

        isElementVisible(callWaiterGuestTestComment);
        isElementVisible(callWaiterSecondMessage);

    }

    @Step("Повторное открытие формы вызовы официанта для проверки что история сохранилась")
    public void isHistorySaved() {

        isElementVisible(callWaiterGuestTestComment);
        isElementVisible(callWaiterSecondMessage);
        isElementInvisible(callWaiterTypingMessagePreloader);

    }

    @Step("Закрытие и открытие браузера для проверки что история сохранилась")
    public void isHistorySavedByClosingBrowser(String url) {

        Cookie itemMess = WebDriverRunner.getWebDriver().manage().getCookieNamed("itemsMess");
        Selenide.closeWebDriver();

        openUrlAndWaitAfter(url);
        WebDriverRunner.getWebDriver().manage().addCookie(itemMess);

        click(callWaiterButton);
        isHistorySaved();

    }

    @Step("Вызов официанта. Проверки на элементы, кликабельность, заполнение, открытие\\закрытие")
    public void isCallWaiterCorrect() {

        openCallWaiterForm();

        isCallContainerWaiterCorrect();

        sendWaiterComment();

        forceWait(1000);

        closeCallWaiterFormByCloseButton();

        click(callWaiterButton);

        isSendSuccessful();

        click(callWaiterCloseButton);

        click(callWaiterButton);

        isHistorySaved();

        typeTextToGetSpecialMessage();

        click(callWaiterCloseButton);

    }

    @Step("Подмена куки юзера")
    public void setUserCookie(String guest, String session) {

        WebDriverRunner.getWebDriver().manage().deleteCookieNamed("guest");
        Cookie cookieGuest = new Cookie("guest", guest);
        WebDriverRunner.getWebDriver().manage().addCookie(cookieGuest);
        WebDriverRunner.getWebDriver().manage().deleteCookieNamed("session");
        Cookie cookieSession = new Cookie("session", session);
        WebDriverRunner.getWebDriver().manage().addCookie(cookieSession);

        refreshPage();

        //closeHintModal();

    }

    @Step("Сохраняем информацию по меню со стола в таппере")
    public HashMap<String,Map<String,String>> saveTapperMenuData() {

        HashMap<String,Map<String,String>> menuData = new HashMap<>();

        for (SelenideElement selenideElement : menuCategoryContainerName) {

            String categoryName = selenideElement.getText();
            System.out.println(categoryName + " имя категории");

            String dishSizeXpath =
                    "//*[@class='orderMenuList']//*[@class='orderMenuList__item'][.//*[contains(text(),'" +
                            categoryName + "')]]//*[@class='orderMenuProductList__item']";

            ElementsCollection dishElement = $$x(dishSizeXpath);

            int dishSize = dishElement.size();
            System.out.println(dishSize + " количество блюд");

            Map<String, String> dishList = new HashMap<>();

            for (SelenideElement element : dishElement) {

                String dishName = element.$(".orderMenuProduct__name").getText();
                System.out.println(dishName + " имя блюда");

                String dishPrice = element.$(".orderMenuProduct__price").getText().replaceAll("\\s₽", "");
                System.out.println(dishPrice + " цена блюда");

                String dishImage = "";

                if (element.$(".orderMenuProduct__photo img").exists()) {

                    dishImage = element.$(".orderMenuProduct__photo img").getAttribute("src");

                }

                System.out.println(dishImage + " фото блюда");

                dishList.put("name", dishName);
                dishList.put("price", dishPrice);
                dishList.put("image", dishImage);

            }

            menuData.put(categoryName, dishList);

        }

        System.out.println(menuData);
        return menuData;

    }

    @Step("Сохранения данных для проверки истории операции в админке")
    public HashMap<Integer, HashMap<String,String>> saveOrderDataForOperationsHistoryInAdmin() {

        HashMap<Integer, HashMap<String,String>> orderData = new HashMap<>();
        HashMap<String,String> temporaryHashMap = new HashMap<>();

        System.out.println(tableNumber);
        String table = convertSelectorTextIntoStrByRgx(tableNumber,"Стол ");
        String name = waiterName.getText();
        String tips = convertSelectorTextIntoStrByRgx(tipsInCheckField,"[^\\d\\.]+");

        if (Objects.equals(tips, "")) {

            tips = "0";

        }

        double totalSum = getOrderAmountForOperationHistory();

        String totalSumStr = String.valueOf(totalSum).replaceAll("\\..+","");

        String date = getCurrentDateInFormat("dd.MM.yyyy");

        temporaryHashMap.put("date",date);
        temporaryHashMap.put("table",table);
        temporaryHashMap.put("name",name);
        temporaryHashMap.put("tips",tips);
        temporaryHashMap.put("totalSum",totalSumStr);

        orderData.put(0,temporaryHashMap);

        System.out.println(orderData);

        return orderData;

    }

    @Step("Сопоставление данных из стола и с операцией в истории операции в админке")
    public void matchTapperOrderDataWithAdminOrderData(HashMap<Integer, HashMap<String,String>> tapperOrderData,
                                                       HashMap<Integer, HashMap<String,String>> adminOrderData) {

        System.out.println("\n TAPPER\n" + tapperOrderData + "\n");
        System.out.println("\n ADMIN\n" + adminOrderData + "\n");

        boolean hasOperationMatch = false;

        for (int index = 0; index < adminOrderData.size(); index++) {

            if (tapperOrderData.get(0).equals(adminOrderData.get(index))) {

                hasOperationMatch = true;
                break;

            }

        }

        Assertions.assertTrue(hasOperationMatch,"Оплаты нет есть в списке операций");
        System.out.println("Оплата есть в списке операций");

    }

    @Step("Открытие стола и смена гостя")
    public void openTableAndSetGuest(String table, String guest, String session) {

        openUrlAndWaitAfter(table);
        setUserCookie(guest,session);

    }

    @Step("Закрываем заказ по API")
    public void closeOrderByAPI(String guid, String restaurantName, String tableId, String apiUri) {

        System.out.println(guid + " guid");

        apiRKeeper.orderPay(rqParamsOrderPay(restaurantName,guid), apiUri);

        Assertions.assertTrue(apiRKeeper.isClosedOrder(restaurantName,tableId,apiUri),"Заказ не закрылся на кассе");
        System.out.println("\nЗаказ закрылся на кассе\n");

    }

    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public
    LinkedHashMap<String, String> getReviewTgMsgData(String guid, int waitingTime) {

        forceWait(waitingTime); // ожидание когда придёт телеграмм сообщение. toDo сделать поллинг пришло ли сообщение в тг

        String notFoundTgMsg = "";

        String msg= telegram.getLastTgPayMsgList(guid);

        if (msg == null) {

            System.out.println("Сообщение не найдено, отправится второй запрос");
            forceWait(waitingTime);
            msg= telegram.getLastTgPayMsgList(guid);
            notFoundTgMsg = " . После ожидания " + waitingTime * 2 / 1000 + " сек сообщение в телеграмме не появилоась";

        }

        Assertions.assertNotNull(msg,"Нет сообщения в телеграмме по этому заказу" + notFoundTgMsg);
        System.out.println("Сообщение появилось в телеграмме");

        Allure.addAttachment("telegram message", String.valueOf(msg));

        HashMap <String,String> msgWithType = telegram.setMsgTypeFlag(msg);

        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

        return parsedMsg;

    }

    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public
    LinkedHashMap<String, String> getPaymentTgMsgData(String guid, int waitingTime) {

        forceWait(waitingTime); // ожидание когда придёт телеграмм сообщение. toDo сделать поллинг пришло ли сообщение в тг

        String notFoundTgMsg = "";

        String msg= telegram.getLastTgPayMsgList(guid);

        if (msg == null) {

            System.out.println("Сообщение не найдено, отправится второй запрос");
            forceWait(waitingTime);
            msg= telegram.getLastTgPayMsgList(guid);
            notFoundTgMsg = " . После ожидания " + waitingTime * 2 / 1000 + " сек сообщение в телеграмме не появилоась";

        }

        Assertions.assertNotNull(msg,"Нет сообщения в телеграмме по этому заказу" + notFoundTgMsg);
        System.out.println("Сообщение появилось в телеграмме");

        Allure.addAttachment("telegram message", String.valueOf(msg));

        HashMap <String,String> msgWithType = telegram.setMsgTypeFlag(msg);

        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

        return parsedMsg;

    }

    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public
    LinkedHashMap<String, String> getCallWaiterTgMsgData(String tableNumber, int waitingTime) {

        forceWait(waitingTime); // ожидание когда придёт телеграмм сообщение. toDo сделать поллинг пришло ли сообщение в тг

        String notFoundTgMsg = "";

        String msg= telegram.getLastTgWaiterMsgList(tableNumber);

        if (msg == null) {

            System.out.println("Сообщение не найдено, отправится второй запрос");
            forceWait(waitingTime);
            msg= telegram.getLastTgWaiterMsgList(tableNumber);
            notFoundTgMsg = " . После ожидания " + waitingTime * 2 / 1000 + " сек сообщение в телеграмме не появилоась";

        }

        Assertions.assertNotNull(msg,"Нет сообщения в телеграмме по этому заказу" + notFoundTgMsg);
        System.out.println("Сообщение появилось в телеграмме");

        HashMap <String,String> msgWithType = telegram.setMsgTypeFlag(msg);

        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

        return parsedMsg;

    }

    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public void getAdminSendingMsgData(String tableId, String textInSending, int waitingTime) {

        forceWait(waitingTime); // ожидание когда придёт телеграмм сообщение. toDo сделать поллинг пришло ли сообщение в тг

        String notFoundTgMsg = "";

        String msg = telegram.getLastTgPayMsgList(textInSending);

        if (msg == null || msg == "") {

            System.out.println("Сообщение не найдено, отправится второй запрос");
            forceWait(waitingTime);
            msg = telegram.getLastTgPayMsgList(textInSending);
            notFoundTgMsg = " . После ожидания " + waitingTime * 2 / 1000 + " сек сообщение в телеграмме не появилоась";

        }

        Assertions.assertNotEquals(msg,"","Нет сообщения в телеграмме по этому типу рассылки" + notFoundTgMsg);
        System.out.println("Сообщение появилось в телеграмме");

    }

    @Step("Сбор данных со стола для проверки с телеграм сообщением")
    public LinkedHashMap<String, String> getTapperDataForTgCallWaiterMsg(String callWaiterComment, String tableNumber) {

        LinkedHashMap<String, String> tapperDataForTgMsg = new LinkedHashMap<>();

        tapperDataForTgMsg.put("tableNumber",tableNumber);
        tapperDataForTgMsg.put("callWaiterComment",callWaiterComment);

        return tapperDataForTgMsg;

    }

    @Step("Сбор данных со стола для проверки с телеграм сообщением")
    public LinkedHashMap<String, String> getTapperDataForTgPaymentMsg(String tableId) {

        double sumInCheckDouble = countAllDishes();
        System.out.println(sumInCheckDouble + " Сумма в чеке");

        double discountDouble = 0;

        if (discountSum.isDisplayed()) {

            discountDouble = convertSelectorTextIntoDoubleByRgx(discountSum,"[^\\d\\.]+");

        }

        sumInCheckDouble += discountDouble;

        double paySumDouble = getClearOrderAmount() + discountDouble;
        System.out.println(paySumDouble + " paySumDouble");

        double restToPayDouble = countAllNonPaidAndDisabledDishesInOrder() - paySumDouble;
        System.out.println("Осталось оплатить: " + restToPayDouble);


        double tipsInTheMiddle = 0;
        String tipsInTheMiddleString = "0";

        if (totalTipsSumInMiddle.isDisplayed()) {

            tipsInTheMiddleString = totalTipsSumInMiddle.getValue();
            tipsInTheMiddle = Double.parseDouble(tipsInTheMiddleString);

        } else {

            tipsInTheMiddle = 0;

        }

        String tipsFinal;

        if (serviceChargeCheckboxSvg.getCssValue("display").equals("none")) {

            System.out.println("СБ не включен, у официанта будут чаевые минусом СБ");

            double serviceChargeSumDouble = Double.parseDouble(tipsInTheMiddleString)
                    / 100 * Constants.SERVICE_CHARGE_PERCENT_WHEN_DEACTIVATED;

            BigDecimal bd = new BigDecimal(Double.toString(serviceChargeSumDouble));
            BigDecimal serviceChargeSum = bd.setScale(2, RoundingMode.HALF_UP);

            tipsInTheMiddle = Double.parseDouble(tipsInTheMiddleString);
            tipsInTheMiddle -= Double.parseDouble(String.valueOf(serviceChargeSum));

            System.out.println("Чаевые: " + tipsInTheMiddle);

            if (tipsInTheMiddle != 0) {

                tipsFinal = String.valueOf(tipsInTheMiddle);

            } else {

                tipsFinal = String.valueOf(tipsInTheMiddle).replaceAll("\\.\\d+", "");

            }

        } else {

            tipsFinal = String.valueOf(tipsInTheMiddle).replaceAll("\\.\\d+", "");

        }

        System.out.println("Сумма оплаты: " + paySumDouble);

        Response rsGetOrder = apiRKeeper.getOrderInfo(tableId, AUTO_API_URI);
        System.out.println(rsGetOrder.body());

        double totalPaidDouble = countAllNonPaidDishesInOrder();
        String payStatus;
        String orderStatus;

         if (rsGetOrder.path("@attributes.prepaySum") == null) {

            totalPaidDouble = paySumDouble;
            System.out.println("Всего оплачено " + totalPaidDouble +  ". Первая оплата");

        } else  {

            String unpaidSumString = rsGetOrder.jsonPath().getString("@attributes.prepaySum");
            totalPaidDouble = paySumDouble + Double.parseDouble(unpaidSumString) / 100;
            System.out.println("Всего оплачено " + totalPaidDouble +  ". Промежуточная оплата");

        }

        payStatus = "Частично оплачено";
        orderStatus = "Предоплата успешно прошла по кассе";

        if (totalPaidDouble == sumInCheckDouble || (totalPaidDouble + discountDouble) == sumInCheckDouble) {

            payStatus = "Полностью оплачено";
            orderStatus = "Успешно закрыт на кассе";
            System.out.println("Всего оплачено " + totalPaidDouble +  ". Последняя оплата");

        }

        String waiter;

        if (tipsContainer.isDisplayed()) {

            waiter = waiterName.getText();

        } else {

            waiter = "Неизвестный официант";

        }

        LinkedHashMap<String, String> tapperDataForTgMsg = new LinkedHashMap<>();

        int sumInCheck = (int) sumInCheckDouble;
        int paySum = (int) paySumDouble;
        int totalPaid = (int) totalPaidDouble;
        int restToPay = (int) restToPayDouble;


        String tableString = tableNumber.getText().replaceAll("(.*Стол\\s)(\\d{3})(.*)","$2");

        tapperDataForTgMsg.put("table", tableString);
        tapperDataForTgMsg.put("sumInCheck",String.valueOf(sumInCheck));
        tapperDataForTgMsg.put("restToPay",String.valueOf(restToPay));
        tapperDataForTgMsg.put("tips",tipsFinal);
        tapperDataForTgMsg.put("paySum",String.valueOf(paySum));
        tapperDataForTgMsg.put("totalPaid",String.valueOf(totalPaid));

        if (discountSum.isDisplayed()) {

            String table = WebDriverRunner.getWebDriver().getCurrentUrl().replaceAll(".*\\/","");
            int discount = (int) getDiscount(table);
            System.out.println(discount + " discount");
            tapperDataForTgMsg.put("discount",String.valueOf(discount));

        }

        tapperDataForTgMsg.put("payStatus",payStatus);
        tapperDataForTgMsg.put("orderStatus",orderStatus);
        tapperDataForTgMsg.put("waiter",waiter);

        System.out.println(tapperDataForTgMsg);

        return tapperDataForTgMsg;

    }

    @Step("Сравнение данных сообщения из тг и таппера")
    public void matchTgMsgDataAndTapperData(LinkedHashMap<String, String> telegramDataForTgMsg,
                                            LinkedHashMap<String, String> tapperDataForTgMsg) {


        System.out.println(telegramDataForTgMsg);
        System.out.println(tapperDataForTgMsg);

        Assertions.assertNotNull(telegramDataForTgMsg,"Пустые данные по сообщению из тг");
        Assertions.assertNotNull(tapperDataForTgMsg,"Пустые данные по тапперу");

        if (telegramDataForTgMsg.containsValue("orderStatus") && telegramDataForTgMsg.get("orderStatus").matches("(?s).*[\\n\\r].*SeqNumber.*")) {

            System.out.println("\nОшибка SeqNumber. Удаляем из карты поле\n");
            telegramDataForTgMsg.remove("orderStatus");
            telegramDataForTgMsg.remove("reasonError");
            tapperDataForTgMsg.remove("orderStatus");

        }

        System.out.println("\n TELEGRAM DATA\n" + telegramDataForTgMsg + "\n TAPPER DATA\n" + tapperDataForTgMsg);

        Assertions.assertEquals(telegramDataForTgMsg,tapperDataForTgMsg,
                "Сообщение в телеграмме не корректное");
        System.out.println("Сообщение в телеграмме полностью соответствует столу");

    }

    @Step("Получаем скидку")
    public double getDiscount(String tableId) {

        Response rsGetOrder = apiRKeeper.getOrderInfo(tableId, AUTO_API_URI);
        return Math.abs(rsGetOrder.jsonPath().getDouble("@attributes.discountSum") / 100);

    }

    @Step("Проверка вайфая на столе")
    public void checkWiFiOnTapperTable(String wifiName,String wifiPassword) {

        isElementVisibleAndClickable(wiFiIcon);
        click(wiFiIcon);

        isWifiContainerCorrect();

        String wifiPasswordText = wiFiPassword.getText().replaceAll("Пароль: ","");
        String wifiNameText = wiFiName.getText().replaceAll("Сеть: ","");

        Assertions.assertEquals(wifiPassword,wifiPasswordText,
                "Пароль wifi не совпадет с установленным в админке");
        System.out.println("Пароль wifi совпадет с установленным в админке");

        Assertions.assertEquals(wifiName,wifiNameText,"Имя wifi совпадет с установленным в админке");
        System.out.println("Имя wifi совпадет с установленным в админке");

        click(wiFiPassword);
        wiFiPassword.shouldHave(text("Скопировано"));

      //  clipboard().shouldHave(content(wifiPasswordText),Duration.ofSeconds(5)); toDO in headless mode doesnt work clipboard. Разобраться почему отвалилось
      //  System.out.println("Текст успешно скопирован в буфер обмена");

        click(wiFiCloseButton);
        isElementInvisible(wiFiContainer);

    }

    @Step("Проверка корректности вайфай контейнера")
    public void isWifiContainerCorrect() {

        isElementVisible(wiFiContainer);
        isElementVisible(wiFiHeader);
        isElementVisible(wiFiCloseButton);
        isElementVisible(wiFiName);
        isElementVisible(wiFiPassword);

    }

    public void forceWaitingForSocketChangePositions(int ms) {

        forceWait(ms);

    }

}


