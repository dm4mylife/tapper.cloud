package support_personal_account.logs_and_permissions;


import com.github.javafaker.Faker;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.Condition.*;
import static data.selectors.SupportPersonalAccount.Common.pagePreloader;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.waitersTab.*;


public class WaiterTab extends BaseActions {
    @Step("Проверка карточки официанта у техподдержки")
    public void isWaiterDetailCardCorrect() {

        isElementVisible(waiterNameInCashDesk);
        waiterNameInCashDesk.shouldBe(disabled);
        isElementVisible(waiterName);
        waiterName.shouldBe(disabled);
        isElementVisible(waiterEmail);
        waiterEmail.shouldBe(disabled);
        isElementVisible(telegramLogin);
        isElementVisible(telegramId);
        isElementVisible(saveButton);

    }

    @Step("Редактирование имени и айди телеграма")
    public HashMap<String, String> setTelegramLoginAndId() {

        Faker tgLoginFaker = new Faker();
        String tgLogin = tgLoginFaker.harryPotter().house();

        Faker tgIdFaker = new Faker();
        String tgId;

        do {

            tgId = tgIdFaker.number().digits(6);

        } while (String.valueOf(tgId).charAt(0) == '0');

        HashMap<String, String> tgData = new HashMap<>();
        tgData.put("login", tgLogin);
        tgData.put("id", tgId);

        telegramLogin.shouldBe(visible, Duration.ofSeconds(5));
        scroll(telegramLogin);
        clearText(telegramLogin);
        sendKeys(telegramLogin,tgLogin);

        clearText(telegramId);

        sendKeys(telegramId,tgId);

        saveButton.shouldNotBe(disabled);
        click(saveButton);
        pagePreloader.shouldNotHave(attributeMatching("style", "background: transparent;")
                , Duration.ofSeconds(10));

        return tgData;

    }

    @Step("Проверка что имя и айди сохранились")
    public void isChangedDataCorrect(HashMap<String, String> tgData) {

        String currentTgLogin = telegramLogin.getValue();
        String currentTgId = telegramId.getValue();

        Assertions.assertEquals(currentTgLogin, tgData.get("login"), "Логин не сохранился в админке");
        Assertions.assertEquals(currentTgId, tgData.get("id"), "Айди не сохранился в админке");

    }

}
