package support_personal_account.history_operations;

import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.SupportPersonalAccount.Common.historyOperationsCategory;
import static data.selectors.SupportPersonalAccount.HistoryOperations.*;


public class HistoryOperations extends BaseActions {

    @Step("Переход в категорию Истории операций")
    public void goToHistoryOperationsCategory() {

        click(historyOperationsCategory);
        pageHeading.shouldHave(text("История операций"), Duration.ofSeconds(5));
        operationsContainer.shouldBe(visible);

    }

    @Step("Проверка отображения всех элементов в категории Истории операций")
    public void isHistoryOperationsCategoryCorrect() {

        isElementVisible(operationsListTab);
        isElementVisible(stuckTransactionTab);
        isElementVisible(restaurantFilterButton);
        isElementVisible(tableNumberFilterButton);
        isElementVisible(orderStatusFilterButton);
        isElementVisible(waiterFilterButton);

        isElementVisible(dayPeriodButton);
        isElementVisible(weekPeriodButton);
        isElementVisible(customPeriodButton);

    }


}
