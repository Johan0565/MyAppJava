package Models.Entities;

public class ElectivePlan {
    private int planId;
    private int electiveId;
    private int semesterId;
    private int numberLectures;
    private int numberPractices;
    private int numberLaboratoryWork;

    public ElectivePlan(int planId, int electiveId, int semesterId, int numberLectures, int numberPractices, int numberLaboratoryWork) {
        this.planId = planId;
        this.electiveId = electiveId;
        this.semesterId = semesterId;
        this.numberLectures = numberLectures;
        this.numberPractices = numberPractices;
        this.numberLaboratoryWork = numberLaboratoryWork;
    }

    // Геттеры и сеттеры
    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }
    public int getElectiveId() { return electiveId; }
    public void setElectiveId(int electiveId) { this.electiveId = electiveId; }
    public int getSemesterId() { return semesterId; }
    public void setSemesterId(int semesterId) { this.semesterId = semesterId; }
    public int getNumberLectures() { return numberLectures; }
    public void setNumberLectures(int numberLectures) { this.numberLectures = numberLectures; }
    public int getNumberPractices() { return numberPractices; }
    public void setNumberPractices(int numberPractices) { this.numberPractices = numberPractices; }
    public int getNumberLaboratoryWork() { return numberLaboratoryWork; }
    public void setNumberLaboratoryWork(int numberLaboratoryWork) { this.numberLaboratoryWork = numberLaboratoryWork; }

    @Override
    public String toString() {
        return "ElectivePlan{id=" + planId + ", electiveId=" + electiveId + ", semesterId=" + semesterId + "}";
    }
}