package data.selectors;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class SupportPersonalAccount {


    public static class Common {

        public static final SelenideElement expandLeftMenuButton = $(".vProfileMenu__info");
        public static final SelenideElement collapseLeftMenuButton =
                $(".vProfileMenu__info .vProfileMenu__resize");
        public static final SelenideElement openedLeftMenuContainer = $("[class='vProfileMenu']");
        public static final SelenideElement searchRestaurantInput = $("[id=\"searchField\"]");
        public static final ElementsCollection searchResultList =
                $$(".VMenuProfileLink__list .VMenuProfileLink__rest");
        public static final SelenideElement profileCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Профиль']]");
        public static final SelenideElement logsAndPermissionsCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Логи/доступы']]");
        public static final SelenideElement logsAndPermissionsCategoryDropdownButton =
                $x("//*[contains(@class,'VMenuProfileLink')][.//*[text()='Логи/доступы']]" +
                        "/*[contains(@class,'vProfileMenu__arrow-right')]");
        public static final SelenideElement historyOperationsCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='История операций']]");
        public static final SelenideElement lockCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Заглушка']]");
        public static final SelenideElement sendingCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Рассылка']]");
        public static final SelenideElement cashDeskInaccessibilityCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Недоступность кассы']]");

        public static final SelenideElement analyticsCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Аналитика']]");


        public static final SelenideElement pagePreloader =
                $(".vLightPreloader");

    }

    public static class Profile {

        public static final SelenideElement profileContainer = $(".section-profile__form");
        public static final SelenideElement profileTitle = $(".section-profile__title");
        public static final SelenideElement restaurantName = $("[id=\"name_shop\"]");
        public static final SelenideElement name = $("[id=\"name\"]");
        public static final SelenideElement phone = $("[id=\"phone\"]");
        public static final ElementsCollection telegramItems = $$(".section-profile__inputs-list-item");
        public static final ElementsCollection telegramItemsLogin =
                $$(".section-profile__inputs-list-item [id='1']");
        public static final SelenideElement email = $("[id=\"email\"]");
        public static final SelenideElement password = $("[id=\"password\"]");
        public static final SelenideElement inputError = $("[class='vLandingInput err']");


        public static final SelenideElement passwordConfirmation = $("[id=\"confirmation\"]");
        public static final SelenideElement saveButton = $(".section-profile__form  .vButton");


    }

    public static class LogsAndPermissions {
        public static class Common {
            public static final SelenideElement logsContainer = $(".logsPage");
            public static final SelenideElement currentChosenRestaurant =
                    $(".logsPage__body .section-profile__title");
            public static final SelenideElement clearCashTableButton =
                    $x("//button[text()=\" Очистить кеш столов \"]");
            public static final SelenideElement clearAllCashServiceButton =
                    $x("//button[text()=\" Очистить общий кеш сервиса \"]");
            public static final SelenideElement tabPreloader = $(".logsPage-tabs .vLightPreloader");

        }

        public static class logsTab {
            public static final SelenideElement logsTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Логи')]");

        }

        public static class permissionsTab {
            public static final SelenideElement permissionsTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Доступы')]");
            public static final SelenideElement logo = $("h3 img");
            public static final SelenideElement id = $("[id=\"id\"]");
            public static final SelenideElement login = $("[id=\"login\"]");
            public static final SelenideElement password = $("[id=\"password\"]");
            public static final SelenideElement loginLicense = $("[id=\"login_license\"]");
            public static final SelenideElement passwordLicense = $("[id=\"password_license\"]");
            public static final SelenideElement tokenLicense = $("[id=\"token_license\"]");
            public static final SelenideElement ipLicense = $("[id=\"url\"]");
            public static final SelenideElement restaurantCodeLicense = $("[id=\"restCode\"]");
            public static final SelenideElement idLicense = $("[id=\"productGUID\"]");

            public static final SelenideElement instanceLicense = $("[id=\"license_instance\"]");
            public static final SelenideElement idCurrency = $("[id=\"id_currency\"]");
            public static final SelenideElement firstRequest = $("[id=\"first_request\"]");
            public static final SelenideElement codeManager = $("[id=\"id_manager\"]");
            public static final SelenideElement codeStation = $("[id=\"id_station\"]");
            public static final SelenideElement idReason = $("[id=\"id_reason\"]");
            public static final SelenideElement accNumber = $("[id=\"acc_number\"]");

            public static final SelenideElement checkboxesContainer = $(".activity_establishments");

            public static final SelenideElement availabilityCheckboxInput =
                    $(".checkbox__conditions.max-width_availability input");

            public static final SelenideElement availabilityCheckbox =
                    $(".checkbox__conditions.max-width_availability");

            public static final SelenideElement plugCheckboxInput = $(".checkbox__conditions.max-width_plug input");
            public static final SelenideElement plugCheckbox = $(".checkbox__conditions.max-width_plug");

            public static final SelenideElement saveButton = $(".logsPage-tabs__list button");

            public static final SelenideElement ip = $("[id=\"ip\"]");
            public static final SelenideElement port = $("[id=\"port\"]");
            public static final SelenideElement shopsId = $("[id=\"shops_id\"]");

            public static final SelenideElement modalConfirmationContainer = $(".vModalConfirmChanges");
            public static final SelenideElement modalConfirmationCancelButton =
                    $(".vModalConfirmChanges__cancelBtnWrapper");
            public static final SelenideElement modalConfirmationSaveButton =
                    $(".vModalConfirmChanges__buttons>button");


        }

        public static class licenseTab {
            public static final SelenideElement licenseIdTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn')" +
                            " and contains(text(),'Лицензия R-keeper')]");
            public static final SelenideElement licenseIdInput = $("[id=\"licenseId\"]");
            public static final SelenideElement xmlApplicationButton = $("[for=\"XML_APPLICATION\"]");

            public static final SelenideElement xmlOrderSaveButton = $("[for=\"XML_ORDER_SAVE\"]");

            public static final SelenideElement saveButton =
                    $x("//*[contains(@class,'vButton') and text()=' Сохранить ']");

            public static final SelenideElement licenseDateInput = $("[id=\"licenseDate\"]");
            public static final SelenideElement getIdLicenseButton =
                    $x("//*[text()=' Получить срок лицензии '] | //*[text()=' Получить id лицензии ']");

        }

        public static class cashDesksTab {
            public static final SelenideElement cashDeskTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'r-keeper/iiko')]");

        }

        public static class operationsTab {
            public static final SelenideElement operationsTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Операции')]");
            public static final SelenideElement operationStatus =
                    $(".vHistoryBlockHeader__dropdown");

        }

        public static class acquiringTab {
            public static final SelenideElement acquiringTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Эквайринг')]");
            public static final SelenideElement acquiringInput =
                    $x("//*[contains(@class,'vSelect__placeholder') and text()='Эквайринг']" +
                            "/following-sibling::div");
            public static final SelenideElement acquiringList =
                    $(".vLogsAcquiring__list .vSelect__list");
            public static final SelenideElement acquiringListB2POption =
                    $x("//*[@class='vLogsAcquiring__list']//*[@class='vSelect__item'" +
                            " and text()='Best2pay']");
            public static final SelenideElement acquiringListB2PBarrelOption =
                    $x("//*[@class='vLogsAcquiring__list']//*[@class='vSelect__item'" +
                            " and text()='BEST2PAY_BARREL']");
            public static final ElementsCollection acquiringListOptions =
                    $$(".vLogsAcquiring__list .vSelect__item");
            public static final SelenideElement currencyInput =
                    $x("//*[contains(@class,'vSelect__placeholder') and text()='Валюта']" +
                            "/following-sibling::div");
            public static final SelenideElement currencyList =
                    $x("//*[contains(@class,'vSelect__placeholder') and text()='Валюта']" +
                            "/following-sibling::div/following-sibling::ul");
            public static final ElementsCollection currencyListOptions =
                    $$x("//*[contains(@class,'vSelect__placeholder') and text()='Валюта']" +
                            "/following-sibling::div/following-sibling::ul/li");
            public static final SelenideElement accNumberInput = $("[id=\"BEST2PAY_BARREL\"]");
            public static final SelenideElement ndsInput =
                    $x("//*[contains(@class,'vSelect__placeholder') and text()='НДС']" +
                            "/following-sibling::div");
            public static final SelenideElement ndsList =
                    $x("//*[contains(@class,'vSelect__placeholder') and text()='НДС']" +
                            "/following-sibling::div/following-sibling::ul");
            public static final ElementsCollection ndsListOptions =
                    $$x("//*[contains(@class,'vSelect__placeholder') and text()='НДС']" +
                            "/following-sibling::div/following-sibling::ul/li");
            public static final SelenideElement taxSystemInput =
                    $x("//*[contains(@class,'vSelect__placeholder') and text()='Систем налогообложения']" +
                            "/following-sibling::div");
            public static final SelenideElement taxSystemList =
                    $x("//*[contains(@class,'vSelect__placeholder') and text()='Систем налогообложения']" +
                            "/following-sibling::div/following-sibling::ul");
            public static final ElementsCollection taxSystemListOptions =
                    $$x("//*[contains(@class,'vSelect__placeholder') and text()='Систем налогообложения']" +
                            "/following-sibling::div/following-sibling::ul/li");
            public static final SelenideElement saveButton =
                    $x("//*[@class='vButton' and text()=' Сохранить ']");

        }

        public static class loaderTab {
            public static final SelenideElement loaderTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Лоадер')]");
            public static final SelenideElement changeLoaderButton = $(".fileInput input");
            public static final SelenideElement changeLoaderButtonContainer = $(".fileInput");
            public static final SelenideElement saveLoaderButton =
                    $x("//*[contains(@class,'vButton') and text()=' Сохранить ']");
            public static final SelenideElement loaderPreviewContainer = $(".phoneBackground");

            public static final SelenideElement loaderPreviewContainerImg = $(".phoneBackground img");
            public static final String loaderInContainerNotSelenide = ".loaderContainer img";

        }

        public static class statisticsTab {
            public static final SelenideElement statisticsTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Статистика')]");
            public static final SelenideElement dateRangeContainer =
                    $(".vLogsStatistic__calendar__period-label");
            public static final String dateRangeInputSelector = ".mx-datepicker.mx-datepicker-range div";
            public static final ElementsCollection daysInDateRange =
                    $$(".mx-calendar-range>.mx-calendar:nth-child(1) .mx-date-row td");
            public static final SelenideElement resetButton = $(".vButtonRed");
            public static final SelenideElement downloadTable =
                    $x("//*[contains(@class,'vButton') and text()=' Выгрузить таблицу ']");
            public static final SelenideElement waitersBalance =
                    $x("//*[contains(@class,'vButton') and text()=' Балансы официантов ']");

        }

        public static class tipsTab {
            public static final SelenideElement tipsTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Чаевые')]");
            public static final SelenideElement tapperLink =
                    $x("//*[@class='vRadioButton'][./*[@for=\"TAPPER\"]]/label");
            public static final SelenideElement tipsByLink =
                    $x("//*[@class='vRadioButton'][./*[@for=\"LINK\"]]/label");
            public static final SelenideElement disableTips =
                    $x("//*[@class='vRadioButton'][./*[@for=\"DISABLED\"]]/label");
            public static final SelenideElement disableTipsInput =
                    $x("//*[@for=\"DISABLED\"]");
            public static final SelenideElement saveButton =
                    $x("//*[@class='vButton' and text()=' Сохранить ']");

        }

        public static class customizationTab {
            public static final SelenideElement customizationTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Кастомизация')]");
            public static final SelenideElement tapperLink =
                    $x("//*[@class='vRadioButton'][./*[@for=\"TAPPER\"]]");
            public static final SelenideElement vtbLink =
                    $x("//*[@class='vRadioButton'][./*[@for=\"VTB\"]]");

            public static final SelenideElement saveButton =
                    $x("//*[@class='vButton' and text()=' Сохранить ']");

        }

        public static class waitersTab {
            public static final SelenideElement waitersTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Официанты')]");
            public static final SelenideElement waiterNameInCashDesk = $("[id=\"name\"]");
            public static final SelenideElement waiterName = $("[id=\"display_name\"]");
            public static final SelenideElement waiterEmail = $("[id=\"email\"]");
            public static final SelenideElement telegramLogin = $("[id=\"loginTelegram\"]");
            public static final SelenideElement telegramId = $("[id=\"idTelegram\"]");

            public static final SelenideElement saveButton = $(".section-profile__form .vButton");

        }

        public static class tablesTab {
            public static final SelenideElement tablesTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Столы')]");
            public static final SelenideElement tablesTabHeading = $(".vLogsTables__title");
            public static final ElementsCollection tablesTabList = $$(".vSectionQr__list .vSectionQr__item");
            public static final SelenideElement tableLoader = $(".vSectionQr__preloader");



        }

        public static class loyaltyTab {
            public static final SelenideElement loyaltyTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn')" +
                            " and contains(text(),'Система лояльности')]");
            public static final SelenideElement offCheckbox =
                    $x("//*[contains(@class,'vRadioButton')][.//label[@for='OFF']]");
            public static final SelenideElement novikovCheckbox =
                    $x("//*[contains(@class,'vRadioButton')][.//label[@for='NOVIKOV']]");

        }

    }

    public static class HistoryOperations {

        public static final SelenideElement preloader = $(".vPreloader");
        public static final SelenideElement emptyOperationsList = $(".VSectionOrderHistory__empty-label");
        public static final String deleteCurrentFilterButton = ".crossOrderStatus";

        public static final SelenideElement operationsListTab =
                $x("//*[contains(@class,'VSectionOrderHistory__switching__tabs') " +
                        "and text()=\" Список операций \"]");
        public static final SelenideElement operationsContainer =
                $(".VSectionOrderHistory__block");
        public static final SelenideElement stuckTransactionTab =
                $x("//*[contains(@class,'VSectionOrderHistory__switching__tabs') " +
                        "and text()=\" Застрявшие транзакции \"]");
        public static final SelenideElement restaurantFilterButton =
                $(".VFiltersRestaurant__tabs:first-child");

        public static final ElementsCollection filterButtons = $$(".VFiltersRestaurant__tabs");

        public static final SelenideElement restaurantFilterContainer = $(".VModalRestaurant");
        public static final SelenideElement restaurantFilterSearch = $(".VModalRestaurant__search");
        public static final SelenideElement restaurantFilterSearchInput =
                $(".VModalRestaurant__search input");
        public static final ElementsCollection restaurantFilterListItems =
                $$(".VModalRestaurant__list__item");

        public static final SelenideElement filterCloseButton= $("[class$='closeModal']");







        public static final SelenideElement tableNumberFilterButton =
                $(".VFiltersRestaurant__tabs:nth-child(2)");

        public static final SelenideElement tableNumberFilterContainer = $(".VModalSearchTable");
        public static final SelenideElement tableNumberFilterSearch= $(".VModalSearchTable__search");
        public static final SelenideElement tableNumberFilterSearchInput =
                $(".VModalSearchTable__search input");
        public static final SelenideElement tableNumberFilterApplyButton = $(".VModalSearchTable button");



        public static final SelenideElement orderStatusFilterButton =
                $x("//*[contains(@class,'VFiltersRestaurant__tabs')]" +
                        "[./div/./p[contains(text(),'Статус заказа')]]");
        public static final SelenideElement waiterFilterButton =
                $x("//*[contains(@class,'VFiltersRestaurant__tabs')]" +
                        "[./div/./p[contains(text(),'Официант')]]");
        public static final SelenideElement dayPeriodButton = $(".vTimePeriod [for=\"day\"]");
        public static final SelenideElement monthPeriodButton = $(".vTimePeriod [for=\"month\"]");
        public static final SelenideElement customPeriodButton = $(".vTimePeriod .vTimePeriod__period");
        public static final SelenideElement showOnlyRefundsButton = $(".VFiltersRestaurant__checkbox");

        public static final SelenideElement showOnlyRefundsInput = $(".VFiltersRestaurant__checkbox input");
        public static final SelenideElement activePeriodDate = $(".VOrderHistoryAmountsSum__date__text");

        public static final SelenideElement totalSumContainer =
                $(".VOrderHistoryAmountsSum__amounts__item:first-child");

        public static final SelenideElement totalTipsContainer =
                $(".VOrderHistoryAmountsSum__amounts__item:last-child");

        public static final ElementsCollection operationsItems = $$(".VOrderHistoryTableItem");

        public static final ElementsCollection restaurantName = $$(".widthName span");
        public static final ElementsCollection tableNumber =
                $$(".VOrderHistoryTableItem tr th:nth-child(2) span");
        public static final ElementsCollection orderId =
                $$(".VOrderHistoryTableItem tr th:nth-child(3) span");

        public static final ElementsCollection dateAndTime =
                $$(".VOrderHistoryTableItem tr th:nth-child(4) span");
        public static final ElementsCollection orderStatus =
                $$(".VOrderHistoryTableItem tr th:nth-child(5) span");

        public static final ElementsCollection totalSum =
                $$(".VOrderHistoryTableItem tr th:nth-child(6) span");

        public static final ElementsCollection orderSum =
                $$(".VOrderHistoryTableItem tr th:nth-child(7) span");
        public static final ElementsCollection tipsSum =
                $$(".VOrderHistoryTableItem tr th:nth-child(8) span");
        public static final ElementsCollection serviceCharge =
                $$(".VOrderHistoryTableItem tr th:nth-child(9) span");
        public static final ElementsCollection waiterName =
                $$(".VOrderHistoryTableItem tr th:nth-child(10) span");

        public static final SelenideElement loadMoreButton = $(".VSectionOrderHistory__loadMore");


        public static final SelenideElement generalCategory = $x("//button[contains(text(),'Основные')]");
        public static final SelenideElement tipsCategory = $x("//button[contains(text(),'Чаевые')]");

        public static final SelenideElement totalStuckSum = $(".VSectionOrderHistory__stuck__info__money");
        public static final SelenideElement totalStuckOperationsAmount =
                $(".VSectionOrderHistory__stuck__info__numberStuckTransactions");

        public static final ElementsCollection generalOperationsItem =
                $$(".VSectionOrderHistory__containerTableItem div");



        public static final ElementsCollection generalRestaurantName =
                $$(".VSectionOrderHistory__containerTableItem div th:nth-child(1) span");
        public static final ElementsCollection generalTableNumber =
                $$(".VSectionOrderHistory__containerTableItem div th:nth-child(2) span");
        public static final ElementsCollection generalB2tpId =
                $$(".VSectionOrderHistory__containerTableItem div th:nth-child(3) span");

        public static final ElementsCollection generalDateAndTime =
                $$(".VSectionOrderHistory__containerTableItem div th:nth-child(4) span");
        public static final ElementsCollection generalTotalsum =
                $$(".VSectionOrderHistory__containerTableItem div th:nth-child(5) span");

        public static final ElementsCollection generalTips =
                $$(".VSectionOrderHistory__containerTableItem div th:nth-child(6) span");

        public static final ElementsCollection generalServiceCharge =
                $$(".VSectionOrderHistory__containerTableItem div th:nth-child(7) span");

        public static final ElementsCollection generalComment =
                $$(".VSectionOrderHistory__containerTableItem div th:nth-child(7) span");















        public static final ElementsCollection stuckRestaurantName =
                $$(".VTippingTransactionsTable tr th:nth-child(1) span");
        public static final ElementsCollection stuckTableNumber =
                $$(".VTippingTransactionsTable tr th:nth-child(2) span");
        public static final ElementsCollection stuckB2tpId =
                $$(".VTippingTransactionsTable tr th:nth-child(3) span");

        public static final ElementsCollection stuckDateAndTime =
                $$(".VTippingTransactionsTable tr th:nth-child(4) span");
        public static final ElementsCollection stuckTips =
                $$(".VTippingTransactionsTable tr th:nth-child(5) span");

        public static final ElementsCollection stuckWaiterName =
                $$(".VTippingTransactionsTable tr th:nth-child(6) span");

        public static final ElementsCollection stuckComment =
                $$(".VTippingTransactionsTable tr th:nth-child(7) span");

        public static final SelenideElement stuckPushButton = $(".pushButton");


    }

    public static class Lock {

        public static final SelenideElement lockContainer = $(".plugForm");
        public static final SelenideElement lockInfoContainer = $(".plugFormInfo");
        public static final SelenideElement whereToLockContainer = $(".vPlugSelect");
        public static final SelenideElement amountRestaurantsToLock = $(".vPlugSelect__value");
        public static final SelenideElement whereToLockButton = $(".vPlugSelect>div");
        public static final SelenideElement chosenTypeOfLock = $(".vSelect__label");

        public static final SelenideElement b2pLockOption =
                $x("//*[contains(@class,'vPlugFilterList__item')][.//img[contains(@src,'b2p')]]");

        public static final SelenideElement howToLockContainer = $(".vSelect");
        public static final SelenideElement howToLockButton = $(".vSelect .vSelect__label");

        public static final SelenideElement saveButton =
                $x("//*[contains(@class,'vButton') and text()=' Сохранить ']");
        public static final SelenideElement dropdownWhereToLockTitle = $(".vPlugDropdown__title");
        public static final SelenideElement dropdownWhereToLockSearchRestaurant =
                $x("//*[contains(@class,'vPlugDropdown__block')]" +
                        "[.//*[@class='vPlugSearch__input']]//input");
        public static final ElementsCollection dropdownWhereToLockFilterOptions =
                $$(".vPlugFilterList li");
        public static final ElementsCollection dropdownWhereToLockRestaurants =
                $$(".vPlugLst .vPlugLst__item");
        public static final SelenideElement applyButton =
                $x("//*[contains(@class,'vButton') and text()=' Применить ']");
        public static final SelenideElement resetAllButton =
                $x("//*[contains(@class,'vButtonRed') and text()=' Сбросить всё ']");
        public static final ElementsCollection dropdownHowToLockRestaurants =
                $$(".vSelect__list .vSelect__item");
        public static final SelenideElement dropdownHowToLockRestaurantsWholeServiceButton =
                $x("//*[@class='vSelect__list']" +
                        "/*[contains(@class,'vSelect__item') and contains(text(),'Сервис полностью')]");
        public static final SelenideElement dropdownHowToLockRestaurantsPaymentOnlyButton =
                $x("//*[@class='vSelect__list']/*[contains(@class,'vSelect__item')" +
                        " and text()='Только оплату']");
        public static final SelenideElement lockPreloader = $(".vPlugLoader");


    }

    public static class Sending {

        public static final SelenideElement sendingContainer = $(".vSectionMailing__content");
        public static final SelenideElement sendingRecipientContainer = $(".vSectionMailing__recipient-info");
        public static final SelenideElement sendingMessageContainer = $(".vSectionMailing__message");
        public static final SelenideElement sendToAllContainer =
                $x("//*[@class='vSectionMailing__radio__cell'][.//*[@value='ALL']]");
        public static final SelenideElement sendToWaiterContainer =
                $x("//*[@class='vSectionMailing__radio__cell'][.//*[@value='WAITER']]");
        public static final SelenideElement sendToManagerContainer =
                $x("//*[@class='vSectionMailing__radio__cell'][.//*[@value='MANAGER']]");
        public static final SelenideElement messageTextArea = $(".vSectionMailing__message__textarea");
        public static final SelenideElement sendButton = $(".vButton");
        public static final SelenideElement confirmationContainer = $(".vModalMailing");
        public static final SelenideElement confirmationSendButton =
                $x("//*[@class='vModalMailing__footer']//*[@class='vButton' and text()=' Отправить ']");
        public static final SelenideElement confirmationCancelButton =
                $x("//*[@class='vModalMailing__footer']//*[@class='vButton' and text()=' Отправить ']");
        public static final SelenideElement confirmationCloseButton = $(".vCloseButton");

    }

    public static class CashDeskInaccessibility {

        public static final SelenideElement cashDeskInaccessibilityContainer = $(".plugForm");
        public static final SelenideElement cashDeskInaccessibilityInfoContainer = $(".plugFormInfo");
        public static final SelenideElement choseRestaurantContainer = $(".vPlugSelect");
        public static final SelenideElement amountChosenRestaurant = $(".vPlugSelect__value");
        public static final SelenideElement choseRestaurantButton = $(".vPlugSelect>div");
        public static final String choseDateButtonSelector = ".mx-datepicker .mx-input-wrapper";
        public static final SelenideElement choseDateContainer = $(".vTimePeriod__period");
        public static final SelenideElement firstDateOfPeriod = $(".mx-calendar-content tr>td");
        public static final SelenideElement applyButton =
                $x("//*[contains(@class,'vButton') and text()=' Применить ']");
        public static final SelenideElement downloadTableButton =
                $x(".//*[contains(@class,'vButton') and text()=' Выгрузить таблицу ']");

    }

    public static class Analytics {

        public static final SelenideElement analyticsContainer = $(".vSectionAnalytics__content");

        public static final SelenideElement conversionStatisticsButton =
                $x("//button[contains(text(),'Статистика по конверсиям')]");

        public static final SelenideElement dateRangeContainer = $(".vBlockConversionStatistics__period");
        public static final String choseDateButtonSelector = ".mx-datepicker .mx-input-wrapper";


        public static final SelenideElement downloadTable = $(".vButton");

    }

}
