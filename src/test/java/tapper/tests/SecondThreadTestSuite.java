package tapper.tests;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@SelectPackages({

        "tapper.tests.keeper_e2e._0_common",
        "tapper.tests.keeper_e2e._1_fullPayment",
        "tapper.tests.keeper_e2e._2_partialPayment",


})
@Suite
@SuiteDisplayName("full test")
public class SecondThreadTestSuite {


}
