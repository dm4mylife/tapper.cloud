package support_personal_account.logs_and_permissions;


import admin_personal_account.tables_and_qr_codes.TablesAndQrCodes;
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
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static data.Constants.*;
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
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.loyaltyTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.operationsTab.operationStatus;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.operationsTab.operationsTab;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.permissionsTab.id;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.permissionsTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.statisticsTab.dateRangeContainer;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.statisticsTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.tablesTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.tipsTab.saveButton;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.tipsTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.waitersTab.waitersTab;


public class LogsAndPermissions extends BaseActions {

    TablesAndQrCodes tablesAndQrCodes = new TablesAndQrCodes();

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

        if(!Objects.requireNonNull
                (logsAndPermissionsCategoryDropdownButton.getAttribute("class")).matches(".*active.*")) {

            click(logsAndPermissionsCategoryDropdownButton);

        }

        clearText(searchRestaurantInput);
        sendKeys(searchRestaurantInput,restaurantName);

        searchResultList.first().shouldHave(matchText(restaurantName),Duration.ofSeconds(5));
        click(searchResultList.first());

        pagePreloader.shouldNotHave(attributeMatching("style", "background: transparent;")
                , Duration.ofSeconds(10));
        pagePreloader.shouldBe(hidden,Duration.ofSeconds(5));

        click(collapseLeftMenuButton);
        isElementInvisible(openedLeftMenuContainer);
        currentChosenRestaurant.shouldHave(text(restaurantName));

    }

    @Step("Проверка таба Доступы если тип ресторана keeper")
    public void isKeeperPermissionTabCorrect() {

        logo.shouldHave(attributeMatching("src",".*keeper.*")
                .because("Должные быть логотип кипера"));
        isElementVisible(id);
        isElementVisible(login);
        isElementVisible(password);
        isElementVisible(loginLicense);
        isElementVisible(passwordLicense);
        isElementVisible(tokenLicense);
        isElementVisible(ipLicense);
        isElementVisible(restaurantCodeLicense);
        isElementVisible(idLicense);
        isElementVisible(instanceLicense);
        isElementVisible(idCurrency);
        isElementVisible(firstRequest);
        isElementVisible(codeManager);
        isElementVisible(codeStation);
        isElementVisible(idReason);
        isElementVisible(accNumber);
        isElementVisible(checkboxesContainer);
        isElementVisible(saveButton);

    }

    @Step("Проверка таба Доступы если тип ресторана iiko")
    public void isIikoPermissionTabCorrect() {

        logo.shouldHave(attributeMatching("src",".*iiko.*")
                .because("Должные быть логотип айко"));
        isElementVisible(id);
        isElementVisible(ip);
        isElementVisible(port);
        isElementVisible(login);
        isElementVisible(password);
        isElementVisible(shopsId);
        isElementVisible(checkboxesContainer);
        isElementVisible(saveButton);

    }

    @Step("Проверка что все элементы в Логи/доступы корректны")
    public void isLogsAndPermissionsCategoryCorrect() {

        isElementVisible(currentChosenRestaurant);
        isElementVisibleAndClickable(clearCashTableButton);
        isElementVisibleAndClickable(clearAllCashServiceButton);

        isElementVisible(permissionsTab);
        isElementVisible(licenseIdTab);
        isElementVisible(cashDeskTab);
        isElementVisible(acquiringTab);
        isElementVisible(loaderTab);
        isElementVisible(statisticsTab);
        isElementVisible(tipsTab);
        isElementVisible(customizationTab);
        isElementVisible(waitersTab);
        isElementVisible(tablesTab);
        isElementVisible(loyaltyTab);

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

        forceWait(WAIT_FOR_IMAGE_IS_FULL_LOAD_ON_CONTAINER);
        isImageCorrect(loaderInContainerNotSelenide, "Лоадер не загружен корректно или битый");

    }

    @Step("Загружаем новый лоадер")
    public void changeLoader(String filePath) {

        String previousLoader = $(loaderInContainerNotSelenide).getAttribute("src");

        File loaderGif = new File(filePath);
        changeLoaderButton.uploadFile(loaderGif);

        forceWait(WAIT_FOR_GIF_IS_FULL_LOAD_ON_CONTAINER);

        String newLoader = $(loaderInContainerNotSelenide).getAttribute("src");

        click(saveLoaderButton);
        forceWait(WAIT_FOR_GIF_IS_FULL_LOAD_ON_CONTAINER);

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


    @Step("Проверка отображения элементов во вкладке Система лояльности")
    public void isLoyaltyTabCorrect() {

        click(loyaltyTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

        isElementVisible(offCheckbox);
        isElementVisible(novikovCheckbox);

    }

    @Step("Выгрузить таблицу и балансы официантов")
    public void downloadStatisticsData() throws FileNotFoundException {

        isElementVisible(dateRangeContainer);
        Selenide.executeJavaScript("document.querySelector('" + dateRangeInputSelector + "').click();");

        click(daysInDateRange.get(10));
        click(daysInDateRange.get(15));

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

    @Step("Поиск столов с начального и до конечного значения")
    public void searchTableRange(int min, int max) {

        if (min != 0) {

            tableSearchFrom.setValue(String.valueOf(min));

        } else {

            tableSearchFrom.setValue("");

        }

        if (max != 0) {

            tableSearchTo.setValue(String.valueOf(max));

        } else {

            tableSearchTo.setValue("");

        }

        click(findTableButton);
        tableLoader.shouldHave(attribute("style",""),Duration.ofSeconds(2));
        resetTableButton.shouldBe(visible);

        if (AdminPersonalAccount.TableAndQrCodes.paginationPages.size() == 1) {

            click(AdminPersonalAccount.TableAndQrCodes.paginationPages.first());
            pagePreloader.shouldBe(visible,Duration.ofSeconds(2));

            if(!Objects.equals(tableSearchFrom.getValue(), ""))
                tableListItem.filter(matchText(String.valueOf(min))).shouldHave(sizeGreaterThanOrEqual(1));

            tableListItem.filter(matchText(String.valueOf(max))).shouldHave(sizeGreaterThanOrEqual(1));

        } else {

            click(AdminPersonalAccount.TableAndQrCodes.paginationPages.first());
            if(!Objects.equals(tableSearchFrom.getValue(), ""))
                tableListItem.filter(matchText(String.valueOf(min))).shouldHave(sizeGreaterThanOrEqual(1));

            click(AdminPersonalAccount.TableAndQrCodes.paginationPages.last());
            tableListItem.filter(matchText(String.valueOf(max))).shouldHave(sizeGreaterThanOrEqual(1));

        }

        tablesAndQrCodes.resetTableSearch();

    }


    @Step("Поиск стола")
    public void searchTable(int fromTableSearchValue, int toTableSearchValue ) {

        searchTableRange(fromTableSearchValue, toTableSearchValue);

    }

}
