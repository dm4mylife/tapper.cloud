package tapper_table;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.TapperTable.FULL_PAY_STATUS_TEXT;
import static data.Constants.TestData.TapperTable.PART_PAY_STATUS_TEXT;
import static data.selectors.TapperTable.Common.orderPageContainer;
import static data.selectors.TapperTable.ReviewPage.*;

public class ReviewPage extends BaseActions {

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
        paymentStatusAfterPay.shouldHave(text(PART_PAY_STATUS_TEXT));
        isElementVisible(paymentTime);

    }

    @Step("Заголовок соответствует полной оплате")
    public void fullPaymentHeading() {

        paymentLogo.shouldBe(visible);
        paymentStatusAfterPay.shouldHave(text(FULL_PAY_STATUS_TEXT));
        paymentTime.shouldBe(visible);

    }

    @Step("Проверка что определенное количество звезд отображает корректную форму с опциями")
    public void isPositiveAndNegativeOptionsCorrect() {

        isSuggestionContainerVisible(review1Star);

        isWhatDoULikeContainerVisible(review5Stars);

        skipThanksReview();

        isSuggestionContainerVisible(review3Stars);

        isWhatDoULikeContainerVisible(review4Stars);

    }

    public void isSuggestionContainerVisible(SelenideElement rateStar) {

        click(rateStar);

        isElementVisible(suggestionHeading);
        suggestionContainer.shouldHave(attribute("style", ""));

    }

    public void skipThanksReview() {

        if (thanksReviewContainer.isDisplayed())
            click(thanksReviewCloseButton);

        isElementInvisible(thanksReviewContainer);

    }

    public void isWhatDoULikeContainerVisible(SelenideElement rateStar) {

        click(rateStar);

        isElementInvisible(suggestionHeading);
        whatDoULikeList.shouldHave(attribute("style", ""));

    }

    @Step("Проверка формы спасибо за отзывы")
    public void isThanksForReviewCorrect() {

        isElementVisible(thanksReviewContainer);
        isElementVisible(thanksReviewCloseButton);
        isElementsListVisible(reviewLinks);
        isElementVisible(reviewNoThanksButton);

        isLinkCorrect(yandexReviewLink, "yandex");
        click(review5Stars);
        isLinkCorrect(doubleGisReviewLink, "2gis");
        click(review5Stars);
        isLinkCorrect(googleReviewLink, "google");
        click(review5Stars);

        click(reviewNoThanksButton);
        isElementInvisible(thanksReviewContainer);

    }

    @Step("Проверка корректности перехода по ссылке")
    public void isLinkCorrect(SelenideElement element, String url) {

        click(element);
        switchBrowserTab(1);

        isTextContainsInURL(url);

        Selenide.closeWindow();
        switchBrowserTab(0);

    }

    @Step("Выбираем рандомное пожелание если 4-5 звезды")
    public void chooseRandomWhatDoULikeWhen() {

        isElementVisible(whatDoULikeList);
        click(whatDoULikeListRandomOption.get(generateRandomNumber(1, 5) - 1));
        isElementsListVisible(activeWhatDoULikeListRandomOption);

    }

    @Step("Выбираем первую и последнюю опцию если 4-5 звезды отзыв")
    public void choseFirstAndLastPositiveWhatDoULikeOptions() {

        isElementVisible(whatDoULikeList);
        click(whatDoULikeListRandomOption.first());
        click(whatDoULikeListRandomOption.last());
        isElementsListVisible(activeWhatDoULikeListRandomOption);
        activeWhatDoULikeListRandomOption.shouldHave(CollectionCondition.size(2));

    }

    @Step("Выбираем первую и последнюю опцию если 1-3 звезды отзыв")
    public void choseFirstAndLastNegativeSuggestionOptions() {

        isElementVisible(suggestionContainer);
        click(suggestionOptions.first());
        click(suggestionOptions.last());

        suggestionOptionsSvg.filter(attribute("style", ""))
                .shouldHave(CollectionCondition.size(2));

    }

    @Step("Выбираем рандомное пожелание")
    public void chooseRandomSuggestion() {

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
        isElementInvisible(reviewContainer);

    }

}
