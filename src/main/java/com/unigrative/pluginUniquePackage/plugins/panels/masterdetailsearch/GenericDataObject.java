package com.unigrative.pluginUniquePackage.plugins.panels.masterdetailsearch;

import com.fbi.fbdata.FBData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericDataObject implements FBData {

    private static final Logger LOGGER = LoggerFactory.getLogger((Class)GenericDataObject.class);

    private int id; //UNIQUE ID FIELD FOR THE OBJECT
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
        return this.getName().compareTo(((GenericDataObject)o).getName());
    }
}
