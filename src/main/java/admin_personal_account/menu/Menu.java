package admin_personal_account.menu;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import tapper_table.RootPage;

import java.io.File;
import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static data.Constants.PASTA_IMG_PATH;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.AdminPersonalAccount.Common.menuCategory;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.Menu.*;
import static data.selectors.TapperTable.RootPage.DishList.orderMenuContainer;
import static data.selectors.TapperTable.RootPage.Menu.*;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;

public class Menu extends BaseActions {

    RootPage rootPage = new RootPage();

    @Step("Переход в категорию меню")
    public void goToMenuCategory() {

        click(menuCategory);
        pageHeading.shouldHave(text("Меню"), Duration.ofSeconds(5));

        if (!menuMobilePlug.exists())
            menuContainer.shouldBe(visible,Duration.ofSeconds(5));

    }

    @Step("Проверка что все элементы в меню корректны и отображаются")
    public void isMenuCategoryCorrect() {

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

        return generateRandomNumber(1, categoryEyeIcons.size()) - 1;

    }

    @Step("Делаем видимым хотя бы одну категорию меню если они скрыты")
    public void makeVisibleMenuCategory(int randomIndex) {

        if (!Objects.requireNonNull
                (categoryEyeIcons.get(randomIndex).getAttribute("class")).matches(".*active.*"))
            click(categoryEyeIcons.get(randomIndex));

        chooseCategory(randomIndex);

    }

    @Step("Делаем видимым хотя бы одно блюдо в категории если они скрыты")
    public void makeVisibleMenuDish() {

        if (!Objects.requireNonNull
                (menuDishItemsEyeIcons.first().getAttribute("class")).matches(".*active.*"))
            click(categoryEyeIcons.first());

    }

    @Step("Проверка элементов карточки блюда в списке")
    public void isDishItemCorrectInList() {

        isElementVisible(menuDishIndexNumber);
        isElementsListVisible(menuDishItemsImageContainer);
        isElementVisible(categoryBlockMenuDishNameOnCashDesk);
        isElementVisible(categoryBlockMenuDishNameOnGuest);
        isElementsListVisible(menuDishIngredients);
        isElementsListVisible(menuDishPrice);
        isElementsListVisible(menuDishWeight);
        isElementsListVisible(menuDishCalories);
        isElementsListVisible(menuDishMark);

    }

    @Step("Выбираем категорию")
    public void chooseCategory(int index) {

        click(categoryItems.get(index));

        //if (menuPagePreLoader.exists())
          //  menuPagePreLoader.shouldBe(hidden).shouldBe(visible).shouldBe(hidden);

    }

    @Step("Активируем категории и блюда для автоматизации")
    public void activateOnlyAllAutoCategoryAndDishes() {

        deactivateAllMenuCategory();
        activateShowGuestSliderIfDeactivated();
        activateAutoCategoryAndDishes(FIRST_AUTO_MENU_CATEGORY);
        activateAutoCategoryAndDishes(SECOND_AUTO_MENU_CATEGORY);

    }

    public void activateAutoCategoryAndDishes(String autoGroup) {

        categoryItems.asDynamicIterable().stream().forEach(element -> {

            if (element.$(categoryItemsNamesSelector).getText().equals(autoGroup))
                if (!Objects.requireNonNull
                        (element.$(categoryEyeIconSelector).getAttribute("class")).matches(".*active.*")) {

                    click(element.$(categoryEyeIconSelector));
                    click(element.$(categoryItemsNamesSelector));

                    if (!Objects.requireNonNull
                            (menuDishItemsEyeIcons.first().getAttribute("class")).matches(".*active.*"))
                        click(menuDishItemsEyeIcons.first());

                    if (!Objects.requireNonNull
                            (menuDishItemsEyeIcons.last().getAttribute("class")).matches(".*active.*"))
                        click(menuDishItemsEyeIcons.last());

                }

        });


    }

    @Step("Сопоставление количество блюд в категориях")
    public void matchDishesItemSizeWithCategorySize() {

        for (int categoryIndex = 0; categoryIndex < categoryItems.size(); categoryIndex++) {

            click(categoryItems.get(categoryIndex));
            isElementsCollectionVisible(menuDishItems);

            int dishSizeInCategory = Integer.parseInt(categoryItemsSize.get(categoryIndex)
                    .getText().replaceAll("\\D+(\\d+).*","$1"));

            Assertions.assertEquals(dishSizeInCategory,menuDishItems.size(),
                    "Количество блюд в категории не совпадает с заголовком");

        }

    }

