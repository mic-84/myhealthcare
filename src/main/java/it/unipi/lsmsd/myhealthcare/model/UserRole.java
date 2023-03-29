package it.unipi.lsmsd.myhealthcare.model;

public class UserRole {
    private String role;
    private Structure structure;

    public UserRole(String role, Structure structure){
        this.role = role;
        this.structure = structure;
    }
    public String getRole() { return role;}
    public Structure getStructure() { return structure;}
}
