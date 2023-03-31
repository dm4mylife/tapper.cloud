package admin_personal_account;

import common.BaseActions;
import io.qameta.allure.Step;

import static data.selectors.AdminPersonalAccount.Common;
import static data.selectors.AdminPersonalAccount.Common.*;
import static data.selectors.AdminPersonalAccount.Waiters.enterEmailField;
import static data.selectors.AdminPersonalAccount.Waiters.waiterNameInCashDesk;


public class AdminAccount extends BaseActions {

    @Step("Выход из личного кабинета администратора")
    public void logOut() {

        click(mainMenuIcon);
        click(exitFromAdmin);
        isTextContainsInURL("tapper");

    }

    @Step("Раскрытие бокового навигационного меню")
    public void openedLeftMenu() {

        click(mainMenuIcon);
        isElementVisible(leftMenuOpened);

    }

    @Step("Проверка что авторизации и по данным и письма на почту")
    public void isRegistrationComplete() {

        isTextContainsInURL("profile");
        isElementVisible(enterEmailField);
        isElementVisible(waiterNameInCashDesk);

    }

}
