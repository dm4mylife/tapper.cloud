package admin_personal_account.menu;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static data.Constants.PASTA_IMG_PATH;
import static data.selectors.AdminPersonalAccount.Common.menuCategory;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.Menu.*;
import static data.selectors.TapperTable.RootPage.DishList.orderMenuContainer;
import static data.selectors.TapperTable.RootPage.Menu.menuCategoryInHeader;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;

public class Menu extends BaseActions {

    @Step("Переход в категорию меню")
    public void goToMenuCategory() {

        click(menuCategory);
        pageHeading.shouldHave(text("Меню"), Duration.ofSeconds(5));
        menuContainer.shouldBe(visible,Duration.ofSeconds(5));

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
        isElementVisible(categoryContainer);
        isElementsListVisible(categoryItems);

    }

    @Step("Забираем рандомную активную категорию")
    public int getRandomActiveCategoryIndex() {

        int randomCategory = generateRandomNumber(1, categoryEyeIcons.size()) - 1;
        System.out.println(randomCategory + " индекс рандомной категории");

        return randomCategory;

    }

    @Step("Делаем видимым хотя бы одну категорию меню если они скрыты")
    public void makeVisibleMenuCategory(int randomIndex) {

        System.out.println(categoryEyeIcons.size() + " количество категорий");

        if (!categoryEyeIcons.get(randomIndex).getAttribute("class").matches(".*active.*")) {

            click(categoryEyeIcons.get(randomIndex));

        }

        chooseCategory(randomIndex);

    }

    @Step("Делаем видимым хотя бы одно блюдо в категории если они скрыты")
    public void makeVisibleMenuDish() {

        System.out.println(menuDishItemsEyeIcons.size() + " количество блюд");

        if (!menuDishItemsEyeIcons.first().getAttribute("class").matches(".*active.*")) {

            click(categoryEyeIcons.first());

        }

    }

    @Step("Проверка элементов карточки блюда в списке")
    public void isDishItemCorrectInList() {


        isElementVisible(menuDishIndexNumber);
        isElementVisible(menuDishItemsImageContainer);
        isElementVisible(menuDishNameOnCashDesk);
        isElementVisible(menuDishNameOnGuest);
        isElementsListVisible(menuDishIngredients);
        isElementVisible(menuDishPrice);
        isElementsListVisible(menuDishWeight);
        isElementVisible(menuDishCalories);
        isElementVisible(menuDishMark);

    }


    @Step("Выбираем категорию")
    public void chooseCategory(int index) {

        System.out.println(categoryItems.get(index) + "категория");
        categoryItems.get(index).click();
        System.out.println("Активируем категорию");
        if (menuPagePreLoader.exists()) {

            isElementVisible(menuPagePreLoader);

        }

    }

    @Step("Забираем рандомное активное блюдо")
    public int getRandomActiveDishIndex() {

        System.out.println(menuDishItemsEyeIcons.size() + " количество блюд");

        int dishIndex = generateRandomNumber(1,menuDishItemsEyeIcons.size()) - 1;
        System.out.println(dishIndex + " индекс рандомного блюда");

        return dishIndex;

    }

    @Step("Делаем видимым хотя бы одно блюдо если оно скрыто")
    public void makeVisibleMenuDish(int randomIndex) {

        if (!menuDishItemsEyeIcons.get(randomIndex).getAttribute("class").matches(".*active.*")) {

            System.out.println(menuDishItems.get(randomIndex));
            menuDishItemsEyeIcons.get(randomIndex).click();
            System.out.println("Активируем блюдо");
            isElementVisible(dishPreloader);

        }

    }

    @Step("Скрываем все категории меню")
    public void deactivateMenuCategory() {

        ElementsCollection activeDishes = categoryEyeIcons
                .filter(attributeMatching("class", ".*active.*"));

        if (activeDishes.size() != 0)
            activeDishes.asFixedIterable().stream().forEach(element -> {

                click(element);
                isElementVisible(menuPagePreLoader);
                element.shouldNotHave(attributeMatching("class", ".*active.*"));

            });

    }

