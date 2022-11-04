package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import constants.Selectors;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.Assert;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static constants.Constant.JSScripts.isShareButtonCorrect;
import static constants.Constant.TestData.*;
import static constants.Constant.TestDataRKeeperAdmin.*;
import static constants.Selectors.Common.bodyJS;
import static constants.Selectors.Common.pagePreLoader;
import static constants.Selectors.RKeeperAdmin.*;
import static constants.Selectors.RootPage.*;
import static constants.Selectors.RootPage.DishList.*;
import static constants.Selectors.RootPage.PayBlock.*;
import static constants.Selectors.RootPage.TapBar.*;
import static constants.Selectors.RootPage.TipsAndCheck.*;

public class RootPage extends BaseActions {

    BaseActions baseActions = new BaseActions();


    public void openTapperLink(String url) {
        baseActions.openPage(url);
    }

    public void isEmptyOrder() { // toDO убрал проверку на "скоро появится заказ" так как есть баг
        baseActions.isElementVisible(DishList.emptyOrderHeading);
        emptyOrderHeading.shouldHave(text("Заказ успешно оплачен"),Duration.ofSeconds(10));

    }


    @Step("Первичная заставка\\лого\\анимация при открытии страницы")
    public void isStartScreenShown() {
        baseActions.isElementVisibleDuringLongTime(Selectors.Common.startScreenLogo,30);
    }

    public void isTableNumberShown() {
        baseActions.isElementVisible(tableNumber);
    }




    @Step("Кнопка поделиться счётом. Кнопка есть, но не активна, т.к. 1 позиция в заказе")
    @Description("Если в меню один заказ, то кнопка разделить счёт не кликабельна")
    public void isDivideCheckSliderWith1Dish() {

        baseActions.isElementVisible(DishList.divideCheckSlider);
        System.out.println(DishList.allDishesInOrder);
        System.out.println(DishList.allDishesInOrder.size());

        if (DishList.allDishesInOrder.size() == 1) {

            DishList.divideCheckSlider.click();
            DishList.dishesWithSharing.shouldHave(CollectionCondition.size(0));

        }

    }

    @Step("Переключаем на разделение счёта, ждём что все статусы прогружены у позиций")
    public void clickDivideCheckSlider() {

        baseActions.forceWait(2000L);// иначе никак, можно 422 получить если сделать быстро
        baseActions.click(divideCheckSlider);

        orderPositionsSpinners.first().shouldNotHave(visible,Duration.ofSeconds(10));

    }

    public void isDivideSliderShown() {

        baseActions.isElementVisibleDuringLongTime(DishList.divideCheckSlider,15);
        baseActions.click(DishList.divideCheckSlider);
        baseActions.isElementsListVisible(DishList.dishesWithSharing);

        baseActions.click(DishList.divideCheckSlider);
        baseActions.isElementsListInVisible(DishList.dishesWithSharing);
    }

    @Step("Кнопка поделиться счётом. Кнопка не присутствует из-за того что в заказе есть скидки")
    @Description("Не установлено никаких скидок в заказе")
    public void isDivideCheckSliderDisabled() {
        baseActions.isElementInvisible(DishList.divideCheckSlider);
    }

    @Step("Меню корректно отображается")
    public void isDishListNotEmptyAndVisible() {
        DishList.dishListContainerWithDishes.shouldBe(visible, Duration.ofSeconds(30));
    }

    public void isTipsDisabledInAdmin() {

        baseActions.openPage(R_KEEPER_ADMIN_URL);
        baseActions.sendHumanKeys(email, ADMIN_LOGIN_EMAIL);
        baseActions.sendHumanKeys(password,ADMIN_PASSWORD);
        baseActions.click(logInButton);
        baseActions.isElementVisibleDuringLongTime(configuration,5);
        baseActions.click(configuration);
        baseActions.isElementVisibleDuringLongTime(optionTabTips,5);
        baseActions.click(optionTabTips);
        tipsDisabled.shouldHave(checked);

    }


    public void isTipsContainerCorrect() {

        baseActions.isElementVisible(TipsAndCheck.tipsContainer);
        baseActions.isElementVisible(TipsAndCheck.tipsWaiter);
        baseActions.isElementVisible(totalTipsSumInMiddle);
        baseActions.isElementsListVisible(tipsPercentList);

        checkCustomTipForError();


    }

