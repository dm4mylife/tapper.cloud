package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeNotEqual;
import static com.codeborne.selenide.Selenide.$;

public class RootCatalog extends BaseActions {

    BaseActions baseActions = new BaseActions();


    private final SelenideElement catalogProducts = $(".catalog__items .product");

    @Step("Каталог присутствует и не пустой")
    public void checkIsCatalogNotEmpty() {

        catalogProducts.shouldBe(Condition.visible);
        catalogProducts.shouldNotHave(Condition.empty);

    }

}
