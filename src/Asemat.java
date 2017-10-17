import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.util.Collections.sort;

/**
 * Created by Administrator on 17.10.2017.
 */
public class Asemat {
    public static void main(String[] args) {
        lahtevatJunatNyt("HKI");
        saapuvatJunatNyt("HKI");
    }


    /*Tällä metodilla haetaan tietyn
    aseman data annetun urlin perusteella*/
    private static List<Juna> lueAsemanJSONData(String urli){
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

    /*Tämä metodi palauttaa taulukkotulosteen
    tietyn aseman lahtevista junista juuri tällä hetkellä +-60 min
    URL: /live-trains/station/<station_shortcode>?minutes_before_departure=<minutes_before_departure>&minutes_after_departure=<minutes_after_departure>&minutes_before_arrival=<minutes_before_arrival>&minutes_after_arrival=<minutes_after_arrival>&version=<change_number>&includeNonstopping=<includeNonstopping
    Esimerkiksi: /live-trains/station/HKI?minutes_before_departure=15&minutes_after_departure=15&minutes_before_arrival=15&minutes_after_arrival=15
     /live-trains/station/HKI?arrived_trains=5&arriving_trains=&5departed_trains=5&departing_trains=5&include_nonstopping=false
    */
    public static void lahtevatJunatNyt(String asema) {
        System.out.println("Lähtevät junat asemalta "+ asema + ":");
        String asemaKoodi = asema;
        String osoite = "/live-trains/station/" + asemaKoodi + "?arrived_trains=0&arriving_trains=0&departed_trains=0&departing_trains=20&include_nonstopping=false";
        List<Juna> asemanJunat = lueAsemanJSONData(osoite);
        luoJunaTaulukko(asemanJunat, "Lähtöaika");
        System.out.println();
    }

    /*Tämä metodi palauttaa taulukkotulosteen tietyn
        aseman saapuvista junista juuri tällä hetkellä*/
    public static void saapuvatJunatNyt(String asema){
        System.out.println("Saapuvat junat asemalle"+ asema +  ": ");
        String asemaKoodi = asema;
        String osoite = "/live-trains/station/" + asemaKoodi + "?arrived_trains=0&arriving_trains=20&departed_trains=0&departing_trains=0&include_nonstopping=false";
        List<Juna> asemanJunat = lueAsemanJSONData(osoite);
        luoJunaTaulukko(asemanJunat, "Saapuu");
    }

    /*Tämä metodi luo ja tulostaa taulukon haetuista junista */
    public static void luoJunaTaulukko(List<Juna> asemanJunat, String lahtovaisaapuminen) {
        ArrayList<String>asematiedot = new ArrayList<>();
        //tähän taulukkoon lähtöaika, juna, määränpää ja raide(String commercialTrack);
        Date lahtoAika;
        String junatyyppi;
        String maaranpaa;
        String raide;

        for (int i = 0; i<20; i++) {
            lahtoAika = asemanJunat.get(i).timeTableRows.get(0).scheduledTime;
            DateFormat da = new SimpleDateFormat("HH:mm");
            junatyyppi = asemanJunat.get(i).getCommuterLineID();
            raide = asemanJunat.get(i).timeTableRows.get(0).commercialTrack;
            maaranpaa = asemanJunat.get(i).timeTableRows.get(asemanJunat.get(i).timeTableRows.size()-1).stationShortCode;
            StringBuilder tiedot = new StringBuilder();
            tiedot.append(da.format(lahtoAika));
            tiedot.append("\t\t");
            tiedot.append(junatyyppi);
            tiedot.append("\t\t");
            tiedot.append(raide);
            tiedot.append("\t\t");
            tiedot.append(maaranpaa);

            asematiedot.add(tiedot.toString());
        }
        String otsikot = lahtovaisaapuminen+ "\tNro \traide \tpääteasema";
        String alaviiva = "____________________________________";
        Collections.sort(asematiedot);
        asematiedot.add(0, alaviiva);
        asematiedot.add(0,otsikot);


        for (int k = 0; k < asematiedot.size(); k++){
            System.out.println(asematiedot.get(k));
        }


    }

    /*
for (ind; ind < saapumisKaikki.length; ind++){
        if (jsonOlio[i].timeTableRows[ind].stationShortCode === "LH" && jsonOlio[i].timeTableRows[ind].type === "ARRIVAL"){
            console.log("Tämä on indeksi: " + ind);
            saapuminen = saapumisKaikki[ind].scheduledTime;
        }*/


    /*Tämä metodi palauttaa taulukkotulosteen
    tietyn aseman
    lähtevistä junista annettuna ajan hetkenä*/
    public static void lahtevatJunatKlo(String asema, Date aika){

    }

    /*Tämä metodi palauttaa taulukkotulosteen tietyn aseman saapuvista junista annettuna ajan hetkenä*/
    public void saapuvatJunatKlo(String asema, Date aika){

    }
}
