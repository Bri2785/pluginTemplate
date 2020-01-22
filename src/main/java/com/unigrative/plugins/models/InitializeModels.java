package com.unigrative.plugins.models;

import com.unigrative.plugins.Plugin;
import com.unigrative.plugins.util.sql.SqlUtil;

public  class InitializeModels {

    public static void init(Plugin plugin) {

        SqlUtil.createTableFromFile("gcs_seed", "create_gcs_table1.sql", plugin.getPluginEveManager());
    }

}
