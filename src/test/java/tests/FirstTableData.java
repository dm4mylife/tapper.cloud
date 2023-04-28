package tests;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static api.ApiData.OrderData.*;
import static data.Constants.TestData.TapperTable.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface FirstTableData {
      String restaurantName() default R_KEEPER_RESTAURANT;
      String tableCode() default TABLE_CODE_111;
      String waiter() default WAITER_ROBOCOP_VERIFIED_WITH_CARD;
      String apiUri() default AUTO_API_URI;
      String tableUrl() default STAGE_RKEEPER_TABLE_111;
      String tableId() default TABLE_AUTO_111_ID;

}
