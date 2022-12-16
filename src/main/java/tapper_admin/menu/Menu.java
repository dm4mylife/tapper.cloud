package tapper_admin.menu;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static constants.TapperAdminSelectors.RKeeperAdmin.Menu.*;
import static constants.TapperAdminSelectors.RKeeperAdmin.menuCategory;
import static constants.TapperAdminSelectors.RKeeperAdmin.pageHeading;
import static constants.TapperTableSelectors.RootPage.Menu.menuCategoryInHeader;

public class Menu extends BaseActions {

    @Step("Переход в категорию меню")
    public void goToMenuCategory() {

        click(menuCategory);
        pageHeading.shouldHave(text("Меню"), Duration.ofSeconds(5));
        menuContainer.shouldBe(visible);

    }

    @Step("Проверка что все элементы в меню корректны и отображаются")
    public void isMenuCorrect() {

        isElementVisible(refreshMenuButton);
        isElementVisible(categoryTitle);
        isElementVisible(enableMenuForVisitorsContainer);
        isElementVisible(categoryNameContainer);
        isElementVisible(viewSwitcherContainer);
        isElementVisible(menuTableHeader);
        isElementVisible(helpAdminContainer);
        isElementVisible(showActivePositionsForGuestContainer);
        isElementVisible(categoryDishContainer);
        isElementsListVisible(categoryDishItems);

    }

    @Step("Делаем видимым хотя бы одну категорию меню если они скрыты")
    public void activateMenuCategory() {

        System.out.println(categoryDishEyeIcons.size() + " количество категорий");

        ElementsCollection gg = categoryDishEyeIcons.filter(attributeMatching("class", ".*active.*"));

        System.out.println(gg.size() + " количество категорий активных");

        if (gg.size() == 0) {

            categoryDishEyeIcons.get(0).click();
            System.out.println("Активных не было, активируем один");
            isElementVisible(menuPagePreLoader);

        }

    }

    @Step("Скрываем все категории меню")
    public void deactivateMenuCategory() {

        ElementsCollection activeDishes = categoryDishEyeIcons
                .filter(Condition.attributeMatching("class", ".*active.*"));

        for (SelenideElement element : activeDishes) {

            System.out.println("click");
            element.click();
            menuPagePreLoader.shouldBe(visible);
            element.shouldNotHave(Condition.attributeMatching("class", ".*active.*"));

        }

    }

    @Step("Включаем по кнопке отображение меню для посетителей если вдруг оно выключено")
    public void activateShowGuestSliderIfDeactivated() {

        if (!enableMenuForVisitorsContainer.$("input").isSelected()) {

            enableMenuForVisitorsButton.click();
            enableMenuForVisitorsContainer.$("input").shouldBe(Condition.checked);

        }

    }

    @Step("Включить меню для посетителей. Если есть неактивные категории или само отображение выключено, включаем")
    public void activateMenuForGuests() {

        activateMenuCategory();
        activateShowGuestSliderIfDeactivated();

    }

    @Step("Проверка все элементы в форме редактирования корректны")
    public void isEditContainerCorrect() {

        isElementVisible(editCategoryContainer);
        isElementVisible(categoryNameInCashDesk);
        isElementVisible(categoryNameForGuest);
        isElementVisible(saveEditedCategoryNameButton);
        isElementVisible(cancelEditedCategoryNameButton);

    }

    @Step("Активируем первую категорию,первую позицию и включаем отображение для гостей")
    public void activateFirstCategoryAndActivateShowGuestMenu() {

        System.out.println(categoryDishEyeIcons.get(0).getCssValue("class") + " class");

        if (!categoryDishEyeIcons.get(0).getCssValue("class").equals(".vAdminMenuCategoryItem__btn.active")) {

            System.out.println("Сделали категорию видимой");
            categoryDishEyeIcons.get(0).click();

        }

        categoryDishItems.get(0).click();

        System.out.println(menuDishItemsEyeIcons.get(0).getCssValue("class") + " class");

        if (!menuDishItemsEyeIcons.get(0).getCssValue("class").equals(".vAdminMenuBtn.active")) {

            System.out.println("Сделали блюдо видимым");
            menuDishItemsEyeIcons.get(0).click();

        }

        if (!enableMenuForVisitorsInput.isSelected()) {

            enableMenuForVisitorsButton.click();

        }

    }

