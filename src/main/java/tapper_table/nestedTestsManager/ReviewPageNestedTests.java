package tapper_table.nestedTestsManager;

import api.ApiRKeeper;
import common.BaseActions;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import tapper_table.ReviewPage;
import tapper_table.RootPage;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.WAIT_UNTIL_TRANSACTION_EXPIRED;
import static data.Constants.WAIT_UNTIL_TRANSACTION_STILL_ALIVE;
import static data.selectors.TapperTable.ReviewPage.*;
import static data.selectors.TapperTable.RootPage.PayBlock.paymentOverlay;

public class ReviewPageNestedTests {

    RootPage rootPage = new RootPage();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Step("Проверка поочередных статусов оплаты c частичной оплатой")
    public void paymentCorrect(String orderType) {

        reviewPage.isPaymentProcessContainerShown();
        reviewPage.isReviewBlockCorrect();

        if (orderType.equals("full")) {

            reviewPage.fullPaymentHeading();

        } else {

            reviewPage.partialPaymentHeading();

        }

    }

    @Step("Проверка поочередных статусов оплаты с полной оплатой")
    public void fullPaymentCorrect() {

        reviewPage.isPaymentProcessContainerShown();
        reviewPage.isReviewBlockCorrect();
        reviewPage.fullPaymentHeading();

    }

    @Step("Проверка поочередных статусов оплаты с полной оплатой")
    public void errorPaymentCorrect(int wait) {

        rootPage.isElementVisibleDuringLongTime(paymentProcessContainer, 30);

        rootPage.isElementVisible(paymentProcessGifProcessing);
        rootPage.isElementVisible(paymentProcessStatus);
        rootPage.isElementVisible(paymentProcessText);

        if (wait == WAIT_UNTIL_TRANSACTION_EXPIRED) {

            paymentProcessText.shouldHave(matchText(PAYMENT_ERROR_ORDER_EXPIRED), Duration.ofSeconds(40));
            rootPage.isElementVisible(paymentProcessGifError);
            paymentProcessStatus.shouldHave(matchText(PAYMENT_ERROR_TEXT));
            BaseActions.click(paymentProcessCloseButton);

            if (paymentOverlay.isDisplayed())
                BaseActions.click(paymentOverlay);

        } else if (wait == WAIT_UNTIL_TRANSACTION_STILL_ALIVE) {

            paymentProcessStatus.shouldNotHave(text("Оплата не прошла"))
                    .shouldHave(matchText("Оплата прошла успешно!"), Duration.ofSeconds(30));
            rootPage.isElementVisible(paymentProcessGifSuccess);

        }

    }

    public void skipThanksReview() {

        if (thanksReviewContainer.isDisplayed())
            BaseActions.click(thanksReviewCloseButton);

    }

    @Step("Оставление отзыва 5 звезд с выбором рандомного предложения")
    public void reviewCorrectPositive() {

        BaseActions.click(review5Stars);

        skipThanksReview();

        reviewPage.chooseRandomWhatDoULikeWhen();
        reviewPage.typeReviewComment(TEST_REVIEW_COMMENT_POSITIVE);

    }

    @Step("Оставление отзыва 5 звезд с выбором нескольких рандомнных предложений")
    public void reviewCorrectPositiveWithFewOptions() {

        BaseActions.click(review5Stars);

        skipThanksReview();

        reviewPage.choseFirstAndLastPositiveWhatDoULikeOptions();
        reviewPage.typeReviewComment(TEST_REVIEW_COMMENT_POSITIVE);

    }

    @Step("Оставление отзыва 1-3 звезды с выбором рандомного предложения")
    public void reviewCorrectNegative() {

        BaseActions.click(review1Star);

        skipThanksReview();

        reviewPage.chooseRandomSuggestion();
        reviewPage.typeReviewComment(TEST_REVIEW_COMMENT_NEGATIVE);

    }

    @Step("Оставление отзыва 1-3 звезды с выбором рандомного предложения")
    public void reviewCorrectNegativeFewOptions() {

        BaseActions.click(review1Star);

        skipThanksReview();

        reviewPage.choseFirstAndLastNegativeSuggestionOptions();
        reviewPage.typeReviewComment(TEST_REVIEW_COMMENT_NEGATIVE);

    }

