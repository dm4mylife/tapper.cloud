package tapper_table;

import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.TestData.Yandex.YANDEX_MAIL_URL;
import static data.selectors.AdminPersonalAccount.Common;
import static data.selectors.YandexMail.*;

public class YandexPage extends BaseActions {

    @Step("Авторизация в яндексе")
    public void yandexAuthorization(String email, String password) {

        openPage(YANDEX_MAIL_URL);

        if (yandexFormTitle.getText().equals("Выберите аккаунт для входа")) {

            yandexTapperAccount.click();

        } else {

            click(enterByEmailButton);
            sendKeys(yandexLogin,email);
            click(signInButton);
            sendKeys(yandexPassword,password);
            click(signInButton);

        }

        if (skipAddReservePassportContainer.isDisplayed()) {

            skipButton.click();

        } else if (skipAddReserveEmail.isDisplayed() && skipAddReserveEmail.getText().matches("Если потеряете доступ")) {

            skipButton.click();

        }

        isTextContainsInURL("yandex.ru");

    }

    @Step("Проверка письма таппера и извлечение пароля")
    public String checkTapperMail() {

        tapperMail.shouldBe(visible, Duration.ofSeconds(60));

        click(tapperMail);

        tapperConfirmAuthInMail.shouldBe(visible);

        String password = Common.authPassword.getText().replaceAll("(\\n|.)+Пароль:\\s(.*)","$2");
        System.out.println(password + " пароль");

        return password;

    }

    @Step("Переход на страницу авторизации из письма")
    public void goToAuthPageFromMail() {

        tapperConfirmAuthInMail.click();
        isTextContainsInURL("tapper");
        switchTab(1);
        System.out.println("Переключились на новую вкладку");

    }

    @Step("Удаление письма из мыла")
    public void deleteMail(String email, String password) {

        yandexAuthorization(email,password);
        tapperMailCheckbox.shouldBe(visible,Duration.ofSeconds(20));
        click(tapperMailCheckbox);
        click(deleteMailButton);
        tapperMail.shouldNotBe(exist);
        System.out.println("Удалили письмо");

    }

}
