package in.teamconsultants.dmac.model;

public class StatusObj {

    String SId;
    String ShortName;

    public StatusObj() {
    }

    public StatusObj(String SId, String shortName) {
        this.SId = SId;
        ShortName = shortName;
    }

    public String getSId() {
        return SId;
    }

    public void setSId(String SId) {
        this.SId = SId;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }
}
