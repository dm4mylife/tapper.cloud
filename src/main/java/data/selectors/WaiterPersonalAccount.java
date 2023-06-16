package data.selectors;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class WaiterPersonalAccount {

    public static final SelenideElement confPolicyModal = $(".vModalPolitics");
    public static final SelenideElement confPolicyModalDonTAgreeButton = $(".vModalPolitics__cancelBtn");
    public static final SelenideElement confPolicyError = $(".sign__error");
    public static final SelenideElement confPolicyModalAgreeButton = $(".vModalPolitics__buttons .vButton");
    public static final SelenideElement pageTitle = $(".section-profile__title");
    public static final SelenideElement imageContainer = $(".section-profile__photo-block");
    public static final SelenideElement imageContainerDownloadImageButton =
            $(".section-profile__photo-block .section-profile__file");
    public static final SelenideElement imageContainerDownloadedImage = $(".section-profile__photo-wrapper");
    public static final String imageContainerDownloadedImageNotSelenide = ".section-profile__photo-wrapper img";
    public static final SelenideElement imageDeleteButton = $(".section-profile__photo-del");
    public static final SelenideElement waiterNameInCashDesk = $("[id=\"name\"][disabled]");
    public static final SelenideElement waiterName = $("[id=\"display_name\"]:not([disabled])");
    public static final SelenideElement telegramLogin = $(".section-profile__login");
    public static final SelenideElement waiterGoalInput = $("[id=\"purposeWaiter\"]");
    public static final SelenideElement waiterGoalErrorInput = $(".vLandingInput__err");
    public static final SelenideElement tipsBlockInfoContainer = $(".blockTipsWaiter__info");
    public static final SelenideElement waiterGoalContainer = $("div>[id=\"purposeWaiter\"]");
    public static final SelenideElement waiterGoalMaxCharsCounter = $(".blockTipsWaiter__input-number");
    public static final SelenideElement waiterEmail = $("[id=\"email\"]");
    public static final SelenideElement waiterPassword = $("[id=\"password\"]");
    public static final SelenideElement waiterPasswordConfirmation = $("[id=\"confirmation\"]");
    public static final SelenideElement linkTelegramLogin = $(".section-profile__button-text.blue");
    public static final SelenideElement unlinkTelegramLogin = $(".section-profile__button.red");
    public static final SelenideElement linkWaiterCard = $x("//*[@class='vButton']");
    public static final SelenideElement saveButton =
            $x("//*[contains(@class,'vButton')][@type=\"submit\"]");
    public static final SelenideElement buttonWithCard =
            $x("//*[contains(@class,'vButton vButton--gray')]");
    public static final SelenideElement changedCardButton = $(".change-card");
    public static final SelenideElement b2pContainer = $("[id=\"payForm\"]");
    public static final SelenideElement b2pCardNumber = $("[id=\"pan\"]");
    public static final SelenideElement b2pCardExpiredDate = $("[id=\"cardDate\"]");
    public static final SelenideElement b2pCardCvc = $("[id=\"temp-cvc\"]");
    public static final SelenideElement b2pSaveButton = $("[id=\"submitButton\"]");

}
