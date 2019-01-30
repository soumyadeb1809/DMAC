package in.teamconsultants.dmac.model;

import java.util.List;

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
