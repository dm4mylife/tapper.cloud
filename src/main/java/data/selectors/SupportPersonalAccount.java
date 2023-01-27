package data.selectors;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

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

        public static final SelenideElement logsContainer = $(".logsPage");
        public static final SelenideElement pagePreloader = $(".vLightPreloader");
        public static final SelenideElement logsTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Логи')]");
        public static final SelenideElement permissionsTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Доступы')]");
        public static final SelenideElement licenseIdTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'id лицензии')]");
        public static final SelenideElement cashDeskTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'r-keeper/iiko')]");
        public static final SelenideElement operationSTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Операции')]");
        public static final SelenideElement acquiringTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Эквайринг')]");
        public static final SelenideElement loaderTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Лоадер')]");
        public static final SelenideElement statisticsTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Статистика')]");
        public static final SelenideElement tipsTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Чаевые')]");
        public static final SelenideElement customizationTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Кастомизация')]");
        public static final SelenideElement waiterTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Официанты')]");
        public static final SelenideElement tablesTab =
                $x("//*[contains(@class,'logsPage-tabs__btn') and contains(text(),'Столы')]");


    }

    public static class HistoryOperations {




    }

    public static class Lock {




    }

    public static class Sending {




    }

    public static class CshDeskInaccessibility {




    }



}
