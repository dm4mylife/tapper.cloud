package tapper.tests;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@SelectPackages({

        "tapper.tests.screenshots_comparison"

})
@Suite
@SuiteDisplayName("makeOriginalScreenshotTest")
public class MakeOriginalScreenshotTest {

}