package support_personal_account.history_operations;

import com.codeborne.selenide.*;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static data.Constants.*;
import static data.Constants.TestData.SupportPersonalAccount.HISTORY_OPERATION_DEFAULT_LIST_ITEM_SIZE;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.OperationsHistory.*;

import static data.selectors.SupportPersonalAccount.Common.historyOperationsCategory;
import static data.selectors.SupportPersonalAccount.HistoryOperations.*;
import static data.selectors.SupportPersonalAccount.HistoryOperations.totalSum;


public class HistoryOperations extends BaseActions {

    private int fromDate;

    @Step("Переход в категорию Истории операций")
    public void goToHistoryOperationsCategory() {

        click(historyOperationsCategory);
        pageHeading.shouldHave(text("История операций"), Duration.ofSeconds(5));
        operationsContainer.shouldBe(visible);

    }

    @Step("Проверка отображения всех элементов в категории Истории операций")
    public void isHistoryOperationsCategoryCorrect() {

        isElementVisible(operationsListTab);
        isElementVisible(stuckTransactionTab);
        isElementVisible(restaurantFilterButton);
        isElementVisible(tableNumberFilterButton);
        isElementVisible(orderStatusFilterButton);
        isElementVisible(waiterFilterButton);

        isElementVisible(dayPeriodButton);
        isElementVisible(monthPeriodButton);
        isElementVisible(customPeriodButton);

        isElementVisible(showOnlyRefundsButton);
        isElementVisible(activePeriodDate);
        isElementVisible(totalSumContainer);
        isElementVisible(totalTipsContainer);



        isElementVisible(operationsItems.first());
        isElementVisible(restaurantName.first());
        isElementVisible(tableNumber.first());
        isElementVisible(orderId.first());
        isElementVisible(dateAndTime.first());
        isElementVisible(orderStatus.first());
        isElementVisible(totalSum.first());
        isElementVisible(orderSum.first());
        isElementVisible(tipsSum.first());
        isElementVisible(serviceCharge.first());
        isElementVisible(waiterName.first());

    }

    public void isStuckOperationsCorrect() {

        click(stuckTransactionTab);
        stuckTransactionTab.shouldBe(enabled);
        preloader.shouldBe(visible).shouldBe(hidden);

        isElementVisible(generalCategory);
        isElementVisible(tipsCategory);
        isElementVisible(totalStuckSum);
        isElementVisible(totalStuckOperationsAmount);
        isElementsCollectionVisible(generalOperationsItem);
        isElementVisible(generalRestaurantName.first());
        isElementVisible(generalTableNumber.first());
        isElementVisible(generalB2tpId.first());
        isElementVisible(generalDateAndTime.first());
        isElementVisible(generalTotalsum.first());
        isElementVisible(generalTips.first());
        isElementVisible(generalServiceCharge.first());

        isElementVisible(loadMoreButton);

        click(tipsCategory);
        preloader.shouldBe(visible).shouldBe(hidden);

        isElementVisible(stuckRestaurantName.first());
        isElementVisible(stuckTableNumber.first());
        isElementVisible(stuckB2tpId.first());
        isElementVisible(stuckDateAndTime.first());
        isElementVisible(stuckTips.first());
        isElementVisible(stuckWaiterName.first());
        stuckComment.first().shouldBe(exist);
        isElementVisible(stuckPushButton);
        isElementVisible(loadMoreButton);

    }

    @Step("Проверка что диапазон в текущий установлен по умолчанию")
    public void isDatePeriodSetByDefault() {

        isElementsCollectionVisible(operationsItems);

        String currentDate = getCurrentDateInFormat(SUPPORT_HISTORY_OPERATIONS_ACTIVE_DATE_FORMAT_PATTERN);
        String adminCurrentDate = activePeriodDate.getText();

        dayPeriodButton.shouldHave(cssValue("background-color","rgba(103, 100, 255, 1)"));

        Assertions.assertEquals(currentDate,adminCurrentDate,"Диапазон в текущий день не выбран по умолчанию");

        String formatDateItems = getCurrentDateInFormat(SUPPORT_HISTORY_OPERATIONS_ITEM_PATTERN);

        operationsItems.filter(matchText(formatDateItems))
                .shouldHave(sizeGreaterThan(0));

    }

