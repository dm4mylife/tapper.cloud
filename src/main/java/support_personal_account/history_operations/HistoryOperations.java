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
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.OperationsHistory.*;

import static data.selectors.SupportPersonalAccount.Common.historyOperationsCategory;
import static data.selectors.SupportPersonalAccount.HistoryOperations.*;
import static data.selectors.SupportPersonalAccount.HistoryOperations.totalSum;


public class HistoryOperations extends BaseActions {

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
        isElementVisible(generalComment.first());
        isElementVisible(loadMoreButton);

        click(tipsCategory);

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
    public void setCustomPeriod() throws ParseException {

        clickByJs(periodButton);

        do {

            leftArrowMonthPeriod.shouldBe(visible,enabled);
            click(leftArrowMonthPeriod);

        } while(!currentMonth.getText().equals("Ноябрь"));

        SelenideElement fromDate = daysOnMonthPeriod.first(),
                        toDate = daysOnMonthPeriod.last();

        String fromPeriodDate =
                convertDataFormat(fromDate.getAttribute("title"),SUPPORT_HISTORY_OPERATIONS_CUSTOM_DATE_PATTERN);

        fromDate.hover().click();

        String
                toDateForCustomPeriodButton =
                convertDataFormat(toDate.getAttribute("title"),SUPPORT_HISTORY_OPERATIONS_CUSTOM_DATE_PATTERN),
                toDateForOperationsItems =
                convertDataFormat(toDate.getAttribute("title"),SUPPORT_HISTORY_OPERATIONS_ITEM_PATTERN),
                toDateForCurrentDateContainer =
                convertDataFormat(toDate.getAttribute("title"),SUPPORT_HISTORY_OPERATIONS_ACTIVE_DATE_FORMAT_PATTERN);

        toDate.hover().click();

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
        isOperationListCorrectWitFilters(restaurantName);

        click($(deleteCurrentFilterButton));
        isElementInvisible(restaurantFilterButton.$(deleteCurrentFilterButton));

        click(restaurantFilterButton);
        isOpenedRestaurantFilterCorrect();
        isRestaurantFromDropdownListCorrect(restaurantName);
        isOperationListCorrectWitFilters(restaurantName);

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

    public void isOperationListCorrectWitFilters(String restaurantName) {

        generalRestaurantName.shouldBe(allMatch("В списке должны быть все операции по этому ресторану",
               element -> element.getText().equals(restaurantName) ));

    }

    public void isOperationListCorrectWitFilters(String restaurantName, String tableNumber) {

        isOperationListCorrectWitFilters(restaurantName);

        generalTableNumber.shouldBe(allMatch("В списке должны быть все операции по этому столу",
                element -> element.getText().equals(tableNumber) ));

    }

    @Step("Проверяем работу фильтра Стола")
    public void isTableFilterCorrect(String restaurantName,String tableNumber) {

        setRestaurantFilter(restaurantName);
        click(tableNumberFilterButton);
        isOpenedTableFilterCorrect();
        isTableFilterBySearchCorrect(tableNumber);
        isOperationListCorrectWitFilters(restaurantName,tableNumber);

        resetAllFilters();

    }

    @Step("Проверяем все элементы в фильтре Столы")
    public void isOpenedTableFilterCorrect() {

        isElementVisible(tableNumberFilterContainer);
        isElementVisible(tableNumberFilterSearch);
        isElementVisible(filterCloseButton);

    }

    @Step("Проверяем корректность поиска ресторана")
    public void isTableFilterBySearchCorrect(String tableNumber) {

        sendKeys(tableNumberFilterSearchInput,tableNumber);
        click(tableNumberFilterApplyButton);
        isAppliedFilter(tableNumberFilterContainer,tableNumberFilterButton,tableNumber);

    }

    public void isAppliedFilter(SelenideElement container, SelenideElement element, String textOnFilter) {

        isElementInvisible(container);
        element.shouldHave(text(textOnFilter));
        isElementVisible(element.$(deleteCurrentFilterButton));
        element.shouldHave(cssValue("border-color","rgb(103, 100, 255)"));

    }

    public void resetAllFilters() {

        filterButtons.asDynamicIterable().stream().forEach(element -> {

            System.out.println(element);
            System.out.println(!Objects.requireNonNull(element.getAttribute("class")).matches(".*disabled"));

            if (!Objects.requireNonNull(element.getAttribute("class")).matches(".*disabled")) {

                System.out.println("click in " + element);

                if(element.$(deleteCurrentFilterButton).exists())
                    element.$(deleteCurrentFilterButton).click();

                element.$(deleteCurrentFilterButton).shouldBe(hidden);
                System.out.println("end");

            }

        });
        $$(deleteCurrentFilterButton).shouldHave(size(0));

    }



}
