package tapper_admin;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static constants.Constant.TestData.TEST_YANDEX_LOGIN_EMAIL;
import static constants.SelectorsTapperAdmin.RKeeperAdmin.*;
import static constants.SelectorsTapperAdmin.RKeeperAdmin.WaiterMenu.*;


public class AdminAccount extends BaseActions {

    @Step("Переход в меню официанта")
    public void goToWaiterCategory() {

        click(waiterMenuCategory);
        waiterHeading.shouldHave(text("Официанты"), Duration.ofSeconds(5));
        waiterContainer.shouldBe(visible);

    }

    @Step("Проверка что официанта находится по имени")
    public void searchWaiter(String waiterName) {

        searchField.clear();
        searchField.sendKeys(waiterName);
        waiterList.shouldBe(CollectionCondition.sizeGreaterThan(0),Duration.ofSeconds(5));
        waiterCardName.shouldHave(text(waiterName));

    }

    @Step("Проверка что поиск выдаст ошибку если такого официанта нет")
    public void searchWaiterNegative() {

        searchField.sendKeys("Ингеборга Эдмундовна Дапкунайте");
        waiterList.shouldBe(CollectionCondition.size(0),Duration.ofSeconds(5));
        searchError.shouldBe(Condition.visible, text("Нет результатов. Попробуйте ввести данные ещё раз"));

    }

    @Step("Проверка что сброс поиска работает корректно")
    public void resetSearchResult() {

        click(searchResetButton);
        waiterList.shouldBe(CollectionCondition.size(10),Duration.ofSeconds(5));
        searchField.shouldHave(empty);
        searchResetButton.shouldNotBe(visible);

    }

    @Step("Проверка что все элементы в детальной карточке корректны")
    public void isWaitingInvitationCardCorrect() {

        isElementVisible(backToPreviousPage);
        isElementVisible(enterEmailField);
        waiterStatusInCard.shouldHave(text(" Статус: Ожидает приглашения"));
        isElementVisible(waiterNameInCashDesk);
        isElementVisible(waiterName);
        isElementVisible(waiterEmail);
        isElementVisible(inviteButton);

    }

    @Step("Проверка что сброс поиска работает корректно")
    public void sendInviteToWaiterEmail(String waiterName) {

        searchWaiter(waiterName);

        click(waiterList.get(0));

        isWaitingInvitationCardCorrect();

        inviteButton.shouldBe(disabled);
        inviteButton.shouldHave(text(" Пригласить официанта "));

        enterEmailField.shouldBe(visible,Duration.ofSeconds(2));
        enterEmailField.shouldHave(value(""));
        forceWait(2000); // toDO не успевает прогрузиться инпут емейла, обрезается
        sendHumanKeys(enterEmailField,TEST_YANDEX_LOGIN_EMAIL);

        click(inviteButton);
        enterEmailField.shouldHave(value(TEST_YANDEX_LOGIN_EMAIL));

        successSendingInvitation.shouldBe(visible,Duration.ofSeconds(3));
        cancelInvitationButton.shouldBe(visible);

    }

    @Step("Проверка что в таппере также изменился статус верификации официанта")
    public void checkVerificationWaiterStatusAfterConfirmation(String waiterName) {


        forceWait(15000); // toDO время до доставки письма
        click(backToPreviousPage);

        searchWaiter(waiterName);

        waiterStatusInPreview.shouldHave(text("Приглашён в систему"));
        System.out.println("статус окей");

    }

}
