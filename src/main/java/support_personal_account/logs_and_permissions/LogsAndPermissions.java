package support_personal_account.logs_and_permissions;


import admin_personal_account.tables_and_qr_codes.TablesAndQrCodes;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import common.BaseActions;
import data.selectors.AdminPersonalAccount;
import data.selectors.SupportPersonalAccount;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import total_personal_account_actions.AuthorizationPage;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Objects;

import static api.ApiData.OrderData.IIKO_RESTAURANT_ID_SUPPORT_SEARCH_RESTAURANT;
import static api.ApiData.OrderData.R_KEEPER_RESTAURANT_ID_SUPPORT_SEARCH_RESTAURANT;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.Constants.TIMEOUT_FOR_FILE_TO_BE_DOWNLOADED;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.OperationsHistory.currentMonth;
import static data.selectors.AdminPersonalAccount.OperationsHistory.leftArrowMonthPeriod;
import static data.selectors.AdminPersonalAccount.TableAndQrCodes.*;
import static data.selectors.SupportPersonalAccount.Common.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.Common.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.acquiringTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.cashDesksTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.customizationTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.licenseTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.loaderTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.loyaltyTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.permissionsTab.id;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.permissionsTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.statisticsTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.tablesTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.tipsTab.saveButton;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.tipsTab.tapperLink;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.tipsTab.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.waitersTab.waitersTab;


public class LogsAndPermissions extends BaseActions {

    TablesAndQrCodes tablesAndQrCodes = new TablesAndQrCodes();
    AuthorizationPage authorizationPage = new AuthorizationPage();

    @Step("Переход в меню профиля")
    public void goToLogsAndPermissionsCategory() {

        click(logsAndPermissionsCategory);
        pageHeading.shouldHave(text("Логи/доступы"), Duration.ofSeconds(5));
        logsContainer.shouldBe(visible);
        isLogsAndPermissionsCategoryCorrect();

    }

