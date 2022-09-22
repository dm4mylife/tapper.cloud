package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.*;
import static constants.Constant.Urls.ROOT_URL;

public class SearchPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    private final SelenideElement searchInput = $("#search_textbox_input_top");
    private final SelenideElement searchContainer = $(".title-search-result");
    private final ElementsCollection linksInSearchContainer = $$(".search-result-grid .title-search-item a");
    private final SelenideElement linkInSearchContainer = $(".search-result-grid .title-search-item a");
    private final SelenideElement resultPageProductContainer = $("a[data-target='click_search_listing']");


    public void simpleBaseTestForDebugging() {

        baseActions.openPage("https://miuz.ru/catalog/rings/R01-PL-34397/");
        baseActions.sendHumanKeys(searchInput,"Кольцо с бриллиантом");
        baseActions.isElementVisible(searchContainer);
        baseActions.isElementsContainsText($$(linksInSearchContainer),"Средство");

    }

    public void checkIsSearchResultContainerVisibleAndInvisible() {

        baseActions.openPage(ROOT_URL);
        baseActions.click(searchInput);
        baseActions.sendHumanKeys(searchInput, "Кольцо");
        baseActions.isElementVisible(searchContainer);
        baseActions.deleteTextInInput(searchInput);
        baseActions.isElementInVisible(searchContainer);

    }

    public void checkIsCategorySearchCorrect(String requestText) {

        baseActions.deleteTextInInput(searchInput);
        baseActions.sendHumanKeys(searchInput,requestText);
        baseActions.isElementVisible(searchContainer);

        linksInSearchContainer.shouldHave(size(8));
        baseActions.isElementsContainsText(linksInSearchContainer,requestText);

    }

    public void checkIsCategoryWithStonesSearchCorrect(String requestText) {


        baseActions.deleteTextInInput(searchInput);
        baseActions.sendHumanKeys(searchInput,requestText);

        baseActions.isElementVisible(linkInSearchContainer);
        linksInSearchContainer.shouldHave(size(8));
        baseActions.isElementsContainsText(linksInSearchContainer,requestText);

    }

    public void checkIsArticleSearchCorrect(String article) {

        baseActions.deleteTextInInput(searchInput);
        baseActions.sendHumanKeys(searchInput,article);

        baseActions.isElementVisible(linkInSearchContainer);
        baseActions.isElementContainsText(linkInSearchContainer,article);

        baseActions.sendKeys(searchInput, Keys.ENTER);

        baseActions.isTextContainsInURL(article);
        baseActions.isClickable(resultPageProductContainer);

        baseActions.click(resultPageProductContainer);

        baseActions.isTextContainsInURL(article);


    }

    public void checkIsIDSearchCorrect(String element_ID) {

        baseActions.deleteTextInInput(searchInput);
        baseActions.sendHumanKeys(searchInput,element_ID);

        baseActions.isElementVisible(linkInSearchContainer);

        baseActions.sendKeys(searchInput, Keys.ENTER);

        baseActions.isTextContainsInURL(element_ID);
        baseActions.isClickable(resultPageProductContainer);

        baseActions.click(resultPageProductContainer);

        baseActions.isTextContainsInURL(element_ID);


    }

    public void checkIsCollectionSearchCorrect(String requestText) {

        baseActions.deleteTextInInput(searchInput);
        baseActions.sendHumanKeys(searchInput,requestText);

        baseActions.isElementVisible(linkInSearchContainer);
        linksInSearchContainer.shouldHave(size(8));

    }

    public void checkIsOnlyStonesSearchCorrect(String requestText) {

        baseActions.deleteTextInInput(searchInput);
        baseActions.sendHumanKeys(searchInput,requestText);

        baseActions.isElementVisible(linkInSearchContainer);

    }

}
