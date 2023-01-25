package tapper_table;

import api.ApiRKeeper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static constants.Constant.RegexPattern.*;
import static io.restassured.RestAssured.given;

public class Telegram {

    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Step("Получаем сообщения из телеграмма")
    public void getUpdates() {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .when()
                .get("https://api.telegram.org/bot5989489181:AAGsWoVW-noi9lDDx11H-nGPNPOuw8XtCZI/getUpdates?offset=20")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");

    }

    @Step("Получаем список всех сообщений об оплате")
    public String getLastTgPayMsgList(String guid) {

        List<Object> tgMessages = apiRKeeper.getUpdates();
        String lastTgMsg;
        int msgListByNeededGuidIndex = 0;

        HashMap<Integer,String> msgListByNeededGuid = new HashMap<>();

        for (Object tgMessage : tgMessages) {

            String currentMsg = tgMessage.toString();

            if (currentMsg.contains(guid)) {

                System.out.println("\nСообщение подходящее по текущему заказу \n");

                msgListByNeededGuid.put(msgListByNeededGuidIndex, currentMsg);
                System.out.println(msgListByNeededGuid.size() + " количество сообщений");

                msgListByNeededGuidIndex++;

            }

        }

        if (msgListByNeededGuid.size() > 0) {

            lastTgMsg = msgListByNeededGuid.get(msgListByNeededGuid.size()-1);

        } else {

            System.out.println("Сообщения не были найдены");
            return null;

        }

        Allure.addAttachment("Сообщение в телеграмме", "text/plain", String.valueOf(lastTgMsg));

        return lastTgMsg;

    }

    @Step("Получаем список всех сообщений об официанте")
    public String getLastTgWaiterMsgList(String tableNumber) {

        List<Object> tgMessages = apiRKeeper.getUpdates();
        String lastTgMsg;
        int msgListByNeededGuidIndex = 0;

        HashMap<Integer,String> msgListByNeededGuid = new HashMap<>();

        for (Object tgMessage : tgMessages) {

            String currentMsg = tgMessage.toString();

            if (currentMsg.contains(tableNumber)) {

                System.out.println("\nСообщение подходящее по текущему заказу \n");

                msgListByNeededGuid.put(msgListByNeededGuidIndex, currentMsg);
                System.out.println(msgListByNeededGuid.size() + " количество сообщений");

                msgListByNeededGuidIndex++;
            }

        }

        System.out.println(msgListByNeededGuid);

        if (msgListByNeededGuid.size() > 0) {

            String tgMsg = msgListByNeededGuid.get(msgListByNeededGuid.size()-1);
            System.out.println("\nПоследнее сообщение в тг\n" + tgMsg);
            lastTgMsg = tgMsg;

        } else {

            System.out.println("Сообщения не были найдены");
            return null;

        }

        System.out.println(lastTgMsg);
        return lastTgMsg;

    }

    @Step("Определяем и устанавливаем тип сообщения")
    public HashMap <String,String> setMsgTypeFlag(String tgMsg) {

        HashMap <String,String> msgToParse = new HashMap<>();

        String isOrderPay = "Статус оплаты:";
        String isOrderError = "Причина:";

        String isCallWaiterMsg = "Вызов официанта";
        String isReviewMsg = "Рейтинг:";
        String noReviewMsg = "Рейтинг: 0";
        String menuAddMsg = "Гость попросил добавить меню";

        if (tgMsg.contains(isOrderPay)) {

            msgToParse.put("msgType","orderPay");

            if (tgMsg.contains(isOrderError)) {

                msgToParse.put("msgType","orderPayError");

            }

        } else if (tgMsg.contains(isCallWaiterMsg)) {

            msgToParse.put("msgType", "callWaiter");

        } else if (tgMsg.contains(noReviewMsg)) {

            msgToParse.put("msgType","zeroReview");

        } else if (tgMsg.contains(isReviewMsg)) {

            msgToParse.put("msgType","review");

        } else if (tgMsg.contains(menuAddMsg)) {

        msgToParse.put("msgType","menuAdd");

    }

        msgToParse.put("message",tgMsg);

        return msgToParse;

    }

