package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@SelectPackages({

    "tapper.tests.keeper_e2e._1_1_common",
    "tapper.tests.keeper_e2e._1_2_full_payment",
    "tapper.tests.keeper_e2e._1_3_tips",
        "tapper.tests.iiko_e2e._6_0_common",
        "tapper.tests.iiko_e2e._6_1_full_payment",
        "tapper.tests.iiko_e2e._6_2_part_payment",
        "tapper.tests.iiko_e2e._6_3_add_and_remove_dishes",
        "tapper.tests.iiko_e2e._6_4_stress"

})
@Suite
class _1_ThreadTestSuite {}
