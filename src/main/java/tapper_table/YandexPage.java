package tapper_table;

import com.codeborne.selenide.SelenideElement;
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

        }  else if (enteredEarlierLogin.isDisplayed()) {

            sendKeys(yandexPassword,password);
            click(signInButton);

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

        tapperMail.shouldBe(visible.because("На почте нет письма с приглашением на авторизацию"),
                Duration.ofSeconds(30));

        click(tapperMail);

        tapperConfirmAuthInMail.shouldBe(visible);
        System.out.println("Ссылка на регистрацию из почты:\n" + tapperConfirmAuthInMail.getAttribute("href"));

        String password = Common.authPassword.getText().replaceAll("(\\n|.)+Пароль:\\s(.*)","$2");
        System.out.println(password + " пароль");

        return password;

    }

    @Step("Переход на страницу авторизации по ссылки из письма с приглашением")
    public void goToAuthPageFromMail() {

        tapperConfirmAuthInMail.click();
        switchTab(1);
        isTextContainsInURL("tapper");
        System.out.println("Переключились на новую вкладку");

    }

    @Step("Удаление письма из мыла")
    public void deleteMail(String email, String password) {

        yandexAuthorization(email,password);

        tapperMailCheckbox.first().shouldBe(visible);

        if (tapperMailCheckbox.first().isDisplayed()) {

            System.out.println("gg");

            for (SelenideElement mailCheckbox : tapperMailCheckbox) {

                click(mailCheckbox);

            }

            click(deleteMailButton);
            tapperMail.shouldNotBe(exist);
            System.out.println("Удалили письмо");

        }

    }

}
