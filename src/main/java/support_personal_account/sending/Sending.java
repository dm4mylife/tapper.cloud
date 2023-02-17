package support_personal_account.sending;

import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.SupportPersonalAccount.Common.sendingCategory;
import static data.selectors.SupportPersonalAccount.Sending.*;

public class Sending extends BaseActions {
    @Step("Переход в категорию Недоступность кассы")
    public void goToSending() {

        click(sendingCategory);
        pageHeading.shouldHave(text("Рассылка"), Duration.ofSeconds(5));
        sendingContainer.shouldBe(visible);

    }

    @Step("Проверка отображения всех элементов в категории Недоступность кассы")
    public void isSendingCategoryCorrect() {

        isElementVisible(sendingRecipientContainer);
        isElementVisible(sendingMessageContainer);
        isElementVisible(sendToAllContainer);
        isElementVisible(sendToWaiterContainer);
        isElementVisible(sendToManagerContainer);
        isElementVisible(messageTextArea);
        isElementVisible(sendButton);

    }

    public void isConfirmationCorrect() {

        isElementVisible(confirmationContainer);
        isElementVisible(confirmationSendButton);
        isElementVisible(confirmationCancelButton);
        isElementVisible(confirmationCloseButton);

    }

    @Step("Отправляем сообщение определенному типу получателей ({recipient})")
    public void sendMessageToCertainTypeRecipient(SelenideElement recipient, String text) {

        clearText(messageTextArea);

        click(recipient);

        sendKeys(messageTextArea,text);

        click(sendButton);

        isConfirmationCorrect();

        click(confirmationSendButton);

    }

}
