package it.unipi.lsmsd.myhealthcare.service;

public class GraphicUtility {
    public static String getStar(int dimension){
        return "<img src=\"/resources/img/star.png\" height=\""
                + dimension + "\" width=\"" + dimension + "\">";
    }

    public static String getEmptyStar(int dimension){
        return "<img src=\"/resources/img/empty_star.png\" height=\""
                + dimension + "\" width=\"" + dimension + "\">";
    }
}
