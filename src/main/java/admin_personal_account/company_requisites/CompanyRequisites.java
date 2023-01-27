package admin_personal_account.company_requisites;


import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static data.selectors.AdminPersonalAccount.Common.companyRequisitesCategory;
import static data.selectors.AdminPersonalAccount.Common.pageHeading;
import static data.selectors.AdminPersonalAccount.CompanyRequisites.*;

public class CompanyRequisites extends BaseActions {

    @Step("Переход в категорию реквизиты компании")

    public void goToCompanyRequisitesCategory() {

        click(companyRequisitesCategory);
        pageHeading.shouldHave(text("Реквизиты компании"), Duration.ofSeconds(5));
        requisitesContainer.shouldBe(visible, Duration.ofSeconds(5));

    }

    @Step("Проверка элементов страницы")
    public void isCompanyRequisitesCategoryCorrect() {

        isElementVisible(organizationName);
        isElementVisible(innOrganization);
        isElementVisible(phoneOrganization);
        isElementVisible(saveButton);

    }


}
