package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;
import java.time.Duration;
import static com.codeborne.selenide.Condition.*;
import static constants.Selectors.RootCatalogPage.*;

public class MainCatalogPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    @Step("Каталог присутствует и не пустой")
    public void checkIsCatalogNotEmpty() {

        firstElementCatalogProducts.shouldBe(Condition.visible);
        firstElementCatalogProducts.shouldNotHave(Condition.empty);

    }

    @Step("Проверка что в каталоге нет пустых\\битых изображений")
    public void checkIsCatalogProductsHasImage() {

        smoothScrollAllCatalog();
        for (SelenideElement element : catalogProducts) {

            element.$(".product__img.js-lazyload")
                    .shouldHave(attributeMatching("style", "display: block.*"), Duration.ofSeconds(10));

        }

    }

    @Step("Плавный скрол всего каталога")
    public void smoothScrollAllCatalog() {

        baseActions.scrollIntoView(first15ElementCatalogProducts);
        baseActions.forceWait(300L);
        baseActions.scrollIntoView(first30ElementCatalogProducts);
        baseActions.forceWait(300L);
        baseActions.scrollIntoView(first45ElementCatalogProducts);
        baseActions.forceWait(300L);
        baseActions.scrollIntoView(first60ElementCatalogProducts);

    }

    public void isProductContainerVisibleByHover() {

        baseActions.isElementInvisible(middleRandomElementCatalogProductsOpenedByHover);
        baseActions.scrollIntoView(middleRandomElementCatalogProductsOpenedByHover);
        Selenide.executeJavaScript("window.scrollBy(0,-1000)");
        baseActions.moveMouseToElement(middleRandomElementCatalogProductsOpenedByHover);
        baseActions.isElementVisible(middleRandomElementCatalogProductsOpenedByHover);

    }


    public void clickOnRandomCatalogProduct() {


        int index = baseActions.generateRandomNumber(0,catalogProducts.size());
        SelenideElement chosenCatalogProduct = catalogProducts.get(index);
        String chosenCatalogProductAttribute = chosenCatalogProduct.$("a").getAttribute("href");

        String text = chosenCatalogProductAttribute.replaceAll(".*/catalog/\\w+\\/(.+)\\/","$1");

        baseActions.click(chosenCatalogProduct);
        baseActions.isTextContainsInURL(text);



    }

    public void checkIsPageElementsCountCorrect() {

        baseActions.scrollIntoView(show20ElementsOnPage);
        baseActions.isElementVisibleAndClickable(show20ElementsOnPage);
        baseActions.click(show20ElementsOnPage);
        baseActions.isTextContainsInURL("?pageElementCount=20");
        baseActions.isElementsSizeGreaterThanNumber(catalogProducts,20);

        baseActions.isElementVisibleAndClickable(show50ElementsOnPage);
        baseActions.click(show50ElementsOnPage);
        baseActions.isTextContainsInURL("?pageElementCount=50");
        baseActions.isElementsSizeGreaterThanNumber(catalogProducts,50);

        baseActions.isElementVisibleAndClickable(show100ElementsOnPage);
        baseActions.click(show100ElementsOnPage);
        baseActions.isTextContainsInURL("?pageElementCount=100");
        baseActions.isElementsSizeGreaterThanNumber(catalogProducts,100);
    }
}


