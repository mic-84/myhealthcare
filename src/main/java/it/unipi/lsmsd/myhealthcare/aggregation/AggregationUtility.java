package it.unipi.lsmsd.myhealthcare.aggregation;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.service.GraphicUtility;

import java.util.List;

public class AggregationUtility {
    public static List<BasicAggregation> getReviewsPerRating(List<BasicAggregation> list){
        for(int i=0; i<list.size(); i++) {
            list.get(i).setDescription("rate " + list.get(i).getId());
            String graphic = "";
            for(int j=0; j<Integer.valueOf(list.get(i).getId()); j++)
                graphic += GraphicUtility.getStar(16);
            for(int j=Integer.valueOf(list.get(i).getId())+1; j<=5; j++)
                graphic += GraphicUtility.getEmptyStar(16);
            list.get(i).setGraphic(graphic);
        }

        return list;
    }

    public static List<BasicAggregation> getBookingsPerMonth(List<BasicAggregation> list){
        for(int i=0; i<list.size(); i++)
            list.get(i).setDescription(MyHealthCareApplication.months.get(
                    Integer.valueOf(list.get(i).getId())));

        return list;
    }

    public static List<BasicAggregation> getBasicAggregation(List<BasicAggregation> list){
        for(int i=0; i<list.size(); i++)
            list.get(i).setDescription(list.get(i).getId());

        return list;
    }
}
