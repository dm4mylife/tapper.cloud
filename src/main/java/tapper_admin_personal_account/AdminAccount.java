package tapper_admin_personal_account;

import common.BaseActions;
import io.qameta.allure.Step;

import static constants.selectors.AdminPersonalAccountSelectors.*;
import static constants.selectors.AdminPersonalAccountSelectors.Waiters.*;


public class AdminAccount extends BaseActions {

    @Step("Выход из личного кабинета администратора")
    public void logOut() {

        click(Common.mainMenuIcon);
        click(Common.exitFromAdmin);
        isTextContainsInURL("https://tapper.staging.zedform.ru/");

    }

    @Step("Проверка что авторизации и по данным и письма на почту")
    public void isRegistrationComplete() {

        isElementVisible(enterEmailField);
        isElementVisible(waiterNameInCashDesk);
        forceWait(2000); //toDO тест очень быстро идёт, визуально не понятно что тут успех)

    }



}
