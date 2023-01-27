package data.selectors;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class WaiterPersonalAccount {

        public static final SelenideElement pageTitle= $(".section-profile__title");
        public static final SelenideElement imageContainer= $(".section-profile__photo-block");
        public static final SelenideElement imageContainerDownloadImageButton=
                $(".section-profile__photo-block .section-profile__file");
        public static final SelenideElement imageContainerDownloadedImage= $(".section-profile__photo-wrapper");
        public static final String imageContainerDownloadedImageNotSelenide  = ".section-profile__photo-wrapper img";
        public static final SelenideElement imageDeleteButton= $(".section-profile__photo-del");
        public static final SelenideElement waiterNameInCashDesk = $("[id=\"name\"][disabled]");
        public static final SelenideElement waiterName = $("[id=\"name\"]:not([disabled])");
        public static final SelenideElement telegramLogin = $("[id=\"1\"]");
        public static final SelenideElement waiterEmail = $("[id=\"email\"]");
        public static final SelenideElement waiterPassword = $("[id=\"password\"]");
        public static final SelenideElement waiterPasswordConfirmation = $("[id=\"confirmation\"]");
        public static final SelenideElement learnMoreLink =
                $("[class='section-profile__legend-info'] .helpModalOpen");
        public static final SelenideElement telegramInstruction = $(".modalInstruction");
        public static final SelenideElement telegramInstructionCloseButton = $(".vCloseButton");
        public static final SelenideElement linkWaiterCard =
                $x("//*[@class='vButton']");
        public static final SelenideElement saveButton =
                $x("//*[contains(@class,'vButton')][@type=\"submit\"]");
        public static final SelenideElement buttonWithCard =
                $x("//*[contains(@class,'vButton')][@disabled]");
        public static final SelenideElement changedCardButton =
                $(".change-card");
        public static final SelenideElement b2pContainer =
                $("[id=\"payForm\"]");
        public static final SelenideElement b2pCardNumber =
                $("[id=\"pan\"]");
        public static final SelenideElement b2pCardExpiredDate =
                $("[id=\"cardDate\"]");
        public static final SelenideElement b2pCardCvc =
                $("[id=\"cvc\"]");
        public static final SelenideElement b2pSaveButton =
                $("[id=\"submitButton\"]");

}