    @Step("Проверка что позиция редактируется и изменения сохраняются")
    public String isCategoryEditNameCorrect() {

        Faker faker = new Faker();

        String newCategoryName = faker.harryPotter().character();

        categoryDishEditButton.get(0).click();

        isEditContainerCorrect();

        String categoryNameBeforeEditing = categoryNameForGuest.getValue();

        categoryNameForGuest.clear();
        categoryNameForGuest.sendKeys(newCategoryName);
        saveEditedCategoryNameButton.click();
        isElementVisible(menuPagePreLoader);
        forceWait(1000);

        String newCategoryNameInItem = categoryDishItemsNames.get(0).getText();
        categoryNameInGuest.shouldHave(text(newCategoryName));

        System.out.println(categoryNameBeforeEditing + " старое имя категории");
        System.out.println(newCategoryNameInItem + " новое имя категории");

        categoryDishItemsNames.get(0).shouldHave(matchText(newCategoryNameInItem));
        System.out.println("Имя категории изменилось");

        return newCategoryName;

    }

    @Step("Очищаем введенное категории ранее")
    public void changeEditedCategoryNameByDefaultValue(String categoryNameBeforeEditing) {

        categoryDishEditButton.get(0).click();
        categoryNameForGuest.clear();
        categoryNameForGuest.sendKeys(categoryNameBeforeEditing);
        saveEditedCategoryNameButton.click();
        menuPagePreLoader.shouldBe(visible, Duration.ofSeconds(5));
        forceWait(1000);
        System.out.println("Заменили имя на старое");

    }

    @Step("Проверка что позиция редактируется и изменения сохраняются")
    public String isDishEditNameCorrect() {

        Faker faker = new Faker();

        String newDishName = faker.harryPotter().spell();

        menuDishItemsEditButtons.get(0).click();
        isElementVisible(editDishNameInput);
        isElementVisible(editDishNameOkButton);

        String dishNameBeforeEditing = editDishNameInput.getValue();

        editDishNameInput.clear();
        editDishNameInput.sendKeys(newDishName);
        editDishNameOkButton.click();
        forceWait(1000);

        dishPreloader.shouldHave(cssValue("display", "none"), Duration.ofSeconds(5));
        String newDishNameInItem = menuDishItemsEditNames.get(0).getText();

        System.out.println(dishNameBeforeEditing + " старое имя блюда");
        System.out.println(newDishNameInItem + " новое имя блюда");

        menuDishItemsEditNames.get(0).shouldHave(matchText(newDishNameInItem));

        System.out.println("Имя блюда успешно изменилось");

        return newDishName;

    }

    @Step("Очищаем введенное имя блюда ранее")
    public void changeEditedDishNameByDefaultValue(String categoryDishBeforeEditing) {

        menuDishItemsEditButtons.get(0).click();
        editDishNameInput.clear();
        editDishNameInput.sendKeys(categoryDishBeforeEditing);
        editDishNameOkButton.click();
        dishPreloader.shouldHave(cssValue("display", "flex"));
        System.out.println("Заменили имя на старое");

    }

    @Step("Сопоставление количество блюд в категориях")
    public void matchDishesItemSizeWithCategorySize() {

        int categorySize = categoryDishItems.size();

        for (int indexSize = 0; indexSize < categorySize; indexSize++) {

           int dishSizeInCategory = Integer.parseInt(categoryDishItemsSize.get(indexSize).getText().replaceAll("\\D+(\\d+).*","$1"));
           System.out.println(dishSizeInCategory + " dish size in category");

           categoryDishItems.get(indexSize).click();
           dishPreloader.shouldHave(cssValue("display","none"),Duration.ofSeconds(5));

           int dishItemSize = menuDishItems.size();
            System.out.println(dishItemSize + " dish size in menu");


            Assertions.assertEquals(dishSizeInCategory,dishItemSize,
                    "Количество блюд в категории не совпадает с заголовком");
            System.out.println("Количество блюд в категории совпадает с заголовком");

        }

    }

