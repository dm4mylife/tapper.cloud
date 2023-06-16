package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;


@SelectPackages({

    "tapper.tests.keeper._1_1_common",
    "tapper.tests.keeper._1_2_full_payment",
    "tapper.tests.keeper._1_3_tips",
    "tapper.tests.keeper._1_4_zero_price_dish",
    "tapper.tests.keeper._4_2_add_and_remove_dishes"


})
@Suite
class _1_ThreadTestSuite {}
