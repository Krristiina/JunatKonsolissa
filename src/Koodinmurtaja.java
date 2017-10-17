import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UnknownFormatConversionException;

/**
 * Created by Administrator on 17.10.2017.
 */
public class Koodinmurtaja {
    public static void main(String[] args) {
        String kaupunki = murraKoodi("TPE");
        System.out.println(kaupunki);
        String koodi = koodaaNimi("Tampere");
        System.out.println(koodi);

    }

    public static String koodaaNimi(String kaupunki){
        List<Asema> asemat = lueAsemat();
        String koodi = "";
        kaupunki = kaupunki +" asema";
        for (int i = 0; i<asemat.size(); i++){
            if(asemat.get(i).getStationName().equals(kaupunki)){
                koodi += asemat.get(i).getStationShortCode();
            }
        }
        return koodi;
    }

    public static String murraKoodi(String nimiKoodi){
        List<Asema> asemat = lueAsemat();
        String kaupunki = "";
        for (int i = 0; i<asemat.size(); i++){
            if(nimiKoodi.equals(asemat.get(i).getStationShortCode())){
                kaupunki = asemat.get(i).getStationName();
                String nimi = kaupunki; //.substring(0,kaupunki.length()-6);
                return nimi;
            }else{
                continue;
            }
        }
        return "Tuntematon asema";
    }

    private static List<Asema> lueAsemat(){
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        String jatkoUrl = "/metadata/stations";
        List<Asema> asemaNimi = new ArrayList<>();
        try {
            URL url = new URL(baseurl+jatkoUrl);
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Asema.class);
            asemaNimi = mapper.readValue(url, tarkempiListanTyyppi);  // pelkkä List.class ei riitä tyypiksi
            // Seuraavaa varten on toteutettava TimeTableRow luokka:
            //System.out.println(junat.get(0).getTimeTableRows().get(0).getScheduledTime());

        } catch (Exception ex) {
            System.out.println(ex);   }
        return asemaNimi;
    }
}

