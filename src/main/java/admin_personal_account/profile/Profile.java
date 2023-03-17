package admin_personal_account.profile;

import admin_personal_account.AdminAccount;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static data.Constants.TestData.AdminPersonalAccount;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.Common.profileCategory;
import static data.selectors.AdminPersonalAccount.Profile.*;


public class Profile extends BaseActions {

    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();

    @Step("Переход в меню профиля")
    public void goToProfileCategory() {

        click(profileCategory);
        pageHeading.shouldHave(text("Профиль"), Duration.ofSeconds(5));
        isElementVisible(profileContainer);
        isProfileCategoryCorrect();

    }

    @Step("Проверка что все элементы в профиле корректны")
    public void isProfileCategoryCorrect() {

        isElementVisible(profileTitle);
        isElementVisible(restaurantName);
        isElementVisible(adminName);
        isElementVisible(adminPhone);
        isElementsListVisible(telegramItems);
        isElementVisible(adminEmail);
        adminEmail.shouldBe(disabled);
        isElementVisible(adminPassword);
        isElementVisible(adminPasswordConfirmation);
        isElementVisible(saveButton);
        isElementsListVisible(telegramItemsMasterIcon);
        isElementsListVisible(telegramItemsIcons);
        telegramItemsHelpTooltip.filter(exist).shouldBe(sizeGreaterThan(0));
        isElementsListVisible(telegramItemsHelp);
        isElementsListVisible(telegramItemsCloseIcon);
        isElementVisible(privateDataContainer);
        isElementVisible(profileContainer);
        isElementVisible(masterInformationContainer);

    }

    @Step("Проверка смены имени ресторана")
    public void isPrivateDateChangedCorrect() {

        changeFieldValue(restaurantName, AdminPersonalAccount.ROBOCOP_WAITER);
        changeFieldValue(adminName, AdminPersonalAccount.OPTIMUS_PRIME_WAITER);
        changeFieldValue(adminPhone, AdminPersonalAccount.ADMIN_TEST_PHONE);

    }

    @Step("Смена значения в поле")
    public void changeFieldValue(SelenideElement element, String value) {

        String previousValue = element.getValue();

        clearText(element);

        element.sendKeys(value);
        click(saveButton);
        isElementVisible(pagePreloader);

        changedDataNotification
                .shouldHave(attributeMatching("class",".*active.*"));
        element.shouldHave(value(value));

        clearText(element);

        element.sendKeys(previousValue);
        click(saveButton);

        assert previousValue != null;
        element.should(value(previousValue));
        isElementVisible(pagePreloader);
        changedDataNotification
                .shouldHave(attributeMatching("class",".*active.*"));
        element.shouldHave(value(previousValue));

    }
    @Step("Удаление телеграма")
    public void deleteTelegramLogin() {

        telegramItems.asDynamicIterable().stream().forEach(element -> {

            if (Objects.equals
                    (element.$(telegramItemsLoginInputSelector).getAttribute("value"), TELEGRAM_AUTO_LOGIN))
                click(element.$(telegramItemsCloseIconSelector));

        });

        clickSaveButton("");

        telegramItems.asDynamicIterable().stream().forEach
                (element -> element.shouldNotHave(value(TELEGRAM_AUTO_LOGIN)));

    }

    @Step("Добавление телеграма")
    public void addTelegramLogin() {

        click(addTelegramLoginButton);

        sendKeys(telegramItemsLogin.last(),TELEGRAM_AUTO_LOGIN);

        clickSaveButton(TELEGRAM_AUTO_LOGIN);

    }

    public void clickSaveButton(String login) {

        click(saveButton);

        isElementVisible(pagePreloader);

        changedDataNotification
                .shouldHave(attributeMatching("class",".*active.*"));
        telegramItemsLogin.last().should(value(login));

    }

    @Step("Добавляем управляющего")
    public void addMaster(String telegramLogin) {

        telegramItems.asDynamicIterable().stream().forEach(element -> {

            if (Objects.equals
                    (element.$(telegramItemsLoginInputSelector).getAttribute("value"), telegramLogin)) {

                SelenideElement masterIcon = $(element.$(telegramItemsMasterIconSelector));
                SelenideElement masterSvg = $(element.$(telegramItemsMasterSvgSelector));

                click(masterIcon);
                masterSvg.shouldHave(attributeMatching("class",".*active.*"));

            }

        });

        clickSaveButton(TELEGRAM_AUTO_LOGIN);

        telegramItemsMasterSvg.last().shouldHave(attributeMatching("class",".*active.*"));

    }

    @Step("Удаляем управляющего")
    public void deleteMaster(String telegramLogin) {

        telegramItems.asDynamicIterable().stream().forEach(element -> {

            if (Objects.equals
                    (element.$(telegramItemsLoginInputSelector).getAttribute("value"), telegramLogin)) {

                SelenideElement masterIcon = $(element.$(telegramItemsMasterIconSelector));
                SelenideElement masterSvg = $(element.$(telegramItemsMasterSvgSelector));

                click(masterIcon);
                masterSvg.shouldNotHave(attributeMatching("class",".*active.*"));

            }

        });

        clickSaveButton(TELEGRAM_AUTO_LOGIN);

        telegramItemsMasterSvg.last().shouldNotHave(attributeMatching("class",".*active.*"));

    }


    @Step("Устанавливаем телеграм логин управляющим")
    public void isMasterCorrect(String telegramLogin) {

        addMaster(telegramLogin);
        deleteMaster(telegramLogin);

    }



    @Step("Смена пароля админской учетной записи")
    public void changeAdminPassword() {

        click(adminPassword);
        sendKeys(adminPassword,ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST);

        click(adminPasswordConfirmation);
        sendKeys(adminPasswordConfirmation,ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST);

        click(saveButton);
        isElementVisible(pagePreloader);


        adminPassword.shouldHave(value(ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST));
        adminPasswordConfirmation.shouldHave(value(ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST));

        adminAccount.logOut();

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST);

        adminPassword.shouldHave(value(""));
        adminPasswordConfirmation.shouldHave(value(""));

        click(adminPassword);
        sendKeys(adminPassword,ADMIN_RESTAURANT_PASSWORD);

        click(adminPasswordConfirmation);
        sendKeys(adminPasswordConfirmation,ADMIN_RESTAURANT_PASSWORD);

        click(saveButton);
        isElementVisible(pagePreloader);

        adminPassword.shouldHave(value(ADMIN_RESTAURANT_PASSWORD));
        adminPasswordConfirmation.shouldHave(value(ADMIN_RESTAURANT_PASSWORD));

        adminAccount.logOut();

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        isProfileCategoryCorrect();

    }

}
