package in.teamconsultants.dmac.network.dto;

import java.util.ArrayList;

import in.teamconsultants.dmac.model.StatusObj;

public class StatusResponse {

    private ArrayList<StatusObj> StatusList;

    public StatusResponse() {
    }

    public StatusResponse(ArrayList<StatusObj> statusList) {
        StatusList = statusList;
    }

    public ArrayList<StatusObj> getStatusList() {
        return StatusList;
    }

    public void setStatusList(ArrayList<StatusObj> statusList) {
        StatusList = statusList;
    }
}
