package waiter_personal_account;

import admin_personal_account.AdminAccount;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import data.selectors.WaiterPersonalAccount;
import io.qameta.allure.Step;
import total_personal_account_actions.AuthorizationPage;

import java.io.File;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.ROBOCOP_IMG_PATH;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.Best2Pay.*;
import static data.selectors.AdminPersonalAccount.Profile.*;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.waiterImage;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.waiterImageNotSelenide;
import static data.selectors.WaiterPersonalAccount.saveButton;
import static data.selectors.WaiterPersonalAccount.*;



public class Waiter extends BaseActions {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    AdminAccount adminAccount = new AdminAccount();

    @Step("Проверка всех элементов на странице профиля официанта")
    public void isWaiterProfileCorrect() {

        pageTitle.shouldHave(text(" Настройки профиля "));
        isElementVisibleDuringLongTime(imageContainer,10);
        isElementVisible(restaurantName);
        restaurantName.shouldBe(disabled);
        isElementVisible(waiterNameInCashDesk);
        waiterNameInCashDesk.shouldBe(disabled);
        isElementVisible(WaiterPersonalAccount.waiterName);
        isElementVisible(privateDataContainer);

        isElementVisible(waiterEmail);
        waiterEmail.shouldBe(disabled);
        isElementVisible(personalInformationContainer);
        isElementVisible(waiterPassword);
        waiterPassword.shouldHave(empty);
        isElementVisible(waiterPasswordConfirmation);
        waiterPasswordConfirmation.shouldHave(empty);
        isElementVisible(linkWaiterCard);
        isElementVisible(saveButton);

    }

    @Step("Загрузка изображения в аватарку официанта")
    public void downloadWaiterImage() {

        File imageFile = new File(ROBOCOP_IMG_PATH);
        imageContainerDownloadImageButton.uploadFile(imageFile);

        isElementVisible(imageDeleteButton);
        pagePreloader.shouldBe(hidden, Duration.ofSeconds(20));
        isElementVisible(imageContainerDownloadedImage.$("img"));

        isImageCorrect(imageContainerDownloadedImageNotSelenide,
                "Загруженное изображение в контейнер не корректное или битое");

    }

    @Step("Проверка добавленной фотографии на столе")
    public void checkDownloadedWaiterImageOnTable() {

        isElementVisible(waiterImage);
        isImageCorrect(waiterImageNotSelenide,"Изображение официанта не корректное или битое");

    }

    @Step("Проверка изображения официанта на столе")
    public void deleteWaiterImage() {

        if (imageContainerDownloadedImage.$("img").exists()) {

            click(imageDeleteButton);
            pagePreloader.shouldBe(hidden, Duration.ofSeconds(20));
            isElementInvisible(imageContainerDownloadedImage.$("img"));

        }

    }

    @Step("Смена имени официанта")
    public void changeWaiterName() {

        clearText(WaiterPersonalAccount.waiterName);
        sendKeys(WaiterPersonalAccount.waiterName,ROBOCOP_WAITER_CHANGED_NAME);

        click(saveButton);
        isElementVisible(pagePreloader);

        changedDataNotification
                .shouldHave(attributeMatching("class", ".*active.*"));
        WaiterPersonalAccount.waiterName.shouldHave(value(ROBOCOP_WAITER_CHANGED_NAME));

    }

    @Step("Проверка смены имени официанта на столе")
    public void checkChangedNameOnTable() {

        data.selectors.TapperTable.RootPage.TipsAndCheck.waiterName.shouldHave(text(ROBOCOP_WAITER_CHANGED_NAME));

    }

    @Step("Проверка изменения телеграмм логина")
    public void setNameToDefault() {

        clearText(WaiterPersonalAccount.waiterName);

        click(saveButton);
        pagePreloader.shouldBe(visible);
        changedDataNotification
                .shouldHave(attributeMatching("class", ".*active.*"));
        WaiterPersonalAccount.waiterName.shouldHave(value(""));

    }

    @Step("Смена пароля учетной записи официанта")
    public void changeWaiterPassword() {

        isSavedPassword(waiterPassword,waiterPasswordConfirmation, WAITER_NEW_PASSWORD_FOR_TEST);

        adminAccount.logOut();
        authorizationPage.authorizationUser(WAITER_LOGIN_EMAIL, WAITER_NEW_PASSWORD_FOR_TEST);
        pageTitle.shouldHave(text(" Настройки профиля "));

        isSavedPassword(waiterPassword,waiterPasswordConfirmation, WAITER_PASSWORD);

    }

    public void isSavedPassword(SelenideElement passwordInput, SelenideElement passwordConfirmationInput, String password) {

        click(passwordInput);
        sendKeys(passwordInput,password);

        click(passwordConfirmationInput);
        sendKeys(passwordConfirmationInput,password);

        click(saveButton);
        pagePreloader.shouldBe(hidden, Duration.ofSeconds(5));
        waiterPassword.shouldHave(value(password));
        waiterPasswordConfirmation.shouldHave(value(password));

    }

    @Step("Привязка карта официанта")
    public void linkWaiterCard() {

        click(linkWaiterCard);
        isElementVisible(b2pContainer);
        b2pCardNumber.sendKeys(TEST_PAYMENT_CARD_NUMBER);
        b2pCardExpiredDate.sendKeys(TEST_PAYMENT_CARD_EXPIRE_MONTH + TEST_PAYMENT_CARD_EXPIRE_YEAR);
        b2pCardCvc.sendKeys(TEST_PAYMENT_CARD_CVV);
        click(b2pSaveButton);

        pageTitle.shouldHave(text(" Настройки профиля "),Duration.ofSeconds(20));

        buttonWithCard.shouldHave(disabled, matchText("Карта привязана"));
        isElementVisible(changedCardButton);
        //toDo нет второй тестовой карты чтобы написать тест на редактирование текущей

    }

    public static void skipConfPolicyModal() {

        if (confPolicyModal.isDisplayed()) {

            click(confPolicyModalAgreeButton);
            confPolicyModal.shouldBe(hidden);
            profileContainer.shouldBe(visible);

        }

    }

    @Step("Проверка формы политики конфиденциальности")
    public void isConfPolicyCorrectModal() {

        isElementVisible(confPolicyModal);
        isElementVisible(confPolicyModalDonTAgreeButton);
        isElementVisible(confPolicyModalAgreeButton);

    }

    @Step("Не соглашаемся с политикой конфиденциальности")
    public void donTAgreeWithConfPolicy() {

        click(confPolicyModalDonTAgreeButton);
        isElementInvisible(confPolicyModal);
        isElementVisible(confPolicyError);


    }

    @Step("Соглашаемся с политикой конфиденциальности")
    public void agreeWithConfPolicy() {

        click(confPolicyModalAgreeButton);
        isElementInvisible(confPolicyModal);
        isTextContainsInURL(PERSONAL_ACCOUNT_PROFILE_STAGE_URL);

    }

}
