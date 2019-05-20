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
    private PreparedStatement setPolledStmt;
    private PreparedStatement countStmt;
    private PreparedStatement doneCountStmt;

    private DBConnection conn;

    public H2Queue(DBConnection conn) {
        this.conn = conn;
        try {
            pushStmt = conn.newStatement("INSERT INTO queue (url, crawled) VALUES (?, false)");
            existsStmt = conn.newStatement("SELECT url FROM queue WHERE url = ?");
            pollStmt = conn.newStatement("SELECT url FROM queue WHERE crawled = false LIMIT 1");
            setPolledStmt = conn.newStatement("UPDATE queue SET crawled = true WHERE url = ?");
            countStmt = conn.newStatement("SELECT count(*) FROM queue WHERE crawled = false");
            doneCountStmt = conn.newStatement("SELECT count(*) FROM queue WHERE crawled = true");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

            //System.out.println("Add url " + url + " to queue");
            pushStmt.setString(1, u);
            pushStmt.execute();
        } catch (SQLException e) {
          System.out.println("Warning: " + e.getMessage());
        }
    }

    @Override
    public int size() {
        try {
            ResultSet rs = countStmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int crawled() {
        try {
            ResultSet rs = doneCountStmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public URL poll() {
        try {
            ResultSet rs = pollStmt.executeQuery();
            rs.next();
            String url = rs.getString(1);

            setPolledStmt.setString(1, url);
            setPolledStmt.execute();

            return new URL(url);
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
