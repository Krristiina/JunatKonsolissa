import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created by Josefin Reuter on 18.10.2017.
 * Tässä luokassa haetaan junia käyttäjän haluamalle reitille.
 */
public class Reittihaku {
    private StringBuilder tulostus = new StringBuilder();
    private ArrayList<String> SAAATANA = new ArrayList<>();
    private DateFormat pvm = new SimpleDateFormat("dd.MM.yyyy");
    private DateFormat aika = new SimpleDateFormat("HH:mm");
    private String paiva;
    private DateFormat palveluitaVarten = new SimpleDateFormat("yyyy-MM-dd");
    //Metodi hakee tarvittavan JSONDatan
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
        Reittihaku r = new Reittihaku();
        List<Juna> junat;
        junat = lueJunanJSONData("/live-trains/station/" + lahtoPaikka + "/" + saapumisPaikka);
        Date lahtoAika;
        Date saapumisAika;
        String lahtoRaide;
        String saapumisRaide;
        String ID;
        int junaNro;
        List<TimeTableRow> aikataulu;
        Set<String> tulostetut = new HashSet<>();
        boolean junaPaluumatkalla = false;
        try {
            System.out.println("\n" + "Seuraavat junat " + Koodinmurtaja.murraKoodi(lahtoPaikka) + " - " +
                    Koodinmurtaja.murraKoodi(saapumisPaikka) + ":" + "\n");
            //Hakee datasta Juna-listan viisi ensimmäistä alkiota. Lisää halutut tiedot tulostettavaan listaan.
            for (int i = 0; i < 5; i++) {
                aikataulu = junat.get(i).getTimeTableRows();
                junaNro = junat.get(i).getTrainNumber();
                ID = junat.get(i).getCommuterLineID();
                //Hakee i-paikassa olevan junan TimeTableRows-listasta lähtöajan, lähtöraiteen, saapumisajan
                // ja saapumisraiteen
                for (int j = 0; j < aikataulu.size(); j++) {
                    //Lähtöasemaa koskeva tieto
                    //Kaukojunat
                    if((junat.get(i).getCommuterLineID() == "" || junat.get(i).getCommuterLineID() == null)){
                        if (aikataulu.get(j).getStationShortCode().equals(lahtoPaikka)
                                && aikataulu.get(j).getType().equals("DEPARTURE")
                                && aikataulu.get(j).isTrainStopping() == true) {
                            lahtoAika = junat.get(i).getTimeTableRows().get(j).getScheduledTime();
                            lahtoRaide = junat.get(i).getTimeTableRows().get(j).getCommercialTrack();
                            r.lisaaLahtoTulostukseen(lahtoAika, lahtoRaide);
                            r.paiva = r.palveluitaVarten.format(lahtoAika);
                        }
                        if (aikataulu.get(j).getStationShortCode().equals(saapumisPaikka)
                                && aikataulu.get(j).getType().equals("ARRIVAL")
                                && aikataulu.get(j).isTrainStopping() == true) {
                            saapumisAika = aikataulu.get(j).getScheduledTime();
                            saapumisRaide = aikataulu.get(j).getCommercialTrack();
                            r.lisaaTuloTulostukseen(saapumisAika, saapumisRaide);
                        }
                    }
                   /* if(junat.get(i).getCommuterLineID() == "I" || junat.get(i).getCommuterLineID() == "P"){
                    }*/
                    //Lähijunat
                    if(junat.get(i).getCommuterLineID() != "" && junat.get(i).getCommuterLineID() != null
                            /*&& junat.get(i).getCommuterLineID() != "I" && junat.get(i).getCommuterLineID() != "P"*/) {
                        if (aikataulu.get(j).getStationShortCode().equals(lahtoPaikka)
                                && aikataulu.get(j).getType().equals("DEPARTURE")
                                && aikataulu.get(j).isTrainStopping() == true) {
                            if (j <= aikataulu.size() / 2) {
                                junaPaluumatkalla = false;
                                lahtoAika = junat.get(i).getTimeTableRows().get(j).getScheduledTime();
                                lahtoRaide = junat.get(i).getTimeTableRows().get(j).getCommercialTrack();
                                r.lisaaLahtoTulostukseen(lahtoAika, lahtoRaide);
                                r.paiva = r.palveluitaVarten.format(lahtoAika);
                            } else {
                            }
                        } /*else if (aikataulu.get(j).getStationShortCode().equals(lahtoPaikka)
                                && aikataulu.get(j).getType().equals("DEPARTURE")
                                && aikataulu.get(j).isTrainStopping() == true) {
                            junaPaluumatkalla = true;
                        } else {
                        }*/
                        //Saapumisasemaa koskeva tieto
                        if (aikataulu.get(j).getStationShortCode().equals(saapumisPaikka)
                                && aikataulu.get(j).getType().equals("ARRIVAL")
                                && aikataulu.get(j).isTrainStopping() == true) {
                            if (!tulostetut.contains(aikataulu.get(j).getStationShortCode())) {
                                tulostetut.add(aikataulu.get(j).getStationShortCode());
                                saapumisAika = aikataulu.get(j).getScheduledTime();
                                saapumisRaide = aikataulu.get(j).getCommercialTrack();
                                r.lisaaTuloTulostukseen(saapumisAika, saapumisRaide);
                            }
                        }
                    }
                }
                r.lisaaTulostukseen(junaNro, ID);
                tulostetut.clear();
            }
            //Printataaan junien tiedot
            System.out.println("Lähtöpäivä      Lähtöaika   Lähtöraide  Saapumisaika    Saapumisraide   Junan numero");
            System.out.println("--------------------------------------------------------------------------------------" +
                    "------------------------------");
            System.out.println(r.tulostus);
            System.out.println("Haluatteko lisätietoa tänään lähtevän junan palveluista? \n 1.Kyllä \n 2.Ei ");
            r.haePalveluita(r.paiva);
            System.out.println("Haluatteko tietää tietyn junan pysähdyspaikat? \n 1.Kyllä \n 2.Ei ");
            r.haePysahdyspaikat();
        }catch (Exception ex){
            System.out.println("Reitille ei löytynyt junia.");
        }
    }
    //Lisää lähtöön liittyvää tietoa tulostukseen
    private void lisaaLahtoTulostukseen (Date lahtoAika, String lahtoRaide){
        tulostus.append(pvm.format(lahtoAika));
        tulostus.append("\t\t");
        tulostus.append(aika.format(lahtoAika));
        tulostus.append("\t\t");
        tulostus.append(lahtoRaide);
    }
    //Lisää saapumiseen liittyvää tietoa tulostukseen
    private void lisaaTuloTulostukseen (Date saapumisAika, String saapumisRaide){
        tulostus.append("\t\t\t\t");
        tulostus.append(aika.format(saapumisAika));
        tulostus.append("\t\t\t");
        tulostus.append(saapumisRaide);
    }
    //Lisää tietoa tulostukseen
    private void lisaaTulostukseen(int junaNro, String ID){
        tulostus.append("\t\t\t");
        tulostus.append(junaNro).append(" ");
        tulostus.append(ID);
        tulostus.append("\t\t\t\t");
        String onkoAikataulussa = MyohastynytJaSyy.onkoJunaAikataulussaint(junaNro);
        tulostus.append(onkoAikataulussa);
        tulostus.append("\n");
    }
    //Haetaan tietyn junan palveluita junanumeron avulla
    private void haePalveluita (String paivamaara){
        try {
            Scanner sc = new Scanner(System.in);
            String luku = sc.nextLine();
            if ("1".equals(luku)) {
                System.out.println("Syöttäkää junan numero (pelkkä numero): ");
                int junaNumero = sc.nextInt();
                JunaInfo juna = new JunaInfo();
                System.out.println(juna.haeJunanPalvelut(paivamaara, junaNumero));
                System.out.println("-----------------------------------------------");
            }else {
                return;
            }
        }catch (Exception ex){
            System.out.println("Valitettavasti junaa ei löytynyt.");
        }
    }
    //Haetaan tietyn junan pysähdyspaikat junanumeron avulla
    private void haePysahdyspaikat (){
        try {
            Scanner sc = new Scanner(System.in);
            String luku = sc.nextLine();
            if ("1".equals(luku)) {
                MyohastynytJaSyy.pysahdyspaikat();
                //System.out.println("-----------------------------------------------");
            }else {
                return;
            }
        }catch (Exception ex){
            System.out.println("Valitettavasti junaa ei löytynyt.");
        }
    }
}