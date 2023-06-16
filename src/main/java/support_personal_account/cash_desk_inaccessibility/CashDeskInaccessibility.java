package support_personal_account.cash_desk_inaccessibility;

import com.codeborne.selenide.ElementsCollection;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import support_personal_account.lock.Lock;

import java.io.FileNotFoundException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TIMEOUT_FOR_FILE_TO_BE_DOWNLOADED;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.SupportPersonalAccount.CashDeskInaccessibility.*;
import static data.selectors.SupportPersonalAccount.Common.cashDeskInaccessibilityCategory;
import static data.selectors.SupportPersonalAccount.Lock.dropdownListItemCheckboxSelector;
import static data.selectors.SupportPersonalAccount.Lock.dropdownWhereToLockRestaurants;

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

        choseRestaurantButton.shouldBe(visible,enabled).click();

        isElementsCollectionVisible(dropdownWhereToLockRestaurants);

        ElementsCollection onlyTestrkeeperRestaurants =
                dropdownWhereToLockRestaurants.filter(matchText(restaurant));

        onlyTestrkeeperRestaurants.asFixedIterable().stream().forEach(
                element -> click(element.$(dropdownListItemCheckboxSelector)));

        click(applyButton);

    }

    @Step("Выбираем дату и проверяем что таблица начинает загружаться")
    public void choseDate() {

        clickByJs(choseDateButtonSelector);
        click(firstDateOfPeriod);

    }

    @Step("Загружаем таблицу")
    public void downloadFile() throws FileNotFoundException {

        Assertions.assertNotNull(downloadTableButton.download(TIMEOUT_FOR_FILE_TO_BE_DOWNLOADED),
                "Файл не может быть скачен");

    }

}