    @Step("Скрыть все категории блюд")
    public void hideAllCategoryMenu() {

        for (SelenideElement categoryDishItem : categoryDishEyeIcons) {

            if (categoryDishItem.getAttribute("class").matches(".*active.*")) {

                categoryDishItem.click();
                forceWait(200);

            }
        }
    }

    @Step("Скрыть все блюда")
    public void hideAllDishInList() {

        for (SelenideElement dishMenu : menuDishItemsEyeIcons) {

            if (dishMenu.getAttribute("class").matches(".*active.*")) {

                dishMenu.click();
                System.out.println("Раскрыли блюдо");
                dishPreloader.shouldNotBe(visible,Duration.ofSeconds(10));
                forceWait(200);

            }
        }
    }



    @Step("Выбрать рандомное число категорий")
    public void chooseRandomCategoryByAmount(int neededCategoryAmount) {

        for (int count = 1; count <= neededCategoryAmount; count++) {

            int index;
            String flag;

            do {

                index = generateRandomNumber(1, categoryDishEyeIcons.size()) - 1;
                flag = categoryDishEyeIcons.get(index).getAttribute("class");

            } while (flag.matches(".*active.*"));

            categoryDishEyeIcons.get(index).click();
            menuPagePreLoader.shouldNotBe(visible,Duration.ofSeconds(10));
            System.out.println("Открыли позицию");

        }

    }

    @Step("Выбрать рандомное число блюд")
    public void chooseRandomDishByAmount(int neededCategoryAmount) {

        for (int categoryIndex = 0 ; categoryIndex < categoryDishItems.size(); categoryIndex++) {

            categoryDishItems.get(categoryIndex).click();
            dishPreloader.shouldNotBe(visible,Duration.ofSeconds(10));

            hideAllDishInList();

            System.out.println(neededCategoryAmount + " количество для выбора");

            for (int count = 1; count <= neededCategoryAmount; count++) {

                int index;
                String flag;

                do {

                    index = generateRandomNumber(1, menuDishItemsEyeIcons.size()) - 1;
                    flag = menuDishItemsEyeIcons.get(index).getAttribute("class");

                } while (flag.matches(".*active.*"));

                menuDishItemsEyeIcons.get(index).click();
                dishPreloader.shouldNotBe(visible,Duration.ofSeconds(10));
                System.out.println("Открыли позицию");

            }

        }

    }

    @Step("Сохраняем список из выбранных категорий")
    public ArrayList<String> saveChosenAmountCategoryInList() {

        ArrayList<String> dishList = new ArrayList<>();

        for (int index = 0; index < categoryDishItems.size(); index++) {

            if (categoryDishEyeIcons.get(index).getAttribute("class").matches(".*active.*")) {

               String categoryName = categoryDishItemsNames.get(index).getText();
               System.out.println(categoryName + " имя категории");
               dishList.add(categoryName);

            }

        }

        System.out.println("\n" + dishList);
        return dishList;

    }

    @Step("Включить 'Показать активные для гостя'")
    public void activateShowActiveForGuest() {

        if (!showActivePositionsForGuestCheckbox.isSelected()) {

            System.out.println("Включили 'Показать активные для гостя'");
            showActivePositionsForGuestContainer.$(".vCheckbox__container").click();
            showActivePositionsForGuestCheckbox.shouldBe(checked);

        }

    }

    @Step("Выключить 'Показать активные для гостя'")
    public void deactivateShowActiveForGuest() {

        if (showActivePositionsForGuestCheckbox.isSelected()) {

            System.out.println("Выключили 'Показать активные для гостя'");
            showActivePositionsForGuestContainer.$(".vCheckbox__container").click();
            showActivePositionsForGuestCheckbox.shouldNotBe(checked);

        }

    }

