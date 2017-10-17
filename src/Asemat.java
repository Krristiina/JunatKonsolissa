import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Date;

/**
 * Created by Administrator on 17.10.2017.
 */
public class Asemat {

    /*Tällä metorilla haetaan tietyn
    aseman data annetun urlin perusteella*/
    private static void lueAsemanJSONData(String url){
        String URLI = url;
        try{
            URL urli = new URL(URLI);
            ObjectMapper mapper = new ObjectMapper();
        } catch (MalformedURLException e) {
            System.err.println("väärä urli")
            ;e.printStackTrace();
        }
    }

    /*Tämä metodi palauttaa taulukkotulosteen
    tietyn aseman lahtevista junista juuri tällä hetkellä*/
    public void lahtevatJunatNyt(String asema){

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
