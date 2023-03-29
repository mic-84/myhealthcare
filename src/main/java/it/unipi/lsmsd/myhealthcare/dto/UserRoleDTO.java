package it.unipi.lsmsd.myhealthcare.dto;

public class UserRoleDTO {
    private String role;
    private StructureNoDetailsDTO structure;

    public UserRoleDTO(String role, StructureNoDetailsDTO structure){
        this.role = role;
        this.structure = structure;
    }
    public String getRole() {
        return role;
    }

    public StructureNoDetailsDTO getStructure() {
        return structure;
    }
}
