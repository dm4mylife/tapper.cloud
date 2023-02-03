package data.selectors;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class SupportPersonalAccount {


    public static class Common {

        public static final SelenideElement expandLeftMenuButton =
                $(".vProfileMenu__info");
        public static final SelenideElement collapseLeftMenuButton =
                $(".vProfileMenu__info .vProfileMenu__resize");
        public static final SelenideElement openedLeftMenuContainer =
                $("[class='vProfileMenu']");
        public static final SelenideElement searchRestaurantInput =
                $("[id=\"searchField\"]");
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
                        "/*[@class='vProfileMenu__arrow-right']");
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

        public static final SelenideElement pagePreloader =
                $(".vLightPreloader");


    }

    public static class Profile {

        public static final SelenideElement profileContainer = $(".section-profile__form");
        public static final SelenideElement profileTitle = $(".section-profile__title");
        public static final SelenideElement restaurantName = $("[id=\"name_shop\"]");
        public static final SelenideElement pagePreloader = $(".vPreloader");
        public static final SelenideElement name = $("[id=\"name\"]");
        public static final SelenideElement phone = $("[id=\"phone\"]");
        public static final ElementsCollection telegramItems = $$(".section-profile__inputs-list-item");
        public static final ElementsCollection telegramItemsLogin =
                $$(".section-profile__inputs-list-item [id='1']");
        public static final SelenideElement email = $("[id=\"email\"]");

        public static final SelenideElement password = $("[id=\"password\"]");
        public static final SelenideElement passwordConfirmation = $("[id=\"confirmation\"]");
        public static final SelenideElement saveButton = $(".section-profile__form  .vButton");


    }

    public static class LogsAndPermissions {
        public static class Common {
            public static final SelenideElement logsContainer = $(".logsPage");
            public static final SelenideElement pagePreloader = $(".vLightPreloader");
            public static final SelenideElement currentChosenRestaurant = $(".logsPage__body .section-profile__title");
            public static final SelenideElement searchInput = $(".vSearch__field");
            public static final SelenideElement choseDateRange = $(".mx-datepicker");
            public static final SelenideElement clearCashTableButton =
                    $x("//button[text()=\" Очистить кеш столов \"]");
            public static final SelenideElement clearAllCashServiceButton =
                    $x("//button[text()=\" Очистить общий кеш сервиса \"]");
            public static final SelenideElement getRestaurantId =
                    $x("//button[text()=\" Получить ID ресторана \"]");
            public static final SelenideElement tabPreloader =
                    $(".logsPage-tabs .vLightPreloader");

        }

        public static class logsTab {
            public static final SelenideElement logsTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Логи')]");

        }

        public static class permissionsTab {
            public static final SelenideElement permissionsTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Доступы')]");

        }

        public static class licenseTab {
            public static final SelenideElement licenseIdTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'id лицензии')]");
            public static final SelenideElement licenseIdInput =
                    $("[id=\"licenseId\"]");
            public static final SelenideElement licenseDateInput =
                    $("[id=\"licenseDate\"]");
            public static final SelenideElement getIdLicenseButton =
                    $x("//*[text()=' Получить id лицензии ']");

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
            public static final String loaderInContainerNotSelenide = ".loaderContainer img";

        }

        public static class statisticsTab {
            public static final SelenideElement statisticsTab =
                    $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Статистика')]");
            public static final SelenideElement dateRangeContainer =
                    $(".vLogsStatistic__calendar__period-label");
            public static final String dateRangeInputSelector =
                    ".mx-datepicker.mx-datepicker-range div";
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
                    $x("//*[@class='vRadioButton'][./*[@for=\"TAPPER\"]]");
            public static final SelenideElement tipsByLink =
                    $x("//*[@class='vRadioButton'][./*[@for=\"LINK\"]]");
            public static final SelenideElement disableTips =
                    $x("//*[@class='vRadioButton'][./*[@for=\"DISABLED\"]]");
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

        }


    }

    public static class HistoryOperations {


        public static final SelenideElement operationsListTab =
                $x("//*[contains(@class,'VSectionOrderHistory__switching__tabs') " +
                        "and text()=\" Список операций \"]");

        public static final SelenideElement stuckTransactionTab =
                $x("//*[contains(@class,'VSectionOrderHistory__switching__tabs') " +
                        "and text()=\" Застрявшие транзакции \"]");
        public static final SelenideElement restaurantFilterButton =
                $x("//*[contains(@class,'VFiltersRestaurant__tabs')]" +
                        "[./div/./p[contains(text(),'Ресторан')]]");

        public static final SelenideElement tableNumberFilterButton =
                $x("//*[contains(@class,'VFiltersRestaurant__tabs')]" +
                        "[./div/./p[contains(text(),'Стол')]]");

        public static final SelenideElement orderStatusFilterButton =
                $x("//*[contains(@class,'VFiltersRestaurant__tabs')]" +
                        "[./div/./p[contains(text(),'Статус заказа УС')]]");

    }

    public static class Lock {

        public static final SelenideElement lockContainer = $(".plugForm");
        public static final SelenideElement lockInfoContainer = $(".plugFormInfo");
        public static final SelenideElement whereToLockContainer = $(".vPlugSelect");
        public static final SelenideElement amountRestaurantsToLock = $(".vPlugSelect__value");
        public static final SelenideElement whereToLockButton = $(".vPlugSelect>div");
        public static final SelenideElement chosenTypeOfLock = $(".vSelect__label");
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
                $x("//*[@class='vSelect__list']" +
                        "/*[contains(@class,'vSelect__item') and contains(text(),'Только оплату')]");


    }

    public static class Sending {

        public static final SelenideElement sendingContainer = $(".vSectionMailing__content");
        public static final SelenideElement sendingRecipientContainer = $(".vSectionMailing__recipient-info");
        public static final SelenideElement sendingMessageContainer= $(".vSectionMailing__message");
        public static final SelenideElement sendToAllContainer=
                $x("//*[@class='vSectionMailing__radio__cell'][.//*[@value='ALL']]");
        public static final SelenideElement sendToWaiterContainer=
                $x("//*[@class='vSectionMailing__radio__cell'][.//*[@value='WAITER']]");
        public static final SelenideElement sendToManagerContainer=
                $x("//*[@class='vSectionMailing__radio__cell'][.//*[@value='MANAGER']]");
        public static final SelenideElement messageTextArea= $(".vSectionMailing__message__textarea");
        public static final SelenideElement sendButton = $(".vButton");

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


}
