import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Portfolio {

    public void createTable() {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            st.execute("CREATE TABLE IF NOT EXISTS stocks (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "quantity INTEGER," +
                    "buy_price REAL," +
                    "current_price REAL," +
                    "position TEXT)");

            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStock(Stock s) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO stocks(name, quantity, buy_price, current_price, position) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, s.getName());
            ps.setInt(2, s.getQuantity());
            ps.setDouble(3, s.getBuyPrice());
            ps.setDouble(4, s.getCurrentPrice());
            ps.setString(5, s.getPosition()); 

            int rows = ps.executeUpdate();
            System.out.println("Inserted rows: " + rows);

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sellStock(String name, int sellQty) {
        try {
            Connection con = DBConnection.getConnection();

            // Get existing stock
            PreparedStatement ps = con.prepareStatement("SELECT * FROM stocks WHERE name=?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int currentQty = rs.getInt("quantity");
                int newQty = currentQty - sellQty;

                String position = newQty >= 0 ? "LONG" : "SHORT";

                PreparedStatement update = con.prepareStatement(
                        "UPDATE stocks SET quantity=?, position=? WHERE name=?");
                update.setInt(1, newQty);
                update.setString(2, position);
                update.setString(3, name);
                update.executeUpdate();

                if (sellQty > currentQty) {
                    JOptionPane.showMessageDialog(null, "Selling more than owned → Short position!");
                }

            } else {
                // No stock exists → direct short
                PreparedStatement insert = con.prepareStatement(
                        "INSERT INTO stocks(name, quantity, buy_price, current_price, position)\r\n" + //
                                "VALUES (?, ?, ?, ?, ?)");
                insert.setString(1, name);
                insert.setInt(2, -sellQty);
                insert.setDouble(3, 0);
                insert.setDouble(4, 0);
                insert.setString(5, "SHORT");
                insert.executeUpdate();

                System.out.println("⚠ No stock → SHORT position opened");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Stock> getStocks() {
        ArrayList<Stock> list = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM stocks");

            while (rs.next()) {
                String name = rs.getString("name");
                int qty = rs.getInt("quantity");
                double buy = rs.getDouble("buy_price");
                double current = rs.getDouble("current_price");

                System.out.println("Fetched: " + name);

                String position = rs.getString("position");

                list.add(new Stock(name, qty, buy, current, position));
            }

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void removeStock(String name) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement("DELETE FROM stocks WHERE name = ?");
            ps.setString(1, name);

            ps.executeUpdate();

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}