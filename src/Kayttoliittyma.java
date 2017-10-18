import java.util.Scanner;

/**
 * Created by Administrator on 17.10.2017.
 */
public class Kayttoliittyma {
    public static void main(String[] args) {

        HaeReittijuna();
    }

    /*Metodi pyytää käyttäjää syöttämään toivotun lähtö ja saapumispaikan. Tämän jälkeen
    * kutsuu Reittihaku-luokan haeJunatReitille metodia joka tulostaa seuraavat 5 lähtöä*/
    public static void HaeReittijuna(){
        String lahto = null;
        String saapu = null;
        Scanner sc = new Scanner(System.in);

        try{
            System.out.println("Syötä lähtöasema: ");
            lahto = sc.nextLine();
            System.out.println("Syötä pääteasema: ");
            saapu = sc.nextLine();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Reittihaku.haeJunatReitille(lahto, saapu);

    }

}
