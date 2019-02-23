package in.teamconsultants.dmac.network.dto;

import java.util.List;

import in.teamconsultants.dmac.model.State;

public class StateResponse {
    private List<State> StateList;

    public StateResponse() {
    }

    public StateResponse(List<State> stateList) {
        StateList = stateList;
    }

    public List<State> getStateList() {
        return StateList;
    }

    public void setStateList(List<State> stateList) {
        StateList = stateList;
    }
}
