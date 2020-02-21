// 
// Decompiled by Procyon v0.5.30
// 

package com.unigrative.plugins.apiExtension.util.property;

import com.unigrative.plugins.apiExtension.util.property.reader.StringReader;

public final class Property
{
    private static final StringReader STRING;
//    private static final BooleanReader BOOLEAN_DEFAULT_FALSE;
//    private static final BooleanReader BOOLEAN_DEFAULT_TRUE;
//    private static final DoubleReader DOUBLE;
//    private static final IntegerReader INTEGER;
//    private static final LongReader LONG;

    public static final com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String> USERNAME;
    public static final com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String> PASSWORD;
    public static final com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String> SQL_CONNECTION_URL;

    public static final com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String> CC_API_KEY;
    public static final com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String> CC_TOKEN;
    public static final com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String> LAST_SYNC_TIME;
    public static final com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String> CAMPAIGN_CREATED_DATE;

    
    static {
        STRING = new StringReader();
//        BOOLEAN_DEFAULT_FALSE = new BooleanReader(false);
//        BOOLEAN_DEFAULT_TRUE = new BooleanReader(true);
//        DOUBLE = new DoubleReader(0.0);
//        INTEGER = new IntegerReader();
//        LONG = new LongReader();
        USERNAME = new com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String>("MSSQLUsername", Property.STRING);
        PASSWORD = new com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String>("MSSQLPassword", Property.STRING);
        SQL_CONNECTION_URL = new com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String>("MSSQLConnectionString", Property.STRING);

        CC_API_KEY = new com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String>("ConstantContactAPIKey", Property.STRING);
        CC_TOKEN = new com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String>("ConstantContactToken", Property.STRING);
        LAST_SYNC_TIME = new com.unigrative.plugins.apiExtension.util.property.GlobalProperty<String>("ConstantContactSyncTime", Property.STRING);
        CAMPAIGN_CREATED_DATE = new GlobalProperty<String>("ConstantContactCampaignCreatedDate", Property.STRING);
    }
}
