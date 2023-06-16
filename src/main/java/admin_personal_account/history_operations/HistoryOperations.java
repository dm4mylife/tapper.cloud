package admin_personal_account.history_operations;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import common.BaseActions;
import io.qameta.allure.Step;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.SUPPORT_HISTORY_OPERATIONS_ITEM_PATTERN;
import static data.Constants.TestData.AdminPersonalAccount.PERIOD_BUTTON_BORDER;
import static data.Constants.TIMEOUT_FOR_FILE_TO_BE_DOWNLOADED;
import static data.selectors.AdminPersonalAccount.Common;
import static data.selectors.AdminPersonalAccount.OperationsHistory.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.statisticsTab.dateRangeContainer;


public class HistoryOperations extends BaseActions {


    @Step("Переход в меню история операций")
    public void goToHistoryOperationsCategory() {

        click(Common.operationsHistoryCategory);
        Common.pageHeading.shouldHave(text("История операций"), Duration.ofSeconds(5));
        operationsHistoryContainer.shouldBe(visible);

    }

    @Step("Переход в таб Отчет")
    public void goToReportTab() {


        //operationsHistoryPagePreloader.shouldBe(visible).shouldBe(hidden,Duration.ofSeconds(20));

        //reportTabButton.shouldBe(visible,interactable);

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

    @Step("Переход в таб История операций")
    public void goToHistoryTab() {

        click(historyOperationTabButton);
        isHistoryOperationsCorrect();

    }

    @Step("Проверка элементов, выставление диапазона в отчете одного дня")
    public void isOneDayReportCorrect(String month,int day) {

        goToReportTab();

        click(choseOnlyOneDayCheckbox);
        isElementVisible(oneDayReportContainer);

        choseFromDatePeriod(month,day);

    }

    @Step("Проверка возврата в операции")
    public void isRefundCorrect() throws IOException, ParseException {

        clearDateAndTimePeriod();

        //choseFromAndToDatePeriod("Июнь",9,9);
        readDownloadedFile(downloadFile(),true);

    }


    @Step("Выбор диапазона одним днем")
    public void choseFromDatePeriod(String month,int day) {

        clickByJs(fromDateCalendarSelector);
        isElementVisible(openedCalendarDateContainer);

        if (!currentMonth.getText().equals(month)) {

            do {

                leftArrowMonthPeriod.shouldBe(visible,enabled);
                click(leftArrowMonthPeriod);

            } while(!currentMonth.getText().equals(month));

        }

        click(daysOnMonthPeriod.get(day - 1));
        isElementInvisible(openedCalendarDateContainer);
        fromDateCalendarInput.shouldNotHave(empty);

    }

    @Step("Выбор диапазона даты")
    public void choseFromAndToDatePeriod(Map<String, Object> dateData) {

        clickByJs(fromDateCalendarSelector);
        isElementVisible(openedCalendarDateContainer);

        String month = String.valueOf(dateData.get("monthName"));
        Integer from = (Integer) (dateData.get("startDay"));
        Integer to = (Integer) (dateData.get("endDay"));

        setDate(month,from,fromDateCalendarInput);

        clickByJs(toDateCalendarSelector);
        isElementVisible(openedCalendarDateContainer);

        setDate(month,to,toDateCalendarInput);

    }

    @Step("Выбор периода, когда дата окончания раньше даты начала")
    public void choseNonExistingDate(String month,int from) {

        clickByJs(fromDateCalendarSelector);
        isElementVisible(openedCalendarDateContainer);

        setDate(month,from,fromDateCalendarInput);

        clickByJs(toDateCalendarSelector);
        isElementVisible(openedCalendarDateContainer);

        daysOnMonthPeriod.get(from + 1)
                .shouldHave(attributeMatching("class",".*disabled.*"));

        clearDateAndTimePeriod();

    }

    public void setDate(String month,int day, SelenideElement input) {

        if (!currentMonth.getText().equals(month)) {

            do {

                if (currentMonth.getText().equals(month))
                    break;

                leftArrowMonthPeriod.shouldBe(visible,enabled);
                click(leftArrowMonthPeriod);

            } while(!currentMonth.getText().equals(month));

        }

        click(daysOnMonthPeriod.get(day - 1));
        isElementInvisible(openedCalendarDateContainer);
        input.shouldNotHave(empty);

    }


    @Step("Выбор диапазона времени")
    public void choseOnlyDateFromPeriod(String month, int from) {

        clickByJs(fromDateCalendarSelector);
        isElementVisible(openedCalendarDateContainer);

        setDate(month,from,fromDateCalendarInput);

        downloadReportButton.shouldBe(disabled);

        clearDateAndTimePeriod();

    }

    public void clearDateAndTimePeriod() {

        Selenide.refresh();
        operationsHistoryPagePreloader.shouldBe(visible).shouldBe(hidden,Duration.ofSeconds(20));
        goToReportTab();

    }

    @Step("Выгрузка отчетов за период, заполнено только поле Дата начала")
    public void  choseTimePeriod(int fromHour,int fromMinute, int toHour, int toMinute) {

        String fromTotalValueText = setValueText(fromHour) + ":" + setValueText(fromMinute) ;
        String toTotalValueText = setValueText(toHour) + ":" + setValueText(toMinute) ;

        setTime(fromTimeCalendarInput,fromTimeCalendarSelector,fromHour,fromMinute,fromTotalValueText);
        setTime(toTimeCalendarInput,toTimeCalendarSelector,toHour,toMinute,toTotalValueText);

    }

    public void setTime(SelenideElement input, String inputSelector, int from, int to, String valueText) {

        clickByJs(inputSelector);
        isElementVisible(openedCalendarDateContainer);

        click(hoursCalendar.get(from));
        click(minutesCalendar.get(to));
        click(title);

        isElementInvisible(openedCalendarTimeContainer);

    }

    public String setValueText(int number) {

       return number < 9 ? "0" + number : String.valueOf(number);

    }


    @Step("Загружаем отчет")
    public File downloadFile() throws FileNotFoundException {

        return downloadReportButton.shouldBe(enabled).download(TIMEOUT_FOR_FILE_TO_BE_DOWNLOADED);

    }

    public LinkedHashMap<Integer, Map<String, String>> readDownloadedFile(File file,boolean hasRefund) throws IOException, ParseException {

        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        Sheet sheet = hasRefund ? workbook.getSheetAt(1) : workbook.getSheetAt(0);
        LinkedHashMap<Integer, Map<String, String>> data = new LinkedHashMap<>();

        if (!hasRefund) {

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                LinkedHashMap<String, String> tempData = new LinkedHashMap<>();

                String nonFormatDateAndTime = row.getCell(1).getStringCellValue();
                String date = nonFormatDateAndTime.replaceAll("(.*)\\s.*","$1");
                date = convertDataFormat(date);

                String time = nonFormatDateAndTime.replaceAll(".*\\s(.*)","$1");
                String convertedDateAndTime = date + " " + time;

                String waiter =  row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";
                String table =  convertDbToStrRemoveTrailingZeros(row.getCell(3).getNumericCellValue());
                String tips = convertDbToStrRemoveTrailingZeros(row.getCell(4).getNumericCellValue());
                String totalOrderSum = convertDbToStrRemoveTrailingZeros(row.getCell(5).getNumericCellValue());

                tempData.put("dateAndTime", convertedDateAndTime);
                tempData.put("waiter", waiter);
                tempData.put("table", table);
                tempData.put("tips", tips);
                tempData.put("totalOrderSum", totalOrderSum);
                data.put(i,tempData);

            }

        } else {

            //toDo дописать проверку на возвраты когда будет апи чтобы их толкать

        }




        return data;

    }

