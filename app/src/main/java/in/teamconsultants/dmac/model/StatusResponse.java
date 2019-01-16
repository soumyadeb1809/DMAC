package in.teamconsultants.dmac.model;

import java.util.ArrayList;

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