    @Step("Проверка что чаевые и суммы к оплате не пустые")
    public void checkTipsAndSumNotEmpty() {

        double sum = Double.parseDouble(totalSumContainer.getText().replaceAll("[^\\d\\.]+",""));
        double tips = Double.parseDouble(totalTipsContainer.getText().replaceAll("[^\\d\\.]+",""));

        Assertions.assertNotEquals(sum,0,"В итого пустое значение");
        Assertions.assertNotEquals(tips,0,"В чаевых пустое значение");

    }

    @Step("Получение периода за неделю")
    public String getDatePeriodForWeek() {

        LocalDate currentDayMinusWeekLocaleDate = LocalDate.now().minusDays(7);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern(SUPPORT_HISTORY_OPERATIONS_ACTIVE_DATE_FORMAT_PATTERN);

        String fromPeriod = currentDayMinusWeekLocaleDate.format(formatters);
        String toPeriod = getCurrentDateInFormat(SUPPORT_HISTORY_OPERATIONS_ACTIVE_DATE_FORMAT_PATTERN);

        return fromPeriod + " - " + toPeriod;

    }

    @Step("Получение периода за месяц")
    public String getDatePeriodForMonth() {

        String fromPeriod = "01." + getCurrentDateInFormat("MM.yyyy");
        String toPeriod = getCurrentDateInFormat(SUPPORT_HISTORY_OPERATIONS_ACTIVE_DATE_FORMAT_PATTERN);
        return fromPeriod + " - " + toPeriod;

    }

