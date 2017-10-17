import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 17.10.2017.
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