    @Step("'Показать активные для гостя' соответствует отображению в админке и на столе")
    public ArrayList<String> showActiveCategoryCorrect() {

        hideAllCategoryMenu();

        activateShowActiveForGuest();

        categoryItems.shouldHave(size(0));

        deactivateShowActiveForGuest();

        chooseRandomCategoryByAmount(3);
        ArrayList<String> dishList = saveChosenAmountCategoryInList();

        activateShowActiveForGuest();

        ArrayList<String> dishListWithActiveCheckboxShowForGuest = saveChosenAmountCategoryInList();

        Assertions.assertEquals(dishList,dishListWithActiveCheckboxShowForGuest);

        return dishListWithActiveCheckboxShowForGuest;

    }

    @Step("Сохранение данных по меню для работы с таппером")
    public LinkedHashMap<String,Map<String,String>> saveMenuData() {

        hideAllCategoryMenu();
        deactivateShowActiveForGuest();

        chooseRandomCategoryByAmount(3);

        activateShowActiveForGuest();

        chooseRandomDishByAmount(1);

        LinkedHashMap<String,Map<String,String>> menuData = new LinkedHashMap<>();

        for (SelenideElement categoryElement: categoryItems) {

            click(categoryElement);

            String categoryName = categoryElement.$(categoryItemsNamesSelector).getText()
                    .replaceAll("\\d+\\.\\s","");

            Map<String, String> dishList = new HashMap<>();

            for (int dishIndex = 0; dishIndex < menuDishItemsEyeIcons.size(); dishIndex++) {

                if (Objects.requireNonNull(menuDishItemsEyeIcons.get(dishIndex).getAttribute("class"))
                        .matches(".*active.*")) {

                    String dishName = menuDishNameByGuest.get(dishIndex).getText().equals("-") ?
                            menuDishNameAtCashDesk.get(dishIndex).getText() :
                            menuDishNameByGuest.get(dishIndex).getText();

                    String dishPrice = menuDishItemsPrices.get(dishIndex).getText();

                    dishList.put("name",dishName);
                    dishList.put("price",dishPrice);

                }

            }

            menuData.put(categoryName,dishList);

        }

        return menuData;

    }

    @Step("Проверка списка категорий с админки со столом в таппере меню")
    public void matchCategoryListWithTapperMenu(ArrayList<String> dishListWithActiveCheckboxShowForGuest) {

        ArrayList<String> tapperCategoryNameHeader = new ArrayList<>();

        isElementsCollectionVisible(menuCategoryInHeader,7);

        menuCategoryInHeader.asDynamicIterable().stream().forEach
                (element -> tapperCategoryNameHeader.add(element.getText()));

        Assertions.assertEquals
                (dishListWithActiveCheckboxShowForGuest.size(),tapperCategoryNameHeader.size(),
                        "Активных категорий в админке и на столе отличаются");

        for (int index = 0; index < dishListWithActiveCheckboxShowForGuest.size(); index++) {

            String tapperCategoryElement = tapperCategoryNameHeader.get(index);
            String adminCategoryElement = dishListWithActiveCheckboxShowForGuest.get(index);

            if (!adminCategoryElement.matches(".*" + tapperCategoryElement + ".*"))
                Assertions.fail("Нет такого значения в столе таппера");

        }

    }

    @Step("Скрываем все категории меню")
    public void deactivateAllMenuCategory() {

        isElementsCollectionVisible(categoryEyeIcons);

        ElementsCollection activeDishes = categoryEyeIcons
                .filter(attributeMatching("class", ".*active.*"));

        if (activeDishes.size() != 0)
            activeDishes.asFixedIterable().stream().forEach(element -> {

                click(element);
                element.shouldNotHave(attributeMatching("class", ".*active.*"));

            });

    }

