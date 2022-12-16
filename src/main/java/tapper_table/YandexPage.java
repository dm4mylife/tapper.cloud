package tapper_table;

import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static constants.Constant.TestData.*;
import static constants.TapperAdminSelectors.RKeeperAdmin.*;
import static constants.TapperAdminSelectors.YandexMail.*;


public class YandexPage extends BaseActions {

    @Step("Авторизация в яндексе")
    public void yandexAuthorization() {

        openPage(YANDEX_MAIL_URL);

        if (yandexFormTitle.getText().equals("Выберите аккаунт для входа")) {

            yandexTapperAccount.click();

        } else {

            click(enterByEmailButton);
            sendKeys(yandexLogin,TEST_YANDEX_LOGIN_EMAIL);
            click(signInButton);
            sendKeys(yandexPassword,TEST_YANDEX_PASSWORD_MAIL);
            click(signInButton);

        }

        isTextContainsInURL("https://mail.yandex.ru/");

    }

    @Step("Проверка письма таппера и извлечение пароля")
    public String checkTapperMail() {

        tapperMail.shouldBe(visible, Duration.ofSeconds(120));

        click(tapperMail);

        tapperConfirmAuthInMail.shouldBe(visible);

        String password = authPassword.getText().replaceAll("(\\n|.)+Пароль:\\s(.*)","$2");
        System.out.println(password + " пароль");

        return password;

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
