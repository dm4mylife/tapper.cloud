package tapper.tests.control_thread;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@SelectPackages({

    "tapper.tests.keeper._2_1_sockets",
    "tapper.tests.keeper._2_2_stress",
    "tapper.tests.screenshots_comparison",
        "tapper.tests.critical_tests"

})
@Suite
class _2_ThreadTestSuite { }
