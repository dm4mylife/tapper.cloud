package tapper_table;

import api.ApiRKeeper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static data.Constants.RegexPattern.TelegramMessage;

public class Telegram {

    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Step("Получаем список всех сообщений об оплате")
    public String getLastTgPayMsgList(String guid) {

        List<Object> tgMessages = apiRKeeper.getUpdates();
        String lastTgMsg;
        int msgListByNeededGuidIndex = 0;

        HashMap<Integer,String> msgListByNeededGuid = new HashMap<>();

        for (Object tgMessage : tgMessages) {

            String currentMsg = tgMessage.toString();

            if (currentMsg.contains(guid) && !currentMsg.contains("Рейтинг:")) {

                msgListByNeededGuid.put(msgListByNeededGuidIndex, currentMsg);
                msgListByNeededGuidIndex++;

            }

        }

        if (msgListByNeededGuid.size() > 0) {

            lastTgMsg = msgListByNeededGuid.get(msgListByNeededGuid.size()-1);

        } else {

            return null;

        }

        Allure.addAttachment("Сообщение в телеграмме", String.valueOf(lastTgMsg));

        return lastTgMsg;

    }

    @Step("Получаем список всех сообщений об официанте")
    public String getLastTgWaiterMsgList(String guid) {

        List<Object> tgMessages = apiRKeeper.getUpdates();
        String lastTgMsg;
        int msgListByNeededGuidIndex = 0;

        HashMap<Integer,String> msgListByNeededGuid = new HashMap<>();

        for (Object tgMessage : tgMessages) {

            String currentMsg = tgMessage.toString();

            if (currentMsg.contains("Рейтинг") &&
                    currentMsg.contains(guid) &&
                    !currentMsg.contains("Рейтинг: 0") ) {

                msgListByNeededGuid.put(msgListByNeededGuidIndex, currentMsg);
                msgListByNeededGuidIndex++;

            }

        }

        if (msgListByNeededGuid.size() > 0) {

            lastTgMsg = msgListByNeededGuid.get(msgListByNeededGuid.size()-1);

        } else {

            return null;

        }

        return lastTgMsg;

    }

    @Step("Получаем список всех сообщений об вызове официанте")
    public String getLastTgCallWaiterMsgList(String tableNumber) {

        List<Object> tgMessages = apiRKeeper.getUpdates();
        String lastTgMsg;
        int msgListByNeededGuidIndex = 0;

        HashMap<Integer,String> msgListByNeededGuid = new HashMap<>();

        for (Object tgMessage : tgMessages) {

            String currentMsg = tgMessage.toString();

            if (currentMsg.contains(tableNumber)) {

                msgListByNeededGuid.put(msgListByNeededGuidIndex, currentMsg);
                msgListByNeededGuidIndex++;

            }

        }

        if (msgListByNeededGuid.size() > 0) {

            lastTgMsg = msgListByNeededGuid.get(msgListByNeededGuid.size()-1);

        } else {

            return null;

        }

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

        String table = textMsg.replaceAll(TelegramMessage.tableRegex, "$2");
        String sumInCheck = textMsg.replaceAll(TelegramMessage.sumInCheckRegex, "$2");
        String restToPay = textMsg.replaceAll(TelegramMessage.restToPayRegex, "$2");
        String tips = textMsg.replaceAll(TelegramMessage.tipsRegex, "$2");
        String paySum = textMsg.replaceAll(TelegramMessage.paySumRegex, "$2");
        String totalPaid = textMsg.replaceAll(TelegramMessage.totalPaidRegex, "$2");
        String payStatus = textMsg.replaceAll(TelegramMessage.payStatusRegex, "$2");
        String orderStatus = textMsg.replaceAll(TelegramMessage.orderStatusRegex, "$2").trim();
        String waiter = textMsg.replaceAll(TelegramMessage.waiterRegex, "$2");

        tgParsedText.put("table", table);
        tgParsedText.put("sumInCheck", sumInCheck);
        tgParsedText.put("restToPay", restToPay);
        tgParsedText.put("tips", tips);
        tgParsedText.put("paySum", paySum);
        tgParsedText.put("totalPaid", totalPaid);

        if (textMsg.contains("Скидка:")) {

            String discount = textMsg.replaceAll(TelegramMessage.discountRegex,"$2");
            tgParsedText.put("discount", discount);

        } else if (textMsg.contains("Наценка:")) {

            String markUp = textMsg.replaceAll(TelegramMessage.markUpRegex,"$2");
            tgParsedText.put("markUp", markUp);

        }

        tgParsedText.put("payStatus", payStatus);
        tgParsedText.put("orderStatus", orderStatus);
        tgParsedText.put("waiter", waiter);

        return tgParsedText;

    }

