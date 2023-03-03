package tapper_table;

import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.TapperTable.TEST_REVIEW_COMMENT;
import static data.selectors.TapperTable.ReviewPage.*;

public class ReviewPage extends BaseActions {

    RootPage rootPage = new RootPage();

    @Step("Форма статуса оплаты отображается. Проверки что нет ошибки, статус производится и успешно корректны")
    public void isPaymentProcessContainerShown() {

        isElementVisibleDuringLongTime(paymentProcessContainer, 30);

        isElementVisible(paymentProcessGifProcessing);
        isElementVisible(paymentProcessStatus);
        isElementVisible(paymentProcessText);
        paymentProcessStatus.shouldHave(matchText("Производится оплата"), Duration.ofSeconds(30));

        paymentProcessStatus.shouldNotHave(text("Оплата не прошла"))
                            .shouldHave(matchText("Оплата прошла успешно!"), Duration.ofSeconds(30));
        isElementVisible(paymentProcessGifSuccess);

    }

    @Step("Проверка заголовка статуса и что все элементы отзыва отображаются")
    public void isReviewBlockCorrect() {

        isElementVisibleDuringLongTime(reviewContainer, 10);

        isElementVisible(paymentLogo);
        isElementVisible(paymentStatusAfterPay);
        isElementVisible(paymentTime);
        isElementVisible(commentHeading);

        isElementVisible(isAllWasGoodHeading);
        isElementsListVisible(reviewStars);
        isElementVisible(reviewTextArea);
        isElementVisibleDuringLongTime(finishReviewButton, 10);

    }

    @Step("Заголовок соответствует частичной оплате")
    public void partialPaymentHeading() {

        isElementVisible(paymentLogo);
        paymentStatusAfterPay.shouldHave(text(" Статус заказа: Частично оплачен "));
        isElementVisible(paymentTime);

    }

    @Step("Заголовок соответствует полной оплате")
    public void fullPaymentHeading() {

        paymentLogo.shouldBe(visible);
        paymentStatusAfterPay.shouldHave(text(" Статус заказа: Полностью оплачен "));
        paymentTime.shouldBe(visible);

    }

    @Step("Проверка что определенное количество звезд отображает корректную форму с опциями")
    public void isPositiveAndNegativeOptionsCorrect() {

        isSuggestionContainerVisible(review1Star);

        isWhatDoULikeContainerVisible(review5Stars);

        isSuggestionContainerVisible(review3Stars);

        isWhatDoULikeContainerVisible(review4Stars);

    }

    public void isSuggestionContainerVisible(SelenideElement rateStar) {

        click(rateStar);

        isElementVisible(suggestionHeading);
        suggestionContainer.shouldHave(attribute("style",""));

    }

    public void isWhatDoULikeContainerVisible(SelenideElement rateStar) {

        click(rateStar);

        isElementInvisible(suggestionHeading);
        whatDoULikeList.shouldHave(attribute("style",""));

    }



    @Step("Выставляем 5 звёзд")
    public void rate5Stars() {

        click(review5Stars);
        review5Stars.shouldHave(attributeMatching("class",".*active.*"));

    }

    @Step("Выбираем рандомное пожелание если 4-5 звезды")
    public void chooseRandomWhatDoULikeWhenGreaterThan3() {

        isElementVisible(whatDoULikeList);
        click(whatDoULikeListRandomOption.get(generateRandomNumber(1, 5) - 1));
        isElementsListVisible(activeWhatDoULikeListRandomOption);

    }

    @Step("Выбираем рандомное пожелание")
    public void chooseRandomSuggestionWhenGreaterThan3() {

        isElementVisible(suggestionHeading);
        isElementsListVisible(suggestionOptions);

        click(suggestionOptions.get(generateRandomNumber(1, 4) - 1));

    }

    @Step("Вводим коммент в поле ввода, проверям что сохранился текст")
    public void typeReviewComment(String reviewComment) {

        sendKeys(reviewTextArea, reviewComment);
        reviewTextArea.shouldHave(value(reviewComment));

    }

    @Step("Клик в кнопку отправить отзыв и ожидание прелоадера")
    public void clickOnFinishButton() {

        click(finishReviewButton);

    }

}
