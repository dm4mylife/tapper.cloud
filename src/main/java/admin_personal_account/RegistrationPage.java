package admin_personal_account;

import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.CollectionCondition.allMatch;
import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.RegistrationData.*;
import static data.Constants.TestData.Yandex.EXISTING_ADMIN_RESTAURANT_MAIL;
import static data.selectors.AdminPersonalAccount.Profile.*;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.registrationLink;
import static data.selectors.AuthAndRegistrationPage.RegistrationPage.*;
import static data.selectors.AuthAndRegistrationPage.RootTapperPage.signInButton;


public class RegistrationPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    @Step("Переход на страницу регистрации")
    public void goToRegistrationPage() {

        openPage(PERSONAL_ACCOUNT_REGISTRATION_STAGE_URL);
        isRegistrationFormCorrect();

    }

    @Step("Переход на страницу регистрации из лендинга")
    public void goToRegistrationPageFromRootPage() {

        baseActions.openPage(ROOT_TAPPER_STAGE_URL);
        click(signInButton);
        isTextContainsInURL(PERSONAL_ACCOUNT_AUTHORIZATION_STAGE_URL);
        click(registrationLink);
        isTextContainsInURL(PERSONAL_ACCOUNT_REGISTRATION_STAGE_URL);

    }

    @Step("Проверка отображения всех элементов на странице регистрации администратора ресторана")
    public void isRegistrationFormCorrect() {

        isElementVisible(signInField);
        isElementVisible(signInLink);
        isElementVisible(nameField);
        isElementVisible(phoneField);
        isElementVisible(emailField);
        isElementVisible(passwordField);
        isElementVisible(passwordConfirmationField);
        isElementVisible(restaurantNameField);
        isElementVisible(applyButton);
        isElementVisible(confPolicyField);
        isElementVisible(confPolicyLink);
        isElementVisible(logoAtBottom);
        isElementVisible(copyright);
        applyButton.shouldBe(disabled);

    }

    @Step("Проверяем политику конфиденциальности на странице авторизации")
    public void isConfPolicyCorrect() {

        click(confPolicyLink);
        confPolicyContainer.shouldHave(attribute("style",""));

    }

    @Step("Вводим данные {text}")
    public void typeDataInField(SelenideElement element, String text) {

        clearText(element);
        sendKeys(element,text);

        if(!element.equals(phoneField))
            element.shouldHave(value(text));

    }

    @Step("Заполняем все необходимые поля")
    public HashMap <String,String> fillRegistrationForm(boolean hasError) {

        typeDataInField(nameField,NAME);
        typeDataInField(phoneField,TELEPHONE_NUMBER);

        if (hasError) {

            typeDataInField(emailField,EXISTING_ADMIN_RESTAURANT_MAIL);

        } else {

            typeDataInField(emailField,EMAIL);

        }

        typeDataInField(passwordField,PASSWORD);
        typeDataInField(passwordConfirmationField,CONFIRMATION_PASSWORD);
        typeDataInField(restaurantNameField,RESTAURANT_NAME);

        HashMap <String,String> registrationData = new HashMap<>();

        registrationData.put("restaurantName",RESTAURANT_NAME);
        registrationData.put("name",NAME);
        registrationData.put("phone",phoneField.getValue());
        registrationData.put("email",EMAIL);

        return registrationData;

    }




    @Step("Заполняем все необходимые поля но не выставляем галочку политики конфиденциальности")
    public void isConfPolicyCorrectWhenDeactivated() {

        if(confPolicyInput.isEnabled())
            click(confPolicyField);

        confPolicyInput.shouldBe(enabled);
        isElementVisible(confPolicyFieldError);
        applyButton.shouldBe(disabled);

        click(confPolicyField);
        Assertions.assertTrue(confPolicyField.isEnabled());

    }


    public void registrationAdministrator(HashMap<String,String> registrationData) {

        allNecessaryInputsForFilling.should(allMatch("Все поля корректно заполнены",
                element -> element.getCssValue("border-color").equals("rgb(230, 231, 235)")));

        isElementInvisible(confPolicyFieldError);
        applyButton.shouldNotBe(disabled);
        click(applyButton);

        successRegistrationModal.shouldBe(visible, Duration.ofSeconds(30));
        successRegistrationModalDescription.shouldBe(visible);
        isImageCorrect(successRegistrationImageSelector,"Иконка успешной регистрации некорректна");

        profileContainer.should(visible,Duration.ofSeconds(10));

        restaurantName.shouldHave(value(registrationData.get("restaurantName")));
        adminName.shouldHave(value(registrationData.get("name")));
        adminPhone.shouldHave(value(registrationData.get("phone")));
        adminEmail.shouldHave(value(registrationData.get("email")));

    }

}
