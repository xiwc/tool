package com.skycloud.tools.sql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JoinSql2OneLineMain {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		InputStream fis = JoinSql2OneLineMain.class.getResourceAsStream("/sql002.sql");
		BufferedReader brReader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

		StringBuffer sBuffer = new StringBuffer();
		String line = null;
		sBuffer.append("");
		while ((line = brReader.readLine()) != null) {
			sBuffer.append("").append(line).append(" ");
		}

		System.out.println(sBuffer.toString());
		
		// SELECT tb1.`name`, tb1.`host`, tb1.hostid, tb1.ip, tb1.priority, tb1.description, tb2.clock, tb2.`value`, tb1.type FROM ( SELECT DISTINCT g.`name`, h.`host`, h.hostid, ifac.ip, t.triggerid, t.priority, t.description, sg.type FROM interface ifac, `hosts` h, groups g, functions f, `triggers` t,items i,hosts_groups hg,skycloud_groupstype_host sgh,skycloud_grouptype sg WHERE NOT EXISTS(SELECT ff.functionid FROM functions ff WHERE ff.triggerid = t.triggerid AND EXISTS(SELECT ii.itemid FROM items ii, `hosts` hh WHERE ff.itemid = ii.itemid AND hh.hostid = ii.hostid AND(ii. STATUS <> 0 OR hh. STATUS <> 0))) AND ifac.hostid = h.hostid AND g.groupid = hg.groupid AND t. STATUS = 0 AND t.flags <> 2 AND((t.priority IN(" + priority + "))) AND f.triggerid = t.triggerid AND f.itemid = i.itemid AND h.hostid = i.hostid AND sgh.hostid = h.hostid AND sg.typeid = sgh.typeid AND hg.hostid = h.hostid {{_permission}} {{_hostid}} {{_type}} )tb1 INNER JOIN( SELECT e.objectid, e.eventid, e.`value`, e.clock FROM `events` e WHERE e.object = 0 {{_value}} )tb2 ON tb2.objectid = tb1.triggerid ORDER BY tb2.clock DESC, tb1.priority DESC {{_limit}}
	}

}
