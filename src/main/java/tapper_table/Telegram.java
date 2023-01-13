package tapper_table;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class Telegram {


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


}
