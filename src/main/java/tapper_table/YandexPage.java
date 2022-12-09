package tapper_table;

import common.BaseActions;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static constants.Constant.TestData.*;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_SUPPORT_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_SUPPORT_PASSWORD;
import static constants.SelectorsTapperAdmin.AuthorizationPage.emailInput;
import static constants.SelectorsTapperAdmin.AuthorizationPage.passwordInput;
import static constants.SelectorsTapperAdmin.RKeeperAdmin.*;
import static constants.SelectorsTapperAdmin.RegistrationPage.*;
import static constants.SelectorsTapperAdmin.YandexMail.*;


public class YandexPage extends BaseActions {

    @Step("Авторизация в яндексе")
    public void yandexAuthorization() {

        openPage(YANDEX_MAIL_URL);
        sendKeys(yandexLogin,TEST_YANDEX_LOGIN_EMAIL);
        click(signInButton);
        sendKeys(yandexPassword,TEST_YANDEX_PASSWORD_MAIL);
        click(signInButton);
        isTextContainsInURL("https://mail.yandex.ru/");

    }

    @Step("Проверка письма таппера и извлечение пароля")
    public String checkTapperMail() {

        tapperMail.shouldBe(visible);
        System.out.println("письмо есть");

        click(tapperMail);
        System.out.println("клик в письмо");

        tapperConfirmAuthInMail.shouldBe(visible);

        String yandexPass = authPassword.getText();
        System.out.println(yandexPass + " пароль взяли гразный" );

        yandexPass = yandexPass.trim();
        System.out.println(yandexPass + " тримим");

        yandexPass = yandexPass.replaceAll("\\S+\\-[^\\s\\\\n]+","");
        System.out.println(yandexPass + "реджексим");

        return yandexPass;

    }

    @Step("Переход на страницу авторизации из письма")
    public void goToAuthPageFromMail() {

        tapperConfirmAuthInMail.click();
        switchTab(1);
        System.out.println("Переключились на новую вкладку");

    }

    @Step("Удаление письма из мыла")
    public void deleteMail() {

        yandexAuthorization();
        click(tapperMailCheckbox);
        click(deleteMailButton);
        tapperMail.shouldNotBe(exist);
        System.out.println("Удалили письмо");

    }

}
