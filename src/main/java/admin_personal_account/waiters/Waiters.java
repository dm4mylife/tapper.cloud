package admin_personal_account.waiters;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.empty;
import static data.Constants.TestData.AdminPersonalAccount.INVITED_IN_SERVICE_TEXT;
import static data.Constants.TestData.AdminPersonalAccount.VERIFIED_WAITER_TEXT;
import static data.Constants.WAIT_FOR_INPUT_IS_FULL_LOAD_ON_AUTHORIZE_PAGE;
import static data.Constants.WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.Common.waiterMenuCategory;
import static data.selectors.AdminPersonalAccount.Waiters.*;


public class Waiters extends BaseActions {

    AuthorizationPage authorizationPage = new AuthorizationPage();

    @Step("Переход в меню официанта")
    public void goToWaiterCategory() {

        click(waiterMenuCategory);
        pageHeading.shouldHave(text("Официанты"), Duration.ofSeconds(5));
        isElementVisible(waiterContainer);
        isWaiterCategoryCorrect();

    }

    @Step("Проверка что все элементы в поиске официанта корректны")
    public void isWaiterCategoryCorrect() {

        isElementVisible(waiterListHeading);
        isElementVisible(refreshListButton);
        waiterPaginationList.shouldHave(sizeGreaterThanOrEqual(1));
        click(refreshListButton);
        pagePreloader.shouldHave(cssValue("display","flex"));
        pagePreloader.shouldHave(cssValue("display","none"),Duration.ofSeconds(20));

    }

    @Step("Проверка что официанта находится по имени")
    public void searchWaiter(String waiterName) {

        searchField.clear();
        searchField.sendKeys(waiterName);
        waiterList.shouldBe(sizeGreaterThan(0),Duration.ofSeconds(5));
        waiterCardName.shouldHave(text(waiterName));

    }

    @Step("Проваливаемся в первую карточку из результатов поиска официантов")
    public void clickInFirstResult() {

        waiterList.first().shouldBe(visible,Duration.ofSeconds(5));
        click(waiterList.first());

    }

    @Step("Проверка что поиск выдаст ошибку если такого официанта нет")
    public void searchWaiterNegative() {

        clearText(searchField);
        searchField.sendKeys("Ингеборга Эдмундовна Дапкунайте");
        waiterList.shouldBe(size(0),Duration.ofSeconds(5));
        searchError.shouldBe(visible, text("Нет результатов. Попробуйте ввести данные ещё раз"));

        resetSearchResult();

    }

    @Step("Проверка что сброс поиска работает корректно")
    public void resetSearchResult() {

        click(searchResetButton);
        waiterList.shouldBe(size(10),Duration.ofSeconds(5));
        searchField.shouldHave(empty);
        searchResetButton.shouldNotBe(visible);

    }

    @Step("Проверка что все элементы в детальной карточке корректны если статус у карточки 'Ожидает приглашения'")
    public void isDetailWaiterCardCorrectWithWaitingInvitationStatus() {

        isElementVisible(backToPreviousPage);
        isElementVisible(waiterNameInCashDesk);
        isElementVisible(waiterName);
        isElementVisible(enterEmailField);

    }

    @Step("Проверка что сброс поиска работает корректно")
    public void sendInviteToWaiterEmail(String waiterName, String email) {

        searchWaiter(waiterName);

        clickInFirstResult();

        if (waiterStatusInCard.getText().matches(VERIFIED_WAITER_TEXT))
            unlinkMailWaiterInCard();

        isDetailWaiterCardCorrectWithWaitingInvitationStatus();

        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_AUTHORIZE_PAGE); // toDO не успевает прогрузиться инпут емейла, обрезается

        if (waiterStatusInCard.getText().matches(INVITED_IN_SERVICE_TEXT)) {

            cancelEMailWaiterInvitationInCard();

        } else if (waiterStatusInCard.getText().matches(VERIFIED_WAITER_TEXT)) {

            unlinkMailWaiterInCard();

        }

        sendKeys(enterEmailField,email);
        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
        click(inviteButton);

