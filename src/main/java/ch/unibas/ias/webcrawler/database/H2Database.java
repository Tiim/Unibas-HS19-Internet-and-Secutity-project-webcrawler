package ch.unibas.ias.webcrawler.database;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

public class H2Database  implements Database{

    private PreparedStatement insertStmt;

    public H2Database(DBConnection conn) {
        try {
            insertStmt = conn.newStatement("INSERT INTO page (url, html, header, date) VALUES (?,?,?,?)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addRecord(String url, String html, String httpHeaders, Date downloadDate) {
        try {
            insertStmt.setString(1, url);
            insertStmt.setCharacterStream(2, new StringReader(html));
            insertStmt.setString(3, httpHeaders);
            insertStmt.setTimestamp(4, new Timestamp(downloadDate.getTime()));
            insertStmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