    @Step("Сравнение списка операций с датой")
    public void compareOperationListWithDate(String fromPeriodDate, String toPeriodDate,
                                             ElementsCollection elementsCollection) {

        elementsCollection.filter(text(fromPeriodDate)).shouldHave(sizeGreaterThan(0));

        if (paginationPages.first().exists()) {

            click(paginationPages.last());
            operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(30));

            elementsCollection.filter(text(toPeriodDate)).shouldHave(sizeGreaterThan(0));

        }

    }


    @Step("Проверка что период за месяц совпадает")
    public void isMonthPeriodCorrect() {

        isElementsCollectionVisible(operationsItems);

        click(monthPeriodButton);
        monthPeriodButton.shouldHave(cssValue("background-color","rgba(103, 100, 255, 1)"));

        checkTipsAndSumNotEmpty();

    }

    @Step("Конвертируем формат даты из строки в шаблон dd.MM.yyy")
    public String convertDataFormat(String date, String datePattern) throws ParseException {

        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);

        return formatter.format(initDate);

    }

    @Step("Выбор диапазона из календаря")
    public void setCustomPeriod(String month, int fromDateValue, int toDateValue) throws ParseException {

        clickByJs(periodButton);

        do {

            leftArrowMonthPeriod.shouldBe(visible,enabled);
            click(leftArrowMonthPeriod);

        } while(!currentMonth.getText().equals(month));

        SelenideElement fromDate = daysOnMonthPeriod.get(fromDateValue-1);

        String fromPeriodDate =
                convertDataFormat(fromDate.getAttribute("title"),SUPPORT_HISTORY_OPERATIONS_ITEM_PATTERN);

        fromDate.hover().click();

        SelenideElement toDate = daysOnMonthPeriod.get(toDateValue-1);

        String
                toDateForCustomPeriodButton =
                convertDataFormat(toDate.getAttribute("title"),SUPPORT_HISTORY_OPERATIONS_ITEM_PATTERN),
                toDateForOperationsItems =
                convertDataFormat(toDate.getAttribute("title"),SUPPORT_HISTORY_OPERATIONS_ITEM_PATTERN),
                toDateForCurrentDateContainer =
                convertDataFormat(toDate.getAttribute("title"),SUPPORT_HISTORY_OPERATIONS_ACTIVE_DATE_FORMAT_PATTERN);

        toDate.hover();
        click(toDate);

        activePeriodDate.shouldHave(text(toDateForCurrentDateContainer),Duration.ofSeconds(10));
        customPeriodButton.shouldHave(text(fromPeriodDate + " - " + toDateForCustomPeriodButton));

        operationsItems.shouldBe(anyMatch("Должна быть дата в списке",
                element -> element
                                .getText()
                                .replaceAll("(\\n|.)*(\\d{2}\\.\\d{2}\\.\\d{4})(\\n|.)*","$2")
                                .matches(toDateForOperationsItems)));

        isElementVisible(loadMoreButton);
        isElementInvisible(emptyOperationsList);

    }

    public void isRestaurantFilterCorrect(String restaurantName) {

        click(restaurantFilterButton);

        isOpenedRestaurantFilterCorrect();
        isRestaurantBySearchCorrect(restaurantName);
        isOperationListCorrectWithFilters(restaurantName);

        click($(deleteCurrentFilterButton));
        isElementInvisible(restaurantFilterButton.$(deleteCurrentFilterButton));

        click(restaurantFilterButton);
        isOpenedRestaurantFilterCorrect();
        isRestaurantFromDropdownListCorrect(restaurantName);
        isOperationListCorrectWithFilters(restaurantName);

        resetAllFilters();

    }

    @Step("Проверяем все элементы в фильтре Рестораны")
    public void isOpenedRestaurantFilterCorrect() {

        isElementVisible(restaurantFilterContainer);
        isElementVisible(restaurantFilterSearch);
        isElementVisible(filterCloseButton);
        isElementsCollectionVisible(restaurantFilterListItems);

    }

    @Step("Проверяем корректность поиска ресторана")
    public void isRestaurantBySearchCorrect(String restaurantName) {

        sendKeys(restaurantFilterSearchInput,restaurantName);
        click(restaurantFilterListItems.first());
        isAppliedFilter(restaurantFilterContainer,restaurantFilterButton,restaurantName);
        forceWait(WAIT_TILL_OPERATION_HISTORY_LIST_IS_UPDATED); // toDo список операций прогружается с задержкой, не понятно за что зацепиться пока

    }
    @Step("Проверяем корректность выбора ресторана из списка")
    public void isRestaurantFromDropdownListCorrect(String restaurantName) {

        restaurantFilterListItems.find(text(restaurantName)).click();
        isAppliedFilter(restaurantFilterContainer,restaurantFilterButton,restaurantName);

    }
    @Step("Устанавливаем ресторана")
    public void setRestaurantFilter(String restaurantName) {

        click(restaurantFilterButton);
        isRestaurantBySearchCorrect(restaurantName);

    }

    @Step("Устанавливаем стол")
    public void setTableFilter(String restaurantName, String tableNumber) {

        isOpenedTableFilterCorrect();
        isTableFilterBySearchCorrect(tableNumber);
        isOperationListCorrectWithFilters(restaurantName,tableNumber);

    }

    @Step("Устанавливаем статус заказа")
    public void setOrderStatusFilter(String restaurantName, String tableNumber,String orderStatus) {

        isOpenedStatusFilterCorrect();
        choseCertainOptionInOrderStatusFilter(orderStatus);
        isOperationListCorrectWithFilters(restaurantName,tableNumber,orderStatus);

    }

    public void isOperationListCorrectWithFilters(String restaurantName) {

        generalRestaurantName.shouldBe(allMatch("В списке должны быть все операции по этому ресторану",
               element -> element.getText().equals(restaurantName) ),Duration.ofSeconds(6));

    }

    public void isOperationListCorrectWithFilters(String restaurantName, String tableNumber) {

        isOperationListCorrectWithFilters(restaurantName);

        generalTableNumber.shouldBe(allMatch("В списке должны быть все операции по этому столу",
                element -> element.getText().equals(tableNumber) ));

    }

    public void isOperationListCorrectWithFilters(String restaurantName, String tableNumber, String orderStatusText) {

        isOperationListCorrectWithFilters(restaurantName,tableNumber);

        orderStatus.shouldBe(allMatch("В списке должны быть все операции по этому статусу",
                element -> element.getText().contains(orderStatusText) ));

    }

    public void isOperationListCorrectWithFilters(String restaurantName, String tableNumber, String orderStatusText,
                                                  String waiter) {

        isOperationListCorrectWithFilters(restaurantName,tableNumber,orderStatusText);

        waiterName.shouldBe(allMatch("В списке должны быть все операции по этому официанту",
                element -> element.getText().contains(waiter) ));

    }

    @Step("Проверяем работу фильтра Стола")
    public void isTableFilterCorrect(String restaurantName,String tableNumber) {

        setRestaurantFilter(restaurantName);

        setTableFilter(restaurantName, tableNumber);

        resetAllFilters();

    }

    @Step("Проверяем работу фильтра Статус заказа")
    public void isOrderStatusFilterCorrect(String restaurantName,String tableNumber, String orderStatus) {

        setRestaurantFilter(restaurantName);

        setTableFilter(restaurantName, tableNumber);

        setOrderStatusFilter(restaurantName,tableNumber,orderStatus);

        resetAllFilters();

    }



    @Step("Проверяем все элементы в фильтре Столы")
    public void isOpenedTableFilterCorrect() {

        click(tableNumberFilterButton);

        isElementVisible(tableNumberFilterContainer);
        isElementVisible(tableNumberFilterSearch);
        isElementVisible(filterCloseButton);

    }

    @Step("Проверяем корректность поиска ресторана")
    public void isTableFilterBySearchCorrect(String tableNumber) {

        sendKeys(tableNumberFilterSearchInput,tableNumber);
        click(tableNumberFilterApplyButton);
        isAppliedFilter(tableNumberFilterContainer,tableNumberFilterButton,tableNumber);
        forceWait(WAIT_TILL_OPERATION_HISTORY_LIST_IS_UPDATED); // toDo список операций прогружается с задержкой, не понятно за что зацепиться пока

    }

    public void isAppliedFilter(SelenideElement container, SelenideElement element, String textOnFilter) {

        isElementInvisible(container);
        element.shouldHave(text(textOnFilter));
        isElementVisible(element.$(deleteCurrentFilterButton));
        element.shouldHave(cssValue("border-color","rgb(103, 100, 255)"));

    }
    public void isAppliedWaiterFilter() {

        isElementInvisible(waiterFilterContainer);
        isElementVisible(waiterFilterButton.$(deleteCurrentFilterButton));
        waiterFilterButton.shouldHave(cssValue("border-color","rgb(103, 100, 255)"));

    }

    @Step("Сбрасываем фильтры")
    public void resetAllFilters() {

        filterButtons.asDynamicIterable().stream().forEach(element -> {

            if (!Objects.requireNonNull(element.getAttribute("class")).matches(".*disabled")) {

                if(element.$(deleteCurrentFilterButton).exists())
                    element.$(deleteCurrentFilterButton).click();

                element.$(deleteCurrentFilterButton).shouldBe(hidden);

            }

        });

        $$(deleteCurrentFilterButton).shouldHave(size(0));

    }

    public void isOpenedStatusFilterCorrect() {

        click(orderStatusFilterButton);

        isElementVisible(orderStatusFilterContainer);
        isElementVisible(orderStatusFilterOpenButton);
        isElementVisible(orderStatusFilterCloseButton);
        isElementVisible(filterCloseButton);

    }

    public void choseCertainOptionInOrderStatusFilter(String orderStatus) {

        if (orderStatus.contains("Открыт")) {

            click(orderStatusFilterOpenButton);

        } else {

            click(orderStatusFilterCloseButton);

        }

        isAppliedFilter(orderStatusFilterContainer,orderStatusFilterButton,orderStatus);

    }

    @Step("Проверка фильтра Официант")
    public void isWaiterFilterCorrect(String restaurantName,String tableNumber, String orderStatus, String waiterName) {

        setRestaurantFilter(restaurantName);

        setTableFilter(restaurantName, tableNumber);

        setOrderStatusFilter(restaurantName,tableNumber,orderStatus);

        click(waiterFilterButton);

        isOpenedWaiterFilterCorrect();

        isWaiterBySearchCorrect(waiterName);

        isOperationListCorrectWithFilters(restaurantName,tableNumber,orderStatus,waiterName);

        click(deleteFilterButtons.last());
        isElementInvisible(waiterFilterButton.$(deleteCurrentFilterButton));

        click(waiterFilterButton);

        isOpenedWaiterFilterCorrect();

        isWaiterFromDropdownListCorrect(waiterName);

        isOperationListCorrectWithFilters(restaurantName,tableNumber,orderStatus,waiterName);

        resetAllFilters();

    }

    @Step("Проверка списка операций с фильтром ресторана и официанта на определенном столе")
    public void isOperationListCorrectWithCertainWaiterAndTable(String restaurantName,String tableNumber, String orderStatus, String waiterName) {

        setRestaurantFilter(restaurantName);

        click(waiterFilterButton);

        isOpenedWaiterFilterCorrect();

        isWaiterBySearchCorrect(waiterName);

        setTableFilter(restaurantName, tableNumber);

        setOrderStatusFilter(restaurantName,tableNumber,orderStatus);

        isOperationListCorrectWithFilters(restaurantName,tableNumber,orderStatus,waiterName);

        resetAllFilters();

    }

    @Step("Проверка списка операций со всеми фильтрами и диапазоном")
    public void isOperationListCorrectWithAllFiltersAndDataRange
            (String restaurantName,String tableNumber, String orderStatus, String waiterName, String month)
            throws ParseException {

        setRestaurantFilter(restaurantName);

        click(waiterFilterButton);

        isOpenedWaiterFilterCorrect();

        isWaiterBySearchCorrect(waiterName);

        setTableFilter(restaurantName, tableNumber);

        isOpenedStatusFilterCorrect();
        choseCertainOptionInOrderStatusFilter(orderStatus);

        setCustomPeriod(month,1,20);

        isOperationListCorrectWithFilters(restaurantName,tableNumber,orderStatus,waiterName);

    }

    @Step("Проверка кнопки Загрузить еще")
    public void isLoadMoreButtonCorrect(String month) throws ParseException {

        setCustomPeriod(month,1,20);

        operationsItems.shouldHave(size(HISTORY_OPERATION_DEFAULT_LIST_ITEM_SIZE));

        loadMoreButton.shouldBe(visible);
        scrollAndClick(loadMoreButton);

        operationsItems.shouldHave(size(HISTORY_OPERATION_DEFAULT_LIST_ITEM_SIZE * 2));

        loadMoreButton.shouldBe(visible);
        scrollAndClick(loadMoreButton);

        operationsItems.shouldHave(size(HISTORY_OPERATION_DEFAULT_LIST_ITEM_SIZE * 3));

    }

    @Step("Проверка Показать только возвраты")
    public void isShowOnlyRefundsCorrect() {

        click(showOnlyRefundsButton);
        showOnlyRefundsInput.shouldBe(checked);

        if (Objects.requireNonNull(dayPeriodButton.getAttribute("class")).matches(".*active.*"))
            click(dayPeriodButton);

        isElementsCollectionVisible(refundSum);

        operationsItems.shouldHave(allMatch("Все операции должны быть с возвратом",
                element -> element.getText().contains("Сумма возврата") ));

        click(showOnlyRefundsButton);
        showOnlyRefundsInput.shouldNotBe(checked);

        operationsItems.shouldHave(allMatch("Все операции должны быть с возвратом",
                element -> !element.getText().contains("Сумма возврата") ));

    }

    @Step("Проверка открытой истории застрявшей операции")
    public void isOpenedRefundOperationCorrect() {

       click(operationsItems.first());

        operationsItems.first().shouldHave(attributeMatching("class",".*active.*"));

        Assertions.assertEquals(refundSum.first().getText(),openedRefundSum.first().getText(),
                "Сумма возврата не совпадает");

    }



    @Step("Проверка корректности элементов в фильтре Официант")
    public void isOpenedWaiterFilterCorrect() {

        isElementVisible(waiterFilterContainer);
        isElementVisible(waiterFilterSearch);
        isElementsCollectionVisible(waiterFilterItems);
        isElementVisible(filterCloseButton);

    }

    @Step("Проверяем корректность поиска официанта")
    public void isWaiterBySearchCorrect(String waiter) {

        sendKeys(waiterFilterSearchInput,waiter);
        click(waiterFilterItems.first());
        isAppliedWaiterFilter();
        forceWait(WAIT_TILL_OPERATION_HISTORY_LIST_IS_UPDATED); // toDo список операций прогружается с задержкой, не понятно за что зацепиться пока

    }

    @Step("Проверяем корректность выбора официанта из списка")
    public void isWaiterFromDropdownListCorrect(String waiter) {

        waiterFilterItems.find(text(waiter)).click();
        isAppliedWaiterFilter();

    }


}
