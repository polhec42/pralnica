package database;

import utilities.AlertBox;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Connect {

    //static String url = "jdbc:sqlite:" + Connect.class.getResource("/") + "test.db";
    //static String url =  Connect.class.getClassLoader().getResource("/test.db").toExternalForm();


    /*public static void createNewDatabase(String fileName) {

        System.out.println(url);
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/

    public static Connection connect(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            // db parameters

            // create a connection to the database
            conn = DriverManager.getConnection("jdbc:sqlite:resources/test.db");

            //System.out.println("Connection to SQLite has been established.");

        } catch (Exception e) {
            AlertBox.error(e.getMessage());
        }
        return conn;
    }

    public void insertVolunteer(String name) {
        String sql = "INSERT INTO volunteers(name) VALUES(?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }
    }

    public void insertUser(String cypher, String name) {
        String sql = "INSERT INTO users(sifra, name) VALUES(?,?)";

        ArrayList<User> users = getUsers();

        for(User u : users){
            if(u.getCypher().equals(cypher)){
                AlertBox.error("Uporabnik s to cifro že obstaja!");
                return;
            }
        }

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cypher);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }
    }

    public ArrayList<Volunteer> getVolunteers(){
        ArrayList<Volunteer> result = new ArrayList<>();
        String sql = "SELECT * FROM volunteers";

        try {
            Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            ResultSetMetaData metadata = rs.getMetaData();
            int numberOfColumns = metadata.getColumnCount();

            while (rs.next()) {
                result.add(new Volunteer(rs.getString("name"), rs.getInt("id")));
            }
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }

        return result;
    }

    public ArrayList<User> getUsers(){
        ArrayList<User> result = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY sifra";

        try {
            Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            ResultSetMetaData metadata = rs.getMetaData();
            int numberOfColumns = metadata.getColumnCount();

            while (rs.next()) {
                result.add(new User(rs.getString("sifra"), rs.getString("name"), rs.getInt("id")));
            }
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }

        return result;
    }

    public int createNewOrder(String volunteerName, String cypher, LocalDate date){

        int returnedId = 0;

        int volunteerId = 0;
        int userId = 0;

        ArrayList<Volunteer> volunteers = getVolunteers();

        for(Volunteer v : volunteers){
            if(v.getName().equals(volunteerName)){
                volunteerId = v.getId();
            }
        }

        ArrayList<User> users = getUsers();

        for(User u : users){
            if(u.getCypher().equals(cypher)){
                userId = u.getId();
            }
        }

        String sql = "INSERT INTO newOrder(volunteerId, usersId, dateOfOrder) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"id"})) {
            pstmt.setInt(1, volunteerId);
            pstmt.setInt(2, userId);
            pstmt.setDate(3, Date.valueOf(date));
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            returnedId = rs.getInt(1);
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }

        return returnedId;
    }

    public void createNewCloth(int orderId, Cloth c){

        String sql = "INSERT INTO cloth(ofOrder, cloth, category, color, detail, done, taken, payed) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setString(2, c.getCloth());
            pstmt.setString(3, c.getCategory());
            pstmt.setString(4, c.getColor());
            pstmt.setString(5, c.getDetail());
            pstmt.setInt(6, c.getDone());
            pstmt.setInt(7, c.getTaken());
            pstmt.setInt(8, c.getPayed());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }
    }

    public void selectAll(String table){
        String sql = "SELECT * FROM "+ table +"";

        try {
            Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            ResultSetMetaData metadata = rs.getMetaData();
            int numberOfColumns = metadata.getColumnCount();

            while (rs.next()) {
                for(int i = 1; i <= numberOfColumns; i++){
                    System.out.print(rs.getString(i) + " ");
                }
            }
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }
    }
/*
    public static void createNewTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE volunteers (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }
    }*/

    public int numberOfClothesInOrder(int orderId){

        int result = 0;

        String sql = "SELECT COUNT(id) FROM cloth WHERE ofOrder = "+ orderId+ "";

        try {
            Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            ResultSetMetaData metadata = rs.getMetaData();

            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }

        return  result;
    }

    public ArrayList<Cloth> getClothesOfOrder(int orderId){

        ArrayList<Cloth> result = new ArrayList<>();

        String sql = "SELECT * FROM cloth WHERE ofOrder = "+ orderId+ "";

        try {
            Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            while (rs.next()) {
                Cloth c = new Cloth(rs.getString("cloth"), rs.getString("category"), rs.getString("color"), rs.getString("detail"));
                c.setId(rs.getInt("id"));
                c.setDone(rs.getInt("done"));
                c.setPayed(rs.getInt("payed"));
                c.setTaken(rs.getInt("taken"));
                result.add(c);
            }
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }

        return  result;
    }

    public ArrayList<Order> getOrders(int archived){

        ArrayList<Order> orders = new ArrayList<>();

        ArrayList<User> users = getUsers();
        ArrayList<Volunteer> volunteers = getVolunteers();

        String sql = "SELECT * FROM newOrder";

        try {
            Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            while (rs.next()) {

                String cypher = "";
                String volunteer = "";

                int userId = rs.getInt("usersId");
                int volunteerId = rs.getInt("volunteerId");

                for(User u : users){
                    if(u.getId() == userId){
                        cypher = u.getCypher();
                    }
                }

                for(Volunteer v : volunteers){
                    if(v.getId() == volunteerId){
                        volunteer = v.getName();
                    }
                }

                Order o = new Order(
                    cypher, volunteer, numberOfClothesInOrder(rs.getInt("id")), rs.getDate("dateOfOrder").toLocalDate(), rs.getInt("done")
                );
                o.setId(rs.getInt("id"));
                o.setArchived(rs.getInt("archived"));

                if(o.getArchived() != archived){
                    continue;
                }

                orders.add(o);
            }
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }

        return orders;
    }

    public void updateCloth(Cloth c){
        String sql = "UPDATE cloth SET cloth = ? , category = ?, color = ? , detail = ? , done = ? , taken = ? , payed = ? WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getCloth());
            pstmt.setString(2, c.getCategory());
            pstmt.setString(3, c.getColor());
            pstmt.setString(4, c.getDetail());
            pstmt.setInt(5, c.getDone());
            pstmt.setInt(6, c.getTaken());
            pstmt.setInt(7, c.getPayed());
            pstmt.setInt(8, c.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }
    }

    public void archiveOrder(int id, boolean value){
        int insertedValue = value ? 1 : 0;

        String sql = "UPDATE newOrder SET archived = ? WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, insertedValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }
    }

    public ArrayList<Order> getFilteredOrders(String category, String key, int archived) {

        ArrayList<Order> orders = null;

        if(category != "Obleka"){

            orders = getOrders(archived);

            for(int i = 0; i < orders.size(); i++){
                switch (category){
                    case "Šifra":{
                        if(!orders.get(i).getCypher().equals(key)){
                            orders.remove(i);
                            i--;
                        }
                        break;
                    }
                    case "Prostovoljec":{
                        if(!orders.get(i).getVolunteer().equals(key)){
                            orders.remove(i);
                            i--;
                        }
                        break;
                    }
                }
            }
        }else{

            HashSet<Integer> orderIDs = new HashSet<>();

            String sql = "SELECT * FROM cloth WHERE cloth = ?";

            try {

                Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, key);
                ResultSet rs = pstmt.executeQuery();

                while(rs.next()){
                    orderIDs.add(rs.getInt("ofOrder"));
                }

                orders = getFilteredOrders(orderIDs, archived);

            }catch (SQLException e){
                AlertBox.error(e.getMessage());
            }

        }
        return orders;
    }

    public ArrayList<Order> getFilteredOrders(Collection c, int archived){

        ArrayList<Order> orders = getOrders(archived);

        for(int i = 0; i < orders.size(); i++){
            if(!c.contains(orders.get(i).getId())){
                orders.remove(i);
                i--;
            }
        }

        return orders;
    }

    public ArrayList<Order> getFilteredOrders(LocalDate dateFrom, LocalDate dateTo, int archived){

        ArrayList<Order> orders = new ArrayList<>();

        ArrayList<User> users = getUsers();
        ArrayList<Volunteer> volunteers = getVolunteers();

        String sql = "SELECT * FROM newOrder WHERE dateOfOrder >= ? AND dateOfOrder <= ?";

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(dateFrom));
            pstmt.setDate(2, Date.valueOf(dateTo));
            ResultSet rs    = pstmt.executeQuery();

            while (rs.next()) {

                String cypher = "";
                String volunteer = "";

                int userId = rs.getInt("usersId");
                int volunteerId = rs.getInt("volunteerId");

                for(User u : users){
                    if(u.getId() == userId){
                        cypher = u.getCypher();
                    }
                }

                for(Volunteer v : volunteers){
                    if(v.getId() == volunteerId){
                        volunteer = v.getName();
                    }
                }

                Order o = new Order(
                        cypher, volunteer, numberOfClothesInOrder(rs.getInt("id")), rs.getDate("dateOfOrder").toLocalDate(), rs.getInt("done")
                );
                o.setId(rs.getInt("id"));
                o.setArchived(rs.getInt("archived"));

                if(o.getArchived() != archived){
                    continue;
                }

                orders.add(o);
            }
        } catch (SQLException e) {
            AlertBox.error(e.getMessage());
        }

        return orders;

    }

    public void deleteOrders(ArrayList<Order> orders){

        for(Order o : orders){

            String sql = "DELETE FROM newOrder WHERE id = ?";

            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, o.getId());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                AlertBox.error(e.getMessage());
            }
        }
    }
}
