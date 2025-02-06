package com.cy.pj.sys.dto;
import java.io.Serializable;
/**基于此对象存储树节点信息*/

public class Node implements Serializable{
	private static final long serialVersionUID = -7022202313802285223L;
	private Integer id;
	private String name;
	private Integer parentId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	
}
