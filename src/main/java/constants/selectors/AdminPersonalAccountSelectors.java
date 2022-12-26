package constants.selectors;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class AdminPersonalAccountSelectors {

        public static class Common {

            public static final SelenideElement mainMenuIcon = $(".vProfileMenu__info");
            public static final SelenideElement exitFromAdmin = $(".vProfileMenu__exit");
            public static final ElementsCollection leftMenuCategory =
                $$x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]");
            public static final SelenideElement profileCategory =
                    $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                            "[.//*[text()='Профиль']]");
            public static final SelenideElement waiterMenuCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Официанты']]");
            public static final SelenideElement tablesAndQrCodesCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Столики и QR-коды']]");
            public static final SelenideElement operationsHistoryCategory =
                    $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                            "[.//*[text()='История операций']]");
            public static final SelenideElement menuCategory =
                    $x("//*[@class='vProfileMenu__list']" +
                            "/*[contains(@class,'VMenuProfileLink')][.//*[text()='Меню']]");
            public static final SelenideElement authPassword = $x("//td[*[contains(text(),'Пароль')]]");
            public static final SelenideElement configuration =
                $(".VMenuProfileLink:nth-of-type(2) .VMenuProfileLink__clickArea");
            public static final SelenideElement optionTabTips =
                $x("//*[@class='logsPage-tabs__btn'][text()='Чаевые']");
            public static final SelenideElement tipsDisabled = $("#DISABLED");
            public static final SelenideElement pageHeading = $(".vProfileHeader__breadcrumbs-item");
        }

        public static class Profile {

            public static final SelenideElement profileContainer = $(".section-profile__form");
            public static final SelenideElement profileTitle = $(".section-profile__title");
            public static final SelenideElement restaurantName = $("[id=\"name_shop\"]");
            public static final SelenideElement pagePreloader = $(".vPreloader");
            public static final SelenideElement adminName = $("[id=\"name\"]");
            public static final SelenideElement adminPhone = $("[id=\"phone\"]");
            public static final ElementsCollection telegramItems = $$(".section-profile__inputs-list-item");
            public static final ElementsCollection telegramItemsLogin =
                    $$(".section-profile__inputs-list-item [id='1']");
            public static final SelenideElement addTelegramLoginButton = $(".section-profile__add-btn");
            public static final SelenideElement adminEmail = $("[id=\"email\"]");
            public static final SelenideElement changedDataNotification =
                    $(".vAuthentication");
            public static final SelenideElement adminPassword = $("[id=\"password\"]");
            public static final SelenideElement adminPasswordConfirmation = $("[id=\"confirmation\"]");
            public static final SelenideElement saveButton = $(".section-profile__form  .vButton");
            public static final ElementsCollection telegramItemsIcons =
                    $$(".section-profile__inputs-list-item .vMessengerInput__telega");
            public static final ElementsCollection telegramItemsHelpTooltip =
                    $$(".section-profile__inputs-list-item .vMessengerInput__tooltip");
            public static final ElementsCollection telegramItemsHelp =
                    $$(".section-profile__inputs-list-item .vMessengerInput__help");
            public static final ElementsCollection telegramItemsCloseIcon =
                    $$(".section-profile__inputs-list-item .vMessengerInput-close");
            public static final SelenideElement privateDataContainer =
                    $x("//*[@class='section-profile__legend'][..//*[contains(text(),'Личные данные')]]");
            public static final SelenideElement personalInformationContainer =
                    $x("//*[@class='section-profile__legend']" +
                            "[..//*[contains(text(),'Персональная информация')]]");

        }

        public static class Waiters {

            public static final SelenideElement waiterListHeading =
                    $(".vSectionWaitersection .section-profile__title");
            public static final SelenideElement refreshListButton =
                    $(".vSectionWaitersection__btn");
            public static final ElementsCollection waiterPaginationList =
                    $$(".vPagination .vPagination__item");
            public static final SelenideElement pagePreloader = $(".vLightPreloader");
            public static final SelenideElement errorMsgEmailWasApplied =
                    $x("//*[@class='section-profile__group']" +
                            "[.//*[contains(text(),'Пригласить официанта в систему')]]//*[@class='vLandingInput__err']");
            public static final SelenideElement waiterContainer = $(".vSectionWaitersection__container");
            public static final SelenideElement searchField = $(".employeeSearch #search");
            public static final SelenideElement searchError = $(".vSectionWaitersection__empty-search");
            public static final SelenideElement searchResetButton =
                    $(".vSectionWaitersection__top .employeeSearch__btn-reset");
            public static final ElementsCollection waiterList =
                    $$(".vSectionWaitersection__container .vWaiterItem");
            public static final SelenideElement waiterCardName =
                    $(".vSectionWaitersection__container .vWaiterItem .vWaiterItem__waiterName");
            public static final SelenideElement inviteButton =
                    $(".vSectionWaitersection__container .vButton");
            public static final SelenideElement cancelInvitationButton =
                    $(".vSectionWaitersection__container .vButtonRed");
            public static final SelenideElement saveButton =
                    $x("//*[@class='vButton'][contains(text(),\"Сохранить\")]");
            public static final SelenideElement enterEmailFieldWrapper =
                    $x("//*[@class='vLandingInput__wrapper'][..//*[input[@id='email']]]");
            public static final SelenideElement enterEmailField = $(".section-profile__group #email");
            public static final SelenideElement waiterTelegramLogin = $("#loginTelegram");
            public static final SelenideElement waiterTelegramID = $("#idTelegram");
            public static final SelenideElement unlinkMailConfirmPopup = $(".vModalUnlinkEmail__content");
            public static final SelenideElement confirmUnlinkEmailButton =
                    $x("//*[@class='vModalUnlinkEmail__content']//button[@class='vButton']");
            public static final SelenideElement cancelMailConfirmPopup = $(".vModalCancelInvitation__content");
            public static final SelenideElement confirmCancelingEmailButton =
                    $x("//*[@class='vModalCancelInvitation__content']//button[@class='vButton']");
            public static final SelenideElement backToPreviousPage = $(".vSectionWaitersection__back");
            public static final SelenideElement waiterStatusInCard =
                    $(".vSectionWaitersection__container .waiter-status");
            public static final SelenideElement waiterStatusInPreview =
                    $(".vSectionWaitersection__container .vWaiterItem__waiterStatus");
            public static final SelenideElement waiterNameInCashDesk = $(".section-profile__group #name");
            public static final SelenideElement waiterName = $(".section-profile__group #display_name");
            public static final SelenideElement successSendingInvitation = $(".vAuthentication.active");
            public static final SelenideElement addPaymentCard =
                    $x("//*[@class='vButton'][contains(text(),\"Привязать карту\")]");

        }

        public static class TableAndQrCodes {

            public static final SelenideElement tableHeading =
                    $(".vSectionQr__header");
            public static final SelenideElement tablesAndQrCodesContainer =
                    $(".vSectionQr");
            public static final SelenideElement tableListHeading =
                    $(".vSectionQr .section-profile__title");
            public static final SelenideElement tableSearchFrom =
                    $("[class=\"vSectionQr__input\"][placeholder*=\"С\"]");
            public static final SelenideElement tableSearchTo =
                    $("[class=\"vSectionQr__input\"][placeholder*=\"По\"]");
            public static final SelenideElement findTableButton =
                    $("[class=\"vSectionQr__btn\"]");
            public static final SelenideElement resetTableButton =
                    $("[class=\"vSectionQr__btn vSectionQr__btn--red\"]");
            public static final ElementsCollection tableListItem =
                    $$(".vSectionQr__list .vSectionQr__item");
            public static final ElementsCollection paginationPages =
                    $$(".vPagination__list .vPagination__item");
            public static final SelenideElement backToTableList =
                    $(".vSectionQr__back");
            public static final SelenideElement tableItem =
                    $(".vSectionQr__item");
            public static final SelenideElement tableItemLink =
                    $(".vSectionQr__item a");
            public static final SelenideElement qrBlockWhite =
                    $("[class='vSectionQr__qr']");
            public static final SelenideElement qrBlockBlack =
                    $(".vSectionQr__qr--black");
            public static final SelenideElement qrDownloadImageWhite =
                    $("[class='vSectionQr__qr'] [class='vSectionQr__download']");
            public static final SelenideElement qrDownloadImageBlack =
                    $("[class='vSectionQr__qr vSectionQr__qr--black'] [class='vSectionQr__download']");
            public static final String qrWhiteImage =
                    "[class='vSectionQr__qr'] img";
            public static final String qrBlackImage =
                    "[class='vSectionQr__qr vSectionQr__qr--black'] img";

        }

        public static class Menu {

            public static final SelenideElement menuPagePreLoader = $(".vPreloader");
            public static final SelenideElement menuContainer = $(".vSectionMenuAdmin__container");
            public static final SelenideElement refreshMenuButton = $(".vAdminMenuAside .vButton");
            public static final SelenideElement categoryTitle = $(".vAdminMenuAside__title");
            public static final SelenideElement enableMenuForVisitorsContainer = $(".vAdminMenuVisitors");
            public static final SelenideElement enableMenuForVisitorsButton = $(".vAdminMenuVisitors__slider");
            public static final SelenideElement enableMenuForVisitorsInput =
                    $(".vAdminMenuVisitors__switch input");
            public static final SelenideElement categoryNameContainer = $(".vAdminMenuCategoryShield");
            public static final SelenideElement categoryNameInGuest =
                    $(".vAdminMenuCategoryShield__el:nth-child(2) .vAdminMenuCategoryShield__el-name");
            public static final SelenideElement viewSwitcherContainer = $(".vAdminMenuViews");
            public static final SelenideElement menuTableHeader = $(".vAdmiMenuTable__header");
            public static final ElementsCollection menuDishItems=
                    $$(".vAdminMenuMainAside__body .vAdmiMenuTable__item");
            public static final ElementsCollection menuDishItemsEditButtons =
                    $$(".vAdmiMenuTable__item-gramm+.vAdmiMenuTable__item-btn");
            public static final ElementsCollection menuDishItemsEyeIcons =
                    $$(".vAdmiMenuTable__item-btn+.vAdmiMenuTable__item-btn .vAdminMenuBtn");
            public static final ElementsCollection menuDishItemsEyeIconsActive =
                    $$(".vAdmiMenuTable__item-btn+.vAdmiMenuTable__item-btn .vAdminMenuBtn.active");
            public static final ElementsCollection menuDishItemsEditName =
                    $$(".vAdmiMenuTable__item-name+.vAdmiMenuTable__item-name");
            public static final ElementsCollection menuDishItemsNames =
                    $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-img+.vAdmiMenuTable__item-name");
            public static final ElementsCollection menuDishItemsPrices =
                    $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-price");
            public static final ElementsCollection menuDishItemsImageContainer =
                    $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-img");
            public static final ElementsCollection menuDishItemsImageInput =
                    $$(".vAdmiMenuTable__item-img .vAdmiMenuTable__upload");
            public static final ElementsCollection menuDishItemsImageDeleteImageIcon =
                    $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-img .vAdmiMenuTable__item-img-del");
            public static final ElementsCollection menuDishItemsImage =
                    $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-img img");
            public static final SelenideElement deleteImageContainer =
                    $(".vAdmiMenuTable__modal-close-content");
            public static final SelenideElement deleteImageContainerDeleteButton =
                    $(".vAdmiMenuTable__modal-close-content [class='vButton']");
            public static final SelenideElement deleteImageContainerCancelButton =
                    $(".vAdmiMenuTable__modal-close-content .vButton--gray");
            public static final SelenideElement imagePreviewContainer =
                    $(".vAdmiMenuTable__picture-modal-content");
            public static final SelenideElement imagePreviewContainerCloseButton =
                    $(".vAdmiMenuTable__picture-modal-content .vCloseButton");
            public static final SelenideElement imagePreviewContainerImage =
                    $(".vAdmiMenuTable__picture-modal-content img");
            public static final String imagePreviewContainerImageNotSelenide =
                   ".vAdmiMenuTable__picture-modal-content img";
            public static final SelenideElement editDishNameInput = $(".vAdmiMenuTable__input");
            public static final SelenideElement editDishNameOkButton =
                    $(".vAdmiMenuTable__ok");
            public static final SelenideElement dishPreloader =
                    $(".vSectionMenuAdmin__main-side .vLightPreloader");
            public static final SelenideElement helpAdminContainer = $(".vAdminMenuHelp__description");
            public static final SelenideElement showActivePositionsForGuestContainer =
                    $(".vAdminMenuAside__active");
            public static final SelenideElement showActivePositionsForGuestCheckbox = $("#showActive");
            public static final SelenideElement categoryContainer = $(".vAdminMenuAside__list");
            public static final ElementsCollection categoryItems =
                    $$("[class='vAdminMenuAside__category'] [class='vAdminMenuCategoryItem']");
            public static final ElementsCollection categoryItemsNames =
                    $$("[class='vAdminMenuAside__category'] " +
                            "[class='vAdminMenuCategoryItem'] .vAdminMenuCategoryItem__info-bottom");
            public static final ElementsCollection categoryItemsSize =
                    $$(".vAdminMenuCategoryItem__info-top");
            public static final ElementsCollection categoryEditButton =
                    $$(".vAdminMenuCategoryItem .vAdminMenuCategoryItem__btn:nth-child(2)");
            public static final ElementsCollection categoryEyeIcons =
                    $$("[class='vAdminMenuAside__category'] .vAdminMenuCategoryItem__btn:nth-child(3)");
            public static final SelenideElement editCategoryContainer = $(".vAdminMenuEditModal__content");
            public static final SelenideElement categoryNameForGuest = $("#ourName");
            public static final SelenideElement saveEditedCategoryNameButton =
                    $(".vAdminMenuEditModal__btn button");
            public static final SelenideElement cancelEditedCategoryNameButton =
                    $(".vAdminMenuEditModal__content .vButton--gray\n");
            public static final SelenideElement categoryNameInCashDesk = $(".vAdminMenuEditModal__name");

        }

        public static class OperationsHistory {

            public static final SelenideElement operationsHistoryContainer = $(".VSectionHistory");
            public static final SelenideElement operationsHistoryTitle = $(".section-profile__title");
            public static final SelenideElement forWeekPeriodButton =
                    $(".vHostoryBlockHeader__switcher [for=\"day\"]");
            public static final SelenideElement forMonthPeriodButton =
                    $(".vHostoryBlockHeader__switcher [for=\"month\"]");
            public static final ElementsCollection allPeriodButtons = $$(".vHostoryBlockHeader>div");
            public static final SelenideElement periodContainer = $(".vHostoryBlockHeader__period");
            public static final String periodButton = ".mx-input-wrapper";
            public static final SelenideElement leftArrowMonthPeriod =
                    $(".mx-calendar:first-child .mx-btn-icon-left");
            public static final SelenideElement firstDayOnMonthPeriod =
                    $$(".mx-calendar:first-child .mx-calendar-content td[class='cell']").first();
            public static final SelenideElement lastDayOnMonthPeriod =
                    $$(".mx-calendar:first-child .mx-calendar-content td[class='cell']").last();
            public static final SelenideElement resetPeriodButton = $(".vHostoryBlockHeader__period-reset");
            public static final SelenideElement historyTotalPeriodDate = $(".vHistoryTotalPeriod__date");
            public static final SelenideElement historyPeriodDate = $(".vHistoryList__date");
            public static final SelenideElement totalSum =
                    $x("//*[contains(@class,'vHistory') and contains(text(),'Итого:')]");
            public static final SelenideElement totalTips =
                    $x("//*[contains(@class,'vHistory') and contains(text(),'Чаевые:')]");
            public static final SelenideElement operationsHistoryPagePreloader = $(".vLightPreloader");
            public static final SelenideElement operationsHistoryListContainer =
                    $(".vHistoryRows");
            public static final ElementsCollection operationsHistoryListItems =
                    $$(".vHistoryRows li");
            public static final ElementsCollection operationsHistoryListItemsDate =
                    $$(".vHistoryRows li .vHistoryRows__col:nth-child(1) span");
            public static final ElementsCollection operationsHistoryListItemsWaiter =
                    $$(".vHistoryRows li .vHistoryRows__col:nth-child(2) p");
            public static final ElementsCollection operationsHistoryListItemsTable =
                    $$(".vHistoryRows li .vHistoryRows__col:nth-child(3) p");
            public static final ElementsCollection operationsHistoryListItemsTips =
                    $$(".vHistoryRows li .vHistoryRows__col:nth-child(4)");
            public static final ElementsCollection operationsHistoryListItemsStatus =
                    $$(".vHistoryRows li .vHistoryRows__col:nth-child(5) p");
            public static final ElementsCollection operationsHistoryListItemsSum =
                    $$(".vHistoryRows li .vHistoryRows__col:nth-child(6)");
            public static final SelenideElement paginationContainer =
                    $(".Pagination");
            public static final ElementsCollection paginationPages =
                    $$(".Pagination li:not([class])");

        }

}
