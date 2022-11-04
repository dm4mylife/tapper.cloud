package constants;

public class Constant {

    public static class TestData {

        public static final String TEST_ROOT_URL = "https://tapper3.zedform.ru/testrkeeper/1000046";
        public static final String STAGE_ROOT_URL = "https://tapper.staging.zedform.ru/testrkeeper/1000046";

        public static final String DEMO = "https://tapper.cloud/demo";

        public static final String TEST_BEST2PAY_URL = "https://test.best2pay.net/";
        public static final String TEST_PAYMENT_CARD_NUMBER = "4809388886227309";
        public static final String TEST_PAYMENT_CARD_EXPIRE_MONTH = "12";
        public static final String TEST_PAYMENT_CARD_EXPIRE_YEAR = "24";
        public static final String TEST_PAYMENT_CARD_CVV = "123";
        public static final String TEST_EMAIL = "autotest@mail.ru";
        public static final String TEST_WAITER_COMMENT = "test waiter comment text area";
        public static final String TEST_REVIEW_COMMENT = "Тестовый коммент для официанта ru\\eng";
        public static final String API_URI = "https://taper.zedform.ru/api/";
        public static final Double SERVICE_PRICE_PERCENT = 1.5;
        public static final String TIPS_ERROR_MSG = " Минимальная сумма чаевых 49 ₽ ";
        public static final String MIN_SUM_FOR_TIPS_ERROR = "48";


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
                      \s
                   };\s
                   return check();""";


    }


}
