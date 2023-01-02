package tapper_table;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class Telegram {

    @Test
    public void tgbot() {

        TelegramBot bot = new TelegramBot("5989489181:AAGsWoVW-noi9lDDx11H-nGPNPOuw8XtCZI");

        bot.setUpdatesListener(updates -> {
            // ... process updates
            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });



        SendResponse response = bot.execute(new SendMessage("892990106", "Hello Mr Freeman!"));

    }


    @Test
    public void getUpdates() {

        Response response = given()
                .contentType(ContentType.JSON)
                .and()
                .when()
                .get("https://api.telegram.org/bot5989489181:AAGsWoVW-noi9lDDx11H-nGPNPOuw8XtCZI/getUpdates")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.getTimeIn(TimeUnit.SECONDS) + "sec response time");
        System.out.println(response.jsonPath().getList("result.channel_post.text"));



    }


}
