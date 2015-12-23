import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class createdb {

    private static String DRIVERDD = "org.postgresql.Driver";
    private static String nomdb = "jdbc:postgresql://192.168.1.111:5432/pokehibernate";

    public static void main(String[] args) {

        //createDB("ss");
        createTablePoke();
        createTableTipe();
        createRelation_Tipe_Poke();
        System.out.println("FI");
    }

    public static void createDB (String nom) {
        Connection c = null;

        try {
            Class.forName(DRIVERDD);
            c = DriverManager.getConnection(nomdb,"marc","marc");
            System.out.println("Created database successfully");
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return;
    }

    public static void createTablePoke(){

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVERDD);
            c = DriverManager.getConnection(nomdb,"marc","marc");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE POKEMONS " +
                    "(IDPok INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public static void createTableTipe(){

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVERDD);
            c = DriverManager.getConnection(nomdb,"marc","marc");
            stmt = c.createStatement();
            String sql = "CREATE TABLE TIPOS " +
                    "(IDTy INT PRIMARY KEY     NOT NULL," +
                    " Tipus           TEXT    NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public static void createRelation_Tipe_Poke(){

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVERDD);
            c = DriverManager.getConnection(nomdb,"marc","marc");
            stmt = c.createStatement();
            String sql = "CREATE TABLE POKE_TIPO " +
                    "(IDP        INT    NOT NULL,"+
                    " IDT         INT    NOT NULL," +
                    " PRIMARY KEY (IDP, IDT) )";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
}
