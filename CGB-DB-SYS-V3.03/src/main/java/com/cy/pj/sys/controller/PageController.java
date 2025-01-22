package com.cy.pj.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cy.pj.common.utils.ShiroUtils;
import com.cy.pj.sys.po.SysUser;
import com.cy.pj.sys.po.SysUserMenu;
import com.cy.pj.sys.service.SysMenuService;

/**
 * 	基于此Controller处理所有页面请求
 */
@RequestMapping("/")
@Controller
public class PageController {
	
	@RequestMapping("doLoginUI")
	public String doLoginUI() {
		return "login";
	}
	//http://localhost/log/log_list
	//http://localhost/menu/menu_list
	//rest风格的url,{}代表是变量表达式

	/**
	 * 请求：http://localhost:8082/xxx/role_list		返回的 model 为 sys/role_list.html
	 * @param moduleUI
	 * @return
	 * 好处：
	 *    1.资源导向：强调了HTTP请求是针对特定资源的操作，使得URL更具语义化。
	 *    2.可扩展性：可以通过添加更多的路径段来定义新的资源或资源子集，方便扩展。
	 *    3.模块化：module可能代表应用的不同模块或功能区域，有助于组织和分离代码。
	 *    4.易于理解：对于开发者来说，这样的URL更容易理解，因为它直接反映了请求的目的。
	 *    5.缓存优化：因为资源的URI是固定的，可以基于URI进行缓存，提高性能。
	 *    6.无状态：每个请求都包含所有必要的信息，服务器不需要存储请求之间的状态，简化了服务器端的设计。
	 *  但此路径的映射 可能造成一些静态资源无法解析--500错误 如：
	 * 	 http://localhost:8082/base64-js-master/base64js.min.js
	 * 	 无法解析 Error resolving template [sys/base64js.min.js]
	 * 	 因此 多增加了一个文件夹 dele-sys-mapping
	 */
	@RequestMapping("{module}/{moduleUI}")
	public String doModuleUI(@PathVariable String moduleUI) {
		return "sys/"+moduleUI;
	}

//	@RequestMapping("menu/menu_list")
//	public String doMenuUI() {
//		return "sys/menu_list";
//	}
//
//	@RequestMapping("log/log_list")
//	public String doLogUI() {
//		return "sys/log_list";
//	}
	

	@RequestMapping("doPageUI")
	public String doPageUI() {
		return "common/page";
	}

	@Autowired
	private SysMenuService sysMenuService;
	@RequestMapping("doIndexUI")
	public String doIndexUI(Model model) {
		//从shiro框架的session对象中取出登录用户信息
		SysUser user=ShiroUtils.getUser();
		//获取用户有访问权限的菜单
		List<SysUserMenu> userMenus=sysMenuService.findUserMenus(user.getId());
		System.out.println("userMenus="+userMenus);
		//将用户对象存储到model
		model.addAttribute("user", user);
		model.addAttribute("userMenus", userMenus);
		return "starter";//ThymeleafViewResolver
	}
}
