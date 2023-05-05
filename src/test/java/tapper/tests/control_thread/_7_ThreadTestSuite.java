package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@SelectPackages({

        "tapper.tests.iiko_e2e._7_1_modifiers",
        "tapper.tests.iiko_e2e._8_1_discount"

})
@Suite
@SuiteDisplayName("e2e tests")
public class _7_ThreadTestSuite {


}
