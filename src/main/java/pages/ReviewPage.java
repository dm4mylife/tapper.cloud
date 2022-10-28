package pages;

import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import constants.Selectors;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$x;
import static constants.Constant.TestData.TEST_REVIEW_COMMENT;
import static constants.Selectors.RootPage.*;
import static constants.Selectors.ReviewPage.paymentStatusAfterPay;

public class ReviewPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    public void isPaymentProcessContainerShown() {

        baseActions.isElementVisibleDuringLongTime(paymentProcessContainer,30);
        baseActions.isElementVisibleDuringLongTime(Selectors.ReviewPage.paymentProcessStatus, 30);

        Selectors.ReviewPage.paymentProcessStatus.shouldHave(matchText("Производится оплата"), Duration.ofSeconds(30));
        System.out.println(Selectors.ReviewPage.paymentProcessStatus.getText());
        Selectors.ReviewPage.paymentProcessStatus.shouldHave(matchText("Оплата прошла успешно!"), Duration.ofSeconds(30));
        System.out.println(Selectors.ReviewPage.paymentProcessStatus.getText());

    }

    public void isReviewBlockCorrect() {

        baseActions.isElementVisibleDuringLongTime(Selectors.ReviewPage.reviewContainer,10);
        isPaymentStatusSuccess();
        baseActions.isElementVisible(Selectors.ReviewPage.reviewStars);
        baseActions.isElementVisible(Selectors.ReviewPage.reviewTextArea);
        baseActions.isElementVisible(Selectors.ReviewPage.finishReviewButton);


    }

    public void isPaymentStatusSuccess() {

        paymentStatusAfterPay.shouldHave(text(" Статус заказа: Полностью оплачен "));

    }

    public void rate5Stars() {

        baseActions.click(Selectors.ReviewPage.review5Stars);
        Selectors.ReviewPage.currentActiveStar.shouldBe(visible);


    }

    public void chooseRandomWhatDoULike() {

        baseActions.isElementVisible(Selectors.ReviewPage.whatDoULikeList);

        String rndNumber = String.valueOf(baseActions.generateRandomNumber(1,5));
        String xPath = "//li[@class=\"review__list-item\"]" + "[" + rndNumber + "]";
        SelenideElement randomItem = $x(xPath);

        baseActions.click(randomItem);
        baseActions.isElementVisible(Selectors.ReviewPage.activeWhatDoULikeListRandomOption);


    }

    public void typeReviewComment() {

        baseActions.sendHumanKeys(Selectors.ReviewPage.reviewTextArea,TEST_REVIEW_COMMENT);
        Selectors.ReviewPage.reviewTextArea.shouldHave(value(TEST_REVIEW_COMMENT));

    }

    public void clickOnFinishButton() {

        baseActions.click(Selectors.ReviewPage.finishReviewButton);
        baseActions.isElementVisible(pagePreLoader);

    }













}
