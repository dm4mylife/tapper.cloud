package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@SelectPackages({

    "tapper.tests.keeper_e2e._4_1_discount",
    "tapper.tests.keeper_e2e._4_2_add_and_remove_dishes"


})
@Suite
class _4_ThreadTestSuite { }
