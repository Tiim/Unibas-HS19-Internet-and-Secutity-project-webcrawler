package ch.unibas.ias.webcrawler;

import ch.unibas.ias.webcrawler.database.DBConnection;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Statistics {

  // SELECT cms, STAT_WP_ADMIN, STAT_STRONG_PASSWORD, STAT_HUMANITY, STAT_JETPACK, STAT_RECAPTCHA FROM page WHERE STAT_WP_ADMIN = true
  // SELECT cms, STAT_WP_ADMIN, STAT_STRONG_PASSWORD, STAT_HUMANITY, STAT_JETPACK, STAT_RECAPTCHA FROM page WHERE cms LIKE 'Wordpress' AND STAT_WP_ADMIN = true

  private static final String OUTPUT_PATH = "./Präsentation";

  private DBConnection connection;

  public Statistics() throws Exception {
    connection = new DBConnection();
  }


  public static void main(String[] argv) throws Exception {
    Statistics stats = new Statistics();
    stats.compute();
  }

  private void compute() throws SQLException {
    countCmsAndVersions();
    countWordpressPlugins();
    queueLength();
    countCMS();

  }

  private void countCMS() throws SQLException {
    try (CSV c = new CSV("count_cms")) {

      ResultSet rs = connection.newStatement("SELECT cms, count(*) as c FROM page GROUP BY cms ORDER BY c DESC").executeQuery();
      c.write("cms", "count");
      while (rs.next()) {
        c.write(rs.getString(1), rs.getString(2));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void countWordpressPlugins() throws SQLException {
    try (CSV c = new CSV("wordpress_plugins")) {

      ResultSet rs = connection.newStatement("SELECT count(*) as c, stat_wp_admin, stat_strong_password, stat_humanity, stat_jetpack, stat_recaptcha " +
              "FROM page WHERE cms LIKE 'Wordpress' GROUP BY " +
              "stat_wp_admin, stat_strong_password, stat_humanity, stat_jetpack, stat_recaptcha " +
              "ORDER BY c DESC").executeQuery();
      c.write("Admin", "Strong Pass", "Humanity", "Jetpack", "Captcha", "Count");
      while (rs.next()) {
        c.write(
                rs.getBoolean(2),
                rs.getBoolean(3),
                rs.getBoolean(4),
                rs.getBoolean(5),
                rs.getBoolean(6),
                rs.getInt(1)
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void countCmsAndVersions() throws SQLException {
    try (CSV c = new CSV("versions")) {
      ResultSet rs = connection.newStatement("SELECT cms, version, count(*) as c FROM page GROUP BY cms, version ORDER BY c DESC").executeQuery();
      c.write("Cms", "Version", "Count");
      while (rs.next()) {
        c.write(
                rs.getString(1),
                rs.getString(2),
                rs.getInt(3)
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void queueLength() throws SQLException {
    ResultSet done = connection.newStatement("SELECT count(*) FROM queue WHERE crawled = true").executeQuery();
    ResultSet notYetDone = connection.newStatement("SELECT count(*) FROM queue WHERE crawled = false").executeQuery();

    try (CSV c = new CSV("queue")) {
      c.write("done", "in_queue");
      done.next();
      notYetDone.next();
      c.write(done.getInt(1), notYetDone.getInt(1));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private class CSV implements Closeable {
    private Writer os;

    public CSV(String name) {
      File f = new File(OUTPUT_PATH + "/" + name + ".csv");
      try {
        os = new FileWriter(f);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void write(Object... l) throws IOException {
      String line = Stream.of(l)
              .map(this::escapeSpecialCharacters)
              .collect(Collectors.joining(";"));
      os.write(line + "\n");

    }

    public String escapeSpecialCharacters(Object d) {
      String data = d.toString();
      String escapedData = data.replaceAll("\\R", " ");
      if (data.contains(",") || data.contains("\"") || data.contains("'")) {
        data = data.replace("\"", "\"\"");
        escapedData = "\"" + data + "\"";
      }
      return escapedData;
    }

    @Override
    public void close() throws IOException {
      os.close();
    }
  }
}
