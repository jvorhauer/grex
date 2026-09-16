package grex.orm;

import grex.control.Option;
import grex.control.Try;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

@SuppressWarnings("Convert2MethodRef")
public class Repository<T> {

  private Option<Connection> oconn;
  private Option<DatabaseMetaData> odbm;

  public Option<DatabaseMetaData> connect() {
    String url = SourceConfig.getString("db.url").getOrElse("jdbc:h2:mem:test");
    oconn = Try.of(() -> DriverManager.getConnection(url)).toOption();
    odbm = oconn.flatMap(conn -> Try.of(() -> conn.getMetaData()).toOption());
    return odbm;
  }

  public Option<String> getDatabaseName() {
    return odbm.flatMap(dbm -> Try.of(() -> dbm.getDatabaseProductName()).toOption());
  }

  public void query() {
    oconn.forEach(conn -> Try.of(() -> {
      var schema = conn.getSchema();
      System.out.println("schema: " + schema);
      return null;
    }));
  }
}
