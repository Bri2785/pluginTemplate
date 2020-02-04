package com.unigrative.pluginUniquePackage.plugins.models;

import com.unigrative.pluginUniquePackage.plugins.GenericPlugin;
import com.unigrative.pluginUniquePackage.plugins.util.sql.SqlUtil;

public  class InitializeModels {

    public static void init(GenericPlugin genericPlugin) {

        SqlUtil.createTableFromFile("gcs_seed", "/searchSqlQueries/create_gcs_table1.sql", genericPlugin.getPluginEveManager());
    }

}
