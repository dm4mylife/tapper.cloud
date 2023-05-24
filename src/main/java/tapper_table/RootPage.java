package tapper_table;

import api.ApiIiko;
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
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.ClipboardConditions.content;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.clipboard;
import static data.Constants.RegexPattern.TapperTable.*;
import static data.Constants.RegexPattern.TelegramMessage.discountRegex;
import static data.Constants.RegexPattern.TelegramMessage.tableRegexTelegramMessage;
import static data.Constants.TestData.TapperTable;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.TestData.Yandex;
import static data.selectors.TapperTable.Common.*;
import static data.selectors.TapperTable.ReviewPage.reviewContainer;
import static data.selectors.TapperTable.RootPage.*;
import static data.selectors.TapperTable.RootPage.DishList.*;
import static data.selectors.TapperTable.RootPage.Menu.*;
import static data.selectors.TapperTable.RootPage.PayBlock.*;
import static data.selectors.TapperTable.RootPage.TapBar.*;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.*;





public class RootPage extends BaseActions {

    ApiRKeeper apiRKeeper = new ApiRKeeper();
    ApiIiko apiIiko = new ApiIiko();
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

    }

    @Step("Обновляем текущую страницу")
    public void refreshPage() {

        Selenide.refresh();

    }

    public void ignoreWifiIcon(){

        if (wiFiIcon.exists())
            Selenide.executeJavaScript("document.querySelector('.appHeader__wifi').style.display = \"none\";");

    }

    @Step("Обновляем текущую страницу")
    public void refreshTableWithOrder() {

        Selenide.refresh();
        isTableHasOrder();

    }

    @Step("Сбор всех блюд со страницы таппера и проверка с блюдами на кассе")
    public void matchTapperOrderWithOrderInKeeper(HashMap<Integer, Map<String, Double>> allDishesInfoFromKeeper) {

        LinkedHashMap<Integer, Map<String, Double>> tapperDishes = new LinkedHashMap<>();

        int i = 0;

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = element.$(dishNameSelector).getText();
            double price = convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);

            temporaryMap.put(name, price);
            tapperDishes.put(i, temporaryMap);

            i++;

        }

        System.out.println("\nTAPPER\n" + tapperDishes + "\nDESK\n " + allDishesInfoFromKeeper );


        if (!allDishesInfoFromKeeper.equals(tapperDishes)) {

            allDishesInfoFromKeeper.entrySet().stream()
                    .filter(entry -> !tapperDishes.containsValue(entry.getValue()))
                    .forEach(entry -> {

                        System.out.println(entry.getKey() + " - " + entry.getValue() +
                                " это не совпадает со столом.На кассе другая цена");
                        Assertions.fail(entry.getValue() + " это не совпадает со столом.На кассе другая цена");

                    });

            tapperDishes.entrySet().stream()
                    .filter(entry -> !allDishesInfoFromKeeper.containsValue(entry.getValue()))
                    .forEach(entry -> {

                        System.out.println(entry.getKey() + " - " + entry.getValue() +
                                " это не совпадает с кассой.На столе другая цена");
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

        if (divideCheckSlider.isDisplayed())
            click(divideCheckSlider);

        divideCheckSliderActive.shouldBe(appear);

    }

    @Step("Отключаем разделение счёта")
    public void deactivateDivideCheckSliderIfActivated() {

        if (discountSum.isDisplayed()) {

            isElementInvisible(divideCheckSlider);

        }

        isElementVisibleDuringLongTime(divideCheckSliderActive, 5);
        click(divideCheckSliderActive);

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

        startScreenLogoContainer.shouldBe(hidden,Duration.ofSeconds(20));
        isElementVisibleDuringLongTime(orderContainer, 60);
        isElementsCollectionVisible(allDishesInOrder);
        reviewContainer.shouldBe(hidden);

    }

    public void skipStartScreenLogo() {

        startScreenLogoContainer.shouldBe(hidden,Duration.ofSeconds(20));

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
                isElementVisible(tipsInfo);
                checkCustomTipForError();

            }




        }

    }

    @Step("Ввод кастомных чаевых")
    public void setCustomTips(String value) {

        totalTipsSumInMiddle.clear();

        click(totalTipsSumInMiddle);
        totalTipsSumInMiddle.clear();
        sendHumanKeys(totalTipsSumInMiddle, value);
        totalTipsSumInMiddle.shouldHave(value(value));

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

        if (serviceChargeContainer.exists() && serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            click(serviceChargeCheckboxButton);
            disableScPopUp();

        }


    }

    public void disableScPopUp() {

        if (serviceChargePopUp.isDisplayed()) {

            click(serviceChargePopUpDisableButton);
            isElementInvisible(serviceChargePopUp);
            serviceChargeCheckboxSvg.getCssValue("display").equals("none");

        }

    }

    @Step("Проверка что у блюд с модификатором есть подписи в позиции")
    public void isModificatorTextCorrect() {

        allDishesInOrder.asFixedIterable().stream().forEach
                (element -> element.$(dishModificatorName).shouldBe(visible).shouldNotBe(empty));

    }

    @Step("Прощёлкиваем все опции чаевых и проверяем что чаевые формируются корректно от общей суммы")
    public void checkAllTipsOptions(double totalCleanSumForDishes) {

        showPaymentOptionsAndTapBar();
        StringBuilder logs = new StringBuilder();
        double cleanTips = 0;

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            activateServiceChargeIfDeactivated();

            click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", ".*active.*"));

            double serviceChargeSum = getCurrentSCSum();
            int percent = convertSelectorTextIntoIntByRgx(tipsOption, "\\D+");
            double paySumInCheck = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

            isTipsInMiddleMatchTipsInCheck();

            double tipsSumInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, tipsInCheckSumRegex);
            int totalTipsSumInMiddle = Integer.parseInt
                    (Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));

            double dishesPriceWithoudDiscount = divideCheckSliderActive.isDisplayed() ?
                    countOnlyAllChosenWithFullPriceDishesDivided() :
                    countAllNonPaidDiscountDishesInOrder();

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;
                cleanTips = dishesPriceWithoudDiscount * percentForCalculation;
                cleanTips = Math.round(cleanTips);

            }

            double totalSumPlusCleanTips = totalCleanSumForDishes + cleanTips;
            double totalSumPlusCleanTipsPlusServiceChargeSum = totalCleanSumForDishes + cleanTips + serviceChargeSum;

            logs
                    .append("\nЧаевые - " + percent + "\n")
                    .append(serviceChargeSum + " чистый сервисный сбор по текущей проценту чаевых: " + percent + "\n")
                    .append(cleanTips + " чистые чаевые по общей сумме за блюда\n")
                    .append(totalCleanSumForDishes + " чистая сумма за блюда без чаевых и без сервисного сбора\n")
                    .append(totalSumPlusCleanTips + " чистая сумма за блюда c чаевым и без сервисного сбора\n")
                    .append(totalSumPlusCleanTipsPlusServiceChargeSum + " чистая сумма за блюда c чаевым и сервисным сбором\n")
                    .append(paySumInCheck + " сумма в поле 'Итого к оплате' c учетом чаевых и сервисного сбора\n");


            Assertions.assertEquals(totalTipsSumInMiddle, cleanTips, 0.1,
                    "Общая сумма чаевых по центру " + totalTipsSumInMiddle +
                            " не совпала с суммой чистых чаевых " + cleanTips);

            Assertions.assertEquals(tipsSumInCheck, cleanTips, 0.1,
                    "Чаевые в 'Чаевые'" + tipsSumInCheck +
                            " не совпали с суммой чистых чаевых " + cleanTips);

            Assertions.assertEquals(totalSumPlusCleanTipsPlusServiceChargeSum, paySumInCheck, 0.1,
                    "Чистая сумма за блюда + чистые чаевые + СБ " + totalSumPlusCleanTipsPlusServiceChargeSum
                            + " равна сумме в 'Итого к оплате' " + paySumInCheck);

            click(resetTipsButton);

        }

        printAndAttachAllureLogs(logs, "Подсчёт сумм");

    }

    @Step("Проверка что чаевые по центру совпадают с полем 'Чаевые'")
    public void isTipsInMiddleMatchTipsInCheck() {

        double tipsSumInCheck = convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, tipsInCheckSumRegex);
        double totalTipsSumInMiddle = Double.parseDouble(TipsAndCheck.totalTipsSumInMiddle.getValue());

        Assertions.assertEquals(tipsSumInCheck,totalTipsSumInMiddle);

    }

    @Step("Проверяем что разные проценты чаевых рассчитываются корректно во всех полях и в суммах вместе с СБ не разделяя счёт")
    public void isAllTipsOptionsAreCorrectWithTotalSumAndSC(double totalSum) {

        checkAllTipsOptions(totalSum);

    }

    @Step("Проверка что логика установленных чаевых по умолчанию от общей суммы корректна")
    public void isDefaultTipsBySumLogicCorrect() {

        scrollTillBottom();
        double totalDishSum = getClearOrderAmount();

        System.out.println(totalDishSum + " totalDishSum");

        if (discountField.exists())
            totalDishSum += convertSelectorTextIntoDoubleByRgx(discountSum,discountInCheckRegex);

        System.out.println(totalDishSum + " totalDishSum");

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

        isElementsCollectionVisible(allNonPaidAndNonDisabledDishes);

        for (int index = 0; index < allNonPaidAndNonDisabledDishes.size(); index++) {

            if (!allNonPaidAndNonDisabledDishesCheckbox.get(index).getCssValue("display").equals("block")) {

                double currentDishPrice = convertSelectorTextIntoDoubleByRgx
                        (allNonPaidAndNonDisabledDishesSum.get(index), dishPriceRegex);
                String currentDishName = allNonPaidAndNonDisabledDishesName.get(index).getText();

                totalDishesSum += currentDishPrice;

                scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
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

    @Step("Выбираем первое")
    public void choseFirstDish(ElementsCollection elements) {

        scrollAndClick(elements.first());

    }

    @Step("Отменяем определенное количество выбранных позиций")
    public void cancelCertainAmountChosenDishes(int count) { //

        hidePaymentOptionsAndTapBar();

        for (int index = 0; index < allNonPaidAndNonDisabledDishes.size(); index++) {

            if (count != 0) {

                if (allNonPaidAndNonDisabledDishesCheckbox.get(index).getAttribute("style").equals("")) {

                    scrollAndClick(allNonPaidAndNonDisabledDishesName.get(index));
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

                allNonPaidAndNonDisabledDishesCheckbox.get(index)
                        .shouldBe(cssValue("display", "block"));

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
                    convertSelectorTextIntoDoubleByRgx(element.$(dishPriceWithoutDiscountSelector), dishPriceRegex);
            String currentDishName = element.$(dishNameSelector).getText();

            totalDishesSum += currentDishPrice;
            totalDishesSum = updateDoubleByDecimalFormat(totalDishesSum);

            logs.append("Блюдо - ").append(currentDishName).append(" - ").append(currentDishPrice)
                .append(". Общая цена ").append(totalDishesSum);

        }

        printAndAttachAllureLogs(logs, "Список не оплаченных и не заблокированных блюд");

        return totalDishesSum;

    }

    @Step("Выбираем последнее блюдо")
    public void chooseLastDish(ElementsCollection elements) {

        scrollAndClick(elements.last().$(dishNameSelector));

    }

    @Step("Выбираем первое блюдо")
    public void chooseFirstDish(ElementsCollection elements) {

        scrollAndClick(elements.first().$(dishNameSelector));

    }

    @Step("Считаем сумму всех выбранных позиций в заказе при разделении") //
    public double countOnlyAllChosenDishesDivided() {

        double totalSumInOrder = 0;
        int counter = 0;

        isElementsCollectionVisible(allNonPaidAndNonDisabledDishes);

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            if (element.$(dishCheckboxSelector).getCssValue("display").equals("block")) {

                double cleanPrice =
                        convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);
                String dishName = element.$(dishNameSelector).getText();

                totalSumInOrder += cleanPrice;
                totalSumInOrder = updateDoubleByDecimalFormat(totalSumInOrder);
                counter++;

            }

        }

        return totalSumInOrder;

    }

    @Step("Считаем сумму всех выбранных позиций в заказе при разделении") //
    public double countOnlyAllChosenWithFullPriceDishesDivided() {

        double totalSumInOrder = 0;
        int counter = 0;

        isElementsCollectionVisible(allNonPaidAndNonDisabledDishes);

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            if (element.$(dishCheckboxSelector).getCssValue("display").equals("block")) {

                double cleanPrice =
                        convertSelectorTextIntoDoubleByRgx(element.$(dishPriceWithoutDiscountSelector), dishPriceRegex);
                String dishName = element.$(dishNameSelector).getText();

                totalSumInOrder += cleanPrice;
                totalSumInOrder = updateDoubleByDecimalFormat(totalSumInOrder);
                counter++;

            }

        }

        return totalSumInOrder;

    }

    @Step("Проверка что сумма за все блюда совпадает с суммой в 'Выбранные позици'")
    public void isMarkedSumCorrect(double totalClearDishSum) {

        double markedDishesSum = convertSelectorTextIntoDoubleByRgx(TipsAndCheck.markedDishesSum, markedDishesRegex);

        Assertions.assertEquals(markedDishesSum, totalClearDishSum, 0.1);

    }

    @Step("Проверка что сумма за все блюда совпадает с суммой в 'Общий счет'")
    public void isTotalCheckSumCorrect(double totalClearDishSum) {

        double totalCheckSum = convertSelectorTextIntoDoubleByRgx(totalOrderPay, markedDishesRegex);

        Assertions.assertEquals(totalCheckSum, totalClearDishSum, 0.1);

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

    @Step("Считаем сумму не оплаченных позиций и заблокированных в заказе при разделении счета")
    public double countAllNonPaidAndDisabledDishesInOrderDivide() {

        double totalSumInOrder = 0;
        int counter = 0;

        for (SelenideElement element : allNonPaidAndDisabledDishes) {

            boolean isNullDishPrice =
                    convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex) != 0;

            String priceSelector = discountField.isDisplayed() && isNullDishPrice ?
                    dishPriceWithDiscountSelector : dishPriceTotalSelector;

            double cleanPrice = convertSelectorTextIntoDoubleByRgx(element.$(priceSelector), dishPriceRegex);
            String dishName = element.$(dishNameSelector).getText();

            totalSumInOrder += cleanPrice;
            totalSumInOrder = updateDoubleByDecimalFormat(totalSumInOrder);
            counter++;


        }

        return totalSumInOrder;

    }

    @Step("Считаем сумму не оплаченных,незаблокированных позиций в заказе")
    public double countAllNonPaidDishesInOrder() {

        double totalSumInOrder = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        isElementsCollectionVisible(allNonPaidAndNonDisabledDishes);

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            double cleanPrice = convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);
            String dishName = element.$(dishNameSelector).getText();

            totalSumInOrder += cleanPrice;
            totalSumInOrder = updateDoubleByDecimalFormat(totalSumInOrder);
            counter++;

            logs.append("\n" + counter + ". " + dishName + " - " + cleanPrice + ". Общая сумма: " + totalSumInOrder);

        }

        printAndAttachAllureLogs(logs, "Список не оплаченных и не заблокированных блюд");

        return totalSumInOrder;

    }

    @Step("Считаем сумму не оплаченных,незаблокированных позиций в заказе со скидкой")
    public double countAllNonPaidDiscountDishesInOrder() {

        double totalSumInOrder = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        isElementsCollectionVisible(allNonPaidAndNonDisabledDishes);

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            double cleanPrice = convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);
            String dishName = element.$(dishNameSelector).getText();

            totalSumInOrder += cleanPrice;
            totalSumInOrder = updateDoubleByDecimalFormat(totalSumInOrder);
            counter++;

            logs.append("\n" + counter + ". " + dishName + " - " + cleanPrice + ". Общая сумма: " + totalSumInOrder);

        }

        printAndAttachAllureLogs(logs, "Список не оплаченных и не заблокированных блюд");

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

        final int[] i = {0};

        isElementsCollectionVisible(allDishesInOrder);

        allDishesInOrder.asDynamicIterable().stream().forEach(element -> {

            if (element.$(dishCheckboxSelector).getAttribute("style").equals("")) {

                Map<String, Double> temporaryMap = new HashMap<>();

                String name = element.$(dishNameSelector).getText();
                double price = convertSelectorTextIntoDoubleByRgx(element.$(dishPriceTotalSelector), dishPriceRegex);

                temporaryMap.put(name, price);
                tapperDishes.put(i[0], temporaryMap);

                i[0]++;

            }

        });

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
    public void isTotalSumInDishesMatchWithTotalPay(double totalSumByDishesInOrder, double discount) {

        isElementVisible(totalPay);

        double totalPaySumInCheck = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex) - discount;

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
                (element -> element.$(dishPriceWithDiscountSelector).should(disappear));

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

        isElementInvisible(tipsContainer);
        isImageCorrect(waiterImageNotSelenide, "Изображение официанта не корректное или битое");
        isElementVisible(tipsWaiter);

        if (roleButtons.size() != 0) {

            isElementVisible(tipsInCheckSum);

        } else {

            isElementInvisible(tipsInCheckSum);
        }

        isElementInvisible(totalTipsSumInMiddle);
        isElementInvisible(activeTipsButton);
        isElementInvisible(resetTipsButton);
        tipsListItem.shouldHave(CollectionCondition.size(0));

    }

    @Step("Чаевые не должны отображаться у не верифицированного официанта без привязанной карты")
    public void checkIsNoTipsElementsIfNonVerifiedNonCard() {

        if (hookahServerButton.exists() || kitchenButton.exists()) {

            isElementInvisible(waiterRoleButton);

        } else {

            isElementInvisible(tipsContainer);

        }

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

        return discountField.exists() ? convertSelectorTextIntoDoubleByRgx(discountSum,discountRegex) : 0;

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

            double totalDiscount = 0;
            if (discountField.isDisplayed())
                totalDiscount = convertSelectorTextIntoDoubleByRgx(discountSum, discountInCheckRegex);

            double totalPaySum = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex) + totalDiscount;
            double totalSumWithTips = totalSumWithoutTips + Math.round(totalPaySum * (tipPercent / 100)) + totalDiscount;

            Assertions.assertEquals(totalSumWithTips, totalPaySum, 0.1,
                    "Процент чаевых установленный по умолчанию не совпадает с общей ценой за все блюда без СБ");

        }

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

        isImageCorrect(shareButtonSvgNotSelenide, "Иконка в кнопке поделиться счётом не корректная");

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

        if (divideCheckSliderActive.isDisplayed()) {

            dishes.asFixedIterable().stream().forEach
                    (element -> element.$(dishOrderStatusSelector).shouldHave(text("Не оплачено")));

        } else {

            dishes.asFixedIterable().stream().forEach
                    (element -> element.$(dishOrderStatusSelector).shouldBe(hidden));

        }

        LinkedHashMap<String,Double> currentTableData = getTableData();
        LinkedHashMap<Integer, Map<String, Double>> currentDishList = getDishList(dishes);

        Assertions.assertEquals(previousTableData,currentTableData,"Данные не совпадают после ошибки с оплатой");
        Assertions.assertEquals(previousDishList,currentDishList,"Заказ не совпадает");

    }

    public void isPaymentDisabled() {

        if (divideCheckSlider.isDisplayed()) {

            allDishesInOrder.asFixedIterable().stream().forEach(element -> {

                if (!element.$(dishPriceTotalSelector).getText().equals("0 ₽"))
                    element.shouldHave(or("Должны быть оплачены или нет",
                            matchText(DISH_STATUS_IS_PAYING),matchText(DISH_STATUS_PAYED)));

            });

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

        isPositiveAndNegativeBankSearchCorrect();

    }

    @Step("Проверка поиска банка и негативный поиск")
    public void isPositiveAndNegativeBankSearchCorrect() {

        click(paymentButton);

        click(paymentBanksAllBanksButton);

        sendKeys(searchBankInput,SEARCH_BANK);

        bankListItems.shouldHave(size(1));
        bankListItems.first().shouldHave(text(SEARCH_BANK));

        click(searchBankClearButton);

        sendKeys(searchBankInput,WRONG_SEARCH_BANK);

        bankListItems.shouldHave(size(0));

        emptyBankListItems.shouldBe(visible);

        click(choseBankCloseModal);
        isCancelPaymentcontainerCorrect();
        click(cancelProcessPayingContainerSaveBtn);
        cancelProcessPayingContainer.shouldBe(disappear);

    }



    @Step("Сохранение общей суммы в таппере для проверки суммы в б2п")
    public double saveTotalPayForMatchWithAcquiring() {

        return convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

    }

    @Step("Получаем чистую сумму за вычетом всех сб,чаевых,скидки,наценки,оплаченных ранее")
    public double getClearOrderAmount() {

        double orderAmount = divideCheckSliderActive.isDisplayed() ?
                countAllNonPaidDAndChosenDishesInOrder() : countAllNonPaidDishesInOrder() ;

        return updateDoubleByDecimalFormat(orderAmount);

    }

    @Step("Получаем чистую сумму за вычетом всех сб,чаевых,скидки,наценки,оплаченных ранее")
    public double getOrderAmountForOperationHistory() {

        double serviceChargeSum = 0;

        double tips = totalTipsSumInMiddle.exists() ?
                convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, dishPriceRegex)  : 0;

        if (serviceChargeCheckboxSvg.getCssValue("display").equals("block")) {

            serviceChargeSum = convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, serviceChargeRegex);
            serviceChargeSum = updateDoubleByDecimalFormat(serviceChargeSum);

        }

        double orderAmount = convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

        orderAmount = orderAmount - tips - serviceChargeSum;
        orderAmount = updateDoubleByDecimalFormat(orderAmount);

        return orderAmount;

    }

    @Step("Сохранение общей суммы, чаевых, СБ для проверки с транзакцией б2п")
    public HashMap<String, String> savePaymentDataTapperForB2b() {

        HashMap<String, String> paymentData = new HashMap<>();

        String fee = "0";

        double orderAmountDouble = getOrderAmountForOperationHistory();

        String orderAmountString = String.valueOf(orderAmountDouble).matches("\\d{1,}\\.\\d{1}$") ?
                orderAmountDouble + "0" :
                String.valueOf(orderAmountDouble);

        String orderAmount = orderAmountString.replaceAll("\\.","");
        paymentData.put("order_amount", orderAmount);

        String tips = totalTipsSumInMiddle.exists() && !totalTipsSumInMiddle.getValue().equals("0") ?
                totalTipsSumInMiddle.getValue() + "00" : "0";

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
        isElementsCollectionVisible(callWaiterSecondMessage);

    }

    @Step("Повторное открытие формы вызовы официанта для проверки что история сохранилась")
    public void isHistorySaved() {

        isElementVisible(callWaiterGuestTestComment);
        isElementsCollectionVisible(callWaiterSecondMessage);
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

                dishList.put("name", dishName);
                dishList.put("price", dishPrice);

            }

            menuData.put(categoryName, dishList);

        }

        return menuData;

    }

    @Step("Сохранения данных для проверки истории операции в админке")
    public HashMap<Integer, HashMap<String, String>> saveOrderDataForOperationsHistoryInAdmin() {

        HashMap<Integer, HashMap<String, String>> orderData = new HashMap<>();
        HashMap<String, String> temporaryHashMap = new HashMap<>();

        String table = convertSelectorTextIntoStrByRgx(tableNumber, "Стол ");
        String name = waiterName.getText();
        String tips = convertSelectorTextIntoStrByRgx(tipsInCheckSum, tipsInCheckSumRegex);

        if (Objects.equals(tips, ""))
            tips = "0";

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



        System.out.println("\nTAPPER\n" + tapperOrderData + "\nADMIN\n" + adminOrderData);

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

        String paymentType = null;

        final String[] msg = new String[1];

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(25, TimeUnit.MINUTES).timeout(Duration.ofSeconds(25)).untilAsserted(
                        () -> Assertions.assertNotNull(msg[0] = telegram.getLastTgPayMsg(guid,paymentType)));

        HashMap<String, String> msgWithType = telegram.setMsgTypeFlag(msg[0]);
        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

        return parsedMsg;

    }

    @Step("Получение и парсинг сообщения отзыва с рейтингом в телеграмм")
    public LinkedHashMap<String, String> getReviewTgMsgData(String guid, String reviewType) {

        final String[] msg = new String[1];

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(
                        () -> Assertions.assertNotNull(msg[0] = telegram.getLastTgWaiterMsg(guid,reviewType)));

        Allure.addAttachment("telegram message", String.valueOf(msg[0]));

        HashMap<String, String> msgWithType = telegram.setMsgTypeFlag(msg[0]);

        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

        return parsedMsg;

    }

    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public LinkedHashMap<String, String> getPaymentTgMsgData(String guid,String payment) {

        final String[] msg = new String[1];

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(15, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(
                        () -> Assertions.assertNotNull(msg[0] = telegram.getLastTgPayMsg(guid, payment),
                                "Сообщение в телеграм не пришло или некорректное"));

        HashMap<String, String> msgWithType = telegram.setMsgTypeFlag(msg[0]);
        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

        return parsedMsg;

    }

    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public String getPaymentErrorTgMsgData(String errorPaymentText) {

        final String[] msg = new String[1];

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(15, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(
                        () -> Assertions.assertNotNull(msg[0] = telegram.getLastTgErrorPayMsg(errorPaymentText),
                                "Сообщение в телеграм не пришло или некорректное"));

        return msg[0];

    }

    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public LinkedHashMap<String, String> getCallWaiterTgMsgData(String tableNumber, String waiterName,
                                                                String messageText) {

        final String[] msg = new String[1];

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(
                        () -> Assertions.assertNotNull
                                (msg[0] = telegram.getLastTgCallWaiterMsgList(tableNumber,waiterName,messageText)));

        HashMap<String, String> msgWithType = telegram.setMsgTypeFlag(msg[0]);
        LinkedHashMap<String, String> parsedMsg = telegram.parseMsg(msgWithType);

        return parsedMsg;

    }

    @Step("Получение сообщения тг, парсинг, преобразование в хешкарту")
    public void getAdminSendingMsgData(String tableId, String textInSending, int waitingTime) {

        String paymentType = null;

        final String[] msg = new String[1];

        Awaitility.await().pollInterval(1, TimeUnit.SECONDS)
                .atMost(20, TimeUnit.MINUTES).timeout(Duration.ofSeconds(20)).untilAsserted(
                        () -> Assertions.assertNotNull
                                (msg[0] = telegram.getLastTgPayMsg(textInSending, paymentType)));

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

    @Step("Проверка что статус блюда отображён и соответствует ранему количеству блюд")
    public void isDishStatusChanged(ElementsCollection elements, int collectionSize) {

        elements.should(allMatch("Все элементы должны быть видны", WebElement::isDisplayed),
                        Duration.ofSeconds(10)).shouldHave(size(collectionSize));

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
    public LinkedHashMap<String, String> getTapperDataForTgPaymentMsg(String tableId, String cashDeskType) {

        String payStatus;
        String orderStatus;
        boolean hasMarkUp = false;
        double serviceChargeSumDouble = 0;
        LinkedHashMap<String, String> tapperDataForTgMsg = new LinkedHashMap<>();

        double sumInCheckDouble = countAllDishes();

        double paySumDouble = convertSelectorTextIntoDoubleByRgx(totalPay,totalPayRegex)
                - getCurrentTipsSum() - getCurrentSCSum();
        paySumDouble = updateDoubleByDecimalFormat(paySumDouble);

        double discountDouble = 0;
        double markUpDouble = 0;

        if (discountField.isDisplayed()) {

            discountDouble = convertSelectorTextIntoDoubleByRgx(discountSum, discountInCheckRegex);

        } else if (markupSum.isDisplayed()) {

            discountDouble -= convertSelectorTextIntoDoubleByRgx(markupSum, discountInCheckRegex);
            markUpDouble = convertSelectorTextIntoDoubleByRgx(markupSum, discountInCheckRegex);
            hasMarkUp = true;

        }

        double restToPayDouble = divideCheckSliderActive.isDisplayed() ?
                countAllNonPaidAndDisabledDishesInOrderDivide() - countOnlyAllChosenDishesDivided() :
                getClearOrderAmount() - getCurrentSCSum() - getCurrentTipsSum();
        restToPayDouble = updateDoubleByDecimalFormat(restToPayDouble);

        double tipsDouble = getTipsFromTapper();

        if (serviceChargeCheckboxSvg.getCssValue("display").equals("none")) {

            serviceChargeSumDouble = tipsDouble / 100 * Constants.SERVICE_CHARGE_PERCENT_WHEN_DEACTIVATED;

            BigDecimal bd = new BigDecimal(Double.toString(serviceChargeSumDouble));
            BigDecimal serviceChargeSum = bd.setScale(2, RoundingMode.HALF_UP);

            tipsDouble -= Double.parseDouble(String.valueOf(serviceChargeSum));

        }

        Response rsGetOrder = null;
        double unpaidSum = 0;
        double prepayedSum = 0;

        double totalPaidDouble = countAllNonPaidDishesInOrder();

        if (cashDeskType.equals("keeper")) {

            rsGetOrder = apiRKeeper.getOrderInfo(tableId, AUTO_API_URI);

            if (rsGetOrder.path("result.CommandResult.Order[\"@attributes\"].prepaySum") == null) {

                totalPaidDouble = paySumDouble;


            } else {

                unpaidSum = rsGetOrder.jsonPath().getDouble
                     ("result.CommandResult.Order[\"@attributes\"].unpaidSum") / 100;

                prepayedSum = rsGetOrder.jsonPath()
                    .getDouble("result.CommandResult.Order[\"@attributes\"].prepaySum") / 100;

                totalPaidDouble = paySumDouble + prepayedSum;

            }

        } else if (cashDeskType.equals("iiko")) {

            rsGetOrder = apiIiko.getOrderInfo(tableId);
            prepayedSum = apiIiko.getPaidSum(rsGetOrder);
            unpaidSum = apiIiko.getUnpaidSum(rsGetOrder);

            totalPaidDouble = paySumDouble + prepayedSum;

        }

        totalPaidDouble = updateDoubleByDecimalFormat(totalPaidDouble);

        if (totalPaidDouble == sumInCheckDouble || (totalPaidDouble + discountDouble) == sumInCheckDouble) {

            payStatus = "Полностью оплачено";
            orderStatus = "Успешно закрыт на кассе";

            restToPayDouble = sumInCheckDouble - (totalPaidDouble + discountDouble);
            restToPayDouble = updateDoubleByDecimalFormat(restToPayDouble);

        } else {

            payStatus = "Частично оплачено";
            orderStatus = "Предоплата успешно прошла по кассе";

        }

        String tableString = tableNumber.getText().replaceAll(tableRegexTelegramMessage, "$2");
        String sumInCheck = convDoubleWithDecimal(sumInCheckDouble);
        String paySum = convDoubleWithDecimal(paySumDouble);
        String totalPaid = convDoubleWithDecimal(totalPaidDouble);
        String restToPay = convDoubleWithDecimal(restToPayDouble);
        String tips = convDoubleWithDecimal(tipsDouble);

        String discount = "";
        String markUp = "";
        
        if (!hasMarkUp) {

            discount = discountDouble != 0 ? convDoubleWithDecimal(discountDouble) : "";
            
        } else {

            markUp = markUpDouble != 0 ? convDoubleWithDecimal(markUpDouble) : "";
            
        }

        String waiter = getWaiterNameFromTapper();
        String restaurantName = getRestaurantNameFromTapper(cashDeskType);

       /* System.out.println("\nbefore status\n");
        System.out.println(totalPaidDouble + " totalPaidDouble");
        System.out.println((totalPaidDouble + discountDouble) + " totalPaidDouble with discount");
        System.out.println(sumInCheckDouble + " sumInCheckDouble");
        System.out.println(restToPayDouble + " restToPayDouble");
        System.out.println(tips + " tips");
        System.out.println(waiter + " waiter");
        System.out.println(restaurantName + " restaurantName");*/

        tapperDataForTgMsg = setCollectionData(restaurantName,tableString,sumInCheck,restToPay,tips,paySum,totalPaid,
                discount,markUp,payStatus,orderStatus,waiter);

        return tapperDataForTgMsg;

    }

    public String getWaiterNameFromTapper() {

        return waiterRoleButton.exists() || waiterHeading.exists() || tipsInfo.exists() ?
                waiterName.getText() : UNKNOWN_WAITER;

    }

    public String getRestaurantNameFromTapper(String cashDeskType) {

        return cashDeskType.equals("keeper") ? "'testrkeeper'" : "'office'" ;

    }

    public double getTipsFromTapper() {

        return totalTipsSumInMiddle.isDisplayed() ? Double.parseDouble(totalTipsSumInMiddle.getValue()) : 0;

    }


    public LinkedHashMap<String, String> setCollectionData (String restaurantName, String table, String sumInCheck,
                                                            String restToPay, String tips, String paySum,
                                                            String totalPaid, String discount, String markUp,
                                                            String payStatus, String orderStatus,String waiter) {

        LinkedHashMap<String, String> tapperDataForTgMsg = new LinkedHashMap<>();

        tapperDataForTgMsg.put("restaurantName", restaurantName);
        tapperDataForTgMsg.put("table", table);
        tapperDataForTgMsg.put("sumInCheck", sumInCheck);
        tapperDataForTgMsg.put("restToPay", restToPay);
        tapperDataForTgMsg.put("tips", tips);
        tapperDataForTgMsg.put("paySum", paySum);
        tapperDataForTgMsg.put("totalPaid", totalPaid);

        if (!discount.equals(""))
            tapperDataForTgMsg.put("discount", discount);

        if (!markUp.equals(""))
            tapperDataForTgMsg.put("markUp", markUp);

        //if (!tipsContainer.exists())
       //     tapperDataForTgMsg.put("markUp", markUp);

        tapperDataForTgMsg.put("payStatus", payStatus);
        tapperDataForTgMsg.put("orderStatus", orderStatus);
        tapperDataForTgMsg.put("waiter", waiter);

        return tapperDataForTgMsg;

    }

    public String convDoubleWithDecimal(double doubleForCheck) {

         return doubleForCheck % 1.0 > 0 ?
                String.valueOf(doubleForCheck) :
                String.valueOf(doubleForCheck).replaceAll("\\.\\d{1,3}", "");

    }

    public Map<Integer,Map<String,Double>>
    saveDishPricesInCollection(ElementsCollection dishes, String priceWithDiscountSelector,
                               String priceWithoutDiscountSelector, String priceRegex) {

        Map<Integer,Map<String,Double>> data = new HashMap<>();
        Map<String,Double> dish = new HashMap<>();

        for (int index = 0; index < dishes.size(); index++) {

            double totalPrice = convertSelectorTextIntoDoubleByRgx
                    (dishes.get(index).$(priceWithoutDiscountSelector),priceRegex);

            double discountPrice = divideCheckSliderActive.isDisplayed() ?
                    convertSelectorTextIntoDoubleByRgx(dishes.get(index).$(priceWithDiscountSelector),priceRegex) :
                    totalPrice;

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
                "Сообщение в телеграмме не корректное\n TELEGRAM DATA\n" + telegramDataForTgMsg +
                        "\nTAPPER DATA\n" + tapperDataForTgMsg);

    }

    @Step("Получаем скидку")
    public double getDiscount(String tableId) {

        Response rsGetOrder = apiRKeeper.getOrderInfo(tableId, AUTO_API_URI);
        return Math.abs(rsGetOrder.jsonPath()
                .getDouble("result.CommandResult.Order[\"@attributes\"].discountSum") / 100);

    }

    @Step("Получаем скидку")
    public double getDiscountFromTable() {

       return convertSelectorTextIntoDoubleByRgx(discountSum,discountInCheckRegex);

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

}


