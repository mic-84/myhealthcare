package it.unipi.lsmsd.myhealthcare.model;

public class StructureReview extends Review {
    private User user;

    public StructureReview(){}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString(){
        return "user " + user.getUsername() + ", rating " + rating + ", " + date
                + " " + text;
    }
}