    public void goToLogsAndPermissionsKeeperRestaurant() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);
        goToLogsAndPermissionsCategory();
        chooseRestaurant(KEEPER_RESTAURANT_NAME);

    }

    @Step("Выбор тестового ресторана")
    public void chooseRestaurant(String restaurantName) {

        click(expandLeftMenuButton);
        isElementVisible(openedLeftMenuContainer);

        if (!Objects.requireNonNull
                (logsAndPermissionsCategoryDropdownButton.getAttribute("class")).matches(".*active.*")) {

            click(logsAndPermissionsCategoryDropdownButton);

        }

        setInputValue(searchRestaurantInput,restaurantName);

        String restaurantId = restaurantName.equals("testrkeeper") ?
                R_KEEPER_RESTAURANT_ID_SUPPORT_SEARCH_RESTAURANT:
                IIKO_RESTAURANT_ID_SUPPORT_SEARCH_RESTAURANT;

        searchResultList.findBy(and("В списке ресторанов должен быть " + restaurantName,
                matchText(restaurantId),matchText(restaurantName))).click();

        pagePreloader.shouldNotHave(attributeMatching("style", "background: transparent;")
                , Duration.ofSeconds(10));
        pagePreloader.shouldBe(hidden,Duration.ofSeconds(5));

        click(collapseLeftMenuButton);
        isElementInvisible(openedLeftMenuContainer);
        currentChosenRestaurant.shouldHave(text(restaurantName));

    }

    public void setInputValue(SelenideElement element,String value) {

        clearText(element);
        sendKeys(element,value);

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
    @Step("Редактируем все поля в форме доступов по айко")
    public void isPrivateDateChangedCorrectForIiko() {

        id.shouldBe(disabled);
        shopsId.shouldBe(disabled);

        Faker faker = new Faker();

        String previousIpAddress = ip.getValue();
        String previousPort = port.getValue();
        String previousLogin = login.getValue();
        String previousPassword = "19112021";

        String newIpAddress = faker.internet().ipV4Address();
        String newPort = faker.number().digits(4);
        String newLogin = faker.name().username();
        String newPassword = faker.internet().password();

        setData(newIpAddress,newPort,newLogin, newPassword, checked,checked);
        saveData();
        isChangesAppliedIiko(newIpAddress,newPort, newLogin, newPassword);

        setData(previousIpAddress,previousPort, previousLogin, previousPassword,not(checked),not(checked));
        saveData();
        isChangesAppliedIiko(previousIpAddress,previousPort, previousLogin, previousPassword);

    }

    @Step("Редактируем все поля в форме доступов по киперу")
    public void isPrivateDateChangedCorrectForKeeper() {

        id.shouldBe(disabled);
        tokenLicense.shouldBe(disabled);

        Faker faker = new Faker();

        String previousLogin = login.getValue();
        String previousPassword = "123456";
        String previousLoginLicense = loginLicense.getValue();
        String previousPasswordLicense = passwordLicense.getValue();
        String previousIpLicense = ipLicense.getValue();
        String previousRestaurantCodeLicense = restaurantCodeLicense.getValue();
        String previousIdLicense = idLicense.getValue();
        String previousInstanceLicense = instanceLicense.getValue();
        String previousIdCurrency = idCurrency.getValue();
        String previousFirstRequest = firstRequest.getValue();
        String previousCodeManager = codeManager.getValue();
        String previousCodeStation = codeStation.getValue();
        String previousIdReason = idReason.getValue();
        String previousAccNumber= accNumber.getValue();

        String newLogin = faker.name().username();
        String newPassword = faker.internet().password();
        String newLoginLicense = faker.internet().emailAddress();
        String newLoginPasswordLicense = faker.internet().password();
        String newLoginIpLicense = faker.internet().url();
        String newRestaurantCodeLicense = faker.number().digits(9);
        String newIdLicense  = faker.internet().macAddress();
        String newInstanceLicense = faker.number().digits(11);
        String newIdCurrency  = faker.number().digits(7);
        String newFirstRequest = String.valueOf(faker.number().numberBetween(1,9));
        String newCodeManager = faker.number().digits(2);
        String newCodeStation = String.valueOf(faker.number().numberBetween(1,9));
        String newIdReason = String.valueOf(faker.number().numberBetween(1,9));
        String newAccNumber = faker.number().digits(4);

        setData
                (newLogin, newPassword, newLoginLicense, newLoginPasswordLicense, newLoginIpLicense,
                newRestaurantCodeLicense, newIdLicense, newInstanceLicense, newIdCurrency, newFirstRequest,
                newCodeManager, newCodeStation, newIdReason, newAccNumber, checked,checked);

        saveData();
        isChangesAppliedKeeper
                (newLogin, newPassword, newLoginLicense, newLoginPasswordLicense, newLoginIpLicense,
                newRestaurantCodeLicense, newIdLicense, newInstanceLicense, newIdCurrency, newFirstRequest,
                newCodeManager, newCodeStation, newIdReason, newAccNumber);

        setData
                (previousLogin, previousPassword, previousLoginLicense, previousPasswordLicense, previousIpLicense,
                        previousRestaurantCodeLicense, previousIdLicense, previousInstanceLicense,
                        previousIdCurrency, previousFirstRequest, previousCodeManager, previousCodeStation,
                        previousIdReason, previousAccNumber, not(checked),not(checked));

        saveData();
        isChangesAppliedKeeper
                (previousLogin, previousPassword, previousLoginLicense, previousPasswordLicense, previousIpLicense,
                        previousRestaurantCodeLicense, previousIdLicense, previousInstanceLicense,
                        previousIdCurrency, previousFirstRequest, previousCodeManager, previousCodeStation,
                        previousIdReason, previousAccNumber);

    }

    public void setData (String loginValue, String passwordValue, String loginLicenseValue, String passwordLicenseValue,
                        String ipLicenseValue, String restaurantCodeLicenseValue, String idLicenseValue,
                        String instanceLicenseValue,  String idCurrencyValue, String firstRequestValue,
                        String codeManagerValue, String codeStationValue, String idReasonValue, String accNumberValue,
                        Condition availabilityCondition,Condition plugCondition) {

        setInputValue(login,loginValue);
        setInputValue(password, passwordValue);
        setInputValue(loginLicense, loginLicenseValue);
        setInputValue(passwordLicense, passwordLicenseValue);
        setInputValue(ipLicense, ipLicenseValue);
        setInputValue(restaurantCodeLicense, restaurantCodeLicenseValue);
        setInputValue(idLicense, idLicenseValue);
        setInputValue(instanceLicense, instanceLicenseValue);
        setInputValue(idCurrency, idCurrencyValue);
        setInputValue(firstRequest, firstRequestValue);
        setInputValue(codeManager, codeManagerValue);
        setInputValue(codeStation, codeStationValue);
        setInputValue(idReason, idReasonValue);
        setInputValue(accNumber, accNumberValue);

        if (!availabilityCheckbox.isEnabled()) {

            availabilityCheckbox.scrollTo().click();
            availabilityCheckboxInput.shouldBe(availabilityCondition);

        }

        if (!plugCheckbox.isEnabled()) {

            plugCheckbox.scrollTo().click();
            plugCheckboxInput.shouldBe(plugCondition);

        }

    }

    public void setData(String ipValue, String portValue, String loginValue, String passwordValue,
                        Condition availabilityCondition,Condition plugCondition) {

        setInputValue(ip,ipValue);
        setInputValue(port, portValue);
        setInputValue(login, loginValue);
        setInputValue(password, passwordValue);

        if (!availabilityCheckbox.isEnabled()) {

            availabilityCheckbox.scrollTo().click();
            availabilityCheckboxInput.shouldBe(availabilityCondition);

        }

        if (!plugCheckbox.isEnabled()) {

            plugCheckbox.scrollTo().click();
            plugCheckboxInput.shouldBe(plugCondition);

        }

    }

    public void saveData() {

        click(SupportPersonalAccount.LogsAndPermissions.permissionsTab.saveButton);
        isModalConfirmationCorrect();
        click(modalConfirmationSaveButton);

    }

    public void isChangesAppliedIiko(String ipValue, String portValue, String loginValue, String passwordValue) {

        ip.shouldHave(value(ipValue));
        port.shouldHave(value(portValue));
        login.shouldHave(value(loginValue));
        password.shouldHave(value(passwordValue));

    }

    public void isChangesAppliedKeeper
            (String loginValue, String passwordValue, String loginLicenseValue, String passwordLicenseValue,
               String ipLicenseValue, String restaurantCodeLicenseValue, String idLicenseValue,
               String instanceLicenseValue,  String idCurrencyValue, String firstRequestValue,
               String codeManagerValue, String codeStationValue, String idReasonValue, String accNumberValue) {

        login.shouldHave(value(loginValue));
        password.shouldHave(value(passwordValue));

        login.shouldHave(value(loginValue));
        password.shouldHave(value(passwordValue));
        loginLicense.shouldHave(value(loginLicenseValue));
        passwordLicense.shouldHave(value(passwordLicenseValue));
        ipLicense.shouldHave(value(ipLicenseValue));
        restaurantCodeLicense.shouldHave(value(restaurantCodeLicenseValue));
        idLicense.shouldHave(value(idLicenseValue));
        instanceLicense.shouldHave(value(instanceLicenseValue));
        idCurrency.shouldHave(value(idCurrencyValue));
        firstRequest.shouldHave(value(firstRequestValue));
        codeManager.shouldHave(value(codeManagerValue));
        codeStation.shouldHave(value(codeStationValue));
        idReason.shouldHave(value(idReasonValue));
        accNumber.shouldHave(value(accNumberValue));

    }

    public void isModalConfirmationCorrect() {

        isElementVisible(modalConfirmationContainer);
        isElementVisible(modalConfirmationCancelButton);
        isElementVisible(modalConfirmationSaveButton);

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

        licenseIdContainer.shouldBe(appear);

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

        saveXmlApplicationOption();

    }

    public void saveXmlApplicationOption() {

        click(xmlApplicationButton);

        click(SupportPersonalAccount.LogsAndPermissions.licenseTab.saveButton);

        isSaveChangesCorrect();

        click(saveChangesSaveButton);

        xmlApplicationInput.shouldBe(checked);
        xmlOrderSaveInput.shouldNotBe(checked);

    }

    @Step("Проверка отмены сохранения изменений во вкладке r-keeper/iiko")
    public void notSaveChangesKeeperAndIikoTab(String restaurantName) {

        click(iikoOption);
        iikoOptionInput.shouldBe(selected);

        click(SupportPersonalAccount.LogsAndPermissions.licenseTab.saveButton);
        isSaveChangesCorrect();
        click(saveChangesCancelButton);

        Selenide.refresh();
        chooseRestaurant(restaurantName);
        click(cashDeskTab);

        cashDeskContainer.shouldBe(appear);
        rkeeperOptionInput.shouldBe(selected);

    }

    @Step("Проверка отмены сохранения изменений во вкладке Лицензия R-keeper")
    public void notSaveChangesLicenseTab(String restaurantName) {

        SelenideElement selectedElement;

        if (xmlApplicationInput.isSelected()) {

            selectedElement = xmlApplicationInput;
            click(xmlOrderSaveButton);

        } else {

            selectedElement = xmlOrderSaveInput;
            click(xmlApplicationButton);

        }

        click(SupportPersonalAccount.LogsAndPermissions.licenseTab.saveButton);
        isSaveChangesCorrect();
        click(saveChangesCancelButton);

        Selenide.refresh();

        chooseRestaurant(restaurantName);

        click(licenseIdTab);

        selectedElement.shouldBe(selected);

    }

    public void saveOrderSaveOption() {

        click(xmlOrderSaveButton);

        click(SupportPersonalAccount.LogsAndPermissions.licenseTab.saveButton);

        isSaveChangesCorrect();

        click(saveChangesSaveButton);

        xmlApplicationInput.shouldNotBe(checked);
        xmlOrderSaveInput.shouldBe(checked);

    }


    public void isSaveChangesCorrect() {

        isElementVisible(saveChangesContainer);
        isElementVisible(saveChangesSaveButton);
        isElementVisible(saveChangesCancelButton);

    }

    @Step("Выбираем вид лицензии XML сохранение заказов")
    public void choseXmlSaveOrderOption() {

        licenseIdContainer.shouldBe(appear);

        isElementVisible(xmlApplicationButton);
        isElementVisible(xmlOrderSaveButton);

        click(xmlOrderSaveButton);

        isElementVisible(SupportPersonalAccount.LogsAndPermissions.licenseTab.saveButton);

        isElementVisible(licenseDateInput);
        isElementVisibleAndClickable(getIdLicenseButton);

        click(getIdLicenseButton);

        licenseDateInput.shouldNotHave(empty);

        saveOrderSaveOption();

    }

    @Step("Проверка отображения элементов во вкладке r-keeper/iiko")
    public void isCashDeskTabCorrect(String restaurantName) {

        click(cashDeskTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

        isElementVisible(rkeeperOption);
        isElementVisible(iikoOption);
        isElementVisible(otherOption);

        setIikoCashdeskType();

        click(cashDeskTab);

        setRkeeperCashDeskType();

        click(cashDeskTab);

        notSaveChangesKeeperAndIikoTab(restaurantName);

    }

    @Step("Выбор айко типа кассы")
    public void setIikoCashdeskType() {

        click(iikoOption);

        click(SupportPersonalAccount.LogsAndPermissions.licenseTab.saveButton);

        isSaveChangesCorrect();

        click(saveChangesSaveButton);
        iikoOptionInput.shouldBe(checked);

        click(permissionsTab);

        isIikoPermissionTabCorrect();

    }

    public void setRkeeperCashDeskType() {

        click(rkeeperOption);

        click(SupportPersonalAccount.LogsAndPermissions.licenseTab.saveButton);

        isSaveChangesCorrect();

        click(saveChangesSaveButton);
        rkeeperOptionInput.shouldBe(checked);

        click(permissionsTab);

        isKeeperPermissionTabCorrect();

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

        loaderPreviewContainerImg.shouldHave(image);
        isImageCorrect(loaderInContainerNotSelenide, "Лоадер не загружен корректно или битый");

    }

    @Step("Загружаем новый лоадер")
    public void changeLoader(String filePath) {

        String previousLoader = $(loaderInContainerNotSelenide).getAttribute("src");

        changeLoaderButton.uploadFile(new File(filePath));

        saveLoaderButton.shouldBe(enabled).click();
        saveLoaderButton.shouldBe(disabled,Duration.ofSeconds(7));
        loaderPreviewContainerImg.shouldHave(image);

        isImageCorrect(loaderInContainerNotSelenide, "Лоадер не загружен корректно или битый");

        assert previousLoader != null;
        $(loaderInContainerNotSelenide).shouldHave(not(text(previousLoader)),Duration.ofSeconds(10));


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
        clickByJs(dateRangeInputSelector);

        do {

            leftArrowMonthPeriod.shouldBe(visible,enabled);
            click(leftArrowMonthPeriod);

        } while(!currentMonth.getText().equals("Май"));

        click(daysInDateRange.get(10));
        click(daysInDateRange.get(15));

        isElementVisible(resetButton);

        Assertions.assertNotNull(downloadTable.download(TIMEOUT_FOR_FILE_TO_BE_DOWNLOADED),
                "Файл 'Выгрузить таблицу' не может быть скачен");

        Assertions.assertNotNull(waitersBalance.download(TIMEOUT_FOR_FILE_TO_BE_DOWNLOADED),
                "Файл 'Балансы официантов' не может быть скачен");

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
    @Step("Отключаем чаевые")
    public void disableTips() {

        click(disableTips);
        disableTipsInput.shouldBe(enabled);
        click(saveButton);
        tabPreloader.shouldBe(visible).shouldBe(hidden);
        disableTipsInput.shouldBe(enabled);

    }

    @Step("Включаем чаевые")
    public void activateTips() {

        click(tapperLink);
        tapperLink.shouldBe(enabled);
        click(saveButton);
        tabPreloader.shouldBe(visible).shouldBe(hidden);
        tapperLink.shouldBe(enabled);

    }

    @Step("Проверка отображения элементов во вкладке Кастомизация")
    public void isCustomizationTabCorrect() {

        click(customizationTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

        isElementVisible(SupportPersonalAccount.LogsAndPermissions.customizationTab.tapperLink);
        isElementVisible(vtbLink);
        isElementVisibleAndClickable(SupportPersonalAccount.LogsAndPermissions.customizationTab.saveButton);
        isElementVisible(SupportPersonalAccount.LogsAndPermissions.customizationTab.serviceChargeContainer);

    }

    @Step("Активируем сервисный сбор в кастомизации")
    public void activateServiceCharge() {

        if (!serviceChargeInput.has(checked)) {

            click(serviceChargeContainer);

            isElementVisible(confirmationContainer);
            click(confirmButton);

            isElementInvisible(confirmationContainer);

            click(SupportPersonalAccount.LogsAndPermissions.customizationTab.saveButton);
            serviceChargeInput.shouldBe(checked);

        }

    }

    @Step("Деактивируем сервисный сбор в кастомизации")
    public void deactivateServiceCharge() {

        if (serviceChargeInput.has(checked)) {

            click(serviceChargeContainer);

            isElementVisible(confirmationContainer);
            click(confirmButton);

            isElementInvisible(confirmationContainer);

            click(SupportPersonalAccount.LogsAndPermissions.customizationTab.saveButton);
            serviceChargeInput.shouldNot(checked);

        }

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
        isElementsCollectionVisible(tableListItem);
        isElementVisible(resetTableButton);

        if (AdminPersonalAccount.TableAndQrCodes.paginationPages.size() == 1) {

            click(AdminPersonalAccount.TableAndQrCodes.paginationPages.first());

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
