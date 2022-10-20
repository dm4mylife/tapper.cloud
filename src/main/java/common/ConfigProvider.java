package common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface ConfigProvider {

    Config config = readConfig();

    static Config readConfig() {

        return ConfigFactory.load("data.cong");
    }



}
