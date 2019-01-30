package in.teamconsultants.dmac.model;

public class State {
    private String StateCode;
    private String StateName;

    public State() {
    }

    public State(String stateCode, String stateName) {
        StateCode = stateCode;
        StateName = stateName;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }
}
