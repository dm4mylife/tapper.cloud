package admin_personal_account.operations_history;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import static com.codeborne.selenide.Condition.*;
import static data.selectors.AdminPersonalAccount.Common;
import static data.selectors.AdminPersonalAccount.OperationsHistory.*;


public class OperationsHistory extends BaseActions {

    @Step("Переход в меню история операций")
    public void goToOperationsHistoryCategory() {

        click(Common.operationsHistoryCategory);
        Common.pageHeading.shouldHave(text("История операций"), Duration.ofSeconds(5));
        operationsHistoryContainer.shouldBe(visible);


    }

    @Step("Проверка всех элементов")
    public void isHistoryOperationsCorrect() {

        isElementVisible(operationsHistoryContainer);
        isElementVisibleDuringLongTime(historyPeriodDate,10);
        isElementVisible(forWeekPeriodButton);
        isElementVisible(forMonthPeriodButton);
        isElementVisible(totalSum);
        isElementVisible(totalTips);
        isElementVisible(operationsHistoryListContainer);
        isElementsListVisible(operationsHistoryListItems);
        isElementsListVisible(operationsHistoryListItemsWaiter);
        isElementsListVisible(operationsHistoryListItemsTable);
        isElementsListVisible(operationsHistoryListItemsTips);
        isElementsListVisible(operationsHistoryListItemsStatus);
        isElementsListVisible(operationsHistoryListItemsSum);
        isElementVisible(paginationContainer);
        isElementsListVisible(paginationPages);

    }

    @Step("Проверка что диапазон в текущий установлен по умолчанию")
    public void isDatePeriodSetByDefault() {

        String currentDate = getCurrentDateInFormat("dd.MM.yyyy");

        String adminCurrentDate = historyPeriodDate.getText();

        Assertions.assertEquals(currentDate,adminCurrentDate,"Диапазон в текущий день не выбран по умолчанию");
        System.out.println("Диапазон в текущий день выбран по умолчанию");

        operationsHistoryListItemsDate.filter(matchText(currentDate)).shouldHave(CollectionCondition.sizeGreaterThan(0));
        System.out.println("В списке операций все операции текущим днём");


    }

    @Step("Проверка что чаевые и суммы к оплате не пустые")
    public void checkTipsAndSumNotEmpty() {

       double sum = Double.parseDouble(totalSum.getText().replaceAll("[^\\d\\.]+",""));
       System.out.println(sum + " сумма");

       double tips = Double.parseDouble(totalTips.getText().replaceAll("[^\\d\\.]+",""));
       System.out.println(tips + " чаевые");

       Assertions.assertNotEquals(sum,0,"В итого пустое значение");
       Assertions.assertNotEquals(tips,0,"В чаевых пустое значение");
       System.out.println("В итого и чаевых не пустые суммы");

    }

    @Step("Получение периода за неделю")
    public String getDatePeriodForWeek() {

        LocalDate currentDayMinusWeekLocaleDate = LocalDate.now().minusDays(7);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String fromPeriod = currentDayMinusWeekLocaleDate.format(formatters);
        String toPeriod = getCurrentDateInFormat("dd.MM.yyyy");

        String concatenatedDate = fromPeriod + " - " + toPeriod;
        System.out.println(concatenatedDate);

        return concatenatedDate;

    }


    @Step("Получение периода за месяц")
    public String getDatePeriodForMonth() {

        String fromPeriod = "01." + getCurrentDateInFormat("MM.yyyy");
        String toPeriod = getCurrentDateInFormat("dd.MM.yyyy");

        String concatenatedDate = fromPeriod + " - " + toPeriod;
        System.out.println(concatenatedDate);

        return concatenatedDate;

    }


