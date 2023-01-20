package tapper_waiter_personal_account;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import common.BaseActions;
import constants.selectors.TapperTableSelectors;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import tapper_admin_personal_account.AdminAccount;
import tapper_admin_personal_account.AuthorizationPage;

import java.io.File;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static constants.Constant.TestData.ROBOCOP_IMG_PATH;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_111;
import static constants.Constant.TestDataRKeeperAdmin.*;
import static constants.selectors.AdminPersonalAccountSelectors.Profile.*;
import static constants.selectors.TapperTableSelectors.RootPage.TipsAndCheck.waiterImage;
import static constants.selectors.TapperTableSelectors.RootPage.TipsAndCheck.waiterImageNotSelenide;
import static constants.selectors.WaiterPersonalAccountSelectors.saveButton;
import static constants.selectors.WaiterPersonalAccountSelectors.*;


public class Waiter extends BaseActions {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    AdminAccount adminAccount = new AdminAccount();

    @Step("Проверка всех элементов на странице профиля официанта")
    public void isWaiterProfileCorrect() {

        pageTitle.shouldHave(text(" Настройки профиля "));
        isElementVisible(imageContainer);
        isElementVisible(restaurantName);
        restaurantName.shouldBe(disabled);
        isElementVisible(waiterNameInCashDesk);
        waiterNameInCashDesk.shouldBe(disabled);
        isElementVisible(waiterName);
        isElementVisible(privateDataContainer);
        isElementVisible(learnMoreLink);
        isLearMoreCorrect();
        isElementVisible(telegramLogin);
        isElementVisible(waiterEmail);
        waiterEmail.shouldBe(disabled);
        isElementVisible(personalInformationContainer);
        isElementVisible(waiterPassword);
        waiterPassword.shouldHave(empty);
        isElementVisible(waiterPasswordConfirmation);
        waiterPasswordConfirmation.shouldHave(empty);
        isElementVisible(linkWaiterCard);
        isElementVisible(saveButton);

    }

    @Step("Проверяем ссылку 'узнать подробнее'")
    public void isLearMoreCorrect() {

        click(learnMoreLink);
        telegramInstruction
                .shouldHave(cssValue("display", "flex"));

        click(telegramInstructionCloseButton);

        telegramInstruction
                .shouldHave(cssValue("display", "none"));

    }

    @Step("Загрузка изображения в аватарку официанта")
    public void downloadWaiterImage() {

        if (imageContainerDownloadedImage.$("img").exists()) {

            deleteWaiterImage();

        }

        File imageFile = new File(ROBOCOP_IMG_PATH);
        imageContainerDownloadImageButton.uploadFile(imageFile);

        isElementVisible(imageDeleteButton);
        pagePreloader.shouldBe(hidden, Duration.ofSeconds(20));
        isElementVisible(imageContainerDownloadedImage.$("img"));

        isImageCorrect(imageContainerDownloadedImageNotSelenide,
                "Загруженное изображение в контейнер не корректное или битое");

    }

    @Step("Проверка добавленной фотографии на столе")
    public void checkDownloadedWaiterImageOnTable() {

        isElementVisible(waiterImage);
        isImageCorrect(waiterImageNotSelenide,"Изображение официанта не корректное или битое");
        System.out.println("Изображение добавилось на столе");

    }

    @Step("Проверка изображения официанта на столе")
    public void deleteWaiterImage() {

        click(imageDeleteButton);
        pagePreloader.shouldBe(hidden, Duration.ofSeconds(20));
        isElementInvisible(imageContainerDownloadedImage.$("img"));
        System.out.println("Удалили фотографию");

    }

    @Step("Смена имени официанта")
    public void changeWaiterName() {

        waiterName.click();
        waiterName.sendKeys(ROBOCOP_WAITER_CHANGED_NAME);
        click(saveButton);
        pagePreloader.shouldBe(visible);
        changedDataNotification
                .shouldHave(attributeMatching("class", ".*active.*"));
        waiterName.shouldHave(value(ROBOCOP_WAITER_CHANGED_NAME));

    }

    @Step("Проверка смены имени официанта на столе")
    public void checkChangedNameOnTable() {

        openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_111);

