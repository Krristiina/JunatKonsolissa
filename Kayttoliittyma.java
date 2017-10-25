import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * Created by Administrator on 17.10.2017.
 */
public class Kayttoliittyma {
    public static void main(String[] args) {
        kaynnista();
    }
    private static void kaynnista() {
        Scanner lukija = new Scanner(System.in);
        System.out.println();
        System.out.println("             0 o oO o o Oo o  o OO oOO OOOO0000");
        System.out.println("         0 O o 00 o o Oo oOo 0 o  o OO OOO00 O oOO o");
        System.out.println("        o o  o    ");
        System.out.println("        o   ______");
        System.out.println("       ___ [_____]");
        System.out.println("       | /  | o |     ");
        System.out.println("      _| |__| T |   1 _____________    _____________    _____________    _____________");
        System.out.println("     (          |____|           |____|           |____|           |____|           |     ");
        System.out.println("    /|/\\~~/\\ ~/\\|==|=| .-.   .-. |==|=| .-.   .-. |==|=| .-.   .-. |==|=| .-.   .-. |");
        System.out.println("   / |\\/^~\\/^~\\/      ( O ) ( O )      ( O ) ( O )      ( O ) ( O )      ( O ) ( O )      ");
        System.out.println(" =V=a=l=t=i=o=n=R=a=i=d=e=l=i=i=k=e=n=t=e=e=n=A=i=k=a=t=a=u=l=u=J=a=R=e=i=t=t=iP=a=l=v=e=l=u      ");
        System.out.println();
        System.out.println("---------------------------------------------------");
        System.out.println("VALTION RAIDELIIKENTEEN AIKATAULU- JA REITTIPALVELU");
        System.out.println("---------------------------------------------------");


        while (true) {
            System.out.println("Tervetuloa!\n\nOlkaa hyvä, ja valitkaa haluamanne palvelu: \n1. Reittihakupalvelu \n2. Junatietopalvelu \n3. Asematietopalvelu \n4. Aikataulutarkistuspalvelu\n5. Lopeta ohjelma");
            String vastaus = lukija.nextLine();
            if ("1".equals(vastaus)) {
                System.out.println("Kiitos vastauksestanne, prosessoidaan...\n");
                haeReittijuna();
            } else if ("2".equals(vastaus)) {
                System.out.println("Kiitos vastauksestanne, prosessoidaan...\n");
                haeJunaTiedot();
            } else if ("3".equals(vastaus)) {
                System.out.println("Kiitos vastauksestanne, prosessoidaan...\n");
                asemaTiedot();
            } else if ("4".equals(vastaus)) {
                System.out.println("Kiitos vastauksestanne, prosessoidaan...\n");
                onkoMyohassa();
            } else if ("5".equals(vastaus)) {
                System.out.println("Kiitos käynnistänne!  \nSammuu...");
                System.exit(0);
            } else {
                System.out.println("Olkaa hyvä ja valitkaa olemassa olevista vaihtoehdoista (1, 2, 3, 4 tai 5)\n\n");
                continue;
            }
        }
    }
    public static void etsiAsemaa() {
        Scanner in = new Scanner(System.in);
        System.out.println("Tiedättekö haluamanne juna-aseman lyhyen kirjainkoodin?\n1. Tiedän kyllä\n2. En tiedä");
        String vastaus = in.nextLine();
        if ("1".equals(vastaus)) {
            System.out.println("Hyvä!");
            return;
        } else if ("2".equals(vastaus)) {
            System.out.println("Valitkaa haluamanne toiminto: \n1. Tulosta kaikki asemat ja kirjainkoodit. \n2. Etsi kirjainkoodi aseman nimen perusteella.");
            String valinta = in.nextLine();
            if ("1".equals(valinta)) {
                Koodinmurtaja.tulostaKaikki();
            } else if ("2".equals(valinta)) {
                System.out.println("Syöttäkää etsimänne aseman nimi tai alkukirjaimet:");
                String alkukirjaimet = in.nextLine();
                Koodinmurtaja.haeAsemaKoodiaKirjaimilla(alkukirjaimet);
            } else if (!"1".equals(valinta) && !"2".equals(valinta)) {
                System.out.println("Ette valinnut mitään järkevää, taidattekin tietää jo koodin, jatketaan siis.");
                return;
            }
        } else if (!"1".equals(vastaus) && !"2".equals(vastaus)) {
            System.out.println("Ette valinnut mitään järkevää, taidattekin tietää jo koodin, jatketaan siis.");
            return;
        }
    }
    /*Metodi pyytää käyttäjää syöttämään toivotun lähtö ja saapumispaikan. Tämän jälkeen
    * kutsuu Reittihaku-luokan haeJunatReitille metodia joka tulostaa seuraavat 5 lähtöä*/
    public static void haeReittijuna() {
        etsiAsemaa();
        String lahto = null;
        String saapu = null;
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Syöttäkää lähtöasema: ");
            lahto = sc.nextLine();
            System.out.println("Syöttäkää pääteasema: ");
            saapu = sc.nextLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Selvä, prosessoidaan...\n");
        Reittihaku.haeJunatReitille(lahto, saapu);
        System.out.println("\nHaluatteko jatkaa Reittiopaspalvelussa? \n1. Kyllä \n2. En");
        String vastaus = sc.nextLine();
        if ("1".equals(vastaus)) {
            haeReittijuna();
        }
        if ("2".equals(vastaus)) {
            kaynnista();
            return;
        }
        if(!"2".equals(vastaus)&&!"1".equals(vastaus)) {
            System.out.println("palataan alkuun...");
            kaynnista();
            return;
        }
    }
    public static void onkoMyohassa() {
        String junanumero = null;
        Scanner sc = new Scanner(System.in);
        try {
            MyohastynytJaSyy.onkoJunaAikataulussa();
            MyohastynytJaSyy.syyMyohastys(0);
        } catch (InputMismatchException exception) {
            System.out.println("Tällä merkkiyhdistelmällä ei löydy junia");
        }
        System.out.println("\nHaluatteko jatkaa Aikatauluntarkistuspalvelussa? \n1. Kyllä \n2. En");
        String vastaus = sc.nextLine();
        if ("1".equals(vastaus)) {
            onkoMyohassa();
        }
        if ("2".equals(vastaus)) {
            kaynnista();
            return;
        }
        if(!"2".equals(vastaus)&&!"1".equals(vastaus)) {
            System.out.println("palataan alkuun...");
            kaynnista();
            return;
        }
    }
    public static void asemaTiedot(){
        Asemat asemaOhjelma = new Asemat();
        Scanner in = new Scanner(System.in);
        System.out.println("Haluatteko tiedustella \n1. lähteviä junia? \n2. saapuvia junia?");
        String luku = in.nextLine();
        if("1".equals(luku)){
            etsiAsemaa();
            System.out.println("Syöttäkää haluamanne aseman nimi:");
            String nimi = in.nextLine();
            System.out.println("Selvä, prosessoidaan...\n");
            asemaOhjelma.lahtevatJunatNyt(nimi);
        }
        if("2".equals(luku)){
            etsiAsemaa();
            System.out.println("Syöttäkää haluamanne aseman nimi:");
            String nimi = in.nextLine();
            System.out.println("Selvä, prosessoidaan...\n");
            asemaOhjelma.saapuvatJunatNyt(nimi);
        }
        System.out.println("\nHaluatteko jatkaa Asematietopalvelussa? \n1. Kyllä \n2. En");
        String vastaus = in.nextLine();
        if ("1".equals(vastaus)) {
            asemaTiedot();
        }
        if ("2".equals(vastaus)) {
            kaynnista();
        } if(!"2".equals(vastaus)&&!"1".equals(vastaus)) {
            System.out.println("palataan alkuun...");
            kaynnista();
        }
    }
    // Tämä metodi hakee tiedot yksittäisen junan palveluista sekä vaunuista, joissa palvelut sijaitsevat
    //Lisäksi on mahdollista hakea kaikki tietyn päivän kokoonpanot
    public static void haeJunaTiedot(){
        Scanner lukija = new Scanner(System.in);
        try {
            System.out.println("Haluatteko tiedustella \n1. yksittäisen junan tietoja? \n2. tietyn päivän kaikkia junakokoonpanoja?");
            String luku = lukija.nextLine();
            if("1".equals(luku)){
                System.out.println("Syötä junan numero:");
                int numero = Integer.parseInt(lukija.nextLine());
                System.out.println("Syötä lähtöpäivä muodossa VVVV-KK-PP:");
                String päivä =lukija.nextLine();
                JunaInfo juna = new JunaInfo();
                juna.haeYksityiskohtaisetTiedot(päivä, numero);
            }
            if("2".equals(luku)){
                System.out.println("Syötä lähtöpäivä muodossa VVVV-KK-PP:");
                String päivä =lukija.nextLine();
                JunaInfo juna = new JunaInfo();
                juna.haeKaikkiPaivanKokoonpanot(päivä);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("\nHaluatteko jatkaa Junatietopalvelussa? \n1. Kyllä \n2. En");
        String vastaus = lukija.nextLine();
        if ("1".equals(vastaus)) {
            haeJunaTiedot();
        }
        if ("2".equals(vastaus)) {
            kaynnista();
        } if(!"2".equals(vastaus)&&!"1".equals(vastaus)) {
            System.out.println("palataan alkuun...");
            kaynnista();
        }
    }
}