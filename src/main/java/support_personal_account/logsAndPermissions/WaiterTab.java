package support_personal_account.logsAndPermissions;


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
        String tgId = tgIdFaker.number().digits(6);

        HashMap<String, String> tgData = new HashMap<>();
        tgData.put("login", tgLogin);
        tgData.put("id", tgId);

        forceWait(2000);
        telegramLogin.shouldBe(visible, Duration.ofSeconds(5));
        clearText(telegramLogin);
        forceWait(1000);
        clearText(telegramId);
        forceWait(1000);

        System.out.println(telegramLogin.getValue() + " login");
        System.out.println(telegramId.getValue() + " id");

        scroll(telegramLogin);
        telegramLogin.sendKeys(tgLogin);
        telegramId.sendKeys(tgId);

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
        System.out.println("Логин и айди телеграма успешно изменились");

    }


}
