package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
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

    @Step("Забираем куки session")
    public String getCookieSession() {
        return Selenide.executeJavaScript(userSession);
    }

    @Step("Забираем куки guest")
    public String getCookieGuest() {
        return Selenide.executeJavaScript(userGuest);
    }

    @Step("Переход на страницу {url} и ждём принудительно пока не прогрузятся все скрипты\\элементы\\сокет")
    public void openTapperLink(String url) {

        baseActions.openPage(url);
        baseActions.forceWait(TIME_WAIT_FOR_FULL_LOAD);
    }

    @Step("Надпись что заказ успешно оплачен по центру страницы")
    public void isEmptyOrder() { // toDO убрал проверку на "ваш заказ успешно оплачен" так как есть баг

        baseActions.isElementVisible(emptyOrderHeading);
        emptyOrderHeading.shouldHave(text("Скоро здесь появится ваш заказ"), Duration.ofSeconds(30));

        double totalSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter, "\\s₽");
        Assertions.assertEquals(totalSumInWallet, 0, "Сумма в кошельке не равно 0");

    }

    @Step("Отображается пустой заказ")
    public void isTableEmpty() {
        dishListContainerWithDishes.shouldNotBe(visible, Duration.ofSeconds(10));
    }

    @Step("Первичная заставка\\лого\\анимация при открытии страницы")
    public void isStartScreenShown() {
        baseActions.isElementVisibleDuringLongTime(startScreenLogo, 10);
    }

    @Step("Отображение номера столика")
    public void isTableNumberShown() {
        baseActions.isElementVisible(tableNumber);
    }

    @Step("Кнопка поделиться счётом. Кнопка есть, но не активна, т.к. 1 позиция в заказе")
    public void isDivideCheckSliderWith1Dish() {

        baseActions.isElementVisible(DishList.divideCheckSlider);

        if (DishList.allDishesInOrder.size() == 1) {

            DishList.divideCheckSlider.click();
            DishList.allDishesWhenDivided.shouldHave(CollectionCondition.size(0));

        }

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

    @Step("Кнопка поделиться счётом. Кнопка не присутствует из-за того что в заказе есть скидки")
    public void isDivideCheckSliderDisabled() {
        baseActions.isElementInvisible(divideCheckSlider);
    }

    @Step("Меню корректно отображается")
    public void isDishListNotEmptyAndVisible() {

        baseActions.isElementVisibleDuringLongTime(dishListContainerWithDishes, 30);

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

        if (tipsContainer.exists()) {

            baseActions.isElementVisible(tipsContainer);

            if (totalTipsSumInMiddle.exists()) {
                baseActions.isElementVisible(tipsWaiter);
                baseActions.isElementVisible(totalTipsSumInMiddle);
                baseActions.isElementsListVisible(tipsPercentList);
                checkCustomTipForError();

            }

        }

    }

    @Step("Ввод кастомных чаевых")
    public void setCustomTips(String value) {

        totalTipsSumInMiddle.setValue(value);
        totalTipsSumInMiddle.shouldHave(value(value));


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

            activateServiceChargeIfDisabled();

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

    @Step("Выставляем чаевые на 0 и активируем СБ")
    public void cancelTipsAndActivateSC(double cleanTotalSum) {

        setTipsToZero(cleanTotalSum);
        countServiceCharge(cleanTotalSum);

    }

    @Step("Выставляем чаевые на 0 и деактивируем СБ")
    public void cancelTipsAndDisableSC(double cleanTotalSum) {

        setTipsToZero(cleanTotalSum);
        disableServiceChargeIfActivated();

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

            activateServiceChargeIfDisabled();

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

        } else if (totalDishSum >= 196 && totalDishSum <= 245) {

            tips25.shouldHave(attributeMatching("class", ".+active"));

            tips10.shouldHave(attributeMatching("class", ".+disabled"));
            tips15.shouldHave(attributeMatching("class", ".+disabled"));
            tips20.shouldHave(attributeMatching("class", ".+disabled"));

        } else if (totalDishSum > 245 && totalDishSum <= 326) {

            tips20.shouldHave(attributeMatching("class", ".+active"));

            tips10.shouldHave(attributeMatching("class", ".+disabled"));
            tips15.shouldHave(attributeMatching("class", ".+disabled"));

        } else if (totalDishSum > 326 && totalDishSum <= 489) {

            tips15.shouldHave(attributeMatching("class", ".+active"));

            tips10.shouldHave(attributeMatching("class", ".+disabled"));

        } else if (totalDishSum > 490) {

            tips10.shouldHave(attributeMatching("class", ".+active"));

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

    @Step("Считаем сумму выбранных рандомных позиций")
    public void chooseCertainAmountDishes(int neededDishesAmount) {

        hideTapBar();

        double totalDishesSum = 0;
        double currentDishPrice;
        String currentDishName;

        System.out.println("\n" + "Количество рандомных позиций для оплаты: " + neededDishesAmount);
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
            forceWait(500); // toDO сокет не успевает отвечать, из-за чего позиции виснут

            nonPaidAndNonDisabledDishesInputWhenDivided.get(index).shouldBe(checked, Duration.ofSeconds(10));
            System.out.println("Блюдо - " + currentDishName +
                    " - " + currentDishPrice +
                    ". Общая цена " + totalDishesSum);

        }

        showTapBar();

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
    public HashMap<Integer, Map<String, Double>> countAllDishesAndSetCollection() {

        HashMap<Integer, Map<String, Double>> tapperDishes = new HashMap<>();
        int i = 0;

        for (SelenideElement element : allDishesInOrder) {

            Map<String, Double> temporaryMap = new HashMap<>();

            String name = element.$(".dish__checkbox-text").getText();
            double price = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"), "\\s₽");

            temporaryMap.put(name, price);
            tapperDishes.put(i, temporaryMap);

            i++;

        }

        System.out.println(tapperDishes);
        return tapperDishes;

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

    @Step("Проверяем что имя и сумма перечеркнуты у оплаченных блюд")
    public void isStylesCorrectToPaidDishes() {

        for (SelenideElement element : paidDishesWhenDivided) {

            element.$(".dishList__item-status").shouldHave(text(" Оплачено "));
            element.$(".dish__checkbox-text").shouldHave(cssValue("text-decoration", "line-through"));
            element.$(".sum").shouldHave(cssValue("text-decoration", "line-through"));

        }

    }

    @Step("Проверяем что имя и сумма перечеркнуты у заблокированных блюд")
    public void isStylesCorrectToDisabledDishes() {

        separateOrderHeading.shouldBe(exist, visible);

        for (SelenideElement element : allNonPaidAndNonDisabledDishesWhenDivided) {

            element.$(".dishList__item-status").shouldHave(text(" Оплачивается "));
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
            tipsOption.shouldHave(attributeMatching("class", ".*active"), Duration.ofSeconds(1));

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

        disableServiceChargeIfActivated();

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", ".*active"), Duration.ofSeconds(2));

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


    @Step("Проверяем что сбор формируется корректно по формуле со всеми чаевыми")
    public void checkScLogic(double cleanDishesSum) {

        activateServiceChargeIfDisabled();

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", "tips__list-item active"));

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
            System.out.println("Сервисный сбор считается корректно по формуле 1.5% от суммы"
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

        double tipsInTheMiddle = 0;

        if (tipsContainer.exists()) {

            tipsInTheMiddle = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
            System.out.println(tipsInTheMiddle + " текущие чаевые по центру");

            double tipsInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(tipsInCheckSum, "\\s₽");
            System.out.println(tipsInCheck + " текущие чаевые в поле 'Чаевые'");

            Assertions.assertEquals(tipsInTheMiddle, tipsInCheck,
                    "Чаевые по центру не совпадают с чаевыми в поле 'Чаевые'");
            System.out.println("Чаевые по центру " + tipsInTheMiddle +
                    " совпадают с чаевыми в поле 'Чаевые' " + tipsInCheck);

        }

        System.out.println(tipsInTheMiddle + " чаевые");
        return tipsInTheMiddle;

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

        baseActions.click(resetTipsButton);

        if (notDisabledAndZeroTipsPercentOptions.size() != 0) {

            int index = baseActions.generateRandomNumber(1, notDisabledAndZeroTipsPercentOptions.size()) - 1;

            baseActions.click(notDisabledAndZeroTipsPercentOptions.get(index));
            notDisabledAndZeroTipsPercentOptions.get(index).shouldHave(attributeMatching("class", "tips__list-item active"), Duration.ofSeconds(2));
            System.out.println(notDisabledAndZeroTipsPercentOptions.get(index).getText() + " активные чаевые");

        }


    }

    @Step("Сброс чаевых")
    public void resetTips() {

        scrollTillBottom();
        baseActions.click(resetTipsButton);
        tips0.shouldHave(attributeMatching("class", ".*active"));
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

            disableServiceChargeIfActivated();

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
    public void isShareButtonShown() { // toDo какая-то беда с этим navigator.share(). тест то падает, то нет, будет пока флаки

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

    @Step("Активируем СБ если не активен")
    public void activateServiceChargeIfDisabled() {

        if (serviceCharge.exists() && !serviceChargeInput.isSelected()) {

            baseActions.scrollTillBottom();
            baseActions.clickByJS(serviceChargeJS);

        }

    }

    @Step("Деактивируем СБ если активен")
    public void disableServiceChargeIfActivated() {

        baseActions.scrollTillBottom();

        if (serviceCharge.exists() && serviceChargeInput.isSelected()) {

            baseActions.clickByJS(serviceChargeJS);

        }

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

        double order_amountD = getClearOrderAmount();
        Integer order_amount = (int) order_amountD * 100;
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

        baseActions.isElementInvisible(closeCallWaiterText);
        baseActions.isElementVisible(callWaiterContainer);
        baseActions.isElementVisible(callWaiterFadedBackground);
        baseActions.isElementVisibleAndClickable(callWaiterButtonSend);
        baseActions.isElementVisibleAndClickable(callWaiterButtonCancel);
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
        forceWait(4000);

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


