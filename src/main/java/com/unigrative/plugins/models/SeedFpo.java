package com.unigrative.plugins.models;

import com.fbi.fbdata.FbEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "Seed") //TODO: CHANGE
@Table(name = "gcs_seed", uniqueConstraints = { @UniqueConstraint(columnNames = { "commonName" }) })
public class SeedFpo extends FbEntity<SeedFpo> {

    public static final int COL_LENGTH_NAME = 60;
    public static final int COL_LENGTH_SCIENTIFIC = 252;

    @Column(length = COL_LENGTH_NAME, nullable = false)
    @Length(max = COL_LENGTH_NAME)
    private String commonName;

    @Column(length = COL_LENGTH_SCIENTIFIC)
    @Length(max = COL_LENGTH_SCIENTIFIC)
    private String scientificName;

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
    public int compareTo(SeedFpo seedFpo) {
        return this.getCommonName().compareTo(seedFpo.getCommonName());
    }
}