    @Step("Удаление фотографии у блюда")
    public void deleteDishImage(int dishIndex) {

        click(menuDishItemsEditButtons.get(dishIndex));
        forceWait(1000);

        click(menuDishItems.get(dishIndex).$(".vAdmiMenuTable__item-img-del"));
        forceWait(1000);

        isElementVisible(deleteImageContainer);
        isElementVisible(deleteImageContainerDeleteButton);
        isElementVisible(deleteImageContainerCancelButton);

        click(deleteImageContainerDeleteButton);
        dishPreloader.shouldBe(Condition.visible);
        forceWait(1000);

        System.out.println("Фотография удалена");


    }

    @Step("Загрузка изображения для блюда")
    public String uploadImageFile(int dishIndex) {

        menuDishItemsEditButtons.get(dishIndex).click();
        forceWait(1000);

        File imageFile = new File(PASTA_IMG_PATH);
        menuDishItemsImageInput.get(dishIndex).uploadFile(imageFile);

        menuDishItems.get(dishIndex).$("img").shouldBe(exist,Duration.ofSeconds(10));
        menuDishItems.get(dishIndex).$("img").shouldHave(attribute("src"));

        Selenide.executeJavaScript(
                "document.querySelectorAll(\".vAdmiMenuTable__item-grid\")" + ".forEach((item)=>{item.remove();})");

        String imageSelector = ".vAdmiMenuTable__item:nth-of-type("+ (dishIndex + 1) +") .vAdmiMenuTable__item-img img";

        menuDishItems.get(dishIndex).$(".vAdmiMenuTable__item-img-del").shouldBe(visible);
        forceWait(3000);

        forceWait(3000);


        System.out.println(menuDishItems.get(dishIndex).$("img"));

        menuDishItems.get(dishIndex).$("img")
                .shouldHave(attributeMatching("src","https://storage.tapper.cloud/.*"),Duration.ofSeconds(5));

        String imageUrl = menuDishItems.get(dishIndex).$("img")
                .getAttribute("src").replaceAll(".*\\/(\\w*)\\..*","$1");

        isImageCorrect(imageSelector,"Загруженное изображение блюда не корректное или битое");

        isImageFullContainerCorrect(dishIndex);

        return imageUrl;

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

        int dishIndex = getRandomActiveCategoryIndex();
        makeVisibleMenuCategory(dishIndex);
        activateShowGuestSliderIfDeactivated();

    }
    @Step("Включаем первую категорию и первое блюдо")
    public void activateFirstCategoryAndDishInMenu() {

        makeVisibleMenuCategory(0);
        makeVisibleMenuDish();
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
    public void activateCategoryAndDishAndActivateShowGuestMenuByIndex(int categoryIndex, int dishIndex) {


        if (!categoryEyeIcons.get(categoryIndex).getAttribute("class").matches(".*active.*")) {

            categoryEyeIcons.get(categoryIndex).click();
            System.out.println("Сделали категорию видимой");

        }

        categoryItems.get(categoryIndex).click();

        if (!menuDishItemsEyeIcons.get(dishIndex).getAttribute("class").matches(".*active.*")) {

            menuDishItemsEyeIcons.get(dishIndex).click();
            System.out.println("Сделали блюдо видимым");

        }

        if (!enableMenuForVisitorsInput.isSelected()) {

            enableMenuForVisitorsButton.click();
            System.out.println("Включили 'Показывать гостям'");

        }

    }



    @Step("Очищаем введенное категории ранее")
    public void changeEditedCategoryNameByDefaultValue(String categoryNameBeforeEditing) {

        categoryEditButton.get(0).click();
        categoryNameForGuest.clear();
        categoryNameForGuest.sendKeys(categoryNameBeforeEditing);
        saveEditedCategoryNameButton.click();
        menuPagePreLoader.shouldBe(visible, Duration.ofSeconds(5));
        forceWait(1000);
        System.out.println("Заменили имя на старое");

    }







    @Step("Проверка что позиция редактируется и изменения сохраняются")
    public String changeName(int dishIndex) {

        Faker faker = new Faker();
        String newDishName = faker.harryPotter().spell();

        click(menuDishItemsEditButtons.get(dishIndex));

        isDishEditModalCorrect();

        clearText(editDishNameByGuestInput);

        sendKeys(editDishNameByGuestInput,newDishName);

        click(saveButton);

        isElementInvisible(editDishContainer);

        return newDishName;

    }

    @Step("Проверяем что смена имени категории корректна")
    public String isCategoryEditNameCorrect(int categoryIndex) {

        Faker faker = new Faker();

        String newCategoryName = faker.harryPotter().character();

        categoryEditButton.get(categoryIndex).click();

        isEditContainerCorrect();

        String categoryNameBeforeEditing = categoryNameForGuest.getValue();

        clearText(categoryNameForGuest);

        sendKeys(categoryNameForGuest,newCategoryName);

        click(saveEditedCategoryNameButton);
        isElementVisible(menuPagePreLoader);
        forceWait(1000);

        String newCategoryNameInItem = categoryItemsNames.get(categoryIndex).getText();
        categoryNameInGuest.shouldHave(text(newCategoryName));

        System.out.println(categoryNameBeforeEditing + " старое имя категории\n" +
                newCategoryNameInItem + " новое имя категории");

        categoryItemsNames.get(categoryIndex).shouldHave(matchText(newCategoryNameInItem));
        System.out.println("Имя категории изменилось");

        return newCategoryName;

    }

    @Step("Проверяем что смена имени блюда корректна")
    public String isDishEditNameByGuestCorrect(int dishIndex) {

        String dishNameBeforeEditing = menuDishItemsNames.get(dishIndex).getText();
        String dishNameAfterEditing = changeName(dishIndex);

        System.out.println(dishNameAfterEditing + " dishNameAfterEditing\n" +
                dishNameBeforeEditing + " dishNameBeforeEditing");

        Assertions.assertNotEquals(dishNameBeforeEditing,dishNameAfterEditing,
                "Старое имя не изменилось на новое");

        return dishNameAfterEditing;

    }
    @Step("Проверяем что смена описании блюда корректна")
    public void isDishEditDescriptionCorrect(int dishIndex) {

        click(menuDishItemsEditButtons.get(dishIndex));

        Faker faker = new Faker();
        String newDescription = faker.food().dish();

        clearText(editDishDescriptionInput);

        sendKeys(editDishDescriptionInput,newDescription);

        click(saveButton);

        isElementInvisible(editDishContainer);

        click(menuDishItemsEditButtons.get(dishIndex));

        editDishDescriptionInput.shouldHave(value(newDescription));

    }

    @Step("Проверяем что смена описании блюда корректна")
    public void isDishEditIngredientsCorrect(int dishIndex) {

        click(menuDishItemsEditButtons.get(dishIndex));

        Faker faker = new Faker();
        String newIngredients = faker.ancient().hero();

        clearText(editDishIngredientsInput);

        sendKeys(editDishIngredientsInput,newIngredients);

        click(saveButton);

        isElementInvisible(editDishContainer);

        Assertions.assertNotNull(menuDishIngredients.findBy(text(newIngredients)));

        click(menuDishItemsEditButtons.get(dishIndex));

        editDishIngredientsInput.shouldHave(value(newIngredients));

        click(editContainerCloseButton);

        isElementInvisible(editDishContainer);

    }

    @Step("Проверяем что смена описании блюда корректна")
    public String isDishEditWeightAndAmountCorrect(int dishIndex) {

        click(menuDishItemsEditButtons.get(dishIndex));

        Faker faker = new Faker();
        String newWeight = faker.number().digits(3);

        isElementVisible(editDishAmountInput);

        forceWait(300); //toDo не успевает прогрузиться инпут веса
        clearText(editDishAmountInput);

        sendKeys(editDishAmountInput,newWeight);

        click(editDishMeasureUnitInput);

        SelenideElement elementToClick = editDishMeasureUnitInputOptions.get(generateRandomNumber(2,7));
        String measureUnit = elementToClick.getText();

        click(elementToClick);

        editDishMeasureUnitInput.shouldHave(text(measureUnit));

        click(saveButton);

        isElementInvisible(editDishContainer);

        SelenideElement weightAndAmountElement =  menuDishWeight.findBy(text(newWeight));
        String weightAndAmount = weightAndAmountElement.getText();

        Assertions.assertNotNull(weightAndAmount);

        System.out.println(weightAndAmount + " weightAndAmount");
        return weightAndAmount;

    }

    @Step("Проверяем что изменения корректны на столе с элементом {textToFind}")
    public void isChangingAppliedOnTable(int tapper, int admin,ElementsCollection elementsWhereToFind, String textToFind) {

        switchBrowserTab(tapper);

        if (!orderMenuContainer.isDisplayed())
            click(appFooterMenuIcon);

       Assertions.assertNotNull(elementsWhereToFind.findBy(matchText(textToFind)),"Элемент " + textToFind +
               " не найден на столе");
        System.out.println("Изменения корректны");

       switchBrowserTab(admin);

    }


    public void isNameByCashDeskShowsWhenNameByGuestIsEmpty() {




    }


    public void isDishEditModalCorrect() {

        isElementVisible(editDishContainer);
        isElementVisible(imageContainer);
        isElementVisible(editPhotoButton);
        isElementVisible(editDishNameByCashDeskInput);
        isElementVisible(editDishNameByGuestInput);
        isElementVisible(editDishDescriptionInput);
        isElementVisible(editDishIngredientsInput);
        isElementVisible(editDishAmountInput);
        isElementVisible(editDishMeasureUnitInput);
        isElementVisible(editDishCaloriesInput);
        isElementVisible(editDishMarksSelect);
        isElementVisible(saveButton);
        isElementVisible(cancelButton);

    }

    @Step("Очищаем введенное имя блюда ранее")
    public void changeEditedDishNameByDefaultValue(String categoryDishBeforeEditing) {

        menuDishItemsEditButtons.get(0).click();
        editDishNameByGuestInput.clear();
        editDishNameByGuestInput.sendKeys(categoryDishBeforeEditing);

        dishPreloader.shouldHave(cssValue("display", "flex"));
        System.out.println("Заменили имя на старое");

    }

    @Step("Сопоставление количество блюд в категориях")
    public void matchDishesItemSizeWithCategorySize() {


        for (int categoryIndex = 0; categoryIndex < categoryItems.size(); categoryIndex++) {

            categoryItems.get(categoryIndex).click();
            dishPreloader.shouldHave(cssValue("display","none"),Duration.ofSeconds(5));

            int dishSizeInCategory = Integer.parseInt(categoryItemsSize.get(categoryIndex).getText().replaceAll("\\D+(\\d+).*","$1"));
            int dishItemSize = menuDishItems.size();

            Assertions.assertEquals(dishSizeInCategory,dishItemSize,
                    "Количество блюд в категории не совпадает с заголовком");
            System.out.println("Количество блюд в категории совпадает с заголовком");

        }

    }

    @Step("Скрыть все категории блюд")
    public void hideAllCategoryMenu() {

        for (SelenideElement categoryDishItem : categoryEyeIcons) {

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

    @Step("Проверка изображения в превью контейнере")
    public void isImageFullContainerCorrect(int dishIndex) {

        click(menuDishItems.get(dishIndex).$("img"));

        isElementVisible(imagePreviewContainer);
        isElementVisible(imagePreviewContainerCloseButton);
        isElementVisible(imagePreviewContainerImage);
        isImageCorrect(imagePreviewContainerImageNotSelenide,"Изображение не корректное или битое");
        forceWait(500);

        click(imagePreviewContainerCloseButton);
        imagePreviewContainer.shouldNotBe(visible);

    }

    @Step("Выбрать рандомное число категорий")
    public void chooseRandomCategoryByAmount(int neededCategoryAmount) {

        for (int count = 1; count <= neededCategoryAmount; count++) {

            int index;
            String flag;

            do {

                index = generateRandomNumber(1, categoryEyeIcons.size()) - 1;
                flag = categoryEyeIcons.get(index).getAttribute("class");

            } while (flag.matches(".*active.*"));

            categoryEyeIcons.get(index).click();
            menuPagePreLoader.shouldNotBe(visible,Duration.ofSeconds(10));
            System.out.println("Открыли позицию");

        }

    }

    @Step("Выбрать рандомное число блюд")
    public void chooseRandomDishByAmount(int neededCategoryAmount) {

        for (SelenideElement categoryItem : categoryItems) {

            categoryItem.click();
            dishPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

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
                dishPreloader.shouldNotBe(visible, Duration.ofSeconds(10));
                System.out.println("Открыли позицию");

            }

        }

    }

    @Step("Сохраняем список из выбранных категорий")
    public ArrayList<String> saveChosenAmountCategoryInList() {

        ArrayList<String> dishList = new ArrayList<>();

        for (int index = 0; index < categoryItems.size(); index++) {

            if (categoryEyeIcons.get(index).getAttribute("class").matches(".*active.*")) {

               String categoryName = categoryItemsNames.get(index).getText();
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

        categoryItems.shouldHave(size(0));

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
        deactivateShowActiveForGuest();

        chooseRandomCategoryByAmount(3);

        activateShowActiveForGuest();

        chooseRandomDishByAmount(1);

        HashMap<String,Map<String,String>> menuData = new HashMap<>();

        for (SelenideElement categoryElement: categoryItems) {

            categoryElement.click();
            forceWait(1000);
            menuPagePreLoader.shouldNotBe(visible,Duration.ofSeconds(10));

            String categoryName = categoryElement.$(".vAdminMenuCategoryItem__info-bottom")
                    .getText().replaceAll("\\d+\\.\\s","");
            System.out.println(categoryName + " текущая категория");

            Map<String, String> dishList = new HashMap<>();

            for (int dishIndex = 0; dishIndex < menuDishItemsEyeIcons.size(); dishIndex++) {

                if (menuDishItemsEyeIcons.get(dishIndex).getAttribute("class").matches(".*active.*")) {

                    String dishName;
                    String dishImage = "";

                    if (menuDishItems.get(dishIndex).$(".vAdmiMenuTable__item-img img").exists()) {

                        dishImage = menuDishItems.get(dishIndex).$(".vAdmiMenuTable__item-img img").getAttribute("src");

                    }

                    if (menuDishItemsEditName.get(dishIndex).getText().equals("-")) {

                        dishName = menuDishItemsNames.get(dishIndex).getText();

                    } else {

                        dishName = menuDishItemsEditName.get(dishIndex).getText();

                    }

                    String dishPrice = menuDishItemsPrices.get(dishIndex).getText();

                    System.out.println(menuDishItemsNames.get(dishIndex).getText() + " имя добавленного блюда");
                    System.out.println(dishPrice + " цена блюда");
                    System.out.println(dishImage + " ссылка на фотографию");

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

        Assertions.assertEquals
                (dishListWithActiveCheckboxShowForGuest.size(),tapperCategoryNameHeader.size(),
                        "Активных категорий в админке и на столе отличаются");

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
