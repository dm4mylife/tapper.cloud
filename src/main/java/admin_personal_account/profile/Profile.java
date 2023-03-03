package admin_personal_account.profile;

import admin_personal_account.AdminAccount;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
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
        isElementsListVisible(telegramItemsIcons);
        telegramItemsHelpTooltip.filter(exist).shouldBe(sizeGreaterThan(0));
        isElementsListVisible(telegramItemsHelp);
        isElementsListVisible(telegramItemsCloseIcon);
        isElementVisible(privateDataContainer);
        isElementVisible(profileContainer);

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

    @Step("Удаление телеграм логина")
    public void deleteTelegramLogin() {

        telegramItems.asDynamicIterable().stream().forEach(element ->  {

            if (Objects.equals(element.$(telegramItemsLoginInputSelector).getAttribute("value"), TELEGRAM_AUTO_LOGIN)) {

                click(element.$(telegramItemsCloseIconSelector));

                click(addTelegramLoginButton);

                sendKeys(telegramItemsLogin.last(),TELEGRAM_AUTO_LOGIN);

                click(saveButton);

                isElementVisible(pagePreloader);

                changedDataNotification
                        .shouldHave(attributeMatching("class",".*active.*"));
                telegramItemsLogin.last().should(value(TELEGRAM_AUTO_LOGIN));
                System.out.println("Удаление происходит корректно");

            }

        });

    }

    @Step("Смена пароля админской учетной записи")
    public void changeAdminPassword() {

        click(adminPassword);
        sendKeys(adminPassword,ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST);

        click(adminPasswordConfirmation);
        sendKeys(adminPasswordConfirmation,ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST);

        click(saveButton);
        isElementVisible(pagePreloader);

        changedDataNotification
                .shouldHave(attributeMatching("class",".*active.*"));
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

        changedDataNotification
                .shouldHave(attributeMatching("class",".*active.*"));
        adminPassword.shouldHave(value(ADMIN_RESTAURANT_PASSWORD));
        adminPasswordConfirmation.shouldHave(value(ADMIN_RESTAURANT_PASSWORD));

        adminAccount.logOut();

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        isProfileCategoryCorrect();
        System.out.println("Смена админского пароля корректно");

    }

}