    @Step("Удаление фотографии у блюда")
    public void deleteDishImage(int dishIndex) {

        click(menuDishItemsEditButtons.get(dishIndex));

        if (!imagePreviewIcon.isDisplayed()) {

            click(deleteDishImageButton);

            isElementVisible(deleteImageContainer);
            isElementVisible(deleteImageContainerDeleteButton);
            isElementVisible(deleteImageContainerCancelButton);

            click(deleteImageContainerDeleteButton);

            isElementVisible(imagePreviewIcon);

        }

    }

    public void isImageCorrectInPreview() {

        isElementVisible(imagePreviewContainer);
        isImageCorrect(menuDishItemsImageInPreviewDishSelector,
                "Изображение блюда в превью редактировании не корректное");

        click(imagePreviewContainerCloseButton);

    }

    @Step("Загрузка изображения для блюда")
    public String uploadImageFile(int dishIndex) {

        File imageFile = new File(PASTA_IMG_PATH);
        imageUploadInput.uploadFile(imageFile);

        downloadedPreviewImage.shouldBe(exist,Duration.ofSeconds(10))
                            .shouldBe(image)
                            .shouldHave(attribute("src"));

        isImageCorrect(editDishImageSelector,"Изображение блюда загрузилось корректно");

        downloadedPreviewImage.shouldBe(visible,enabled);
        click(downloadedPreviewImage);
        isImageCorrectInPreview();

        click(saveButton);

        $(menuDishItemsImageInDishListSelector).shouldHave(image,Duration.ofSeconds(5));
        isImageCorrect(menuDishItemsImageInDishListSelector,
                "Изображение блюда в списке блюд не корректное");

        click(menuDishItemsImage.get(dishIndex));
        isImageCorrectInPreview();

        return menuDishNameByGuest.get(dishIndex).getText();

    }

    @Step("Включаем по кнопке отображение меню для посетителей если вдруг оно выключено")
    public void activateShowGuestSliderIfDeactivated() {

        if (!enableMenuForVisitorsInput.isSelected())
            click(enableMenuForVisitorsButton);

        enableMenuForVisitorsInput.shouldBe(checked);

    }

    @Step("Выключаем по кнопке отображение меню для посетителей если вдруг оно включено")
    public void deactivateShowGuestSliderIfActivated() {

        if (enableMenuForVisitorsInput.isSelected())
            click(enableMenuForVisitorsButton);

        enableMenuForVisitorsInput.shouldNotBe(checked);

    }

    @Step("Включаем первую категорию и первое блюдо")
    public void activateFirstCategoryAndDishInMenu() {

        makeVisibleMenuCategory(0);
        makeVisibleMenuDish();
        activateShowGuestSliderIfDeactivated();

    }

    public void setMeasureUnitValue(int measureUnitOptionsIndex) {

        editDishMeasureUnitInput.shouldBe(interactable).click();

        SelenideElement elementToClick = editDishMeasureUnitInputOptions.get(measureUnitOptionsIndex);
        String measureUnit = elementToClick.getText();

        click(elementToClick);

        editDishMeasureUnitInput.shouldHave(text(measureUnit));

    }

