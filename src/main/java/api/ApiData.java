package api;

public class ApiData {

    public static class EndPoints {

        public static final String createOrder = "rkeeper/createorder";
        public static final String fillingOrder = "rkeeper/fillingorder";
        public static final String deleteOrder = "rkeeper/delorder";
        public static final String orderGet = "order/get";
        public static final String b2bPaymentTransactionStatus = "https://apitapper.zedform.ru/api/payment/transaction-status/";
        public static final String deletePosition = "rkeeper-automation/delete-position";


    }

    public static class orderData {

        public static final String R_KEEPER_RESTAURANT = "testrkeeper";
        public static final String TABLE_3 = "12"; //5
        public static final String TABLE_10 = "10"; //5
        public static final String TABLE_3_ID = "1000046";
        public static final String TABLE_10_ID = "1000044";

        public static final String WAITER_ROBOCOP_VERIFIED_WITH_CARD = "23";
        public static final String WAITER_TERMINATOR_VERIFIED_NON_CARD = "233";
        public static final String WAITER_IRONMAN_NON_VERIFIED_NON_CARD = "2333";
        public static final String BARNOE_PIVO = "1000361";
        public static final String GLAZUNYA = "1000368";
        public static final String TORT = "1000385";
        public static final String SOLYANKA = "1000364";
        public static final String GOVYADINA_PORTION = "1000363";

        public static final String DISH_WITH_FREE_NOT_NECESSARY_MODI = "1000108";
        public static final String DISH_WITH_FREE_NECESSARY_MODI = "1000102";
        public static final String DISH_WITH_PAID_NOT_NECESSARY_MODI = "1000107";
        public static final String DISH_WITH_PAID_NECESSARY_MODI = "1000101";

        public static final String BARANINA = "1000367";
        public static final String LIMONAD = "1000362";

        public static final String BORSH = "1000102";
        public static final String FREE_NECESSARY_MODI_SALT = "1000114";
        public static final String FREE_NECESSARY_MODI_PEPPER = "1000115";

        public static final String XOLODEC = "1000108";
        public static final String FREE_NON_NECESSARY_MODI_BUTTER = "1000120";
        public static final String FREE_NON_NECESSARY_MODI_MAYONES = "1000121";

        public static final String CAESAR = "1000101";
        public static final String PAID_NECESSARY_MODI_BANAN_SIROP = "1000112";
        public static final String PAID_NECESSARY_MODI_KARAMEL_SIROP = "1000111";

        public static final String RAGU = "1000107";
        public static final String PAID_NON_NECESSARY_MODI_KARTOSHKA_FRI = "1000117";
        public static final String PAID_NON_NECESSARY_MODI_SOUS = "1000118";
        public static final String PAID_NON_NECESSARY_MODI_SALAT = "1000388";

        public static final String VODKA = "1000130";
        public static final String PAID_NON_NECESSARY_MIX_MODI_SALO = "1000128";
        public static final String PAID_NON_NECESSARY_MIX_MODI_BREAD = "1000129";

        public static final String PASTA = "1000103";
        public static final String FREE_NECESSARY_MODI_SOUS = "1000125";
        public static final String PAID_NECESSARY_MODI_BACON = "1000126";



    }

    public static class QueryParams {

        public static String rqParamsCreateOrderBasic(String subDomen, String tableCode, String waiterCode) {

            return "{\n" +
                    "  \"subDomen\": \"" + subDomen + "\",\n" +
                    "  \"tableCode\": " + tableCode + ",\n" +
                    "  \"waiterCode\": \"" + waiterCode + "\",\n" +
                    "  \"persistentComment\": 100500\n" +
                    "}";

        }

        public static String rqParamsFillingOrderBasic(String subDomen, String visit, String dishId, String quantity) {

            return "{\n" +
                    "  \"subDomen\": \"" + subDomen + "\",\n" +
                    "  \"quantity\": " + quantity + ",\n" +
                    "  \"visit\": \"" + visit + "\",\n" +
                    "  \"dishId\": \"" + dishId + "\"\n" +
                    "}";

        }

        public static String rqParamsDeletePosition(String domen, String guid, String station, String uni, String quantity) {

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": \"" + station + "\",\n" +
                    "  \"item_code\": \"" + uni + "\"\n" +
                    "  \"quantity\": " + quantity + "\n" +
                    "}";

        }

    }

}
