import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class pkdb {

    /*
        getHTML és una llibreria que s'encarrega de gestionar les peticions GET.
        A l'última setmana de M09 d'aquest any ho veurem en detall.

    * */

    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        conn.disconnect();
        return result.toString();
    }

    /*

            MAIN: Tria la funció que vols realitzar de manera pulida.
            Un cop i sortir.

     */

    public static void main(String[] args) throws Exception {

        boolean exit = true;

        //while(exit){

            System.out.println("Tria una opció:");
            System.out.println("1. Insereix pokemon");
            System.out.println("2. Mostra pokemons");
            System.out.println("3. Mostra tipus");
            System.out.println("4. Mostra pokemons d'un tipus");
            System.out.println("5. Mostra info d'un pokemon");
            System.out.println("Altres: Sortir");

            Scanner sc = new Scanner(System.in);
            int opcio = Integer.parseInt(sc.nextLine());


            switch (opcio) {
                case 1:
                    insereix_poke();
                    break;
                case 2:
                    mostraPokes();
                    break;
                case 3:
                    mostraTipus();
                    break;
                case 4:
                    mostraPokesperTipus();
                    break;
                case 5:
                    mostraPokesmon();
                    break;
                default:
                    exit = false;
                    System.out.println("Que tingui un bon dia!");

            }


    }

    /**
     * Implementada la funció mostra pokemon de manera que té relació amb
     * la base de dades.
     * @throws Exception
     */

    private static void mostraPokesmon() throws Exception {
        System.out.println("Només tinc els pokemons de la llista següent:");
        DAO.show_pokes();
        Scanner sc = new Scanner(System.in);
        System.out.println("Escull un (tria el número):");
        int k = Integer.parseInt(sc.nextLine());
        /*
        La manera mandrosa de fer això seria fent la peticio GET.
        El que farem aquí és accedir a la BBDD.
         */
        Pk plle = DAO.getPokefromBBDD(k);
        plle.imprimir();
        sc.close();
    }

    /**
     * Implementaada la funció que insereix un pokemon
     * @throws Exception
     */

    public static void insereix_poke() throws Exception{

        Scanner sc = new Scanner(System.in);
        System.out.println("Introdueix la Id:");
        int k = Integer.parseInt(sc.nextLine());
        sc.close();
        Pk pk2 = getPoke(k);

        if(!DAO.exist_Poke(pk2.getIdentificador())){
            DAO.insert_Poke(pk2);
            for (int i = 0; i < pk2.getTipos().length; i++) {
                DAO.insert_TP(pk2.getIdentificador(), pk2.getTipos()[i].getIdty());
            }
            for (int j = 0; j < pk2.getTipos().length ; j++){
                if(!DAO.exist_Tipe(pk2.getTipos()[j].getIdty())){
                    DAO.insert_Tipes(pk2.getTipos()[j]);
                }
            }

            System.out.println("S'ha inserit en "+pk2.getNombre());
        }else{
            System.out.println("El poke ja existia");
        }

    }

    public static Pk getPoke(int k) throws Exception {

        String url = "http://pokeapi.co/api/v1/pokemon/"+k;
        String jsonurl = getHTML(url);
        return SJPokeApi(jsonurl);

    }

    public static void mostraPokes() throws Exception{
        DAO.show_pokes();
    }

    public static void mostraTipus() throws Exception{
        DAO.show_tipes();
    }

    public static void mostraPokesperTipus() throws Exception{
        System.out.println("Només tinc tipus de la següent llista:");
        DAO.show_tipes();
        Scanner sc = new Scanner(System.in);
        System.out.println("Escull un (tria el número):");
        int k = Integer.parseInt(sc.nextLine());
        DAO.show_poke_per_tipus(k);
        sc.close();
    }

    public static Pk SJPokeApi(String cadena){
        Object obj = JSONValue.parse(cadena);
        JSONObject jobj = (JSONObject)obj; // Contiene toda la información del JSON
        String pknom = (String)jobj.get("name");
        String id = Long.toString((Long)jobj.get("national_id"));
        JSONArray jarray = (JSONArray)jobj.get("types");
        String mmg[] = new String[jarray.size()];

        int mmg_id[] = new int[jarray.size()];

        for (int i = 0; i < jarray.size(); i++) {
            JSONObject jobjda = (JSONObject)jarray.get(i);
            mmg[i] = (String)jobjda.get("name");
            mmg_id[i] = tratar_cadena_id_tipo((String)jobjda.get("resource_uri"));
        }
        return new Pk(pknom,id,mmg,mmg_id);
    }

    private static int tratar_cadena_id_tipo(String cadena){
        String definitiva = cadena.substring(13,cadena.length()-1);
        System.out.println(definitiva);
        return  Integer.parseInt(definitiva);
    }

}
