package com.cy.pj.sys.pojo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


public class SysMenu implements Serializable{
	private static final long serialVersionUID = -4948259309231173588L;

	@ApiModelProperty(value = "菜单ID")
	private Integer id;

	/**菜单名称*/
	@ApiModelProperty(value = "菜单名称")
	private String name;

	/**菜单url: log/doFindPageObjects*/
	@ApiModelProperty(value = "菜单uri")
	private String url;

	/**菜单类型(两种:按钮,普通菜单)*/
	@ApiModelProperty(value = "菜单类型")
	private Integer type=1;

	/**排序(序号)*/
	@ApiModelProperty(value = "菜单序号")
	private Integer sort;

	/**备注*/
	@ApiModelProperty(value = "备注")
	private String note;

	/**上级菜单id*/
	@ApiModelProperty(value = "父节点ID")
	private Integer parentId;

	@ApiModelProperty(value = "父节点名称")
	private String parentName;

	/**菜单对应的权限标识(sys:log:delete)*/
	@ApiModelProperty(value = "菜单权限标识")
	private String permission;

	/**创建用户*/
	@ApiModelProperty(value = "创建用户")
	private String createdUser;

	/**修改用户*/
	@ApiModelProperty(value = "修改用户")
	private String modifiedUser;

	@ApiModelProperty(value = "创建时间")
	private Date createdTime;
	@ApiModelProperty(value = "修改时间")
	private Date modifiedTime;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
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
	@Override
	public String toString() {
		return "SysMenu [id=" + id + ", name=" + name + ", url=" + url + ", type=" + type + ", sort=" + sort + ", note="
				+ note + ", parentId=" + parentId + ", parentName=" + parentName + ", permission=" + permission
				+ ", createdUser=" + createdUser + ", modifiedUser=" + modifiedUser + ", createdTime=" + createdTime
				+ ", modifiedTime=" + modifiedTime + "]";
	}

	
}
