package data.selectors;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class AdminPersonalAccount {

    public static class Common {

        public static final SelenideElement mobileFooter = $(".vProfileMobileMenu");

        public static final SelenideElement mainMenuIcon = $(".vProfileMenu__info");
        public static final SelenideElement exitFromAdmin = $(".vProfileMenu__exit");
        public static final SelenideElement closeLeftMenu = $(".vProfileMenu__resize");

        public static final SelenideElement leftMenuOpened = $("[class='vProfileMenu']");
        public static final SelenideElement profileCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Профиль']]");

        public static final String integrationCategorySelector = "a[href*=\"integration\"]";


        public static final SelenideElement integrationCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Интеграции']]");
        public static final SelenideElement waiterMenuCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Официанты']]");
        public static final SelenideElement customizationCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Кастомизация']]");
        public static final SelenideElement tablesAndQrCodesCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Столики и QR-коды']]");
        public static final SelenideElement operationsHistoryCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='История операций']]");
        public static final SelenideElement companyRequisitesCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Реквизиты компании']]");
        public static final SelenideElement configNotifications =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]" +
                        "[.//*[text()='Настройка уведомлений']]");
        public static final SelenideElement menuCategory =
                $x("//*[@class='vProfileMenu__list']" +
                        "/*[contains(@class,'VMenuProfileLink')][.//*[text()='Меню']]");
        public static final SelenideElement tipsCategory =
                $x("//*[@class='vProfileMenu__list']" +
                        "/*[contains(@class,'VMenuProfileLink')][.//*[text()='Чаевые']]");
        public static final SelenideElement authPassword = $x("//td[*[contains(text(),'Пароль')]]");
        public static final SelenideElement configuration =
                $(".VMenuProfileLink:nth-of-type(2) .VMenuProfileLink__clickArea");
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
        public static final SelenideElement changedDataNotification = $(".vAuthentication");
        public static final SelenideElement adminPassword = $("[id=\"password\"]");
        public static final SelenideElement adminPasswordConfirmation = $("[id=\"confirmation\"]");
        public static final SelenideElement saveButton = $(".section-profile__form  .vButton");
        public static final ElementsCollection telegramItemsIcons =
                $$(".section-profile__inputs-list-item .vMessengerInput__telega");
        public static final By telegramItemsInput = By.cssSelector(".vMessengerInput__container input");
        public static final By telegramItemsInput2 = By.cssSelector(".vMessengerInput__container input");
        public static final ElementsCollection telegramItemsHelpTooltip =
                $$(".section-profile__inputs-list-item .vMessengerInput__tooltip");
        public static final ElementsCollection telegramItemsHelp =
                $$(".section-profile__inputs-list-item .vMessengerInput__help");
        public static final ElementsCollection telegramItemsCloseIcon =
                $$(".section-profile__inputs-list-item .vMessengerInput-close");
        public static final String telegramItemsCloseIconSelector = ".vMessengerInput-close";
        public static final String telegramItemsLoginInputSelector = "input";
        public static final SelenideElement privateDataContainer =
                $x("//*[@class='section-profile__legend'][..//*[contains(text(),'Личные данные')]]");


        public static final SelenideElement personalInformationContainer =
                $x("//*[@class='section-profile__legend']" +
                        "[..//*[contains(text(),'Персональная информация')]]");

        public static final ElementsCollection telegramItemsMasterIcon = $$(".section-profile__star");
        public static final String telegramItemsMasterSvgSelector = ".section-profile__star svg path";
        public static final ElementsCollection telegramItemsMasterSvg =
                $$(".section-profile__star svg path");

        public static final String telegramItemsMasterIconSelector = ".section-profile__star";

    }

    public static class Integrations {

        public static final SelenideElement integrationContainer = $(".vSectionIntegration.section-profile");
        public static final ElementsCollection integrationItems = $$(".vSectionIntegration__item");
        public static final ElementsCollection integrationItemsBtn = $$(".vSectionIntegration__btn");
        public static final ElementsCollection integrationItemsImg = $$(".vSectionIntegration__icon img");

    }

    public static class Waiters {

        public static final SelenideElement waiterListHeading =
                $(".vSectionWaitersection .section-profile__title");
        public static final SelenideElement refreshListButton = $(".vSectionWaitersection__btn");
        public static final ElementsCollection waiterPaginationList = $$(".vPagination .vPagination__item");
        public static final SelenideElement pagePreloader = $(".vLightPreloader");
        public static final By waiterAvatarBy = By.cssSelector(" .vWaiterItem__ava");


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
        public static final ElementsCollection waiterListName =
                $$(".vSectionWaitersection__container .vWaiterItem .vWaiterItem__waiterName");
        public static final By waiterListNameBy =
                By.cssSelector(".vSectionWaitersection__container .vWaiterItem .vWaiterItem__waiterName");
        public static final SelenideElement waiterCashDeskNameInCard =
                $x("//*[@class='vLandingInput'][.//*[@id='name']]");

        public static final SelenideElement inviteButton = $(".vSectionWaitersection__container .vButton");
        public static final SelenideElement cancelInvitationButton =
                $(".vSectionWaitersection__container .vButtonRed");
        public static final SelenideElement saveButton =
                $x("//*[@class='vButton'][contains(text(),\"Сохранить\")]");
        public static final SelenideElement enterEmailFieldWrapper =
                $x("//*[@class='vLandingInput__wrapper'][..//*[input[@id='email']]]");
        public static final SelenideElement enterEmailField = $(".section-profile__group #email");
        public static final SelenideElement wrongEmailError =
                $x("//*[@class='vLandingInput__err' and text()=' Введите корректный E-mail']");
        public static final SelenideElement unlinkMailConfirmPopup = $(".vModalUnlinkEmail");
        public static final SelenideElement unlinkEmailConfirmButton =
                $x("//*[@class='vModalUnlinkEmail']//button[@class='vButton']");
        public static final SelenideElement cancelMailConfirmationPopup = $(".vModalCancelInvitation__content");
        public static final SelenideElement cancelMailConfirmationSaveButton =
                $x("//*[@class='vModalCancelInvitation__content']//button[@class='vButton']");
        public static final SelenideElement backToPreviousPage = $(".vSectionWaitersection__back");
        public static final SelenideElement waiterStatusInCard =
                $(".vSectionWaitersection__container .waiter-status");
        public static final SelenideElement waiterStatusInPreview =
                $(".vSectionWaitersection__container .vWaiterItem__waiterStatus");
        public static final String waiterStatusSelector = ".vWaiterItem__waiterStatus";
        public static final SelenideElement waiterNameInCashDesk = $(".section-profile__group #name");
        public static final SelenideElement waiterName = $(".section-profile__group #display_name");
        public static final SelenideElement successSendingInvitation = $(".vAuthentication.active");
        public static final SelenideElement submitButton = $(".section-profile__form button");

    }

    public static class Customization {

        public static final SelenideElement wifiTab =
                $x("//*[contains(@class,'vSectionCustomization__switching-tabs') " +
                        "and contains(text(),'Информация о Wi-Fi')]");
        public static final SelenideElement callWaiterTab =
                $x("//*[contains(@class,'vSectionCustomization__switching-tabs') " +
                        "and contains(text(),'Вызов официанта')]");
        public static final SelenideElement reviewTab =
                $x("//*[contains(@class,'vSectionCustomization__switching-tabs') " +
                        "and contains(text(),'Отзывы на внешних сервисах')]");
        public static final SelenideElement wifiSlider =
                $(".vAdminDisplayingWiFi__switch");

        public static final SelenideElement wifiActivatedSlider =
                $(".vBlockWiFiInformation input[id='network']:not([disabled])");
        public static final SelenideElement wifiSliderInput =
                $(".vAdminDisplayingWiFi__switch input");
        public static final SelenideElement wifiDeactivatedSlider =
                $(".vBlockWiFiInformation input[id='network'][disabled]");
        public static final SelenideElement wifiNetworkName = $("[id=\"network\"]");
        public static final SelenideElement wifiNetworkPassword = $("[id=\"password\"]");
        public static final SelenideElement saveWifiButton = $(".vBlockWiFiInformation__container .vButton");
        public static final SelenideElement customizationContainer =
                $(".vSectionCustomization.section-profile");
        public static final SelenideElement toWhomSendMsgTitle = $(".vBlockCallingWaiter__recipient h3");
        public static final SelenideElement waiterAndManagerButton =
                $x("//div[../*[@value=\"WAITER_AND_MANAGER\"]]");
        public static final SelenideElement waiterAndManagerInput = $("[value=\"WAITER_AND_MANAGER\"]");
        public static final SelenideElement onlyManagerButton = $x("//div[../*[@value=\"MANAGER\"]]");
        public static final SelenideElement onlyManagerInput = $("[value=\"MANAGER\"]");

        public static final SelenideElement saveButton = $(".vButton");
        public static final SelenideElement recipientContainer = $(".vBlockCallingWaiter__recipient-info");
        public static final SelenideElement recipientWaiterIcon =
                $(".vBlockCallingWaiter__recipient-info g+rect");
        public static final SelenideElement recipientManagerIcon =
                $(".vBlockCallingWaiter__recipient-info g+rect+rect");


        public static final SelenideElement reviewContainer = $(".vBlockReview");

        public static final SelenideElement yandexInput = $("[id=\"yandex\"]");
        public static final SelenideElement twoGisInput = $("[id=\"twoGis\"]");
        public static final SelenideElement googleInput = $("[id=\"google\"]");
        public static final SelenideElement reviewInfo =
                $(".vSectionCustomization__reviewInfo .vBlockReviewInfo");
        public static final ElementsCollection reviewToggles =
                $$(".vSectionCustomization__reviewInfo .vBlockReviewInfo__toggle");
        public static final SelenideElement reviewToggle =
                $(".vSectionCustomization__reviewInfo .vBlockReviewInfo__toggle");
        public static final ElementsCollection reviewToggleInfo =
                $$(".vSectionCustomization__reviewInfo .vBlockReviewInfo__toggle .vBlockReviewInfo__toggle-list");

        public static final SelenideElement reviewToggleInfoElement =
                $(".vSectionCustomization__reviewInfo .vBlockReviewInfo__toggle .vBlockReviewInfo__toggle-list");
        public static final SelenideElement yandexTextLabel = $("[for=\"checkYandex\"]");
        public static final SelenideElement twoGisTextLabel = $("[for=\"checkTwoGis\"]");
        public static final SelenideElement googleTextLabel = $("[for=\"checkGoogle\"]");
        public static final SelenideElement yandexCheckboxContainer =
                $(".vLandingInput__wrapper [id=\"yandex\"]");
        public static final SelenideElement twoGisCheckboxContainer =
                $(".vLandingInput__wrapper [id=\"twoGis\"]");
        public static final SelenideElement googleCheckboxContainer =
                $(".vLandingInput__wrapper [id=\"google\"]");
        public static final SelenideElement yandexCheckbox = $("[id=\"checkYandex\"]");
        public static final SelenideElement twoGisCheckbox = $("[id=\"checkTwoGis\"]");
        public static final SelenideElement googleCheckbox = $("[id=\"checkGoogle\"]");
        public static final SelenideElement errorEmailInput = $(".vLandingInput__err");

    }

    public static class TableAndQrCodes {

        public static final SelenideElement tableHeading = $(".vSectionQr__header");
        public static final SelenideElement tablesAndQrCodesContainer = $(".vSectionQr");
        public static final SelenideElement tableListHeading = $(".vSectionQr .section-profile__title");
        public static final SelenideElement tableAndQRCodesPreloader = $(".vSectionQr__preloader");

        public static final SelenideElement tableSearchFrom =
                $("[class=\"vSectionQr__input\"][placeholder*=\"С\"]");
        public static final SelenideElement tableSearchTo =
                $("[class=\"vSectionQr__input\"][placeholder*=\"По\"]");
        public static final SelenideElement findTableButton = $("[class=\"vSectionQr__btn\"]");
        public static final SelenideElement resetTableButton =
                $("[class=\"vSectionQr__btn vSectionQr__btn--red\"]");
        public static final ElementsCollection tableListItem =
                $$(".vSectionQr__list .vSectionQr__item");
        public static final ElementsCollection paginationPages =
                $$(".vPagination__list .vPagination__item");
        public static final SelenideElement backToTableList = $(".vSectionQr__back");
        public static final SelenideElement tableItem = $(".vSectionQr__item");
        public static final SelenideElement tableItemLink = $(".vSectionQr__item a");
        public static final SelenideElement qrBlockWhite = $("[class='vSectionQr__qr']");
        public static final SelenideElement qrBlockBlack = $(".vSectionQr__qr--black");
        public static final SelenideElement qrDownloadImageWhite =
                $("[class='vSectionQr__qr'] [class='vSectionQr__download']");
        public static final SelenideElement qrDownloadImageBlack =
                $(".vSectionQr__qr--black .vSectionQr__download");
        public static final String qrWhiteImage = "[class='vSectionQr__qr'] img";
        public static final String qrBlackImage = "[class='vSectionQr__qr vSectionQr__qr--black'] img";

    }

    public static class Menu {



        public static final SelenideElement menuMobilePlug = $(".vSectionMenuAdmin__zaglushka");
        public static final SelenideElement menuPagePreLoader = $(".vPreloader");
        public static final SelenideElement menuDishListPreLoader = $(".vLightPreloader.notFixed");


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
        public static final ElementsCollection menuTableHeaderItems = $$(".vAdmiMenuTable__header-item");
        public static final SelenideElement listViewButton = $(".vAdminMenuViews__el:first-child");
        public static final SelenideElement tileViewButton = $(".vAdminMenuViews__el:last-child");
        public static final ElementsCollection dishCardContainer = $$(".vAdmiMenuTable__item-grid");
        public static final SelenideElement cancelConfirmationContainer =
                $(".modal__confirmation .modal__content");
        public static final SelenideElement cancelConfirmationYesButton =
                $(".modal__confirmation [class='vButton']");
        public static final SelenideElement cancelConfirmationNoButton =
                $(".modal__confirmation .vButton--gray");
        public static final SelenideElement deleteDishImageButton = $(".image__delete-icon");
        public static final SelenideElement deleteImageContainer = $(".modal__confirmation .modal__content");
        public static final SelenideElement imagePreviewIcon = $(".image__preview .image__preview-icon");
        public static final SelenideElement imageUploadInput = $("[ id=\"uploadImg\"]");
        public static final SelenideElement downloadedPreviewImage = $(".image__lot-img");



        public static final SelenideElement deleteImageContainerDeleteButton =
                $(".modal__confirmation [class='vButton']");
        public static final SelenideElement deleteImageContainerCancelButton =
                $(".modal__confirmation .vButton--gray");
        public static final SelenideElement imagePreviewContainer =
                $(".vAdmiMenuTable__picture-modal-content");
        public static final SelenideElement imagePreviewContainerCloseButton =
                $(".vAdmiMenuTable__picture-modal-content .vCloseButton");
        public static final SelenideElement imagePreviewContainerImage =
                $(".vAdmiMenuTable__picture-modal-content img");
        public static final SelenideElement dishPreloader =
                $(".vSectionMenuAdmin__main-side .vLightPreloader");
        public static final SelenideElement helpAdminContainer = $(".vAdminMenuHelp__description");
        public static final SelenideElement showActivePositionsForGuestContainer =
                $(".vAdminMenuAside__active");
        public static final String categoryItemsCheckboxSelector = ".vCheckbox__container";
        public static final SelenideElement showActivePositionsForGuestCheckbox = $("#showActive");
        public static final SelenideElement categoryContainer = $(".vAdminMenuAside__list");
        public static final ElementsCollection categoryItems =
                $$("[class='vAdminMenuAside__category'] [class='vAdminMenuCategoryItem']");
        public static final ElementsCollection categoryItemsNames =
                $$("[class='vAdminMenuAside__category'] " +
                        "[class='vAdminMenuCategoryItem'] .vAdminMenuCategoryItem__info-bottom");
        public static final String categoryItemsNamesSelector = ".vAdminMenuCategoryItem__info-bottom";
        public static final ElementsCollection categoryItemsSize =
                $$(".vAdminMenuCategoryItem__info-top");
        public static final ElementsCollection categoryEditButton =
                $$(".vAdminMenuCategoryItem .vAdminMenuCategoryItem__btn:nth-child(2)");

        public static final String categoryEditButtonSelector =
                ".vAdminMenuCategoryItem__btn:nth-child(2)";
        public static final ElementsCollection categoryEyeIcons =
                $$("[class='vAdminMenuAside__category'] .vAdminMenuCategoryItem__btn:nth-child(3)");

        public static final ElementsCollection notAutoMenuCategory =
                $$x("//*[@class='vAdminMenuCategoryItem']" +
                        "[..//*[@class='vAdminMenuCategoryItem__info-bottom'" +
                        " and not(contains(text(),'Не трогать эту категорию'))]]");



        public static final String categoryEyeIconSelector = ".--eye";

        public static final String categoryNameTotalSelector = ".vAdminMenuCategoryItem__info";
        public static final String categoryNameSelector = ".vAdminMenuCategoryItem__info-bottom";
        public static final SelenideElement editCategoryContainer = $(".vAdminMenuEditModal__content");
        public static final SelenideElement categoryNameForGuest = $("#ourName");
        public static final SelenideElement saveEditedCategoryNameButton =
                $(".vAdminMenuEditModal__btn button");
        public static final SelenideElement cancelEditedCategoryNameButton =
                $(".vAdminMenuEditModal__content .vButton--gray\n");
        public static final SelenideElement categoryNameInCashDesk = $(".vAdminMenuEditModal__name");
        public static final ElementsCollection menuDishItems =
                $$(".vAdminMenuMainAside__body .vAdmiMenuTable__item");
        public static final ElementsCollection menuDishItemsEditButtons =
                $$(".vAdmiMenuTable__item-btn .--edit");
        public static final ElementsCollection menuDishItemsEyeIcons =
                $$(".vAdmiMenuTable__item-btn+.vAdmiMenuTable__item-btn .vAdminMenuBtn");
        public static final ElementsCollection menuDishItemsNames =
                $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-img+.vAdmiMenuTable__item-name");
        public static final ElementsCollection menuDishItemsPrices =
                $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-price");
        public static final SelenideElement menuDishIndexNumber =
                $(".vAdmiMenuTable__item .vAdmiMenuTable__item-num");
        public static final ElementsCollection menuDishItemsImageContainer =
                $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-img");
        public static final ElementsCollection menuDishItemsImage =
                $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-img img");
        public static final String menuDishItemsImageInDishListSelector =
                ".vAdmiMenuTable__item .vAdmiMenuTable__item-img img";
        public static final String menuDishItemsImageInPreviewDishSelector = ".vAdmiMenuTable__picture-modal img";
        public static final String editDishImageSelector = ".image__lot img";
        public static final SelenideElement categoryBlockMenuDishNameOnCashDesk =
                $x("//*[@class='vAdminMenuCategoryShield__el-title' and text()=' Название на кассе ']" +
                        "/following-sibling::div");
        public static final SelenideElement categoryBlockMenuDishNameOnGuest =
                $x("//*[@class='vAdminMenuCategoryShield__el-title' and text()=' Название у гостя ']" +
                        "/following-sibling::div");
        public static final ElementsCollection menuDishNameAtCashDesk =
                $$(".vAdmiMenuTable__item [class='vAdmiMenuTable__item-name']");
        public static final ElementsCollection menuDishNameByGuest =
                $$(".vAdmiMenuTable__item-name.vAdmiMenuTable__item-our-name");
        public static final ElementsCollection menuDishIngredients =
                $$(".vAdmiMenuTable__item-name.vAdmiMenuTable__item-ingredients");
        public static final ElementsCollection menuDishPrice =
                $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-price");
        public static final ElementsCollection menuDishWeight =
                $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-gramm");
        public static final ElementsCollection menuDishCalories =
                $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-gramm+.vAdmiMenuTable__item-gramm");
        public static final ElementsCollection menuDishMark =
                $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-mark");
        public static final SelenideElement editDishContainerCloseButton = $(".modal .modal__close");
        public static final SelenideElement editDishContainer = $(".modal .modal__content");
        public static final SelenideElement imageContainer = $("[for=\"uploadImg\"]");
        public static final SelenideElement editPhotoButton = $(".modal__image .image__text");
        public static final SelenideElement editDishNameByCashDeskInput = $("[id=\"restName\"]");
        public static final SelenideElement editDishNameByGuestInput = $("[id=\"ourName\"]");
        public static final SelenideElement editDishNameByCashDeskInputCounter =
                $("[id=\"ourName\"]+div+span");
        public static final SelenideElement editDishDescriptionInput = $("[id=\"description\"]");
        public static final SelenideElement editDishDescriptionInputCounter =
                $("[id=\"description\"]+div+span");
        public static final SelenideElement editDishIngredientsInput = $("[id=\"composition\"]");
        public static final SelenideElement editDishIngredientsInputCounter =
                $("[id=\"composition\"]+div+span");
        public static final SelenideElement editDishAmountInput = $("[id=\"count\"]");
        public static final SelenideElement editDishAmountInputCounter = $("[id=\"count\"]+div+span");
        public static final SelenideElement editDishMeasureUnitInput =
                $x("//*[contains(@class,'vSelect')][./*[text()='Ед. измерения']]" +
                        "//*[@class='vSelect__label']");
        public static final ElementsCollection editDishMeasureUnitInputOptions = $$(".modal__row .vSelect li");
        public static final SelenideElement editDishAmountErrorInput =
                $x("//*[@class='vLimitedInput__wrapper'][..//input[@id='count']]/following::div[1]");
        public static final SelenideElement editDishAmountErrorContainer =
                $x("//*[@class='vLimitedInput__wrapper'][..//input[@id='count']]");
        public static final SelenideElement editDishMeasureUnitErrorContainer =
                $(".vSelect__err");
        public static final SelenideElement editDishCaloriesInput = $("[id=\"calories\"]");
        public static final SelenideElement editDishCaloriesInputCounter = $("[id=\"calories\"]+div+span");
        public static final SelenideElement editDishMarksSelect =
                $(".modal__input-wrap.modal__field-label+.modal__input-wrap .vSelect .vSelect__label");
        public static final ElementsCollection editDishMarksSelectOptions =
                $$(".modal__input-wrap.modal__field-label+.modal__input-wrap .vSelect .vSelect__item");
        public static final SelenideElement saveButton = $(".modal button[class=vButton]");
        public static final SelenideElement cancelButton = $(".modal .vButton.vButton--gray");

    }

    public static class OperationsHistory {

        public static final SelenideElement operationsHistoryContainer = $(".VSectionHistory");
        public static final SelenideElement forWeekPeriodButton =
                $(".vHistoryBlockHeader__switcher [for=\"day\"]");
        public static final SelenideElement forMonthPeriodButton =
                $(".vHistoryBlockHeader__switcher [for=\"month\"]");
        public static final ElementsCollection allPeriodButtons = $$(".vHistoryBlockHeader>div");
        public static final SelenideElement dateRangeContainer = $(".vHistoryBlockHeader__period");
        public static final String periodButton = ".mx-input-wrapper";
        public static final SelenideElement leftArrowMonthPeriod =
                $(".mx-calendar:first-child .mx-btn-icon-left");
        public static final SelenideElement currentMonth =
                $(".mx-calendar-panel-date:first-child .mx-calendar-header .mx-calendar-header-label " +
                        ".mx-btn-current-month");
        public static final ElementsCollection daysOnMonthPeriod = $$(".mx-calendar:first-child " +
                ".mx-calendar-content tbody tr td:not([class*='not-current-month'])");
        public static final SelenideElement resetPeriodButton = $(".vHistoryBlockHeader__period-reset");
        public static final SelenideElement historyTotalPeriodDate = $(".vHistoryTotalPeriod__date");
        public static final SelenideElement historyPeriodDate = $(".vHistoryList__date");
        public static final By historyPeriodDateBy = By.cssSelector(".vHistoryList__date");

        public static final SelenideElement totalSum =
                $x("//*[contains(@class,'vHistory') and contains(text(),'Итого:')]");

        public static final By totalSumBy =
                By.xpath("//*[@class='vHistoryList__el' and contains(text(),'Итого')]/span");

        public static final SelenideElement totalTips =
                $x("//*[contains(@class,'vHistory') and contains(text(),'Чаевые:')]");

        public static final By totalTipsBy =
                By.xpath("//*[@class='vHistoryList__el' and contains(text(),'Чаевые')]/span");
        public static final SelenideElement operationsHistoryPagePreloader = $(".vLightPreloader");

        public static final SelenideElement emptyOperationContainer =
                $(".VSectionHistory__block > div:not([class])");

        public static final SelenideElement operationsHistoryListContainer = $(".vHistoryRows");
        public static final ElementsCollection operationsHistoryListItems = $$(".vHistoryRows li");
        public static final ElementsCollection operationsHistoryListItemsDate =
                $$(".vHistoryRows li .vHistoryRows__col:nth-child(1) span");
        public static final By operationsHistoryListItemsDateBy =
                By.cssSelector(".vHistoryRows li .vHistoryRows__col:nth-child(1)");
        public static final ElementsCollection operationsHistoryListItemsWaiter =
                $$(".vHistoryRows li .vHistoryRows__col:nth-child(2) p");

        public static final By operationsHistoryListItemsWaiterBy =
                By.cssSelector(".vHistoryRows li .vHistoryRows__col:nth-child(2) p");
        public static final ElementsCollection operationsHistoryListItemsTable =
                $$(".vHistoryRows li .vHistoryRows__col:nth-child(3) p");

        public static final By operationsHistoryListItemsTableBy =
                By.cssSelector(".vHistoryRows li .vHistoryRows__col:nth-child(3) p");
        public static final ElementsCollection operationsHistoryListItemsTips =
                $$(".vHistoryRows li .vHistoryRows__col:nth-child(4)");
        public static final By operationsHistoryListItemsTipsBy =
                By.cssSelector(".vHistoryRows li .vHistoryRows__col:nth-child(4)");
        public static final ElementsCollection operationsHistoryListItemsStatus =
                $$(".vHistoryRows li .vHistoryRows__col:nth-child(5) p");
        public static final By operationsHistoryListItemsStatusBy =
                By.cssSelector(".vHistoryRows li .vHistoryRows__col:nth-child(5) p");
        public static final ElementsCollection operationsHistoryListItemsSum =
                $$(".vHistoryRows li .vHistoryRows__col:nth-child(6)");
        public static final By operationsHistoryListItemsSumBy =
                By.cssSelector(".vHistoryRows li .vHistoryRows__col:nth-child(6)");
        public static final SelenideElement paginationContainer =
                $("div>.Pagination");
        public static final ElementsCollection paginationPages =
                $$("div>.Pagination li:not([class])");

    }

    public static class CompanyRequisites {

        public static final SelenideElement requisitesContainer = $(".sectionProfileReq");
        public static final SelenideElement organizationName = $("[id=\"name_organization\"]");
        public static final SelenideElement innOrganization = $("[id=\"inn\"]");
        public static final SelenideElement phoneOrganization = $("[id=\"phone\"]");
        public static final SelenideElement saveButton = $(".vButton");

    }

    public static class ConfigNotifications {

        public static final SelenideElement configNotificationsContainer = $(".sectionTelegram");
        public static final SelenideElement legendContainer =
                $(".sectionTelegram [class='section-profile__legend']");
        public static final ElementsCollection telegramLoginInputs = $$(".vLandingInput__wrapper input");
        public static final ElementsCollection telegramLoginSettingsSvg = $$(".telegram__settings svg");
        public static final ElementsCollection telegramLoginSettingsDeleteIcon = $$(".telegram__del svg");
        public static final SelenideElement addButton = $(".sectionTelegram .addButton");
        public static final SelenideElement saveButton = $(".sectionTelegram .vButton");
        public static final SelenideElement deleteTelegramContainer = $(".sectionTelegram-modal__content");
        public static final SelenideElement deleteTelegramContainerUnlinkButton =
                $(".sectionTelegram-modal__content [class='vButton']");
        public static final SelenideElement deleteTelegramContainerCancelButton =
                $(".sectionTelegram-modal__content .vButton--gray");
        public static final ElementsCollection typeNotificationList =
                $$(".sectionTelegram-modal__checkBoxList li.sectionTelegram-modal__checkboxItem");
        public static final SelenideElement settingsContainerSaveButton =
                $x("//*[contains(@class,'vButton') and text()=' Применить ']");
        public static final SelenideElement typeCallWaiterNotificationOption =
                $x("//*[contains(@class,'sectionTelegram-modal__checkboxItem')" +
                        " and text()=' Сообщения о вызове официанта ']");
        public static final SelenideElement typeRatingNotificationOption =
                $x("//*[contains(@class,'sectionTelegram-modal__checkboxItem')" +
                        " and text()=' Сообщения о рейтинге и отзывах ']");
        public static final SelenideElement typePaymentNotificationOption =
                $x("//*[contains(@class,'sectionTelegram-modal__checkboxItem')" +
                        " and text()=' Сообщения об оплатах ']");

        public static final SelenideElement deleteTelegramContainerCloseButton =
                $(".sectionTelegram-modal__close");
        public static final SelenideElement errorTextInLoginTelegram =
                $(".vLandingInput.err .vLandingInput__err");

    }

    public static class Tips {

        public static final SelenideElement tipsContainer = $(".vBlockTipsTeam");
        public static final SelenideElement tipsInfoContainer = $(".vSectionTips__content [class$='__info']");
        public static final SelenideElement imageContainer = $(".section-profile__photo-block");
        public static final SelenideElement avatarStub =
                $(".section-profile__photo-wrapper img[src*=v-icon-avatar]");
        public static final SelenideElement avatarImage =
                $(".section-profile__photo-wrapper img:not([src*=v-icon-avatar])");
        public static final String avatarImgSelector = ".section-profile__photo-wrapper img";

        public static final SelenideElement deleteAvatarButton = $(".section-profile__photo-del");
        public static final SelenideElement tipsTypeButtons = $(".vSectionTips__switching-tabs");
        public static final SelenideElement kitchenTipsButton =
                $x("//*[contains(@class,'vSectionTips__switching-tabs') and contains(.,'Чаевые кухне')]");
        public static final SelenideElement downloadAvatarInput = $(".section-profile__file");

        public static final SelenideElement linCreditCardButton =
                $x("//button[text()=' Привязать карту ']");

        public static final SelenideElement hookahNameContainer = $("div>[id=\"nameHookah\"]");
        public static final SelenideElement hookahNameInput = $("[id=\"nameHookah\"]");
        public static final SelenideElement hookahGoaContainer = $("div>[id=\"purposeHookah\"]");
        public static final SelenideElement inputError = $(".vLandingInput.err");



        public static final SelenideElement hookahGoalInput = $("[id=\"purposeHookah\"]");
        public static final SelenideElement kitchenGoalContainer = $("div>[id=\"purposeKitchen\"]");

        public static final SelenideElement kitchenGoalInput = $("[id=\"purposeKitchen\"]");
        public static final SelenideElement tipsCheckbox = $(".vBlockCheckboxTips .vClickedCheckbox__view");

        public static final SelenideElement maxGoalCharsCounter = $("[class$='__input-number']");
        public static final SelenideElement changeCreditCard = $("[class$='__change-map']");

        public static final SelenideElement linkedCardButton =
                $x("//*[contains(@class,'vButton')][contains(.,'Карта привязана')]");
        public static final SelenideElement toLinkCardButton =
                $x("//*[contains(@class,'vButton')][contains(.,'Привязать карту')]");
        public static final SelenideElement saveButton =
                $x("//*[contains(@class,'vButton')][contains(.,'Сохранить')]");
        public static final String choseDateButtonSelector = ".mx-datepicker .mx-input-wrapper";
        public static final SelenideElement downloadTable = $(".vButton");



    }

}
