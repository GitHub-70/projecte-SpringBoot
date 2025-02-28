package com.cy.pj.sys.service;

import java.util.List;
import java.util.Map;

import com.cy.pj.sys.dto.Node;
import com.cy.pj.sys.po.SysDept;


public interface SysDeptService {
	 List<Map<String,Object>> findObjects();
	 List<Node> findZTreeNodes();
	 int saveObject(SysDept entity);
	 int updateObject(SysDept entity);
	 int deleteObject(Integer id);
}
