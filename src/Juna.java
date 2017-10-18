import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 17.10.2017.
 */
public class Juna {
    boolean cancelled;
    String commuterLineID;
    //LocalDate departureDate;  // Jackson ei oikein pidä Java 8 päivistä oletuksena
    Date departureDate;
    String operatorShortCode;
    int operatorUICCode;
    boolean runningCurrently;
    List<TimeTableRow> timeTableRows; // luodaan uusi luokka TimeTableRow
    Date timetableAcceptanceDate;
    String timetableType;
    String trainCategory;
    int trainNumber;
    String trainType;
    long version;
    List<journeySections> journeySections; // luodaan uusi luokka journeySections (lisätietoa luokasta luokan kohdalla)



    @Override
    public String toString() {
        return "Juna{" + "cancelled=" + cancelled + ", commuterLineID='" + commuterLineID + '\'' + ", departureDate=" + departureDate + ", operatorShortCode='" + operatorShortCode + '\'' + ", operatorUICCode=" + operatorUICCode + ", runningCurrently=" + runningCurrently + ", timeTableRows=" + timeTableRows + ", timetableAcceptanceDate=" + timetableAcceptanceDate + ", timetableType='" + timetableType + '\'' + ", trainCategory='" + trainCategory + '\'' + ", trainNumber=" + trainNumber + ", trainType='" + trainType + '\'' + ", version=" + version + '}';
    }

    public List<journeySections> getJourneySections() {
        return journeySections;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getCommuterLineID() {
        return commuterLineID;
    }

    public void setCommuterLineID(String commuterLineID) {
        this.commuterLineID = commuterLineID;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getOperatorShortCode() {
        return operatorShortCode;
    }

    public void setOperatorShortCode(String operatorShortCode) {
        this.operatorShortCode = operatorShortCode;
    }

    public int getOperatorUICCode() {
        return operatorUICCode;
    }

    public void setOperatorUICCode(int operatorUICCode) {
        this.operatorUICCode = operatorUICCode;
    }

    public boolean isRunningCurrently() {
        return runningCurrently;
    }

    public void setRunningCurrently(boolean runningCurrently) {
        this.runningCurrently = runningCurrently;
    }

    public List<TimeTableRow> getTimeTableRows() {
        return timeTableRows;
    }

    public void setTimeTableRows(List<TimeTableRow> timeTableRows) {
        this.timeTableRows = timeTableRows;
    }

    public Date getTimetableAcceptanceDate() {
        return timetableAcceptanceDate;
    }

    public void setTimetableAcceptanceDate(Date timetableAcceptanceDate) {
        this.timetableAcceptanceDate = timetableAcceptanceDate;
    }

    public String getTimetableType() {
        return timetableType;
    }

    public void setTimetableType(String timetableType) {
        this.timetableType = timetableType;
    }

    public String getTrainCategory() {
        return trainCategory;
    }

    public void setTrainCategory(String trainCategory) {
        this.trainCategory = trainCategory;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class TimeTableRow {

    String stationShortCode;
    String stationUICCode;
    String countryCode;
    String type;
    boolean trainStopping;
    boolean commercialStop;
    int commercialTrack;
    boolean cancelled;
    Date scheduledTime;
    Date actualTime;
    int differenceInMinutes;
    String wagonType;
    private List<Causes> causes; //luodaan uusi luokka Causes (lisätietoa luokasta luokan kohdalla)



    public String getWagonType() {
        return wagonType;
    }

    public void setWagonType(String wagonType) {
        this.wagonType = wagonType;
    }


    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    public void setStationUICCode(String stationUICCode) {
        this.stationUICCode = stationUICCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTrainStopping(boolean trainStopping) {
        this.trainStopping = trainStopping;
    }

    public void setCommercialStop(boolean commercialStop) {
        this.commercialStop = commercialStop;
    }

    public void setCommercialTrack(int commercialTrack) {
        this.commercialTrack = commercialTrack;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public void setActualTime(Date actualTime) {
        this.actualTime = actualTime;
    }

    public void setDifferenceInMinutes(int differenceInMinutes) {
        this.differenceInMinutes = differenceInMinutes;
    }

    public String getStationShortCode() {
        return stationShortCode;
    }

    public String getStationUICCode() {
        return stationUICCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getType() {
        return type;
    }

    public boolean isTrainStopping() {
        return trainStopping;
    }

    public boolean isCommercialStop() {
        return commercialStop;
    }

    public int getCommercialTrack() {
        return commercialTrack;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public Date getActualTime() {
        return actualTime;
    }

    public int getDifferenceInMinutes() {
        return differenceInMinutes;
    }


}

// Luokka 'journeySections' pitää sisällään yksittäiseen junaan liittyviä tietoja
@JsonIgnoreProperties(ignoreUnknown = true)
class journeySections {
    int maximumSpeed;
    private List<wagons> wagons;
    List<TimeTableRow> locomotives;

    public List<wagons> getWagons() {
        return wagons;
    }

    public int getMaximumSpeed() {
        return maximumSpeed;
    }

}
// Luokka 'wagons' on luokan 'journeySections' eräänlainen "alaluokka", eli se pitää sisällään yksittäisen junan vaunuihin liittyviä tietoja
@JsonIgnoreProperties(ignoreUnknown = true)
class wagons {

    int length;
    boolean playground;
    boolean video;
    boolean disabled;
    boolean catering;
    boolean pet;
    int location;
    int salesNumber;

    public int getSalesNumber() {
        return salesNumber;
    }

    public int getLocation() {
        return location;
    }


    public int getLength() {
        return length;
    }


    public boolean isPlayground() {
        return playground;
    }


    public boolean isVideo() {
        return video;
    }


    public boolean isDisabled() {
        return disabled;
    }


    public boolean isCatering() {
        return catering;
    }


    public boolean isPet() {
        return pet;
    }

}

// Luokka 'Causes' on luokan 'TimeTableRows' eräälainen alaluokka
// Luokka pitää sisällään muuttujia, joiden avulla voidaan selvittää myöhässä olevan junan myöhästymissyy
@JsonIgnoreProperties(ignoreUnknown = true)
class Causes {
    private int detailedCategoryCodeId;
    public int getdetailedCategoryCodeId() {
        return detailedCategoryCodeId;
    }
    public void setdetailedCategoryCodeId(int detailedCategoryCodeId) {
        this.detailedCategoryCodeId = detailedCategoryCodeId;
    }
}

