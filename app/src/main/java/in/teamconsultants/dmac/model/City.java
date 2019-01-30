package in.teamconsultants.dmac.model;

public class City {

    private String CityCode;
    private String CityName;
    private String StateName;
    private String StateCode;

    public City() {
    }

    public City(String cityCode, String cityName, String stateName, String stateCode) {
        CityCode = cityCode;
        CityName = cityName;
        StateName = stateName;
        StateCode = stateCode;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }
}
