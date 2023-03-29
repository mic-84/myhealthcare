package it.unipi.lsmsd.myhealthcare.aggregation;

public class BasicAggregation {
    private String _id;
    private Long count;
    private Float cost;
    private String description;
    private String graphic;

    public BasicAggregation(){}

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGraphic() {
        return graphic;
    }

    public void setGraphic(String graphic) {
        this.graphic = graphic;
    }
}