    public void isAmountAndMeasureUnitInputsCorrectError(int dishIndex) {

        click(menuDishItemsEditButtons.get(dishIndex));

        clearText(editDishAmountInput);

        setMeasureUnitValue(0);

        setMeasureUnitValue(1);

        editDishAmountErrorInput.shouldHave(text(NOT_CHOSEN_DISH_AMOUNT_INPUT_ERROR));
        editDishAmountErrorContainer
                .shouldHave(cssValue("border-top-color",EDIT_DISH_ERROR));

        clearText(editDishAmountInput);
        sendKeys(editDishAmountInput,String.valueOf(generateRandomNumber(100,200)));

        setMeasureUnitValue(0);

        isElementVisible(editDishMeasureUnitErrorContainer);

        clearText(editDishAmountInput);

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
    public void activateNonAutoCategoryAndDishAndActivateShowGuestMenuByIndex(int categoryIndex, int dishIndex) {

        isElementsCollectionVisible(notAutoMenuCategory);

        deactivateAllMenuCategory();

        if (!enableMenuForVisitorsInput.isSelected()) {

            click(enableMenuForVisitorsButton);
            enableMenuForVisitorsInput.shouldBe(selected);

        }

        click(notAutoMenuCategory.get(categoryIndex));
        isCategoryChosen(categoryIndex);

        click(notAutoMenuCategory.get(categoryIndex).$(categoryEyeIconSelector));
        isCategoryActive(categoryIndex);

        if (!Objects.requireNonNull(menuDishItemsEyeIcons.get(dishIndex)
                .getAttribute("class")).matches(".*active.*")) {

            click(menuDishItemsEyeIcons.get(dishIndex));
            isDishActive(dishIndex);

        }

    }

    public void isCategoryActive(int categoryIndex) {

        notAutoMenuCategory.get(categoryIndex).$(categoryEyeIconSelector)
                .shouldHave(attributeMatching("class",".*active.*"));

    }

    public void isCategoryChosen(int categoryIndex) {

        notAutoMenuCategory.get(categoryIndex).$(categoryNameTotalSelector)
                .shouldHave(cssValue("border-color",CHOSEN_MENU_CATEGORY));

    }

    public void isDishActive(int dishIndex) {

        menuDishItemsEyeIcons.get(dishIndex)
                .shouldHave(attributeMatching("class",".*active.*"));

    }

    @Step("Проверка что позиция редактируется и изменения сохраняются")
    public String changeName(int dishIndex) {

        Faker faker = new Faker();
        String newDishName = faker.harryPotter().spell();

        click(menuDishItemsEditButtons.get(dishIndex));

        isDishEditModalCorrect();

        clearText(editDishNameByGuestInput);

        sendKeys(editDishNameByGuestInput,newDishName);

        clickCancelButtonIfNoChangeOrSaveData();

        isElementInvisible(editDishContainer);

        return newDishName;

    }

    @Step("Проверка что позиция редактируется и изменения сохраняются")
    public String changeNameButNotSave(int dishIndex) {

        Faker faker = new Faker();
        String newDishName = faker.harryPotter().spell();

        menuDishItemsEditButtons.get(dishIndex).shouldBe(visible,enabled).click();

        isDishEditModalCorrect();

        clearText(editDishNameByGuestInput);

        sendKeys(editDishNameByGuestInput,newDishName);

        click(editDishContainerCloseButton);

        isCancelConfirmationContainerCorrect();

        click(cancelConfirmationYesButton);

        isElementInvisible(editDishContainer);

        return newDishName;

    }

    @Step("Проверка закрытия окна редактирования без внесения изменений")
    public void isPopupClosedWithoutConfirmationAfterNotEditingData(int dishIndex) {

        click(menuDishItemsEditButtons.get(dishIndex));

        isDishEditModalCorrect();

        click(editDishContainerCloseButton);

        isElementInvisible(editDishContainer);

        isElementsListVisible(menuDishItems);

    }

    @Step("Проверка отображения вида списка блюда")
    public void isDishListTileViewCorrect() {

        activateFirstCategoryAndDishInMenu();

        int dishesContainerSize = dishCardContainer.size();

        click(tileViewButton);

        dishCardContainer.filter(cssValue("display","flex"))
                .shouldHave(size(dishesContainerSize));

        menuTableHeaderItems.filter(hidden)
                .shouldHave(sizeGreaterThan(0));

        click(listViewButton);

        menuTableHeaderItems.filter(attributeMatching("style",""))
                .shouldHave(sizeGreaterThan(0));

    }

    @Step("Проверка функциональности кнопки Обновить меню")
    public void isRefreshMenuCorrect() {

        click(refreshMenuButton);
        //menuPagePreLoader.shouldBe(hidden).shouldBe(visible).shouldBe(hidden);
        menuPagePreLoader.shouldHave(matchText("Выгрузка меню"));
        menuPagePreLoader.shouldBe(hidden,Duration.ofSeconds(40));

    }

    public String isInputAndCounterHasCharLimit(SelenideElement input, String inputTextOverLimit, String inputTextLimit,
                                              SelenideElement inputCounter, String inputCounterText) {

        clearText(input);

        sendKeys(input,inputTextOverLimit);
        input.shouldHave(value(inputTextLimit),Duration.ofSeconds(2));

        inputCounter.shouldHave(text(inputCounterText));

        return input.getValue();

    }

    public void setDefaultTextToInput(SelenideElement element,String previousText) {

        clearText(element);
        sendKeys(element,previousText);

        element.shouldHave(value(previousText));

    }

    @Step("Заполнение полей максимальным количеством знаков и проверка отображения")
    public HashMap<String,String> isInputsLimitCorrect() {

        HashMap<String,String> previousData = new HashMap<>();

        String previousNameByGuest = isInputAndCounterHasCharLimit
                (editDishNameByGuestInput,OVER_LIMIT_CHARS_NAME_BY_GUEST_INPUT,LIMIT_CHARS_NAME_BY_GUEST_INPUT,
                        editDishNameByCashDeskInputCounter,LIMIT_CHARS_NAME_BY_GUEST_COUNTER);

        previousData.put("previousNameByGuest",previousNameByGuest);

        String previousDescription = isInputAndCounterHasCharLimit
                (editDishDescriptionInput,OVER_LIMIT_CHARS_DESCRIPTIONS_INPUT,LIMIT_CHARS_DESCRIPTIONS_INPUT,
                        editDishDescriptionInputCounter,LIMIT_CHARS_DESCRIPTIONS_COUNTER);
        previousData.put("previousDescription",previousDescription);

        String previousIngredients = isInputAndCounterHasCharLimit
                (editDishIngredientsInput,OVER_LIMIT_CHARS_INGREDIENTS_INPUT,LIMIT_CHARS_INGREDIENTS_INPUT,
                        editDishIngredientsInputCounter,LIMIT_CHARS_INGREDIENTS_COUNTER);
        previousData.put("previousIngredients",previousIngredients);

        String previousAmount = isInputAndCounterHasCharLimit
                (editDishAmountInput, OVER_LIMIT_CHARS_AMOUNT_INPUT, LIMIT_CHARS_AMOUNT_INPUT,
                        editDishAmountInputCounter, LIMIT_CHARS_AMOUNT_COUNTER);
        previousData.put("previousAmount",previousAmount);

        setMeasureUnitValue(1);

        String previousCalories = isInputAndCounterHasCharLimit
                (editDishCaloriesInput, OVER_LIMIT_CHARS_CALORIES_INPUT, LIMIT_CHARS_CALORIES_INPUT,
                        editDishCaloriesInputCounter, LIMIT_CHARS_CALORIES_COUNTER);
        previousData.put("previousCalories",previousCalories);

        clickCancelButtonIfNoChangeOrSaveData();

        isElementsListVisible(menuDishItems);

        return previousData;

    }

    @Step("Установка прошлых значений в карточке товара")
    public void setDefaultInputsValue(HashMap<String,String> previousData) {

        setDefaultTextToInput(editDishNameByGuestInput,previousData.get("previousNameByGuest"));
        setDefaultTextToInput(editDishDescriptionInput,previousData.get("previousDescription"));
        setDefaultTextToInput(editDishIngredientsInput,previousData.get("previousIngredients"));
        setDefaultTextToInput(editDishAmountInput,previousData.get("previousAmount"));
        setDefaultTextToInput(editDishCaloriesInput,previousData.get("previousCalories"));

        clickCancelButtonIfNoChangeOrSaveData();

    }

    @Step("Проверка на сохранения изменений в позиции после обновления страницы")
    public void isChangingSavedAfterPageReload(int dishIndex) {

        rootPage.refreshPage();
        //menuPagePreLoader.shouldBe(hidden).shouldBe(visible).shouldBe(hidden);

        activateFirstCategoryAndDishInMenu();

        String dishNameAfterRefresh = menuDishNameByGuest.get(dishIndex).getText();

        menuDishNameByGuest.get(dishIndex).shouldHave(text(dishNameAfterRefresh));

    }

    @Step("Проверка формы отмены внесенных изменений")
    public void isCancelConfirmationContainerCorrect() {

        isElementVisible(cancelConfirmationContainer);
        isElementVisible(cancelConfirmationNoButton);
        isElementVisible(cancelConfirmationYesButton);

    }

    @Step("Проверяем что смена имени категории корректна")
    public String isCategoryEditNameCorrect(int categoryIndex) {

        Faker faker = new Faker();

        String newCategoryName = faker.harryPotter().character();

        click(notAutoMenuCategory.get(categoryIndex).$(categoryEditButtonSelector));

        isEditContainerCorrect();

        clearText(categoryNameForGuest);
        sendKeys(categoryNameForGuest,newCategoryName);

        click(saveEditedCategoryNameButton);
        //menuPagePreLoader.shouldBe(hidden).shouldBe(visible).shouldBe(hidden);

        isElementsCollectionVisible(categoryItemsNames);

        String newCategoryNameInItem = notAutoMenuCategory.get(categoryIndex).$(categoryNameSelector).getText();

        notAutoMenuCategory.get(categoryIndex).$(categoryNameSelector).shouldHave(matchText(newCategoryNameInItem));
        categoryNameInGuest.shouldHave(text(newCategoryName));

        return newCategoryName;

    }

    @Step("Проверяем что смена имени блюда корректна")
    public String isDishEditNameByGuestCorrect(int dishIndex) {

        String dishNameBeforeEditing = menuDishItemsNames.get(dishIndex).getText();
        String dishNameAfterEditing = changeName(dishIndex);

        Assertions.assertNotEquals(dishNameBeforeEditing,dishNameAfterEditing,
                "Старое имя не изменилось на новое");

        return dishNameAfterEditing;

    }

    @Step("Проверяем что смена имени блюда и затем закрытие формы не сохранит результат")
    public void isDishEditNameByGuestCorrect_Negative(int dishIndex) {

        String dishNameBeforeEditing = menuDishItemsNames.get(dishIndex).getText();
        String dishNameAfterEditing = changeNameButNotSave(dishIndex);

        Assertions.assertNotEquals(dishNameBeforeEditing,dishNameAfterEditing,
                "Изменения сохранились, но не должны были");

    }

    @Step("Проверяем что смена описании блюда корректна")
    public HashMap<String,String> isDishEditDescriptionCorrect(int dishIndex) {

        String dishName = menuDishNameByGuest.get(dishIndex).getText();

        click(menuDishItemsEditButtons.get(dishIndex));

        Faker faker = new Faker();
        String newDescription = faker.food().dish();

        clearText(editDishDescriptionInput);

        sendKeys(editDishDescriptionInput,newDescription);

        clickCancelButtonIfNoChangeOrSaveData();

        isElementInvisible(editDishContainer);

        click(menuDishItemsEditButtons.get(dishIndex));

        editDishDescriptionInput.shouldHave(value(newDescription));

        click(editDishContainerCloseButton);

        isElementInvisible(editDishContainer);

        HashMap<String,String> dishNameAndDescription = new HashMap<>();
        dishNameAndDescription.put("dishName",dishName);
        dishNameAndDescription.put("description",newDescription);

        return dishNameAndDescription;

    }

    @Step("Проверяем что смена описании блюда корректна")
    public HashMap<String,String> isDishEditIngredientsCorrect(int dishIndex) {

        String dishName = menuDishNameByGuest.get(dishIndex).getText();

        click(menuDishItemsEditButtons.get(dishIndex));

        Faker faker = new Faker();
        String newIngredients = faker.ancient().hero();

        clearText(editDishIngredientsInput);

        sendKeys(editDishIngredientsInput,newIngredients);

        clickCancelButtonIfNoChangeOrSaveData();

        isElementInvisible(editDishContainer);

        Assertions.assertNotNull(menuDishIngredients.findBy(text(newIngredients)));

        click(menuDishItemsEditButtons.get(dishIndex));

        editDishIngredientsInput.shouldHave(value(newIngredients));

        click(editDishContainerCloseButton);

        isElementInvisible(editDishContainer);

        HashMap<String,String> dishNameAndIngredients = new HashMap<>();
        dishNameAndIngredients.put("dishName",dishName);
        dishNameAndIngredients.put("ingredients",newIngredients);

        return dishNameAndIngredients;

    }

    @Step("Проверяем что смена цены блюда корректна")
    public String getDishPrice(int dishIndex) {

        return menuDishPrice.get(dishIndex).getText();

    }

    public void isDownloadedImageCorrectOnTable(String imageName) {

        if (!orderMenuContainer.isDisplayed())
            click(appFooterMenuIcon);

        SelenideElement elementWithNewPhoto  = dishMenuItemsName.findBy(text(imageName));

        rootPage.isImageCorrect(menuDishPhotosSelector,"Фотография на столе загружена корректно");

        click(elementWithNewPhoto);

        rootPage.isImageCorrect(menuDishPhotoInDetailCard,
                "Фотография на столе в карточке товара корректна");

    }

    public void clickCancelButtonIfNoChangeOrSaveData() {

        if (saveButton.getAttribute("disabled") == null) {

            click(saveButton);


        } else {

            click(editDishContainerCloseButton);

        }

        isElementsCollectionVisible(menuDishItems);

    }

    @Step("Проверяем что смена веса и количества блюда корректна")
    public String isDishEditWeightAndAmountCorrect(int dishIndex) {

        click(menuDishItemsEditButtons.get(dishIndex));

        Faker faker = new Faker();
        String newWeight = String.valueOf(faker.number().randomDigitNotZero());

        isElementVisible(editDishAmountInput);

        clearText(editDishAmountInput);

        sendKeys(editDishAmountInput,newWeight);

        click(editDishMeasureUnitInput);

        SelenideElement elementToClick = editDishMeasureUnitInputOptions.get(generateRandomNumber(1,6));
        String measureUnit = elementToClick.getText();

        click(elementToClick);

        editDishMeasureUnitInput.shouldHave(text(measureUnit));

        clickCancelButtonIfNoChangeOrSaveData();

        isElementInvisible(editDishContainer);

        SelenideElement weightAndAmountElement =  menuDishWeight.findBy(text(newWeight));
        String weightAndAmount = weightAndAmountElement.getText();

        Assertions.assertNotNull(weightAndAmount);

        return weightAndAmount;

    }

    @Step("Проверяем что смена калорий блюда корректна")
    public String isDishEditCaloriesCorrect(int dishIndex) {

        click(menuDishItemsEditButtons.get(dishIndex));

        Faker faker = new Faker();
        String newCalories = String.valueOf(faker.number().randomDigitNotZero());

        isElementVisible(editDishCaloriesInput);

        clearText(editDishCaloriesInput);

        sendKeys(editDishCaloriesInput,newCalories);

        editDishCaloriesInput.shouldHave(value(newCalories));

        clickCancelButtonIfNoChangeOrSaveData();

        isElementInvisible(editDishContainer);

        SelenideElement caloriesElement =  menuDishCalories.findBy(text(newCalories));
        String calories = caloriesElement.getText();

        Assertions.assertNotNull(calories);

        return newCalories;

    }

    @Step("Проверяем что смена меток корректна")
    public String isDishEditMarkCorrect(int dishIndex) {

        click(menuDishItemsEditButtons.get(dishIndex));

        click(editDishMarksSelect);

        if (!editDishMarksSelect.getText().equals("Пусто")) {

            click(editDishMarksSelectOptions.get(0));
            click(editDishMarksSelect);

        }

        SelenideElement elementToClick = editDishMarksSelectOptions.get(generateRandomNumber(1,3));
        String markTextOnOption = elementToClick.getText();

        click(elementToClick);

        editDishMarksSelect.shouldHave(text(markTextOnOption));

        clickCancelButtonIfNoChangeOrSaveData();

        isElementInvisible(editDishContainer);

        SelenideElement markElementInDishListing =  menuDishMark.findBy(text(markTextOnOption));
        String markText = markElementInDishListing.getText();

        Assertions.assertNotNull(markText);

        return markText;

    }

    @Step("Проверяем что изменения корректны на столе с элементом {textToFind}")
    public void isChangingAppliedOnTable(ElementsCollection elementsWhereToFind,
                                         String textToFind) {

        if (!orderMenuContainer.isDisplayed())
            click(appFooterMenuIcon);

        SelenideElement element = elementsWhereToFind.findBy(matchText(textToFind));

        Assertions.assertNotNull(element,"Элемент " + textToFind + " не найден на столе");

    }

    @Step("Проверяем что изменения корректны в карточке блюда с элементом")
    public void isChangingAppliedOnTableInDishCard(ElementsCollection elementsWhereToFind,
                                                   HashMap<String,String> data, String type) {

        if (!orderMenuContainer.isDisplayed())
            click(appFooterMenuIcon);

        SelenideElement element = elementsWhereToFind.findBy(matchText(data.get("dishName")));

        click(element);

        String neededText;

        if (type.equals("description")) {

            neededText = data.get("description");

        } else {

            neededText = data.get("ingredients");

        }

        Assertions.assertNotNull(dishDetailCardContent,"Элемент " + neededText +
                " не в карточке товара");

        click(dishDetailCardCloseButton);

    }

    @Step("Проверяем что изменения корректны в карточке блюда с элементом {textToFind}")
    public void isChangingAppliedOnTableInDishCard(ElementsCollection elementsWhereToFind,
                                                   String textToFind) {

        if (!orderMenuContainer.isDisplayed())
            click(appFooterMenuIcon);

        SelenideElement element = elementsWhereToFind.findBy(matchText(textToFind));

        click(element);

        Assertions.assertNotNull(element,"Элемент " + textToFind +
                " не в карточке товара");

        click(dishDetailCardOverlay);

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

    @Step("Скрыть все категории блюд")
    public void hideAllCategoryMenu() {

        rootPage.isElementsCollectionVisible(categoryEyeIcons);

        categoryEyeIcons.asDynamicIterable().stream().forEach(element -> {

        if (Objects.requireNonNull(element.getAttribute("class")).matches(".*active.*"))
            click(element);

        });

    }

    @Step("Скрыть все блюда")
    public void hideAllDishInList() {

        rootPage.isElementsCollectionVisible(categoryEyeIcons);

        menuDishItemsEyeIcons.asDynamicIterable().stream().forEach(element -> {

            if (Objects.requireNonNull(element.getAttribute("class")).matches(".*active.*")) {

                click(element);
                dishPreloader.shouldNotBe(visible, Duration.ofSeconds(10));

            }

        });

    }

    @Step("Выбрать рандомное число категорий")
    public void chooseRandomCategoryByAmount(int neededCategoryAmount) {

        for (int count = 1; count <= neededCategoryAmount; count++) {

            int index;
            String flag;

            do {

                index = generateRandomNumber(1, categoryEyeIcons.size()) - 1;
                flag = categoryEyeIcons.get(index).getAttribute("class");

            } while (Objects.requireNonNull(flag).matches(".*active.*"));

            click(categoryEyeIcons.get(index));
            categoryEyeIcons.get(index).shouldHave(attributeMatching("class",".*active.*"));

        }

    }

    @Step("Выбрать рандомное число блюд")
    public void chooseRandomDishByAmount(int neededCategoryAmount) {

        for (SelenideElement categoryItem : categoryItems) {

            click(categoryItem);
            dishPreloader.shouldBe(hidden, Duration.ofSeconds(10));

            hideAllDishInList();

            for (int count = 1; count <= neededCategoryAmount; count++) {

                int index;
                String flag;

                do {

                    index = generateRandomNumber(1, menuDishItemsEyeIcons.size()) - 1;
                    flag = menuDishItemsEyeIcons.get(index).getAttribute("class");

                } while (Objects.requireNonNull(flag).matches(".*active.*"));

                click(menuDishItemsEyeIcons.get(index));
                dishPreloader.shouldBe(hidden, Duration.ofSeconds(10));

            }

        }

    }

    @Step("Сохраняем список из выбранных категорий")
    public ArrayList<String> saveChosenAmountCategoryInList() {

        ArrayList<String> dishList = new ArrayList<>();

        for (int index = 0; index < categoryItems.size(); index++) {

            if (Objects.requireNonNull
                    (categoryEyeIcons.get(index).getAttribute("class")).matches(".*active.*"))
               dishList.add(categoryItemsNames.get(index).getText());

        }

        return dishList;

    }

    @Step("Включить 'Показать активные для гостя'")
    public void activateShowActiveForGuest() {

        if (!showActivePositionsForGuestCheckbox.isSelected()) {

            click(showActivePositionsForGuestContainer.$(categoryItemsCheckboxSelector));
            showActivePositionsForGuestCheckbox.shouldBe(checked);

        }

    }

    @Step("Выключить 'Показать активные для гостя'")
    public void deactivateShowActiveForGuest() {

        if (showActivePositionsForGuestCheckbox.isSelected()) {

            click(showActivePositionsForGuestContainer.$(categoryItemsCheckboxSelector));
            showActivePositionsForGuestCheckbox.shouldNotBe(checked);

        }

    }

    @Step("Переключаемся на другую вкладку и обновляем её")
    public void switchTabAndRefreshPage(int tab) {

        rootPage.switchBrowserTab(tab);
        rootPage.refreshPage();

    }

}