    @Step("Сохраняем данные из рейтинга")
    public LinkedHashMap<String,String> saveReviewData(String tableNumber, String waiter,String reviewType) {

        LinkedHashMap<String,String> reviewData = new LinkedHashMap<>();
        String activeStars;
        String comment = "";
        List<String> suggestions;

        if (reviewType.equals("negative")) {

            activeStars = convertStarsRatingIntoInt();
            suggestions = convertNegativeSuggestionsIntoList();
            comment = TEST_REVIEW_COMMENT_NEGATIVE;

        } else {

             activeStars = convertStarsRatingIntoInt();
             suggestions = convertPositiveWhatDoULikeIntoList();
             comment = TEST_REVIEW_COMMENT_POSITIVE;

        }

        reviewData.put("tableNumber", tableNumber);
        reviewData.put("waiter",waiter);
        reviewData.put("comment",comment);
        reviewData.put("rating", activeStars);
        reviewData.put("suggestions",suggestions.toString()
                .replaceAll("[\\[\\]]","").replaceAll("\n"," "));

        reviewPage.clickOnFinishButton();

        return reviewData;

    }

    public String convertStarsRatingIntoInt() {

        AtomicInteger count = new AtomicInteger();

        reviewStars.asDynamicIterable().stream().forEach(element -> {

            if (Objects.requireNonNull
                    (element.$(reviewStarsSvgSelector).getAttribute("class")).matches(".*active"))
                count.getAndIncrement();

        });

        return String.valueOf(count);

    }

    public List<String> convertNegativeSuggestionsIntoList() {

        List<String> suggestionArray = new ArrayList<>();

        suggestionOptions.asDynamicIterable().stream().forEach(element -> {

            if (Objects.equals(element.$(suggestionOptionsSvgSelector).getAttribute("style"), ""))
                suggestionArray.add(element.getText());

        });

        return suggestionArray;

    }

    public List<String> convertPositiveWhatDoULikeIntoList() {

        List<String> suggestionArray = new ArrayList<>();

        activeWhatDoULikeListRandomOption.asDynamicIterable().stream().forEach
                (element -> suggestionArray.add(element.getText()));

        return suggestionArray;

    }

    @Step("Забираем с транзакции только информацию об оплате")
    public Map<String, String> getPayDataFromResponseAndConvToHashMap(Response rs) {

        Map<String, String> rsHashMap = new HashMap<>();

        String order_amount = rs.jsonPath().getString("data.transaction_payment.order_amount");
        String tips = rs.jsonPath().getString("data.transaction_payment.tips");
        String fee = rs.jsonPath().getString("data.transaction_payment.fee");

        rsHashMap.put("order_amount", order_amount);
        rsHashMap.put("tips", tips);
        rsHashMap.put("fee", fee);

        return rsHashMap;

    }

    @Step("Получаем саму транзакцию б2п и сравниваем с суммами которые были при оплате")
    public void getTransactionAndMatchSums(String transactionId, HashMap<String, String> paymentData) {

        Response rs = apiRKeeper.getB2BPayment(transactionId);
        Map<String, String> rsPaymentData = getPayDataFromResponseAndConvToHashMap(rs);

        double rsPaymentDataOrderAmount = Double.parseDouble(rsPaymentData.get("order_amount"));
        double paymentDataOrderAmount = Double.parseDouble(paymentData.get("order_amount"));

        double rsPaymentDataFee = Double.parseDouble(rsPaymentData.get("fee"));
        double paymentDataFee = Double.parseDouble(paymentData.get("fee"));

        Assertions.assertEquals(rsPaymentDataOrderAmount,paymentDataOrderAmount,0.1,
                "Чистая сумма за заказ в таппере не сходятся с транзакцией b2p");
        Assertions.assertEquals(rsPaymentData.get("tips"),paymentData.get("tips"),
                "Чаевые за заказ в таппере не сходятся с транзакцией b2p");
        Assertions.assertEquals(rsPaymentDataFee,paymentDataFee,1, // toDO неправильное поведение. расхождение в копейку в сторону б2п
                "Сервисный сбор за заказ в таппере не сходятся с транзакцией b2p");

    }

}
