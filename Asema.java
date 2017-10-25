import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Created by Minna Vares on 17.10.2017.
 * Tämä on asema luokka, jolla on muuttujina nimi, ja nimikoodi,
 * käytetään Koodinmurtajaluokassa
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Asema {
    String stationName;
    String stationShortCode;
    public String getStationName() {
        return stationName;
    }
    public String getStationShortCode() {
        return stationShortCode;
    }
    @Override
    public String toString() {
        return "Asema{" +
                "stationName='" + stationName + '\'' +
                ", stationShortCode='" + stationShortCode + '\'' +
                '}';
    }
}