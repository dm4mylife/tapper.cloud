package tapper_admin_personal_account.config_notifications;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static constants.Constant.TestDataRKeeperAdmin.AUTO_CHANNEL_LOGIN;
import static constants.selectors.AdminPersonalAccountSelectors.Common.configNotifications;
import static constants.selectors.AdminPersonalAccountSelectors.Common.pageHeading;
import static constants.selectors.AdminPersonalAccountSelectors.ConfigNotifications.*;

public class ConfigNotifications extends BaseActions {

    @Step("Переход в категорию реквизиты компании")

    public void goToConfigNotificationsCategory() {

        click(configNotifications);
        pageHeading.shouldHave(text("Настройка уведомлений"), Duration.ofSeconds(5));
        configNotificationsContainer.shouldBe(visible, Duration.ofSeconds(5));

    }

    @Step("Проверка элементов страницы")
    public void isConfigNotificationsCategoryCorrect() {

        isElementVisible(legendContainer);
        isElementVisible(telegramLoginInputs.first());
        isElementVisible(telegramLoginSettingsSvg.first());
        isElementVisible(telegramLoginSettingsDeleteIcon.first());
        isElementVisible(saveButton);

    }

    @Step("Проверка попап удаления телеграм логина")
    public void isDeleteTelegramLoginPopCorrect() {

        isElementVisible(deleteTelegramContainer);
        isElementVisible(deleteTelegramContainerUnlinkButton);
        isElementVisible(deleteTelegramContainerCancelButton);
        isElementVisible(deleteTelegramContainerCloseButton);

    }

    @Step("Находим канал с автоматизацией и удаляем его")
    public void findAutoChannel() {

     for (int index = 0; index <telegramLoginInputs.size(); index++) {

         if (telegramLoginInputs.get(index).getValue().equals(AUTO_CHANNEL_LOGIN)) {

             telegramLoginSettingsDeleteIcon.get(index).click();
             isDeleteTelegramLoginPopCorrect();

             deleteTelegramContainerUnlinkButton.click();

         }

     }

     telegramLoginInputs.filter(attribute("id",AUTO_CHANNEL_LOGIN)).shouldBe(CollectionCondition.size(0));

     addAutoChannel();

    }

    @Step("Добавляем канал с автоматизацией")
    public void addAutoChannel() {

        isElementVisible(addButton);
        click(addButton);
        forceWait(500);

        telegramLoginInputs
                .last()
                .shouldBe(appear)
                .shouldHave(value(""));

        telegramLoginInputs
                .last()
                .sendKeys(AUTO_CHANNEL_LOGIN);

        click(saveButton);

        telegramLoginInputs
                .last()
                .shouldHave(value(AUTO_CHANNEL_LOGIN),Duration.ofSeconds(10));

    }

}
