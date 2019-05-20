package ch.unibas.ias.webcrawler;

import ch.unibas.ias.webcrawler.database.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Statistics {

  private DBConnection connection;

  public Statistics () throws Exception {
    connection = new DBConnection();
  }


  public static void main(String[] argv) throws Exception {
    Statistics stats = new Statistics();
    stats.compute();
  }

  private void compute() throws SQLException {
    countCmsAndVersions();
  }

  private void countCmsAndVersions() throws SQLException {
    ResultSet rs = connection.newStatement("SELECT cms, version, count(*) FROM page GROUP BY cms, version").executeQuery();
    while (rs.next()) {
      System.out.println(rs.getString(1) + " | " + rs.getString(2) + " --> " + rs.getInt(3));
    }
  }
}
