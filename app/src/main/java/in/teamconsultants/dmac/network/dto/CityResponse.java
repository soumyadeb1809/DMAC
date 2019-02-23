package in.teamconsultants.dmac.network.dto;

import java.util.List;

import in.teamconsultants.dmac.model.City;

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
