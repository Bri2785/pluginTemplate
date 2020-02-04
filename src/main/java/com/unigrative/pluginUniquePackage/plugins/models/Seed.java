package com.unigrative.pluginUniquePackage.plugins.models;

import com.fbi.fbdata.FBData;
import com.fbi.util.exception.CodeErrorConst;
import com.fbi.util.exception.CodeObjectConst;
import com.fbi.util.exception.ExceptionMainFree;

public class Seed implements FBData<Seed> //,FBDataAutoMethods
{
    private int id;
    private String commonName;
    private String scientificName;


    public Seed() {
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int i) {
        this.id = i;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    @Override
    public int compareTo(Seed seed) {
        return this.commonName.compareTo(seed.getCommonName());
    }

    public void validate() {
        if (this.getCommonName() == null || this.getCommonName().isEmpty()) {
            throw new ExceptionMainFree(CodeObjectConst.BLANK, CodeErrorConst.MISSING_INFORMATION_LISTED, new Object[]{this.getId(), "CommonName"});
        }
    }
}
