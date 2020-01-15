package com.unigrative.plugins.panels.masterdetailsearch;

import com.fbi.fbo.impl.dataexport.QueryRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Mappings {

    private static final Logger LOGGER = LoggerFactory.getLogger(Mappings.class);

    //CUSTOM MAPPING WILL HAVE TO BE MADE FOR EACH USAGE OF THE SEARCH PANEL
    public static List<GenericDataObject> QueryRowToGenericDataObject (List<QueryRow> rows){
        try {
            List<GenericDataObject> data = new ArrayList<>();
            for (QueryRow row : rows
            ) {
                GenericDataObject dataObject = new GenericDataObject();

                //ADD LINES FOR EACH PROPERTY
                //ROW fields match the sql query field names
                dataObject.setId(row.getInt("id"));
                dataObject.setName(row.getString("name"));

                data.add(dataObject);
            }
            return data;
        }
        catch (Exception e){
            LOGGER.error("Error mapping query row to Data Object",e);
            return null;
        }

    }

}
