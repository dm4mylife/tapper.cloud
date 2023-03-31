package admin_personal_account.integrations;


import com.codeborne.selenide.CollectionCondition;
import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static data.selectors.AdminPersonalAccount.Common.integrationCategory;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.Integrations.*;
import static data.selectors.AdminPersonalAccount.Profile.pagePreloader;

public class Integrations extends BaseActions {

    @Step("Переход в категорию интеграций")
    public void goToIntegrationsCategory() {

        click(integrationCategory);
        pageHeading.shouldHave(text("Интеграции"), Duration.ofSeconds(5));
        integrationContainer.shouldBe(visible, Duration.ofSeconds(5));

    }

    @Step("Проверка элементов страницы")
    public void isIntegrationsCategoryCorrect() {

        isElementsListVisible(integrationItems);
        isElementsListVisible(integrationItemsBtn);
        isElementsListVisible(integrationItemsImg);

        integrationItems.shouldHave(size(3));
        integrationItemsBtn.shouldHave(size(3));
        integrationItemsImg.shouldHave(size(2));

    }

}