    public String convertDbToStrRemoveTrailingZeros(double db) {

        DecimalFormat df = new DecimalFormat("#.###");
        String strNum = df.format(db).replace(",", ".");

        return removeTrailingZeros(strNum);

    }

    public static String removeTrailingZeros(String str) {
        if (str.contains(".")) {
            while (str.endsWith("0")) {
                str = str.substring(0, str.length() - 1);
            }
            if (str.endsWith(".")) {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    public LinkedHashMap<Integer, Map<String, String>> collectOperationsData() {

        LinkedHashMap<Integer, Map<String, String>> data = new LinkedHashMap<>();
        final int[] counter = {1};

        isElementsCollectionVisible(operationsHistoryListItems);

        operationsHistoryListItems.asFixedIterable().stream().forEach(element -> {

            LinkedHashMap<String, String> tempData = new LinkedHashMap<>();

            String nonFormatDateAndTime = element.$(operationsHistoryListItemsDateSelector).getText();
            String date = nonFormatDateAndTime.replaceAll("(.*)\\s.*","$1");
            String time = nonFormatDateAndTime.replaceAll(".*\\s(.*)","$1");


            String convertedDateAndTime = date + " " + time;
            String waiter = element.$(operationsHistoryListItemsWaiterSelector).getText();
            String table = element.$(operationsHistoryListItemsTableSelector).getText();
            String tips = element.$(operationsHistoryListItemsTipsSelector).getText()
                    .replaceAll("\\D+","");
            String totalOrderSum = element.$(operationsHistoryListItemsTotalOrderSumSelector).getText()
                    .replaceAll("[а-яА-Я\\s₽]+","");

            tempData.put("dateAndTime", convertedDateAndTime);
            tempData.put("waiter", waiter);
            tempData.put("table", table);
            tempData.put("tips", tips);
            tempData.put("totalOrderSum", totalOrderSum);
            data.put(counter[0],tempData);

            counter[0]++;

        });


        return data;

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
    public void compareOperationListWithDate(String fromPeriodDate, String toPeriodDate) {

        /*System.out.println(fromPeriodDate + "fromPeriodDate" +toPeriodDate + " toPeriodDate");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime startDate = LocalDateTime.parse(fromPeriodDate, formatter);
        LocalDateTime endDate = LocalDateTime.parse(toPeriodDate,formatter);

        findMatchInDateRange(startDate,endDate);

        if (paginationPages.first().exists()) {

            click(paginationPages.last());
            operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(30));

            findMatchInDateRange(startDate,endDate);

        }*/

    }

    public void findMatchInDateRange(LocalDateTime startDate,LocalDateTime endDate) {

        boolean allHasMatch = false;

        for (SelenideElement element: operationsHistoryListItemsDate) {

            LocalDateTime dateTime = LocalDateTime.parse(element.getText(),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            if ((dateTime.isAfter(startDate) || dateTime.isEqual(startDate)) &&
                    dateTime.isBefore(endDate) || dateTime.isEqual(endDate)) {

                allHasMatch = true;

            } else {

                allHasMatch = false;
                break;

            }

        }

        Assertions.assertTrue(allHasMatch,"Все операции отображаются по указанному диапазону");


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

        compareOperationListWithDate(toPeriodDate, fromPeriodDate);

    }

    @Step("Проверка что период за месяц совпадает")
    public void isMonthPeriodCorrect() {

        setPeriod(forMonthPeriodButton);

        checkTipsAndSumNotEmpty();

        String periodDate = getDatePeriodForMonth();

        String fromPeriodDate = getCurrentDateInFormat("MM.yyyy");
        String toPeriodDate = getCurrentDateInFormat("MM.yyyy");

        historyTotalPeriodDate.shouldHave(text(periodDate));

        compareOperationListWithDate(fromPeriodDate, toPeriodDate);

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
    public void setCustomPeriodInAdminHistoryOperations(String month, int from, int to) throws ParseException {

        clickByJs(periodButton);

        do {

            if (currentMonth.getText().equals(month))
                break;

            leftArrowMonthPeriod.shouldBe(visible,enabled);
            click(leftArrowMonthPeriod);

        } while(!currentMonth.getText().equals(month));

        SelenideElement fromDate = daysOnMonthPeriod.get(from - 1);
        SelenideElement toDate = daysOnMonthPeriod.get(to - 1);

        String fromPeriodDate = convertDataFormat(fromDate.getAttribute("title"));
        String toPeriodDate = convertDataFormat(toDate.getAttribute("title"));

        click(fromDate);
        click(toDate);

        operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(40));

        historyTotalPeriodDate.shouldHave(text(fromPeriodDate + " - " + toPeriodDate));

    }

    @Step("Выбор диапазона из календаря")
    public void setCustomPeriodInReport(Map<String,Object> dateDate) {

        clickByJs(periodButton);

        String month = String.valueOf(dateDate.get("monthName"));
        String from = String.valueOf(dateDate.get("startDay"));
        String to = String.valueOf(dateDate.get("endDay"));

        System.out.println(month + " month");

        do {

            if (currentMonth.getText().equals(month))
                break;

            leftArrowMonthPeriod.shouldBe(visible,enabled);
            click(leftArrowMonthPeriod);

        } while(!currentMonth.getText().equals(month));

        SelenideElement fromDate = daysOnMonthPeriod.find(text(from));
        SelenideElement toDate = daysOnMonthPeriod.find(text(to));

        String fromPeriodDate = getCurrentDateInFormat("MM.yyyy");
        String toPeriodDate = getCurrentDateInFormat("MM.yyyy");

        click(fromDate);
        click(toDate);

        operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(40));

       // historyPeriodDate.shouldHave(text(fromPeriodDate + " - " + toPeriodDate));

    }





    @Step("Выбор диапазона из календаря")
    public void setAvailablePeriodForReport(Map<String,Object> dateDate) throws ParseException {

        clickByJs(periodButton);

        String month = String.valueOf(dateDate.get("monthName"));
        String from = String.valueOf(dateDate.get("startDay"));
        String to = String.valueOf(dateDate.get("endDay"));

        System.out.println(from + " fromDate");
        System.out.println(to + " toDate");

        click(daysOnMonthPeriod.find(text(from)));
        click(daysOnMonthPeriod.find(text(to)));

        operationsHistoryPagePreloader.shouldNotBe(visible,Duration.ofSeconds(40));

        // dateRangeContainer.shouldHave(text(fromPeriodDate + " - " + toPeriodDate));

    }

    public  Map<String,Object> getDateData() {

        LocalDate currentDate = LocalDate.now();
        int previousDay = currentDate.minusDays(1).getDayOfMonth();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonthIndex = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        String[] monthNames = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

        HashMap<String, Object> map = new HashMap<>();
        map.put("startDay", previousDay);
        map.put("endDay", currentDay);
        map.put("monthIndex", currentMonthIndex);
        map.put("year", currentYear);
        map.put("monthName", monthNames[currentMonthIndex - 1]);

        System.out.println(map);

        return map;

    }

    public static int getMonthIndex(String monthName) {
        Month month = Month.valueOf(monthName.toUpperCase(new Locale("ru")));

        return month.getValue();
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

    public void matchOperationsData(LinkedHashMap<Integer, Map<String, String>> adminOperationsHistoryData,
                                    LinkedHashMap<Integer, Map<String, String>> xlsxData) {

        String nonExistingOperation = null;

        boolean allValuesMatch = true;
        for (Object value : adminOperationsHistoryData.values()) {
            if (!xlsxData.containsValue(value)) {
                allValuesMatch = false;
                nonExistingOperation = String.valueOf(value);
                break;
            }

        }

        Assertions.assertTrue(allValuesMatch, "В отчете нет операции, которая есть в админской части " +
                nonExistingOperation);

    }

    public void isDateHasInDateRange(LinkedHashMap<Integer, Map<String, String >> xlsxData,int year, int monthFrom,int monthTo,
                                        int dayFrom, int dayTo,int hourFrom, int hourTo, int minuteFrom, int minuteTo) {

        boolean allHasMatch = false;
        LocalDateTime startDate = LocalDateTime.of(year, monthFrom, dayFrom, hourFrom, minuteFrom);
        LocalDateTime endDate = LocalDateTime.of(year, monthTo, dayTo, hourTo, minuteTo);

        for (Map.Entry<Integer, Map<String, String >> entry : xlsxData.entrySet()) {

            Map<String, String > innerMap =  entry.getValue();
            LocalDateTime dateTime = LocalDateTime.parse(innerMap.get("dateAndTime"),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

            if ((dateTime.isAfter(startDate) || dateTime.isEqual(startDate)) &&
                    dateTime.isBefore(endDate) || dateTime.isEqual(endDate)) {

                allHasMatch = true;

            } else {

                allHasMatch = false;
                break;

            }

        }

        Assertions.assertTrue(allHasMatch,"Все операции отображаются по указанному диапазону");

    }

    public void isDateHasInDateRange(LinkedHashMap<Integer, Map<String, String>> xlsxData,int year, int monthFrom,int monthTo,
                                     int dayFrom, int dayTo) {

        boolean allHasMatch = false;
        LocalDateTime startDate = LocalDateTime.of(year, monthFrom, dayFrom, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, monthTo, dayTo, 23, 59);

        for (Map.Entry<Integer, Map<String, String >> entry : xlsxData.entrySet()) {

            Map<String, String > innerMap = entry.getValue();
            LocalDateTime dateTime = LocalDateTime.parse(innerMap.get("dateAndTime"),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

            if ((dateTime.isAfter(startDate) || dateTime.isEqual(startDate)) &&
                    dateTime.isBefore(endDate) || dateTime.isEqual(endDate)) {

                allHasMatch = true;

            } else {

                allHasMatch = false;
                break;

            }

        }


        Assertions.assertTrue(allHasMatch,"Все операции отображаются по указанному диапазону");

    }


}