        TapperTableSelectors.RootPage.TipsAndCheck.
                waiterName.shouldHave(text(ROBOCOP_WAITER_CHANGED_NAME));
        System.out.println("Имя официанта на столе сменилось на " + ROBOCOP_WAITER_CHANGED_NAME);

        Selenide.closeWindow();
        Selenide.switchTo().window(0);

    }

    @Step("Проверка изменения телеграмм логина")
    public void setNameToDefault() {

        waiterName.click();
        waiterName.sendKeys(Keys.CONTROL + "A");
        waiterName.sendKeys(Keys.BACK_SPACE);
        click(saveButton);
        pagePreloader.shouldBe(visible);
        changedDataNotification
                .shouldHave(attributeMatching("class", ".*active.*"));
        waiterName.shouldHave(value(""));
        System.out.println("Имя сменилось прежнее");

    }

    @Step("Проверка изменения телеграмм логина")
    public void changeTelegramLogin() {

        Faker faker = new Faker();
        String newWaiterTelegramLogin = faker.gameOfThrones().character();

        telegramLogin.click();
        telegramLogin.sendKeys(Keys.CONTROL + "A");
        telegramLogin.sendKeys(Keys.BACK_SPACE);
        telegramLogin.sendKeys(newWaiterTelegramLogin);
        click(saveButton);
        pagePreloader.shouldBe(hidden, Duration.ofSeconds(5));
        System.out.println("Логин телеграмма " + newWaiterTelegramLogin);
        telegramLogin.shouldHave(value(newWaiterTelegramLogin));

        telegramLogin.click();
        telegramLogin.sendKeys(Keys.CONTROL + "A");
        telegramLogin.sendKeys(Keys.BACK_SPACE);
        telegramLogin.sendKeys(newWaiterTelegramLogin);
        click(saveButton);
        System.out.println("Сменили имя в телеграмме на " + newWaiterTelegramLogin);

    }

    @Step("Смена пароля учетной записи официанта")
    public void changeWaiterPassword() {

        System.out.println("Новый пароль " + WAITER_NEW_PASSWORD_FOR_TEST);

        waiterPassword.click();
        waiterPassword.sendKeys(WAITER_NEW_PASSWORD_FOR_TEST);
        waiterPasswordConfirmation.click();
        waiterPasswordConfirmation.sendKeys(WAITER_NEW_PASSWORD_FOR_TEST);
        click(saveButton);
        pagePreloader.shouldBe(hidden, Duration.ofSeconds(5));
        waiterPassword.shouldHave(value(WAITER_NEW_PASSWORD_FOR_TEST));
        waiterPasswordConfirmation.shouldHave(value(WAITER_NEW_PASSWORD_FOR_TEST));

        adminAccount.logOut();

        authorizationPage.authorizationUser(WAITER_LOGIN_EMAIL, WAITER_NEW_PASSWORD_FOR_TEST);

        waiterPassword.shouldHave(value(""));
        waiterPasswordConfirmation.shouldHave(value(""));
        pageTitle.shouldHave(text(" Настройки профиля "));
        System.out.println("Смена пароля корректная");

        waiterPassword.click();
        waiterPassword.sendKeys(WAITER_PASSWORD);
        waiterPasswordConfirmation.click();
        waiterPasswordConfirmation.sendKeys(WAITER_PASSWORD);
        click(saveButton);
        pagePreloader.shouldBe(visible);
        waiterPassword.shouldHave(value(WAITER_PASSWORD));
        waiterPasswordConfirmation.shouldHave(value(WAITER_PASSWORD));
        System.out.println("Вернули старый пароль + " + WAITER_PASSWORD);

    }

    @Step("Привязка карта официанта")
    public void linkWaiterCard() {

        click(linkWaiterCard);
        isElementVisible(b2pContainer);
        b2pCardNumber.sendKeys("4809388886227309");
        b2pCardExpiredDate.sendKeys("1224");
        b2pCardCvc.sendKeys("123");
        click(b2pSaveButton);

        pageTitle.shouldHave(text(" Настройки профиля "),Duration.ofSeconds(20));

        buttonWithCard.shouldHave(disabled, matchText("Карта привязана"));
        isElementVisible(changedCardButton);
        //toDo нет второй тестовой карты чтобы написать тест на редактирование текущей

    }

}
