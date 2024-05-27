package com.cy.pj.sys.pojo;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
/**
 * 	基于此对象封装角色信息
 * @author pc
 */
public class SysRole implements Serializable{
	private static final long serialVersionUID = -8557710039441592130L;
	@ApiModelProperty(value = "角色ID")
	 private Integer id;
	@ApiModelProperty(value = "角色名称")
	 private String name;
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
		return "SysRole [id=" + id + ", name=" + name + ", note=" + note + ", createdTime=" + createdTime
				+ ", modifiedTime=" + modifiedTime + ", createdUser=" + createdUser + ", modifiedUser=" + modifiedUser
				+ "]";
	}
	 
	 
}
