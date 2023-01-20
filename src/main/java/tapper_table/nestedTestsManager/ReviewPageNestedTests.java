package tapper_table.nestedTestsManager;

import api.ApiRKeeper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import tapper_table.ReviewPage;

import java.util.HashMap;
import java.util.Map;

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

        System.out.println(transactionId + " trans_id");
        System.out.println(paymentData + " payment data");

        Response rs = apiRKeeper.getB2BPayment(transactionId);
        Map<String, Integer> rsPaymentData = getPayDataFromResponseAndConvToHashMap(rs);

        System.out.println(paymentData + " tapper data");
        System.out.println(rsPaymentData + " b2b data");
        Assertions.assertEquals(rsPaymentData, paymentData, "Суммы в таппере не сходятся с транзакцией b2p");
        System.out.println("Все суммы в таппере сходятся с транзакцией b2p");


    }

}
