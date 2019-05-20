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
    queueLength();
  }

  private void countCmsAndVersions() throws SQLException {
    System.out.println("CMS AND VERSIONS");
    System.out.println("================");
    ResultSet rs = connection.newStatement("SELECT cms, version, count(*) as c FROM page GROUP BY cms, version ORDER BY c DESC").executeQuery();
    System.out.println("Cms, Version, Count");
    while (rs.next()) {
      System.out.println(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getInt(3));
    }
  }

  private void queueLength() throws SQLException {
    ResultSet done = connection.newStatement("SELECT count(*) FROM queue WHERE crawled = true").executeQuery();
    ResultSet notYetDone = connection.newStatement("SELECT count(*) FROM queue WHERE crawled = false").executeQuery();

    done.next();
    notYetDone.next();
    System.out.println("QUEUE");
    System.out.println("================");
    System.out.println("Done: " + done.getInt(1));
    System.out.println("Not Done: " + notYetDone.getInt(1));

  }
}
