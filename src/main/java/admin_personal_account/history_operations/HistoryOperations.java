package admin_personal_account.history_operations;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.SUPPORT_HISTORY_OPERATIONS_ITEM_PATTERN;
import static data.Constants.TestData.AdminPersonalAccount.PERIOD_BUTTON_BORDER;
import static data.Constants.WAIT_FOR_FILE_TO_BE_DOWNLOADED;
import static data.selectors.AdminPersonalAccount.Common;
import static data.selectors.AdminPersonalAccount.OperationsHistory.*;
import static data.selectors.TapperTable.RootPage.Menu.menuDishPhotosBy;


public class HistoryOperations extends BaseActions {
      public static Set<By> ignoredArraySelectorsInHistoryOperations = new HashSet<>(Arrays.asList(
              menuDishPhotosBy,
              totalSumBy,
              totalTipsBy,
              operationsHistoryListItemsDateBy,
              operationsHistoryListItemsWaiterBy,
              operationsHistoryListItemsTableBy,
              operationsHistoryListItemsTipsBy,
              operationsHistoryListItemsStatusBy,
              operationsHistoryListItemsSumBy
      ));

    @Step("Переход в меню история операций")
    public void goToHistoryOperationsCategory() {

        click(Common.operationsHistoryCategory);
        Common.pageHeading.shouldHave(text("История операций"), Duration.ofSeconds(5));
        operationsHistoryContainer.shouldBe(visible);

    }

    @Step("Переход в таб Отчет")
    public void goToReportTab() {

        click(reportTabButton);
        isElementVisible(reportContainer);
        isElementVisible(choseOnlyOneDayCheckbox);
        isElementVisible(fromDateCalendarContainer);
        isElementVisible(toDateCalendarContainer);
        isElementVisible(fromTimeCalendarContainer);
        isElementVisible(toTimeCalendarContainer);
        isElementVisible(reportInfoContainer);
        isElementVisible(downloadReportButton);

        isElementVisibleOnPage(fromDateCalendarContainer);

    }

    @Step("Проверка элементов, выставление диапазона в отчете одного дня")
    public void isOneDayReportCorrect(String month,int day) {

        click(choseOnlyOneDayCheckbox);
        isElementVisible(oneDayReportContainer);

        choseFromDatePeriod(month,day);

    }


    @Step("Выбор диапазона одним днем")
    public void choseFromDatePeriod(String month,int day) {

        clickByJs(fromDateCalendarSelector);
        isElementVisible(openedCalendarContainer);

        if (!currentMonth.getText().equals(month)) {

            do {

                leftArrowMonthPeriod.shouldBe(visible,enabled);
                click(leftArrowMonthPeriod);

            } while(!currentMonth.getText().equals(month));

        }

        click(daysOnMonthPeriod.get(day - 1));
        isElementInvisible(openedCalendarContainer);
        fromDateCalendarInput.shouldNotHave(empty);

    }

    @Step("Выбор диапазона даты")
    public void choseFromAndToDatePeriod(String month,int from, int to) {

        clickByJs(fromDateCalendarSelector);
        isElementVisible(openedCalendarContainer);

        setDate(month,from,fromDateCalendarInput);

        clickByJs(toDateCalendarSelector);
        isElementVisible(openedCalendarContainer);

        setDate(month,to,toDateCalendarInput);

    }

    public void setDate(String month,int day, SelenideElement input) {

        if (!currentMonth.getText().equals(month)) {

            do {

                leftArrowMonthPeriod.shouldBe(visible,enabled);
                click(leftArrowMonthPeriod);

            } while(!currentMonth.getText().equals(month));

        }

        click(daysOnMonthPeriod.get(day - 1));
        isElementInvisible(openedCalendarContainer);
        input.shouldNotHave(empty);

    }


    @Step("Выбор диапазона времени")
    public void choseTimePeriod(int fromHour, int toHour, int fromMin, int toMin) {

        clickByJs(fromTimeCalendarSelector);
        isElementVisible(openedCalendarContainer);

        click(hoursCalendar.get(fromHour));
        click(minutesCalendar.get(fromMin));

        clickByJs(toTimeCalendarSelector);

        click(hoursCalendar.get(toHour));
        click(minutesCalendar.get(toMin));

        fromTimeCalendarContainer.shouldNotHave(empty);
        toTimeCalendarContainer.shouldNotHave(empty);

    }

    @Step("Загружаем отчет")
    public void downloadFile() throws FileNotFoundException {

        downloadReportButton.shouldBe(enabled);

        Assertions.assertNotNull(downloadReportButton.download(WAIT_FOR_FILE_TO_BE_DOWNLOADED),
                "Файл не может быть скачен");

    }



    @Step("Проверка всех элементов")
    public void isHistoryOperationsCorrect() {

        isElementVisible(operationsHistoryContainer);
        isElementVisibleDuringLongTime(historyPeriodDate,20);
        isElementVisible(forWeekPeriodButton);
        isElementVisible(forMonthPeriodButton);
        isElementVisible(totalSum);
        isElementVisible(totalTips);
        isElementVisible(operationsHistoryListContainer);
        isElementsCollectionVisible(operationsHistoryListItems);
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

        operationsHistoryListItemsDate.filter(matchText(currentDate))
                .shouldHave(CollectionCondition.sizeGreaterThan(0));

    }

