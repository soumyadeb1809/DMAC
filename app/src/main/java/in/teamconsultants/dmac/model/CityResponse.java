package in.teamconsultants.dmac.model;

import java.util.List;

public class CityResponse {

    private List<City> CityList;

    public CityResponse() {
    }

    public CityResponse(List<City> cityList) {
        CityList = cityList;
    }

    public List<City> getCityList() {
        return CityList;
    }

    public void setCityList(List<City> cityList) {
        CityList = cityList;
    }
}
