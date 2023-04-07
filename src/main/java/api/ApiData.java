package api;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;

public class ApiData {

    public static class EndPoints {

        public static final String selenoidUiHubUrl = "http://localhost:4444/wd/hub";
        public static final String selenoidHubUrl = "http://localhost:8081";
        public static final String createOrder = "rkeeper/createorder";

        public static final String getOrderInfo = " rkeeper-automation/order";
        public static final String fillingOrder = "rkeeper-automation/order/fill";
        public static final String deleteOrder = "rkeeper/delorder";
        public static final String checkOrderClosed = "rkeeper-automation/check-order-closed";
        public static final String checkPrepayment = "rkeeper-automation/check-prepayment";
        public static final String getPrepayment = "rkeeper-automation/prepayment";
        public static final String orderPay = "rkeeper-automation/order/pay";
        public static final String deletePosition = "rkeeper-automation/position";
        public static final String deleteDiscount = "rkeeper-automation/discount";
        public static final String deleteRestaurantAdmin = "automation/admin";
        public static final String adminLogin = "users/login";


        public static final String addDiscount = "rkeeper-automation/create-discount";
        public static final String addModificatorOrder = "rkeeper-automation/add-modificator-order";
        public static final String b2bPaymentTransactionStatus =
                "https://apitapper.zedform.ru/api/payment/transaction-status/";
        public static final String loginPersonalAccount = "https://apitapper.zedform.ru/api/users/login";



    }

    public static class orderData {

        public static final String R_KEEPER_RESTAURANT = "testrkeeper";

        public static final String TABLE_CODE_111 = "12";
        public static final String TABLE_CODE_222 = "21";
        public static final String TABLE_CODE_10 = "10";
        public static final String TABLE_CODE_333 = "22";
        public static final String TABLE_CODE_444 = "18";
        public static final String TABLE_CODE_555 = "17";
        public static final String TABLE_CODE_666 = "20";

        public static final String TABLE_AUTO_111_ID = "1000046";
        public static final String TABLE_AUTO_222_ID = "1000397";
        public static final String TABLE_AUTO_10_ID = "1000044";
        public static final String TABLE_AUTO_333_ID = "1000398";
        public static final String TABLE_AUTO_444_ID = "1000423";
        public static final String TABLE_AUTO_555_ID = "1000437";
        public static final String TABLE_AUTO_666_ID = "1000438";

        public static final String WAITER_ROBOCOP_VERIFIED_WITH_CARD = "23";
        public static final String WAITER_TERMINATOR_VERIFIED_NON_CARD = "233";
        public static final String WAITER_IRONMAN_NON_VERIFIED_NON_CARD = "2333";
        public static final String BARNOE_PIVO = "1000361";
        public static final String GLAZUNYA = "1000368";
        public static final String TORT = "1000385";
        public static final String SOLYANKA = "1000364";
        public static final String DISCOUNT_WITH_CUSTOM_SUM = "1000057";
        public static final String DISCOUNT_BY_ID = "1000343";
        public static final String BARANINA = "1000367";
        public static final String LIMONAD = "1000362";
        public static final String ZERO_PRICE_DISH = "1000187";

        public static final String GOVYADINA_PORTION = "1000363";
        public static final String FREE_MODI_SOLT_ZERO_PRICE = "1000388";
        public static final String PAID_MODI_KARTOFEL_FRI = "1000117";
        public static final String PAID_MODI_SOUS = "1000118";
        public static final String PAID_MODI_VEG_SALAD = "1000421";
        public static final String BORSH = "1000102";
        public static final String FREE_NECESSARY_MODI_SALT = "1000114";
        public static final String FREE_NECESSARY_MODI_PEPPER = "1000115";
        public static final String FREE_NECESSARY_MODI_GARLIC = "1000420";

        public static final String ABISTA_ZERO_PRICE = "1000283";
        public static final String ABISTA_PAID_NECESSART_MODI = "1000308";
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

        public static String rqParamsDeletePosition(String domen, String guid, String uni, int quantity) {

            return "{\n" +
                    "  \"domen\": \"" + domen + "\",\n" +
                    "  \"guid\": \"" + guid + "\",\n" +
                    "  \"station\": 1,\n" +
                    "  \"uni\": \"" + uni + "\",\n" +
                    "  \"quantity\": " + quantity + "\n" +
                    "}";

        }

        public static String rqParamsCheckPrePayment(String transactionId){

            return "{\n" +
                    "  \"transaction_id\": \"" + transactionId + "\"\n" +
                    "}";

        }

        static ApiRKeeper apiRKeeper = new ApiRKeeper();
        public static ArrayList<LinkedHashMap<String, Object>> allTypesModificatorList = new ArrayList<>() {
            {
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,1,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SALT,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,1,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SALT,2));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,2,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SALT,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,1,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_PEPPER,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,1,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_PEPPER,2));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,2,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_PEPPER,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,2,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SALT,1));
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_PEPPER,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,2,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SALT,2));
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_PEPPER,2));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(XOLODEC,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NON_NECESSARY_MODI_BUTTER,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(XOLODEC,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NON_NECESSARY_MODI_BUTTER,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(XOLODEC,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NON_NECESSARY_MODI_MAYONES,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(XOLODEC,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NON_NECESSARY_MODI_MAYONES,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(XOLODEC,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NON_NECESSARY_MODI_BUTTER,1));
                        add(apiRKeeper.createModificatorObject(FREE_NON_NECESSARY_MODI_MAYONES,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(CAESAR,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_BANAN_SIROP,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(CAESAR,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_BANAN_SIROP,2));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(CAESAR,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_BANAN_SIROP,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(CAESAR,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_KARAMEL_SIROP,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(CAESAR,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_KARAMEL_SIROP,2));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(CAESAR,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_KARAMEL_SIROP,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(CAESAR,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_BANAN_SIROP,1));
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_KARAMEL_SIROP,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(CAESAR,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_BANAN_SIROP,2));
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_KARAMEL_SIROP,2));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(RAGU,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MODI_SOUS,1));
                    }
                }));

                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(RAGU,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MODI_SOUS,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(RAGU,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MODI_SALAT,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(RAGU,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MODI_SALAT,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(RAGU,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MODI_SOUS,1));
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MODI_SALAT,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(VODKA,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MIX_MODI_SALO,1));
                    }
                }));

                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(VODKA,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MIX_MODI_SALO,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(VODKA,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MIX_MODI_BREAD,1));
                    }
                }));

                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(VODKA,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MIX_MODI_BREAD,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(VODKA,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MIX_MODI_SALO,1));
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MIX_MODI_BREAD,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(PASTA,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SOUS,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(PASTA,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SOUS,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(PASTA,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_BACON,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(PASTA,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_BACON,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(PASTA,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SOUS,1));
                        add(apiRKeeper.createModificatorObject(PAID_NECESSARY_MODI_BACON,1));
                    }
                }));

                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(GOVYADINA_PORTION,2, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_MODI_SOLT_ZERO_PRICE,1));
                        add(apiRKeeper.createModificatorObject(PAID_MODI_KARTOFEL_FRI,2));
                        add(apiRKeeper.createModificatorObject(PAID_MODI_SOUS,1));
                    }
                }));

            }

        };

    }

}
