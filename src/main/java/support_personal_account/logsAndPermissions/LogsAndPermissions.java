package support_personal_account.logsAndPermissions;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import data.selectors.AdminPersonalAccount;
import data.selectors.SupportPersonalAccount;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static data.Constants.WAIT_FOR_FILE_TO_BE_DOWNLOADED;
import static data.Constants.TestData.SupportPersonalAccount.RESTAURANT_NAME;
import static data.Constants.WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.OperationsHistory.paginationPages;
import static data.selectors.AdminPersonalAccount.OperationsHistory.*;
import static data.selectors.AdminPersonalAccount.TableAndQrCodes.*;
import static data.selectors.SupportPersonalAccount.Common.pagePreloader;
import static data.selectors.SupportPersonalAccount.Common.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.Common.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.acquiringTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.cashDesksTab.cashDeskTab;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.customizationTab.customizationTab;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.customizationTab.vtbLink;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.licenseTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.loaderTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.logsTab.logsTab;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.operationsTab.operationStatus;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.operationsTab.operationsTab;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.permissionsTab.permissionsTab;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.statisticsTab.dateRangeContainer;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.statisticsTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.tablesTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.tipsTab.saveButton;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.tipsTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.waitersTab.waitersTab;


public class LogsAndPermissions extends BaseActions {

    @Step("Переход в меню профиля")
    public void goToLogsAndPermissionsCategory() {

        click(logsAndPermissionsCategory);
        pageHeading.shouldHave(text("Логи/доступы"), Duration.ofSeconds(5));
        logsContainer.shouldBe(visible);
        isLogsAndPermissionsCategoryCorrect();

    }

    @Step("Выбор тестового ресторана")
    public void chooseRestaurant(String restaurantName) {

        click(expandLeftMenuButton);
        isElementVisible(openedLeftMenuContainer);
        click(logsAndPermissionsCategoryDropdownButton);
        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
        searchRestaurantInput.sendKeys(restaurantName);
        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
        click(searchResultList.first());

        pagePreloader.shouldNotHave(attributeMatching("style", "background: transparent;")
                , Duration.ofSeconds(10));

        click(collapseLeftMenuButton);
        isElementInvisible(openedLeftMenuContainer);
        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
        currentChosenRestaurant.shouldHave(text(RESTAURANT_NAME));

    }

    @Step("Проверка что все элементы в Логи/доступы корректны")
    public void isLogsAndPermissionsCategoryCorrect() {

        isElementVisible(currentChosenRestaurant);
        isElementVisible(searchInput);
        isElementVisible(choseDateRange);
        isElementVisibleAndClickable(clearCashTableButton);
        isElementVisibleAndClickable(clearAllCashServiceButton);

        isElementVisible(logsTab);
        isElementVisible(permissionsTab);
        isElementVisible(licenseIdTab);
        isElementVisible(cashDeskTab);
        isElementVisible(operationsTab);
        isElementVisible(acquiringTab);
        isElementVisible(loaderTab);
        isElementVisible(statisticsTab);
        isElementVisible(tipsTab);
        isElementVisible(customizationTab);
        isElementVisible(waitersTab);
        isElementVisible(tablesTab);

    }

