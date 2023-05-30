package admin_personal_account.tips;

import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import common.BaseActions;
import data.selectors.AdminPersonalAccount;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import tapper_table.RootPage;

import java.io.File;
import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static data.Constants.*;
import static data.Constants.TestData.Best2Pay.*;
import static data.Constants.TestData.Best2Pay.TEST_PAYMENT_CARD_CVV;
import static data.selectors.AdminPersonalAccount.Common.*;
import static data.selectors.AdminPersonalAccount.Tips.*;
import static data.selectors.AdminPersonalAccount.Tips.imageContainer;
import static data.selectors.WaiterPersonalAccount.*;
import static data.selectors.WaiterPersonalAccount.changedCardButton;

public class Tips extends BaseActions {

    RootPage rootPage = new RootPage();

    @Step("Переход в категорию чаевые")
    public void goToTipsCategory() {

        click(tipsCategory);
        pageHeading.shouldHave(text("Чаевые"));

    }

    @Step("Переход в таб Чаевые кухне")
    public void goToKitchenTipsCategory() {

        click(kitchenTipsButton);
        isElementVisible(kitchenGoalContainer);

    }

    @Step("Проверка что все элементы в табе Чаевые кальянщику корректны")
    public void isServiceWorkerRoleTipsTabCorrect(String role) {

        isElementVisible(tipsTypeButtons);
        isElementVisible(tipsInfoContainer);
        isElementVisible(imageContainer);
        Assertions.assertTrue(avatarStub.exists() || avatarImage.exists(),
                "Должна быть или заглушка аватарки или загруженное изображение");

        if (role.equals("hookah")) {

            isElementVisible(hookahNameContainer);
            isElementVisible(hookahGoaContainer);

        } else {

            isElementVisible(kitchenGoalContainer);

        }

        isElementVisible(maxGoalCharsCounter);
        isElementVisible(tipsCheckbox);

        if (linkedCardButton.exists()) {

            linkedCardButton.shouldBe(disabled);
            isElementVisible(AdminPersonalAccount.Tips.saveButton);
            AdminPersonalAccount.Tips.saveButton.shouldBe(disabled);

        } else {

            isElementVisible(toLinkCardButton);

        }

    }


    @Step("Загрузка аватарки")
    public void downloadAvatar() {

        if (!avatarStub.exists())
            deleteAvatar();

        downloadAvatarInput.uploadFile(new File(HOOKAH_AVATAR_JPG));

        avatarImage.shouldBe(exist,Duration.ofSeconds(10))
                .shouldBe(image)
                .shouldHave(attribute("src"));

        isImageCorrect(avatarImgSelector,"Аватарка загрузилась корректно");
        isElementVisible(deleteAvatarButton);

    }

    @Step("Удаление аватарки")
    public void deleteAvatar() {

        click(deleteAvatarButton);
        avatarStub.shouldBe(visible,Duration.ofSeconds(10));
        isElementInvisible(avatarImage);

    }

    public void setInputValue(SelenideElement element, String text) {

        clearText(element);
        sendKeys(element,text);

        click(AdminPersonalAccount.Tips.saveButton);

        element.shouldHave(value(text));

    }

    public void activateTips() {

        if (!Objects.requireNonNull(tipsCheckbox.getAttribute("class")).matches(".*active.*")) {

            click(tipsCheckbox);
            click(AdminPersonalAccount.Tips.saveButton);
            tipsCheckbox.shouldHave(attributeMatching("class",".*active.*"));

        }

    }

    public void deactivateTips() {

        if (Objects.requireNonNull(tipsCheckbox.getAttribute("class")).matches(".*active.*")) {

            click(tipsCheckbox);
            click(AdminPersonalAccount.Tips.saveButton);
            tipsCheckbox.shouldNotHave(attributeMatching("class",".*active.*"));

        }

    }

    @Step("Устанавливаем имя работника")
    public String setServiceWorkerName() {

        String newName = new Faker().name().username();

        setInputValue(hookahNameInput,newName);

        return newName;

    }

    @Step("Проверка ошибки при сохранении пустого имени")
    public void isEmptyHookahNameIncorrect() {

        clearText(hookahNameInput);

        isElementVisible(inputError);

        String newName = new Faker().name().username();

        setInputValue(hookahNameInput,newName);

    }


    @Step("Устанавливаем цель накоплений")
    public String setGoal(String role) {

        String newGoal = new Faker().name().username();

        if (role.equals("hookah")) {

            setInputValue(hookahGoalInput,newGoal);

        } else {

            setInputValue(kitchenGoalInput,newGoal);

        }

        return newGoal;

    }

    @Step("Перепревязка кредитной карты")
    public void changeCreditCard(String role) {

        click(changeCreditCard);

        isElementVisible(b2pContainer);
        b2pCardNumber.sendKeys(TEST_PAYMENT_CARD_NUMBER);
        b2pCardExpiredDate.sendKeys(TEST_PAYMENT_CARD_EXPIRE_MONTH + TEST_PAYMENT_CARD_EXPIRE_YEAR);
        b2pCardCvc.sendKeys(TEST_PAYMENT_CARD_CVV);
        click(b2pSaveButton);

        pageTitle.shouldHave(text(" Настройки профиля "),Duration.ofSeconds(20));

        goToTipsCategory();

        if (role.equals("kitchen"))
            click(kitchenTipsButton);

        linkedCardButton.shouldHave(matchText("Карта привязана"));

    }


}
