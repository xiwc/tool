SELECT DISTINCT
	h.`host`,
	t.triggerid,
	t.`value`
FROM
	`triggers` t
INNER JOIN(
	SELECT
		MAX(e.eventid),
		e.objectid
	FROM
		`events` e
	WHERE
		e.object = 0
	GROUP BY
		e.objectid
)t1 ON t.triggerid = t1.objectid
INNER JOIN functions f ON f.triggerid = t.triggerid
INNER JOIN items i ON i.itemid = f.itemid
INNER JOIN `hosts` h ON h.hostid = i.hostid
INNER JOIN hosts_groups hg ON hg.hostid = h.hostid
INNER JOIN skycloud_groupstype_host sgh ON sgh.hostid = h.hostid
INNER JOIN skycloud_grouptype sg ON sg.typeid = sgh.typeid
WHERE
	t.`value` IN(0, 1)
AND t.`status` = 0
AND t.lastchange <> 0
AND t.flags <> 2
AND t.priority IN(2, 3, 4, 5)
AND i.`status` = 0
AND h.`status` = 0
AND NOT EXISTS(
	SELECT
		1
	FROM
		skycloud_alarm_ok
	WHERE
		triggerid = t.triggerid
	AND confirm = 1
	AND `repair` = 1
	AND triggerid IN(
		SELECT
			triggerid
		FROM
			`triggers`
		WHERE
			`value` = 0
	)
)
AND NOT EXISTS(
	SELECT
		1
	FROM
		skycloud_alarm_hidden
	WHERE
		triggerid = t.triggerid
);