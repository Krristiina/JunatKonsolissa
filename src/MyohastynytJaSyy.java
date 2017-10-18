import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Administrator on 17.10.2017.
 */
public class MyohastynytJaSyy {
    public static void main(String[] args) {

        //onkoJunaAikataulussa();
        syyMyohastys();

    }

    private static List<Juna> lueJSONData(String urli){
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        String jatkoUrl = urli;
        List<Juna> junat = new ArrayList<>();
        try {
            URL url = new URL(baseurl+jatkoUrl);
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Juna.class);
            junat = mapper.readValue(url, tarkempiListanTyyppi);  // pelkkä List.class ei riitä tyypiksi
            // Seuraavaa varten on toteutettava TimeTableRow luokka:
            //System.out.println(junat.get(0).getTimeTableRows().get(0).getScheduledTime());
        } catch (Exception ex) {
            System.out.println(ex);   }
        return junat;
    }

    public static void onkoJunaAikataulussa () {
        Scanner lukija = new Scanner(System.in);
        System.out.println("Anna junan numero: ");
        int junaNumero = lukija.nextInt();
        String osoite = "/trains/latest/" + junaNumero;
        List<Juna> aikatauluJunat = lueJSONData(osoite);

        for ( Juna juna: aikatauluJunat) {

            if (juna.getTimeTableRows().get(juna.getTimeTableRows().size() - 1).getDifferenceInMinutes() == 0) {
                System.out.println("Juna on aikataulussa.");


            } else if (juna.getTimeTableRows().get(juna.getTimeTableRows().size() - 1).getDifferenceInMinutes() > 0) {
                System.out.println("\nJuna nro " + junaNumero + " on myöhässä " + juna.getTimeTableRows().get(juna.getTimeTableRows().size() - 1).getDifferenceInMinutes() + " minuuttia.");
                System.out.println("\nSyy numero: " + juna.getTimeTableRows().get(0).getCauses().get(0).getdetailedCategoryCodeId());

            }

        }}

    public static void syyMyohastys () {
        String osoite = "/metadata/detailed-cause-category-codes";
        List<Juna> syynKuvaus = lueJSONData(osoite);

        for (Juna juna : syynKuvaus) {
            System.out.println(juna.getDetailedCause(95));
        }


    }


}