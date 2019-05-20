package ch.unibas.ias.webcrawler.database;

import java.sql.*;

public class DBConnection {

  private Connection connection;
  private PreparedStatement updateMetaStmt;
  private PreparedStatement getMetaStmt;

  public DBConnection() throws Exception {
    Class.forName("org.h2.Driver");
    connection = DriverManager.getConnection("jdbc:h2:./test", "sa", "");
    init();
  }

  private void init() throws SQLException {

    String compress = "SET COMPRESS_LOB DEFLATE";

    String tablePage = "CREATE TABLE IF NOT EXISTS page (" +
            "id IDENTITY auto_increment PRIMARY KEY," +
            "url VARCHAR(500) UNIQUE," +
            "html CLOB," +
            "header VARCHAR(2048)," +
            "date TIMESTAMP," +
            // Stats:
            "cms VARCHAR(50)," +
            "version VARCHAR(50)," +
            "stat_wp_admin BOOLEAN," +
            "stat_strong_password BOOLEAN," +
            "stat_humanity BOOLEAN," +
            "stat_jetpack BOOLEAN," +
            "stat_recaptcha BOOLEAN" +
            ")";

    String tableQueue = "CREATE TABLE IF NOT EXISTS queue (" +
            "id IDENTITY auto_increment PRIMARY KEY," +
            "crawled BOOLEAN," +
            "url VARCHAR(500) UNIQUE)";

    String tableMeta = "CREATE TABLE IF NOT EXISTS meta (" +
            "key VARCHAR(100) PRIMARY KEY," +
            "value VARCHAR(200))";

    connection.createStatement().execute(compress);
    Statement stmt = connection.createStatement();
    stmt.execute(tablePage);
    Statement stmt2 = connection.createStatement();
    stmt2.execute(tableQueue);
    Statement stmt3 = connection.createStatement();
    stmt3.execute(tableMeta);

    String update = "MERGE INTO meta KEY (key) VALUES (?, ?)";
    updateMetaStmt = connection.prepareStatement(update);
    String get = "SELECT value FROM meta WHERE key = ?";
    getMetaStmt = connection.prepareStatement(get);
  }

  public void setSetting(String key, String value) {
    try {
      updateMetaStmt.setString(1, key);
      updateMetaStmt.setString(2, value);
      updateMetaStmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public String getSetting(String key, String def) {
    try {
      getMetaStmt.setString(1, key);
      ResultSet rs = getMetaStmt.executeQuery();
      if (rs.next()) {
        return rs.getString(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return def;
  }

  public PreparedStatement newStatement(String sql) throws SQLException {
    return connection.prepareStatement(sql);
  }
}
