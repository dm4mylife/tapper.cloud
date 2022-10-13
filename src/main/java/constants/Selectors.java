package constants;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class Selectors {

    public static class SearchPage {

        public static final SelenideElement searchInput =
                $("#search_textbox_input_top");
        public static final SelenideElement searchContainer =
                $(".title-search-result");
        public static final ElementsCollection linksInSearchContainer =
                $$(".search-result-grid .title-search-item a");
        public static final SelenideElement linkInSearchContainer =
                $(".search-result-grid .title-search-item a");
        public static final SelenideElement resultPageProductContainer =
                $("a[data-target='click_search_listing']");

    }

    public static class RootCatalogPage {

        public static final ElementsCollection catalogProducts =
                $$(".catalog__items [class=\"product\"]");
        public static final SelenideElement firstElementCatalogProducts =
                $x("//*[@class=\"catalog__items\"]/*[@class=\"product\"][1]");
        public static final SelenideElement middleRandomElementCatalogProductsOpenedByHover =
                $x("//*[@class=\"catalog__items\"]/*[@class=\"product\"][10]/div[@class=\"product__overlay\"]/div[@class=\"product__info\"]");
        public static final SelenideElement first15ElementCatalogProducts =
                $(".catalog__items .product:nth-child(20)");
        public static final SelenideElement first30ElementCatalogProducts =
                $(".catalog__items .product:nth-child(40)");
        public static final SelenideElement first45ElementCatalogProducts =
                $(".catalog__items .product:nth-child(60)");
        public static final SelenideElement first60ElementCatalogProducts =
                $("footer");
        public static final SelenideElement show20ElementsOnPage =
                $x("//*[@class=\"sort\"]//*[contains(@class,\"sort__block-link\")][contains(@onclick,\"20\")]");
        public static final SelenideElement show50ElementsOnPage =
                $x("//*[@class=\"sort\"]//*[contains(@class,\"sort__block-link\")][contains(@onclick,\"50\")]");
        public static final SelenideElement show100ElementsOnPage =
                $x("//*[@class=\"sort\"]//*[contains(@class,\"sort__block-link\")][contains(@onclick,\"100\")]");
    }


}
