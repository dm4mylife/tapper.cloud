package admin_personal_account.customization;


import common.BaseActions;
import io.qameta.allure.Step;
import tapper_table.RootPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.AdminPersonalAccount.TEST_ADMIN_ADMINISTRATOR_TEXT_PATTERN_COMMENT;
import static data.selectors.AdminPersonalAccount.Common.customizationCategory;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.Customization.*;
import static data.selectors.AdminPersonalAccount.Profile.pagePreloader;
import static data.selectors.TapperTable.RootPage.TapBar.successCallWaiterHeading;
import static data.selectors.TapperTable.RootPage.TapBar.successLogoCallWaiter;

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
        isElementVisible(toWhomSendMsgTitle);
        isElementVisible(waiterAndManagerButton);
        isElementVisible(onlyManagerButton);
        isElementVisible(patternTextMsg);
        isElementVisible(saveButton);
        isElementVisible(recipientContainer);
        isElementVisible(recipientWaiterIcon);
        isElementVisible(recipientManagerIcon);
        isElementVisible(msgTextContainer);
        isElementVisible(msgTextExample);

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

    @Step("Ввод текста в шаблон сообщения")
    public void setMsgAsTextPattern() {

        clearText(patternTextMsg);
        patternTextMsg.sendKeys(TEST_ADMIN_ADMINISTRATOR_TEXT_PATTERN_COMMENT);

        click(saveButton);

        msgTextExample.shouldHave(text(TEST_ADMIN_ADMINISTRATOR_TEXT_PATTERN_COMMENT));

    }

    @Step("Проверка изменения шаблона текста на столе")
    public void isChangedTextPatternCorrectOnTable(String textPattern) {

        rootPage.openCallWaiterForm();
        rootPage.sendWaiterComment();
        isElementVisible(successCallWaiterHeading);
        isElementVisible(successLogoCallWaiter);

        System.out.println(successCallWaiterHeading.getText());
        successCallWaiterHeading.shouldHave(text(textPattern));

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

        clearText(wifiNetworkName);
        clearText(wifiNetworkPassword);
        wifiNetworkName.sendKeys(wifiName);
        wifiNetworkPassword.sendKeys(wifiPassword);
        click(saveButton);

    }



}