    public void openNewWindow(String url) {

        Selenide.closeWindow();
        Selenide.open(url);





    }


    @Step("Проверям что разные проценты чаевых рассчитываются корректно во всех полях и в суммах вместе с СБ не разделяя счёт")
    public void single_isAllTipsOptionsAreCorrectWithTotalSumWithSC(double totalSum) { //toDo вывести логи сумм в шаги, в текстовый вид отчёта. Переработать вывод

        activateServiceChargeIfDisabled();
        baseActions.scrollByJS(bodyJS);


        for (SelenideElement tipsOption: TipsAndCheck.notDisabledTipsPercentOptions) {


            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class","tips__list-item active"));


            double serviceCharge = baseActions.convertSelectorTextIntoDoubleByRgx(PayBlock.serviceCharge,"[^\\d\\.]+");

            System.out.println(serviceCharge + " before format");

            String formattedDouble = new DecimalFormat("#0.00").format(serviceCharge).replace(",",".");

            double serviceChargeSum = Double.parseDouble(formattedDouble);

            System.out.println(serviceChargeSum + " after format");

            int percent = baseActions.convertSelectorTextIntoIntByRgx(tipsOption,"\\D+");

            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");

            double tipsSumInCheck= baseActions.convertSelectorTextIntoDoubleByRgx(checkTipsSumWithDivide,"\\s₽");

            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));

            double currentSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(TapBar.totalSumInWalletCounter,"\\s₽");

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;

                double cleanTips = totalSum * percentForCalculation;
                cleanTips = Math.round(cleanTips);

                System.out.println(totalSum + " общая сумма за блюда без чаевых и без сервисного сбора");
                System.out.println(cleanTips + " чаевые посчитанные по общей сумме за блюда (чистые)");
                System.out.println(totalSum + cleanTips + " общая сумма за блюда c чаевым, но без сервисного сбора\n");
                System.out.println(serviceChargeSum + " сервисный сбор по текущей проценту чаевых: " + percent + "\n");
                System.out.println(totalSum + cleanTips + serviceChargeSum + " общая сумма за блюда c чаевым и сервисным сбором\n");
                System.out.println(totalPaySum + " общая сумма в графе 'Итого к оплате' c учетом чаевых и сервисного сбора\n");


                Assert.assertEquals(totalTipsSumInMiddle,cleanTips, 0.01);
                System.out.println("total tips sum in middle = clean tips");
                Assert.assertEquals(tipsSumInCheck,cleanTips, 0.01);
                System.out.println("tips sum in check = clean tips");
                Assert.assertEquals(totalSum + cleanTips + serviceChargeSum,totalPaySum, 0.01);
                System.out.println("total sum dishes = totalPaySum + clean tips + service charge");

