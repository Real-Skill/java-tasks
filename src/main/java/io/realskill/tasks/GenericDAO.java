package io.realskill.tasks;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenericDAO {

    private final static String DB_URL = "";
    private Connection connection = null;

    public void establishDbConnection() {
        //CODE HERE
    }

    public void closeDbConnection() {
        //CODE HERE
    }

    public List<String> getAllFishNames() {
        List<String> result = new ArrayList<>();
        //CODE HERE
        return result;
    }

    public List<String> getFishInAquarium() {
        List<String> result = new ArrayList<>();
        //CODE HERE
        return result;
    }

    public String findOldestFish() {
        String result = "";
        //CODE HERE
        return result;
    }

    public Integer getFishAgeByName(String name) {
        Integer result = 0;
        //CODE HERE
        return result;
    }
}
