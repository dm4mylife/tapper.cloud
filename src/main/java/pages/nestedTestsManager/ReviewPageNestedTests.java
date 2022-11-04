package pages.nestedTestsManager;

import io.qameta.allure.Step;
import pages.ReviewPage;

public class ReviewPageNestedTests {

    ReviewPage reviewPage = new ReviewPage();


    @Step("Проверка поочередных статусов оплаты")
    public void paymentCorrect() {

        reviewPage.isPaymentProcessContainerShown();
        reviewPage.isReviewBlockCorrect();


    }

    @Step("Оставление отзыва")
    public void reviewCorrect() {

        reviewPage.rate5Stars();
        reviewPage.chooseRandomWhatDoULike();
        reviewPage.typeReviewComment();
        reviewPage.clickOnFinishButton();

    }


}
