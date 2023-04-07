package support_personal_account.profile;


import common.BaseActions;
import data.selectors.SupportPersonalAccount;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.selectors.AdminPersonalAccount.Common.*;
import static data.selectors.SupportPersonalAccount.Common.profileCategory;
import static data.selectors.SupportPersonalAccount.Profile.name;
import static data.selectors.SupportPersonalAccount.Profile.*;


public class Profile extends BaseActions {


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
        isElementVisible(name);
        isElementVisible(phone);
        isElementVisible(telegramItemsLogin.first());
        isElementsListVisible(telegramItems);
        isElementVisible(email);
        email.shouldBe(disabled);
        isElementVisible(password);
        isElementVisible(passwordConfirmation);
        isElementVisible(saveButton);

    }


    @Step("Проверка что все элементы в боковом меню корректны")
    public void isOpenedLeftMenuCorrect() {

        if (mobileFooter.isDisplayed()) {

            isElementVisible(mobileFooter);

        } else {

            click(mainMenuIcon);

        }

        isElementVisible(exitFromAdmin);
        isElementVisible(profileCategory);
        isElementVisible(SupportPersonalAccount.Common.logsAndPermissionsCategory);
        isElementVisible(SupportPersonalAccount.Common.historyOperationsCategory);
        isElementVisible(SupportPersonalAccount.Common.lockCategory);
        isElementVisible(SupportPersonalAccount.Common.sendingCategory);
        isElementVisible(SupportPersonalAccount.Common.cashDeskInaccessibilityCategory);
        isElementVisible(SupportPersonalAccount.Common.analyticsCategory);

    }


}
