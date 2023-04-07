package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@SelectPackages({

    "tapper.tests.keeper_e2e._1_1_common",
    "tapper.tests.keeper_e2e._1_2_fullPayment",
    "tapper.tests.keeper_e2e._1_3_tips"



})
@Suite
@SuiteDisplayName("full test")
public class _1_ThreadTestSuite {


}