                baseActions.click(TipsAndCheck.resetTipsButton);

            } else {

                System.out.println(totalSum + " общая сумма за блюда без чаевых и без сервисного сбора");
                System.out.println(totalSum + serviceChargeSum + " общая сумма за блюда c сервисным сбором");
                System.out.println(totalPaySum + " общая сумма в графе 'Итого к оплате' c учетом скидки и чаевых\n");
                System.out.println(serviceChargeSum + " сервисный сбор по текущей проценту чаевых: " + percent);

                Assert.assertEquals(percent,0);
                Assert.assertEquals(totalTipsSumInMiddle, 0, 0.01);
                Assert.assertEquals(tipsSumInCheck,0, 0.01);
                Assert.assertEquals(totalPaySum,currentSumInWallet, 0.01);
                Assert.assertEquals(totalSum+serviceChargeSum,totalPaySum, 0.01);

            }

        }

    }

    @Step("Проверям что разные проценты чаевых рассчитываются корректно во всех полях и в суммах без СБ не разделяя счёт")
    public void single_isAllTipsOptionsAreCorrectWithTotalSumWithoutSC(double totalSum) { //toDo вывести логи сумм в шаги, в текстовый вид отчёта. Переработать вывод.

        disableServiceChargeIfActivated();
        baseActions.scrollByJS(bodyJS);

        for (SelenideElement tipsOption: TipsAndCheck.notDisabledTipsPercentOptions) {

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class","tips__list-item active"));

            int percent = baseActions.convertSelectorTextIntoIntByRgx(tipsOption,"\\D+");
            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");
            double tipsSumInCheck= baseActions.convertSelectorTextIntoDoubleByRgx(checkTipsSumWithDivide,"\\s₽");
            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));
            double currentSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(TapBar.totalSumInWalletCounter,"\\s₽");

            System.out.println("\n" + "Текущий активный процент скидки " + percent);
            System.out.println("Сумма в 'Итого к оплате' " + totalPaySum);
            System.out.println("Чаевые в поле 'Чаевые' " + tipsSumInCheck);
            System.out.println("Чаевые по центру блока " + totalTipsSumInMiddle);
            System.out.println("Сумма в иконке кошелька " + currentSumInWallet + "\n");

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;

                double cleanTips = totalSum * percentForCalculation;
                cleanTips = Math.round(cleanTips);

                System.out.println("Чистые чаевые " + cleanTips);

                Assert.assertEquals(totalTipsSumInMiddle,cleanTips, 0.01);
                System.out.println("total tips sum in middle = clean tips");
                Assert.assertEquals(tipsSumInCheck,cleanTips, 0.01);
                System.out.println("tips sum in check = clean tips");
                Assert.assertEquals(totalSum+cleanTips,totalPaySum, 0.01);
                System.out.println("total sum dishes = totalPaySum + clean tips");

                baseActions.click(TipsAndCheck.resetTipsButton);

            } else {

                Assert.assertEquals(percent,0);
                Assert.assertEquals(totalTipsSumInMiddle, 0, 0.01);
                Assert.assertEquals(tipsSumInCheck,0, 0.01);
                Assert.assertEquals(totalPaySum,currentSumInWallet, 0.01);

            }

        }


    }

    @Step("Проверям что разные проценты чаевых рассчитываются корректно во всех полях и в суммах вместе с СБ при разделении счёта")
    public void divide_isAllTipsOptionsAreCorrectWithTotalSumWithSC(double totalSum) { //toDo вывести логи сумм в шаги, в текстовый вид отчёта. Переработать sout

        activateServiceChargeIfDisabled();
        baseActions.scrollByJS(bodyJS);

        for (SelenideElement tipsOption: TipsAndCheck.notDisabledTipsPercentOptions) {

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class","tips__list-item active"));

            double serviceChargeSum = baseActions.convertSelectorTextIntoDoubleByRgx(PayBlock.serviceCharge,"[^\\d\\.]+");
            serviceChargeSum = Math.floor(serviceChargeSum*100) / 100.0;

            int percent = baseActions.convertSelectorTextIntoIntByRgx(tipsOption,"\\D+");

            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");

            double tipsSumInCheck= baseActions.convertSelectorTextIntoDoubleByRgx(TipsAndCheck.checkTipsSumWithDivide,"\\s₽");

            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));

            double currentSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(TapBar.totalSumInWalletCounter,"\\s₽");

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;

                double cleanTips = totalSum * percentForCalculation;
                cleanTips = Math.round(cleanTips);

                System.out.println(totalSum + " общая сумма за блюда без чаевых и без сервисного сбора");
                System.out.println(cleanTips + " чаевые посчитанные по общей сумме за блюда (чистые)");
                System.out.println(totalSum+cleanTips + " общая сумма за блюда c чаевым, но без сервисного сбора\n");
                System.out.println(serviceChargeSum + " сервисный сбор по текущей проценту чаевых: " + percent + "\n");
                System.out.println(totalSum + cleanTips + serviceChargeSum + " общая сумма за блюда c чаевым и сервисным сбором\n");
                System.out.println(totalPaySum + " общая сумма в графе 'Итого к оплате' c учетом чаевых и сервисного сбора\n");


                Assert.assertEquals(totalTipsSumInMiddle,cleanTips, 0.01);
                System.out.println("total tips sum in middle = clean tips");
                Assert.assertEquals(tipsSumInCheck,cleanTips, 0.01);
                System.out.println("tips sum in check = clean tips");
                Assert.assertEquals(totalSum + cleanTips + serviceChargeSum,totalPaySum, 0.01);
                System.out.println("total sum dishes = totalPaySum + clean tips + service charge");

                baseActions.click(TipsAndCheck.resetTipsButton);

            } else {

                System.out.println(totalSum + " общая сумма за блюда без чаевых и без сервисного сбора");
                System.out.println(totalSum + serviceChargeSum + " общая сумма за блюда c сервисным сбором");
                System.out.println(totalPaySum + " общая сумма в графе 'Итого к оплате' c учетом скидки и чаевых\n");
                System.out.println(serviceChargeSum + " сервисный сбор по текущей проценту чаевых: " + percent);

                Assert.assertEquals(percent,0);
                Assert.assertEquals(totalTipsSumInMiddle, 0, 0.01);
                Assert.assertEquals(tipsSumInCheck,0, 0.01);
                Assert.assertEquals(totalPaySum,currentSumInWallet, 0.01);
                Assert.assertEquals(totalSum + serviceChargeSum,totalPaySum, 0.01);

            }

        }

    }

    @Step("Проверям что разные проценты чаевых рассчитываются корректно во всех полях и в суммах без СБ при разделении счёта")
    public void divide_isAllTipsOptionsAreCorrectWithTotalSumWithoutSC(double totalSum ) { //toDo вывести логи сумм в шаги, в текстовый вид отчёта. Переработать sout.

        disableServiceChargeIfActivated();
        baseActions.scrollByJS(bodyJS);

        for (SelenideElement tipsOption: TipsAndCheck.notDisabledTipsPercentOptions) {

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class","tips__list-item active"));

            int percent = baseActions.convertSelectorTextIntoIntByRgx(tipsOption,"\\D+");
            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");
            double tipsSumInCheck= baseActions.convertSelectorTextIntoDoubleByRgx(TipsAndCheck.checkTipsSumWithDivide,"\\s₽");
            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));
            double currentSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(TapBar.totalSumInWalletCounter,"\\s₽");

            System.out.println("\n" + "Текущий активный процент скидки " + percent);
            System.out.println("Сумма в 'Итого к оплате' " + totalPaySum);
            System.out.println("Чаевые в поле 'Чаевые' " + tipsSumInCheck);
            System.out.println("Чаевые по центру блока " + totalTipsSumInMiddle);
            System.out.println("Сумма в иконке кошелька " + currentSumInWallet + "\n");

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;
                double cleanTips = totalSum * percentForCalculation;
                cleanTips = Math.round(cleanTips);


                System.out.println("Чистые чаевые " + cleanTips);
                Assert.assertEquals(totalTipsSumInMiddle,cleanTips, 0.01);
                System.out.println("Чаевые по середине в чеке равным чистым чаевым");
                Assert.assertEquals(tipsSumInCheck,cleanTips, 0.01);
                System.out.println("Чаевые в 'Итого к оплате' равным чистым чаевым");
                Assert.assertEquals(totalSum+cleanTips,totalPaySum, 0.01);
                System.out.println("Сумма всех позиций с чистыми чаевыми равна сумме в 'Итого к оплате'");

                baseActions.click(TipsAndCheck.resetTipsButton);

            } else {

                Assert.assertEquals(percent,0);
                Assert.assertEquals(totalTipsSumInMiddle, 0, 0.01);
                Assert.assertEquals(tipsSumInCheck,0, 0.01);
                Assert.assertEquals(totalPaySum,currentSumInWallet, 0.01);

            }

        }


    }

    @Step("Принудительно прячем тап бар, выставляем чаевые 0 и отключаем СБ")
    public void cancelTipsAndServiceCharge() {

        hideTapBar();

        if (tipsSum.exists()) {

            baseActions.scrollByJS(bodyJS);
            baseActions.click(Tips0);

        }

        if (serviceChargeInput.exists() && !serviceChargeInput.isSelected()) {


            baseActions.scrollByJS(bodyJS);
            baseActions.clickByJS(serviceChargeJS);
            baseActions.forceWait(1000L); // вынуждено чтобы сумма успела пересчитаться

        }

    }

    @Step("Проверка что логика установленных чаевых по умолчанию от общей суммы корректна")
    public void isDefaultTipsBySumLogicCorrect() { // toDo не готово еще, доделать

        double totalSum = single_countAllDishesInOrder();
        System.out.println(totalSum + " total sum");

        if (totalSum < 196) {

            int totalTipsSumInMiddleNumber = Integer.parseInt(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));

            activeTipsButton.shouldHave(exist);
            Assert.assertEquals(totalTipsSumInMiddleNumber,49);
            notActiveTipsPercentOptions.shouldHave(CollectionCondition.size(4));


        } else if (totalSum >= 196 && totalSum < 245) {


            Tips25.shouldHave(cssClass(TipsAndCheck.Tips25.getClass() + " active"));

        } else if (totalSum >= 245 && totalSum <= 326) {


            TipsAndCheck.Tips20.shouldHave(cssClass(TipsAndCheck.Tips25.getClass() + " active"));

        } else if (totalSum > 327 && totalSum < 489) {


            TipsAndCheck.Tips15.shouldHave(cssClass(TipsAndCheck.Tips25.getClass() + " active"));

        } else if (totalSum > 490) {


            TipsAndCheck.Tips10.shouldHave(cssClass(TipsAndCheck.Tips25.getClass() + " active"));
        }


    }


    public void isTypeCustomTipsAvailable() {




    }


    public void countServiceCharge() { // toDO доделать

        double totalSum = single_countAllDishesInOrder();
        System.out.println(totalSum + " total sum");

        double serviceChargeFromTotalPay = totalSum * (SERVICE_PRICE_PERCENT / 100);
        System.out.println(serviceChargeFromTotalPay + " clear service charge from total pay");


        int tipsCount = Integer.parseInt(Objects.requireNonNull(TipsAndCheck.totalTipsSumInMiddle.getValue()));
        System.out.println(tipsCount + " active tips");

        double serviceChargeFromTips = tipsCount * (SERVICE_PRICE_PERCENT / 100);
        System.out.println(serviceChargeFromTips + " clear service from tips");

        double totalServiceCharge = serviceChargeFromTotalPay + serviceChargeFromTips;
        System.out.println(totalServiceCharge + " total clear service charge");


        double serviceChargeSum = baseActions.convertSelectorTextIntoDoubleByRgx(PayBlock.serviceCharge,"[^\\d\\.]+");
        serviceChargeSum = Math.floor(serviceChargeSum*100) /100.0;



    }

    @Step("Считаем сумму всех выбранных рандомных позиций ({neededDishesAmount})")
    public double chooseCertainAmountDishesAndCountCosts(int neededDishesAmount) {

        hideTapBar();
        double totalDishesSum = 0;
        double currentDishPrice;
        String currentDishName;

        System.out.println("\n" + "Количество рандомных позиций для оплаты: " + neededDishesAmount);

        for (int count = 1; count <= neededDishesAmount; count++) {

            baseActions.forceWait(800L);

            int index;
            boolean flag;
            int totalDishesCount = nonPaidAndNonSharedDishes.size();

            System.out.println(totalDishesCount + " общее число позиций");

            do {

                index = baseActions.generateRandomNumber(1, totalDishesCount) -1;

                System.out.println(index + " generated number");
                System.out.println(nonPaidAndNonSharedDishesInput.get(index).isSelected() + " is selected checkbox?");

                flag = nonPaidAndNonSharedDishesInput.get(index).isSelected();

                System.out.println(flag + " flag");

                currentDishPrice = baseActions.convertSelectorTextIntoDoubleByRgx
                        (nonPaidAndNonSharedDishesSum.get(index),"\\s₽");
                currentDishName = nonPaidAndNonSharedDishesName.get(index).getText();


            } while (flag);

            totalDishesSum += currentDishPrice;

            baseActions.scroll(nonPaidAndNonSharedDishesName.get(index));
            baseActions.click(nonPaidAndNonSharedDishesName.get(index));

            System.out.println("clicked");

            nonPaidAndNonSharedDishesInput.get(index).shouldBe(selected,Duration.ofSeconds(10));

            System.out.println("Блюдо " + currentDishName);
            System.out.println("Цена блюда "+ currentDishPrice);
            System.out.println("Общая цена " + totalDishesSum + "\n");

        }

        showTapBar();
        return totalDishesSum;

    }

    @Step("Считаем сумму всех выбранных рандомных позиций")
    public void getChosenDishesName() { // toDO dodelat

        ElementsCollection gg = $$("input");

        for (SelenideElement element : nonPaidSharedDishes) {

            if (element.$("input").isSelected()) {


            }

        }

    }

    @Step("Считаем сумму всех выбранных позиций при разделении заказа") // toDO не оплаченные позиции без шаринга
    public double divide_countAllChosenDishes() {

        double totalSumInMenu = 0;
        int counter = 0;

        for (SelenideElement element : nonPaidSharedDishes) {

            if(!element.$("input+span").getCssValue("background-image").equals("none")) {

                double cleanPrice = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"),"\\s₽");
                String dishName = element.$(".dish__checkbox-text").getText();

                totalSumInMenu += cleanPrice;
                counter++;

                System.out.println(counter + ". " + dishName + " - " + cleanPrice + " --- цена текущей позиции");
                System.out.println( "Общая сумма: " + totalSumInMenu);

            }

        }

        return totalSumInMenu;

    }

    @Step("Считаем сумму всех позиций")
    public double single_countAllDishesInOrder() {

        double totalSumInMenu = 0;
        int counter = 0;

        for (SelenideElement element : allDishesInOrder) {

            double cleanPrice = baseActions.convertSelectorTextIntoDoubleByRgx(element.$(".sum"),"\\s₽");
            String dishName = element.$(".dish__checkbox-text").getText();

            totalSumInMenu += cleanPrice;
            counter++;

            System.out.println(counter + ". " + dishName + " - " + cleanPrice + " --- цена текущей позиции");
            System.out.println( "Общая сумма: " + totalSumInMenu);

        }
        System.out.println(totalSumInMenu + " total sum in menu");
        return totalSumInMenu;

    }

    @Step("Проверям что сумма всех блюд совпадает с итоговой без чаевыех и СБ")
    public void isTotalSumInDishesMatchWithTotalPay(double totalSumByDishesInOrder) {

        cancelTipsAndServiceCharge();

        baseActions.isElementVisibleDuringLongTime(totalPay,2);
        double totalPaySumInCheck = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");
        System.out.println(totalPaySumInCheck + " total pay sum");
        Assert.assertEquals(totalPaySumInCheck,totalSumByDishesInOrder, 0.01);
        System.out.println("""
                Сумма за все блюда совпала с суммой в 'Итого к оплате'
                """);

    }

    @Step("Проверям что сумма 'Итого к оплате' совпадает с суммой счетчика в иконке кошелька")
    public void isSumInWalletMatchWithTotalPay( ) {

        if (!tabBar.exists()) {

            showTapBar();

            String totalPaySum = baseActions.convertSelectorTextIntoStrByRgx(totalPay,"\\s₽");
            String currentSumInWallet = baseActions.convertSelectorTextIntoStrByRgx(TapBar.totalSumInWalletCounter,"\\s₽");

            System.out.println(totalPaySum + " общая сумма");
            System.out.println(currentSumInWallet + " сумма в кошельке");

            Assert.assertEquals(totalPaySum,currentSumInWallet);

            System.out.println("""
                    Сумма 'Итого к оплате' совпадает с суммой в иконке кошелька
                    """);

        }

    }

    @Step("Проверям что процент установленных по умолчанию чаевых рассчитывается корректно без СБ")
    public void isActiveTipPercentCorrectWithTotalSumWithoutSC(double totalSumWithoutTips) {

        if (TipsAndCheck.tipsSum.exists()) {

            disableServiceChargeIfActivated();

            baseActions.isElementVisibleDuringLongTime(activeTipsButton,10);

            double tipPercent = baseActions.convertSelectorTextIntoDoubleByRgx(TipsAndCheck.activeTipsButton,"\\D+");
            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");
            double totalSumWithTips = totalSumWithoutTips + (totalSumWithoutTips * (tipPercent / 100));

            System.out.println("Процент чаевых " + tipPercent);
            System.out.println("Сумма в 'Итого к оплате' " + totalPaySum);
            System.out.println("Сумма 'Итого к оплате' вместе с чаевыми " + totalSumWithTips + "\n");

            Assert.assertEquals(totalSumWithTips,totalPaySum, 0.00001);

            System.out.println("""
                    Процент скидки установленный по умолчанию совпадает с общей ценой за все блюда без СБ\s
                    """);

        }

    }

    @Step("Проверям что процент установленных по умолчанию чаевыех рассчитывается корректно вместе с СБ")
    public void isActiveTipPercentCorrectWithTotalSumWithSC(double totalSumWithoutTips) {

        if (TipsAndCheck.tipsSum.exists()) {

            activateServiceChargeIfDisabled();

            double tipPercent = baseActions.convertSelectorTextIntoDoubleByRgx(TipsAndCheck.activeTipsButton,"\\D+");
            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");
            double serviceChargeSum = baseActions.convertSelectorTextIntoDoubleByRgx(PayBlock.serviceCharge,"[^\\d\\.]+");
            serviceChargeSum = Math.floor(serviceChargeSum*100) / 100.0;

            double totalSumWithTipsAndAServiceCharge = totalSumWithoutTips + (totalSumWithoutTips * (tipPercent / 100) + serviceChargeSum);

            System.out.println(tipPercent + " --- tips");
            System.out.println(serviceChargeSum + " service charge");
            System.out.println(totalPaySum + " sum in check");
            System.out.println(totalSumWithTipsAndAServiceCharge + " total sum with tips and service charge");

            Assert.assertEquals(totalSumWithTipsAndAServiceCharge,totalPaySum, 0.01);

        }

    }










    @Step("Проверка что в форме 'Итого к оплате' ")
    public void isCheckContainerShown() {

        baseActions.isElementVisible(checkContainer);

        baseActions.isElementVisible(tipsInCheck);
        baseActions.isElementVisible(totalPay);



    }

    @Step("Проверка на ошибку чаевыех при вводе значения меньше установленного минимального")
    public void checkCustomTipForError() {

        String defaultTips = totalTipsSumInMiddle.getValue();
        totalTipsSumInMiddle.clear();
        baseActions.sendHumanKeys(totalTipsSumInMiddle,MIN_SUM_FOR_TIPS_ERROR);
        baseActions.isElementVisible(tipsErrorMsg);
        tipsErrorMsg.shouldHave(text(TIPS_ERROR_MSG));

        totalTipsSumInMiddle.clear();
        baseActions.sendHumanKeys(totalTipsSumInMiddle,defaultTips);
        baseActions.isElementInvisible(tipsErrorMsg);

    }

    @Step("Кнопка 'Оплатить' отображается корректно")
    public void isPaymentButtonShown() {

        baseActions.scrollByJS(bodyJS);
        baseActions.isElementVisibleDuringLongTime(paymentButton,15);

    }

    @Step("Проверка кнопки поделиться счётом, кнопка отображается и вызывает меню шаринга")
    public void isShareButtonShown() {

        baseActions.isElementVisibleAndClickable(shareButton);

        boolean isShareActive = Boolean.TRUE.equals(Selenide.executeJavaScript(isShareButtonCorrect));

        Assert.assertTrue(isShareActive);

    }

    public void isServiceChargeShown() {

        if (PayBlock.serviceChargeInput.exists()) {

            baseActions.isElementVisible(PayBlock.serviceCharge);

        }

    }

    @Step("Проверка политики конфиденциальности")
    public void isConfPolicyShown() { //

        baseActions.scrollByJS(bodyJS);
        baseActions.isElementVisible(confPolicyLink);
        baseActions.click(confPolicyLink);
        confPolicyContainer.shouldHave(cssValue("display","block"));
        confPolicyContent.shouldHave(matchText("УСЛОВИЯ ИСПОЛЬЗОВАНИЯ И ПОЛИТИКА КОНФИДЕНЦИАЛЬНОСТИ TAPPER"));
        Selenide.executeJavaScript("document.querySelector(\".vLandingPoliticModal\").style.display = \"none\";");
        baseActions.isElementInvisible(confPolicyContainer);

    }

    @Step("Клик по кнопке оплаты")
    public void clickOnPaymentButton() {

        baseActions.scrollByJS(bodyJS);
        baseActions.click(paymentButton);

    }

    @Step("Отображается лоадер на странице")
    public void isPageLoaderShown() {
        baseActions.isElementVisibleDuringLongTime(pagePreLoader,6);
    }

    @Step("Активация СБ")
    public void clickOnServiceCharge() {

        baseActions.scrollByJS(bodyJS);
        baseActions.moveMouseToElement(serviceChargeInput);
        baseActions.click(serviceChargeInput);

    }

    @Step("Активируем СБ если не активен")
    public void activateServiceChargeIfDisabled() {

        baseActions.scrollTillBottom();
        System.out.println(serviceChargeInput.isSelected() + " is selected SC?");

        if (serviceCharge.exists() && !serviceChargeInput.isSelected()) {
            System.out.println("clicked and activated service charge");
            baseActions.click(serviceCharge);

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
        return baseActions.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");
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
        baseActions.sendHumanKeys(callWaiterCommentArea,TEST_WAITER_COMMENT);
        TapBar.callWaiterCommentArea.shouldHave(value(TEST_WAITER_COMMENT));
        baseActions.click(callWaiterButtonSend);
        baseActions.isElementVisible(successCallWaiterHeading);
        baseActions.click(closeCallWaiterFormInSuccess);
        callWaiterContainer.shouldNot(visible);


        baseActions.scrollByJS(bodyJS);

    }




}


