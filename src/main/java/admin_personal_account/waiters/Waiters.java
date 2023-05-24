package admin_personal_account.waiters;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import tapper_table.RootPage;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.AdminPersonalAccount.Common.*;
import static data.selectors.AdminPersonalAccount.Waiters.*;


public class Waiters extends BaseActions {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    RootPage rootPage = new RootPage();

    @Step("Переход в меню официанта")
    public void goToWaiterCategory() {

        click(waiterMenuCategory);
        pageHeading.shouldHave(text("Официанты"), Duration.ofSeconds(5));
        isElementVisible(waiterContainer);
        isWaiterCategoryCorrect();

    }

    public void isCorrectAfterPageRefresh() {

        rootPage.refreshPage();
        isWaiterCategoryCorrect();

    }

    @Step("Проверка что все элементы в поиске официанта корректны")
    public void isWaiterCategoryCorrect() {

        if (mobileFooter.getCssValue("display").equals("none")) {

            isElementVisible(waiterListHeading);

        }

        isElementVisible(refreshListButton);
        waiterPaginationList.shouldHave(sizeGreaterThanOrEqual(1));
        click(refreshListButton);
        pagePreloader.shouldHave(cssValue("display","flex"));
        pagePreloader.shouldHave(cssValue("display","none"),Duration.ofSeconds(20));

    }

    @Step("Проверка что официанта находится по имени")
    public void searchWaiter(String waiterName) {

        clearText(searchField);
        sendKeys(searchField,waiterName);
        waiterList.shouldBe(sizeGreaterThan(0),Duration.ofSeconds(5));
        waiterListName.first().shouldHave(text(waiterName));

    }

    @Step("Проваливаемся в первую карточку из результатов поиска официантов")
    public void clickInFirstResult() {

        isElementVisibleDuringLongTime(waiterList.first(),5);
        click(waiterList.first());

    }

    @Step("Проверка что поиск выдаст ошибку если такого официанта нет")
    public void searchWaiterNegative() {

        clearText(searchField);
        sendKeys(searchField,NON_EXIST_WAITER);
        waiterList.shouldBe(size(0),Duration.ofSeconds(5));
        searchError.shouldBe(visible, text(SEARCH_WAITER_ERROR_TEXT));

        resetSearchResult();

    }

    @Step("Проверка отрицательного поиска без сброса")
    public void searchWaiterNegativeWithoutReset() {

        clearText(searchField);
        sendKeys(searchField,NON_EXIST_WAITER);
        waiterList.shouldBe(size(0),Duration.ofSeconds(5));
        searchError.shouldBe(visible, text(SEARCH_WAITER_ERROR_TEXT));

    }

    @Step("Проверка что сброс поиска работает корректно")
    public void resetSearchResult() {

        click(searchResetButton);
        waiterList.shouldBe(size(10),Duration.ofSeconds(5));
        searchField.shouldHave(empty);
        isElementInvisible(searchResetButton);


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

        if (waiterStatusInCard.getText().matches("(.|\\n)*" + INVITED_IN_SERVICE_TEXT)) {

            cancelEMailWaiterInvitationInCard();

        } else if (waiterStatusInCard.getText().matches("(.|\\n)*" + VERIFIED_WAITER_TEXT)) {

            unlinkMailWaiterInCard();

        }

        isDetailWaiterCardCorrectWithWaitingInvitationStatus();

        sendKeys(enterEmailField,email);
        inviteButton.shouldBe(visible,enabled);
        click(inviteButton);

        isSendInvitationCorrect(email);

    }

    @Step("Проверка что сброс поиска работает корректно")
    public void sendInviteToWaiterWithWrongEmail(String waiterName, String email) {

        searchWaiter(waiterName);

        clickInFirstResult();

        if (waiterStatusInCard.getText().matches(INVITED_IN_SERVICE_TEXT)) {

            cancelEMailWaiterInvitationInCard();

        } else if (waiterStatusInCard.getText().matches(VERIFIED_WAITER_TEXT)) {

            unlinkMailWaiterInCard();

        }

        isDetailWaiterCardCorrectWithWaitingInvitationStatus();

        sendKeys(enterEmailField,email);

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
        inviteButton.shouldBe(disabled);

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

    @Step("Переход в детальную карточку определенного официанта")
    public void goToCertainWaiterDetailCard(String waiterName) {

        searchWaiter(waiterName);
        clickInFirstResult();
        isDetailCardCorrect();

    }

    @Step("Проверка что элементы в карточке официанта корректны")
    public void isDetailCardCorrect() {

        isElementVisible(waiterStatusInCard);
        isElementVisible(waiterNameInCashDesk);
        isElementVisible(waiterName);
        isElementVisible(enterEmailField);
        isElementVisible(submitButton);

    }

    @Step("Проверка сортировки по алфавиту")
    public void isAlphabeticOrderCorrect() {

        List<String> actualTexts = new ArrayList<>();

        for (SelenideElement element : waiterListName) {
            actualTexts.add(element.getText());
        }

        Collections.sort(actualTexts);

        waiterListName.shouldHave(texts(actualTexts));

    }

    @Step("Поиск первого совпадения карточки официанта по статусу '{waiterStatus}' и клик в неё")
    public void findFirstMatchByStatusAndClickInWaiterCard(String waiterStatus) {

        for (SelenideElement paginationPage : waiterPaginationList) {

            click(paginationPage);
            isElementsCollectionVisible(waiterList);

            for (SelenideElement element: waiterList) {

                if(element.$(waiterStatusSelector).getText().equals(waiterStatus)) {

                    click(element);
                    isDetailCardCorrect();
                    return;

                }

            }

        }

    }

    @Step("Проверка ошибки раннее привязанной почты к другому официанту")
    public void isErrorMailCorrect(String email) {

        sendKeys(enterEmailField,email);
        click(inviteButton);

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
        cancelInvitationButton.shouldBe(visible,enabled);
        click(unlinkEmailConfirmButton);
        unlinkMailConfirmPopup.shouldBe(hidden,Duration.ofSeconds(5));
        enterEmailField.shouldHave(value(""));
        inviteButton.shouldBe(visible,disabled);

    }

    @Step("Отмена отправленного приглашению на авторизацию")
    public void cancelEMailWaiterInvitationInCard() {

        successSendingInvitation.shouldNotBe(visible,Duration.ofSeconds(6));
        cancelInvitationButton.shouldBe(visible,enabled);
        click(cancelInvitationButton);
        cancelMailConfirmationPopup.shouldBe(visible);
        click(cancelMailConfirmationSaveButton);
        cancelMailConfirmationPopup.shouldBe(hidden,Duration.ofSeconds(5));
        enterEmailField.shouldHave(value(""));
        inviteButton.shouldBe(visible,disabled);

    }

}