    @Step("'Показать активные для гостя' соответствует отображению в админке и на столе")
    public ArrayList<String>  showActiveCategoryCorrect() {

        hideAllCategoryMenu();

        activateShowActiveForGuest();

        categoryDishItems.shouldHave(size(0));

        deactivateShowActiveForGuest();

        chooseRandomCategoryByAmount(3);
        ArrayList<String> dishList = saveChosenAmountCategoryInList();

        activateShowActiveForGuest();

        ArrayList<String> dishListWithActiveCheckboxShowForGuest = saveChosenAmountCategoryInList();

        System.out.println(dishList + " категории отмеченные для видимости");
        System.out.println(dishListWithActiveCheckboxShowForGuest + " категории c 'Показать активные для гостя'");

        Assertions.assertEquals(dishList,dishListWithActiveCheckboxShowForGuest);
        System.out.println("Список активных категорий и список категорий со включенной 'Показать активные для гостя полностью совпадают'");

        return dishListWithActiveCheckboxShowForGuest;

    }

    @Step("Сохранение данных по меню для работы с таппером")
    public HashMap<String,Map<String,String>> saveMenuData() {

        hideAllCategoryMenu();

        chooseRandomCategoryByAmount(3);

        activateShowActiveForGuest();

        chooseRandomDishByAmount(1);

        HashMap<String,Map<String,String>> menuData = new HashMap<>();

        for (SelenideElement categoryElement:categoryDishItems) {

            categoryElement.click();
            forceWait(500);
            menuPagePreLoader.shouldNotBe(visible,Duration.ofSeconds(10));
            String categoryName = categoryElement.$(".vAdminMenuCategoryItem__info-bottom")
                    .getText().replaceAll("\\d+\\.\\s","");

            System.out.println(categoryName + " текущая категория");

            Map<String, String> dishList = new HashMap<>();

            for (int index = 0; index < menuDishItemsEyeIcons.size();index++) {

                if (menuDishItemsEyeIcons.get(index).getAttribute("class").matches(".*active.*")) {

                    String dishName;

                    System.out.println(menuDishItemsNames.get(index).getText() + " имя добавленного блюда");

                    String dishPrice = menuDishItemsPrices.get(index).getText();
                    System.out.println(dishPrice + " цена блюда");

                    String dishImage = "";

                    if (menuDishItemsImage.get(index).exists()) {

                        dishImage = menuDishItemsImage.get(index).getAttribute("src");
                        System.out.println(dishImage + " ссылка на фотографию");

                    }

                    if (menuDishItemsEditNames.get(index).getText().equals("-")) {

                        dishName = menuDishItemsNames.get(index).getText();

                    } else {

                        dishName = menuDishItemsEditNames.get(index).getText();

                    }

                    dishList.put("name",dishName);
                    dishList.put("price",dishPrice);
                    dishList.put("image",dishImage);

                }

            }

            menuData.put(categoryName,dishList);

        }

        System.out.println(menuData);
        return menuData;

    }

    @Step("Проверка списка категорий с админки со столом в таппере меню")
    public void matchCategoryListWithTapperMenu(ArrayList<String> dishListWithActiveCheckboxShowForGuest) {

        ArrayList<String> tapperCategoryNameHeader = new ArrayList<>();

        for (SelenideElement element: menuCategoryInHeader) {

            tapperCategoryNameHeader.add(element.getText());

        }

        System.out.println(tapperCategoryNameHeader);

        for (int index = 0; index < dishListWithActiveCheckboxShowForGuest.size(); index++) {

           String tapperCategoryElement = tapperCategoryNameHeader.get(index);
           String adminCategoryElement = dishListWithActiveCheckboxShowForGuest.get(index);

            System.out.println(tapperCategoryElement + " значение в таппере");
            System.out.println(adminCategoryElement + " значение в админке");

           if (!adminCategoryElement.matches(".*" + tapperCategoryElement + ".*")) {

                Assertions.fail("Нет такого значения в столе таппера");

            }

        }

        System.out.println("Все значения совпали со столом и админкой");

    }

}
