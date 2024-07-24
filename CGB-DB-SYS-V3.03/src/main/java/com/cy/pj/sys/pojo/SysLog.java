package com.cy.pj.sys.pojo;
import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
/**
 * 	基于此对象封装从数据库查询到的日志信息:
 *      类似对象的特性:
 *  1)添加set/get/toString/constructor
 *  2)实现Serializable接口(建议所有用于封装数据的对象都实现此接口)
 *  2.1)序列化:将对象转换为字节便于传输和存储
 *  2.2)反序列化:将网络中的或存储介质中的字节转换为对象
 */

public class SysLog implements Serializable{//ObjectOutputStream/ObjectInputStream
	private static final long serialVersionUID = -1592163223057343412L;

//	@Excel(name="日志ID")
	private Integer id;
	//用户名
	@Excel(name="用户名",width=20, orderNum="1")
	private String username;
	//用户操作
	@Excel(name="用户操作",width=40, orderNum="2")
	private String operation;
	//请求方法
	@Excel(name="请求方法",width=60, orderNum="3")
	private String method;
	//请求参数
	@Excel(name="请求参数",width=20, orderNum="4")
	private String params;
	//执行时长(毫秒)
	@Excel(name="执行时长(毫秒)",width=20, orderNum="6")
	private Long time;
	//IP地址
	@Excel(name="IP地址",width=20, orderNum="5")
	private String ip;
	//创建时间
	private Date createdTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	@Override
	public String toString() {
		return "SysLog [id=" + id + ", username=" + username + ", operation=" + operation + ", method=" + method
				+ ", params=" + params + ", time=" + time + ", ip=" + ip + ", createdTime=" + createdTime + "]";
	}
	
	
}
