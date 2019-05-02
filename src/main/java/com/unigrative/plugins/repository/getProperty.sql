SELECT info
FROM pluginProperties
WHERE dataKey = %1$s
AND plugin = %2$s
LIMIT 1