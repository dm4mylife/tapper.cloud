package admin_personal_account.customization;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import tapper_table.RootPage;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.*;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_333;
import static data.selectors.AdminPersonalAccount.Common.customizationCategory;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.Customization.*;
import static data.selectors.AdminPersonalAccount.Profile.pagePreloader;
import static data.selectors.TapperTable.RootPage.TapBar.*;

public class Customization extends BaseActions {

    RootPage rootPage = new RootPage();

    @Step("Переход в категорию кастомизации")
    public void goToCustomizationCategory() {

        click(customizationCategory);
        pageHeading.shouldHave(text("Кастомизация"), Duration.ofSeconds(5));
        customizationContainer.shouldBe(visible,Duration.ofSeconds(5));

    }

    @Step("Проверка элементов страницы")
    public void isCustomizationCategoryCorrect() {

        isElementVisible(callWaiterTab);
        isElementVisible(wifiTab);
        isElementVisible(reviewTab);
        isElementVisible(toWhomSendMsgTitle);
        isElementVisible(waiterAndManagerButton);
        isElementVisible(onlyManagerButton);
        isElementVisible(saveButton);
        isElementVisible(recipientContainer);
        isElementVisible(recipientWaiterIcon);
        isElementVisible(recipientManagerIcon);

    }

    @Step("Выбор варианта 'Официанту и менеджеру'")
    public void choseWaiterAndManager() {

        click(waiterAndManagerButton);
        waiterAndManagerInput.shouldBe(checked);

        recipientWaiterIcon.shouldHave(cssValue("stroke","rgb(52, 61, 93)"));
        recipientManagerIcon.shouldBe(cssValue("fill","rgb(52, 61, 93)"));

        click(saveButton);
        saveButton.shouldBe(disabled);

    }

    @Step("Выбор варианта 'Официанту и менеджеру'")
    public void choseOnlyManager() {

        click(onlyManagerButton);
        waiterAndManagerInput.shouldNotBe(checked);
        onlyManagerInput.shouldBe(checked);

        recipientWaiterIcon.shouldHave(cssValue("stroke","rgb(204, 207, 215)"));
        recipientManagerIcon.shouldBe(cssValue("fill","rgb(52, 61, 93)"));

        click(saveButton);
        saveButton.shouldBe(disabled);

    }


    @Step("Проверка изменения шаблона текста на столе")
    public void checkCallWaiterButtonTypeOnTable(String recipientsType,String heading) {

        callWaiterButtonText.shouldHave(matchText(recipientsType));
        click(callWaiterButton);
        callWaiterHeading.shouldHave(matchText(heading));
        click(callWaiterCloseButton);

    }

    @Step("Проверка таба вайфая")
    public void isWiFiTabCorrect() {

        isElementVisible(wifiSlider);
        isElementVisible(wifiNetworkName);
        isElementVisible(wifiNetworkPassword);
        isElementVisible(saveWifiButton);

    }

    @Step("Включаем вайфай")
    public void activateWifiIfDeactivated() {

        if (wifiDeactivatedSlider.exists()) {

            click(wifiSlider);
            pagePreloader.shouldBe(hidden,Duration.ofSeconds(5));
            wifiActivatedSlider.shouldBe(appear);

        }

    }

    @Step("Выключаем вайфай")
    public void deactivateWifiIfActivated() {

        if (wifiActivatedSlider.exists()) {

            click(wifiSlider);
            pagePreloader.shouldBe(hidden,Duration.ofSeconds(5));
            wifiDeactivatedSlider.shouldBe(appear);

        }

        wifiNetworkName.shouldBe(disabled);
        wifiNetworkPassword.shouldBe(disabled);
        saveWifiButton.shouldBe(disabled);

    }

    @Step("Установка вайфая и пароля")
    public void setWifiConfiguration(String wifiName, String wifiPassword) {

        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
        clearText(wifiNetworkName);
        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
        clearText(wifiNetworkPassword);

        if (Objects.equals(wifiNetworkName.getValue(), "") && Objects.equals(wifiNetworkPassword.getValue(), "")) {

            wifiNetworkName.sendKeys(wifiName);
            forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
            wifiNetworkPassword.sendKeys(wifiPassword);
            forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
            click(saveButton);

        }

    }

    @Step("Проверка элементов в табе Отзывы на внешнем сервисе")
    public void isReviewCorrect() {

        isElementVisible(reviewContainer);
        isElementVisible(yandexInput);
        isElementVisible(twoGisInput);
        isElementVisible(googleInput);
        isElementVisible(reviewInfo);
        isElementsListVisible(reviewToggles);
        isElementVisible(yandexCheckboxContainer);
        isElementVisible(twoGisCheckboxContainer);
        isElementVisible(googleCheckboxContainer);

        for (SelenideElement toggle : reviewToggles) {

            click(toggle);
            forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);

        }

        reviewToggleInfo.filter(visible).shouldHave(CollectionCondition.size(3));

    }
    public void clearLink(SelenideElement input, SelenideElement checkbox, SelenideElement label) {

        clearText(input);

        if (checkbox.isSelected())
            click(label);

    }

    public void fillLink(SelenideElement input, String text, SelenideElement checkbox, SelenideElement label) {

        sendKeys(input, text);

        if (!checkbox.isSelected())
            click(label);

    }
    @Step("Удаляем ссылки, отключаем чекбоксы, сохраняем")
    public void clearAllForms() {

        clearLink(yandexInput,yandexCheckbox,yandexTextLabel);
        clearLink(twoGisInput,twoGisCheckbox,twoGisTextLabel);
        clearLink(googleInput,googleCheckbox,googleTextLabel);

        click(saveButton);

        yandexCheckbox.shouldNotBe(selected);
        twoGisCheckbox.shouldNotBe(selected);
        googleCheckbox.shouldNotBe(selected);

    }

    @Step("Вводим ссылки, включаем чекбоксы")
    public void fillReviewLinks() {

        fillLink(yandexInput,YANDEX_REVIEW_LINK,yandexCheckbox,yandexTextLabel);
        fillLink(twoGisInput,TWOGIS_REVIEW_LINK,twoGisCheckbox,twoGisTextLabel);
        fillLink(googleInput,GOOGLE_REVIEW_LINK,googleCheckbox,googleTextLabel);

        click(saveButton);

        yandexInput.shouldNotBe(empty);
        twoGisInput.shouldNotBe(empty);
        googleInput.shouldNotBe(empty);
        yandexCheckbox.shouldBe(selected);
        twoGisCheckbox.shouldBe(selected);
        googleCheckbox.shouldBe(selected);

    }

    @Step("Пытаемся сохранить вместе с пустыми ссылками, но выбранными чекбоксами")
    public void saveWithEmptyLinks() {

        clearText(yandexInput);

        if (!yandexCheckbox.isSelected())
            click(yandexTextLabel);

        click(saveButton);

        errorEmailInput.shouldHave(matchText(FORM_MUST_BE_FIELD_ERROR_TEXT));

        clearText(yandexInput);

    }

    @Step("Пытаемся сохранить некорректную ссылку")
    public void typeIncorrectLink() {

        clearText(yandexInput);

        sendKeys(yandexInput,TYPE_CORRECT_LINK_ERROR_TEXT);
        errorEmailInput.shouldHave(matchText(TYPE_CORRECT_LINK_ERROR_TEXT));

        clearText(yandexInput);

    }



}
