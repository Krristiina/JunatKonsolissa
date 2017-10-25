/**
 * Created by Ville Hyvärinen on 18.10.2017.
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
// import com.sun.java.util.jar.pack.ConstantPool;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
public class MyohastynytJaSyy {
    public static void main(String[] args) {
        // onkoJunaAikataulussa();
        // syyMyohastys(0);
        pysahdyspaikat();
    }
    /*  Metodi hakee JSONdatan */
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
    /* Metodi kysyy käyttäjältä tietyn junan numeroa. Numeron perusteella metodi hakee JSONdatasta tiedon, onko
     * juna myöhässä ja palauttaa tiedon. */
    public static void onkoJunaAikataulussa () {
        Scanner lukija = new Scanner(System.in);
        System.out.println("Anna junan numero: ");
        int junaNumero = lukija.nextInt();
        String osoite = "/trains/latest/" + junaNumero;
        List<Juna> aikatauluJunat = lueJSONData(osoite);
        /* Looppi käy läpi tietyn junan tiedot ja etsii sen viimeisen rekisteröidyn asemasijainnin. Asemasijainnista otetaan arvo
        * "differencesInMinutes", ja käyttäjälle tulostetaan minuuteissa mahdollinen aikataulumuutos/myöhästyminen. */
        for ( Juna juna: aikatauluJunat) {
            System.out.println();
            if (juna.getTimeTableRows().get(juna.getTimeTableRows().size() - 1).getDifferenceInMinutes() == 0) {
                System.out.println("Juna on aikataulussa.");
            } else if (juna.getTimeTableRows().get(juna.getTimeTableRows().size() - 1).getDifferenceInMinutes() > 0) {
                System.out.println("\nJuna nro " + junaNumero + " on myöhässä " + juna.getTimeTableRows().get(juna.getTimeTableRows().size() - 1).getDifferenceInMinutes() + " minuuttia.");
                int syyNumero = 0;
                // Jos juna on myöhässä, niin sille etsitään mahdollinen syy toisesta URL-osoitteesta.
                try {
                    syyNumero = juna.getTimeTableRows().get(0).getCauses().get(0).getdetailedCategoryCodeId();
                    System.out.println("\nSyy numero: " + syyNumero);
                    System.out.println("Syy: " + syyMyohastys(syyNumero));
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Syytä ei määritelty");
                }
            }}
        System.out.println("");
        System.out.println("Haluatteko lisätietoja junan kulusta?");
        System.out.println("1. Kyllä");
        System.out.println("2. En");
        int vastaus = lukija.nextInt();
        int kylla = 1;
        int en = 2;
        if (vastaus == kylla) {
            for (Juna juna : aikatauluJunat) {
                System.out.println();
                DateFormat da = new SimpleDateFormat("HH:mm");
                int syyNumero = 0;
                // Junan suunniteltu ja toteutunut aikataulu sekä arviot
                try {
                    System.out.println("Asema              Aikataulu       Aika/arvio        Muutos");
                    System.out.println("---------------------------------------------------------------");
                    String asemaApu = Koodinmurtaja.murraKoodi(juna.getTimeTableRows().get(0).getStationShortCode());
                    if (asemaApu.length() > 11) {
                        asemaApu = asemaApu.substring(0, 11);
                    }
                    String asema1 = String.format("%-11s", asemaApu);
                    System.out.println(asema1 + "    :    " +
                            da.format(juna.getTimeTableRows().get(0).getScheduledTime())
                            + "     :     " + ((juna.getTimeTableRows().get(0).getActualTime() != null) ?
                            da.format(juna.getTimeTableRows().get(0).getActualTime()) : da.format(juna.getTimeTableRows().get(0).getLiveEstimateTime())) +
                            "    :    Myöhässä " + juna.getTimeTableRows().get(0).getDifferenceInMinutes() + " min.");
                    for (int i = 1; i < juna.getTimeTableRows().size(); i += 2) {
                        String asema = Koodinmurtaja.murraKoodi(juna.getTimeTableRows().get(i).getStationShortCode());
                        if (asema.length() > 11) {
                            asema = asema.substring(0, 11);
                        }
                        String asema10 = String.format("%-11s", asema);
                        if (juna.getTimeTableRows().get(i).isTrainStopping() == true) {
                            System.out.println(
                                    asema10 + "    :    " +
                                            da.format(juna.getTimeTableRows().get(i).getScheduledTime())
                                            + "     :     " + ((juna.getTimeTableRows().get(i).getActualTime() != null) ?
                                            da.format(juna.getTimeTableRows().get(i).getActualTime()) + "    :    Myöhässä " + juna.getTimeTableRows().get(i).getDifferenceInMinutes() + " min."
                                            : da.format(juna.getTimeTableRows().get(i).getLiveEstimateTime()) +
                                            "    :    Myöhässä " + juna.getTimeTableRows().get(i).getDifferenceInMinutes() + " min.")
                            );
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("");
                } catch (NullPointerException ex) {
                    System.out.println("");
                }
            }
        }
    }
    /* Jos juna on myöhässä, niin sille haetaan JSONdatasta mahdollinen syy. Tieto haetaan syötetyn junanumeron perusteella.
    * Mahdollista syytä ei kuitenkaan ole usein määritelty, vaikka juna olisikin myöhässä. */
    private static List<Syy> syyJSONData(String urli){
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        String jatkoUrl = urli;
        List<Syy> junat = new ArrayList<>();
        try {
            URL url = new URL(baseurl+jatkoUrl);
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Syy.class);
            junat = mapper.readValue(url, tarkempiListanTyyppi);  // pelkkä List.class ei riitä tyypiksi
            // Seuraavaa varten on toteutettava TimeTableRow luokka:
            // System.out.println(junat.get(0).getTimeTableRows().get(0).getScheduledTime());
        } catch (Exception ex) {
            System.out.println(ex);   }
        return junat;
    }
    /* Jos juna oli myöhässä, metodi käy läpi listan tarkemmista syymäärityksistä ja tulostaa syyNumeron perusteella
    * tarkemman syymäärityksen. */
    public static String syyMyohastys (int syyNumero) {
        String osoite = "/metadata/detailed-cause-category-codes";
        List<Syy> syynKuvaus = syyJSONData(osoite);
        for (Syy juna : syynKuvaus) {
            if (juna.getId() == syyNumero) {
                return juna.getDetailedCategoryName();
            }
        }return "";
    }
    public static String onkoJunaAikataulussaint(int junanro) {
        int junaNumero = junanro;
        String osoite = "/trains/latest/" + junaNumero;
        List<Juna> aikatauluJunat = lueJSONData(osoite);
        for ( Juna juna: aikatauluJunat) {
            if (juna.getTimeTableRows().get(juna.getTimeTableRows().size() - 1).getDifferenceInMinutes() == 0) {
                return "Juna on aikataulussa.";
            } else if (juna.getTimeTableRows().get(juna.getTimeTableRows().size() - 1).getDifferenceInMinutes() > 0) {
                return "Juna on myöhässä " +
                        juna.getTimeTableRows().get(juna.getTimeTableRows().size() - 1).getDifferenceInMinutes() + " minuuttia.";
            }
        }
        return "Tietoja ei saatavilla.";
    }
    // Metodi junan pysähdyspaikkojen etsintää varten
    public static void pysahdyspaikat () {
        Scanner lukija = new Scanner(System.in);
        System.out.println("Anna junan numero: ");
        int junaNumero = lukija.nextInt();
        String osoite = "/trains/latest/" + junaNumero;
        List<Juna> aikatauluJunat = lueJSONData(osoite);
        for (Juna juna : aikatauluJunat) {
            System.out.println();
            DateFormat da = new SimpleDateFormat("HH:mm");
            int syyNumero = 0;
            // Junan suunniteltu ja toteutunut aikataulu sekä arviot
            try {
                System.out.println("Asema              Aikataulu       ");
                System.out.println("-----------------------------");
                String asemaApu = Koodinmurtaja.murraKoodi(juna.getTimeTableRows().get(0).getStationShortCode());
                if (asemaApu.length() > 11) {
                    asemaApu = asemaApu.substring(0, 11);
                }
                String asema1 = String.format("%-11s", asemaApu);
                System.out.println(asema1 + "    :    " +
                        da.format(juna.getTimeTableRows().get(0).getScheduledTime()));
                for (int i = 1; i < juna.getTimeTableRows().size(); i += 2) {
                    String asema = Koodinmurtaja.murraKoodi(juna.getTimeTableRows().get(i).getStationShortCode());
                    if (asema.length() > 11) {
                        asema = asema.substring(0, 11);
                    }
                    String asema10 = String.format("%-11s", asema);
                    if (juna.getTimeTableRows().get(i).isTrainStopping() == true) {
                        System.out.println(
                                asema10 + "    :    " +
                                        da.format(juna.getTimeTableRows().get(i).getScheduledTime()));
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("");
            } catch (NullPointerException ex) {
                System.out.println("");
            }
        }
    }
}