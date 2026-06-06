package com.cy.pj.sys.bo;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 	当基于角色id执行角色以及对应的菜单信息查询时,将查询到结果存储到此对象,
 * 	并将此对象的数据最终更新到编辑页面上.
 * 	查询方案: (现在数据来自两种表)
 *  1)业务层进行多次单表查询(先查角色表再查菜单表)-最简单的一种
 *  2)数据层做嵌套查询(数据层多次查询,基于角色表的查询结果再次查询角色菜单关系表)
 *  3)在数据层执行多表关联查询(join)
 *
 *  BO: business object：业务对象BO把业务逻辑封转为一个对象，
 *  通过调用DAO方法，结合PO或VO进行业务操作PO组合，如投保人是一个PO，被保险人是一个PO，
 *  险种信息是一个PO等等，他们组合气来是第一张保单的BO
 */
@Data
public class SysRoleMenu implements Serializable{
	private static final long serialVersionUID = -2671028987524519218L;
	/**角色id*/
	private Integer id;
	/**角色名称*/
	private String name;
	/**角色备注*/
	private String note;
	/**角色对应的菜单id(这个数据存在角色和菜单关系表中)*/
	private List<Integer> menuIds;
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
	public List<Integer> getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(List<Integer> menuIds) {
		this.menuIds = menuIds;
	}
	@Override
	public String toString() {
		return "SysRoleMenu [id=" + id + ", name=" + name + ", note=" + note + ", menuIds=" + menuIds + "]";
	}
	
	
}





