package tapper_admin_personal_account.customization;


import common.BaseActions;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import tapper_table.RootPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static constants.Constant.TestData.TEST_ADMIN_ADMINISTRATOR_TEXT_PATTERN_COMMENT;
import static constants.selectors.AdminPersonalAccountSelectors.Common.customizationCategory;
import static constants.selectors.AdminPersonalAccountSelectors.Common.pageHeading;
import static constants.selectors.AdminPersonalAccountSelectors.Customization.*;
import static constants.selectors.TapperTableSelectors.RootPage.TapBar.successCallWaiterHeading;
import static constants.selectors.TapperTableSelectors.RootPage.TapBar.successLogoCallWaiter;

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

        patternTextMsg.click();
        patternTextMsg.sendKeys(Keys.CONTROL + "A");
        patternTextMsg.sendKeys(Keys.BACK_SPACE);
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


}
