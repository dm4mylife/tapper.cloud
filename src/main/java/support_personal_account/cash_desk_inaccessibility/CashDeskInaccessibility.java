package support_personal_account.cash_desk_inaccessibility;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import support_personal_account.lock.Lock;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.SupportPersonalAccount.CashDeskInaccessibility.*;
import static data.selectors.SupportPersonalAccount.CashDeskInaccessibility.applyButton;
import static data.selectors.SupportPersonalAccount.Common.cashDeskInaccessibilityCategory;
import static data.selectors.SupportPersonalAccount.Common.sendingCategory;
import static data.selectors.SupportPersonalAccount.Lock.*;
import static data.selectors.SupportPersonalAccount.Sending.*;

public class CashDeskInaccessibility extends BaseActions {

    Lock lock = new Lock();
    @Step("Переход в категорию Недоступность кассы")
    public void goToCashDeskInaccessibility() {

        click(cashDeskInaccessibilityCategory);
        pageHeading.shouldHave(text("Недоступность кассы"), Duration.ofSeconds(5));
        cashDeskInaccessibilityContainer.shouldBe(visible);

    }

    @Step("Проверка отображения всех элементов в категории Недоступность кассы")
    public void isCashDeskInaccessibilityCategoryCorrect() {

        isElementVisible(cashDeskInaccessibilityInfoContainer);
        isElementVisible(choseRestaurantContainer);
        isElementVisible(amountChosenRestaurant);
        isElementVisible(choseRestaurantButton);
        isElementVisible(choseDateContainer);
        isElementVisible(downloadTableButton);

    }

    @Step("Выбираем только определенный ресторан и выбираем его по чекбоксу")
    public void choseOnlyCertainRestaurants(String restaurant) {

        lock.resetAllRestaurants();
        forceWait(500);
        click(choseRestaurantButton);

        ElementsCollection onlyTestrkeeperRestaurants =
                dropdownWhereToLockRestaurants.filter(matchText(restaurant));

        for (SelenideElement element : onlyTestrkeeperRestaurants) {

            element.$(".vPlugLst__checkbox").click();

        }

        click(applyButton);

    }

    @Step("Выбираем дату и проверяем что таблица начинает загружаться")
    public void choseDate() {

        clickByJs(choseDateButtonSelector);
        click(firstDateOfPeriod);

    }

}
