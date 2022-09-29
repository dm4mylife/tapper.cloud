package pages;

import common.BaseActions;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.size;
import static constants.Constant.Urls.ROOT_URL;
import static constants.Selectors.SearchPage.*;

public class SearchPage extends BaseActions {

    BaseActions baseActions = new BaseActions();


    public void simpleBaseTestForDebugging() {

        baseActions.openPage("https://miuz.ru/catalog/rings/R01-PL-34397/");
        baseActions.sendHumanKeys(searchInput,"Кольцо с бриллиантом");
        baseActions.isElementVisible(searchContainer);
        baseActions.isElementContainsText(linkInSearchContainer,"Средство");

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
            baseActions.sendHumanKeys(searchInput, requestText);
            baseActions.isElementVisible(searchContainer);

            linksInSearchContainer.shouldHave(size(8));

    }

    public void checkIsCategoryWithStonesSearchCorrect(String requestText) {

        baseActions.deleteTextInInput(searchInput);
        baseActions.sendHumanKeys(searchInput,requestText);

        baseActions.isElementVisible(linkInSearchContainer);
        linksInSearchContainer.shouldHave(size(8));


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

            baseActions.isElementVisibleLongWait(linkInSearchContainer);

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
