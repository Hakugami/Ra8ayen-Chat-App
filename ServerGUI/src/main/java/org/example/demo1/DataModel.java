package org.example.demo1;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
    public List<String> simulateGetDataFromDatabase()
    {
        List<String> name = new ArrayList();

        name.add("John Doe");
        name.add("Jane Doe");
        name.add("Kim Jackson");
        name.add("James Jones");

        return name;
    }
    
}