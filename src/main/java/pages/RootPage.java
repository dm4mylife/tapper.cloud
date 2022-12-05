package pages;

import com.codeborne.selenide.*;
import common.BaseActions;
import io.qameta.allure.Allure;
import io.qameta.allure.Flaky;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Cookie;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static constants.Constant.JSScripts.*;
import static constants.Constant.TestData.*;
import static constants.Constant.TestDataRKeeperAdmin.*;
import static constants.Selectors.Common.*;
import static constants.Selectors.RKeeperAdmin.*;
import static constants.Selectors.RootPage.*;
import static constants.Selectors.RootPage.DishList.*;
import static constants.Selectors.RootPage.PayBlock.*;
import static constants.Selectors.RootPage.TapBar.*;
import static constants.Selectors.RootPage.TipsAndCheck.*;

public class RootPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    @Step("Сбор всех блюд со страницы таппера и проверка с блюдами на кассе")
    public void matchTapperOrderWithOrderInKeeper(HashMap<Integer, Map<String, Double>> allDishesInfoFromKeeper) {

        HashMap<Integer, Map<String, Double>> tapperDishes = new HashMap<>();

        int i = 0;

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = baseActions.getSelectorText(element.$(".dish__checkbox-text"));
            double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");

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
    public void openTapperLink(String url) {

        baseActions.openPage(url);
        baseActions.forceWait(TIME_WAIT_FOR_FULL_LOAD);
    }

    @Step("Проверки что стол освободился, статусы в заголовке корректные")
    public void isEmptyOrderAfterClosing() {

        baseActions.isElementVisible(emptyOrderHeading);
        emptyOrderHeading.shouldHave(text("Заказ успешно оплачен"), Duration.ofSeconds(5));
        System.out.println("Заказ успешно оплачен");
        emptyOrderHeading.shouldHave(text("Скоро здесь появится ваш заказ"), Duration.ofSeconds(10));
        System.out.println("Скоро здесь появится ваш заказ");

        double totalSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");
        Assertions.assertEquals(totalSumInWallet, 0, "Сумма в кошельке не равно 0");

    }

    @Step("Надпись что заказ успешно оплачен по центру страницы")
    public void isEmptyOrder() { // toDO убрал проверку на "ваш заказ успешно оплачен" так как есть баг

        baseActions.isElementVisible(emptyOrderHeading);
        emptyOrderHeading.shouldHave(text("Скоро здесь появится ваш заказ"), Duration.ofSeconds(10));
        System.out.println("Скоро здесь появится ваш заказ");

        double totalSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");
        Assertions.assertEquals(totalSumInWallet, 0, "Сумма в кошельке не равно 0");

    }

    @Step("Отображается пустой заказ")
    public void isTableEmpty() {
        dishListContainerWithDishes.shouldNotBe(visible, Duration.ofSeconds(10));
    }

    @Step("Первичная заставка\\лого\\анимация при открытии страницы")
    public void isStartScreenShown() {
        startScreenLogoContainer.shouldNotHave(cssValue("display","block"), Duration.ofSeconds(5));
    }

    @Step("Переключаем на разделение счёта, ждём что все статусы прогружены у позиций")
    public void clickDivideCheckSlider() {

        baseActions.forceWait(2000);  // toDO если после активации раздельного меню сразу выбрать позицию, то гарантировано 422, поэтому ждём
        baseActions.isElementVisibleDuringLongTime(divideCheckSlider, 5);
        baseActions.click(divideCheckSlider);

    }

    @Step("Проверка функционала кнопки поделиться счётом")
    public void isDivideSliderCorrect() {

        baseActions.isElementVisibleDuringLongTime(divideCheckSlider, 15);
        baseActions.click(divideCheckSlider);
        baseActions.isElementsListVisible(allDishesWhenDivided);

        baseActions.click(divideCheckSlider);
        baseActions.isElementsListInVisible(allDishesWhenDivided);
    }

    @Step("Меню корректно отображается")
    public void isDishListNotEmptyAndVisible() {

        baseActions.isElementVisibleDuringLongTime(dishListContainerWithDishes, 30);

    }

    public HashMap<String,Double> saveSumsInCheck() {

        double totalPaySumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");
        double totalTipsInTheMiddleSum = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
        double activePercentTips = 0;

        if (activeTipsButton.exists()) {

            activePercentTips = baseActions.convertSelectorTextIntoDoubleByRgx(activeTipsButton,"\\D+");

        }

        System.out.println(totalPaySumInCheck + " totalPaySumInCheck");
        System.out.println(totalTipsInTheMiddleSum + " totalTipsInTheMiddleSum");
        System.out.println(activePercentTips + " activePercentTips");

        HashMap<String,Double> sumsInfo  = new HashMap<>();

        sumsInfo.put("totalPaySumInCheck", totalPaySumInCheck);
        sumsInfo.put("totalTipsInTheMiddleSum", totalTipsInTheMiddleSum);
        sumsInfo.put("activePercentTips", activePercentTips);

        return sumsInfo;

    }

    @Step("Проверка что в админке ресторана отключены чаевые")
    public void isTipsDisabledInAdmin() { // toDo доделать. Пока апи нет, как костыль написан

        baseActions.openPage(R_KEEPER_ADMIN_URL);
        baseActions.sendHumanKeys(email, ADMIN_LOGIN_EMAIL);
        baseActions.sendHumanKeys(password, ADMIN_PASSWORD);
        baseActions.click(logInButton);
        baseActions.isElementVisibleDuringLongTime(configuration, 5);
        baseActions.click(configuration);
        baseActions.isElementVisibleDuringLongTime(optionTabTips, 5);
        baseActions.click(optionTabTips);
        tipsDisabled.shouldHave(checked);

    }

    @Step("Проверка что все элементы в блоке чаевых отображаются")
    public void isTipsContainerCorrect() {

        if (totalTipsSumInMiddle.exists()) {

            baseActions.isElementVisible(tipsContainer);

            if (totalTipsSumInMiddle.exists()) {

                boolean image = Boolean.TRUE.equals(Selenide.executeJavaScript(isWaiterImageBroken));
                Assertions.assertTrue(image, "Изображение официанта битое ил его нет");

                baseActions.isElementVisible(tipsWaiter);

                baseActions.isElementVisible(totalTipsSumInMiddle);
                baseActions.isElementsListVisible(tipsPercentList);
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

        if (serviceCharge.exists() && !serviceChargeInput.isSelected()) {

            activateServiceChargeIfDeactivated();

            double tapperDiscount = 0;

            if (discountField.exists()) {

                tapperDiscount = baseActions.convertSelectorTextIntoDoubleByRgx(discountField, "[^\\.\\d]+");
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
            double serviceChargeInField = baseActions.convertSelectorTextIntoDoubleByRgx(serviceCharge, "[^\\d\\.]+");
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

        totalTipsSumInMiddle.setValue(value);
        totalTipsSumInMiddle.shouldHave(value(value));
        activeTipsButton.shouldNotHave(exist);


    }

    @Step("Выставляем чаевые на 0 и активируем СБ")
    public void cancelTipsAndActivateSC(double cleanTotalSum) {

        setTipsToZero(cleanTotalSum);
        countServiceCharge(cleanTotalSum);

    }

    @Step("Выставляем чаевые на 0 и деактивируем СБ")
    public void cancelTipsAndDeactivateSc(double cleanTotalSum) {

        setTipsToZero(cleanTotalSum);
        deactivateServiceChargeIfActivated();

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

        if (serviceCharge.exists() && !serviceChargeInput.isSelected()) {

            baseActions.scrollTillBottom();
            baseActions.clickByJS(serviceChargeJS);

        }

    }

    @Step("Деактивируем СБ если активен")
    public void deactivateServiceChargeIfActivated() {

        baseActions.scrollTillBottom();

        if (serviceCharge.exists() && serviceChargeInput.isSelected()) {

            baseActions.clickByJS(serviceChargeJS);

        }

    }


    @Step("Проверка что у блюд с модификатором есть подписи в позиции")
    public void isModificatorTextCorrect() {

        for (SelenideElement element : allDishesInOrder) {

            element.$(".dish__checkbox-text-small").shouldBe(exist);

        }

        System.out.println("Текст модификатора выводится под блюдом");

    }

    @Step("Выставляем чаевые на 0, проверяем что чаевые не участвуют в общей сумме и СБ")
    public void setTipsToZero(double totalSum) {

        if (totalTipsSumInMiddle.exists()) {

            scrollTillBottom();
            baseActions.click(tips0);
            tips0.shouldHave(attributeMatching("class", "tips__list-item active"), Duration.ofSeconds(4));

            double serviceChargeSum = countServiceCharge(totalSum);
            int percent = baseActions.convertSelectorTextIntoIntByRgx(tips0, "\\D+");
            double tipsSumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(checkTipsSumWithDivide, "\\s₽");
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
            tipsOption.shouldHave(attributeMatching("class", "tips__list-item active"), Duration.ofSeconds(2));

            double serviceChargeSum = baseActions.convertSelectorTextIntoDoubleByRgx(serviceCharge, "[^\\d\\.]+");
            serviceChargeSum = baseActions.convertDouble(serviceChargeSum);

            int percent = baseActions.convertSelectorTextIntoIntByRgx(tipsOption, "\\D+");
            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

            double tipsSumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(checkTipsSumWithDivide, "\\s₽");
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

        if (tipsSum.exists()) {

            baseActions.scrollByJS(bodyJS);
            baseActions.click(tips0);

        }

        if (serviceCharge.exists() && serviceChargeInput.isSelected()) {

            baseActions.scrollByJS(bodyJS);
            baseActions.clickByJS(serviceChargeJS);

        }

    }

    @Step("Проверка что логика установленных чаевых по умолчанию от общей суммы корректна")
    public void isDefaultTipsBySumLogicCorrect() {

        double totalDishSum = countAllNonPaidDishesInOrder();

        if (totalDishSum < 196) {

            totalTipsSumInMiddle.shouldHave(value(MIN_SUM_TIPS_));

            tips10.shouldHave(attributeMatching("class", ".+disabled"));
            tips15.shouldHave(attributeMatching("class", ".+disabled"));
            tips20.shouldHave(attributeMatching("class", ".+disabled"));
            tips25.shouldHave(attributeMatching("class", ".+disabled"));

            System.out.println("Чаевые по умолчанию " + totalTipsSumInMiddle.getValue()
                    + " при сумме заказа " + totalDishSum);

        } else if (totalDishSum >= 196 && totalDishSum <= 245) {

            tips25.shouldHave(attributeMatching("class", ".+active"));

            tips10.shouldHave(attributeMatching("class", ".+disabled"));
            tips15.shouldHave(attributeMatching("class", ".+disabled"));
            tips20.shouldHave(attributeMatching("class", ".+disabled"));

            System.out.println("Чаевые по умолчанию " + activeTipsButton.getText()
                    + " при сумме заказа " + totalDishSum);

        } else if (totalDishSum > 245 && totalDishSum <= 326) {

            tips20.shouldHave(attributeMatching("class", ".+active"));

            tips10.shouldHave(attributeMatching("class", ".+disabled"));
            tips15.shouldHave(attributeMatching("class", ".+disabled"));

            System.out.println("Чаевые по умолчанию " + activeTipsButton.getText()
                    + " при сумме заказа " + totalDishSum);

        } else if (totalDishSum > 326 && totalDishSum <= 489) {

            tips15.shouldHave(attributeMatching("class", ".+active"));

            tips10.shouldHave(attributeMatching("class", ".+disabled"));

            System.out.println("Чаевые по умолчанию " + activeTipsButton.getText()
                    + " при сумме заказа " + totalDishSum);

        } else if (totalDishSum > 490) {

            tips10.shouldHave(attributeMatching("class", ".+active"));

            System.out.println("Чаевые по умолчанию " + activeTipsButton.getText()
                    + " при сумме заказа " + totalDishSum);

        }

    }

    @Step("Выбираем все не оплаченные позиции")
    public void chooseAllNonPaidDishes() {

        hideTapBar();
        double totalDishesSum = 0;

        System.out.println(allNonPaidAndNonDisabledDishesWhenDivided.size() + " общее число не оплаченных и не заблокированных позиций\n");

        for (int count = 0; count < allNonPaidAndNonDisabledDishesWhenDivided.size(); count++) {

            double currentDishPrice = baseActions.convertSelectorTextIntoDoubleByRgx
                    (allNonPaidAndNonDisabledDishesSumWhenDivided.get(count), "\\s₽");
            String currentDishName = nonPaidAndNonDisabledDishesNameWhenDivided.get(count).getText();

            totalDishesSum += currentDishPrice;

            scrollAndClick(nonPaidAndNonDisabledDishesNameWhenDivided.get(count));
            nonPaidAndNonDisabledDishesInputWhenDivided.get(count).shouldBe(checked, Duration.ofSeconds(10));

            System.out.println("Блюдо - " + currentDishName +
                    " - " + currentDishPrice +
                    ". Общая цена " + totalDishesSum);

        }

        showTapBar();

    }

    @Step("Отменяем определенное количество выбранных позиций")
    public void cancelCertainAmountChosenDishes(int count) { //

        hideTapBar();

        System.out.println(allNonPaidAndNonDisabledDishesWhenDivided.size() + " общее число не оплаченных и не заблокированных позиций\n");

        for (int i = 0; i < count; i++) {

            System.out.println("work on loop");

            if (allNonPaidAndNonDisabledDishesWhenDivided.get(i).$("input").isSelected()) {
                System.out.println("match on condition");
                scrollAndClick(nonPaidAndNonDisabledDishesNameWhenDivided.get(i));

            } else {

                System.out.println(allNonPaidAndNonDisabledDishesWhenDivided.get(i).$("input"));
                System.out.println(allNonPaidAndNonDisabledDishesWhenDivided.get(i).$("input").isSelected());
                System.out.println("not clicked");

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
        System.out.println(allNonPaidAndNonDisabledDishesWhenDivided.size() + " общее число позиций");

        for (int count = 1; count <= neededDishesAmount; count++) {

            int index;
            boolean flag;

            do {

                index = baseActions.generateRandomNumber(1, allNonPaidAndNonDisabledDishesWhenDivided.size()) - 1;
                flag = nonPaidAndNonDisabledDishesInputWhenDivided.get(index).isSelected();

                currentDishPrice = baseActions.convertSelectorTextIntoDoubleByRgx
                        (allNonPaidAndNonDisabledDishesSumWhenDivided.get(index), "\\s₽");
                currentDishName = nonPaidAndNonDisabledDishesNameWhenDivided.get(index).getText();


            } while (flag);

            totalDishesSum += currentDishPrice;

            scrollAndClick(nonPaidAndNonDisabledDishesNameWhenDivided.get(index));
            forceWait(300); // toDO сокет не успевает отвечать, из-за чего позиции виснут

            nonPaidAndNonDisabledDishesInputWhenDivided.get(index).shouldBe(checked, Duration.ofSeconds(10));
            System.out.println("Блюдо - " + currentDishName +
                    " - " + currentDishPrice +
                    ". Общая цена " + totalDishesSum);

        }

        showTapBar();

    }

    @Step("Выбираем определенное число блюд и считаем сумму выбранных рандомных позиций")
    public double countAllDishesNotDivided() { //toDo logs Cannot invoke "java.lang.StringBuilder.append(String)" because "logs" is null

        double totalDishesSum = 0;

     //   StringBuilder logs = null;

        System.out.println(allDishesInOrder.size() + " общее число позиций");

        for (SelenideElement element: allDishesInOrder) {

           if  (element.$(".dishList__item-status").getText().equals("")) {

               double currentDishPrice = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");
               String currentDishName = element.$(".dish__checkbox-content").getText();

               totalDishesSum += currentDishPrice;

               System.out.println("Блюдо - " + currentDishName +
                       " - " + currentDishPrice +
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

        double totalSumInMenu = 0;
        int counter = 0;

        for (SelenideElement element : nonPaidDishesWhenDivided) {

            if (!element.$("input+span").getCssValue("background-image").equals("none")) {

                double cleanPrice = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");
                String dishName = element.$(".dish__checkbox-text").getText();

                totalSumInMenu += cleanPrice;
                counter++;

                System.out.println(counter + ". " + dishName + " - " + cleanPrice + ". Общая сумма: " + totalSumInMenu);

            }

        }

        double markedDishesSum = baseActions.convertSelectorTextIntoDoubleByRgx(markedDishes, "\\s₽");

        Assertions.assertEquals(markedDishesSum, totalSumInMenu, 0.1);
        System.out.println("Сумма в поле 'Отмеченные позиции' " +
                markedDishesSum + " совпадает с общей чистой суммой заказа " + totalSumInMenu + "\n");

        return totalSumInMenu;

    }

    @Step("Считаем сумму не оплаченных позиций в заказе при разделении")
    public double countAllNonPaidDishesInOrderDivided() {

        double totalSumInMenu = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        System.out.println(allNonPaidAndNonDisabledDishesWhenDivided.size() + " все не оплаченные и не блокированные позиции");

        for (SelenideElement element : allNonPaidAndNonDisabledDishesWhenDivided) {

            double cleanPrice = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");
            String dishName = element.$(".dish__checkbox-text").getText();

            totalSumInMenu += cleanPrice;
            counter++;

            logs
                    .append("\n").append(counter).append(". ").append(dishName).append(" - ").append(cleanPrice)
                    .append(". Общая сумма: ").append(totalSumInMenu);

        }

        System.out.println(logs);
        return totalSumInMenu;

    }

    @Step("Считаем сумму всех оплаченных позиций в заказе не разделяя")
    public double countAllPaidDishesInOrder() {

        double totalSumInMenu = 0;
        int counter = 0;

        for (SelenideElement element : paidDishes) {

            double cleanPrice = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");
            String dishName = element.$(".dish__checkbox-text").getText();

            totalSumInMenu += cleanPrice;
            counter++;

            System.out.println(counter + ". " + dishName + " - " + cleanPrice + " --- цена текущей позиции");
            System.out.println("Общая сумма: " + totalSumInMenu);

        }
        System.out.println(totalSumInMenu + " total sum in menu");
        return totalSumInMenu;

    }

    @Step("Считаем сумму не оплаченных позиций в заказе не разделяя")
    public double countAllNonPaidDishesInOrder() {

        double totalSumInMenu = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        System.out.println(allNonPaidAndNonDisabledDishes.size() + " все не оплаченные и не блокированные позиции");

        for (SelenideElement element : allNonPaidAndNonDisabledDishes) {

            double cleanPrice = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");
            String dishName = element.$(".dish__checkbox-text").getText();

            totalSumInMenu += cleanPrice;
            counter++;

            logs
                    .append("\n").append(counter).append(". ").append(dishName).append(" - ").append(cleanPrice)
                    .append(". Общая сумма: ").append(totalSumInMenu);

        }

        System.out.println(logs);
        return totalSumInMenu;

    }

    @Step("Забираем коллекцию заблокированных позиций в заказе для следующего теста")
    public HashMap<Integer, Map<String, Double>> countDisabledDishesAndSetCollection() {

        HashMap<Integer, Map<String, Double>> tapperDishes = new HashMap<>();
        int i = 0;

        for (SelenideElement element : nonPaidDishesWhenDivided) {

            Map<String, Double> temporaryMap = new HashMap<>();

            if (!element.$("input+span").getCssValue("background-image").equals("none")) {

                String name = element.$(".dish__checkbox-text").getText();
                double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");

                temporaryMap.put(name, price);
                tapperDishes.put(i, temporaryMap);

                i++;
            }

        }

        return tapperDishes;

    }

    @Step("Забираем коллекцию всех позиций в заказе для следующего теста")
    public HashMap<Integer, Map<String, Double>> getAllDishesAndSetCollection() {

        HashMap<Integer, Map<String, Double>> tapperDishes = new HashMap<>();
        int i = 0;

        System.out.println("\n" + "Количество не оплаченных и не заблокированных блюд : " + allDishesInOrder.size());

        for (SelenideElement element : allDishesInOrder) {

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = element.$(".dish__checkbox-text").getText();
            double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");

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

        System.out.println("\n" + "Количество не оплаченных и не заблокированных блюд : " + allNonPaidAndNonDisabledDishesWhenDivided.size());

        for (SelenideElement element : allNonPaidAndNonDisabledDishesWhenDivided) {

            if (element.$("input").isSelected()) {

                Map<String, Double> temporaryMap = new HashMap<>();

                String name = element.$(".dish__checkbox-text").getText();
                double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");

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

        dishListContainerWithDishes.shouldBe(visible,Duration.ofSeconds(2));

        HashMap<Integer, Map<String, Double>> chosenDishesCurrent = new HashMap<>();
        int i = 0;

        System.out.println(divideCheckSlider.isSelected() + " разделяется счёт?");
        System.out.println(allNonPaidAndNonDisabledDishesWhenDivided.size() + " не оплаченных и не заблоченных блюд количество");
        System.out.println(disabledAndPainDishesWhenDivided.size() + " заблоченных или оплаченных количество");
        System.out.println(allDishesWhenDivided.filterBy(text(" Оплачивается ")));

        System.out.println("\n" + "Количество заблокированных блюд : " + disabledDishes.size());

        for (SelenideElement element : disabledDishes) {

            Map<String, Double> temporaryMap = new HashMap<>();

            element.$x("*[contains(text(),'Оплачивается')]").shouldHave(exist);

            String name = element.$(".dish__checkbox-text").getText();
            double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");

            temporaryMap.put(name, price);
            chosenDishesCurrent.put(i, temporaryMap);

            i++;

        }

        System.out.println(chosenDishesEarlier + " ранее заказ");
        System.out.println(chosenDishesCurrent + " текущий заказ");

        Assertions.assertEquals(chosenDishesEarlier,chosenDishesCurrent,"Блюда не совпадают");
        System.out.println("Блюда совпадают");


    }

    @Step("Проверяем коллекцию всех оплаченных позиций первым гостем со втором")
    public void checkIfDishesPaidEarlier(HashMap<Integer, Map<String, Double>> chosenDishesEarlier) {

        dishListContainerWithDishes.shouldBe(visible,Duration.ofSeconds(2));

        HashMap<Integer, Map<String, Double>> chosenDishesCurrent = new HashMap<>();
        int i = 0;

        System.out.println("\n" + "Количество оплаченных блюд : " + paidDishes.size() );

        for (SelenideElement element : paidDishes) {

            Map<String, Double> temporaryMap = new HashMap<>();

            element.$x("//*[contains(text(),'Оплачено')]").shouldHave(text(" Оплачено "));

            String name = element.$(".dish__checkbox-text").getText();
            double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");

            temporaryMap.put(name, price);
            chosenDishesCurrent.put(i, temporaryMap);

            i++;

        }

        System.out.println(chosenDishesEarlier + " ранее заказ");
        System.out.println(chosenDishesCurrent + " текущий заказ");

        Assertions.assertEquals(chosenDishesEarlier,chosenDishesCurrent,"Блюда не совпадают");
        System.out.println("Блюда совпадают");


    }

    @Step("Проверяем коллекцию всех выбранных ранее позиций что они теперь оплачены")
    public void checkIfDishesDisabledAtAnotherGuestArePaid(HashMap<Integer, Map<String, Double>> chosenDishesByAnotherGuest) {

        dishListContainerWithDishes.shouldBe(visible,Duration.ofSeconds(2));

        HashMap<Integer, Map<String, Double>> paidDishes = new HashMap<>();
        int i = 0;

        System.out.println("\n" + "Количество оплаченных и не заблокированных блюд : " + DishList.paidDishes.size() );

        for (SelenideElement element : DishList.paidDishes) {

            element.$x("*[contains(text(),'Оплачено')]").shouldHave(exist);

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = element.$(".dish__checkbox-text").getText();

            double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");

            System.out.println("Блюдо - " + name + " - " + price);

            temporaryMap.put(name, price);
            paidDishes.put(i, temporaryMap);

            i++;

        }

        System.out.println(chosenDishesByAnotherGuest + " заказ ранее выбранных позиций");
        System.out.println(paidDishes + " заказ оплаченных позиций");

        Assertions.assertEquals(chosenDishesByAnotherGuest,paidDishes,"Блюда не совпадают");
        System.out.println("Блюда совпадают");

    }

    @Step("Проверяем что блюда, которые выбраны ранее, в статусе 'Ожидается'")
    public void dishesAreDisabledInDishList(HashMap<Integer, Map<String, Double>> chosenDishes) {

        int successAmount = 0;
        System.out.println(disabledDishes.size() + " disabled dishes size");

        for (int i = 0; i < chosenDishes.size(); i++) {

            for (int k = 0; k < disabledDishes.size(); k++) {

                String dishNameInCurrentDividedMenu = disabledDishes.get(i).$(".dish__checkbox-text").getText();
                System.out.println(dishNameInCurrentDividedMenu);
                boolean isDishNameSameAsDividedEarlier = chosenDishes.get(k).containsKey(dishNameInCurrentDividedMenu);
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

        for (SelenideElement element : allNonPaidAndNonDisabledDishesWhenDivided) {

            element.$(".shouldHave(text(' Оплачивается ')").shouldHave(text(" Оплачивается "));
            element.$(".dishList__item-status img").shouldBe(visible, Duration.ofSeconds(2));
            element.$("input+span").shouldHave(cssValue("background", "#dcdee3"));
            element.$("input+span").shouldBe(selected);

        }

    }

    @Step("Проверяем что сумма всех блюд совпадает с итоговой без чаевых и СБ")
    public void isTotalSumInDishesMatchWithTotalPay(double totalSumByDishesInOrder) {

        baseActions.isElementVisibleDuringLongTime(totalPay, 2);

        double totalPaySumInCheck = getClearOrderAmount();

        System.out.println(totalPaySumInCheck + " общая сумма в 'Итого к оплате'");
        Assertions.assertEquals(totalPaySumInCheck, totalSumByDishesInOrder, 0.01,
                "Сумма за все блюда " + totalSumByDishesInOrder + " совпала с суммой в 'Итого к оплате' "
                        + totalPaySumInCheck);

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
            double tipsSumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(checkTipsSumWithDivide, "\\s₽");

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
            double tipsSumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(checkTipsSumWithDivide, "\\s₽");

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
        boolean image = Boolean.TRUE.equals(Selenide.executeJavaScript(isWaiterImageBroken));
        Assertions.assertTrue(image, "Изображение официанта битое ил его нет");
        baseActions.isElementVisible(tipsWaiter);
        baseActions.isElementInvisible(tipsInCheckSum);
        baseActions.isElementInvisible(totalTipsSumInMiddle);
        baseActions.isElementInvisible(activeTipsButton);
        baseActions.isElementInvisible(resetTipsButton);
        tipsOptions.shouldHave(CollectionCondition.size(0));

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

            double serviceChargeInField = baseActions.convertSelectorTextIntoDoubleByRgx(serviceCharge, "[^\\d\\.]+");
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

        if (serviceCharge.exists() && serviceChargeInput.isSelected()) {

            System.out.println("СБ включены");
            serviceChargeSum = baseActions.convertSelectorTextIntoDoubleByRgx(serviceCharge, "[^\\d\\.]+");
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

        baseActions.click(divideCheckSlider);
        divideCheckSliderInput.shouldNotBe(checked);
        allDishesWhenDivided.shouldHave(CollectionCondition.size(0));
        System.out.println("Разделить чек нельзя из-за одного блюда в заказе");

    }

    @Step("Устанавливаем рандомные чаевые")
    public void setRandomTipsOption() {

        scrollTillBottom();
        baseActions.click(resetTipsButton);

        if (notDisabledAndZeroTipsPercentOptions.size() != 0) {

            int index = baseActions.generateRandomNumber(1, notDisabledAndZeroTipsPercentOptions.size()) - 1;

            baseActions.click(notDisabledAndZeroTipsPercentOptions.get(index));
            notDisabledAndZeroTipsPercentOptions.get(index)
                    .shouldHave(attributeMatching("class", ".*active.*"), Duration.ofSeconds(2));
            System.out.println(notDisabledAndZeroTipsPercentOptions.get(index).getText() + " активные чаевые");

        }

    }

    @Step("Сброс чаевых")
    public void resetTips() {

        scrollTillBottom();
        baseActions.click(resetTipsButton);
        tips0.shouldHave(attributeMatching("class", ".*active.*"));
        totalTipsSumInMiddle.shouldHave(value("0"));
        double tipsInTheCheck = baseActions.convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
        Assertions.assertEquals(tipsInTheCheck, 0);
        System.out.println("Сброс работает корректно");

    }

    @Step("Проверяем что сумма 'Итого к оплате' совпадает с суммой счетчика в иконке кошелька")
    public void isSumInWalletMatchWithTotalPay() {

        if (!tabBar.exists()) {

            System.out.println(serviceChargeInput.isSelected() + " тут сбор включен? isSumInWalletMatchWithTotalPay");

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

        if (tipsSum.exists()) {

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

        double totalPaidSum = 0;
        double anotherGuestSum = baseActions.convertSelectorTextIntoDoubleByRgx(TipsAndCheck.anotherGuestSum, "[^\\d\\.]+");

        for (SelenideElement element : disabledAndPainDishesWhenDivided) {

            double currentElementSum = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");
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

        String defaultTips = totalTipsSumInMiddle.getValue();
        totalTipsSumInMiddle.clear();
        baseActions.sendHumanKeys(totalTipsSumInMiddle, MIN_SUM_FOR_TIPS_ERROR);
        baseActions.isElementVisible(tipsErrorMsg);
        tipsErrorMsg.shouldHave(text(TIPS_ERROR_MSG));
        paymentButton.shouldBe(disabled);

        totalTipsSumInMiddle.clear();

        assert defaultTips != null;
        baseActions.sendHumanKeys(totalTipsSumInMiddle, defaultTips);
        baseActions.isElementInvisible(tipsErrorMsg);

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

        if (serviceChargeInput.exists()) {

            baseActions.isElementVisible(serviceCharge);

        }

    }

    @Step("Проверка политики конфиденциальности")
    public void isConfPolicyShown() { //

        scrollTillBottom();
        baseActions.isElementVisible(confPolicyLink);
        baseActions.click(confPolicyLink);
        confPolicyContainer.shouldHave(cssValue("display", "block"));
        confPolicyContent.shouldHave(matchText("УСЛОВИЯ ИСПОЛЬЗОВАНИЯ И ПОЛИТИКА КОНФИДЕНЦИАЛЬНОСТИ TAPPER"));
        Selenide.executeJavaScript("document.querySelector(\".vLandingPoliticModal\").style.display = \"none\";");
        baseActions.isElementInvisible(confPolicyContainer);

    }

    @Step("Клик по кнопке оплаты")
    public void clickOnPaymentButton() {

        scrollTillBottom();
        baseActions.click(paymentButton);

    }

    @Step("Отображается лоадер на странице")
    public void isPageLoaderShown() {

        baseActions.isElementVisibleDuringLongTime(pagePreLoader, 6);

    }

    @Step("Сохранение общей суммы в таппере для передачи в другой тест")
    public double saveTotalPayForMatchWithAcquiring() {
        return baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
    }

    @Step("Сбрасываем чаевые и СБ чтобы получить чистую сумму за позиции")
    public double getClearOrderAmount() {

        double serviceChargeSum = 0;
        double tips = 0;

        if (serviceChargeInput.isSelected()) {

            serviceChargeSum = baseActions.convertSelectorTextIntoDoubleByRgx(serviceCharge, "[^\\d\\.]+");
            serviceChargeSum = baseActions.convertDouble(serviceChargeSum);

        }

        if (totalTipsSumInMiddle.exists()) {

            tips = baseActions.convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");

        }

        double order_amount = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        order_amount = order_amount - tips - serviceChargeSum;

        System.out.println(serviceChargeSum + " sc");
        System.out.println(tips + " tips");
        System.out.println(order_amount + " order_amount");

        return order_amount;

    }

    @Step("Сохранение всех сумм для проверки что транзакция создалась на b2p")
    public HashMap<String, Integer> savePaymentDataTapperForB2b() {

        HashMap<String, Integer> paymentData = new HashMap<>();

        int tips = 0;
        int fee = 0;

        double order_amountD = getClearOrderAmount() * 100;
        Integer order_amount = (int) order_amountD;

        paymentData.put("order_amount", order_amount);

        if (totalTipsSumInMiddle.exists()) {

            double tipsD = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
            tips = (int) (tipsD) * 100;

        }

        paymentData.put("tips", tips);

        if (serviceCharge.exists() && serviceChargeInput.isSelected()) {

            double feeD = baseActions.convertSelectorTextIntoDoubleByRgx(serviceCharge, "[^\\d\\.]+");
            feeD = convertDouble(feeD) * 100;
            fee = (int) feeD;

        }

        paymentData.put("fee", fee);
        System.out.println(paymentData + " данные для б2п");
        return paymentData;

    }

    @Step("Проверка нижнего навигационного меню. Проверки на элементы, кликабельность, переходы, открытие")
    public void isTapBarShown() {

        baseActions.scroll(tabBar);
        baseActions.isElementVisible(tabBar);
        baseActions.isElementVisibleAndClickable(tabBarMenuIcon);
        baseActions.isElementVisibleAndClickable(tabBarWalletIcon);

        baseActions.click(tabBarMenuIcon);
        baseActions.isElementVisible(menuDishesContainer);

        baseActions.isElementInvisible(dishListContainerWithDishes);

        baseActions.click(tabBarWalletIcon);

        baseActions.isElementVisible(dishListContainerWithDishes);
        baseActions.isElementInvisible(menuDishesContainer);

    }

    @Step("Вызов официанта. Проверки на элементы, кликабельность, заполнение, открытие\\закрытие")
    public void isCallWaiterCorrect() {

        baseActions.scroll(callWaiterButton);
        baseActions.isElementVisibleAndClickable(callWaiterButton);
        baseActions.click(callWaiterButton);

        baseActions.isElementVisible(callWaiterHeading);
        baseActions.isElementInvisible(closeCallWaiterText);
        baseActions.isElementVisible(callWaiterContainer);
        baseActions.isElementVisible(callWaiterFadedBackground);
        baseActions.isElementVisibleAndClickable(callWaiterButtonSend);
        baseActions.isElementVisibleAndClickable(callWaiterButtonCancel);
        baseActions.isElementVisible(callWaiterCloseButton);
        baseActions.isElementVisible(callWaiterCommentArea);

        baseActions.click(callWaiterButton);
        baseActions.click(callWaiterButtonCancel);
        baseActions.isElementInvisible(callWaiterContainer);

        baseActions.click(callWaiterButton);
        callWaiterCommentArea.shouldHave(attribute("placeholder", "Комментарий..."));
        baseActions.sendHumanKeys(callWaiterCommentArea, TEST_WAITER_COMMENT);
        baseActions.elementShouldHaveValue(callWaiterCommentArea, TEST_WAITER_COMMENT);

        baseActions.click(callWaiterButtonSend);
        baseActions.isElementVisible(successCallWaiterHeading);
        baseActions.click(closeCallWaiterFormInSuccess);
        callWaiterContainer.shouldNot(visible);

        baseActions.scrollByJS(bodyJS);

    }

    @Step("Открытие страницы в новой вкладке с фокусом")
    public void openInNewTabUrl(String url) {
        Selenide.executeJavaScript("window.open('" + url + "', '_blank').focus();");
        forceWait(2000);

    }

    @Step("Подмена куки юзера")
    public void setAnotherGuestCookie() {

        WebDriverRunner.getWebDriver().manage().deleteCookieNamed("guest");
        Cookie cookieGuest = new Cookie("guest", "11111");
        WebDriverRunner.getWebDriver().manage().addCookie(cookieGuest);
        Selenide.refresh();

    }

    @Step("Открытие страницы в новой вкладке")
    public void openNewTabAndSwitchTo(String url) {

        openInNewTabUrl(url);
        Selenide.switchTo().window(1);

    }

}


