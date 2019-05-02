// 
// Decompiled by Procyon v0.5.30
// 

package com.unigrative.plugins.util.property;

import com.unigrative.plugins.util.property.reader.StringReader;

public final class Property
{
    private static final StringReader STRING;
//    private static final BooleanReader BOOLEAN_DEFAULT_FALSE;
//    private static final BooleanReader BOOLEAN_DEFAULT_TRUE;
//    private static final DoubleReader DOUBLE;
//    private static final IntegerReader INTEGER;
//    private static final LongReader LONG;

    public static final GlobalProperty<String> USERNAME;
    public static final GlobalProperty<String> PASSWORD;
    public static final GlobalProperty<String> SQL_CONNECTION_URL;

    public static final GlobalProperty<String> CC_API_KEY;
    public static final GlobalProperty<String> CC_TOKEN;
    public static final GlobalProperty<String> LAST_SYNC_TIME;
    public static final GlobalProperty<String> CAMPAIGN_CREATED_DATE;

    
    static {
        STRING = new StringReader();
//        BOOLEAN_DEFAULT_FALSE = new BooleanReader(false);
//        BOOLEAN_DEFAULT_TRUE = new BooleanReader(true);
//        DOUBLE = new DoubleReader(0.0);
//        INTEGER = new IntegerReader();
//        LONG = new LongReader();
        USERNAME = new GlobalProperty<String>("MSSQLUsername", Property.STRING);
        PASSWORD = new GlobalProperty<String>("MSSQLPassword", Property.STRING);
        SQL_CONNECTION_URL = new GlobalProperty<String>("MSSQLConnectionString", Property.STRING);

        CC_API_KEY = new GlobalProperty<String>("ConstantContactAPIKey", Property.STRING);
        CC_TOKEN = new GlobalProperty<String>("ConstantContactToken", Property.STRING);
        LAST_SYNC_TIME = new GlobalProperty<String>("ConstantContactSyncTime", Property.STRING);
        CAMPAIGN_CREATED_DATE = new GlobalProperty<String>("ConstantContactCampaignCreatedDate", Property.STRING);
    }
}
