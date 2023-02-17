package admin_personal_account.profile;

import admin_personal_account.AdminAccount;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.AdminPersonalAccount;
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
        profileContainer.shouldBe(visible);
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
        telegramItemsHelpTooltip.filter(exist).shouldBe(CollectionCondition.sizeGreaterThan(0));
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
        element.shouldHave(empty);
        element.sendKeys(value);
        click(saveButton);
        pagePreloader.shouldBe(visible);
        changedDataNotification
                .shouldHave(attributeMatching("class",".*active.*"));
        element.shouldHave(value(value));


        clearText(element);
        element.sendKeys(previousValue);
        click(saveButton);
        element.should(value(previousValue));
        pagePreloader.shouldBe(visible);
        changedDataNotification
                .shouldHave(attributeMatching("class",".*active.*"));
        element.shouldHave(value(previousValue));

    }

    @Step("Удаление телеграм логина")
    public void deleteTelegramLogin() {

        int index = 0;

        int previousSize = telegramItemsLogin.size();

        for (; index < telegramItemsLogin.size(); index++) {

            if (Objects.equals(telegramItemsLogin.get(index).getAttribute("value"), "user_unknown_nb")) {

                break;

            }

        }

        String telegramLoginValue = telegramItemsLogin.get(index).getValue();

        telegramItemsCloseIcon.get(index-1).click();
        addTelegramLoginButton.shouldBe(visible);
        telegramItemsLogin.shouldHave(CollectionCondition.size(previousSize - 1));

        for (; index < telegramItemsLogin.size(); index++) {

            if (!Objects.equals(telegramItemsLogin.get(index).getAttribute("value"), telegramLoginValue)) {

                System.out.println("Good");

            } else {

                Assertions.fail("Не удалился ранее удаленный логин");

            }

        }

        click(addTelegramLoginButton);
        telegramItemsLogin.shouldHave(CollectionCondition.size(previousSize));
        telegramItemsLogin.last().click();
        telegramItemsLogin.last().sendKeys(telegramLoginValue);
        click(saveButton);

        pagePreloader.shouldBe(visible);
        changedDataNotification
                .shouldHave(attributeMatching("class",".*active.*"));
        telegramItemsLogin.last().should(value(telegramLoginValue));
        System.out.println("Удаление происходит корректно");

    }

    @Step("Смена пароля админской учетной записи")
    public void changeAdminPassword() {

        adminPassword.click();
        adminPassword.sendKeys(AdminPersonalAccount.ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST);
        adminPasswordConfirmation.click();
        adminPasswordConfirmation.sendKeys(AdminPersonalAccount.ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST);
        click(saveButton);
        pagePreloader.shouldBe(visible);
        changedDataNotification
                .shouldHave(attributeMatching("class",".*active.*"));
        adminPassword.shouldHave(value(AdminPersonalAccount.ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST));
        adminPasswordConfirmation.shouldHave(value(AdminPersonalAccount.ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST));

        adminAccount.logOut();

        authorizationPage.authorizationUser(AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL, AdminPersonalAccount.ADMIN_RESTAURANT_NEW_PASSWORD_FOR_TEST);

        adminPassword.shouldHave(value(""));
        adminPasswordConfirmation.shouldHave(value(""));

        adminPassword.click();
        adminPassword.sendKeys(AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD);
        adminPasswordConfirmation.click();
        adminPasswordConfirmation.sendKeys(AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD);
        click(saveButton);
        pagePreloader.shouldBe(visible);
        changedDataNotification
                .shouldHave(attributeMatching("class",".*active.*"));
        adminPassword.shouldHave(value(AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD));
        adminPasswordConfirmation.shouldHave(value(AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD));

        adminAccount.logOut();

        authorizationPage.authorizationUser(AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL, AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD);
        isProfileCategoryCorrect();
        System.out.println("Смена админского пароля корректно");

    }

}
