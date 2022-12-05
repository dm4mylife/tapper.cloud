package constants;

public class Constant {

    public static class TestData {

        public static final String TEST_ROOT_URL = "https://tapper3.zedform.ru/testrkeeper/1000046";

        public static final String STAGE_RKEEPER_TABLE_3 = "https://tapper.staging.zedform.ru/testrkeeper/1000046"; //   https://tapper-ssr.zedform.ru/testrkeeper/1000046
        public static final String STAGE_RKEEPER_TABLE_10 = "https://tapper.staging.zedform.ru/testrkeeper/1000044";
        public static final String STAGE_IIKO_URL = "https://tapper.staging.zedform.ru/office/3";

        public static final String DEMO = "https://tapper.cloud/demo";
        public static final String IPHONE12PRO = "390x844";
        public static final String IPHONE12PRO_TEST = "540x900";
        public static final String TEST_BEST2PAY_URL = "https://test.best2pay.net/";
        public static final String TEST_PAYMENT_CARD_NUMBER = "4809388886227309";
        public static final String TEST_PAYMENT_CARD_EXPIRE_MONTH = "12";
        public static final String TEST_PAYMENT_CARD_EXPIRE_YEAR = "24";
        public static final String TEST_PAYMENT_CARD_CVV = "123";
        public static final String TEST_YANDEX_LOGIN_EMAIL = "autotests@tapper.cloud";
        public static final String TEST_YANDEX_PASSWORD_MAIL = "V8JRPGwr";
        public static final Integer TIME_WAIT_FOR_FULL_LOAD = 3000;

        public static final String TEST_WAITER_COMMENT = "test\\тест";
        public static final String TEST_REVIEW_COMMENT = "test\\тест";
        public static final String API_TEST_URI = "https://taper.zedform.ru/api/";
        public static final String API_STAGE_URI = "https://apitapper.zedform.ru/api/";

        public static final Double SERVICE_PRICE_PERCENT_FROM_TOTAL_SUM = 1.5;
        public static final Double SERVICE_PRICE_PERCENT_FROM_TIPS = 5.0;
        public static final String TIPS_ERROR_MSG = " Минимальная сумма чаевых 49 ₽ ";
        public static final String MIN_SUM_FOR_TIPS_ERROR = "48";
        public static final String MIN_SUM_TIPS_ = "49";
        public static final String YANDEX_MAIL_URL = "https://passport.yandex.ru/auth?retpath=https%3A%2F%2Fmail.yandex.ru%2F&backpath=https%3A%2F%2Fmail.yandex.ru%2F%3Fnoretpath%3D1&from=mail&origin=hostroot_homer_auth_ru";


    }

    public static class TestDataRKeeperAdmin {

        public static final String R_KEEPER_ADMIN_URL = "https://tapper3.zedform.ru/users";
        public static final String ADMIN_LOGIN_EMAIL = "varlone_mag2@mail.ru";
        public static final String ADMIN_PASSWORD = "123123";


    }

    public static class JSScripts {

        public static final String isShareButtonCorrect = """
                function check() {
                   if (navigator.share) {
                       return true;
                   } else {
                       return false;
                   }
                }; return check();""";

        public static final String userSession = """
                function getCookie(name) {
                  const value = `; ${document.cookie}`;
                  const parts = value.split(`; ${name}=`);
                        if (parts.length === 2) return parts.pop().split(';').shift();
                }; return getCookie("session")""";

        public static final String userGuest = """
                function getCookie(name) {
                  const value = `; ${document.cookie}`;
                  const parts = value.split(`; ${name}=`);
                        if (parts.length === 2) return parts.pop().split(';').shift();
                }; return getCookie("guest")""";

        public static final String isWaiterImageBroken = """
                function isImageNotBroken()  {
                    var img = document.querySelector(".tips__waiter img");
                    if (img.complete &&  typeof img.naturalWidth != "undefined" && img.naturalWidth > 0)\s
                        {return true;}
                    else
                        {return false;}
                };
                return isImageNotBroken();""";



    }


}
