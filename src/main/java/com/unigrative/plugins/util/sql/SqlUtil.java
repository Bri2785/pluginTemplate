package com.unigrative.plugins.util.sql;

import com.evnt.client.common.EVEManager;
import com.evnt.common.MethodConst;
import com.evnt.eve.event.EVEvent;
import com.evnt.util.KeyConst;
import com.fbi.fbdata.general.DataExportFpo;
import com.fbi.fbo.impl.dataexport.DataExportResult;
import com.fbi.gui.util.UtilGui;
import com.fbi.util.exception.ExceptionMainFree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SqlUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlUtil.class);
    private static final Map<String, String> storedSql = new ConcurrentHashMap();

    public static Object[] paramsListToString(List params){
        if (params == null) {
            return new Object[0];
        } else {
            Object[] strings = new String[params.size()];

            for(int i = 0; i < params.size(); ++i) {
                if (params.get(i) == null) {
                    strings[i] = "NULL";
                } else if (params.get(i) instanceof List) {
                    List<Object> list = (List)params.get(i);
                    if (list.isEmpty()) {
                        strings[i] = "NULL";
                    } else {
                        strings[i] = (String)list.stream().map(String::valueOf).collect(Collectors.joining(","));
                    }
                } else {
                    strings[i] = quote(params.get(i).toString());
                }
            }

            return strings;
        }
    }

    public static String paramsMapToWhereClause(HashMap params){

        boolean first = true;
        StringBuilder whereClause = new StringBuilder();

        if (params == null) {
            return "";
        } else {

            for (Object o : params.entrySet()) {
                Map.Entry e = (Map.Entry) o;
                String key = (String) e.getKey();
                Object comp = e.getValue();

                if(first) {
                    whereClause.append(" WHERE ").append(key.toString()).append(" LIKE ").append(quote(comp.toString()));
                    first = false;
                }
                else{
                    whereClause.append(" AND ").append(key.toString()).append(" LIKE ").append(quote(comp.toString()));
                }
            }
            return whereClause.toString();
        }
    }



    public static Object[] paramsToString(Object[] params) {
        if (params == null) {
            return new Object[0];
        } else {
            Object[] strings = new String[params.length];

            for(int i = 0; i < params.length; ++i) {
                if (params[i] == null) {
                    strings[i] = "NULL";
                } else if (params[i] instanceof List) {
                    List<Object> list = (List)params[i];
                    if (list.isEmpty()) {
                        strings[i] = "NULL";
                    } else {
                        strings[i] = (String)list.stream().map(String::valueOf).collect(Collectors.joining(","));
                    }
                } else {
                    strings[i] = quote(params[i].toString());
                }
            }

            return strings;
        }
    }

    public static String quote(Object o) {
        return "'" + o.toString().replaceAll("'", "''") + "'";
    }

    public static String loadSql(String fileName, Object... params) {


        String sqlString = (String)storedSql.computeIfAbsent(fileName, (k) -> {
            try {
                InputStream stream = SqlUtil.class.getResourceAsStream(k);
                Throwable var2 = null;

                Object var7;
                try {
                    InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                    Throwable var4 = null;

                    try {
                        Writer writer = new StringWriter();
                        Throwable var6 = null;

                        try {
                            while(reader.ready()) {
                                writer.write(reader.read());
                            }

                            var7 = writer.toString();
                        } catch (Throwable var54) {
                            var7 = var54;
                            var6 = var54;
                            throw var54;
                        } finally {
                            if (writer != null) {
                                if (var6 != null) {
                                    try {
                                        writer.close();
                                    } catch (Throwable var53) {
                                        var6.addSuppressed(var53);
                                    }
                                } else {
                                    writer.close();
                                }
                            }

                        }
                    } catch (Throwable var56) {
                        var4 = var56;
                        throw var56;
                    } finally {
                        if (reader != null) {
                            if (var4 != null) {
                                try {
                                    reader.close();
                                } catch (Throwable var52) {
                                    var4.addSuppressed(var52);
                                }
                            } else {
                                reader.close();
                            }
                        }

                    }
                } catch (Throwable var58) {
                    var2 = var58;
                    throw var58;
                } finally {
                    if (stream != null) {
                        if (var2 != null) {
                            try {
                                stream.close();
                            } catch (Throwable var51) {
                                var2.addSuppressed(var51);
                            }
                        } else {
                            stream.close();
                        }
                    }

                }

                return (String)var7;
            } catch (IOException var60) {
                LOGGER.error(var60.getMessage(), var60);
                LOGGER.error("Could not load file: {}", k);
                return "";
            }
        });
        return String.format(sqlString, params);
    }

    public static void createView(String SQLStatement, EVEManager eveManager ){

        runElevatedQuery(SQLStatement.toString(), eveManager);
    }

    public static void createTableFromFile(String tableName, String createSqlFilePath, EVEManager eveManager){


        if (!checkTableExists(tableName, eveManager)) {
            LOGGER.info(String.format("Creating table %s", tableName));
            String sql = loadSql(createSqlFilePath, null);
            LOGGER.debug("Creation query loaded");

            LOGGER.debug(sql);
            runElevatedQuery(sql, eveManager);
            LOGGER.debug("Table created");
        }

    }

    private static boolean checkTableExists(String tableName, EVEManager eveManager){
        //check if table exist first
        final String query = String.format("SHOW TABLES LIKE '%s'", tableName);


        EVEvent response = runElevatedQuery(query, eveManager);

        DataExportResult queryData = response.getObject(KeyConst.DATA_EXPORT_RESULTS, DataExportResult.class);

        if (response.isMessageException()){
            //return true;
            UtilGui.showMessageDialog("Error Running Query. Check logs for details");

        }

        return (Integer) queryData.getResults().get(0).get(1) != 0;
    }

    private static EVEvent runElevatedQuery(String query, EVEManager eveManager) {
        LOGGER.debug("Running Query");
        LOGGER.debug(query);

        EVEvent request = eveManager.createRequest(MethodConst.RUN_DATA_EXPORT);
        request.add(KeyConst.DATA_EXPORT_QUERY, query);
        request.add(KeyConst.SUPPORT_LOGGED_IN, true);

        try {
            DataExportFpo.validateSQL(query, true);
        } catch (ExceptionMainFree var4) {
            LOGGER.error("Ran from elevated, so ignore not allowed warning");
            LOGGER.error("Error validating query", var4);
        }


        return eveManager.sendAndWait(request);



    }

    private static void grantViewAccess(String viewName, EVEManager eveManager){
        LOGGER.debug("Running grant on " + viewName);

        String query = "Grant SELECT ON " + viewName + " TO 'gone'";

        EVEvent request = eveManager.createRequest(MethodConst.RUN_DATA_EXPORT);
        request.add(KeyConst.DATA_EXPORT_QUERY, query);
        request.add(KeyConst.SUPPORT_LOGGED_IN, true);

        try {
            DataExportFpo.validateSQL(query, true);
        } catch (ExceptionMainFree var4) {
            LOGGER.error("Error validating query", var4);
        }

        EVEvent response = eveManager.sendAndWait(request);
        DataExportResult queryData;
        queryData = (DataExportResult)response.getObject(KeyConst.DATA_EXPORT_RESULTS, DataExportResult.class);

        if (response.isMessageException()){
            UtilGui.showMessageDialog("Error granting select permissions. Check logs for details");
        }
    }
}
