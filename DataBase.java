package banking;

import java.sql.*;

public class DataBase {

    private String url;

    public DataBase(String fileName){
        this.url = "jdbc:sqlite:" + fileName;
    }

    //Подключение к базе данных
    private Connection connect() {
        // SQLite connection string

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    //Создание нового файла БД, если его ещё нет. Для тестов не нужен
    public void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn == null) {
                System.out.println("A new database NOT been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Создание таблицы в базе данных
    public void createNewTable() {

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "    id INTEGER,\n"
                + "    number TEXT,\n"
                + "    pin TEXT,\n"
                + "    balance INTEGER DEFAULT 0\n"
                + ");";
//Это создание крутой таблицы с автоинкрементом id, но тест с ней не проходит
//        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
//                + "    id integer PRIMARY KEY ASC,\n"
//                + "    number text NOT NULL,\n"
//                + "    pin text NOT NULL,\n"
//                + "    balance INTEGER DEFAULT 0\n"
//                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Вносим данные в таблицу базы
    public void insert(String cardNumber, String cardPin, int balance) {

        String sql = "INSERT INTO card(number,pin,balance) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, cardPin);
            pstmt.setInt(3, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Просмотр содержимого базы, сделано для удобства тестирования
    public void selectAll() {
        String sql = "SELECT id, number, pin, balance FROM card";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("number") + "\t" +
                        rs.getString("pin") + "\t" +
                        rs.getInt("balance"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Запрос на номер карты из БД
    public BankCard selectCard(String cardNumber, String cardPin) {
        BankCard result  = new BankCard();
        String resultNumber = "";
        String resultPin = "";
        int resultBalance = 0;
        String sql = "SELECT number, pin, balance FROM card WHERE number=" + cardNumber
                + " AND pin=" + cardPin;

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                resultNumber = rs.getString("number");
                resultPin = rs.getString("pin");
                resultBalance = rs.getInt("balance");
                System.out.println("selectCard() = " + resultNumber + "\n pin=" + rs.getString("pin"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        result.setCardNumber(resultNumber);
        result.setCardPin(resultPin);
        result.setBalance(resultBalance);
        return result;
    }
}
