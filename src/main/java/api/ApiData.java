package api;

public class ApiData {

    public static class EndPoints {

        public static final String createOrder = "rkeeper/createorder";
        public static final String fillingOrder = "rkeeper/fillingorder";
        public static final String deleteOrder = "rkeeper/delorder";
        public static final String getOrderInfo = "rkeeper/order";
        public static final String checkOrderClosed = "rkeeper-automation/check-order-closed";
        public static final String checkPrepayment = "rkeeper-automation/check-prepayment";
        public static final String getPrepayment = "rkeeper-automation/prepayment";

        public static final String orderPay = "rkeeper-automation/order/pay";
        public static final String deletePosition = "rkeeper-automation/position";
        public static final String deleteDiscount = "rkeeper-automation/discount";
        public static final String addDiscount = "rkeeper-automation/create-discount";
        public static final String addModificatorOrder = "rkeeper-automation/add-modificator-order";
        public static final String b2bPaymentTransactionStatus = "https://apitapper.zedform.ru/api/payment/transaction-status/";

    }

    public static class orderData {

        public static final String R_KEEPER_RESTAURANT = "testrkeeper";
        public static final String TABLE_111 = "12";
        public static final String TABLE_222 = "21";
        public static final String TABLE_333 = "22";
        public static final String TABLE_10 = "10";
        public static final String TABLE_AUTO_1_ID = "1000046";
        public static final String TABLE_10_ID = "1000044";
        public static final String TABLE_AUTO_2_ID = "1000397";
        public static final String TABLE_AUTO_3_ID = "1000398";

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

        public static final String CUSTOM_DISCOUNT_ON_ORDER = "1000057";
        public static final String DISCOUNT_ON_DISH = "1000343";

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
                    "  \"persistentComment\": \"100500\"" +
                    "\n}";

        }

        public static String rqParamsFillingOrderBasic(String subDomen, String visit, String dishId, String quantity) {

            return "{\n" +
                    "  \"subDomen\": \"" + subDomen + "\",\n" +
                    "  \"quantity\": " + quantity + ",\n" +
                    "  \"visit\": \"" + visit + "\",\n" +
                    "  \"dishId\": \"" + dishId + "\"\n" +
                    "}";

        }

        public static String rqParamsDeletePosition(String domen, String guid, String uni, int quantity) {

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": 1,\n" +
                    "  \"uni\": \"" + uni + "\",\n" +
                    "  \"quantity\": " + quantity + "\n" +
                    "}";

        }

        public static String rqParamsAddCustomDiscount(String domen, String guid, String id_discount, String amount_discount) {

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": 1,\n" +
                    "  \"id_discount\": \"" + id_discount + "\",\n" +
                    "  \"amount_discount\": \"" + amount_discount + "\"\n" +
                    "}";

        }

        public static String rqParamsAddDiscount(String domen, String guid, String id_discount) {

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": 1,\n" +
                    "  \"id_discount\": \"" + id_discount + "\"\n" +
                    "}";

        }

        public static String rqParamsDeleteDiscount(String domen, String guid, String uni) {

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": 1,\n" +
                    "  \"uni\": \"" + uni + "\"\n" +
                    "}";

        }

        public static String rqParamsAddModificatorWith1Position(String domen, String guid, String dish,
                                                                  String quantity, String modificatorId,
                                                                  String modificatorQuantity) {

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": 1,\n" +
                    "  \"dish\": \"" + dish + "\",\n" +
                    "  \"quantity\": " + quantity + ",\n" +
                    "  \"modificators\": [\n" +
                    "    {\n" +
                    "      \"id\": \"" + modificatorId + "\",\n" +
                    "      \"quantity\": \"" + modificatorQuantity + "\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

        }

        public static String rqParamsAddModificatorWith2Positions(String domen, String guid, String dish,
                                                            String quantity, String modificatorId,
                                                            String modificatorQuantity, String secondModificatorId,
                                                            String secondModificatorQuantity) {

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": 1,\n" +
                    "  \"dish\": \"" + dish + "\",\n" +
                    "  \"quantity\": " + quantity + ",\n" +
                    "  \"modificators\": [\n" +
                    "    {\n" +
                    "      \"id\": \"" + modificatorId + "\",\n" +
                    "      \"quantity\": \"" + modificatorQuantity + "\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "       \"id\": \"" + secondModificatorId + "\",\n" +
                    "      \"quantity\": \"" + secondModificatorQuantity + "\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

        }

        public static String rqParamsAddModificatorWith3Positions(String domen, String guid, String dish,
                                                                  String quantity, String modificatorId,
                                                                  String modificatorQuantity, String secondModificatorId,
                                                                  String secondModificatorQuantity,
                                                                  String thirdModificatorId, String thirdModificatorQuantity) {

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": 1,\n" +
                    "  \"dish\": \"" + dish + "\",\n" +
                    "  \"quantity\": " + quantity + ",\n" +
                    "  \"modificators\": [\n" +
                    "    {\n" +
                    "      \"id\": \"" + modificatorId + "\",\n" +
                    "      \"quantity\": \"" + modificatorQuantity + "\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "       \"id\": \"" + secondModificatorId + "\",\n" +
                    "      \"quantity\": \"" + secondModificatorQuantity + "\"\n" +
                    "    },\n" +
                    "       \"id\": \"" + thirdModificatorId + "\",\n" +
                    "      \"quantity\": \"" + thirdModificatorQuantity + "\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";



            }

        public static String rqParamsIsOrderClosed(String domen, String guid){

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": 1\n" +
                    "}";

        }

        public static String rqParamsOrderPay(String domen, String guid){

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": 1,\n" +
                    "  \"pay\": 9000000000 \n" +
                    "}";

        }

        public static String rqParamsCheckPrePayment(String transactionId){

            return "{\n" +
                    "  \"transaction_id\": \"" + transactionId + "\"\n" +
                    "}";

        }

        public static String rqParamsGetPrepayment(String domen, String guid){

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": 1\n" +
                    "}";

        }


    }

}
