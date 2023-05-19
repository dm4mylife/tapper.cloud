package support_personal_account.lock;

import com.codeborne.selenide.ElementsCollection;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.SupportPersonalAccount.Common.lockCategory;
import static data.selectors.SupportPersonalAccount.Lock.*;

public class Lock extends BaseActions {
    @Step("Переход в раздел Заглушка")
    public void goToLockCategory() {

        click(lockCategory);
        pageHeading.shouldHave(text("Заглушка"), Duration.ofSeconds(5));
        lockContainer.shouldBe(visible);

    }

    @Step("Проверка отображения раздела Заглушка")
    public void isLockCorrect() {

        isElementVisible(lockInfoContainer);
        isElementVisible(whereToLockContainer);
        isElementVisible(howToLockContainer);
        isElementVisible(saveButton);

    }

    @Step("Проверяем отображение элементов в открытой форме Где заглушить")
    public void isWhereToLockCorrect() {

        isElementVisible(dropdownWhereToLockTitle);
        isElementVisible(dropdownWhereToLockSearchRestaurant);
        isElementsListVisible(dropdownWhereToLockFilterOptions);
        dropdownWhereToLockFilterOptions.shouldHave(size(3));
        isElementsListVisible(dropdownWhereToLockRestaurants);
        isElementVisible(applyButton);
        isElementVisible(resetAllButton);

    }

    @Step("Активировать все рестораны что не заглушены")
    public void activateAllRestaurants() {

        click(whereToLockButton);

        int chosenRestaurants = dropdownWhereToLockRestaurants
                .filter(attributeMatching("class", ".*active")).size();

        int restaurantsSize = dropdownWhereToLockRestaurants.size();

        if (chosenRestaurants == restaurantsSize) {

            click(lockInfoContainer);

        } else {

            click(dropdownWhereToLockFilterOptions.first());

            dropdownWhereToLockRestaurants.asFixedIterable().stream().forEach
                    (element -> element.shouldHave(attributeMatching("class", ".*active")));

            click(applyButton);

            int restaurantsToLock = convertSelectorTextIntoIntByRgx(amountRestaurantsToLock, "\\D+");

            isElementInvisible(dropdownWhereToLockTitle);

            Assertions.assertEquals(restaurantsToLock, restaurantsSize, "Не все рестораны были отменены");

        }

    }

    @Step("Сбросить все рестораны что заглушены")
    public void resetAllRestaurants() {

        int restaurantsToLock = convertSelectorTextIntoIntByRgx(amountRestaurantsToLock, "\\D+");

        if (restaurantsToLock != 0) {

            click(whereToLockButton);

            click(resetAllButton);

            click(applyButton);

            isElementInvisible(dropdownWhereToLockTitle);

            saveChanges();

            restaurantsToLock = convertSelectorTextIntoIntByRgx(amountRestaurantsToLock, "\\D+");

            Assertions.assertEquals(restaurantsToLock, 0, "Не все рестораны были отменены");

        }

    }

    @Step("Выбираем все рестораны чтобы заглушить")
    public void choseAllRestaurantToLockOption() {

        click(whereToLockButton);

        isWhereToLockCorrect();

        activateAllRestaurants();

    }

    @Step("Выбираем только b2p")
    public void choseOnlyBest2Pay() {

        resetAllRestaurants();

        click(whereToLockButton);
        isWhereToLockCorrect();

        click(b2pLockOption);

        click(applyButton);

        isElementInvisible(dropdownWhereToLockTitle);

        saveChanges();

    }

    public void saveChanges() {

        click(saveButton);
        lockPreloader.shouldHave(cssValue("display","flex"));
        saveButton.shouldBe(disabled);

    }

    @Step("Проверяем отображение элементов в открытой форме Как заглушить")
    public void isHowToLockCorrect() {

        isElementsListVisible(dropdownHowToLockRestaurants);
        isElementVisible(dropdownHowToLockRestaurantsWholeServiceButton);
        isElementVisible(dropdownHowToLockRestaurantsPaymentOnlyButton);

    }

    @Step("Выбираем заглушить сервис полностью")
    public void choseWholeServiceToLockOption() {

        click(howToLockButton);

        isHowToLockCorrect();

        click(dropdownHowToLockRestaurantsWholeServiceButton);

        chosenTypeOfLock.shouldHave(text("Сервис полностью"));

        saveChanges();

        chosenTypeOfLock.shouldHave(text("Сервис полностью"));

    }

    @Step("Выбираем заглушить только оплату")
    public void choseOnlyPaymentToLockOption() {

        click(howToLockButton);

        isHowToLockCorrect();

        click(dropdownHowToLockRestaurantsPaymentOnlyButton);

        chosenTypeOfLock.shouldHave(text("Только оплату"));

        saveChanges();

        chosenTypeOfLock.shouldHave(text("Только оплату"));

    }

    @Step("Выбираем только определенный ресторан и выбираем его по чекбоксу")
    public void choseOnlyCertainRestaurants(String restaurant) {

        resetAllRestaurants();

        click(whereToLockButton);

        ElementsCollection onlyTestrkeeperRestaurants =
           dropdownWhereToLockRestaurants.filter(matchText(restaurant));

        onlyTestrkeeperRestaurants.asFixedIterable().stream().forEach
            (element -> element.$(".vPlugLst__checkbox").click());

        click(applyButton);

        saveChanges();

    }

    @Step("Выбираем только определенный ресторан и выбираем его по поиску")
    public void choseOnlyCertainRestaurantsBySearch(String restaurant) {

        resetAllRestaurants();

        click(whereToLockButton);

        sendHumanKeys(dropdownWhereToLockSearchRestaurant,restaurant);

        ElementsCollection onlyTestrkeeperRestaurants =
                dropdownWhereToLockRestaurants.filter(matchText(restaurant));

        onlyTestrkeeperRestaurants.asFixedIterable().stream().forEach
                (element -> element.$(".vPlugLst__checkbox").click());

        click(applyButton);

        saveChanges();

    }


}
