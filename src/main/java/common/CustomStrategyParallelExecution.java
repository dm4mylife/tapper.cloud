package common;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;


public class CustomStrategyParallelExecution implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {
    @Override
    public int getParallelism() {
        return 4;
    }

    @Override
    public int getMinimumRunnable() {
        return 4;
    }

    @Override
    public int getMaxPoolSize() {
        return 4;
    }

    @Override
    public int getCorePoolSize() {
        return 4;
    }

    @Override
    public int getKeepAliveSeconds() {
        return 50;
    }

    @Override
    public ParallelExecutionConfiguration createConfiguration(final ConfigurationParameters configurationParameters) {
        return this;
    }
}

