package tapper_table;

import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.TapperTable.TEST_REVIEW_COMMENT;
import static data.selectors.TapperTable.ReviewPage.*;

public class ReviewPage extends BaseActions {

    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();

    @Step("Форма статуса оплаты отображается. Проверки что нет ошибки, статус производится и успешно корректны")
    public void isPaymentProcessContainerShown() {

        baseActions.isElementVisibleDuringLongTime(paymentProcessContainer, 30);

        baseActions.isElementVisible(paymentProcessGifProcessing);
        baseActions.isElementVisible(paymentProcessStatus);
        baseActions.isElementVisible(paymentProcessText);
        paymentProcessStatus.shouldHave(matchText("Производится оплата"), Duration.ofSeconds(30));

        paymentProcessStatus.shouldNotHave(text("Оплата не прошла"))
                            .shouldHave(matchText("Оплата прошла успешно!"), Duration.ofSeconds(30));
        baseActions.isElementVisible(paymentProcessGifSuccess);

    }

    @Step("Проверка заголовка статуса и что все элементы отзыва отображаются")
    public void isReviewBlockCorrect() {

        baseActions.isElementVisibleDuringLongTime(reviewContainer, 10);

        baseActions.isElementVisible(reviewStars);
        baseActions.isElementVisible(reviewTextArea);
        baseActions.isElementVisibleDuringLongTime(finishReviewButton, 10);

    }

    @Step("Заголовок соответствует частичной оплате")
    public void partialPaymentHeading() {

        paymentLogo.shouldBe(visible);
        paymentStatusAfterPay.shouldHave(text(" Статус заказа: Частично оплачен "));
        paymentTime.shouldBe(visible);

    }

    @Step("Заголовок соответствует полной оплате")
    public void fullPaymentHeading() {

        paymentLogo.shouldBe(visible);
        paymentStatusAfterPay.shouldHave(text(" Статус заказа: Полностью оплачен "));
        paymentTime.shouldBe(visible);

    }

    @Step("Выставляем 5 звёзд")
    public void rate5Stars() {

        baseActions.click(review5Stars);
        review5Stars.$("svg").shouldHave(attributeMatching("class",".*active.*"));

    }

    @Step("Выставляем 1 звезду")
    public void rate1Stars() {

        click(reviewStars);
        click(review1Stars);

    }

    @Step("Выбираем рандомное пожелание если 4-5 звезды")
    public void chooseRandomWhatDoULikeWhenGreaterThan3() {

        isElementVisible(whatDoULikeList);
        click(whatDoULikeListRandomOption.get(generateRandomNumber(1, 5) - 1));
        isElementVisible(activeWhatDoULikeListRandomOption);

    }

    @Step("Выбираем рандомное пожелание")
    public void chooseRandomSuggestionWhenGreaterThan3() {

        isElementVisible(suggestionHeading);
        isElementsListVisible(suggestionOptions);

        click(suggestionOptions.get(generateRandomNumber(1, 4) - 1));

    }

    @Step("Вводим коммент в поле ввода, проверям что сохранился текст")
    public void typeReviewComment() {

        sendKeys(reviewTextArea, TEST_REVIEW_COMMENT);
        reviewTextArea.shouldHave(value(TEST_REVIEW_COMMENT));

    }

    @Step("Клик в кнопку отправить отзыв и ожидание прелоадера")
    public void clickOnFinishButton() {

        click(finishReviewButton);

    }

}
