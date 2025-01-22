package com.cy.pj.sys.po;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门PO对象
 */

public class SysDept implements Serializable{
	private static final long serialVersionUID = 8876920804134951849L;
	@ApiModelProperty(value = "部门ID")
	private Integer id;
	@ApiModelProperty(value = "部门名称")
	private String name;
	@ApiModelProperty(value = "父节点ID")
	private Integer parentId;
	@ApiModelProperty(value = "部门序号")
	private Integer sort;
	@ApiModelProperty(value = "备注")
	private String note;
	@ApiModelProperty(value = "创建时间")
	private Date createdTime;
	@ApiModelProperty(value = "修改时间")
	private Date modifiedTime;
	@ApiModelProperty(value = "创建用户")
	private String createdUser;
	@ApiModelProperty(value = "修改用户")
	private String modifiedUser;
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
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getModifiedUser() {
		return modifiedUser;
	}
	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}
	@Override
	public String toString() {
		return "SysDept [id=" + id + ", name=" + name + ", parentId=" + parentId + ", sort=" + sort + ", note=" + note
				+ ", createdTime=" + createdTime + ", modifiedTime=" + modifiedTime + ", createdUser=" + createdUser
				+ ", modifiedUser=" + modifiedUser + "]";
	}
	
	
}
