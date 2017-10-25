import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristiina Rönnberg on 17.10.2017.
 */
public class JunaInfo {
    String paiva;
    int junanumero;
    Juna juna;

    public JunaInfo() {
        this.paiva = paiva;
        this.junanumero = junanumero;
    }



    public static void main(String[] args) {

        JunaInfo juna = new JunaInfo();

        juna.haeKaikkiPaivanKokoonpanot("2017-10-18");
    }

    //Tämä metodi hakee kaikki tietyn päivän junakokoonpanot
    public void haeKaikkiPaivanKokoonpanot(String paiva){
        String baseurl = "https://rata.digitraffic.fi/api/v1/compositions/";
        List<Juna> junat = new ArrayList<>();
        try {
            URL url = new URL(baseurl+paiva);
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Juna.class);
            junat = mapper.readValue(url, tarkempiListanTyyppi);  // pelkkä List.class ei riitä tyypiksi

            StringBuilder paivanKokoonpanot = new StringBuilder();
            paivanKokoonpanot.append("Junanumerojärjestyksessä 10 ensimmäistä kokoonpanoa päivänä: " + paiva);
            paivanKokoonpanot.append("\n");
            paivanKokoonpanot.append("--------------------------------------------------------------------------------------");
            int indeksi;
            for (indeksi=0; indeksi < 10; indeksi++) {
                paivanKokoonpanot.append("\n");
                paivanKokoonpanot.append("Junan numero: ").append("\t\t\t\t\t").append(junat.get(indeksi).getTrainNumber());
                paivanKokoonpanot.append("\n");
                paivanKokoonpanot.append("Operaattori:").append("\t\t\t\t\t").append(junat.get(indeksi).getOperatorShortCode());
                paivanKokoonpanot.append("\n");
                paivanKokoonpanot.append("Operaattorin UIC-koodi: ").append("\t\t").append(junat.get(indeksi).getOperatorUICCode());
                paivanKokoonpanot.append("\n");
                paivanKokoonpanot.append("Junatyyppi: ").append("\t\t\t\t\t").append(junat.get(indeksi).getTrainType());
                paivanKokoonpanot.append("\n");
                paivanKokoonpanot.append("Junan kokonaispituus: ").append("\t\t\t").append(junat.get(indeksi).journeySections.get(0).getTotalLength()).append(" metriä");
                paivanKokoonpanot.append("\n");
                paivanKokoonpanot.append("Junan maksiminopeus: ").append("\t\t\t").append(junat.get(indeksi).journeySections.get(0).getMaximumSpeed()).append(" km/h");
                paivanKokoonpanot.append("\n");
                if (junat.get(indeksi).getTrainCategory().equals("Long-distance")){
                    paivanKokoonpanot.append("Junakategoria:").append("\t\t\t\t\t").append("Kaukoliikenne");
                    paivanKokoonpanot.append("\n");
                } else {
                    paivanKokoonpanot.append("Junakategoria:").append("\t\t\t\t\t").append("Lähiliikenne");
                    paivanKokoonpanot.append("\n");
                }
                paivanKokoonpanot.append("Vaunujen kappalemäärä:").append("\t\t\t").append(junat.get(indeksi).journeySections.get(0).getWagons().size());
                paivanKokoonpanot.append("\n");
                String onkoAikataulussa = MyohastynytJaSyy.onkoJunaAikataulussaint(junat.get(indeksi).getTrainNumber());
                paivanKokoonpanot.append("Aikataulupoikkeama: ").append("\t\t\t").append(onkoAikataulussa);
                paivanKokoonpanot.append("\n");
                paivanKokoonpanot.append("--------------------------------------------------------------------------------------");
                paivanKokoonpanot.append("\n");
            }


            System.out.println("Yksittäisiä kokoonpanoja on tänä päivänä yhteensä " + (junat.size()-1) + " kappaletta");
            System.out.println(paivanKokoonpanot);


        } catch (Exception ex) {
            System.out.println(ex);   }
    }

    //Metodi hakee paivan ja junanumeron perusteella luodusta osoitteesta tietoa

    public Juna haeJuna (String paiva, int junanumero){

        String baseurl = "https://rata.digitraffic.fi/api/v1/";

        try {

            URL url = new URL(baseurl+"/compositions/" + paiva +"/" + junanumero);
            // luodun urlin tiedot eivät ole json-arrayn vaan json-olion muodossa

            ObjectMapper mapper = new ObjectMapper();
            Juna juna = mapper.readValue(url, Juna.class);
            return juna;

        } catch (UnrecognizedPropertyException e){
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

        //metodi palauttaa StrinBuilder-muodossa erittelyn junan palveluista ja niiden mahdollisesta vaunusijainnista

        public StringBuilder haeJunanPalvelut (String paiva, int junanumero){

        try {

            Juna juna = haeJuna(paiva, junanumero);

            if (juna == null){
                throw new Exception("Junan palveluita ei ole saatavilla.");
            }

            // Luodaan lista vaunut, josta löytyy yksittäisten vaunujen tiedot (Huom! Veturi ei ole näissä mukana.)
            List<Wagons> vaunut = juna.getJourneySections().get(0).getWagons();

            /* Tarkastetaan, onko junassa seuraavia ominaisuuksia:
                ravintolaosasto
                eläinosasto
                leikkiosasto
                liikuntarajoitteisten osasto
                videomahdollisuus

                Käyttäjälle tulostetaan, löytyykö ominaisuutta. Jos löytyy, kerrotaan myös vaunu, jossa ominaisuus sijaitsee.


             */


            int onkoCatering = 0;
            int onkoPet = 0;
            int onkoDisabled = 0;
            int onkoPlayground = 0;
            int onkoVideo = 0;
            int onkoSmoking = 0;
            int onkoLuggage = 0;



            for (int i= 0; i < vaunut.size(); i++ ){

                int vaununumero = vaunut.get(i).getSalesNumber();

                if (vaunut.get(i).isCatering()) {
                    onkoCatering = vaununumero;
                }

                if (vaunut.get(i).isPet()) {
                    onkoPet = vaununumero;
                }

                if (vaunut.get(i).isDisabled()) {
                    onkoDisabled = vaununumero;
                }

                if (vaunut.get(i).isPlayground()){
                    onkoPlayground = vaununumero;
                }

                if (vaunut.get(i).isVideo()) {
                    onkoVideo = vaununumero;
                }

                if (vaunut.get(i).isSmoking()){
                    onkoSmoking = vaununumero;
                }

                if (vaunut.get(i).isLuggage()){
                    onkoLuggage = vaununumero;
                }

            }

            StringBuilder taulukkotulostus = new StringBuilder();
            taulukkotulostus.append("PALVELU                     SIJAINTI VAUNUSSA NUMERO");
            taulukkotulostus.append("\n");
            taulukkotulostus.append("--------------------------------------------------------------------------------------");
            taulukkotulostus.append("\n");


            if (onkoCatering > 0){
                taulukkotulostus.append("Ravintolaosasto").append("\t\t\t\t\t\t").append(onkoCatering);
                taulukkotulostus.append("\n");
            } else {
                taulukkotulostus.append("Ravintolaosasto").append("\t\t\t\t\t\t").append("-");
                taulukkotulostus.append("\n");
            }

            if (onkoPet > 0){
                taulukkotulostus.append("Lemmikkiosasto").append("\t\t\t\t\t\t").append(onkoPet);
                taulukkotulostus.append("\n");
            } else {
                taulukkotulostus.append("Lemmikkiosasto").append("\t\t\t\t\t\t").append("-");
                taulukkotulostus.append("\n");
            }

            if (onkoDisabled > 0){
                taulukkotulostus.append("Invalidiystävällinen osasto").append("\t\t\t").append(onkoDisabled);
                taulukkotulostus.append("\n");
            } else {
                taulukkotulostus.append("Invalidiystävällinen osasto").append("\t\t\t").append("-");
                taulukkotulostus.append("\n");
            }

            if (onkoPlayground > 0){
                taulukkotulostus.append("Leikkipaikka").append("\t\t\t\t\t\t").append(onkoPlayground);
                taulukkotulostus.append("\n");
            } else {
                taulukkotulostus.append("Leikkipaikka").append("\t\t\t\t\t\t").append("-");
                taulukkotulostus.append("\n");
            }

            if (onkoVideo > 0){
                taulukkotulostus.append("Videonäyttömahdollisuus").append("\t\t\t\t").append(onkoVideo);
                taulukkotulostus.append("\n");
            } else {
                taulukkotulostus.append("Videonäyttömahdollisuus").append("\t\t\t\t").append("-");
                taulukkotulostus.append("\n");
            }

            if (onkoSmoking > 0){
                taulukkotulostus.append("Tupakointipaikka").append("\t\t\t\t\t").append(onkoSmoking);
                taulukkotulostus.append("\n");
            } else {
                taulukkotulostus.append("Tupakointipaikka").append("\t\t\t\t\t").append("-");
                taulukkotulostus.append("\n");
            }

            if (onkoLuggage > 0){
                taulukkotulostus.append("Matkatavarasäilytysmahdollisuus").append("\t\t").append(onkoLuggage);
                taulukkotulostus.append("\n");
            } else {
                taulukkotulostus.append("Matkatavarasäilytysmahdollisuus").append("\t\t").append("-");
                taulukkotulostus.append("\n");
            }
            taulukkotulostus.append("\n");
            taulukkotulostus.append("\n");

            return taulukkotulostus;


        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }

    }

    //Metodi jatkokehittelee yllä esiteltyä haeJunanPalvelut-metodia. Tässä metodissa kootaan yksittäisen junan palveluiden lisäksi
    //myös teknisempiä tietoja junasta
    public void haeYksityiskohtaisetTiedot (String paiva, int junanumero){

        try {

            Juna juna = haeJuna(paiva, junanumero);

            if (juna == null){
                throw new Exception("Junan tietoja ei ole saatavilla.");
            }

            DateFormat lahtopaivaaVarten = new SimpleDateFormat("yyyy-MM-dd");

            StringBuilder yksityiskohtaiset = new StringBuilder();
            yksityiskohtaiset.append("Alla on tiedot junalle numero " + juna.getTrainNumber() + ", lähtöpäivänä " + lahtopaivaaVarten.format(juna.getDepartureDate()));
            yksityiskohtaiset.append("\n");
            yksityiskohtaiset.append("\n");
            yksityiskohtaiset.append("JUNATIETO                     ARVO");
            yksityiskohtaiset.append("\n");
            yksityiskohtaiset.append("--------------------------------------------------------------------------------------");
            yksityiskohtaiset.append("\n");
            yksityiskohtaiset.append("Operaattori:").append("\t\t\t\t").append(juna.getOperatorShortCode());
            yksityiskohtaiset.append("\n");
            yksityiskohtaiset.append("Operaattorin UIC-koodi:").append("\t\t").append(juna.getOperatorUICCode());
            yksityiskohtaiset.append("\n");
            if (juna.getTrainCategory().equals("Long-distance")){
                yksityiskohtaiset.append("Junakategoria:").append("\t\t\t\t").append("Kaukoliikenne");
                yksityiskohtaiset.append("\n");
            } else {
                yksityiskohtaiset.append("Junakategoria:").append("\t\t\t\t").append("Lähiliikenne");
                yksityiskohtaiset.append("\n");
            }
            yksityiskohtaiset.append("Junatyyppi:").append("\t\t\t\t\t").append(juna.getTrainType());
            yksityiskohtaiset.append("\n");
            yksityiskohtaiset.append("Junan kokonaispituus:").append("\t\t").append(juna.getJourneySections().get(0).getTotalLength() + " metriä");
            yksityiskohtaiset.append("\n");
            yksityiskohtaiset.append("Maksiminopeus:").append("\t\t\t\t").append(juna.getJourneySections().get(0).getMaximumSpeed()).append(" km/h");
            yksityiskohtaiset.append("\n");
            yksityiskohtaiset.append("--------------------------------------------------------------------------------------");
            yksityiskohtaiset.append("\n");
            yksityiskohtaiset.append("\n");


            //luodaan Stringbuilder, johon lisätään metodikutsuilla vaunujen palvelutiedot ja veturi-info
            StringBuilder vaunuJaVeturitiedot = new StringBuilder();
            vaunuJaVeturitiedot.append(haeJunanPalvelut(paiva, junanumero));
            vaunuJaVeturitiedot.append(haeVeturitiedot(paiva, junanumero));

            StringBuilder kaikki = new StringBuilder();
            kaikki.append(yksityiskohtaiset).append(vaunuJaVeturitiedot);
            System.out.println(kaikki);

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    //Tämä metodi palauttaa StringBuilder-muodossa tietyn junan veturiin liittyviä tietoja
    public StringBuilder haeVeturitiedot (String paiva, int junanumero) {

        try {

            Juna juna = haeJuna(paiva, junanumero);

            if (juna == null) {
                throw new Exception("Junan tietoja ei ole saatavilla.");
            }


            // Luodaan lista vaunut, josta löytyy yksittäisten vaunujen tiedot (Huom! Veturi ei ole näissä mukana.)
            List<Locomotives> veturi = juna.getJourneySections().get(0).getLocomotives();

            StringBuilder veturitaulukko = new StringBuilder();
            veturitaulukko.append("VETURITIETO                         ARVO");
            veturitaulukko.append("\n");
            veturitaulukko.append("--------------------------------------------------------------------------------------");
            veturitaulukko.append("\n");


            if (veturi.get(0).location < 2) {
                veturitaulukko.append("Veturin sijainti:").append("\t\t\t\t").append("Kokoonpanon kärjessä");
                veturitaulukko.append("\n");
            } else {
                veturitaulukko.append("Veturin sijainti:").append("\t\t\t\t").append("Kokoonpanon viimeisenä");
                veturitaulukko.append("\n");
            }

            veturitaulukko.append("Veturin tyyppi:").append("\t\t\t\t\t").append(veturi.get(0).locomotiveType);
            veturitaulukko.append("\n");
            veturitaulukko.append("Veturin vetovoimalaji:").append("\t\t\t").append(veturi.get(0).powerType);
            veturitaulukko.append("\n");
            veturitaulukko.append("--------------------------------------------------------------------------------------");

            return veturitaulukko;

        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }
}



