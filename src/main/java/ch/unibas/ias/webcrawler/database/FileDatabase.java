package ch.unibas.ias.webcrawler.database;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class FileDatabase implements Database {

  private static final String FILE = "database.txt";

  private final FileWriter output;

  public FileDatabase() throws IOException {
    output = new FileWriter(FILE);
  }

  @Override
  public void addRecord(String url, String html, String httpHeaders, Date downloadDate) {
    StringBuilder b = new StringBuilder();

    b.append(escape(url)).append(';').append(escape(html)).append(';').append(escape(httpHeaders)).append(';')
        .append(escape(downloadDate.toString()));
    try {
      output.write(b.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String escape(String v) {
    return v.replaceAll(";", "\\;").replaceAll("\\", "\\\\").replaceAll("\n", "\\n").replaceAll("\r", "\\r");
  }
}