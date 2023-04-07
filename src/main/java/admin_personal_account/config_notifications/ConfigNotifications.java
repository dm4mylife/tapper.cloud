package admin_personal_account.config_notifications;


import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.TapperTable.TEST_WAITER_COMMENT;
import static data.selectors.AdminPersonalAccount.Common.*;
import static data.selectors.AdminPersonalAccount.ConfigNotifications.*;

public class ConfigNotifications extends BaseActions {

    @Step("Переход в категорию Настройка уведомлений")

    public void goToConfigNotificationsCategory() {

        click(configNotifications);
        pageHeading.shouldHave(text("Настройка уведомлений"), Duration.ofSeconds(5));
        configNotificationsContainer.shouldBe(visible, Duration.ofSeconds(5));

    }

    @Step("Проверка элементов страницы")
    public void isConfigNotificationsCategoryCorrect() {

        if (!mobileFooter.getCssValue("display").equals("flex"))
            isElementVisible(legendContainer);
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

    @Step("Проверяем ошибку ввода логина телеграмма")
    public void isErrorTelegramLoginCorrect() {

        int currentSizeBeforeAdding = telegramLoginInputs.size();

        addLogin(TEST_WAITER_COMMENT,true);

        Assertions.assertEquals(currentSizeBeforeAdding,telegramLoginInputs.size());

    }

    public void addLogin(String telegramLogin,boolean hasError) {

        addButton.shouldBe(visible,enabled).click();

        telegramLoginInputs.last().shouldBe(visible,editable,interactable).shouldHave(value(""))
                .sendKeys(telegramLogin);

        if (hasError) {

            errorTextInLoginTelegram.shouldHave(appear);
            click(telegramLoginSettingsDeleteIcon.last());

        } else {

            click(saveButton);

        }

    }


    public boolean isTelegramLoginExist(String telegramLogin) {

        return telegramLoginInputs.asFixedIterable().stream()
                .anyMatch(element -> Objects.equals(element.getValue(), telegramLogin));

    }

    @Step("Удаляем телеграмм группу")
    public void deleteTelegramGroup(String telegramLogin) {

        for (int index = 0; index <telegramLoginInputs.size(); index++) {

            if (Objects.equals(telegramLoginInputs.get(index).getValue(), telegramLogin)) {

                telegramLoginSettingsDeleteIcon.get(index).click();
                isDeleteTelegramLoginPopCorrect();

                deleteTelegramContainerUnlinkButton.click();

                click(saveButton);

            }

        }

        telegramLoginInputs.filter(attribute("id", telegramLogin)).shouldBe(size(0));

    }

    @Step("Добавляем телеграм группу")
    public void addTelegramGroup(String telegramLogin) {

        addLogin(telegramLogin,false);

        telegramLoginInputs.last().shouldHave(value(telegramLogin),Duration.ofSeconds(10));

        Selenide.refresh();

        goToConfigNotificationsCategory();

        isTelegramLoginExist(telegramLogin);

    }

    @Step("Проверяем редактирование группы, отображение полей и чек боксов")
    public void isElementNotificationSettingCorrect() {

        click(addButton);

        click(telegramLoginSettingsSvg.last());

        isElementsListVisible(typeNotificationList);

        click(typeNotificationList.first());

        typeNotificationList.filter(attributeMatching("class","sectionTelegram-modal__checkboxItem"))
                .shouldHave(size(4));

        click(typeNotificationList.first());

        System.out.println(typeNotificationList);
        typeNotificationList.filter(attributeMatching("class",".*active"))
                .shouldHave(size(4));

        click(deleteTelegramContainerCloseButton);

        isElementInvisible(deleteTelegramContainer);

    }

    @Step("Проверка опции уведомления в настройках")
    public void isTypeOptionCorrect(SelenideElement optionToBeChosen) {

        click(telegramLoginSettingsSvg.last());

        typeNotificationList.asFixedIterable().stream().forEach(
                element -> {
                    if (Objects.requireNonNull(element.getAttribute("class")).matches(".*active"))
                        element.click();

        });

        click(optionToBeChosen);

        click(settingsContainerSaveButton);

        click(telegramLoginSettingsSvg.last());

        optionToBeChosen.shouldHave(attributeMatching("class",".*active"));
        typeNotificationList
                .filter(attributeMatching("class",".*active")).shouldHave(size(1));

        click(deleteTelegramContainerCloseButton);

    }

    public void isCorrectAfterPageRefresh() {

        Selenide.refresh();
        isConfigNotificationsCategoryCorrect();

    }


}