    @Step("Проверка отображения элементов во вкладке Логи")
    public void isLogsTabCorrect() {

        click(logsTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

    }

    @Step("Проверка отображения элементов во вкладке Доступы")
    public void isPermissionsTabCorrect() {

        click(permissionsTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

    }

    @Step("Проверка отображения элементов во вкладке id лицензии")
    public void isLicenseIdTabCorrect() {

        click(licenseIdTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

        isElementVisible(xmlApplicationButton);
        isElementVisible(xmlOrderSaveButton);
        isElementVisible(licenseDateInput);
        isElementVisibleAndClickable(getIdLicenseButton);

        licenseIdInput.shouldNotHave(empty);
        licenseDateInput.shouldNotHave(empty);

    }

    @Step("Выбираем вид лицензии XML интерфейс для приложения")
    public void choseXmlApplicationOption() {

        isElementVisible(xmlApplicationButton);
        isElementVisible(xmlOrderSaveButton);

        click(xmlApplicationButton);

        isElementVisible(SupportPersonalAccount.LogsAndPermissions.licenseTab.saveButton);

        isElementVisible(licenseIdInput);
        isElementVisible(licenseDateInput);
        isElementVisibleAndClickable(getIdLicenseButton);

        click(getIdLicenseButton);

        licenseIdInput.shouldNotHave(empty);
        licenseDateInput.shouldNotHave(empty);

    }

    @Step("Выбираем вид лицензии XML сохранение заказов")
    public void choseXmlSaveOrderOption() {

        isElementVisible(xmlApplicationButton);
        isElementVisible(xmlOrderSaveButton);

        click(xmlOrderSaveButton);

        isElementVisible(SupportPersonalAccount.LogsAndPermissions.licenseTab.saveButton);

        isElementVisible(licenseDateInput);
        isElementVisibleAndClickable(getIdLicenseButton);

        click(getIdLicenseButton);

        licenseDateInput.shouldNotHave(empty);

    }

    @Step("Проверка отображения элементов во вкладке r-keeper/iiko")
    public void isCashDeskTabCorrect() {

        click(cashDeskTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

    }

    @Step("Проверка отображения элементов во вкладке Операции")
    public void isOperationsTabCorrect() {

        click(operationsTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

        isElementVisible(operationsHistoryContainer);
        isElementVisible(forWeekPeriodButton);
        isElementVisible(forMonthPeriodButton);
        isElementVisible(AdminPersonalAccount.OperationsHistory.dateRangeContainer);
        isElementVisible(operationStatus);
        isElementVisible(historyPeriodDate);
        isElementVisible(totalSum);
        isElementVisible(totalTips);
        isElementVisible(operationsHistoryListContainer);
        isElementsListVisible(operationsHistoryListItems);
        isElementsListVisible(operationsHistoryListItemsWaiter);
        isElementsListVisible(operationsHistoryListItemsTable);
        isElementsListVisible(operationsHistoryListItemsTips);
        isElementsListVisible(operationsHistoryListItemsStatus);
        isElementsListVisible(operationsHistoryListItemsSum);
        paginationContainer.shouldBe(visible.because("На странице не оказалось элемента пагинации"));
        scrollTillBottom();
        isElementVisible(paginationContainer);
        isElementsListVisible(paginationPages);

    }

    @Step("Проверка отображения элементов во вкладке Эквайринг")
    public void isAcquiringTabCorrect() {

        click(acquiringTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

        isElementVisible(acquiringInput);
        isElementVisible(currencyInput);
        isElementVisible(accNumberInput);
        isElementVisible(ndsInput);
        isElementVisible(taxSystemInput);
        isElementVisible(saveButton);

        acquiringInput.shouldNotHave(empty);
        currencyInput.shouldNotHave(empty);
        accNumberInput.shouldNotHave(empty);
        ndsInput.shouldNotHave(empty);
        taxSystemInput.shouldNotHave(empty);
        saveButton.shouldBe(disabled);

    }

    @Step("Раскрываем категорию, проверяем что опции доступны для выбора")
    public void openedCategoryAndCheck(SelenideElement input, SelenideElement container, ElementsCollection options) {

        click(input);

        container.shouldHave(attribute("style", ""));
        isElementsListVisible(options);

        click(input);

        container.shouldHave(attribute("style", "display: none;"));

    }

    @Step("Проверяем что есть варианты опций у типов эквайринга")
    public void isAllAcquiringOptionsExists() {

        openedCategoryAndCheck(acquiringInput, acquiringList, acquiringListOptions);
        openedCategoryAndCheck(currencyInput, currencyList, currencyListOptions);
        openedCategoryAndCheck(ndsInput, ndsList, ndsListOptions);
        openedCategoryAndCheck(taxSystemInput, taxSystemList, taxSystemListOptions);

    }

    @Step("Проверяем что есть варианты опций у типов эквайринга")
    public void changeAcquiringToBest2Pay() {

        click(acquiringInput);
        click(acquiringListB2POption);

        isElementVisible(ndsInput);
        isElementVisible(taxSystemInput);

        saveButton.shouldBe(disabled);

    }

    @Step("Проверяем что есть варианты опций у типов эквайринга")
    public void returnToDefaultAcquiring() {

        click(acquiringInput);
        click(acquiringListB2PBarrelOption);

        isElementVisible(acquiringInput);
        isElementVisible(currencyInput);
        isElementVisible(accNumberInput);
        isElementVisible(ndsInput);
        isElementVisible(taxSystemInput);
        isElementVisible(saveButton);

        acquiringInput.shouldNotHave(empty);
        currencyInput.shouldNotHave(empty);
        accNumberInput.shouldNotHave(empty);
        ndsInput.shouldNotHave(empty);
        taxSystemInput.shouldNotHave(empty);
        saveButton.shouldNotBe(disabled);

    }

    @Step("Проверка отображения элементов во вкладке Лоадер")
    public void isLoaderTabCorrect() {

        click(loaderTab);

        isElementVisible(changeLoaderButtonContainer);
        isElementVisible(saveLoaderButton);
        isElementVisible(loaderPreviewContainer);

        forceWait(5000);
        isImageCorrect(loaderInContainerNotSelenide, "Лоадер не загружен корректно или битый");

    }

    @Step("Загружаем новый лоадер")
    public void changeLoader(String filePath) {

        forceWait(3500);

        String previousLoader = $(loaderInContainerNotSelenide).getAttribute("src");

        File loaderGif = new File(filePath);
        changeLoaderButton.uploadFile(loaderGif);

        String newLoader = $(loaderInContainerNotSelenide).getAttribute("src");

        click(saveLoaderButton);
        forceWait(8000);
        Assertions.assertNotEquals(previousLoader, newLoader, "Загруженная анимация\\гиф не загрузилась");

        isImageCorrect(loaderInContainerNotSelenide, "Лоадер не загружен корректно или битый");

    }

    @Step("Проверка отображения элементов во вкладке Статистика")
    public void isStatisticsTabCorrect() {

        click(statisticsTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

        isElementVisible(dateRangeContainer);
        isElementVisible(downloadTable);
        isElementVisible(waitersBalance);

    }

    @Step("Выгрузить таблицу и балансы официантов")
    public void downloadStatisticsData() throws FileNotFoundException {

        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE); //toDo подумать что можно сделать с этим ужасом
        Selenide.executeJavaScript("document.querySelector('" + dateRangeInputSelector + "').click();");

        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
        click(daysInDateRange.get(10));
        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
        click(daysInDateRange.get(15));
        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
        isElementVisible(resetButton);

        File downloadedTable = downloadTable.download(WAIT_FOR_FILE_TO_BE_DOWNLOADED);

        Assertions.assertNotNull(downloadedTable, "Файл 'Выгрузить таблицу' не может быть скачен");

        File waitersBalanceDownloaded = waitersBalance.download(WAIT_FOR_FILE_TO_BE_DOWNLOADED);

        Assertions.assertNotNull(waitersBalanceDownloaded, "Файл 'Балансы официантов' не может быть скачен");

    }

    @Step("Проверка отображения элементов во вкладке Чаевые")
    public void isTipsTabCorrect() {

        click(tipsTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

        isElementVisible(tapperLink);
        isElementVisible(tipsByLink);
        isElementVisible(disableTips);
        isElementVisibleAndClickable(saveButton);

    }

    @Step("Проверка отображения элементов во вкладке Кастомизация")
    public void isCustomizationTabCorrect() {

        click(customizationTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

        isElementVisible(SupportPersonalAccount.LogsAndPermissions.customizationTab.tapperLink);
        isElementVisible(vtbLink);
        isElementVisibleAndClickable(SupportPersonalAccount.LogsAndPermissions.customizationTab.saveButton);

    }

    @Step("Проверка отображения элементов во вкладке Официанты")
    public void isWaiterTabCorrect() {

        click(waitersTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

    }

    @Step("Проверка отображения элементов во вкладке Столы")
    public void isTablesTabCorrect() {

        click(tablesTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

        isElementVisible(tablesTabHeading);
        isElementsListVisible(tablesTabList);
        isElementVisible(tableSearchFrom);
        isElementVisible(tableSearchTo);
        isElementVisible(findTableButton);

    }

}
