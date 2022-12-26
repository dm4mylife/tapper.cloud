package tapper_admin_personal_account.waiters;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static constants.selectors.AdminPersonalAccountSelectors.Common.*;
import static constants.selectors.AdminPersonalAccountSelectors.Waiters.*;
import static constants.Constant.TestData.TEST_YANDEX_LOGIN_EMAIL;


public class Waiters extends BaseActions {

    @Step("Переход в меню официанта")
    public void goToWaiterCategory() {

        click(waiterMenuCategory);
        pageHeading.shouldHave(text("Официанты"), Duration.ofSeconds(5));
        waiterContainer.shouldBe(visible);
        isWaiterCategoryCorrect();

    }

    @Step("Проверка что все элементы в поиске официанта корректны")
    public void isWaiterCategoryCorrect() {

        waiterListHeading.shouldBe(visible);
        refreshListButton.shouldBe(visible);
        waiterPaginationList.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        refreshListButton.click();
        pagePreloader.shouldHave(cssValue("display","flex"));
        pagePreloader.shouldHave(cssValue("display","none"),Duration.ofSeconds(20));

    }

    @Step("Проверка что официанта находится по имени")
    public void searchWaiter(String waiterName) {

        searchField.clear();
        searchField.sendKeys(waiterName);
        waiterList.shouldBe(CollectionCondition.sizeGreaterThan(0),Duration.ofSeconds(5));
        waiterCardName.shouldHave(text(waiterName));
        forceWait(1200); //toDo слишком быстро идёт тест, визуально не видно

    }

    @Step("Проваливаемся в первую карточку из результатов поиска официантов")
    public void clickInFirstResult() {
        click(waiterList.get(0));
    }

    @Step("Проверка что поиск выдаст ошибку если такого официанта нет")
    public void searchWaiterNegative() {

        searchField.clear();
        searchField.sendKeys("Ингеборга Эдмундовна Дапкунайте");
        waiterList.shouldBe(CollectionCondition.size(0),Duration.ofSeconds(5));
        searchError.shouldBe(Condition.visible, text("Нет результатов. Попробуйте ввести данные ещё раз"));
        System.out.println("Негативный поиск отработал корректно");
        forceWait(2000); // toDO слишком быстро работает тест, не успеваем посмотреть отрицательный поиск

    }

    @Step("Проверка что сброс поиска работает корректно")
    public void resetSearchResult() {

        click(searchResetButton);
        waiterList.shouldBe(CollectionCondition.size(10),Duration.ofSeconds(5));
        searchField.shouldHave(empty);
        searchResetButton.shouldNotBe(visible);

    }

    @Step("Проверка что все элементы в детальной карточке корректны если статус у карточки 'Ожидает приглашения'")
    public void isDetailWaiterCardCorrectWithWaitingInvitationStatus() {

        isElementVisible(backToPreviousPage);
        waiterStatusInCard.shouldHave(text(" Статус: Ожидает приглашения"));
        isElementVisible(waiterNameInCashDesk);
        isElementVisible(waiterName);
        isElementVisible(enterEmailField);
        isElementVisible(inviteButton);

    }

    @Step("Проверка что все элементы в детальной карточке корректны если статус у карточки 'Официант верифицирован'")
    public void isDetailWaiterCardCorrectWithWaiterVerifiedStatus() {

        isElementVisible(backToPreviousPage);
        waiterStatusInCard.shouldHave(text(" Статус: Официант верифицирован"));
        isElementVisible(waiterNameInCashDesk);
        isElementVisible(waiterName);
        isElementVisible(enterEmailField);
        enterEmailField.shouldBe(disabled);
        isElementVisible(saveButton);

    }

    @Step("Проверка что все элементы в детальной карточке корректны если статус у карточки 'Приглашен в систему'")
    public void isDetailWaiterCardCorrectWithInvitedToSystemStatus() {

        isElementVisible(backToPreviousPage);
        waiterStatusInCard.shouldHave(text(" Статус: Приглашен в систему"));
        isElementVisible(waiterNameInCashDesk);
        isElementVisible(waiterName);
        isElementVisible(enterEmailField);
        enterEmailField.shouldBe(disabled);
        inviteButton.shouldBe(disabled);
        isElementVisible(inviteButton);

    }

    @Step("Проверка что сброс поиска работает корректно")
    public void sendInviteToWaiterEmail(String waiterName, String email) {

        searchWaiter(waiterName);

        clickInFirstResult();

        isDetailWaiterCardCorrectWithWaitingInvitationStatus();

        forceWait(1000); // toDO не успевает прогрузиться инпут емейла, обрезается

        sendKeys(enterEmailField,email);
        click(inviteButton);

        isSendInvitationCorrect(email);

    }

    @Step("Проверка что появилось уведомление об отправке приглашения, почта сохранилась в детальной карточке официанта")
    public void isSendInvitationCorrect(String email) {

        enterEmailField.shouldHave(value(email));
        successSendingInvitation.shouldBe(visible,Duration.ofSeconds(5));
        cancelInvitationButton.shouldBe(visible);

    }

    @Step("Проверка что в таппере также изменился статус верификации официанта в превью и детальной карточке")
    public void isWaiterStatusCorrectInPreviewAndCard(String waiterName,String waiter_status) {

        searchWaiter(waiterName);
        waiterStatusInPreview.shouldHave(matchText(waiter_status));

        click(waiterList.get(0));

        waiterStatusInCard.shouldHave(matchText(waiter_status));

    }

    @Step("Поиск первого совпадения карточки официанта по статусу '{waiterStatus}' и клик в неё")
    public void findFirstMatchByStatusAndClickInWaiterCard(String waiterStatus) {

        int paginationSize = waiterPaginationList.size();

        for (int index = 0; index < paginationSize; index++) {

            for (SelenideElement element: waiterList) {

                if(element.$(".vWaiterItem__waiterStatus").getText().equals(waiterStatus)) {

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

        errorMsgEmailWasApplied.shouldHave(Condition.text("E-mail уже привязан к другому официанту "), Duration.ofSeconds(3));
        enterEmailFieldWrapper.shouldHave(cssValue("border-color","rgb(236, 78, 78)"));
        System.out.println("Ошибка привязанного email отображается");

    }

    @Step("Удаление почты и привязки у официанта")
    public void unlinkMailWaiter() {

        click(cancelInvitationButton);
        unlinkMailConfirmPopup.shouldBe(visible);
        click(confirmUnlinkEmailButton);
        enterEmailField.shouldHave(value(""));
        inviteButton.shouldBe(visible,disabled);
        System.out.println("Удалена полностью почта, официант более не в статусе верифицирован");

    }

    @Step("Отмена отправленного приглашению на авторизацию")
    public void cancelMailWaiter() {

        successSendingInvitation.shouldNotBe(visible,Duration.ofSeconds(6));
        click(cancelInvitationButton);
        cancelMailConfirmPopup.shouldBe(visible);
        click(confirmCancelingEmailButton);
        enterEmailField.shouldHave(value(""));
        inviteButton.shouldBe(visible,disabled);
        System.out.println("Удалена полностью почта, официант более не в статусе верифицирован");

    }

}
