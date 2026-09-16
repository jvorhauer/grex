package grex.orm;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import grex.control.Option;

public class SourceConfig {
  private static Config config  = ConfigFactory.load();

  private SourceConfig() {}

  public static Option<String> getString(final String key) {
    return config.hasPath(key) ? Option.of(config.getString(key)) : Option.none();
  }

  public static Option<Integer> getInt(final String key) {
    return config.hasPath(key) ? Option.of(config.getInt(key)) : Option.none();
  }
}
