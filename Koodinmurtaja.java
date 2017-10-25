import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UnknownFormatConversionException;
/**
 * Created by Minna Vares on 17.10.2017.
 * Tämä luokka murtaa koodeja :). Metodeina on .haeAsemaKoodiaKirjaimilla(String abc).tulostaKaikki(), .koodaaNimi(), ja
 * .murraKoodi(). ks. kuvaukset alla.
 * Koodinmurtaja-luokka käyttää Asema-luokkaa, jossa aseman nimi ja koodi.
 */
public class Koodinmurtaja {
    public static void main(String[] args) {
        //String kaupunki = murraKoodi("VS");
        //System.out.println(kaupunki);
        //String koodi = koodaaNimi("Vaasa asema");
        //System.out.println(koodi);
        haeAsemaKoodiaKirjaimilla("öyäöy");
    }
    /*Tämä metodi hakee aseman koodia alkukirjainten perusteella*/
    public static void haeAsemaKoodiaKirjaimilla(String abc) {
        String alkukirjaimet = abc;
        List<Asema> asemat = lueAsemienNimet();
        List<Asema> otos = new ArrayList<>();
        for (int i = 0; i < asemat.size(); i++) {
            if(asemat.get(i).getStationName().contains(alkukirjaimet)){
                otos.add(asemat.get(i));
            }
        }
        if(otos.size()==0){
            System.out.println("Tällä kirjainyhdistelmällä ei löytynyt asemia");
            return;
        }
        System.out.println("Asemat, jotka sisältävät kirjaimet '" + alkukirjaimet + "':");
        for (int i = 0; i < otos.size(); i++) {
            System.out.println(otos.get(i).getStationName() + " : " + otos.get(i).getStationShortCode());
        }
    }
    /*Tällä metodilla saa tulostettua kaikki asemat listaksi*/
    public static void tulostaKaikki() {
        List<Asema> asemat = lueAsemienNimet();
        for (int i = 0; i < asemat.size(); i++) {
            System.out.println(asemat.get(i).getStationName() + " : " + asemat.get(i).getStationShortCode());
        }
    }
    /*Tämä metodi muuttaa aseman nimen koodiksi. HUOM! Aseman nimen täytyy olla ehdottoman oikein,
    jotta metodi toimii. Esim Tampereen pääasema on "Tampere asema", palauttaa nimen koodin */
    public static String koodaaNimi(String kaupunki){
        List<Asema> asemat = lueAsemienNimet();
        String koodi = "";
        for (int i = 0; i<asemat.size(); i++){
            if(asemat.get(i).getStationName().equals(kaupunki)){
                koodi = asemat.get(i).getStationShortCode();
            }
        }
        return koodi;
    }
    /*Tämä metodi palauttaa kaupungin / kautta aseman nimen. Metodille annetaan nimikoodi,
    palauttaa aseman nimen. Voidaan käyttää esim tulostuksissa*/
    public static String murraKoodi(String nimiKoodi){
        List<Asema> asemat = lueAsemienNimet();
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
    /*Tämä metodi lukee asemien nimet, palauttaa taulukon jossa Asema-olioita, joilla metodit
    on .getStationShortCode ja .getStationName*/
    private static List<Asema> lueAsemienNimet(){
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        String jatkoUrl = "/metadata/stations";
        List<Asema> asemaNimi = new ArrayList<>();
        try {
            URL url = new URL(baseurl+jatkoUrl);
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Asema.class);
            asemaNimi = mapper.readValue(url, tarkempiListanTyyppi);
        } catch (Exception ex) {
            System.out.println(ex);   }
        return asemaNimi;
    }
}
