package grex.orm;

import grex.control.Option;
import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

class RepositoryTest {

  @Test
  void config() {
    final Option<String> url = SourceConfig.getString("db.url");
    expect(url).toBeDefined();
    expect(url).toContain("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
  }

  @Test
  void connect() {
    Repository<String> repo = new Repository<>();
    var conn = repo.connect();
    expect(conn).toBeDefined();

    var name = repo.getDatabaseName();
    expect(name).toBeDefined();
    expect(name).toContain("H2");

    repo.query();
  }
}
