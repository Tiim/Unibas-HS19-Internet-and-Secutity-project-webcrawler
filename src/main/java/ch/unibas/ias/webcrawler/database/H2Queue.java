package ch.unibas.ias.webcrawler.database;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Queue implements UrlQueue {

    private PreparedStatement pushStmt;
    private PreparedStatement existsStmt;
    private PreparedStatement pollStmt;
    private PreparedStatement countStmt;

    private int index = 0;
    private DBConnection conn;

    public H2Queue(DBConnection conn) {
        this.conn = conn;
        try {
            pushStmt = conn.newStatement("INSERT INTO queue (url) VALUES (?)");
            existsStmt = conn.newStatement("SELECT url FROM queue WHERE url = ?");
            pollStmt = conn.newStatement("SELECT url FROM queue WHERE id = ?");
            countStmt = conn.newStatement("SELECT count(*) FROM queue");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        index = Integer.parseInt(conn.getSetting("queue.index", "0"));
    }

    @Override
    public void push(URL url) {
        String u = url.toString();
        try {
            existsStmt.setString(1, u);
            ResultSet rs = existsStmt.executeQuery();

            // URL Already in Queue
            if (rs.next()) {
                return;
            }

            pushStmt.setString(1, u);
            pushStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int size() {
        try {
            ResultSet rs = countStmt.executeQuery();
            rs.next();
            return rs.getInt(1) - index;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int crawled() {
        return index;
    }

    @Override
    public URL poll() {
        conn.setSetting("queue.index", String.valueOf(++index));
        try {
            pollStmt.setInt(1, index);
            ResultSet rs = pollStmt.executeQuery();
            rs.next();
            return new URL(rs.getString(1));
        } catch (SQLException | MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
}
