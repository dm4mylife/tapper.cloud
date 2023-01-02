package tapper_table;

import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static constants.Constant.TestData.TEST_REVIEW_COMMENT;
import static constants.selectors.TapperTableSelectors.ReviewPage.*;

public class ReviewPage extends BaseActions {

    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();

    @Step("Форма статуса оплаты отображается. Проверки что нет ошибки, статус производится и успешно корректны")
    public void isPaymentProcessContainerShown() {

        rootPage.isStartScreenShown();
        baseActions.isElementVisibleDuringLongTime(paymentProcessContainer, 10);

        baseActions.isElementVisible(paymentProcessGifProcessing);
        baseActions.isElementVisible(paymentProcessStatus);
        baseActions.isElementVisible(paymentProcessText);
        paymentProcessStatus.shouldHave(matchText("Производится оплата"), Duration.ofSeconds(30));

        paymentProcessStatus.shouldNotHave(text("Оплата не прошла"));
        paymentProcessStatus.shouldHave(matchText("Оплата прошла успешно!"), Duration.ofSeconds(30));
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

        baseActions.click(reviewStars);
        review1Stars.shouldBe(visible);

    }

    @Step("Выбираем рандомное пожелание если 4-5 звезды")
    public void chooseRandomWhatDoULikeWhenGreaterThan3() {

        baseActions.isElementVisible(whatDoULikeList);
        baseActions.click(whatDoULikeListRandomOption.get(generateRandomNumber(1, 5) - 1));
        baseActions.isElementVisible(activeWhatDoULikeListRandomOption);

    }

    @Step("Выбираем рандомное пожелание")
    public void chooseRandomSuggestionWhenGreaterThan3() {

        baseActions.isElementVisible(suggestionHeading);
        baseActions.isElementsListVisible(suggestionOptions);

        baseActions.click(suggestionOptions.get(generateRandomNumber(1, 4) - 1));

    }

    @Step("Вводим коммент в поле ввода, проверям что сохранился текст")
    public void typeReviewComment() {

        baseActions.sendKeys(reviewTextArea, TEST_REVIEW_COMMENT);
        reviewTextArea.shouldHave(value(TEST_REVIEW_COMMENT));


    }

    @Step("Клик в кнопку отправить отзыв и ожидание прелоадера")
    public void clickOnFinishButton() {

        hideTapBar();
        scrollTillBottom();
        baseActions.click(finishReviewButton);
        showTapBar();

    }

}
