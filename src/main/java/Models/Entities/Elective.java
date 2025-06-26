package Models.Entities;

public class Elective {
    private int electiveId;
    private String electiveName;

    public Elective(int electiveId, String electiveName) {
        this.electiveId = electiveId;
        this.electiveName = electiveName;
    }

    public int getElectiveId() { return electiveId; }
    public void setElectiveId(int electiveId) { this.electiveId = electiveId; }
    public String getElectiveName() { return electiveName; }
    public void setElectiveName(String electiveName) { this.electiveName = electiveName; }

    @Override
    public String toString() {
        return "Elective{id=" + electiveId + ", name='" + electiveName + "'}";
    }
}