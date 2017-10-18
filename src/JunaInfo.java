import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 17.10.2017.
 */
public class JunaInfo {
    String paiva;
    int junanumero;
    Juna juna;

    public JunaInfo(String paiva, int junanumero) {
        this.paiva = paiva;
        this.junanumero = junanumero;
    }



    public static void main(String[] args) {

        JunaInfo juna = new JunaInfo("2017-10-17", 1);

        juna.haeJunanPalvelut();
    }



    public Juna haeJuna (){

        String baseurl = "https://rata.digitraffic.fi/api/v1/";

        try {

            URL url = new URL(baseurl+"/compositions/" + this.paiva +"/" + this.junanumero);
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


        public void haeJunanPalvelut (){

        try {

            Juna juna = haeJuna();

            if (juna == null){
                throw new Exception("Junaa ei löydy.");
            }

            // Luodaan lista vaunut, josta löytyy yksittäisten vaunujen tiedot (Huom! Veturi ei ole näissä mukana.)
            List<wagons> vaunut = juna.getJourneySections().get(0).getWagons();

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
            }

            if (onkoCatering > 0){
                System.out.println("Junassa on ravintolaosasto, joka löytyy vaunusta " + onkoCatering);
            } else {
                System.out.println("Junassa ei ole ravintolavaunua.");
            }

            if (onkoPet > 0){
                System.out.println("Junassa on eläinlaosasto, joka löytyy vaunusta " + onkoPet);
            } else {
                System.out.println("Junassa ei ole eläinosastoa.");
            }

            if (onkoDisabled > 0){
                System.out.println("Junassa on liikuntarajoitteisten osasto, joka löytyy vaunusta " + onkoDisabled);
            } else {
                System.out.println("Junassa ei ole liikuntarajoitteisten osastoa.");
            }

            if (onkoPlayground > 0){
                System.out.println("Junassa on leikkiosasto, joka löytyy vaunusta " + onkoPlayground);
            } else {
                System.out.println("Junassa ei ole leikkiosastoa.");
            }

            if (onkoVideo > 0){
                System.out.println("Junassa on videomahdollisuus vaunussa " + onkoVideo);
            } else {
                System.out.println("Junassa ei ole videomahdollisuutta.");
            }

            // Kerrotaan käyttäjälle junan maksiminopeus:

            System.out.println("Junan maksiminopeus on " + juna.getJourneySections().get(0).getMaximumSpeed() + " km/h");



        } catch (Exception ex) {
            System.out.println(ex);
        }

    }




    }



