package com.cy.pj.sys.pojo;
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
	private Integer id;
	//用户名
	private String username;
	//用户操作
	private String operation;
	//请求方法
	private String method;
	//请求参数
	private String params;
	//执行时长(毫秒)
	private Long time;
	//IP地址
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