    @Step("Наполнение хешкарты если сообщение типа оплаты, без ошибки")
    public LinkedHashMap<String, String> paymentFiller(String textMsg,LinkedHashMap<String, String> tgParsedText) {

        String table = textMsg.replaceAll(tableRegex, "$2");
        String sumInCheck = textMsg.replaceAll(sumInCheckRegex, "$2");
        String restToPay = textMsg.replaceAll(restToPayRegex, "$2");
        String tips = textMsg.replaceAll(tipsRegex, "$2");
        String paySum = textMsg.replaceAll(paySumRegex, "$2");
        String totalPaid = textMsg.replaceAll(totalPaidRegex, "$2");
        String payStatus = textMsg.replaceAll(payStatusRegex, "$2");
        String orderStatus = textMsg.replaceAll(orderStatusRegex, "$2").trim();
        String waiter = textMsg.replaceAll(waiterRegex, "$2");

        tgParsedText.put("table", table);
        tgParsedText.put("sumInCheck", sumInCheck);
        tgParsedText.put("restToPay", restToPay);
        tgParsedText.put("tips", tips);
        tgParsedText.put("paySum", paySum);
        tgParsedText.put("totalPaid", totalPaid);

        if (textMsg.contains("Скидка:")) {

            String discount = textMsg.replaceAll(discountRegex,"$2");
            System.out.println(discount + " discount");
            tgParsedText.put("discount", discount);

        } else if (textMsg.contains("Наценка:")) {

            String markUp = textMsg.replaceAll(markUpRegex,"$2");
            System.out.println(markUp + " markUp");
            tgParsedText.put("markUp", markUp);

        }

        tgParsedText.put("payStatus", payStatus);
        tgParsedText.put("orderStatus", orderStatus);
        tgParsedText.put("waiter", waiter);

        return tgParsedText;

    }

    @Step("Наполнение хешкарты если сообщение типа оплаты с ошибкой")
    public LinkedHashMap<String, String> paymentErrorFiller(String textMsg,LinkedHashMap<String, String> tgParsedText) {

        LinkedHashMap<String, String> paymentData = paymentFiller(textMsg,tgParsedText);

        String reason = textMsg.replaceAll(reasonError, "$2");
        System.out.println(reason + " reason");

        paymentData.put("reasonError",reason);

        return paymentData;

    }

    @Step("Наполнение хешкарты если сообщение типа нулевой отзыв")
    public LinkedHashMap<String, String> zeroReviewFiller(String textMsg,LinkedHashMap<String, String> tgParsedText) {

        String restaurantName= textMsg.replaceAll(restaurantNameRegex, "$2");
        String waiter = textMsg.replaceAll(waiterRegex, "$2");
        String table = textMsg.replaceAll(tableRegex, "$2");
        String ratingComment = textMsg.replaceAll(ratingCommentRegex, "$2");
        String rating = textMsg.replaceAll(ratingRegex, "$2");

        tgParsedText.put("restaurantName", restaurantName);
        tgParsedText.put("table", table);
        tgParsedText.put("waiter", waiter);
        tgParsedText.put("ratingComment", ratingComment);
        tgParsedText.put("rating", rating);

        return tgParsedText;

    }

    @Step("Наполнение хешкарты если сообщение типа отзыв")
    public LinkedHashMap<String, String> reviewFiller(String textMsg,LinkedHashMap<String, String> tgParsedText) {

        String waiter = textMsg.replaceAll(waiterRegex, "$2");
        String table = textMsg.replaceAll(tableRegex, "$2");
        String ratingComment = textMsg.replaceAll(ratingCommentRegex, "$2");
        String rating = textMsg.replaceAll(ratingRegex, "$2");
        String suggestion = textMsg.replaceAll(suggestionRegex, "$2");

        tgParsedText.put("table", table);
        tgParsedText.put("waiter", waiter);
        tgParsedText.put("ratingComment", ratingComment);
        tgParsedText.put("rating", rating);
        tgParsedText.put("suggestion", suggestion);

        return tgParsedText;

    }

    @Step("Наполнение хешкарты если сообщение типа вызов официанта")
    public LinkedHashMap<String, String> callWaiterFiller(String textMsg,LinkedHashMap<String, String> tgParsedText) {

        String tableReview = textMsg.replaceAll(tableReviewRegex, "$2");
        String callWaiterComment = textMsg.replaceAll(callWaiterCommentRegex, "$2");

        tgParsedText.put("tableNumber", tableReview);
        tgParsedText.put("callWaiterComment", callWaiterComment);

        return tgParsedText;

    }




    @Step("Парсим сообщение и преобразовываем в в хешкарту в зависимости от типа сообщения")
    public LinkedHashMap<String, String>  parseMsg(HashMap <String,String> msg) {

        String textMsg = msg.get("message");

        LinkedHashMap<String, String> tgParsedText = new LinkedHashMap<>();

        switch (msg.get("msgType")) {

            case "orderPay" -> {

                System.out.println("Оплата\n");
                paymentFiller(textMsg,tgParsedText);

            }
            case "orderPayError" -> {

                System.out.println("Оплата с ошибкой\n");
                paymentErrorFiller(textMsg,tgParsedText);

            }
            case "review" -> {

                System.out.println("Отзыв\n");
                reviewFiller(textMsg,tgParsedText);

            }
            case "zeroReview" -> {

                System.out.println("Нулевой отзыв\n");
                zeroReviewFiller(textMsg,tgParsedText);

            }
            case "callWaiter" -> {

                System.out.println("Вызов официанта\n");
                callWaiterFiller(textMsg,tgParsedText);

            }

            case "menuAdd" -> {

                System.out.println("Добавить меню\n");
                tgParsedText.put("message","success");

            }

        }

        return tgParsedText;

    }


}
