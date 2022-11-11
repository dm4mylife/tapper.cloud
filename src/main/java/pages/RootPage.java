package pages;

import com.codeborne.selenide.*;
import common.BaseActions;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.Assertions;


import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static constants.Constant.JSScripts.isShareButtonCorrect;
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

    @Step("Переход на страницу {url}")
    public void openTapperLink(String url) {
        baseActions.openPage(url);
    }

    @Step("Надпись что заказ успешно оплачен по центру страницы")
    public void isEmptyOrder() { // toDO убрал проверку на "скоро появится заказ" так как есть баг

        baseActions.isElementVisible(emptyOrderHeading);
        emptyOrderHeading.shouldHave(text("Заказ успешно оплачен"), Duration.ofSeconds(10));

    }

    @Step("Отображается пустой заказ")
    public void isTableEmpty() {
        dishListContainerWithDishes.shouldNotBe(visible,Duration.ofSeconds(10));
    }

    @Step("Первичная заставка\\лого\\анимация при открытии страницы")
    public void isStartScreenShown() {
        baseActions.isElementVisibleDuringLongTime(startScreenLogo, 30);
    }

    @Step("Отображение номера столика")
    public void isTableNumberShown() {
        baseActions.isElementVisible(tableNumber);
    }

    @Step("Кнопка поделиться счётом. Кнопка есть, но не активна, т.к. 1 позиция в заказе")
    @Description("Если в меню один заказ, то кнопка разделить счёт не кликабельна")
    public void isDivideCheckSliderWith1Dish() {

        baseActions.isElementVisible(DishList.divideCheckSlider);

        if (DishList.allDishesInOrder.size() == 1) {

            DishList.divideCheckSlider.click();
            DishList.allDishesWhenDivided.shouldHave(CollectionCondition.size(0));

        }

    }

    @Step("Переключаем на разделение счёта, ждём что все статусы прогружены у позиций")
    public void clickDivideCheckSlider() {

        baseActions.forceWait(2500L);  // иначе никак
        baseActions.isElementVisibleDuringLongTime(divideCheckSlider,5);
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
    @Description("Не установлено никаких скидок в заказе")
    public void isDivideCheckSliderDisabled() {
        baseActions.isElementInvisible(divideCheckSlider);
    }

    @Step("Меню корректно отображается")
    public void isDishListNotEmptyAndVisible() {
        dishListContainerWithDishes.shouldBe(visible, Duration.ofSeconds(30));
    }

    public void isTipsDisabledInAdmin() {

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

    @Step("Проверка что все элементы в блоке чаевыех отображаются")
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

    @Step("Очищаем локал и куки для разделения чека")
    public void clearAllSiteData() {

        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
        Selenide.refresh();
        baseActions.forceWait(1000L); // иначе никак 422

    }

    @Step("Проверка логики суммы сервисного сбора с общей суммый и чаевыми")
    public double countServiceCharge(double totalSum) { //

        double serviceChargeSumClear = 0;
        double serviceChargeSumInCheckbox;

        if (serviceCharge.exists()) {

            activateServiceChargeIfDisabled();

            serviceChargeSumClear = convertDouble(totalSum * (SERVICE_PRICE_PERCENT_FROM_TOTAL_SUM / 100));
            serviceChargeSumInCheckbox = convertDouble
                    (baseActions.convertSelectorTextIntoDoubleByRgx(serviceCharge, "[^\\d\\.]+"));

            if (tipsContainer.exists()) {

                int tipsCount = Integer.parseInt(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
                double serviceChargeFromTips = convertDouble(tipsCount * (SERVICE_PRICE_PERCENT_FROM_TIPS / 100));

                serviceChargeSumClear = convertDouble(serviceChargeSumClear + serviceChargeFromTips);

            }

            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
            double currentSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(TapBar.totalSumInWalletCounter, "\\s₽");

            Assertions.assertEquals(serviceChargeSumInCheckbox,serviceChargeSumClear,0.1);
            System.out.println("\n" + "Сервисный сбор в чекбоксе " + serviceChargeSumInCheckbox
                    + " совпадает с общей суммой если считать по отдельности " + serviceChargeSumClear);

            Assertions.assertEquals(currentSumInWallet, totalPaySum, 0.1);
            System.out.println("Сумма в 'Итого к оплате' " + totalPaySum
                    + " совпадает с суммой в иконке кошелька вместе с СБ "
                    + currentSumInWallet + "\n");

            baseActions.clickByJS(serviceChargeJS);
            Assertions.assertEquals(totalPaySum, currentSumInWallet, 0.1);

            baseActions.clickByJS(serviceChargeJS);

        }

        return serviceChargeSumClear;

    }

    @Step("Выставляем чаевые на 0 и активируем СБ")
    public void cancelTipsAndActivateSC(double cleanTotalSum) {

        setTipsToZero(cleanTotalSum);
        countServiceCharge(cleanTotalSum);


    }


    @Step("Выставляем чаевые на 0, проверяем что кнопка активна и суммы пересчитались")
    public void setTipsToZero(double totalSum) {

        if (tipsContainer.exists()) {

            scrollTillBottom();
            baseActions.click(Tips0);
            Tips0.shouldHave(attributeMatching("class", "tips__list-item active"), Duration.ofSeconds(2));

            double serviceChargeSum = countServiceCharge(totalSum);
            int percent = baseActions.convertSelectorTextIntoIntByRgx(Tips0, "\\D+");
            double tipsSumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(checkTipsSumWithDivide, "\\s₽");
            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));

            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
            double currentSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(TapBar.totalSumInWalletCounter, "\\s₽");

            Assertions.assertEquals(percent, 0,"Текущий процент чаевых не равен 0");
            Assertions.assertEquals(totalTipsSumInMiddle, 0, 0.1,"Общие чаевые в центре не равны 0");
            Assertions.assertEquals(tipsSumInCheck, 0, 0.1, "Чаевые в поле 'Чаевые' не равны 0");

            Assertions.assertEquals(totalPaySum, currentSumInWallet, 0.1,
                    "Сумма в 'Итого к оплате' не равна сумме в иконке кошелька");
            Assertions.assertEquals(totalSum + serviceChargeSum, totalPaySum, 0.1,
                    "Общая чистая сумма не равна сумме в 'Итого к оплате'");


        }

    }


    @Step("Прощелкиваем все чаевые и проверяем что суммых корректны и сходятся")
    public void checkAllTipsOptions(double totalSum) {

        showTapBar();
        scrollTillBottom();
        StringBuilder logs = new StringBuilder();
        double cleanTips = 0;

        for (SelenideElement tipsOption : notDisabledTipsPercentOptions) {

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class", "tips__list-item active"), Duration.ofSeconds(2));

            double serviceChargeSum = countServiceCharge(totalSum);
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

//                System.out.println("\n" + totalSum + " общая сумма за блюда без чаевых и без сервисного сбора");
//                System.out.println(cleanTips + " чаевые посчитанные по общей сумме за блюда (чистые)");
//                System.out.println(totalSum + cleanTips + " общая сумма за блюда c чаевым, но без сервисного сбора");
//                System.out.println(serviceChargeSum + " сервисный сбор по текущей проценту чаевых: " + percent + "");
//                System.out.println(totalSum + cleanTips + serviceChargeSum + " общая сумма за блюда c чаевым и сервисным сбором");
//                System.out.println(totalPaySum + " общая сумма в графе 'Итого к оплате' c учетом чаевых и сервисного сбора\n");


                Assertions.assertEquals(totalTipsSumInMiddle, cleanTips, 0.1,
                        "Общая сумма чаевых по центру " + totalTipsSumInMiddle +
                                " не совпала с суммой чистых чаевых " + cleanTips);

                Assertions.assertEquals(tipsSumInCheck, cleanTips, 0.1,
                        "Чаевые в 'Чаевые'" + tipsSumInCheck +
                                " не совпали с суммой чистых чаевых " + cleanTips);

                Assertions.assertEquals(totalSumPlusCleanTipsPlusServiceChargeSum, totalPaySum, 0.1,
                        "Чистая сумма за блюда + чистые чаевые + СБ " + totalSumPlusCleanTipsPlusServiceChargeSum
                                + " равна сумме в 'Итого к оплате' + чаевые + СБ " + totalPaySum);


                baseActions.click(resetTipsButton);



        }

        System.out.println(logs);

        Allure.addAttachment("Подсчёт сумм", "text/plain", logs.toString());



    }

    @Step("Проверям что разные проценты чаевых рассчитываются корректно во всех полях и в суммах вместе с СБ не разделяя счёт")
    public void isAllTipsOptionsAreCorrectWithTotalSumAndSC(double totalSum) { //toDo вывести логи сумм в шаги, в текстовый вид отчёта. Переработать вывод
        checkAllTipsOptions(totalSum);
    }

    @Step("Принудительно прячем тап бар, выставляем чаевые 0 и отключаем СБ")
    public void setTipsBy0AndCancelServiceCharge() {

        hideTapBar();

        if (tipsSum.exists()) {

            baseActions.scrollByJS(bodyJS);
            baseActions.click(Tips0);

        }

        if (serviceCharge.exists() && serviceChargeInput.isSelected()) {

            baseActions.scrollByJS(bodyJS);
            baseActions.clickByJS(serviceChargeJS);

        }

    }

    @Step("Проверка что логика установленных чаевых по умолчанию от общей суммы корректна")
    public void isDefaultTipsBySumLogicCorrect() { // toDo не готово еще, доделать и будет меняться логика

        double totalSum = countAllNonPaidDishesInOrder();
        System.out.println(totalSum + " total sum");

        if (totalSum < 196) {

            int totalTipsSumInMiddleNumber = Integer.parseInt(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));

            activeTipsButton.shouldHave(exist);
            Assertions.assertEquals(totalTipsSumInMiddleNumber, 49);
            notActiveTipsPercentOptions.shouldHave(CollectionCondition.size(4));


        } else if (totalSum >= 196 && totalSum < 245) {


            Tips25.shouldHave(cssClass(Tips25.getClass() + " active"));

        } else if (totalSum >= 245 && totalSum <= 326) {


            Tips20.shouldHave(cssClass(Tips25.getClass() + " active"));

        } else if (totalSum > 327 && totalSum < 489) {


            Tips15.shouldHave(cssClass(Tips25.getClass() + " active"));

        } else if (totalSum > 490) {


            Tips10.shouldHave(cssClass(Tips25.getClass() + " active"));
        }


    }

    //  <---------- Actions with dishList ---------->

    @Step("Выбираем все не оплаченные позиции")
    public void chooseAllNonPaidDishes() {

        hideTapBar();
        double totalDishesSum = 0;

        System.out.println(nonPaidAndNonDisabledDishesWhenDivided.size() + " общее число не оплаченных и не заблокированных позиций\n");

        for (int count = 0; count < nonPaidAndNonDisabledDishesWhenDivided.size(); count++) {

            double currentDishPrice = baseActions.convertSelectorTextIntoDoubleByRgx
                    (nonPaidAndNonDisabledDishesSumWhenDivided.get(count), "\\s₽");
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

    @Step("Считаем сумму выбранных рандомных позиций")
    public void chooseCertainAmountDishes(int neededDishesAmount) {

        hideTapBar();
        double totalDishesSum = 0;
        double currentDishPrice;
        String currentDishName;

        System.out.println("\n" + "Количество рандомных позиций для оплаты: " + neededDishesAmount);
        System.out.println(nonPaidAndNonDisabledDishesWhenDivided.size() + " общее число позиций");

        for (int count = 1; count <= neededDishesAmount; count++) {

            int index;
            boolean flag;

            do {

                index = baseActions.generateRandomNumber(1, nonPaidAndNonDisabledDishesWhenDivided.size()) - 1;
                flag = nonPaidAndNonDisabledDishesInputWhenDivided.get(index).isSelected();

                currentDishPrice = baseActions.convertSelectorTextIntoDoubleByRgx
                        (nonPaidAndNonDisabledDishesSumWhenDivided.get(index), "\\s₽");
                currentDishName = nonPaidAndNonDisabledDishesNameWhenDivided.get(index).getText();


            } while (flag);

            totalDishesSum += currentDishPrice;

            scrollAndClick(nonPaidAndNonDisabledDishesNameWhenDivided.get(index));

            nonPaidAndNonDisabledDishesInputWhenDivided.get(index).shouldBe(checked, Duration.ofSeconds(10));
            System.out.println("Блюдо - " + currentDishName +
                    " - " + currentDishPrice +
                    ". Общая цена " + totalDishesSum);

        }



        showTapBar();

    }

    @Step("Считаем сумму всех выбранных позиций при разделении заказа") //
    public double countAllChosenDishes() {

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

        double markedDishesSum = baseActions.convertSelectorTextIntoDoubleByRgx(markedDishes,"\\s₽");

        Assertions.assertEquals(markedDishesSum,totalSumInMenu,0.1);
        System.out.println("Сумма в поле 'Отмеченные позиции' " +
                markedDishesSum + " совпадает с общей чистой суммой заказа " + totalSumInMenu + "\n");


        return totalSumInMenu;

    }

    @Step("Считаем сумму не оплаченных позиций в заказе")
    public double countAllNonPaidDishesInOrder() {

        double totalSumInMenu = 0;
        int counter = 0;
        StringBuilder logs = new StringBuilder();

        System.out.println(allDishesInOrder.size() + " все не оплаченные и не блокированные позиции");

        for (SelenideElement element : allDishesInOrder) {

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

    @Step("Считаем сумму всех оплаченных позиций в заказе")
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

    @Step("Забираем коллекцию заблокированных позиций в заказе для следующего теста")
    public ArrayList<String> countDisabledDishesAndSetCollection() {

        ArrayList<String> chosenDishes = new ArrayList<>();

        for (SelenideElement element : nonPaidDishesWhenDivided) {

            if (!element.$("input+span").getCssValue("background-image").equals("none")) {

                chosenDishes.add(element.$(".dish__checkbox-text").getText());

            }

        }

        return chosenDishes;

    }

    @Step("Проверяем что блюда, которые выбраны ранее, в статусе 'Ожидается'")
    public void dishesAreDisabledInDishList(ArrayList<String> chosenDishes) {

        int successAmount = 0;
        System.out.println(disabledDishes.size() + " disabled dishes size");

        for ( int i = 0; i < chosenDishes.size(); i++) {

            for (int k = 0; k < disabledDishes.size(); k++) {

                if(disabledDishes.get(i).$(".dish__checkbox-text").getText().equals(chosenDishes.get(k))) {
                    /* System.out.println(disabledSharedDishes.get(i).$(".dish__checkbox-text").getText());
                    System.out.println(chosenDishes.get(k)); */
                    successAmount++;
                    break;
                }

            }

        }

        Assertions.assertEquals(chosenDishes.size(),successAmount);

    }

    //  <----------  ---------->

    @Step("Проверяем что имя и сумма перечеркнуты у оплаченных блюд")
    public void isStylesCorrectToPaidDishes() {

        for (SelenideElement element : paidDishesWhenDivided) {

            element.$(".dishList__item-status").shouldHave(text(" Оплачено "));
            element.$(".dish__checkbox-text").shouldHave(cssValue("text-decoration","line-through"));
            element.$(".sum").shouldHave(cssValue("text-decoration","line-through"));

        }

    }

    @Step("Проверяем что имя и сумма перечеркнуты у заблокированных блюд")
    public void isStylesCorrectToDisabledDishes() {

        separateOrderHeading.shouldBe(exist,visible);

        for (SelenideElement element : nonPaidAndNonDisabledDishesWhenDivided) {

            element.$(".dishList__item-status").shouldHave(text(" Оплачивается "));
            element.$(".dishList__item-status img").shouldBe(visible,Duration.ofSeconds(2));
            element.$("input+span").shouldHave(cssValue("background","#dcdee3"));
            element.$("input+span").shouldBe(selected);

        }

    }

    @Step("Проверям что сумма всех блюд совпадает с итоговой без чаевыех и СБ")
    public void isTotalSumInDishesMatchWithTotalPay(double totalSumByDishesInOrder) {

        baseActions.isElementVisibleDuringLongTime(totalPay, 2);
        double totalPaySumInCheck;

        disableServiceChargeIfActivated();

        totalPaySumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");


        System.out.println(totalPaySumInCheck + " общая сумма в 'Итого к оплате'");
        Assertions.assertEquals(totalPaySumInCheck, totalSumByDishesInOrder, 0.01,
                "Сумма за все блюда " + totalSumByDishesInOrder + " совпала с суммой в 'Итого к оплате' "
                        + totalPaySumInCheck);


    }



    @Step("Проверям что сумма 'Итого к оплате' совпадает с суммой счетчика в иконке кошелька")
    public void isSumInWalletMatchWithTotalPay() {

        if (!tabBar.exists()) {

            showTapBar();

            String totalPaySum = baseActions.convertSelectorTextIntoStrByRgx(totalPay, "\\s₽");
            String currentSumInWallet = baseActions.convertSelectorTextIntoStrByRgx(TapBar.totalSumInWalletCounter, "\\s₽");

            Assertions.assertEquals(totalPaySum, currentSumInWallet);

            System.out.println("""
                    Сумма 'Итого к оплате' совпадает с суммой в иконке кошелька
                    """);

        }

    }

    @Step("Проверям что процент установленных по умолчанию чаевых рассчитывается корректно с СБ и без")
    public void isActiveTipPercentCorrectWithTotalSum(double totalSumWithoutTips) {

        if (tipsSum.exists()) {

            baseActions.isElementVisibleDuringLongTime(activeTipsButton, 15);

            double tipPercent = baseActions.convertSelectorTextIntoDoubleByRgx(activeTipsButton, "\\D+");
            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
            double totalSumWithTips = totalSumWithoutTips + (totalSumWithoutTips * (tipPercent / 100));

            System.out.println("\nПроцент чаевых " + tipPercent);
            System.out.println("Сумма в 'Итого к оплате' " + totalPaySum);
            System.out.println("Сумма 'Итого к оплате' вместе с чаевыми " + totalSumWithTips);

            Assertions.assertEquals(totalSumWithTips, totalPaySum, 0.00001);
            System.out.println("Процент скидки установленный по умолчанию совпадает с общей ценой за все блюда без СБ");

        }

    }

    @Step("Проверка что сумма 'Другой пользователь' совпадает с суммой оплаченых позиций")
    public void isAnotherGuestSumCorrect() {

        double totalPaidSum = 0;
        double anotherGuestSum = baseActions.convertSelectorTextIntoDoubleByRgx(TipsAndCheck.anotherGuestSum,"[^\\d\\.]+");

        for (SelenideElement element: disabledAndPainDishesWhenDivided) {

          double currentElementSum = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"),"\\s₽");
          totalPaidSum += currentElementSum;

        }

        Assertions.assertEquals(anotherGuestSum,totalPaidSum, 0.1);
        System.out.println("\nОплаченная сумма другого пользователя " + anotherGuestSum +
                " совпадает с оплаченными позициями " + totalPaidSum +  "\n");

    }


    @Step("Проверка что в форме 'Итого к оплате' ")
    public void isCheckContainerShown() {

        baseActions.isElementVisible(checkContainer);
        baseActions.isElementVisible(totalPay);

        if(tipsContainer.exists()) {

            baseActions.isElementVisible(tipsInCheck);

        }


    }

    @Step("Проверка на ошибку чаевыех при вводе значения меньше установленного минимального")
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

    @Step("Проверка кнопки поделиться счётом, кнопка отображается и вызывает меню шаринга")
    public void isShareButtonShown() {

        baseActions.isElementVisibleAndClickable(shareButton);

        boolean isShareActive = Boolean.TRUE.equals(Selenide.executeJavaScript(isShareButtonCorrect));

        Assertions.assertTrue(isShareActive);

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

        baseActions.scrollByJS(bodyJS);
        baseActions.click(paymentButton);
        System.out.println("clicked on payment button");

    }

    @Step("Отображается лоадер на странице")
    public void isPageLoaderShown() {

        baseActions.isElementVisibleDuringLongTime(pagePreLoader, 6);
        dishesSumChangedHeading.shouldNotBe(exist,Duration.ofSeconds(5));

    }

    @Step("Активация СБ")
    public void clickOnServiceCharge() {

        baseActions.scrollTillBottom();
        baseActions.clickByJS(serviceChargeJS);

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

        baseActions.isElementVisible(callWaiterContainer);
        baseActions.isElementVisibleAndClickable(callWaiterButtonSend);
        baseActions.isElementVisibleAndClickable(callWaiterButtonCancel);
        baseActions.isElementVisible(callWaiterCommentArea);

        baseActions.click(callWaiterButton);
        baseActions.click(callWaiterButtonCancel);
        baseActions.isElementInvisible(callWaiterContainer);

        baseActions.click(callWaiterButton);
        baseActions.sendHumanKeys(callWaiterCommentArea, TEST_WAITER_COMMENT);
        callWaiterCommentArea.shouldHave(value(TEST_WAITER_COMMENT));
        baseActions.click(callWaiterButtonSend);
        baseActions.isElementVisible(successCallWaiterHeading);
        baseActions.click(closeCallWaiterFormInSuccess);
        callWaiterContainer.shouldNot(visible);

        baseActions.scrollByJS(bodyJS);

    }


}


