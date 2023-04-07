package data.table_data_annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface FifthTableData {
      String restaurantName() default R_KEEPER_RESTAURANT;
      String tableCode() default TABLE_CODE_555;
      String waiter() default WAITER_ROBOCOP_VERIFIED_WITH_CARD;
      String apiUri() default AUTO_API_URI;
      String tableUrl() default STAGE_RKEEPER_TABLE_555;
      String tableId() default TABLE_AUTO_555_ID;

}