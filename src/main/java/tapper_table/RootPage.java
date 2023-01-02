package tapper_table;

import com.codeborne.selenide.*;
import common.BaseActions;
import io.qameta.allure.Allure;
import io.qameta.allure.Flaky;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Cookie;
import tapper_admin_personal_account.operations_history.OperationsHistory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$x;
import static constants.Constant.JSScripts.isShareButtonCorrect;
import static constants.Constant.TestData.*;
import static constants.selectors.TapperTableSelectors.Common.*;
import static constants.selectors.TapperTableSelectors.RootPage.*;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.*;
import static constants.selectors.TapperTableSelectors.RootPage.Menu.menuCategoryContainerName;
import static constants.selectors.TapperTableSelectors.RootPage.PayBlock.*;
import static constants.selectors.TapperTableSelectors.RootPage.TapBar.*;
import static constants.selectors.TapperTableSelectors.RootPage.TipsAndCheck.*;

public class RootPage extends BaseActions {

    BaseActions baseActions = new BaseActions();
    OperationsHistory operationsHistory = new OperationsHistory();

    @Step("Сбор всех блюд со страницы таппера и проверка с блюдами на кассе")
    public void matchTapperOrderWithOrderInKeeper(HashMap<Integer, Map<String, Double>> allDishesInfoFromKeeper) {

        HashMap<Integer, Map<String, Double>> tapperDishes = new HashMap<>();

        int i = 0;

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = element.$(".orderItem__name").getText();
            double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price"), "\\s₽");

            temporaryMap.put(name, price);
            tapperDishes.put(i, temporaryMap);

            i++;

        }

