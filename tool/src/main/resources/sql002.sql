)tb2 ON tb2.objectid = tb1.triggerid
ORDER BY
tb2.clock DESC,
tb1.priority DESC