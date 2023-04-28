package support_personal_account.profile;


import admin_personal_account.AdminAccount;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import data.Constants;
import data.selectors.SupportPersonalAccount;
import io.qameta.allure.Step;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.selectors.AdminPersonalAccount.Common.*;
import static data.selectors.AdminPersonalAccount.Profile.*;
import static data.selectors.SupportPersonalAccount.Common.profileCategory;
import static data.selectors.SupportPersonalAccount.Profile.name;
import static data.selectors.SupportPersonalAccount.Profile.*;
import static data.selectors.SupportPersonalAccount.Profile.profileContainer;
import static data.selectors.SupportPersonalAccount.Profile.profileTitle;
import static data.selectors.SupportPersonalAccount.Profile.restaurantName;
import static data.selectors.SupportPersonalAccount.Profile.saveButton;
import static data.selectors.SupportPersonalAccount.Profile.telegramItems;
import static data.selectors.SupportPersonalAccount.Profile.telegramItemsLogin;


public class Profile extends BaseActions {

    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();


    @Step("Переход в меню профиля")
    public void goToProfileCategory() {

        click(profileCategory);
        pageHeading.shouldHave(text("Профиль"), Duration.ofSeconds(5));
        profileContainer.shouldBe(visible);
        isProfileCategoryCorrect();

    }

    @Step("Проверка что все элементы в профиле корректны")
    public void isProfileCategoryCorrect() {

        isElementVisible(profileTitle);
        isElementVisible(restaurantName);
        isElementVisible(name);
        isElementVisible(phone);
        isElementVisible(telegramItemsLogin.first());
        isElementsListVisible(telegramItems);
        isElementVisible(email);
        email.shouldBe(disabled);
        isElementVisible(password);
        isElementVisible(passwordConfirmation);
        isElementVisible(saveButton);

    }


    @Step("Проверка что все элементы в боковом меню корректны")
    public void isOpenedLeftMenuCorrect() {

        if (mobileFooter.isDisplayed()) {

            isElementVisible(mobileFooter);

        } else {

            click(mainMenuIcon);

        }

        isElementVisible(exitFromAdmin);
        isElementVisible(profileCategory);
        isElementVisible(SupportPersonalAccount.Common.logsAndPermissionsCategory);
        isElementVisible(SupportPersonalAccount.Common.historyOperationsCategory);
        isElementVisible(SupportPersonalAccount.Common.lockCategory);
        isElementVisible(SupportPersonalAccount.Common.sendingCategory);
        isElementVisible(SupportPersonalAccount.Common.cashDeskInaccessibilityCategory);
        isElementVisible(SupportPersonalAccount.Common.analyticsCategory);

    }

    @Step("Смена значения в поле")
    public void changeFieldValue(SelenideElement element, String value) {

        String previousValue = element.getValue();

        setInputValue(element,value);

        setInputValue(element,previousValue);

        assert previousValue != null;

        element.should(value(previousValue));

    }
    @Step("Проверка смены имени ресторана")
    public void isPrivateDateChangedCorrect() {

        changeFieldValue(restaurantName, Constants.TestData.RegistrationData.RESTAURANT_NAME);
        changeFieldValue(name, Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL);
        changeFieldValue(phone, Constants.TestData.SupportPersonalAccount.SUPPORT_TEST_PHONE);
        changeFieldValue(telegramItemsLogin.first(), Constants.TestData.SupportPersonalAccount.IIKO_RESTAURANT_NAME);

    }

    public void setInputValue(SelenideElement element,String value ) {

        clearText(element);
        sendKeys(element,value);

        click(saveButton);
        isElementVisible(pagePreloader);

        isChangedSavingsHeadingCorrect();

        element.shouldHave(value(value));

    }

    public void isChangedSavingsHeadingCorrect() {

        changedDataNotification
                .shouldHave(attributeMatching("class",".*active.*"))
                .shouldNotHave(attributeMatching("class",".*active.*"));

    }

    @Step("Смена пароля админской учетной записи")
    public void changeAdminPassword() {

        sendKeys(password,SUPPORT_RESTAURANT_NEW_PASSWORD_FOR_TEST);
        sendKeys(passwordConfirmation,SUPPORT_RESTAURANT_NEW_PASSWORD_FOR_TEST);

        click(saveButton);
        isElementVisible(pagePreloader);

        adminPassword.shouldHave(value(SUPPORT_RESTAURANT_NEW_PASSWORD_FOR_TEST));
        adminPasswordConfirmation.shouldHave(value(SUPPORT_RESTAURANT_NEW_PASSWORD_FOR_TEST));

        adminAccount.logOut();

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_RESTAURANT_NEW_PASSWORD_FOR_TEST);

        password.shouldHave(value(""));
        passwordConfirmation.shouldHave(value(""));


        sendKeys(password,SUPPORT_PASSWORD);
        sendKeys(passwordConfirmation,SUPPORT_PASSWORD);

        click(saveButton);
        isElementVisible(pagePreloader);

        adminPassword.shouldHave(value(SUPPORT_PASSWORD));
        adminPasswordConfirmation.shouldHave(value(SUPPORT_PASSWORD));

        adminAccount.logOut();

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);
        isProfileCategoryCorrect();

    }

    @Step("Проверяем ошибки по некорректным данными")
    public void isErrorFieldsCorrect() {

        String previousPhone = phone.getValue();

        clearText(phone);
        sendKeys(phone,INCORRECT_DATA);
        inputError.shouldBe(visible);
        sendKeys(phone,previousPhone);

        sendKeys(password,INCORRECT_DATA);
        inputError.shouldBe(visible);
        clearText(password);

        sendKeys(passwordConfirmation,INCORRECT_DATA);
        inputError.shouldBe(visible);
        clearText(passwordConfirmation);

    }


}
