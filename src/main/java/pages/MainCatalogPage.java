package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.attributeMatching;
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

}