        isSendInvitationCorrect(email);

    }

    @Step("Проверка что сброс поиска работает корректно")
    public void sendInviteToWaiterWithWrongEmail(String waiterName, String email) {

        searchWaiter(waiterName);

        clickInFirstResult();

        if (waiterStatusInCard.getText().matches(VERIFIED_WAITER_TEXT)) {

            unlinkMailWaiterInCard();

        }

        isDetailWaiterCardCorrectWithWaitingInvitationStatus();

        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_AUTHORIZE_PAGE); // toDO не успевает прогрузиться инпут емейла, обрезается

        if (waiterStatusInCard.getText().matches(INVITED_IN_SERVICE_TEXT)) {

            cancelEMailWaiterInvitationInCard();

        } else if (waiterStatusInCard.getText().matches(VERIFIED_WAITER_TEXT)) {

            unlinkMailWaiterInCard();

        }

        sendKeys(enterEmailField,email);
        click(inviteButton);

        isNegativeSendInvitationCorrect(email);

    }

    @Step("Проверка что появилось уведомление об отправке приглашения, почта сохранилась в детальной карточке официанта")
    public void isSendInvitationCorrect(String email) {

        enterEmailField.shouldHave(value(email));
        successSendingInvitation.shouldBe(visible,Duration.ofSeconds(5));
        cancelInvitationButton.shouldBe(visible);

    }

    @Step("Проверка что появилось уведомление об отправке приглашения, почта сохранилась в детальной карточке официанта")
    public void isNegativeSendInvitationCorrect(String email) {

        enterEmailField.shouldHave(value(email));
        isElementVisible(wrongEmailError);

    }

    @Step("Проверка что в таппере также изменился статус верификации официанта в превью и детальной карточке")
    public void isWaiterStatusCorrectInPreviewAndCard(String waiterName,String waiter_status) {

        searchWaiter(waiterName);
        waiterStatusInPreview.shouldHave(matchText(waiter_status)
                .because("Статус в списке официантов должен быть " + waiter_status));

        click(waiterList.first());

        waiterStatusInCard.shouldHave(matchText(waiter_status)
                .because("Статус в карточке официанта должен быть " + waiter_status));

    }

    @Step("Поиск первого совпадения карточки официанта по статусу '{waiterStatus}' и клик в неё")
    public void findFirstMatchByStatusAndClickInWaiterCard(String waiterStatus) {

        for (int index = 0; index < waiterPaginationList.size(); index++) {

            for (SelenideElement element: waiterList) {

                if(element.$(waiterStatusSelector).getText().equals(waiterStatus)) {

                    element.click();
                    break;

                }

            }

        }

    }

    @Step("Проверка ошибки раннее привязанной почты к другому официанту")
    public void isErrorMailCorrect(String email) {

        enterEmailField.sendKeys(email);
        inviteButton.click();

        errorMsgEmailWasApplied
                .shouldHave(Condition.text("E-mail уже привязан к другому официанту "), Duration.ofSeconds(3));
        enterEmailFieldWrapper.shouldHave(cssValue("border-color","rgb(236, 78, 78)"));

    }

    @Step("Удаление почты и привязки у официанта")
    public void unlinkMailWaiter(String login, String password, String waiterName) {

        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();

        authorizationPage.authorizationUser(login, password);
        goToWaiterCategory();
        searchWaiter(waiterName);
        clickInFirstResult();

        if (waiterStatusInCard.getText().matches(INVITED_IN_SERVICE_TEXT)) {

            cancelEMailWaiterInvitationInCard();

        } else {

            unlinkMailWaiterInCard();

        }

    }

    @Step("Удаление почты и привязки у официанта")
    public void unlinkMailWaiterInCard() {

        click(cancelInvitationButton);
        unlinkMailConfirmPopup.shouldBe(visible,Duration.ofSeconds(2));
        click(unlinkEmailConfirmButton);
        enterEmailField.shouldHave(value(""));
        inviteButton.shouldBe(visible,disabled);
        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);

    }

    @Step("Отмена отправленного приглашению на авторизацию")
    public void cancelEMailWaiterInvitationInCard() {

        successSendingInvitation.shouldNotBe(visible,Duration.ofSeconds(6));
        click(cancelInvitationButton);
        cancelMailConfirmationPopup.shouldBe(visible);
        click(cancelMailConfirmationSaveButton);
        enterEmailField.shouldHave(value(""));
        inviteButton.shouldBe(visible,disabled);

    }

}
