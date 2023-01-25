package tapper.tests;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import tapper.tests.keeper_e2e._2_partialPayment._2_0_TipsScTest;
import tapper.tests.keeper_e2e._2_partialPayment._2_1_TipsNoScTest;


@SelectPackages({

        "tapper.tests.keeper_e2e._0_common",
        "tapper.tests.keeper_e2e._1_fullPayment",
        "tapper.tests.keeper_e2e._2_partialPayment",
        "tapper.tests.keeper_e2e._3_tips"

})
@Suite
@SuiteDisplayName("full test")
public class SecondThreadTestSuite {


}