        Assertions.assertEquals(allDishesInfoFromKeeper, tapperDishes, " не совпадает список в заказе");
        System.out.println("Позиции и суммы на кассе полностью совпадают с тем что на столе таппера");

    }

    @Step("Получаем информацию о заказе с кассы")
    public HashMap<Integer, Map<String, Double>> getOrderInfoFromKeeperAndConvToHashMap(Response rs) {

        HashMap<Integer, Map<String, Double>> allDishesInfo = new HashMap<>();

        int i = 0;

        System.out.println(rs.jsonPath().getString("Session.Dish['@attributes'].name"));
        System.out.println(rs.jsonPath().getString("Session.Dish['@attributes'].price"));

        System.out.println(rs.jsonPath().getList("Session").size() + " session size");

        for (; i < rs.jsonPath().getList("Session").size(); i++) {

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = rs.jsonPath().getString("Session[" + i + "].Dish.name");
            double priceStringJSON = rs.jsonPath().getDouble("Session[" + i + "].Dish.price");
            double priceDouble = baseActions.convertDouble(priceStringJSON) / 100;

            temporaryMap.put(name, priceDouble);
            allDishesInfo.put(i, temporaryMap);

        }

        System.out.println(allDishesInfo);
        return allDishesInfo;

    }

    @Step("Переход на страницу {url} и ждём принудительно пока не прогрузятся все скрипты\\элементы\\сокет")
    public void openTapperTable(String url) {

        baseActions.openPage(url);
        baseActions.forceWait(TIME_WAIT_FOR_FULL_LOAD);
    }

    @Step("Проверки что стол освободился, статусы в заголовке корректные")
    public void isEmptyOrderAfterClosing() {

        baseActions.isElementVisible(emptyOrderHeading);
        // emptyOrderHeading.shouldHave(text("Заказ успешно оплачен"), Duration.ofSeconds(5)); //toDO слишком быстро появляется этот заголовок, на заднем фоне. подумать
        System.out.println("Заказ успешно оплачен");
        emptyOrderHeading.shouldHave(text("Скоро здесь появится ваш заказ"), Duration.ofSeconds(10));
        System.out.println("Скоро здесь появится ваш заказ");

        double totalSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");
        Assertions.assertEquals(totalSumInWallet, 0, "Сумма в кошельке не равно 0");

    }


    @Step("Первичная заставка\\лого\\анимация при открытии страницы")
    public void isStartScreenShown() {
        startScreenLogoContainer.shouldNotHave(cssValue("display", "flex"), Duration.ofSeconds(5));
    }

    @Step("Переключаем на разделение счёта, ждём что все статусы прогружены у позиций")
    public void clickDivideCheckSlider() {

        baseActions.forceWait(1000);  // toDO если после активации раздельного меню сразу выбрать позицию, то гарантировано 422, поэтому ждём
        baseActions.isElementVisibleDuringLongTime(divideCheckSlider, 5);
        baseActions.click(divideCheckSlider);

    }

    @Step("Проверка функционала кнопки разделения счётом")
    public void isDivideSliderCorrect() {

        baseActions.isElementVisibleDuringLongTime(divideCheckSlider, 15);
        baseActions.click(divideCheckSlider);

        divideCheckSliderActive.shouldBe(visible);
        baseActions.isElementsListVisible(allDishesCheckboxes);
        allDishesStatuses.filterBy(cssValue("display", "flex"))
                .shouldBe(CollectionCondition.sizeGreaterThan(0));

        baseActions.click(divideCheckSliderActive);
        baseActions.isElementsListInVisible(allDishesCheckboxes);
        allDishesStatuses.filterBy(cssValue("display", "none"))
                .shouldBe(CollectionCondition.sizeGreaterThan(0));

       // baseActions.forceWait(2000); //toDo иначе 422

    }

    @Step("Меню корректно отображается")
    public void isDishListNotEmptyAndVisible() {

        baseActions.isElementVisibleDuringLongTime(orderContainer, 30);

    }

    public HashMap<String, Double> saveSumsInCheck() {

        double totalPaySumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        double totalTipsInTheMiddleSum = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
        double activePercentTips = 0;

        if (activeTipsButton.exists()) {

            activePercentTips = baseActions.convertSelectorTextIntoDoubleByRgx(activeTipsButton, "\\D+");

        }

        System.out.println(totalPaySumInCheck + " totalPaySumInCheck");
        System.out.println(totalTipsInTheMiddleSum + " totalTipsInTheMiddleSum");
        System.out.println(activePercentTips + " activePercentTips");

        HashMap<String, Double> sumsInfo = new HashMap<>();

        sumsInfo.put("totalPaySumInCheck", totalPaySumInCheck);
        sumsInfo.put("totalTipsInTheMiddleSum", totalTipsInTheMiddleSum);
        sumsInfo.put("activePercentTips", activePercentTips);

        return sumsInfo;

    }



    @Step("Проверка что все элементы в блоке чаевых отображаются")
    public void isTipsContainerCorrect() {

        if (totalTipsSumInMiddle.exists()) {

            baseActions.isElementVisible(tipsContainer);

            if (totalTipsSumInMiddle.exists()) {

                baseActions.isImageCorrect(waiterImageNotSelenide);

                baseActions.isElementVisible(tipsWaiter);

                baseActions.isElementVisible(totalTipsSumInMiddle);
                baseActions.isElementsListVisible(tipsListItem);
                checkCustomTipForError();

            }

        }

    }

    @Step("Очищаем локал и куки для разделения чека")
    public void clearAllSiteData() {

        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
        Selenide.refresh();

    }

    @Step("Проверка логики суммы сервисного сбора с общей суммой и чаевыми")
    public double countServiceCharge(double totalSum) { //

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("none")) {

            activateServiceChargeIfDeactivated();

            double tapperDiscount = 0;

            if (discountSum.exists()) {

                tapperDiscount = baseActions.convertSelectorTextIntoDoubleByRgx(discountSum, "[^\\.\\d]+");
                System.out.println(tapperDiscount + " tapper discount");

            }

            totalSum -= tapperDiscount;
            System.out.println(totalSum + " total sum");
            double serviceChargeSumClear = convertDouble(totalSum * (SERVICE_PRICE_PERCENT_FROM_TOTAL_SUM / 100));

            if (tipsContainer.exists()) {

                int tipsCount = Integer.parseInt(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
                double serviceChargeFromTips = convertDouble(tipsCount * (SERVICE_PRICE_PERCENT_FROM_TIPS / 100));

                serviceChargeSumClear = convertDouble(serviceChargeSumClear + serviceChargeFromTips);

            }

            System.out.println(serviceChargeSumClear + " serviceChargeSumClear");

            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽") - tapperDiscount;
            double currentSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(TapBar.totalSumInWalletCounter, "\\s₽") - tapperDiscount;
            double serviceChargeInField = baseActions.convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
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

        return convertDouble(totalSum * (SERVICE_PRICE_PERCENT_FROM_TOTAL_SUM / 100));

    }

    @Step("Ввод кастомных чаевых")
    public void setCustomTips(String value) {

        totalTipsSumInMiddle.clear();

        baseActions.click(totalTipsSumInMiddle);
        totalTipsSumInMiddle.clear();
        baseActions.sendHumanKeys(totalTipsSumInMiddle, value);
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

        System.out.println(serviceChargeCheckboxSvg.getCssValue("display").equals("block"));

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("none")) {

            baseActions.scrollTillBottom();
            baseActions.click(serviceChargeCheckboxButton);
            System.out.println("Установлен сервисный сбор");

        }

    }

    @Step("Деактивируем СБ если активен")
    public void deactivateServiceChargeIfActivated() {

        baseActions.scrollTillBottom();

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            baseActions.click(serviceChargeCheckboxButton);
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

    @Step("Выставляем чаевые на 0, проверяем что чаевые не участвуют в общей сумме и СБ")
    public void setTipsToZero(double totalSum) {

        if (totalTipsSumInMiddle.exists()) {

            scrollTillBottom();
            System.out.println(tipsListItem);
            System.out.println(tips0);
            tips0.shouldBe(visible,Duration.ofSeconds(5)).click();
            activeTipsButton.shouldHave(text("0%"));

            double serviceChargeSum = countServiceCharge(totalSum);
            int percent = baseActions.convertSelectorTextIntoIntByRgx(tips0, "\\D+");
            double tipsSumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));

            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
            double currentSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(TapBar.totalSumInWalletCounter, "\\s₽");

            Assertions.assertEquals(percent, 0, "Текущий процент чаевых не равен 0");
            Assertions.assertEquals(totalTipsSumInMiddle, 0, 0.1, "Общие чаевые в центре не равны 0");
            Assertions.assertEquals(tipsSumInCheck, 0, 0.1, "Чаевые в поле 'Чаевые' не равны 0");

            Assertions.assertEquals(totalPaySum, currentSumInWallet, 0.1,
                    "Сумма в 'Итого к оплате' не равна сумме в иконке кошелька");
            Assertions.assertEquals(totalSum + serviceChargeSum, totalPaySum, 0.1,
                    "Общая чистая сумма не равна сумме в 'Итого к оплате'");

        }

    }

    @Step("Прощёлкиваем все чаевые и проверяем что суммы корректны и сходятся. Добавляем лог в аллюр")
    public void checkAllTipsOptions(double totalSum) {

        showTapBar();
        scrollTillBottom();
        StringBuilder logs = new StringBuilder();
        double cleanTips = 0;

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            activateServiceChargeIfDeactivated();

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(2));

            double serviceChargeSum = baseActions.convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            serviceChargeSum = baseActions.convertDouble(serviceChargeSum);

            int percent = baseActions.convertSelectorTextIntoIntByRgx(tipsOption, "\\D+");
            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

            double tipsSumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;
                cleanTips = totalSum * percentForCalculation;
                cleanTips = Math.round(cleanTips);

            }

            double totalSumPlusCleanTips = totalSum + cleanTips;
            double totalSumPlusCleanTipsPlusServiceChargeSum = totalSum + cleanTips + serviceChargeSum;

            logs.append("\n").append("Скидка - ").append(percent).append("\n")
                    .append(serviceChargeSum).append(" чистый сервисный сбор по текущей проценту чаевых: ").append(percent).append("\n")
                    .append(cleanTips).append(" чистые чаевые по общей сумме за блюда\n")
                    .append(totalSum).append(" чистая сумма за блюда без чаевых и без сервисного сбора\n")
                    .append(totalSumPlusCleanTips).append(" чистая сумма за блюда c чаевым и без сервисного сбора\n")
                    .append(totalSumPlusCleanTipsPlusServiceChargeSum).append(" чистая сумма за блюда c чаевым и сервисным сбором\n")
                    .append(totalPaySum).append(" сумма в поле 'Итого к оплате' c учетом чаевых и сервисного сбора\n");

            Assertions.assertEquals(totalTipsSumInMiddle, cleanTips, 0.1,
                    "Общая сумма чаевых по центру " + totalTipsSumInMiddle +
                            " не совпала с суммой чистых чаевых " + cleanTips);

            Assertions.assertEquals(tipsSumInCheck, cleanTips, 0.1,
                    "Чаевые в 'Чаевые'" + tipsSumInCheck +
                            " не совпали с суммой чистых чаевых " + cleanTips);

            Assertions.assertEquals(totalSumPlusCleanTipsPlusServiceChargeSum, totalPaySum, 0.1,
                    "Чистая сумма за блюда + чистые чаевые + СБ " + totalSumPlusCleanTipsPlusServiceChargeSum
                            + " равна сумме в 'Итого к оплате' " + totalPaySum);

            baseActions.click(resetTipsButton);

        }

        System.out.println(logs);
        Allure.addAttachment("Подсчёт сумм", "text/plain", logs.toString());

    }

    @Step("Проверяем что разные проценты чаевых рассчитываются корректно во всех полях и в суммах вместе с СБ не разделяя счёт")
    public void isAllTipsOptionsAreCorrectWithTotalSumAndSC(double totalSum) {
        checkAllTipsOptions(totalSum);
    }

    @Step("Принудительно прячем тап бар, выставляем чаевые 0 и отключаем СБ")
    public void setTipsBy0AndCancelServiceCharge() {

        hideTapBar();

        if (totalTipsSumInMiddle.exists()) {

            baseActions.scrollByJS(bodyJS);
            baseActions.click(tips0);

        }

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            baseActions.scrollByJS(bodyJS);
            baseActions.click(serviceChargeCheckboxButton);

        }

    }

    @Step("Проверка что логика установленных чаевых по умолчанию от общей суммы корректна")
    public void isDefaultTipsBySumLogicCorrect() {

        scrollTillBottom();
        double totalDishSum = countAllNonPaidDishesInOrder();
        System.out.println(totalTipsSumInMiddle + " чаевые");

        if (totalDishSum < 490) {

            totalTipsSumInMiddle.shouldHave(value(MIN_SUM_TIPS_));

            if (totalDishSum < 196) {

                tips0.shouldNotHave(attributeMatching("class", ".+disabled"));

                tips10.shouldHave(attributeMatching("class", ".+disabled"));
                tips15.shouldHave(attributeMatching("class", ".+disabled"));
                tips20.shouldHave(attributeMatching("class", ".+disabled"));
                tips25.shouldHave(attributeMatching("class", ".+disabled"));

            } else if (totalDishSum >= 196 && totalDishSum <= 245) {

                tips25.shouldNotHave(attributeMatching("class", ".+disabled"));

                tips10.shouldHave(attributeMatching("class", ".+disabled"));
                tips15.shouldHave(attributeMatching("class", ".+disabled"));
                tips20.shouldHave(attributeMatching("class", ".+disabled"));

            } else if (totalDishSum > 245 && totalDishSum <= 326) {

                tips20.shouldNotHave(attributeMatching("class", ".+disabled"));
                tips25.shouldNotHave(attributeMatching("class", ".+disabled"));

                tips10.shouldHave(attributeMatching("class", ".+disabled"));
                tips15.shouldHave(attributeMatching("class", ".+disabled"));

            } else if (totalDishSum > 326 && totalDishSum <= 489) {

                tips15.shouldNotHave(attributeMatching("class", ".+disabled"));
                tips20.shouldNotHave(attributeMatching("class", ".+disabled"));
                tips25.shouldNotHave(attributeMatching("class", ".+disabled"));

                tips10.shouldHave(attributeMatching("class", ".+disabled"));

            }

        } else if (totalDishSum > 490) {

            tips10.shouldNotHave(attributeMatching("class", ".+disabled"));
            tips15.shouldNotHave(attributeMatching("class", ".+disabled"));
            tips20.shouldNotHave(attributeMatching("class", ".+disabled"));
            tips25.shouldNotHave(attributeMatching("class", ".+disabled"));

            activeTipsButton.shouldHave(text("10%"));

        }

    }

    @Step("Выбираем все не оплаченные позиции")
    public void chooseAllNonPaidDishes() {

        hideTapBar();
        double totalDishesSum = 0;

        System.out.println(allNonPaidAndNonDisabledDishes.size() + " общее число не оплаченных и не заблокированных позиций\n");

        for (int index = 0; index < allNonPaidAndNonDisabledDishes.size(); index++) {

            double currentDishPrice = baseActions.convertSelectorTextIntoDoubleByRgx
                    (allNonPaidAndNonDisabledDishesSum.get(index), "\\s₽");
            String currentDishName = allNonPaidAndNonDisabledDishesName.get(index).getText();

            totalDishesSum += currentDishPrice;

            scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
            allNonPaidAndNonDisabledDishesCheckbox.get(index).shouldBe(cssValue("display", "block"), Duration.ofSeconds(2));

            System.out.println("Блюдо - " + currentDishName +
                    " - " + currentDishPrice +
                    ". Общая цена " + totalDishesSum);

        }

        showTapBar();

    }

    @Step("Отменяем определенное количество выбранных позиций")
    public void cancelCertainAmountChosenDishes(int count) { //

        hideTapBar();

        System.out.println(allNonPaidAndNonDisabledDishes.size() + " общее число не оплаченных и не заблокированных позиций\n");
        System.out.println(count + " столько выберет блюд");

        for (int index = 0; count != 0; index++ ) {

            System.out.println("run");

            if (allNonPaidAndNonDisabledDishesCheckbox.get(index).getAttribute("style").equals("")) {

                scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
                forceWait(300); // toDo иначе 422
                System.out.println("Отменили выбранную позицию");
                count--;

            }

        }

        showTapBar();

    }

    @Step("Выбираем определенное число блюд и считаем сумму выбранных рандомных позиций")
    public void chooseCertainAmountDishes(int neededDishesAmount) {

        hideTapBar();

        double totalDishesSum = 0;
        double currentDishPrice;
        String currentDishName;

        System.out.println("\n" + "Количество рандомных позиций для выбора: " + neededDishesAmount);
        System.out.println(allNonPaidAndNonDisabledDishes.size() + " общее число позиций");

        for (int count = 1; count <= neededDishesAmount; count++) {

            int index;
            String flag;

            do {

                index = baseActions.generateRandomNumber(1, allNonPaidAndNonDisabledDishes.size()) - 1;
                flag = allNonPaidAndNonDisabledDishesCheckbox.get(index).getCssValue("display");

                currentDishPrice = baseActions.convertSelectorTextIntoDoubleByRgx
                        (allNonPaidAndNonDisabledDishesSum.get(index), "\\s₽");
                currentDishName = allNonPaidAndNonDisabledDishesName.get(index).getText();

            } while (flag.equals("block"));

            totalDishesSum += currentDishPrice;

            scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
            System.out.println("Выбрали позицию");
            forceWait(1000); // toDo иначе 422

            allNonPaidAndNonDisabledDishesCheckbox.get(index).shouldBe(cssValue("display", "block"), Duration.ofSeconds(5));
            System.out.println("Блюдо - " + currentDishName +
                    " - " + currentDishPrice +
                    ". Общая цена " + totalDishesSum);

        }

        showTapBar();

    }

    @Step("Считаем сумму всех не выбранных позиций")
    public double countAllDishesNotDivided() { //toDo logs Cannot invoke "java.lang.StringBuilder.append(String)" because "logs" is null

        double totalDishesSum = 0;

        //   StringBuilder logs = null;

        System.out.println(allDishesInOrder.size() + " общее число позиций");

        for (SelenideElement element : allDishesInOrder) {

            if (element.$(".orderItem__status").getText().equals("")) {

                double currentDishPrice =
                        baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price"), "\\s₽");
                String currentDishName = element.$(".orderItem__name").getText();

                totalDishesSum += currentDishPrice;

                System.out.println("Блюдо - " + currentDishName + " - " + currentDishPrice +
                        ". Общая цена " + totalDishesSum);

                // logs.append("Блюдо - ").append(currentDishName).append(" - ").append(currentDishPrice)
                //    .append(". Общая цена ").append(totalDishesSum);

            }

        }

        //  Allure.addAttachment("Блюда","text/plain", logs.toString());

        System.out.println(totalDishesSum + " Сумма за все блюда");
        return totalDishesSum;

    }

    @Step("Считаем сумму всех выбранных позиций в заказе при разделении") //
    public double countAllChosenDishesDivided() {

        double totalSumInOrder = 0;
        int counter = 0;

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            if (element.$(".iconCheck").getCssValue("display").equals("block")) {

                double cleanPrice = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price"), "\\s₽");
                String dishName = element.$(".orderItem__name").getText();

                totalSumInOrder += cleanPrice;
                counter++;

                System.out.println(counter + ". " + dishName + " - " + cleanPrice + ". Общая сумма: " + totalSumInOrder);

            }

        }

        double markedDishesSum = baseActions.convertSelectorTextIntoDoubleByRgx(TipsAndCheck.markedDishesSum, "\\s₽");

        Assertions.assertEquals(markedDishesSum, totalSumInOrder, 0.1);
        System.out.println("Сумма в поле 'Отмеченные позиции' " +
                markedDishesSum + " совпадает с общей чистой суммой заказа " + totalSumInOrder + "\n");

        return totalSumInOrder;

    }

    @Step("Считаем сумму не оплаченных позиций в заказе не разделяя")
    public double countAllNonPaidDishesInOrder() {

        double totalSumInOrder = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        System.out.println(allNonPaidAndNonDisabledDishes.size() + " все не оплаченные и не блокированные позиции");

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            double cleanPrice = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price"), "\\s₽");
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

    @Step("Забираем коллекцию всех позиций в заказе для следующего теста")
    public HashMap<Integer, Map<String, Double>> getAllDishesAndSetCollection() {

        HashMap<Integer, Map<String, Double>> tapperDishes = new HashMap<>();
        int i = 0;

        System.out.println("\n" + "Количество не оплаченных и не заблокированных блюд : " + allDishesInOrder.size());

        for (SelenideElement element : allDishesInOrder) {

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = element.$(".orderItem__name").getText();
            double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price"), "\\s₽");

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

        for (int index = 0; index < allDishesInOrder.size(); index++) {

            if (allDishesInOrder.get(index).$(".iconCheck").getAttribute("style").equals("")) {

                Map<String, Double> temporaryMap = new HashMap<>();

                String name = allDishesInOrder.get(index).$(".orderItem__name").getText();
                double price = baseActions
                        .convertSelectorTextIntoDoubleByRgx(allDishesInOrder.get(index).$(".orderItem__price"), "\\s₽");

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

        System.out.println(divideCheckSliderActive + " разделяется счёт?");
        System.out.println(allNonPaidAndNonDisabledDishes.size() + " не оплаченных и не заблоченных блюд количество");
        System.out.println(disabledAndPaidDishes.size() + " заблоченных или оплаченных количество");
        System.out.println(allDishesInOrder.filterBy(text(" Оплачивается ")));

        System.out.println("\n" + "Количество заблокированных блюд : " + disabledDishes.size());

        for (int index = 0; index < allDishesInOrder.size(); index++) {

            if (allDishesInOrder.get(index).$(".orderItem__status").getText().equals("Оплачивается")) {

                Map<String, Double> temporaryMap = new HashMap<>();

                String name = allDishesInOrder.get(index).$(".orderItem__name").getText();
                double price = baseActions
                        .convertSelectorTextIntoDoubleByRgx
                                (allDishesInOrder.get(index).$(".orderItem__price"), "\\s₽");

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

    @Step("Проверяем коллекцию всех оплаченных позиций первым гостем со втором")
    public void checkIfDishesPaidEarlier(HashMap<Integer, Map<String, Double>> chosenDishesEarlier) {

        HashMap<Integer, Map<String, Double>> chosenDishesCurrent = new HashMap<>();
        int i = 0;

        System.out.println("\n" + "Количество оплаченных блюд : " + paidDishes.size());

        for (SelenideElement element : paidDishes) {

            Map<String, Double> temporaryMap = new HashMap<>();

            element.$x("//*[contains(text(),'Оплачено')]").shouldHave(text(" Оплачено "));

            String name = element.$(".orderItem__name").getText();
            double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price"), "\\s₽");

            temporaryMap.put(name, price);
            chosenDishesCurrent.put(i, temporaryMap);

            i++;

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

            double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price"), "\\s₽");

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

    @Step("Проверяем что блюда, которые выбраны ранее, в статусе 'Ожидается'")
    public void dishesAreDisabledInDishList(HashMap<Integer, Map<String, Double>> chosenDishes) {

        int successAmount = 0;
        System.out.println(disabledDishes.size() + " disabled dishes size");

        for (int i = 0; i < chosenDishes.size(); i++) {

            for (int k = 0; k < disabledDishes.size(); k++) {

                String dishNameInCurrentDividedOrder = disabledDishes.get(i).$(".orderItem__name").getText();
                System.out.println(dishNameInCurrentDividedOrder);
                boolean isDishNameSameAsDividedEarlier = chosenDishes.get(k).containsKey(dishNameInCurrentDividedOrder);
                System.out.println(isDishNameSameAsDividedEarlier);

                if (isDishNameSameAsDividedEarlier) {

                    successAmount++;
                    break;

                }

            }

        }

        Assertions.assertEquals(chosenDishes.size(), successAmount);

    }

    @Step("Проверяем что имя и сумма перечеркнуты у заблокированных блюд")
    public void isStylesCorrectToDisabledDishes() {

        separateOrderHeading.shouldBe(exist, visible);

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            element.$(".shouldHave(text(' Оплачивается ')").shouldHave(text(" Оплачивается "));
            element.$(".dishList__item-status img").shouldBe(visible, Duration.ofSeconds(2));
            element.$("input+span").shouldHave(cssValue("background", "#dcdee3"));
            element.$("input+span").shouldBe(selected);

        }

    }

    @Step("Проверяем что сумма всех блюд совпадает с итоговой без чаевых и СБ")
    public void isTotalSumInDishesMatchWithTotalPay(double totalSumByDishesInOrder) {

        double discount = 0;
        double markup = 0;

        if (discountSum.exists()) {

            discount = baseActions.convertSelectorTextIntoDoubleByRgx(discountSum, "[^\\d\\.]+");

        }

        if (markupField.exists()) {

            markup = baseActions.convertSelectorTextIntoDoubleByRgx(markupSum, "[^\\d\\.]+");

        }

        baseActions.isElementVisibleDuringLongTime(totalPay, 2);

        double totalPaySumInCheck = getClearOrderAmount();
        totalSumByDishesInOrder -= discount;


        System.out.println(totalPaySumInCheck + " общая сумма в 'Итого к оплате'");
        Assertions.assertEquals(totalPaySumInCheck, totalSumByDishesInOrder, 0.01,
                "Сумма за все блюда не совпала с суммой в 'Итого к оплате' ");
        System.out.println("Сумма за все блюда " + totalSumByDishesInOrder + " совпала с суммой в 'Итого к оплате' "
                + totalPaySumInCheck);
    }

    @Step("Удаляем скидки из суммы")
    public void removeDiscountFromTotalPaySum(double discount) {

        discountField.shouldNotBe(exist);

        double totalClearOrderAmount = getClearOrderAmount();
        double totalPaySumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

        totalClearOrderAmount -= discount;

        Assertions.assertEquals(totalClearOrderAmount, totalPaySumInCheck, 0.1,
                "Чистая сумма совпадает с 'Итого к оплате после удаления скидки'");
        System.out.println("Чистая сумма " + totalClearOrderAmount +
                " совпадает с 'Итого к оплате после удаления скидки' " + totalPaySumInCheck);

    }

    @Step("Проверяем что общая чистая сумма совпадает с 'Итого к оплате'")
    public void checkCleanSumMatchWithTotalPay(double cleanDishesSum) {

        double currentTips = getCurrentTipsSum();
        double currentSC = getCurrentSCSum();

        double totalCleanPaySum = countTotalDishesSumWithTipsAndSC(cleanDishesSum, currentTips, currentSC);
        double totalPaySumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        double totalPaySumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");

        Assertions.assertEquals(totalCleanPaySum, totalPaySumInCheck,
                "Чистая сумма не совпадает с суммой в 'Итого к оплате'");
        System.out.println("\nЧистая сумма совпадает с суммой в 'Итого к оплате'\n");

        Assertions.assertEquals(totalPaySumInCheck, totalPaySumInWallet,
                "Сумма 'Итого к оплате' не совпадает с суммой в счётчике кошелька");
        System.out.println("\nСумма 'Итого к оплате' совпадает с суммой в счётчике кошелька\n");

    }

    @Step("Проверяем все варианты чаевых с общей суммой с сб")
    public void checkTipsOptionWithSC(double cleanDishesSum) {

        StringBuilder logs = new StringBuilder(); // toDo вывести логи

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(1));

            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));
            double tipsSumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");

            Assertions.assertEquals(totalTipsSumInMiddle, tipsSumInCheck,
                    "Чаевые по центру не совпадают с чаевыми в поле 'Чаевые'");
            System.out.println("Чаевые по центру " + totalTipsSumInMiddle +
                    " совпадают с чаевыми в поле 'Чаевые' " + tipsSumInCheck + "");

            double cleanTips = 0;
            int percent = baseActions.convertSelectorTextIntoIntByRgx(tipsOption, "\\D+");

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;
                cleanTips = cleanDishesSum * percentForCalculation;
                cleanTips = Math.round(cleanTips);

            }

            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
            double totalPaySumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");
            double serviceChargeSum = getCurrentSCSum();
            double totalCleanPaySum = cleanDishesSum + serviceChargeSum + cleanTips;

            Assertions.assertEquals(totalCleanPaySum, totalPaySum,
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

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(2));

            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));
            double tipsSumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");

            Assertions.assertEquals(totalTipsSumInMiddle, tipsSumInCheck,
                    "Чаевые по центру не совпадают с чаевыми в поле 'Чаевые'");
            System.out.println("Чаевые по центру " + totalTipsSumInMiddle +
                    " совпадают с чаевыми в поле 'Чаевые' " + tipsSumInCheck + "");

            double cleanTips = 0;
            int percent = baseActions.convertSelectorTextIntoIntByRgx(tipsOption, "\\D+");

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;
                cleanTips = cleanDishesSum * percentForCalculation;
                cleanTips = Math.round(cleanTips);

            }

            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
            double totalPaySumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");
            double totalCleanPaySum = cleanDishesSum + cleanTips;

            Assertions.assertEquals(totalCleanPaySum, totalPaySum,
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

        baseActions.isElementVisible(tipsContainer);
        baseActions.isImageCorrect(waiterImageNotSelenide);
        baseActions.isElementVisible(tipsWaiter);
        baseActions.isElementInvisible(tipsInCheckSum);
        baseActions.isElementInvisible(totalTipsSumInMiddle);
        baseActions.isElementInvisible(activeTipsButton);
        baseActions.isElementInvisible(resetTipsButton);
        tipsListItem.shouldHave(CollectionCondition.size(0));

    }

    @Step("Чаевые не должны отображаться у не верифицированного официанта без привязанной карты")
    public void checkIsNoTipsElementsIfNonVerifiedNonCard() {

        baseActions.isElementInvisible(tipsContainer);

    }

    @Step("Проверяем что сбор формируется корректно по формуле со всеми чаевыми")
    public void checkScLogic(double cleanDishesSum) {

        activateServiceChargeIfDeactivated();

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", ".*active.*"));

            double tipsSumInTheMiddle = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));

            double serviceChargeInField = baseActions.convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            serviceChargeInField = convertDouble(serviceChargeInField);

            double serviceChargeSumClear = convertDouble(cleanDishesSum * (SERVICE_PRICE_PERCENT_FROM_TOTAL_SUM / 100));
            System.out.println(serviceChargeSumClear + " сервисный сбор от суммы");

            double serviceChargeTipsClear = convertDouble(tipsSumInTheMiddle * (SERVICE_PRICE_PERCENT_FROM_TIPS / 100));
            System.out.println(serviceChargeTipsClear + " сервисный сбор от чаевых\n");

            double totalDishesCleanSum = cleanDishesSum + tipsSumInTheMiddle + serviceChargeTipsClear + serviceChargeSumClear;
            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
            double totalPaySumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");

            Assertions.assertEquals(serviceChargeInField, serviceChargeSumClear + serviceChargeTipsClear, 0.1,
                    "Сервисный сбор считается не корректно по формуле 1.5% от суммы и 5% от чаевых");
            System.out.println("Сервисный сбор считается корректно по формуле 1.5% от суммы "
                    + totalDishesCleanSum + " и 5% от чаевых " + tipsSumInTheMiddle);

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

            double tipsInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
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

        baseActions.scrollTillBottom();
        double serviceChargeSum = 0;

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            System.out.println("СБ включены");
            serviceChargeSum = baseActions.convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.\\-]+");

            Assertions.assertTrue(serviceChargeSum > 0,
                    "Сервисный сбор имеет отрицательное значение");

            serviceChargeSum = baseActions.convertDouble(serviceChargeSum);

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
        baseActions.click(resetTipsButton);

        if (notDisabledAndNotZeroTipsPercentOptions.size() != 0) {

            int index = baseActions.generateRandomNumber(1, notDisabledAndNotZeroTipsPercentOptions.size()) - 1;

            System.out.println(activeTipsButton.getText() + " до клика активные чаевые");
            baseActions.click(notDisabledAndNotZeroTipsPercentOptions.get(index));
            System.out.println(notDisabledAndNotZeroTipsPercentOptions.get(index).getText() + " активные чаевые");
            activeTipsButton
                    .shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(1));

        }

    }

    @Step("Сброс чаевых")
    public void resetTips() {

        scrollTillBottom();
        baseActions.click(resetTipsButton);
        activeTipsButton.shouldHave(text("0%"));
        System.out.println(activeTipsButton);
        System.out.println(activeTipsButton.getText());
        totalTipsSumInMiddle.shouldHave(value("0"));
        double tipsInTheCheck = baseActions.convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
        Assertions.assertEquals(tipsInTheCheck, 0);

        activeTipsButton.shouldHave(text("0%"));

        int percent = baseActions.convertSelectorTextIntoIntByRgx(tips0, "\\D+");
        double tipsSumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
        int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));

        double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        double currentSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(TapBar.totalSumInWalletCounter, "\\s₽");

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

            showTapBar();

            String totalPaySum = baseActions.convertSelectorTextIntoStrByRgx(totalPay, "\\s₽");
            String currentSumInWallet = baseActions.convertSelectorTextIntoStrByRgx(TapBar.totalSumInWalletCounter, "\\s₽");

            Assertions.assertEquals(totalPaySum, currentSumInWallet,
                    "Сумма 'Итого к оплате' не совпадает с суммой в иконке кошелька");
            System.out.println("Сумма 'Итого к оплате' совпадает с суммой в иконке кошелька");

        }

    }

    @Step("Проверяем что процент установленных по умолчанию чаевых рассчитывается корректно с СБ и без")
    public void isActiveTipPercentCorrectWithTotalSumAndSC(double totalSumWithoutTips) {

        if (totalTipsSumInMiddle.exists()) {

            deactivateServiceChargeIfActivated();

            baseActions.isElementVisibleDuringLongTime(activeTipsButton, 15);

            double tipPercent = baseActions.convertSelectorTextIntoDoubleByRgx(activeTipsButton, "\\D+");
            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
            double totalSumWithTips = totalSumWithoutTips + Math.round(totalSumWithoutTips * (tipPercent / 100));

            System.out.println("\nПроцент чаевых " + tipPercent +
                    "\nСумма в 'Итого к оплате' " + totalPaySum +
                    "\nСумма 'Итого к оплате' вместе с чаевыми " + totalSumWithTips
            );

            Assertions.assertEquals(totalSumWithTips, totalPaySum, 0.1);
            System.out.println("Процент скидки установленный по умолчанию совпадает с общей ценой за все блюда без СБ");

        }

    }

    @Step("Проверка что сумма 'Другой пользователь' совпадает с суммой оплаченных позиций")
    public void isAnotherGuestSumCorrect() {

        anotherGuestField.shouldBe(visible);

        double totalPaidSum = 0;
        double anotherGuestSum = baseActions.convertSelectorTextIntoDoubleByRgx(TipsAndCheck.anotherGuestSum, "[^\\d\\.]+");

        for (SelenideElement element : disabledAndPaidDishes) {

            double currentElementSum = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".orderItem__price"), "\\s₽");
            totalPaidSum += currentElementSum;

        }

        Assertions.assertEquals(anotherGuestSum, totalPaidSum, 0.1);
        System.out.println("\nОплаченная сумма другого пользователя " + anotherGuestSum +
                " совпадает с оплаченными позициями " + totalPaidSum + "\n");

    }

    @Step("Проверка что в форме 'Итого к оплате' ")
    public void isCheckContainerShown() {

        baseActions.isElementVisible(checkContainer);
        baseActions.isElementVisible(totalPay);
        baseActions.isElementVisible(paymentMethodsContainer);
        paymentMethods.shouldBe(CollectionCondition.size(3));

        if (tipsContainer.exists()) {

            baseActions.isElementVisible(tipsInCheckField);

        }

    }

    @Step("Проверка на ошибку чаевых при вводе значения меньше установленного минимального")
    public void checkCustomTipForError() {

        scrollTillBottom();

        String defaultTips = totalTipsSumInMiddle.getValue();

        baseActions.click(totalTipsSumInMiddle);
        totalTipsSumInMiddle.clear();
        baseActions.sendHumanKeys(totalTipsSumInMiddle, MIN_SUM_FOR_TIPS_ERROR);
        tipsErrorMsg.shouldHave(cssValue("display", "block"));
        tipsErrorMsg.shouldHave(text(TIPS_ERROR_MSG));
        paymentButton.shouldBe(disabled);

        totalTipsSumInMiddle.clear();

        assert defaultTips != null;
        baseActions.sendHumanKeys(totalTipsSumInMiddle, defaultTips);
        tipsErrorMsg.shouldHave(cssValue("display", "none"));

    }

    @Step("Кнопка 'Оплатить' отображается корректно")
    public void isPaymentButtonShown() {

        baseActions.scrollByJS(bodyJS);
        baseActions.isElementVisibleDuringLongTime(paymentButton, 15);

    }

    @Flaky
    @Step("Проверка кнопки поделиться счётом, кнопка отображается и вызывает меню шаринга")
    public void isShareButtonShown() {

        baseActions.isElementVisibleAndClickable(shareButton);

        boolean isShareActive = Boolean.TRUE.equals(Selenide.executeJavaScript(isShareButtonCorrect));

        Assertions.assertTrue(isShareActive, "Кнопка 'Поделиться счётом' не вызывает панель поделиться ссылкой");
        System.out.println("Кнопка 'Поделиться счётом' работает корректно");

    }

    @Step("Проверка что сервисный сбор отображается")
    public void isServiceChargeShown() {

        if (serviceChargeContainer.exists()) {

            baseActions.isElementVisible(serviceChargeContainer);

        }

    }

    @Step("Проверка политики конфиденциальности")
    public void isConfPolicyShown() { //

        scrollTillBottom();
        baseActions.isElementVisible(confPolicyContainer);
        baseActions.click(confPolicyContainer);
        baseActions.isElementVisible(confPolicyContent);
        confPolicyContent.shouldHave(matchText("УСЛОВИЯ ИСПОЛЬЗОВАНИЯ И ПОЛИТИКА КОНФИДЕНЦИАЛЬНОСТИ TAPPER"));
        Selenide.executeJavaScript("document.querySelector('.mainPage .privacyPolicyModal').style.display = 'none';");
        baseActions.isElementInvisible(confPolicyContent);

    }

    @Step("Клик по кнопке оплаты")
    public void clickOnPaymentButton() {

        scrollTillBottom();
        baseActions.click(paymentButton);

    }

    @Step("Отображается лоадер на странице")
    public void isPageLoaderShown() {

        pagePreLoader.shouldNotHave(cssValue("display", "none"));

    }

    @Step("Сохранение общей суммы в таппере для передачи в другой тест")
    public double saveTotalPayForMatchWithAcquiring() {
        return baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
    }

    @Step("Сбрасываем чаевые и СБ чтобы получить чистую сумму за позиции")
    public double getClearOrderAmount() {

        double serviceChargeSum = 0;
        double tips = 0;

        if (serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            serviceChargeSum = baseActions.convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            serviceChargeSum = baseActions.convertDouble(serviceChargeSum);

        }

        if (totalTipsSumInMiddle.exists()) {

            tips = baseActions.convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");

        }

        double orderAmount = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        orderAmount = orderAmount - tips - serviceChargeSum;

        orderAmount = convertDouble(orderAmount);

        return orderAmount;

    }



    @Step("Сохранение всех сумм для проверки что транзакция создалась на b2p")
    public HashMap<String, Integer> savePaymentDataTapperForB2b() {

        HashMap<String, Integer> paymentData = new HashMap<>();

        int tips = 0;
        int fee = 0;

        double order_amountD = getClearOrderAmount();
        order_amountD  = order_amountD * 100;
        Integer order_amount = (int) order_amountD;

        paymentData.put("order_amount", order_amount);

        if (totalTipsSumInMiddle.exists()) {

            System.out.println(totalTipsSumInMiddle.getValue() + " clean string");

            double tipsD = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));

            System.out.println(totalTipsSumInMiddle.getValue() + " clean double");

            tips = (int) (tipsD) * 100;

            System.out.println(totalTipsSumInMiddle.getValue() + " clean int");

        }

        paymentData.put("tips", tips);

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            double feeD = baseActions.convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            feeD = convertDouble(feeD) * 100;
            fee = (int) feeD;

        }

        paymentData.put("fee", fee);
        System.out.println(paymentData + " данные для б2п");
        return paymentData;

    }

    @Step("Проверка нижнего навигационного меню. Проверки на элементы, кликабельность, переходы, открытие")
    public void isTapBarShown() {

        baseActions.scroll(appFooter);
        baseActions.isElementVisible(appFooter);
        baseActions.isElementVisibleAndClickable(appFooterMenuIcon);
        baseActions.isElementVisibleAndClickable(appFooterWalletIcon);

        baseActions.click(appFooterMenuIcon);
        baseActions.isElementVisible(orderMenuContainer);

        baseActions.isElementInvisible(orderContainer);

        baseActions.click(appFooterWalletIcon);

        baseActions.isElementVisible(orderContainer);
        baseActions.isElementInvisible(orderMenuContainer);

    }

    @Step("Открытие формы вызова официанта")
    public void openCallWaiterForm() {

        baseActions.scroll(callWaiterButton);
        baseActions.isElementVisibleAndClickable(callWaiterButton);
        baseActions.click(callWaiterButton);

        baseActions.isElementVisible(callWaiterContainer);

    }

    @Step("Проверка элементов формы вызова официанта")
    public void isCallContainerWaiterCorrect() {

        baseActions.isElementVisible(callWaiterFadedBackground);
        baseActions.isElementVisible(callWaiterHeading);
        baseActions.isElementVisibleAndClickable(callWaiterButtonSend);
        baseActions.isElementVisibleAndClickable(callWaiterButtonCancel);
        baseActions.isElementVisible(callWaiterCloseButton);
        baseActions.isElementVisible(callWaiterCommentArea);

    }

    @Step("Закрытие формы по кнопке официанта")
    public void closeCallWaiterFormByCallWaiterButton() {

        baseActions.click(callWaiterButton);
        baseActions.isElementInvisible(callWaiterContainer);

    }

    @Step("Закрытие формы по кнопке отмены")
    public void closeCallWaiterFormByCancelButton() {

        baseActions.click(callWaiterButton);
        baseActions.click(callWaiterButtonCancel);
        baseActions.isElementInvisible(callWaiterContainer);

    }

    @Step("Закрытие формы по крестику закрытия")
    public void closeCallWaiterFormByCloseButton() {

        baseActions.click(callWaiterButton);
        baseActions.click(callWaiterCloseButton);
        baseActions.isElementInvisible(callWaiterContainer);

    }

    @Step("Отправка текста в комментарий официанта")
    public void sendWaiterComment() {

        callWaiterCommentArea.shouldHave(attribute("placeholder", "Комментарий..."));
        baseActions.sendHumanKeys(callWaiterCommentArea, TEST_WAITER_COMMENT);
        baseActions.elementShouldHaveValue(callWaiterCommentArea, TEST_WAITER_COMMENT);

        baseActions.click(callWaiterButtonSend);

    }

    @Step("Ввод текста в комментарий официанта")
    public void isSendSuccessful() {

        baseActions.isElementVisible(successCallWaiterHeading);
        baseActions.isElementVisible(successLogoCallWaiter);
        baseActions.click(closeCallWaiterFormInSuccessButton);
        baseActions.isElementInvisible(callWaiterContainer);

    }

    @Step("Вызов официанта. Проверки на элементы, кликабельность, заполнение, открытие\\закрытие")
    public void isCallWaiterCorrect() {

        openCallWaiterForm();

        isCallContainerWaiterCorrect();

        closeCallWaiterFormByCallWaiterButton();

        closeCallWaiterFormByCloseButton();

        baseActions.click(callWaiterButton);

        sendWaiterComment();

        isSendSuccessful();

    }

    @Step("Подмена куки юзера")
    public void setAnotherGuestCookie() {

        WebDriverRunner.getWebDriver().manage().deleteCookieNamed("guest");
        Cookie cookieGuest = new Cookie("guest", "11111");
        WebDriverRunner.getWebDriver().manage().addCookie(cookieGuest);
        Selenide.refresh();

    }


    @Step("Сохраняем информацию по меню со стола в таппере")
    public HashMap<String,Map<String,String>> saveTapperMenuData() {

        HashMap<String,Map<String,String>> menuData = new HashMap<>();

        int menuCategorySize = menuCategoryContainerName.size();

        for (int categoryIndex = 0; categoryIndex < menuCategorySize; categoryIndex++) {

            String categoryName = menuCategoryContainerName.get(categoryIndex).getText();
            System.out.println(categoryName + " имя категории");

            String dishSizeXpath =
                    "//*[@class='orderMenuList']//*[@class='orderMenuList__item'][.//*[contains(text(),'" +
                            categoryName + "')]]//*[@class='orderMenuProductList__item']";

            ElementsCollection dishElement = $$x(dishSizeXpath);

            int dishSize = dishElement.size();
            System.out.println(dishSize + " количество блюд");

            Map<String,String> dishList = new HashMap<>();

            for (SelenideElement element : dishElement) {

                String dishName = element.$(".orderMenuProduct__name").getText();
                System.out.println(dishName + " имя блюда");

                String dishPrice = element.$(".orderMenuProduct__price").getText().replaceAll("\\s₽","");
                System.out.println(dishPrice + " цена блюда");

                String dishImage = "";

                if (element.$(".orderMenuProduct__photo img").exists()) {

                    dishImage = element.$(".orderMenuProduct__photo img").getAttribute("src");

                }

                System.out.println(dishImage + " фото блюда");

                dishList.put("name",dishName);
                dishList.put("price",dishPrice);
                dishList.put("image",dishImage);

            }

            menuData.put(categoryName,dishList);

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

        double totalSum = getClearOrderAmount();

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


}


