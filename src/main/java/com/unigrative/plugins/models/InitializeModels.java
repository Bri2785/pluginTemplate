package com.unigrative.plugins.models;

import com.unigrative.plugins.GenericPlugin;
import com.unigrative.plugins.util.sql.SqlUtil;

public  class InitializeModels {

    public static void init(GenericPlugin genericPlugin) {

        SqlUtil.createTableFromFile("gcs_seed", "/searchSqlQueries/create_gcs_table1.sql", genericPlugin.getPluginEveManager());
    }

}
