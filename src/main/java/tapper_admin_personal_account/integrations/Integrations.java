package tapper_admin_personal_account.integrations;


import com.codeborne.selenide.CollectionCondition;
import common.BaseActions;
import io.qameta.allure.Step;
import tapper_table.RootPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static constants.selectors.AdminPersonalAccountSelectors.Common.integrationCategory;
import static constants.selectors.AdminPersonalAccountSelectors.Common.pageHeading;
import static constants.selectors.AdminPersonalAccountSelectors.Integrations.*;

public class Integrations extends BaseActions {

    RootPage rootPage = new RootPage();

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

        integrationItems.shouldHave(CollectionCondition.size(3));
        integrationItemsBtn.shouldHave(CollectionCondition.size(3));
        integrationItemsImg.shouldHave(CollectionCondition.size(2));

    }

}
