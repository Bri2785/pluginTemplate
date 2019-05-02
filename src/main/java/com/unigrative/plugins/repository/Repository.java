
package com.unigrative.plugins.repository;

import com.evnt.util.Util;
import com.fbi.fbo.impl.dataexport.QueryRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class Repository {
    private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);
    private static final int FIRE_BIRD_LIST_LIMIT = 1500;
    private static int databaseVersion = -1;
    public static final int FB_VERSION_2017_01 = 100;
    private final Repository.RunSql sql;
    private final Map<String, String> storedSql = new ConcurrentHashMap();

    public Repository(Repository.RunSql sql) {
        this.sql = sql;
    }


    public String getCompanyName() {
        List<QueryRow> result = this.sql.executeSql("SELECT name FROM company");
        return result.isEmpty() ? "" : ((QueryRow)result.get(0)).getString("name");
    }


    public String getProperty(String key) {
        List<QueryRow> result = this.sql.executeSql(this.loadSql(Compatibility.handleFirebirdCompatibility(this, "getProperty.sql"), quote(key), quote("Customer Addons")));
        return Util.isEmpty(result) ? "" : ((QueryRow)result.get(0)).getString("info");
    }

    public synchronized int getDatabaseVersion() {
        if (databaseVersion == -1) {
            databaseVersion = ((QueryRow)this.sql.executeSql("SELECT MAX(version) AS version FROM databaseVersion ").get(0)).getInt("version").intValue();
        }

        return databaseVersion;
    }

    private String loadSql(String fileName, Object... params) {
        String sqlString = (String)this.storedSql.computeIfAbsent(fileName, (k) -> {
            try {
                InputStream stream = Repository.class.getResourceAsStream(k);
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
        return String.format(sqlString, this.paramsToString(params));
    }

    private Object[] paramsToString(Object[] params) {
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
                    strings[i] = params[i].toString();
                }
            }

            return strings;
        }
    }

    private static String quote(Object o) {
        return "'" + o.toString().replaceAll("'", "''") + "'";
    }

    private static String escape(Object o) {
        return "'" + o.toString().replaceAll("([|_%;])", "|$1").replaceAll("'", "''") + "'";
    }

    private QueryRow getSingleResult(List<QueryRow> queryRowList) {
        return queryRowList.isEmpty() ? null : (QueryRow)queryRowList.get(0);
    }

    private static <T> List<T> page(List<T> l, int start) {
        return l.subList(start, Math.min(l.size(), start + 1500));
    }

    @FunctionalInterface
    public interface RunSql {
        List<QueryRow> executeSql(String var1);
    }
}
