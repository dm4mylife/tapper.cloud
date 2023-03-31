package admin_personal_account;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.collections.AllMatch;
import common.BaseActions;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.allMatch;
import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.RegistrationData.*;
import static data.Constants.WAIT_FOR_FULL_LOAD_PAGE;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.*;
import static data.selectors.AuthAndRegistrationPage.RegistrationPage.*;
import static data.selectors.AuthAndRegistrationPage.RootTapperPage.signInButton;


public class RegistrationPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    @Step("Переход на страницу регистрации")
    public void goToRegistrationPage() {

        openPage(ADMIN_REGISTRATION_STAGE_URL);
        forceWait(WAIT_FOR_FULL_LOAD_PAGE); // toDo не успевает прогрузиться
        isRegistrationFormCorrect();

    }

    @Step("Переход на страницу регистрации из лендинга")
    public void goToRegistrationPageFromRootPage() {

        baseActions.openPage(ROOT_TAPPER_STAGE_URL);
        click(signInButton);
        isTextContainsInURL(ADMIN_AUTHORIZATION_STAGE_URL);
        click(registrationLink);
        isTextContainsInURL(ADMIN_REGISTRATION_STAGE_URL);

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
        isElementVisible(registrationButton);
        isElementVisible(confPolicyField);
        isElementVisible(confPolicyLink);
        isElementVisible(logoAtBottom);
        isElementVisible(copyright);
        registrationButton.shouldBe(disabled);

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

    }

    @Step("Заполняем все необходимые поля")
    public void fillRegistrationForm() {

        typeDataInField(nameField,NAME);
        typeDataInField(phoneField,TELEPHONE_NUMBER);
        typeDataInField(emailField,EMAIL);
        typeDataInField(passwordField,PASSWORD);
        typeDataInField(passwordConfirmationField,CONFIRMATION_PASSWORD);
        typeDataInField(restaurantNameField,RESTAURANT_NAME);

    }

    public void isReadyForRegistration() {

        allNecessaryInputsForFilling.should(allMatch("Все поля корректно заполнены",
                element -> element.getCssValue("border-color").equals("rgb(230, 231, 235)")));

        isElementInvisible(confPolicyFieldError);
        registrationButton.shouldNotBe(disabled);
        //click(registrationButton);

    }

}
