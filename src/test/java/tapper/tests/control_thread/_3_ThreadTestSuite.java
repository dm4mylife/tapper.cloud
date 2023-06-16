package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;


@SelectPackages({

    "tapper.tests.keeper._3_1_waiter",
    "tapper.tests.keeper._3_2_modifiers",
    "tapper.tests.keeper._3_3_part_payment",
    "tapper.tests.keeper._3_4_portions"

})
@Suite
class _3_ThreadTestSuite {}
