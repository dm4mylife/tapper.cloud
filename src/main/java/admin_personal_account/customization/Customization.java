package admin_personal_account.customization;


import common.BaseActions;
import io.qameta.allure.Step;
import tapper_table.RootPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.AdminPersonalAccount.TEST_ADMIN_ADMINISTRATOR_TEXT_PATTERN_COMMENT;
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

        forceWait(500);
        clearText(wifiNetworkName);
        forceWait(500);
        clearText(wifiNetworkPassword);

        if (wifiNetworkName.getValue().equals("") && wifiNetworkPassword.getValue().equals("")) {

            wifiNetworkName.sendKeys(wifiName);
            wifiNetworkPassword.sendKeys(wifiPassword);
            click(saveButton);
            System.out.println("Установили вайфай\n" +
                    "Логин: " + wifiNetworkName.getValue() + "\nПароль: " + wifiNetworkPassword.getValue());


        }

    }

}