    @Step("Сравнение списка операций с датой")
    public void compareOperationListWithDate(String fromPeriodDate, String toPeriodDate, ElementsCollection elementsCollection) {

        elementsCollection.filter(text(fromPeriodDate)).shouldHave(CollectionCondition.sizeGreaterThan(0));
        System.out.println("В списке операции есть операции по начальной дате");

        if (paginationPages.first().exists()) {

            paginationPages.last().click();
            operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(20));

            elementsCollection.filter(text(toPeriodDate)).shouldHave(CollectionCondition.sizeGreaterThan(0));
            System.out.println("В списке операции есть операции по конечной дате");

        }

    }

    @Step("Проверка что период за неделю совпадает")
    public void isWeekPeriodCorrect() {

        click(forWeekPeriodButton);
        operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(20));
        forWeekPeriodButton.shouldHave(cssValue("background-color","rgba(103, 100, 255, 1)"));
        isElementVisible(resetPeriodButton);

        checkTipsAndSumNotEmpty();

        String periodDate = getDatePeriodForWeek();

        LocalDate currentDayMinusWeekLocaleDate = LocalDate.now().minusDays(7);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String fromPeriodDate = currentDayMinusWeekLocaleDate.format(formatters);
        String toPeriodDate = getCurrentDateInFormat("dd.MM.yyyy");

        historyTotalPeriodDate.shouldHave(text(periodDate));
        System.out.println("Недельный фильтр работает корректно");

        compareOperationListWithDate(toPeriodDate, fromPeriodDate, operationsHistoryListItemsDate);


    }

    @Step("Проверка что период за месяц совпадает")
    public void isMonthPeriodCorrect() {

        click(forMonthPeriodButton);
        operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(20));
        forMonthPeriodButton.shouldHave(cssValue("background-color","rgba(103, 100, 255, 1)"));
        isElementVisible(resetPeriodButton);

        checkTipsAndSumNotEmpty();

        String periodDate = getDatePeriodForMonth();

        String fromPeriodDate = getCurrentDateInFormat("MM.yyyy");
        String toPeriodDate = getCurrentDateInFormat("MM.yyyy");

        historyTotalPeriodDate.shouldHave(text(periodDate));
        System.out.println("Месячный фильтр работает корректно");

        compareOperationListWithDate(toPeriodDate, fromPeriodDate, operationsHistoryListItemsDate);

    }

    @Step("Сброс диапазона")
    public void resetPeriodDate() {

        if (resetPeriodButton.exists()) {

            click(resetPeriodButton);

        }

        operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(20));

        allPeriodButtons.filter(cssValue("background-color","rgba(0, 0, 0, 0)"))
                .shouldHave(CollectionCondition.size(3));

        isElementInvisible(resetPeriodButton);

        String periodDate = getCurrentDateInFormat("dd.MM.yyyy");

        historyPeriodDate.shouldHave(text(periodDate));
        System.out.println("Сброс фильтра сработал корректно");

    }

    @Step("Выбор диапазона из календаря")
    public void setCustomPeriod() throws ParseException {

        Selenide.executeJavaScript("document.querySelector('" + periodButton +"').click();");

        System.out.println(currentMonth.getText());

        do {

            forceWait(300);
            click(leftArrowMonthPeriod);

        } while(!currentMonth.getText().equals("Ноябрь"));

        SelenideElement fromDate = daysOnMonthPeriod.first();
        SelenideElement toDate = daysOnMonthPeriod.last();

        String fromPeriodDate = convertDataFormat(fromDate.getAttribute("title"));
        String toPeriodDate = convertDataFormat(toDate.getAttribute("title"));

        click(fromDate);
        click(toDate);
        operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(30));

        dateRangeContainer.shouldHave(text(fromPeriodDate + " - " + toPeriodDate));
        System.out.println("Кастомный формат установился корректно");

        compareOperationListWithDate(toPeriodDate, fromPeriodDate, operationsHistoryListItemsDate);

    }

    @Step("Конвертируем формат даты из строки в шаблон dd.MM.yyy")
    public String convertDataFormat(String date) throws ParseException {

        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String parsedDate = formatter.format(initDate);
        System.out.println(parsedDate);

        return parsedDate;

    }

    @Step("Извлечение данных по заказу в коллекцию")
    public HashMap<Integer, HashMap<String,String>> saveAdminOrderData() {

        HashMap<Integer, HashMap<String,String>> adminOrderData = new HashMap<>();

        for (int operationIndex = 0; operationIndex < operationsHistoryListItems.size(); operationIndex++) {

            HashMap<String, String> temporaryHashMap = new HashMap<>();

            String date = operationsHistoryListItemsDate.get(operationIndex).getText();
            String name = operationsHistoryListItemsWaiter.get(operationIndex).getText();
            String table = operationsHistoryListItemsTable.get(operationIndex)
                    .getText().replaceAll("Стол ","");
            String tips = operationsHistoryListItemsTips.get(operationIndex)
                    .getText().replaceAll("[^\\d\\.]+","");
            String totalSum = operationsHistoryListItemsSum.get(operationIndex)
                    .getText().replaceAll("[^\\d\\.]+","");

            temporaryHashMap.put("date", date);
            temporaryHashMap.put("name", name);
            temporaryHashMap.put("table", table);
            temporaryHashMap.put("tips", tips);
            temporaryHashMap.put("totalSum", totalSum);

            adminOrderData.put(operationIndex, temporaryHashMap);

        }

        return adminOrderData;

    }



}
