package tapper_table;

import api.ApiRKeeper;
import com.codeborne.selenide.*;
import common.BaseActions;
import data.Constants;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Cookie;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.ClipboardConditions.content;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.clipboard;
import static data.Constants.RegexPattern.TapperTable.*;
import static data.Constants.RegexPattern.TelegramMessage.discountRegex;
import static data.Constants.RegexPattern.TelegramMessage.tableRegexTelegramMessage;
import static data.Constants.TestData.TapperTable;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.TestData.Yandex;
import static data.Constants.*;
import static data.selectors.TapperTable.Common.*;
import static data.selectors.TapperTable.RootPage.*;
import static data.selectors.TapperTable.RootPage.DishList.*;
import static data.selectors.TapperTable.RootPage.Menu.*;
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

    @Step("Возвращаемся на прошлую страницу")
    public void returnToPreviousPage() {

        Selenide.back();
        forceWait(WAIT_FOR_FULL_LOAD_PAGE);

    }

    @Step("Обновляем текущую страницу")
    public void refreshPage() {

        Selenide.refresh();
        forceWait(WAIT_FOR_FULL_LOAD_PAGE);

    }


    @Step("Обновляем текущую страницу")
    public void refreshTableWithOrder() {

        Selenide.refresh();
        isTableHasOrder();

    }

    @Step("Сбор всех блюд со страницы таппера и проверка с блюдами на кассе")
    public void matchTapperOrderWithOrderInKeeper(HashMap<Integer, Map<String, Double>> allDishesInfoFromKeeper) {

        HashMap<Integer, Map<String, Double>> tapperDishes = new HashMap<>();

        int i = 0;

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = element.$(dishNameSelector).getText();
            double price = convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);

            temporaryMap.put(name, price);
            tapperDishes.put(i, temporaryMap);

            i++;

        }


        if (!allDishesInfoFromKeeper.equals(tapperDishes)) {

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

    }

    @Step("Переход на страницу {url} и ждём принудительно пока не прогрузятся все скрипты\\элементы\\сокет")
    public void openNotEmptyTable(String url) {

        openPage(url);
        skipStartScreenLogo();
        isTableHasOrder();

    }

    public void clickOnMenuInFooter() {

        if (!menuDishContainer.isDisplayed())
            click(appFooterMenuIcon);

    }

    @Step("Проверки что стол освободился, статусы в заголовке корректные")
    public void isEmptyOrderAfterClosing() {

        skipStartScreenLogo();
        emptyOrderHeading.shouldHave(visible.because("Не появился пустой стол в течение определенного времени")
                , Duration.ofSeconds(7));
        emptyOrderHeading.shouldHave(matchText("Ваш заказ появится здесь"), Duration.ofSeconds(10));

        double totalSumInWallet = convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, totalPayRegex);
        Assertions.assertEquals(totalSumInWallet, 0, "Сумма в кошельке не равно 0");

    }

    @Step("Первичная заставка\\лого\\анимация при открытии страницы")
    public void isStartScreenShown() {

        startScreenLogoContainer.shouldHave(appear, Duration.ofSeconds(8));

        if (startScreenLogoContainerImage.exists()) {

            isImageCorrect(startScreenLogoContainerImageNotSelenide,
                    "Изображение\\анимация загрузки стола не корректная или битая");

        }

        startScreenLogoContainer.shouldNotHave(cssValue("display", "flex"), Duration.ofSeconds(15));

    }

    @Step("Переключаем на разделение счёта")
    public void activateDivideCheckSliderIfDeactivated() {

        if (divideCheckSlider.isDisplayed()) {

            click(divideCheckSlider);
          //  forceWait(WAIT_FOR_FULL_LOAD_PAGE); // toDO если после активации раздельного меню сразу выбрать позицию, то гарантировано 422, поэтому ждём

        }

    }

    @Step("Отключаем разделение счёта")
    public void deactivateDivideCheckSliderIfActivated() {

        if (discountSum.isDisplayed()) {

            isElementInvisible(divideCheckSlider);

        }

        isElementVisibleDuringLongTime(divideCheckSliderActive, 5);
        click(divideCheckSliderActive);
        forceWait(1000);  // toDO если после активации раздельного меню сразу выбрать позицию, то гарантировано 422, поэтому ждём

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

    }

    @Step("Заказ не пустой и блюда отображаются")
    public void isTableHasOrder() {

        startScreenLogoContainer.shouldBe(hidden,Duration.ofSeconds(15));
        isElementVisibleDuringLongTime(orderContainer, 60);

    }

    public void skipStartScreenLogo() {

        startScreenLogoContainer.shouldBe(hidden,Duration.ofSeconds(15));

    }

    @Step("Проверка что все элементы в блоке чаевых отображаются")
    public void isTipsContainerCorrect() {

        if (totalTipsSumInMiddle.exists()) {

            isElementVisible(tipsContainer);

            if (totalTipsSumInMiddle.exists()) {

                isImageCorrect(waiterImageNotSelenide, "Изображение официанта не корректное или битое");

                isElementVisible(tipsWaiter);

                isElementVisible(totalTipsSumInMiddle);
                isElementsListVisible(tipsListItem);
                checkCustomTipForError();

            }

        }

    }

    @Step("Проверка логики суммы сервисного сбора с общей суммой и чаевыми")
    public double countServiceCharge(double totalSum) { //

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("none")) {

            activateServiceChargeIfDeactivated();

            double tapperDiscount = 0;

            if (discountSum.exists()) {

                tapperDiscount = convertSelectorTextIntoDoubleByRgx(discountSum, discountRegex);

            }

            totalSum -= tapperDiscount;
            double serviceChargeSumClear =
                    updateDoubleByDecimalFormat(totalSum * (SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM / 100));

            if (tipsContainer.exists()) {

                int tipsCount = Integer.parseInt(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
                double serviceChargeFromTips =
                        updateDoubleByDecimalFormat(tipsCount * (SERVICE_CHARGE_PERCENT_FROM_TIPS / 100));

                serviceChargeSumClear = updateDoubleByDecimalFormat(serviceChargeSumClear + serviceChargeFromTips);

            }

            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex) - tapperDiscount;
            double currentSumInWallet =
                    convertSelectorTextIntoDoubleByRgx
                            (TapBar.totalSumInWalletCounter, totalPayRegex) - tapperDiscount;
            double serviceChargeInField =
                    convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            serviceChargeInField = updateDoubleByDecimalFormat(serviceChargeInField);


            Assertions.assertEquals(totalPaySum, currentSumInWallet, 0.1,
                    "Сумма в 'Итого к оплате' " + totalPaySum
                            + " не совпадает с суммой в иконке кошелька вместе с СБ "
                            + currentSumInWallet + "\n");

            Assertions.assertEquals(serviceChargeSumClear, serviceChargeInField, 0.1,
                    "Сервисный сбор рассчитывается не корректно из чаевых и суммы заказа");

        }

        return updateDoubleByDecimalFormat(totalSum * (SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM / 100));

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

        checkTipsAfterReset();
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
                serviceChargeCheckboxSvg.getCssValue("display").equals("none"))
            click(serviceChargeCheckboxButton);

    }

    @Step("Деактивируем СБ если активен")
    public void deactivateServiceChargeIfActivated() {

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("block"))
            click(serviceChargeCheckboxButton);

    }

    @Step("Проверка что у блюд с модификатором есть подписи в позиции")
    public void isModificatorTextCorrect() {

        allDishesInOrder.asFixedIterable().stream().forEach
                (element -> element.$(dishModificatorName).shouldBe(visible).shouldNotBe(empty));

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
                    .shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(1));

            double serviceChargeSum =
                    convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");
            serviceChargeSum = updateDoubleByDecimalFormat(serviceChargeSum);

            int percent = convertSelectorTextIntoIntByRgx(tipsOption, "\\D+");
            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

            double tipsSumInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, tipsInCheckSumRegex);
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
                    .append("\nЧаевые - " + percent + "\n")
                    .append(serviceChargeSum + " чистый сервисный сбор по текущей проценту чаевых: " + percent + "\n")
                    .append(cleanTips + " чистые чаевые по общей сумме за блюда\n")
                    .append(totalSum + " чистая сумма за блюда без чаевых и без сервисного сбора\n")
                    .append(totalSumPlusCleanTips + " чистая сумма за блюда c чаевым и без сервисного сбора\n")
                    .append(totalSumPlusCleanTipsPlusServiceChargeSum + " чистая сумма за блюда c чаевым и сервисным сбором\n")
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

        printAndAttachAllureLogs(logs, "Подсчёт сумм");

    }

    @Step("Проверяем что разные проценты чаевых рассчитываются корректно во всех полях и в суммах вместе с СБ не разделяя счёт")
    public void isAllTipsOptionsAreCorrectWithTotalSumAndSC(double totalSum) {

        checkAllTipsOptions(totalSum);

    }


    @Step("Проверка что логика установленных чаевых по умолчанию от общей суммы корректна")
    public void isDefaultTipsBySumLogicCorrect() {

        scrollTillBottom();
        double totalDishSum = getClearOrderAmount();

        if (totalDishSum < 490) {

            if (totalDishSum != 0) {

                totalTipsSumInMiddle.shouldHave(value(MIN_SUM_TIPS_));

            } else {

                totalTipsSumInMiddle.shouldHave(value("0"));

            }

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

            activeTipsButton.shouldHave(text("10%")
                    .because("Если сумма заказа больше 490, то должно быть 10% чаевых по умолчанию"));

        }

    }

    @Step("Выбираем все не оплаченные позиции")
    public void chooseAllNonPaidDishes() {

        hidePaymentOptionsAndTapBar();
        double totalDishesSum = 0;

        isElementsCollectionIsVisible(allNonPaidAndNonDisabledDishes);

        for (int index = 0; index < allNonPaidAndNonDisabledDishes.size(); index++) {

            if (!allNonPaidAndNonDisabledDishesCheckbox.get(index).getCssValue("display").equals("block")) {

                double currentDishPrice = convertSelectorTextIntoDoubleByRgx
                        (allNonPaidAndNonDisabledDishesSum.get(index), dishPriceRegex);
                String currentDishName = allNonPaidAndNonDisabledDishesName.get(index).getText();

                totalDishesSum += currentDishPrice;

                scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
                forceWaitingForSocketChangePositions(WAIT_FOR_SOCKETS_CHANGE_POSITION);
                allNonPaidAndNonDisabledDishesCheckbox.get(index)
                        .shouldBe(cssValue("display", "block"), Duration.ofSeconds(4));

            }

        }

        showPaymentOptionsAndTapBar();

    }

    @Step("Выбираем первое и последнее блюдо")
    public void choseFirstAndLastDishes(ElementsCollection elements) {

        hidePaymentOptionsAndTapBar();
        scrollAndClick(elements.first());
        scrollAndClick(elements.last());
        showPaymentOptionsAndTapBar();

    }

    @Step("Отменяем определенное количество выбранных позиций")
    public void cancelCertainAmountChosenDishes(int count) { //

        hidePaymentOptionsAndTapBar();

        for (int index = 0; index < allNonPaidAndNonDisabledDishes.size(); index++) {

            if (count != 0) {

                if (allNonPaidAndNonDisabledDishesCheckbox.get(index).getAttribute("style").equals("")) {

                    scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
                    forceWait(WAIT_BETWEEN_SET_DISHES_CHECKBOXES); // toDo иначе 422
                    count--;

                }

            }

        }

        showPaymentOptionsAndTapBar();

    }

    @Step("Отменяем все выбранные")
    public void cancelAllChosenDishes() { //

        hidePaymentOptionsAndTapBar();

        for (int index = 0; index < allDishesInOrder.size(); index++) {

            if (allNonPaidAndNonDisabledDishesCheckbox.get(index).getAttribute("style").equals("")) {

                scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
                forceWait(WAIT_BETWEEN_SET_DISHES_CHECKBOXES); // toDo иначе 422

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

        int chosenDishesSize = allNonPaidAndNonDisabledDishesCheckbox
                .filter(attribute("display", "block")).size();

        for (int count = 1; count <= neededDishesAmount; count++) {

            int index;
            String flag;

            if (chosenDishesSize != allNonPaidAndNonDisabledDishes.size()) {

                do {

                    index = generateRandomNumber(1, allNonPaidAndNonDisabledDishes.size()) - 1;
                    flag = allNonPaidAndNonDisabledDishesCheckbox.get(index).getCssValue("display");

                    currentDishPrice = convertSelectorTextIntoDoubleByRgx
                            (allNonPaidAndNonDisabledDishesSum.get(index), dishPriceRegex);
                    currentDishName = allNonPaidAndNonDisabledDishesName.get(index).getText();

                } while (flag.equals("block"));

                totalDishesSum += currentDishPrice;

                scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
                forceWait(1500); // toDo иначе 422

                allNonPaidAndNonDisabledDishesCheckbox.get(index)
                        .shouldBe(cssValue("display", "block"), Duration.ofSeconds(5));

            } else {

                break;

            }

        }

        showPaymentOptionsAndTapBar();

    }

    @Step("Считаем сумму всех не выбранных позиций")
    public double countAllDishes() {

        double totalDishesSum = 0;

        StringBuilder logs = new StringBuilder();

        for (SelenideElement element : allDishesInOrder) {

            double currentDishPrice =
                    convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);
            String currentDishName = element.$(dishNameSelector).getText();

            totalDishesSum += currentDishPrice;
            totalDishesSum = updateDoubleByDecimalFormat(totalDishesSum);

             logs.append("Блюдо - ").append(currentDishName).append(" - ").append(currentDishPrice)
                .append(". Общая цена ").append(totalDishesSum);

        }

        printAndAttachAllureLogs(logs, "Список не оплаченных и не заблокированных блюд");

        return totalDishesSum;

    }

    @Step("Выбираем первое блюдо")
    public void chooseLastDish(ElementsCollection elements) {

        scrollAndClick(elements.last().$(dishNameSelector));
        forceWait(WAIT_BETWEEN_SET_DISHES_CHECKBOXES);

    }

    @Step("Считаем сумму всех выбранных позиций в заказе при разделении") //
    public double countAllChosenDishesDivided() {

        double totalSumInOrder = 0;
        int counter = 0;
        String logs = "";

        isElementsCollectionIsVisible(allNonPaidAndNonDisabledDishes);

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            if (element.$(dishCheckboxSelector).getCssValue("display").equals("block")) {

                double cleanPrice =
                        convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);
                String dishName = element.$(dishNameSelector).getText();

                totalSumInOrder += cleanPrice;
                totalSumInOrder = updateDoubleByDecimalFormat(totalSumInOrder);
                counter++;

                logs += counter + ". " + dishName + " - " + cleanPrice + ". Общая сумма: " + totalSumInOrder + "\n";

            }

        }

        double markedDishesSum = convertSelectorTextIntoDoubleByRgx(TipsAndCheck.markedDishesSum, markedDishesRegex);

        Assertions.assertEquals(markedDishesSum, totalSumInOrder, 0.1);
        return totalSumInOrder;

    }

    @Step("Считаем сумму не оплаченных позиций и заблокированных в заказе")
    public double countAllNonPaidAndDisabledDishesInOrder() {

        double totalSumInOrder = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        for (SelenideElement element : allNonPaidAndDisabledDishes) {

            double cleanPrice = convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);
            String dishName = element.$(dishNameSelector).getText();

            totalSumInOrder += cleanPrice;
            totalSumInOrder = updateDoubleByDecimalFormat(totalSumInOrder);
            counter++;

            logs
                    .append("\n").append(counter).append(". ").append(dishName).append(" - ").append(cleanPrice)
                    .append(". Общая сумма: ").append(totalSumInOrder);

        }

        return totalSumInOrder;

    }

    @Step("Считаем сумму не оплаченных,незаблокированных позиций в заказе")
    public double countAllNonPaidDishesInOrder() {

        double totalSumInOrder = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        isElementsCollectionIsVisible(allNonPaidAndNonDisabledDishes);

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            double cleanPrice = convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);
            String dishName = element.$(dishNameSelector).getText();

            totalSumInOrder += cleanPrice;
            totalSumInOrder = updateDoubleByDecimalFormat(totalSumInOrder);
            counter++;

            logs.append("\n" + counter + ". " + dishName + " - " + cleanPrice + ". Общая сумма: " + totalSumInOrder);

        }

        printAndAttachAllureLogs(logs, "Список не оплаченных и не заблокированных блюд");
        System.out.println(totalSumInOrder +  " total sum");

        return totalSumInOrder;

    }

    public void printAndAttachAllureLogs(StringBuilder logs, String logsName) {

        Allure.addAttachment(logsName, "text/plain", String.valueOf(logs));

    }

    @Step("Считаем сумму не оплаченных и выбранных позиций в заказе")
    public double countAllNonPaidDAndChosenDishesInOrder() {

        double totalSumInOrder = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        for (int index = 0; index < allNonPaidAndNonDisabledDishes.size(); index++) {

            if (allNonPaidAndNonDisabledDishesCheckbox.get(index).getCssValue("display").equals("block")) {

                double cleanPrice = convertSelectorTextIntoDoubleByRgx(allNonPaidAndNonDisabledDishes.get(index)
                        .$(dishPriceTotalSelector), dishPriceRegex);
                String dishName = allNonPaidAndNonDisabledDishes.get(index).$(dishNameSelector).getText();

                totalSumInOrder += cleanPrice;
                totalSumInOrder = updateDoubleByDecimalFormat(totalSumInOrder);
                counter++;

                logs
                        .append("\n").append(counter).append(". ").append(dishName).append(" - ").append(cleanPrice)
                        .append(". Общая сумма: ").append(totalSumInOrder);

            }

        }

        return totalSumInOrder;

    }

    @Step("Забираем коллекцию всех выбранных позиций в заказе для следующего теста")
    public HashMap<Integer, Map<String, Double>> getChosenDishesAndSetCollection() {

        HashMap<Integer, Map<String, Double>> tapperDishes = new HashMap<>();

        int i = 0;

        for (SelenideElement element : allDishesInOrder) {

            if (element.$(dishCheckboxSelector).getAttribute("style").equals("")) {

                Map<String, Double> temporaryMap = new HashMap<>();

                String name = element.$(dishNameSelector).getText();
                double price = convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);

                temporaryMap.put(name, price);
                tapperDishes.put(i, temporaryMap);

                i++;

            }

        }

        return tapperDishes;

    }

    @Step("Проверяем что выбранные ранее блюда заблокированы и в статусе 'Ожидается'")
    public void checkIfDishesDisabledEarlier(HashMap<Integer, Map<String, Double>> chosenDishesEarlier) {

        HashMap<Integer, Map<String, Double>> chosenDishesCurrent = new HashMap<>();

        int i = 0;

        isElementVisible(separateOrderHeading);

        for (SelenideElement element : allDishesInOrder) {

            if (element.$(dishOrderStatusSelector).getText().equals(DISH_STATUS_IS_PAYING)) {

                element.$(dishPreloaderSpinnerSelector)
                        .shouldBe(visible,attribute("style",""));

                Map<String, Double> temporaryMap = new HashMap<>();

                String name = element.$(dishNameSelector).getText();
                double price = convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);

                temporaryMap.put(name, price);
                chosenDishesCurrent.put(i, temporaryMap);

                i++;

            }

        }

        Assertions.assertEquals(chosenDishesEarlier, chosenDishesCurrent, "Блюда не совпадают");

    }

    @Step("Проверяем коллекцию всех выбранных ранее позиций что они теперь оплачены")
    public void checkIfDishesDisabledAtAnotherGuestArePaid(HashMap<Integer,
            Map<String, Double>> chosenDishesByAnotherGuest) {

        HashMap<Integer, Map<String, Double>> paidDishes = new HashMap<>();

        int i = 0;

        for (SelenideElement element : DishList.allPaidDishes) {

            element.$(dishOrderStatusSelector).shouldHave(exist);

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = element.$(dishNameSelector).getText();

            double price = convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);

            temporaryMap.put(name, price);
            paidDishes.put(i, temporaryMap);

            i++;

        }

        Assertions.assertEquals(chosenDishesByAnotherGuest, paidDishes, "Блюда не совпадают");

    }

    @Step("Проверяем что оплаченные,заблокированные позиции нельзя снова выбрать")
    public void checkIfPaidAndDisabledDishesCantBeChosen() {

        hidePaymentOptionsAndTapBar();

        int i = 0;

        for (SelenideElement element : disabledAndPaidDishes) {

            element.$(dishOrderStatusSelector).shouldHave(exist);
            scrollAndClick(element.$(dishNameSelector));
            element.$(dishCheckboxSelector).shouldNotHave(cssValue("display", "block"));

            String name = element.$(dishNameSelector).getText();
            double price = convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);

            i++;

        }

        showPaymentOptionsAndTapBar();

    }

    @Step("Проверяем что сумма всех блюд совпадает с итоговой без чаевых и СБ")
    public void isTotalSumInDishesMatchWithTotalPay(double totalSumByDishesInOrder) {

        isElementVisibleDuringLongTime(totalPay, 2);

        double totalPaySumInCheck = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

        Assertions.assertEquals(totalPaySumInCheck, totalSumByDishesInOrder, 0.01,
                "Сумма за все блюда не совпала с суммой в 'Итого к оплате' ");

    }

    @Step("Удаляем скидки из суммы")
    public void checkTotalSumCorrectAfterRemovingDiscount() {

        discountField.shouldNotBe(exist, Duration.ofSeconds(2));

        double totalClearOrderAmount = getClearOrderAmount();

        if (!totalTipsSumInMiddle.getValue().equals("0"))
            checkTipsAfterReset();

        allDishesInOrder.asDynamicIterable().stream().forEach
                (element -> element.$(dishPriceWithoutDiscountSelector).should(disappear));

        deactivateServiceChargeIfActivated();

        double totalPaySumInCheck = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

        Assertions.assertEquals(totalClearOrderAmount, totalPaySumInCheck, 0.1,
                "Чистая сумма не совпадает с 'Итого к оплате после удаления скидки'");

    }

    @Step("Проверяем что общая чистая сумма совпадает с 'Итого к оплате'")
    public void checkCleanSumMatchWithTotalPay(double cleanDishesSum) {

        double currentTips = getCurrentTipsSum();
        double currentSC = getCurrentSCSum();

        double totalCleanPaySum = countTotalDishesSumWithTipsAndSC(cleanDishesSum, currentTips, currentSC);
        double totalPaySumInCheck = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);
        double totalPaySumInWallet = convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, totalSumInWalletRegex);
        double totalPaySumInPayButton = convertSelectorTextIntoDoubleByRgx(paymentButton, serviceChargeRegex);

        Assertions.assertEquals(totalCleanPaySum, totalPaySumInCheck, 0.1,
                "Чистая сумма не совпадает с суммой в 'Итого к оплате'");

        Assertions.assertEquals(totalPaySumInCheck, totalPaySumInWallet, 0.1,
                "Сумма 'Итого к оплате' не совпадает с суммой в счётчике кошелька");

        Assertions.assertEquals(totalCleanPaySum, totalPaySumInPayButton, 0.1,
                "Сумма в кнопке оплатить не совпадает с чистой суммой");

    }

    @Step("Проверяем все варианты чаевых с общей суммой с сб")
    public void checkTipsOptionWithSC(double cleanDishesSum) {

        StringBuilder logs = new StringBuilder();

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            click(tipsOption);
            tipsOption
                    .shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(1));

            int totalTipsSumInMiddle =
                    Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));
            double tipsSumInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, tipsInCheckSumRegex);

            Assertions.assertEquals(totalTipsSumInMiddle, tipsSumInCheck,
                    "Чаевые по центру не совпадают с чаевыми в поле 'Чаевые'");

            double cleanTips = 0;
            int percent = convertSelectorTextIntoIntByRgx(tipsOption, percentRegex);

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;
                cleanTips = Math.round(cleanDishesSum * percentForCalculation);

            }

            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);
            double totalPaySumInWallet =
                    convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, totalSumInWalletRegex);

            double serviceChargeSum = getCurrentSCSum();
            double totalCleanPaySum = cleanDishesSum + serviceChargeSum + cleanTips;

            Assertions.assertEquals(totalCleanPaySum, totalPaySum, 1,
                    "Чистая общая сумма не совпадает с 'Итого к оплате'");
            Assertions.assertEquals(totalPaySum, totalPaySumInWallet,
                    "Сумма 'Итого к оплате' не совпадает с суммой в счётчике кошелька");

        }

    }

    @Step("Проверяем все варианты чаевых с общей суммой без сб")
    public void checkTipsOptionWithoutSC(double cleanDishesSum) {

        StringBuilder logs = new StringBuilder();

        deactivateServiceChargeIfActivated();

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(2));

            int totalTipsSumInMiddle =
                    Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));
            double tipsSumInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, tipsInCheckSumRegex);

            Assertions.assertEquals(totalTipsSumInMiddle, tipsSumInCheck,
                    "Чаевые по центру не совпадают с чаевыми в поле 'Чаевые'");

            double cleanTips = 0;
            int percent = convertSelectorTextIntoIntByRgx(tipsOption, percentRegex);

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;
                cleanTips = Math.round(cleanDishesSum * percentForCalculation);

            }

            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);
            double totalPaySumInWallet =
                    convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, totalSumInWalletRegex);
            double totalCleanPaySum = cleanDishesSum + cleanTips;

            Assertions.assertEquals(totalCleanPaySum, totalPaySum, 1,
                    "Чистая общая сумма не совпадает с 'Итого к оплате'");
            Assertions.assertEquals(totalPaySum, totalPaySumInWallet,
                    "Сумма 'Итого к оплате' не совпадает с суммой в счётчике кошелька");

        }

    }

    @Step("Чаевые не должны отображаться у верифицированного официанта без привязанной карты")
    public void checkIsNoTipsElementsIfVerifiedNonCard() {

        isElementVisible(tipsContainer);
        isImageCorrect(waiterImageNotSelenide, "Изображение официанта не корректное или битое");
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

            double serviceChargeInField =
                    convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, serviceChargeRegex);
            serviceChargeInField = updateDoubleByDecimalFormat(serviceChargeInField);

            double serviceChargeSumClear =
                    updateDoubleByDecimalFormat
                            (cleanDishesSum * (SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM / 100));

            double serviceChargeTipsClear =
                    updateDoubleByDecimalFormat
                            (tipsSumInTheMiddle * (SERVICE_CHARGE_PERCENT_FROM_TIPS / 100));

            double serviceChargeTotal = serviceChargeTipsClear + serviceChargeSumClear;
            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

            if ((cleanDishesSum / 100 * SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM) +
                    (tipsSumInTheMiddle / 100 * SERVICE_CHARGE_PERCENT_FROM_TIPS) > SERVICE_CHARGE_MAX) {

                Assertions.assertTrue(serviceChargeInField <= SERVICE_CHARGE_MAX,
                        "Максимальный сервисный сбор не установился");
                serviceChargeTotal = SERVICE_CHARGE_MAX;

            }

            double totalDishesCleanSum = cleanDishesSum + tipsSumInTheMiddle + serviceChargeTotal;
            double totalPaySumInWallet =
                    convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, totalSumInWalletRegex);

            Assertions.assertEquals(serviceChargeInField, serviceChargeTotal, 0.1,
                    "Сервисный сбор считается не корректно по формуле "
                            + SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM + "  от суммы и "
                            + SERVICE_CHARGE_PERCENT_FROM_TIPS + " от чаевых");

            Assertions.assertEquals(totalDishesCleanSum, totalPaySum, 0.1,
                    "Чистая общая сумма не совпадает с 'Итого к оплате'");

            Assertions.assertEquals(totalDishesCleanSum, totalPaySumInWallet, 0.1,
                    "Сумма 'Итого к оплате' не совпадает с суммой в счётчике кошелька");

        }

    }

    @Step("Проверяем что текущие чаевые в поле 'Чаевые' сходятся с чаевыми по центру. Отдаём чаевые")
    public double getCurrentTipsSum() {

        double tipsInTheMiddleSum = 0;

        if (totalTipsSumInMiddle.exists()) {

            tipsInTheMiddleSum = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));

            double tipsInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, tipsInCheckSumRegex);

            Assertions.assertEquals(tipsInTheMiddleSum, tipsInCheck,
                    "Чаевые по центру не совпадают с чаевыми в поле 'Чаевые'");

        }

        return tipsInTheMiddleSum;

    }


    public double getCurrentDiscount() {

        double discount = 0;

        if (discountField.exists())
            discount = convertSelectorTextIntoDoubleByRgx(discountSum,discountRegex);

        return discount;

    }

    @Step("Забираем сумму сервисного сбора если он включен")
    public double getCurrentSCSum() {

        double serviceChargeSum = 0;

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            serviceChargeSum = convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, serviceChargeRegex);

            Assertions.assertTrue(serviceChargeSum >= 0,
                    "Сервисный сбор имеет отрицательное значение");

            serviceChargeSum = updateDoubleByDecimalFormat(serviceChargeSum);

        }

        return serviceChargeSum;

    }

    @Step("Считаем общую сумму за все блюда + чаевые + сбор")
    public double countTotalDishesSumWithTipsAndSC(double cleanDishesSum, double currentTips, double currentSC) {

        cleanDishesSum = cleanDishesSum + currentTips + currentSC;
        return cleanDishesSum;

    }

    @Step("Устанавливаем рандомные чаевые")
    public void setRandomTipsOption() {

        click(resetTipsButton);

        if (notDisabledAndNotZeroTipsPercentOptions.size() != 0) {

            int index = generateRandomNumber(1, notDisabledAndNotZeroTipsPercentOptions.size()) - 1;

            click(notDisabledAndNotZeroTipsPercentOptions.get(index));
            activeTipsButton
                    .shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(1));

        }

    }

    @Step("Сброс чаевых и проверка на всех полях")
    public void checkTipsAfterReset() {

        scrollTillBottom();
        click(resetTipsButton);
        activeTipsButton.shouldHave(text("0%"));

        totalTipsSumInMiddle.shouldHave(value("0"));
        double tipsInTheCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, tipsInCheckSumRegex);
        Assertions.assertEquals(tipsInTheCheck, 0);

        int percent = convertSelectorTextIntoIntByRgx(tips0, percentRegex);
        double tipsSumInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, tipsInCheckSumRegex);
        int totalTipsSumInMiddle =
                Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));
        double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);
        double currentSumInWallet =
                convertSelectorTextIntoDoubleByRgx(TapBar.totalSumInWalletCounter, totalSumInWalletRegex);

        Assertions.assertEquals(percent, 0, "Текущий процент чаевых не равен 0");
        Assertions.assertEquals(totalTipsSumInMiddle, 0, 0.1, "Общие чаевые в центре не равны 0");
        Assertions.assertEquals(tipsSumInCheck, 0, 0.1, "Чаевые в поле 'Чаевые' не равны 0");
        Assertions.assertEquals(totalPaySum, currentSumInWallet, 0.1,
                "Сумма в 'Итого к оплате' не равна сумме в иконке кошелька");

    }

    @Step("Сброс чаевых")
    public void resetTips() {

        click(resetTipsButton);
        activeTipsButton.shouldHave(text("0%"));
        totalTipsSumInMiddle.shouldHave(value("0"));

    }


    @Step("Проверяем что сумма 'Итого к оплате' совпадает с суммой счетчика в иконке кошелька")
    public void isSumInWalletMatchWithTotalPay() {

        if (!appFooter.exists())
            showPaymentOptionsAndTapBar();

        String totalPaySum = convertSelectorTextIntoStrByRgx(totalPay, totalPayRegex);
        String currentSumInWallet =
                convertSelectorTextIntoStrByRgx(TapBar.totalSumInWalletCounter, dishPriceRegex);

        Assertions.assertEquals(totalPaySum, currentSumInWallet,
                "Сумма 'Итого к оплате' не совпадает с суммой в иконке кошелька");

    }

    @Step("Проверяем что процент установленных по умолчанию чаевых рассчитывается корректно с СБ и без")
    public void isActiveTipPercentCorrectWithTotalSumAndSC(double totalSumWithoutTips) {

        if (totalTipsSumInMiddle.exists()) {

            deactivateServiceChargeIfActivated();

            isElementVisibleDuringLongTime(activeTipsButton, 15);

            double tipPercent = convertSelectorTextIntoDoubleByRgx(activeTipsButton, percentRegex);
            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);
            double totalSumWithTips = totalSumWithoutTips + Math.round(totalSumWithoutTips * (tipPercent / 100));

            Assertions.assertEquals(totalSumWithTips, totalPaySum, 0.1,
                    "Процент чаевых установленный по умолчанию не совпадает с общей ценой за все блюда без СБ");

        }

    }

    @Step("Проверка что сумма 'Другой пользователь' совпадает с суммой оплаченных позиций")
    public void isAnotherGuestSumCorrect() {

        anotherGuestField.shouldBe(visible);

        double totalPaidSum = 0;
        double anotherGuestSum =
                convertSelectorTextIntoDoubleByRgx(TipsAndCheck.anotherGuestSum, anotherGuestSumInCheckRegex);

        for (SelenideElement element : disabledAndPaidDishes) {

            double currentElementSum =
                    convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);
            totalPaidSum += currentElementSum;

        }

        Assertions.assertEquals(anotherGuestSum, totalPaidSum, 0.1);

    }

    @Step("Проверка что в форме 'Итого к оплате' ")
    public void isCheckContainerShown() {

        isElementVisible(checkContainer);
        isElementVisible(totalPay);

        if (tipsContainer.exists())
            isElementVisible(tipsInCheckField);

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
        isImageCorrect(shareButtonSvgNotSelenide, "Иконка в кнопке поделиться счётом не корректная");

        //  Assertions.assertTrue(isShareActive, "Кнопка 'Поделиться счётом' не вызывает панель поделиться ссылкой"); toDo придумать что можно сделать с "Поделиться счётом"
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

        hidePaymentOptionsAndTapBar();

        isElementVisible(termOfUseLink);
        isElementVisible(confPolicyLink);

        click(termOfUseLink);
        isElementVisible(termOfUseContainer);
        termOfUseContainer.shouldHave(matchText(TERM_OF_USE_HEADING_CONTENT));
        termOfUseOverlay.click(ClickOptions.usingJavaScript());
        isElementInvisible(termOfUseContainer);

        click(confPolicyLink);
        isElementVisible(confPolicyContainer);
        confPolicyContainer.shouldHave(matchText(CONF_LINK_HEADING_CONTENT));
        confPolicyOverlay.click(ClickOptions.usingJavaScript());
        isElementInvisible(confPolicyContainer);

        showPaymentOptionsAndTapBar();

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

    public LinkedHashMap<String,Double> getTableData() {

        LinkedHashMap<String,Double> tableData = new LinkedHashMap<>();

        double totalPaySum = countAllNonPaidAndDisabledDishesInOrder();
        double serviceCharge = getCurrentSCSum();
        double tips = getCurrentTipsSum();
        double discount = getCurrentDiscount();

        tableData.put("totalPaySum",totalPaySum);
        tableData.put("serviceCharge",serviceCharge);
        tableData.put("tips",tips);
        tableData.put("discount",discount);

        return tableData;

    }

    public LinkedHashMap<Integer, Map<String,Double>> getDishList(ElementsCollection dishes) {

        LinkedHashMap<Integer, Map<String,Double>> dishList = new LinkedHashMap<>();

        for (int dishIndex = 0 ;dishIndex < dishes.size(); dishIndex++) {

            String dishName = dishes.get(dishIndex).$(dishNameSelector).getText();
            double dishPrice =
                    convertSelectorTextIntoDoubleByRgx(dishes.get(dishIndex).$(dishPriceTotalSelector),dishPriceRegex);

            Map<String,Double> dishNameAndPrice = new HashMap<>();
            dishNameAndPrice.put(dishName,dishPrice);
            dishList.put(dishIndex,dishNameAndPrice);

        }

        return dishList;

    }


    public void isCurrentTableDataCorrectAfterErrorPayment(ElementsCollection dishes,
                                                           LinkedHashMap<Integer, Map<String, Double>> previousDishList,
                                                           LinkedHashMap<String,Double> previousTableData) {

        dishes.asFixedIterable().stream().forEach(element -> element.$(dishOrderStatusSelector).shouldHave(empty));

        LinkedHashMap<String,Double> currentTableData = getTableData();
        LinkedHashMap<Integer, Map<String, Double>> currentDishList = getDishList(dishes);

        Assertions.assertEquals(previousTableData,currentTableData,"Данные не совпадают после ошибки с оплатой");
        Assertions.assertEquals(previousDishList,currentDishList,"Заказ не совпадает");

    }

    public void isPaymentDisabled() {

        if (divideCheckSlider.isDisplayed()) {

            allDishesInOrder.asDynamicIterable().stream().forEach(element ->
                    element.shouldHave(or("Должны быть оплачены или нет",
                            matchText(DISH_STATUS_IS_PAYING),matchText(DISH_STATUS_PAYED))));

        }

        paymentButton.shouldHave(disabled,matchText(PAYMENT_BUTTON_DISABLED_PAYMENT));
        totalTipsSumInMiddle.shouldHave(value("0"));
        totalSumInWalletCounter.shouldHave(matchText("0 ₽"));

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

        return convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

    }

    @Step("Получаем чистую сумму за вычетом всех сб,чаевых,скидки,наценки,оплаченных ранее")
    public double getClearOrderAmount() {

        double serviceChargeSum = 0;
        double tips = 0;
        double anotherGuestSumPaid;
        double orderAmount;

        if (serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            serviceChargeSum = convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, serviceChargeRegex);
            serviceChargeSum = updateDoubleByDecimalFormat(serviceChargeSum);

        }

        if (totalTipsSumInMiddle.exists()) {

            tips = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, dishPriceRegex);

        }

        if (anotherGuestSum.isDisplayed()) {

            anotherGuestSumPaid = convertSelectorTextIntoDoubleByRgx(anotherGuestSum, anotherGuestSumInCheckRegex);

        }

        if (divideCheckSliderActive.isDisplayed()) {

            orderAmount = countAllNonPaidDAndChosenDishesInOrder();

        } else {

            orderAmount = countAllNonPaidDishesInOrder();

        }

        orderAmount = updateDoubleByDecimalFormat(orderAmount);

        return orderAmount;

    }

    @Step("Получаем чистую сумму за вычетом всех сб,чаевых,скидки,наценки,оплаченных ранее")
    public double getOrderAmountForOperationHistory() {

        double serviceChargeSum = 0;
        double tips = 0;

        if (serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            serviceChargeSum = convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, serviceChargeRegex);
            serviceChargeSum = updateDoubleByDecimalFormat(serviceChargeSum);

        }

        if (totalTipsSumInMiddle.exists()) {

            tips = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, dishPriceRegex);

        }

        double orderAmount = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

        orderAmount = orderAmount - tips - serviceChargeSum;
        orderAmount = updateDoubleByDecimalFormat(orderAmount);

        return orderAmount;

    }

    @Step("Сохранение общей суммы, чаевых, СБ для проверки с транзакцией б2п")
    public HashMap<String, String> savePaymentDataTapperForB2b() {

        HashMap<String, String> paymentData = new HashMap<>();

        String tips = "0";
        String fee = "0";

        double orderAmountDouble = getOrderAmountForOperationHistory();

        String orderAmountString = String.valueOf(orderAmountDouble);

        if (orderAmountString.matches("\\d{1,}\\.\\d{1}$")) {

            orderAmountString = String.valueOf(orderAmountDouble) + "0";

        } else {

            orderAmountString = String.valueOf(orderAmountDouble);

        }

        String orderAmount = orderAmountString.replaceAll("\\.","");

        paymentData.put("order_amount", orderAmount);

        if (totalTipsSumInMiddle.exists() && !totalTipsSumInMiddle.getValue().equals("0")) {

            tips = totalTipsSumInMiddle.getValue() + "00";

        }

        paymentData.put("tips", tips);

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            double feeDouble = convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, serviceChargeRegex);
            String feeConverted = convDoubleWithDecimal(feeDouble);

            if (feeConverted.matches("\\d{1,}\\.\\d{1}$")) {

                fee = feeConverted + "0";

            } else if (feeConverted.matches("\\d{1,}$")) {

                fee = feeConverted + "00";

            } else {

                fee = feeConverted;

            }

            fee = fee.replaceAll("\\.","");

        }

        paymentData.put("fee", fee);
        return paymentData;

    }

    @Step("Проверка нижнего навигационного меню. Проверки на элементы, кликабельность, переходы, открытие")
    public void isTapBarShown() {

        isElementVisible(appFooter);
        isElementVisibleAndClickable(appFooterMenuIcon);
        isElementVisibleAndClickable(totalSumInWalletCounter);

        click(appFooterMenuIcon);

        if (!emptyOrderMenuButton.isDisplayed())
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
        callWaiterTypingMessagePreloader.shouldHave(disappear, Duration.ofSeconds(5));
        isElementVisibleDuringLongTime(callWaiterFirstGreetingsMessage, 5);

    }

    @Step("Проверка меню")
    public void emptyMenuCorrect() {

        isElementVisible(emptyTableLogoClock);
        isElementVisible(emptyOrderMenuDescription);
        emptyOrderMenuButton.shouldNotBe(disabled);

        click(emptyOrderMenuButton);
        isElementVisible(thanksFeedBackAlert);

    }

    @Step("Закрытие формы по крестику закрытия")
    public void closeCallWaiterFormByCloseButton() {

        callWaiterCloseButton.shouldBe(visible,enabled);
        click(callWaiterCloseButton);
        isElementInvisible(callWaiterContainer);

    }

    @Step("Написать 'счет' чтобы получить специальное сообщение от таппера")
    public void typeTextToGetSpecialMessage() {

        sendKeys(callWaiterCommentArea, "Счет");
        callWaiterCommentArea.shouldHave(value("Счет"));

        isElementVisible(callWaiterButtonSend);
        click(callWaiterButtonSend);

        callWaiterTapperMessage.shouldBe(visible);

        sendKeys(callWaiterCommentArea, "Счет");
        callWaiterCommentArea.shouldHave(value("Счет"));

        isElementVisible(callWaiterButtonSend);
        click(callWaiterButtonSend);

        callWaiterTypingMessagePreloader.shouldHave(disappear, Duration.ofSeconds(5));
        callWaiterSecondMessage.last().shouldBe(visible,Duration.ofSeconds(5));


    }

    @Step("Отправка текста в комментарий официанта")
    public void sendWaiterComment() {

        callWaiterCommentArea.shouldHave(attribute("placeholder", "Cообщение"));
        sendKeys(callWaiterCommentArea, TapperTable.TEST_WAITER_COMMENT);
        callWaiterCommentArea.shouldHave(value(TapperTable.TEST_WAITER_COMMENT));

        isElementVisible(callWaiterButtonSend);
        click(callWaiterButtonSend);
        callWaiterSecondMessage.first().shouldBe(visible,Duration.ofSeconds(3));

    }

    @Step("Ввод текста в комментарий официанта")
    public void isSendSuccessful() {

        isElementVisible(callWaiterGuestTestComment);
        isElementsCollectionIsVisible(callWaiterSecondMessage);

    }

    @Step("Повторное открытие формы вызовы официанта для проверки что история сохранилась")
    public void isHistorySaved() {

        isElementVisible(callWaiterGuestTestComment);
        isElementsCollectionIsVisible(callWaiterSecondMessage);
        isElementInvisible(callWaiterTypingMessagePreloader);

    }

    @Step("Закрытие и открытие браузера для проверки что история сохранилась")
    public void isHistorySavedByClosingBrowser(String url) {

        Cookie itemMess = WebDriverRunner.getWebDriver().manage().getCookieNamed("itemsMess");
        Selenide.closeWebDriver();

        openPage(url);
        skipStartScreenLogo();
        WebDriverRunner.getWebDriver().manage().addCookie(itemMess);

        click(callWaiterButton);
        isHistorySaved();

    }

    @Step("Вызов официанта. Проверки на элементы, кликабельность, заполнение, открытие\\закрытие")
    public void isCallWaiterCorrect() {

        openCallWaiterForm();

        isCallContainerWaiterCorrect();

        sendWaiterComment();

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
        isTableHasOrder();

    }

    @Step("Сохраняем информацию по меню со стола в таппере")
    public LinkedHashMap<String, Map<String, String>> saveTapperMenuData() {

        LinkedHashMap<String, Map<String, String>> menuData = new LinkedHashMap<>();

        for (SelenideElement selenideElement : menuCategoryContainerName) {

            String categoryName = selenideElement.getText();
            String dishSizeXpath =
                    "//*[@class='orderMenuList']//*[@class='orderMenuList__item'][.//*[contains(text(),'" +
                            categoryName + "')]]//*[@class='orderMenuProductList__item']";

            ElementsCollection dishElement = $$x(dishSizeXpath);

            Map<String, String> dishList = new HashMap<>();

            for (SelenideElement element : dishElement) {

                String dishName = element.$(dishMenuItemsNameSelector).getText();
                String dishPrice = convertSelectorTextIntoStrByRgx(element.$(dishMenuItemsPriceSelector),dishPriceRegex);
                String dishImage = "";

                if (element.$(dishMenuPhotoSelector).exists())
                    dishImage = element.$(dishMenuPhotoSelector).getAttribute("src");

                dishList.put("name", dishName);
                dishList.put("price", dishPrice);
                dishList.put("image", dishImage);

            }

            menuData.put(categoryName, dishList);

        }

        return menuData;

    }

    @Step("Сохранения данных для проверки истории операции в админке")
    public HashMap<Integer, HashMap<String, String>> saveOrderDataForOperationsHistoryInAdmin() {

        HashMap<Integer, HashMap<String, String>> orderData = new HashMap<>();
        HashMap<String, String> temporaryHashMap = new HashMap<>();

        System.out.println(tableNumber);
        String table = convertSelectorTextIntoStrByRgx(tableNumber, "Стол ");
        String name = waiterName.getText();
        String tips = convertSelectorTextIntoStrByRgx(tipsInCheckField, tipsInCheckSumRegex);

        if (Objects.equals(tips, "")) {

            tips = "0";

        }

        double totalSum = getOrderAmountForOperationHistory();

        String totalSumStr = String.valueOf(totalSum).replaceAll("\\..+", "");

        String date = getCurrentDateInFormat("dd.MM.yyyy");

        temporaryHashMap.put("date", date);
        temporaryHashMap.put("table", table);
        temporaryHashMap.put("name", name);
        temporaryHashMap.put("tips", tips);
        temporaryHashMap.put("totalSum", totalSumStr);

        orderData.put(0, temporaryHashMap);

        return orderData;

    }

    @Step("Сопоставление данных из стола и с операцией в истории операции в админке")
    public void matchTapperOrderDataWithAdminOrderData(HashMap<Integer, HashMap<String, String>> tapperOrderData,
                                                       HashMap<Integer, HashMap<String, String>> adminOrderData) {

       // System.out.println("\n TAPPER\n" + tapperOrderData + "\n ADMIN\n" + adminOrderData + "\n");

        boolean hasOperationMatch = false;

        for (int index = 0; index < adminOrderData.size(); index++) {

            if (tapperOrderData.get(0).equals(adminOrderData.get(index))) {

                hasOperationMatch = true;
                break;

            }

        }

        Assertions.assertTrue(hasOperationMatch, "Оплаты нет есть в списке операций");

    }

    @Step("Открытие стола и смена гостя")
    public void openTableAndSetGuest(String table, String guest, String session) {

        openNotEmptyTable(table);
        setUserCookie(guest, session);

    }

    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public LinkedHashMap<String, String> getPaymentTgMsgData(String guid) {

        final String[] msg = new String[1];

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(
                        () -> Assertions.assertNotNull(msg[0] = telegram.getLastTgPayMsgList(guid)));

        Allure.addAttachment("telegram message", String.valueOf(msg[0]));

        HashMap<String, String> msgWithType = telegram.setMsgTypeFlag(msg[0]);

        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

        return parsedMsg;

    }

    @Step("Получение и парсинг сообщения отзыва с рейтингом в телеграмм")
    public LinkedHashMap<String, String> getReviewTgMsgData(String guid) {

        forceWait(WAIT_FOR_TELEGRAM_MESSAGE_REVIEW);

        final String[] msg = new String[1];

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(
                        () -> Assertions.assertNotNull(msg[0] = telegram.getLastTgWaiterMsgList(guid)));

        Allure.addAttachment("telegram message", String.valueOf(msg[0]));

        HashMap<String, String> msgWithType = telegram.setMsgTypeFlag(msg[0]);

        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

        return parsedMsg;

    }


    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public LinkedHashMap<String, String> getPaymentTgMsgData(String guid,String payment) {

        forceWait(WAIT_FOR_TELEGRAM_MESSAGE_FULL_PAY);

        final String[] msg = new String[1];

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(
                        () -> Assertions.assertNotNull(msg[0] = telegram.getLastTgPayMsgList(guid)));

        Allure.addAttachment("telegram message", String.valueOf(msg[0]));

        HashMap<String, String> msgWithType = telegram.setMsgTypeFlag(msg[0]);

        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

        return parsedMsg;

    }

    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public LinkedHashMap<String, String> getCallWaiterTgMsgData(String tableNumber, int waitingTime) {

        forceWait(waitingTime);

        final String[] msg = new String[1];

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(
                        () -> Assertions.assertNotNull(msg[0] = telegram.getLastTgCallWaiterMsgList(tableNumber)));

        System.out.println("Сообщение появилось в телеграмме");

        HashMap<String, String> msgWithType = telegram.setMsgTypeFlag(msg[0]);

        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

        return parsedMsg;

    }

    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public void getAdminSendingMsgData(String tableId, String textInSending, int waitingTime) {

        final String[] msg = new String[1];

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(
                        () -> Assertions.assertNotNull(msg[0] = telegram.getLastTgPayMsgList(textInSending)));

        System.out.println("Сообщение появилось в телеграмме");

        HashMap<String, String> msgWithType = telegram.setMsgTypeFlag(msg[0]);

        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

    }

    @Step("Сбор данных со стола для проверки с телеграм сообщением")
    public LinkedHashMap<String, String> getTapperDataForTgCallWaiterMsg(String waiter,String callWaiterComment, String tableNumber) {

        LinkedHashMap<String, String> tapperDataForTgMsg = new LinkedHashMap<>();

        tapperDataForTgMsg.put("waiter", waiter);
        tapperDataForTgMsg.put("tableNumber", tableNumber);
        tapperDataForTgMsg.put("callWaiterComment", callWaiterComment);

        return tapperDataForTgMsg;

    }

    @Step("Сбор данных со стола для проверки с телеграм сообщением")
    public LinkedHashMap<String, String> getTapperDataForReviewMsg(String tableNumber,String waiter,
                                                                   String comment, String rating,
                                                                   String suggestions) {

        LinkedHashMap<String, String> tapperDataForTgMsg = new LinkedHashMap<>();

        tapperDataForTgMsg.put("tableNumber", tableNumber);
        tapperDataForTgMsg.put("waiter", waiter);
        tapperDataForTgMsg.put("comment", comment);
        tapperDataForTgMsg.put("rating", rating);

        if (suggestions != null)
            tapperDataForTgMsg.put("suggestions", suggestions);

        return tapperDataForTgMsg;

    }

    @Step("Проверяем что чаевые и процент сбрасываются согласно сумме после обновления страницы")
    public void isTipsResetAfterRefreshPage() {

        scrollTillBottom();
        click(tips25);

        double activeTipsPercentBeforeRefresh =
                Integer.parseInt(activeTipsButton.getText().replaceAll("%",""));

        double activeTipsBeforeRefresh = Integer.parseInt(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));

        refreshPage();
        isTableHasOrder();

        double activeTipsPercentAfterRefresh =
                Integer.parseInt(activeTipsButton.getText().replaceAll("%",""));

        double activeTipsAfterRefresh = Integer.parseInt(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));

        Assertions.assertNotEquals(activeTipsPercentBeforeRefresh,activeTipsPercentAfterRefresh,
                "Процент чаевых по умолчанию сохранился после обновления страницы, но не должен");
        Assertions.assertNotEquals(activeTipsBeforeRefresh,activeTipsAfterRefresh,
                "Чаевые по умолчанию сохранились после обновления страницы, но не должны были");

    }

    @Step("Сбор данных со стола для проверки с телеграм сообщением")
    public LinkedHashMap<String, String> getTapperDataForTgPaymentMsg(String tableId) {

        String payStatus;
        String orderStatus;
        double serviceChargeSumDouble = 0;
        LinkedHashMap<String, String> tapperDataForTgMsg = new LinkedHashMap<>();

        double sumInCheckDouble = countAllDishes();

        double discountDouble = discountSum.isDisplayed() ?
                convertSelectorTextIntoDoubleByRgx(discountSum, discountInCheckRegex) : 0;

        sumInCheckDouble += discountDouble;

        double restToPayDouble = divideCheckSliderActive.isDisplayed() ?
                 countAllNonPaidAndDisabledDishesInOrder() - countAllChosenDishesDivided() :
                countAllNonPaidAndDisabledDishesInOrder();

        restToPayDouble = updateDoubleByDecimalFormat(restToPayDouble);

        double tipsDouble = totalTipsSumInMiddle.isDisplayed() ?
                Double.parseDouble(totalTipsSumInMiddle.getValue()) : 0;

        if (serviceChargeCheckboxSvg.getCssValue("display").equals("none")) {

            serviceChargeSumDouble = tipsDouble
                    / 100 * Constants.SERVICE_CHARGE_PERCENT_WHEN_DEACTIVATED;

            BigDecimal bd = new BigDecimal(Double.toString(serviceChargeSumDouble));
            BigDecimal serviceChargeSum = bd.setScale(2, RoundingMode.HALF_UP);

            tipsDouble -= Double.parseDouble(String.valueOf(serviceChargeSum));

        }

        double paySumDouble = getClearOrderAmount();

        Response rsGetOrder = apiRKeeper.getOrderInfo(tableId, AUTO_API_URI);

        double totalPaidDouble = countAllNonPaidDishesInOrder();

        if (rsGetOrder.path("result.CommandResult.Order[\"@attributes\"].prepaySum") == null) {

            totalPaidDouble = paySumDouble;

        } else {

            double unpaidSum = Double.parseDouble(apiRKeeper.getPrepaySumSumFromGetOrder(rsGetOrder)) / 100;
            totalPaidDouble = paySumDouble + unpaidSum;

        }

        payStatus = "Частично оплачено";
        orderStatus = "Предоплата успешно прошла по кассе";

        if (totalPaidDouble == sumInCheckDouble || (totalPaidDouble + discountDouble) == sumInCheckDouble) {

            payStatus = "Полностью оплачено";
            orderStatus = "Успешно закрыт на кассе";

            restToPayDouble = (sumInCheckDouble - discountDouble) - totalPaidDouble;

        }

        String tableString = tableNumber.getText().replaceAll(tableRegexTelegramMessage, "$2");
        String sumInCheck = convDoubleWithDecimal(sumInCheckDouble);
        String paySum = convDoubleWithDecimal(paySumDouble);
        String totalPaid = convDoubleWithDecimal(totalPaidDouble);
        String restToPay = convDoubleWithDecimal(restToPayDouble);
        String tips = convDoubleWithDecimal(tipsDouble);
        String discount = convDoubleWithDecimal(discountDouble);
        String waiter = tipsContainer.isDisplayed() ? waiterName.getText() : UNKNOWN_WAITER;

        tapperDataForTgMsg.put("table", tableString);
        tapperDataForTgMsg.put("sumInCheck", sumInCheck);
        tapperDataForTgMsg.put("restToPay", restToPay);
        tapperDataForTgMsg.put("tips", tips);
        tapperDataForTgMsg.put("paySum", paySum);
        tapperDataForTgMsg.put("totalPaid", totalPaid);

        if (discountSum.isDisplayed())
            tapperDataForTgMsg.put("discount", discount);

        tapperDataForTgMsg.put("payStatus", payStatus);
        tapperDataForTgMsg.put("orderStatus", orderStatus);
        tapperDataForTgMsg.put("waiter", waiter);

      //  System.out.println("\nПодготовка данных к сообщению в тг\n" + sumInCheckDouble + " Сумма в чеке" +
       //         "Осталось оплатить: " + restToPayDouble + "Всего оплачено " + totalPaidDouble);

        return tapperDataForTgMsg;

    }

    public String convDoubleWithDecimal(double doubleForCheck) {

        String convertedDouble = "";

        if (doubleForCheck % 1.0 > 0) {

            convertedDouble = String.valueOf(doubleForCheck);

        } else {

            convertedDouble = String.valueOf(doubleForCheck).replaceAll("\\.\\d{1,3}", "");

        }

        return convertedDouble;

    }

    public Map<Integer,Map<String,Double>>
    saveDishPricesInCollection(ElementsCollection dishes, String priceWithDiscountSelector,
                               String priceWithoutDiscountSelector, String priceRegex) {

        Map<Integer,Map<String,Double>> data = new HashMap<>();
        Map<String,Double> dish = new HashMap<>();

        for (int index = 0; index < dishes.size(); index++) {

            double totalPrice = convertSelectorTextIntoDoubleByRgx
                    (dishes.get(index).$(priceWithoutDiscountSelector),priceRegex);
            double discountPrice = convertSelectorTextIntoDoubleByRgx
                (dishes.get(index).$(priceWithDiscountSelector),priceRegex);

            dish.put("totalPrice",totalPrice);
            dish.put("discountPrice",discountPrice);

            data.put(index,dish);

        }

        return data;

    }


    public void matchDishesDiscountPriceAfterAddingDiscount(Map<Integer,Map<String,Double>> dishesBeforeAddingDiscount,
                                                            Map<Integer,Map<String,Double>> dishesAfterAddingDiscount) {

       for (int index = 0; index < dishesBeforeAddingDiscount.size(); index++) {

           double totalPriceBefore = dishesBeforeAddingDiscount.get(index).get("totalPrice") ;
           double totalPriceAfter = dishesAfterAddingDiscount.get(index).get("totalPrice");
           double discountPriceBefore = dishesBeforeAddingDiscount.get(index).get("discountPrice") ;
           double discountPriceAfter = dishesAfterAddingDiscount.get(index).get("discountPrice");

           if (totalPriceBefore != totalPriceAfter )
               Assertions.fail("После добавления еще одной скидки изменилась общая цена");

           if (discountPriceAfter > discountPriceBefore )
               Assertions.fail("Цена должна уменьшиться, т.к. добавилась еще одна скидка к прошлой");

       }

    }


    @Step("Сравнение данных сообщения из тг и таппера")
    public void matchTgMsgDataAndTapperData(LinkedHashMap<String, String> telegramDataForTgMsg,
                                            LinkedHashMap<String, String> tapperDataForTgMsg) {

        Assertions.assertNotNull(telegramDataForTgMsg, "Пустые данные по сообщению из тг");
        Assertions.assertNotNull(tapperDataForTgMsg, "Пустые данные по тапперу");

        if (telegramDataForTgMsg.containsValue("orderStatus") &&
                telegramDataForTgMsg.get("orderStatus").matches("(?s).*[\\n\\r].*SeqNumber.*")) {

            System.out.println("\nОшибка SeqNumber. Удаляем из карты поле\n");
            telegramDataForTgMsg.remove("orderStatus");
            telegramDataForTgMsg.remove("reasonError");
            tapperDataForTgMsg.remove("orderStatus");

        }

        //System.out.println("\nTELEGRAM DATA\n" + telegramDataForTgMsg + "\nTAPPER DATA\n" + tapperDataForTgMsg);

        Assertions.assertEquals(telegramDataForTgMsg, tapperDataForTgMsg,
                "Сообщение в телеграмме не корректное");

    }

    @Step("Получаем скидку")
    public double getDiscount(String tableId) {

        Response rsGetOrder = apiRKeeper.getOrderInfo(tableId, AUTO_API_URI);
        return Math.abs(rsGetOrder.jsonPath()
                .getDouble("result.CommandResult.Order[\"@attributes\"].discountSum") / 100);

    }

    @Step("Проверка вайфая на столе")
    public void checkWiFiOnTapperTable(String wifiName, String wifiPassword) {

        isElementVisibleAndClickable(wiFiIcon);
        click(wiFiIcon);

        isWifiContainerCorrect(true);

        String wifiPasswordText = wiFiPassword.getText().replaceAll("Пароль: ", "");
        String wifiNameText = wiFiName.getText().replaceAll("Сеть: ", "");

        Assertions.assertEquals(wifiPassword, wifiPasswordText,
                "Пароль wifi не совпадет с установленным в админке");

        Assertions.assertEquals(wifiName, wifiNameText, "Имя wifi совпадет с установленным в админке");

        click(wiFiPassword);
        wiFiPassword.shouldHave(text("Скопировано"));

        clipboard().shouldHave(content(wifiPasswordText),Duration.ofSeconds(5));

        click(wiFiCloseButton);
        isElementInvisible(wiFiContainer);

    }

    @Step("Проверка вайфая на столе")
    public void checkWiFiOnTapperTableWithoutPassword(String wifiName) {

        isElementVisibleAndClickable(wiFiIcon);
        click(wiFiIcon);

        isWifiContainerCorrect(false);

        String wifiNameText = wiFiName.getText().replaceAll("Сеть: ", "");

        wiFiPassword.shouldBe(hidden);

        Assertions.assertEquals(wifiName, wifiNameText, "Имя wifi совпадет с установленным в админке");

    }

    @Step("Проверка корректности вайфай контейнера")
    public void isWifiContainerCorrect(boolean hasPassword) {

        isElementVisible(wiFiContainer);
        isElementVisible(wiFiHeader);
        isElementVisible(wiFiCloseButton);
        isElementVisible(wiFiName);

        if(hasPassword)
            isElementVisible(wiFiPassword);

    }

    public void forceWaitingForSocketChangePositions(int ms) {

        forceWait(ms);

    }

}

