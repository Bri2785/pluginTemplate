package com.unigrative.plugins.panels.masterdetailsearch.details.table;

import com.fbi.fbdata.FBData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericItemDataObject implements FBData {

    private static final Logger LOGGER = LoggerFactory.getLogger((Class)GenericItemDataObject.class);

    private int id; //TODO UNIQUE ID FIELD FOR THE OBJECT
    private String name; //Generic field for example


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Object o) {
        return this.name.compareTo(((GenericItemDataObject)o).getName());
    }
}
