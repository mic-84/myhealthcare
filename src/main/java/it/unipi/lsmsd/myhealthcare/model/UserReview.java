package it.unipi.lsmsd.myhealthcare.model;

public class UserReview extends Review {
    private Structure structure;

    public UserReview(){}

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public String toString(){
        return "structure "
                + structure.getName() + ", rating " + rating + ", " + date
                + " " + text;
    }
}
