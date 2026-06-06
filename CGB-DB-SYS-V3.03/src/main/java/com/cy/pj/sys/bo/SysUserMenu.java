package com.cy.pj.sys.bo;

import java.io.Serializable;
import java.util.List;


/**
 * 封装用户菜单信息
 *
 * BO: business object：业务对象BO把业务逻辑封转为一个对象，
 * 通过调用DAO方法，结合PO或VO进行业务操作PO组合，如投保人是一个PO，被保险人是一个PO，
 * 险种信息是一个PO等等，他们组合气来是第一张保单的BO
 */

public class SysUserMenu implements Serializable{
	private static final long serialVersionUID = -8089063957163137501L;
	private Integer id;
	private String name;
	private String url;
	private List<SysUserMenu> childs;
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
	public List<SysUserMenu> getChilds() {
		return childs;
	}
	public void setChilds(List<SysUserMenu> childs) {
		this.childs = childs;
	}
	@Override
	public String toString() {
		return "SysUserMenu [id=" + id + ", name=" + name + ", url=" + url + ", childs=" + childs + "]";
	}
	
	
}
