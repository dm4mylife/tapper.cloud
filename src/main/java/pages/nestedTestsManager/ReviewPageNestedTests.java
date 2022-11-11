package pages.nestedTestsManager;

import io.qameta.allure.Step;
import pages.ReviewPage;

public class ReviewPageNestedTests {

    ReviewPage reviewPage = new ReviewPage();


    @Step("Проверка поочередных статусов оплаты c частичной оплатой")
    public void partialPaymentCorrect() {

        reviewPage.isPaymentProcessContainerShown();
        reviewPage.isReviewBlockCorrect();
        reviewPage.partialPaymentHeading();

    }

    @Step("Проверка поочередных статусов оплаты с полной оплатой")
    public void fullPaymentCorrect() {

        reviewPage.isPaymentProcessContainerShown();
        reviewPage.isReviewBlockCorrect();
        reviewPage.fullPaymentHeading();

    }


    @Step("Оставление отзыва 5 звезд с выбором рандомного предложения")
    public void reviewCorrectPositive() {

        reviewPage.rate5Stars();
        reviewPage.chooseRandomWhatDoULikeWhenGreaterThan3();
        reviewPage.typeReviewComment();
        reviewPage.clickOnFinishButton();

    }

    @Step("Оставление отзыва 3 звезды с выбором рандомного предложения")
    public void reviewCorrectNegative() {

        reviewPage.rate1Stars();
        reviewPage.chooseRandomSuggestionWhenGreaterThan3();
        reviewPage.typeReviewComment();
        reviewPage.clickOnFinishButton();

    }

}
