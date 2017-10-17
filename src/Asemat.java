import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 17.10.2017.
 */
public class Asemat {
    public static void main(String[] args) {
    lahtevatJunatNyt();
    }
    /*Tällä metorilla haetaan tietyn
    aseman data annetun urlin perusteella
    URL: /live-trains/station/<station_shortcode>?minutes_before_departure=<minutes_before_departure>&minutes_after_departure=<minutes_after_departure>&minutes_before_arrival=<minutes_before_arrival>&minutes_after_arrival=<minutes_after_arrival>&version=<change_number>&includeNonstopping=<includeNonstopping
    Esimerkiksi: /live-trains/station/HKI?minutes_before_departure=15&minutes_after_departure=15&minutes_before_arrival=15&minutes_after_arrival=15
    */
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
    tietyn aseman lahtevista junista juuri tällä hetkellä*/
    public static void lahtevatJunatNyt() {
        String osoite = "/live-trains/station/HKI/";
        List<Juna> asemanJunat = lueAsemanJSONData(osoite);
        for (Juna juna: asemanJunat) {
            System.out.println(juna.getDepartureDate() + "+ " + juna.getTrainNumber() + juna.getTrainType());
        }

    }
    /*Tämä metodi palauttaa taulukkotulosteen
    tietyn aseman
    lähtevistä junista annettuna ajan hetkenä*/
    public void lahtevatJunatKlo(String asema, LocalTime aika){

    }

    /*Tämä metodi palauttaa taulukkotulosteen tietyn
    aseman saapuvista junista juuri tällä hetkellä*/
    public void saapuvatJunatNyt(String asema){



    }
    /*Tämä metodi palauttaa taulukkotulosteen tietyn aseman saapuvista junista annettuna ajan hetkenä*/
    public void saapuvatJunatKlo(String asema, LocalTime aika){

    }
}
