package admin_personal_account.company_requisites;


import common.BaseActions;
import io.qameta.allure.Step;
import tapper_table.RootPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static data.selectors.AdminPersonalAccount.Common.companyRequisitesCategory;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.CompanyRequisites.*;
import static data.selectors.AdminPersonalAccount.Profile.pagePreloader;

public class CompanyRequisites extends BaseActions {

    RootPage rootPage = new RootPage();

    @Step("Переход в категорию реквизиты компании")

    public void goToCompanyRequisitesCategory() {

        click(companyRequisitesCategory);
        pageHeading.shouldHave(text("Реквизиты компании"), Duration.ofSeconds(5));
        requisitesContainer.shouldBe(visible, Duration.ofSeconds(5));

    }

    @Step("Проверка элементов страницы")
    public void isCompanyRequisitesCategoryCorrect() {

        pagePreloader.shouldNotBe(visible,Duration.ofSeconds(5));
        isElementVisible(organizationName);
        isElementVisible(innOrganization);
        isElementVisible(phoneOrganization);
        isElementVisible(saveButton);

    }
    @Step("Проверка что после обновления страницы мы остались на этом табе")
    public void isCorrectAfterRefresh() {

        rootPage.refreshPage();
        isCompanyRequisitesCategoryCorrect();

    }

}
