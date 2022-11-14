package pages;

import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static constants.Constant.TestData.TEST_REVIEW_COMMENT;
import static constants.Selectors.Best2PayPage.paymentProcessContainer;
import static constants.Selectors.Common.bodyJS;
import static constants.Selectors.Common.pagePreLoader;
import static constants.Selectors.ReviewPage.*;

public class ReviewPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    @Step("Форма статуса оплаты отображается. Проверки что нет ошибки, статус производится и успешно корректны")
    public void isPaymentProcessContainerShown() {

        baseActions.isElementVisibleDuringLongTime(paymentProcessContainer,30);
        baseActions.isElementVisibleDuringLongTime(paymentProcessStatus, 30);

        paymentProcessStatus.shouldHave(matchText("Производится оплата"), Duration.ofSeconds(30));
        paymentProcessStatus.shouldNotHave(text("Оплата не прошла"));
        paymentProcessStatus.shouldHave(matchText("Оплата прошла успешно!"), Duration.ofSeconds(30));


    }

    @Step("Проверка заголовка статуса и что все элементы отзыва отображаются")
    public void isReviewBlockCorrect() {

        baseActions.isElementVisibleDuringLongTime(reviewContainer,10);

        baseActions.isElementVisible(reviewStars);
        baseActions.isElementVisible(reviewTextArea);
        baseActions.isElementVisibleDuringLongTime(finishReviewButton,10);

    }
    @Step("Заголовок соответствует частичной оплате")
    public void partialPaymentHeading() {

       paymentStatusAfterPay.shouldHave(text(" Статус заказа: Частично оплачен "));

    }

    @Step("Заголовок соответствует полной оплате")
    public void fullPaymentHeading() {

        paymentStatusAfterPay.shouldHave(text(" Статус заказа: Полностью оплачен "));

    }

    @Step("Выставляем 5 звёзд")
    public void rate5Stars() {

        baseActions.click(review5Stars);
        currentActiveStar.shouldBe(visible);

    }

    @Step("Выставляем 1 звёзду")
    public void rate1Stars() {

        baseActions.click(reviewStars);
        currentActiveStar.shouldBe(visible);

    }

    @Step("Выбираем рандомное пожелание если 4-5 звезды")
    public void chooseRandomWhatDoULikeWhenGreaterThan3() {

        baseActions.isElementVisible(whatDoULikeList);
        baseActions.click(whatDoULikeListRandomOption.get(generateRandomNumber(1,5)-1));
        baseActions.isElementVisible(activeWhatDoULikeListRandomOption);


    }

    @Step("Выбираем рандомное пожелание")
    public void chooseRandomSuggestionWhenGreaterThan3() {

        baseActions.isElementVisible(suggestionHeading);
        baseActions.isElementsListVisible(suggestionOptions);

        baseActions.click(suggestionOptions.get(generateRandomNumber(1,4)-1));


    }

    @Step("Вводим коммент в поле ввода, проверям что сохранился текст")
    public void typeReviewComment() {

        baseActions.sendHumanKeys(reviewTextArea,TEST_REVIEW_COMMENT);
        reviewTextArea.shouldHave(value(TEST_REVIEW_COMMENT));


    }

    @Step("Клик в кнопку отправить отзыв и ожидание прелоадера")
    public void clickOnFinishButton() {

        hideTapBar();
        baseActions.scroll(finishReviewButton);
        baseActions.click(finishReviewButton);
        baseActions.isElementVisible(pagePreLoader);
        showTapBar();

    }


}