    @Step("Проверка что чаевые и суммы к оплате не пустые")
    public void checkTipsAndSumNotEmpty() {

       double sum = Double.parseDouble(totalSum.getText().replaceAll("[^\\d\\.]+",""));
       double tips = Double.parseDouble(totalTips.getText().replaceAll("[^\\d\\.]+",""));

       Assertions.assertNotEquals(sum,0,"В итого пустое значение");
       Assertions.assertNotEquals(tips,0,"В чаевых пустое значение");

    }

    @Step("Получение периода за неделю")
    public String getDatePeriodForWeek() {

        LocalDate currentDayMinusWeekLocaleDate = LocalDate.now().minusDays(7);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String fromPeriod = currentDayMinusWeekLocaleDate.format(formatters);
        String toPeriod = getCurrentDateInFormat("dd.MM.yyyy");

        return fromPeriod + " - " + toPeriod;

    }

    @Step("Получение периода за месяц")
    public String getDatePeriodForMonth() {

        String fromPeriod = "01." + getCurrentDateInFormat("MM.yyyy");
        String toPeriod = getCurrentDateInFormat("dd.MM.yyyy");
        return fromPeriod + " - " + toPeriod;

    }

    @Step("Сравнение списка операций с датой")
    public void compareOperationListWithDate(String fromPeriodDate, String toPeriodDate,
                                             ElementsCollection elementsCollection) {

        elementsCollection.filter(text(fromPeriodDate)).shouldHave(CollectionCondition.sizeGreaterThan(0));

        if (paginationPages.first().exists()) {

            click(paginationPages.last());
            operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(30));

            elementsCollection.filter(text(toPeriodDate)).shouldHave(CollectionCondition.sizeGreaterThan(0));


        }

    }

    public void setPeriod(SelenideElement periodElement) {

        click(periodElement);
        operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(20));
        periodElement.shouldHave(cssValue("background-color",PERIOD_BUTTON_BORDER));
        isElementVisible(resetPeriodButton);

    }


    @Step("Проверка что период за неделю совпадает")
    public void isWeekPeriodCorrect() {

        setPeriod(forWeekPeriodButton);

        checkTipsAndSumNotEmpty();

        String periodDate = getDatePeriodForWeek();

        LocalDate currentDayMinusWeekLocaleDate = LocalDate.now().minusDays(7);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern(SUPPORT_HISTORY_OPERATIONS_ITEM_PATTERN);

        String fromPeriodDate = currentDayMinusWeekLocaleDate.format(formatters);
        String toPeriodDate = getCurrentDateInFormat(SUPPORT_HISTORY_OPERATIONS_ITEM_PATTERN);

        historyTotalPeriodDate.shouldHave(text(periodDate));

        compareOperationListWithDate(toPeriodDate, fromPeriodDate, operationsHistoryListItemsDate);

    }



    @Step("Проверка что период за месяц совпадает")
    public void isMonthPeriodCorrect() {

        setPeriod(forMonthPeriodButton);

        checkTipsAndSumNotEmpty();

        String periodDate = getDatePeriodForMonth();

        String fromPeriodDate = getCurrentDateInFormat("MM.yyyy");
        String toPeriodDate = getCurrentDateInFormat("MM.yyyy");

        historyTotalPeriodDate.shouldHave(text(periodDate));

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

    }

    @Step("Выбор диапазона из календаря")
    public void setCustomPeriod() throws ParseException {

        clickByJs(periodButton);

        do {

            leftArrowMonthPeriod.shouldBe(visible,enabled);
            click(leftArrowMonthPeriod);

        } while(!currentMonth.getText().equals("Ноябрь"));

        SelenideElement fromDate = daysOnMonthPeriod.first();
        SelenideElement toDate = daysOnMonthPeriod.last();

        String fromPeriodDate = convertDataFormat(fromDate.getAttribute("title"));
        String toPeriodDate = convertDataFormat(toDate.getAttribute("title"));

        click(fromDate);
        click(toDate);

        operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(40));

        dateRangeContainer.shouldHave(text(fromPeriodDate + " - " + toPeriodDate));

        compareOperationListWithDate(toPeriodDate, fromPeriodDate, operationsHistoryListItemsDate);

    }

    @Step("Проверка периода за который не было операций")
    public void noResultsOperationPeriod() {

        Selenide.executeJavaScript("document.querySelector('" + periodButton +"').click();");

        do {

            leftArrowMonthPeriod.shouldBe(visible,enabled);
            click(leftArrowMonthPeriod);

        } while(!currentMonth.getText().equals("Ноябрь"));

        SelenideElement fromDate = daysOnMonthPeriod.first();
        SelenideElement toDate = daysOnMonthPeriod.last();

        click(fromDate);
        click(toDate);

        operationsHistoryPagePreloader.shouldBe(hidden,Duration.ofSeconds(40));
        isElementVisible(emptyOperationContainer);

        resetPeriodDate();

    }

    @Step("Проверка что после обновления страницы мы остаемся на этой вкладке")
    public void isCorrectAfterRefreshPage() {

        Selenide.refresh();
        isHistoryOperationsCorrect();

    }

    @Step("Конвертируем формат даты из строки в шаблон dd.MM.yyy")
    public String convertDataFormat(String date) throws ParseException {

        Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        return formatter.format(initDate);

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
