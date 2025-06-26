package Models.Entities;

public class Department {
    private String departmentName;
    private int electiveId;

    public Department(String departmentName, int electiveId) {
        this.departmentName = departmentName;
        this.electiveId = electiveId;
    }

    public String getDepartmentName() {
        return departmentName;
    }


    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getElectiveId() {
        return electiveId;
    }

    public void setElectiveId(int electiveId) {
        this.electiveId = electiveId;
    }

}