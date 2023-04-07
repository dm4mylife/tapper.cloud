package tapper_table;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.Yandex.YANDEX_MAIL_URL;
import static data.selectors.AdminPersonalAccount.Common;
import static data.selectors.YandexMail.*;

public class YandexPage extends BaseActions {

    RootPage rootPage = new RootPage();

    @Step("Авторизация в яндексе")
    public void yandexAuthorization(String email, String password) {

        openPage(YANDEX_MAIL_URL);

        if (yandexFormTitle.getText().equals("Выберите аккаунт для входа")) {

            yandexTapperAccount.click();

        }  else if (enteredEarlierLogin.isDisplayed()) {

            sendKeys(yandexPassword,password);
            click(signInButton);

        }  else {

            click(enterByEmailButton);
            sendKeys(yandexLogin,email);
            click(signInButton);
            sendKeys(yandexPassword,password);
            click(signInButton);

        }

        if (skipAddReservePassportContainer.isDisplayed()) {

            skipButton.click();

        } else if (skipAddReserveEmail.isDisplayed() &&
                skipAddReserveEmail.getText().matches("Если потеряете доступ")) {

            skipButton.click();

        }  else if (attachPhoto.isDisplayed()) {

            skipButtonWhenAddPhoto.click();

        }

        isTextContainsInURL("yandex.ru");

    }

    @Step("Проверка письма таппера и извлечение пароля")
    public String checkTapperMail() {

        tapperMail.shouldBe(visible.because("На почте нет письма с приглашением на авторизацию"),
                Duration.ofSeconds(120));

        click(tapperMail);

        tapperConfirmAuthInMail.shouldBe(visible);

        return Common.authPassword.getText().replaceAll("(\\n|.)+Пароль:\\s(.*)","$2");

    }

    @Step("Проверка письма таппера и извлечение пароля")
    public void checkRecoverMail() {

        tapperMail.shouldBe(visible.because("На почте нет письма с приглашением на авторизацию"),
                Duration.ofSeconds(120));

        click(recoveryMail);

        recoveryMailLinkInMail.shouldBe(visible).click();
        switchTab(1);
        isTextContainsInURL("users/forget");

    }


    @Step("Переход на страницу авторизации по ссылки из письма с приглашением")
    public void goToAuthPageFromMail() {

        tapperConfirmAuthInMail.click();
        rootPage.switchBrowserTab(1);


    }

    @Step("Удаление письма из мыла")
    public void deleteMail(String email, String password, ElementsCollection checkboxes, SelenideElement element) {

        yandexAuthorization(email,password);

        checkboxes.first().shouldBe(visible);

        if (checkboxes.first().isDisplayed()) {

            checkboxes.asDynamicIterable().stream().forEach(SelenideElement::click);

            deleteMailButton.shouldBe(visible,enabled);
            click(deleteMailButton);
            element.shouldNotBe(exist);

        }

    }

}
