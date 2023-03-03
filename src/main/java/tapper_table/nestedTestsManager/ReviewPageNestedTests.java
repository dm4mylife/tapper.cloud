package tapper_table.nestedTestsManager;

import api.ApiRKeeper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import tapper_table.ReviewPage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.codeborne.selenide.Selenide.$$;
import static data.Constants.TestData.TapperTable.TEST_REVIEW_COMMENT_NEGATIVE;
import static data.Constants.TestData.TapperTable.TEST_REVIEW_COMMENT_POSITIVE;
import static data.selectors.TapperTable.ReviewPage.*;

public class ReviewPageNestedTests {

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

    @Step("Оставление отзыва 5 звезд с выбором рандомного предложения")
    public void reviewCorrectPositive() {

        reviewPage.click(review5Stars);
        reviewPage.chooseRandomWhatDoULikeWhenGreaterThan3();
        reviewPage.typeReviewComment(TEST_REVIEW_COMMENT_POSITIVE);
        reviewPage.clickOnFinishButton();

    }

    @Step("Оставление отзыва 1-3 звезды с выбором рандомного предложения")
    public void reviewCorrectNegative() {

        reviewPage.click(review1Star);
        reviewPage.chooseRandomSuggestionWhenGreaterThan3();
        reviewPage.typeReviewComment(TEST_REVIEW_COMMENT_NEGATIVE);
        reviewPage.clickOnFinishButton();

    }

    @Step("Сохраняем данные из рейтинга")
    public LinkedHashMap<String,String> saveReviewData(String tableNumber, String waiter,String reviewType) {

        LinkedHashMap<String,String> reviewData = new LinkedHashMap<>();
        String activeStars;
        List<String> suggestions;

        if (reviewType.equals("negative")) {

            activeStars = convertStarsRatingIntoInt();
            suggestions = convertNegativeSuggestionsIntoList();

        } else {

             activeStars = convertStarsRatingIntoInt();
             suggestions = convertPositiveSuggestionsIntoList();

        }

        reviewData.put("tableNumber", tableNumber);
        reviewData.put("waiter",waiter);
        reviewData.put("comment",reviewTextArea.getValue());
        reviewData.put("rating", activeStars);
        reviewData.put("suggestions",suggestions.toString());

        System.out.println(reviewData);
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

            if (Objects.equals(element.$(suggestionOptionsSvgSelector).getAttribute("style"), "")) {

                String suggestionName = element.getText();
                suggestionArray.add(suggestionName);

            }

        });


        System.out.println(suggestionArray);
        return suggestionArray;

    }

    public List<String> convertPositiveSuggestionsIntoList() {

        List<String> suggestionArray = new ArrayList<>();

        activeWhatDoULikeListRandomOption.asDynamicIterable().stream().forEach(element -> {

           String suggestion = element.getText();
           suggestionArray.add(suggestion);

        });


        System.out.println(suggestionArray);
        return suggestionArray;

    }



    @Step("Забираем с транзакции только информацию об оплате")
    public Map<String, Integer> getPayDataFromResponseAndConvToHashMap(Response rs) {

        Map<String, Integer> rsHashMap = new HashMap<>();

        Integer order_amount = rs.jsonPath().getInt("data.transaction_payment.order_amount");
        Integer tips = rs.jsonPath().getInt("data.transaction_payment.tips");
        Integer fee = rs.jsonPath().getInt("data.transaction_payment.fee");

        rsHashMap.put("order_amount", order_amount);
        rsHashMap.put("tips", tips);
        rsHashMap.put("fee", fee);

        return rsHashMap;

    }

    @Step("Получаем саму транзакцию б2п и сравниваем с суммами которые были при оплате")
    public void getTransactionAndMatchSums(String transactionId, HashMap<String, Integer> paymentData) {

        Response rs = apiRKeeper.getB2BPayment(transactionId);
        Map<String, Integer> rsPaymentData = getPayDataFromResponseAndConvToHashMap(rs);

        System.out.println(paymentData + " tapper data\n" + rsPaymentData + " b2b data");

        Assertions.assertEquals(rsPaymentData, paymentData, "Суммы в таппере не сходятся с транзакцией b2p");
        System.out.println("Все суммы в таппере сходятся с транзакцией b2p");

    }

}
