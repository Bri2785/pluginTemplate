
package com.unigrative.plugins.repository;

import com.fbi.fbo.impl.dataexport.QueryRow;

public final class Compatibility
{
    public static final int FB_VERSION_2017_01 = 100;
    
    public static String handleFirebirdCompatibility(final Repository repository, final String method) {
        if (repository.getDatabaseVersion() < 100) {
            return "firebird/" + method;
        }
        return method;
    }
    
    public static boolean getBoolean(final Repository repository, final QueryRow queryRow, final String property) {
        if (repository.getDatabaseVersion() >= 100) {
            return queryRow.getBoolean(property);
        }
        return queryRow.getInt(property) == 1;
    }
    
    public static String getServiceCode(final Repository repository, final String service, final Integer carrierId, final String carrierName) {
        if (repository.getDatabaseVersion() < 100) {
            String code = carrierName;
            if (code.length() > 10) {
                code = code.substring(0, 10);
            }
            return code + carrierId;
        }
        return service;
    }
    

}