    @Step("Наполнение хешкарты если сообщение типа оплаты с ошибкой")
    public void paymentErrorFiller(String textMsg, LinkedHashMap<String, String> tgParsedText) {

        LinkedHashMap<String, String> paymentData = paymentFiller(textMsg,tgParsedText);

        String reason = textMsg.replaceAll(TelegramMessage.reasonError, "$2");

        paymentData.put("reasonError",reason);

    }

    @Step("Наполнение хешкарты если сообщение типа нулевой отзыв")
    public void zeroReviewFiller(String textMsg, LinkedHashMap<String, String> tgParsedText) {

        String restaurantName= textMsg.replaceAll(TelegramMessage.restaurantNameRegex, "$2");
        String waiter = textMsg.replaceAll(TelegramMessage.waiterRegex, "$2");
        String table = textMsg.replaceAll(TelegramMessage.tableRegex, "$2");
        String ratingComment = textMsg.replaceAll(TelegramMessage.ratingCommentRegex, "$2");
        String rating = textMsg.replaceAll(TelegramMessage.ratingRegex, "$2");

        tgParsedText.put("restaurantName", restaurantName);
        tgParsedText.put("table", table);
        tgParsedText.put("waiter", waiter);
        tgParsedText.put("ratingComment", ratingComment);
        tgParsedText.put("rating", rating);

    }

    @Step("Наполнение хешкарты если сообщение типа отзыв")
    public void reviewFiller(String textMsg, LinkedHashMap<String, String> tgParsedText) {

        String waiter = textMsg.replaceAll(TelegramMessage.waiterRegex, "$2");
        String table = textMsg.replaceAll(TelegramMessage.tableRegex, "$2");
        String ratingComment = textMsg.replaceAll(TelegramMessage.ratingCommentRegex, "$2");
        String rating = textMsg.replaceAll(TelegramMessage.ratingRegex, "$2");
        String suggestions = textMsg.replaceAll(TelegramMessage.suggestionRegex, "$2");

        tgParsedText.put("tableNumber", table);
        tgParsedText.put("waiter", waiter);
        tgParsedText.put("comment", ratingComment.trim());
        tgParsedText.put("rating", rating.trim());
        tgParsedText.put("suggestions", suggestions.replaceAll("\n"," "));

    }

    @Step("Наполнение хешкарты если сообщение типа вызов официанта")
    public void callWaiterFiller(String textMsg, LinkedHashMap<String, String> tgParsedText) {

        String waiter = textMsg.replaceAll(TelegramMessage.waiterRegex, "$2");
        String tableReview = textMsg.replaceAll(TelegramMessage.tableReviewRegex, "$2");
        String callWaiterComment = textMsg.replaceAll(TelegramMessage.callWaiterCommentRegex, "$2");

        tgParsedText.put("waiter", waiter);
        tgParsedText.put("tableNumber", tableReview);
        tgParsedText.put("callWaiterComment", callWaiterComment);

    }

    @Step("Парсим сообщение и преобразовываем в в хешкарту в зависимости от типа сообщения")
    public LinkedHashMap<String, String>  parseMsg(HashMap <String,String> msg) {

        String textMsg = msg.get("message");

        LinkedHashMap<String, String> tgParsedText = new LinkedHashMap<>();

        switch (msg.get("msgType")) {

            case "orderPay" -> {

                paymentFiller(textMsg,tgParsedText);

            }
            case "orderPayError" -> {

                paymentErrorFiller(textMsg,tgParsedText);

            }
            case "review" -> {

                reviewFiller(textMsg,tgParsedText);

            }
            case "zeroReview" -> {

                zeroReviewFiller(textMsg,tgParsedText);

            }
            case "callWaiter" -> {

                callWaiterFiller(textMsg,tgParsedText);

            }

            case "menuAdd" -> {

                tgParsedText.put("message","success");

            }

        }

        return tgParsedText;

    }

}
