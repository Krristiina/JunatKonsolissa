import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 18.10.2017.
 */
public class Reittihaku {
    //Metodi hakee tarvittava JSONData
    private static List<Juna> lueJunanJSONData(String urli){

        String baseurl = "https://rata.digitraffic.fi/api/v1";
        String jatkoUrl = urli;
        List<Juna> junat = new ArrayList<>();
        try {
            URL url = new URL(baseurl+jatkoUrl);
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Juna.class);
            junat = mapper.readValue(url, tarkempiListanTyyppi);  // pelkkä List.class ei riitä tyypiksi
        } catch (Exception ex) {
            System.out.println(ex);   }
        return junat;
    }

    /*Metodi tulostaa seuraavat 5 lähtöä toivotulle reitille. Tulostus sisältää lähtöpäivän, lähtöajan,
    lähtöraiteen, saapumisajan, saapumisraiteen ja junanumeron */
    public static void haeJunatReitille(String lahtoPaikka, String saapumisPaikka){

        List<Juna> junat;
        junat = lueJunanJSONData("/live-trains/station/" + lahtoPaikka + "/" + saapumisPaikka);
        Date lahtoAika;
        Date saapumisAika;
        int lahtoRaide;
        int saapumisRaide;
        DateFormat dd = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat dt = new SimpleDateFormat("HH:mm");
        List<TimeTableRow> aikataulu;
        int junaNro;
        StringBuilder tulostus = new StringBuilder();

        try {
            System.out.println("\n" + "Seuraavat junat " + lahtoPaikka + "-" + saapumisPaikka + ":" + "\n");
            //Hakee datasta Juna-listan viisi ensimmäistä alkiota. Lisää halutut tiedot tulostettavaan listaan.
            for (int i = 0; i < 5; i++) {
                aikataulu = junat.get(i).getTimeTableRows();
                junaNro = junat.get(i).getTrainNumber();

                //Hakee i-paikassa olevan junan TimeTableRows-listasta lähtöajan, lähtöraiteen, saapumisajan
                // ja saapumisraiteen
                for (int j = 0; j < aikataulu.size(); j++) {

                    //Lähtöasemaa koskeva tieto
                    if (aikataulu.get(j).getStationShortCode().equals(lahtoPaikka)
                            && aikataulu.get(j).getType().equals("DEPARTURE")) {
                        lahtoAika = junat.get(i).getTimeTableRows().get(j).getScheduledTime();
                        lahtoRaide = junat.get(i).getTimeTableRows().get(j).getCommercialTrack();
                        tulostus.append(dd.format(lahtoAika));
                        tulostus.append("\t\t");
                        tulostus.append(dt.format(lahtoAika));
                        tulostus.append("\t\t");
                        tulostus.append(lahtoRaide);
                    }
                    //Saapumisasemaa koskecva tieto
                    if (aikataulu.get(j).getStationShortCode().equals(saapumisPaikka)
                            && aikataulu.get(j).getType().equals("ARRIVAL")) {
                        saapumisAika = aikataulu.get(j).getScheduledTime();
                        saapumisRaide = aikataulu.get(j).getCommercialTrack();
                        tulostus.append("\t\t\t");
                        tulostus.append(dt.format(saapumisAika));
                        tulostus.append("\t\t\t");
                        tulostus.append(saapumisRaide);
                        tulostus.append("\t\t\t\t");
                    }

                }
                tulostus.append(junaNro);
                tulostus.append("\n");

            }
            System.out.println("Lähtöpäivä      Lähtöaika   Lähtöraide  Saapumisaika    Saapumisraide   Junan numero");
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println(tulostus);

        }catch (Exception ex){
            System.out.println("Reitille ei löytynyt junia.");
        }


    }

}

