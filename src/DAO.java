

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


import java.io.Serializable;
import java.sql.*;


public class DAO {


    private static String DRIVERDD = "org.postgresql.Driver";
    private static String nomdb = "jdbc:postgresql://192.168.1.111:5432/pokehibernate";

    public static SessionFactory factory;

    /*
     * DATA ACCES OBJECT és un objecte que emmascara
     * l'acces a les bases de dades.
     * Serà materia de la UF4,
     * però us recomano que comenceu a passar.
     *
     */


    /**
     *  Aquesta funció insereix el pokemon. No tocar!
     * @param k
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    public static void insert_Poke(Pk k) throws SQLException, ClassNotFoundException {

        factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = null;
        Integer pkid = null;
        try{
            tx = session.beginTransaction();
            pkid = (Integer)session.save(k);
            tx.commit();
        }catch (HibernateException e){
            if(tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        System.out.println(pkid);
    }

    /**
     *
     * @param tipe: Tipus de pokemon que es vol comprovar si existeix
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    public static boolean exist_Tipe(int tipe) throws SQLException, ClassNotFoundException {

        Connection c = DriverManager.getConnection(nomdb,"marc","marc");
        Statement stmt = null;

        int variable = 0;
        Class.forName(DRIVERDD);
        c.setAutoCommit(false);

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM TIPOS WHERE IDTy="+tipe);
        while (rs.next()) {
            variable++;
        }
        stmt.close();
        rs.close();
        c.commit();
        c.close();
        return (variable!=0);
    }



    public static void insert_Tipes(Tipo tp) {


        try {
            Class.forName(DRIVERDD);
            Connection c = DriverManager.getConnection(nomdb,"marc","marc");
            c.setAutoCommit(false);

            PreparedStatement sql = c.prepareStatement("INSERT INTO TIPOS (IDTy, Tipus) VALUES (?,?)");
            sql.setInt(1, tp.getIdty());
            sql.setString(2, tp.getTipus());
            sql.executeUpdate();
            sql.close();
            c.commit();
            c.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

    }


    public static boolean exist_Poke(int id) throws SQLException, ClassNotFoundException{
        Class.forName(DRIVERDD);
        Connection c = DriverManager.getConnection(nomdb,"marc","marc");
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();
        int variable = 0;

        ResultSet rs = stmt.executeQuery("SELECT * FROM POKEMONS WHERE IDPok="+id);

        while (rs.next()) {
            variable++;
        }

        stmt.close();
        rs.close();
        c.commit();
        c.close();
        return (variable!=0);
    }

    public static void show_pokes() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVERDD);
        Connection c = DriverManager.getConnection(nomdb,"marc","marc");
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM POKEMONS ORDER BY IDPok;");
        System.out.println("-------ID-------|-------NAME-------");
        while (rs.next()) {
                int id = rs.getInt("IDPok");
                String name = rs.getString("NAME");
                System.out.println("\t"+id+"\t\t\t|\t"+name);
        }
        rs.close();
        stmt.close();
        c.close();
    }

    public static void show_tipes() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVERDD);
        Connection c = DriverManager.getConnection(nomdb,"marc","marc");
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM TIPOS ORDER BY IDTy;");
        System.out.println("-------ID-------|-------Tipus-------");
        while (rs.next()) {
                int id = rs.getInt("IDTy");
                String name = rs.getString("Tipus");
                System.out.println("\t"+id+"\t\t\t|\t"+name);
        }

        rs.close();
        stmt.close();
        c.close();

    }

    public static void insert_TP(int i, int i1) throws Exception{

        Class.forName(DRIVERDD);
        Connection c = DriverManager.getConnection(nomdb,"marc","marc");
        c.setAutoCommit(false);

        PreparedStatement sql = c.prepareStatement("INSERT INTO POKE_TIPO (IDT, IDP) VALUES (?,?)");
        sql.setInt(1, i);
        sql.setInt(2, i1);
        sql.executeUpdate();
        sql.close();
        c.commit();
        c.close();

    }

    public static Pk getPokefromBBDD(int id) throws Exception{

        int id_pok = 0;
        String nom_pok = null;
        String tip[] = {"",""};
        int id_tip[] = {0,0};

        Class.forName(DRIVERDD);
        Connection c = DriverManager.getConnection(nomdb,"marc","marc");
        c.setAutoCommit(false);

        Statement stmt = c.createStatement();

        String query =
                    "SELECT IDPok, NAME, IDTy, Tipus " +
                    "FROM POKEMONS, POKE_TIPO, TIPOS " +
                            "WHERE IDT = IDPok AND IDT ="+id+" AND IDP=IDTy;";
        ResultSet rs = stmt.executeQuery(query);

        int ll = 0;

        while (rs.next()){
                nom_pok = rs.getString("NAME");
                id_pok = rs.getInt("IdPok");
                tip[ll] = rs.getString("Tipus");
                id_tip[ll] = rs.getInt("IDTy");
                ll++;
        }

        Pk pk = new Pk(nom_pok,Integer.toString(id_pok),tip,id_tip);

        rs.close();
        stmt.close();
        c.close();

        return pk;


    }


    public static void show_poke_per_tipus(int tria) throws Exception{

        Class.forName(DRIVERDD);
        Connection c = DriverManager.getConnection(nomdb,"marc","marc");
        c.setAutoCommit(false);

        Statement stmt = c.createStatement();
        String query =  "SELECT IDPok, NAME " +
                        "FROM POKEMONS, POKE_TIPO, TIPOS " +
                        "WHERE IDT = IDPok AND IDP = IDTy AND IDP = IDTY AND IDTy ="+tria+";";
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("-------ID-------|-------NAME-------");
        while (rs.next()) {
            int id = rs.getInt("IDPok");
            String name = rs.getString("NAME");
            System.out.println("\t"+id+"\t\t\t|\t"+name+"--->");
        }
        rs.close();
        stmt.close();
        c.close();

    }


}
