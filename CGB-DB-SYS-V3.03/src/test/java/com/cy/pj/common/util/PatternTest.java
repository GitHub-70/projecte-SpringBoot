package com.cy.pj.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pattern类方法详解
 * @author Administrator
 * 		1.Pattern complie(String regex)：编译正则表达式，并创建Pattern类。
 * 		2.String pattern()：返回正则表达式的字符串形式。
 * 		3.Pattern compile(String regex, int flags)。编译正则表达式，并创建Pattern类。
 * 			-- flag参数用来控制正则表达式的匹配行为,可取值范围如下：:
 * 				-- Pattern.CANON_EQ：启用规范等价。
 * 				-- Pattern.CASE_INSENSITIVE：启用不区分大小写的匹配。
 * 				-- Pattern.COMMENTS：模式中允许空白和注释。
 * 				-- Pattern.DOTALL：启用dotall模式。在这种模式下，表达式‘.’可以匹配任意字符，包括表示一行的结束符。
 * 				-- Pattern.LITERAL：启用模式的字面值解析。
 * 		4.int flags()：返回当前Pattern的匹配flag参数。
 * 		5.String[] split(CharSequence input)。
 * 		6.static boolean matches(String regex, CharSequence input)。
 * 			-- 用于快速匹配字符串，该方法适合用于只匹配一次，且匹配全部字符串。并不需要生成一个Matcher实例。
 * 		7.Matcher matcher(CharSequence input)。
 * 			Pattern.matcher(CharSequence input)返回一个Matcher对象。Matcher类的构造方法也是私有的，
 * 			不能随意创建，只能通过Pattern.matcher(CharSequence input)方法得到该类的实例。
 * 			Pattern类只能做一些简单的匹配操作，要想得到更强更便捷的正则匹配操作，
 * 			那就需要将Pattern与Matcher一起合作。Matcher类提供了对正则表达式的分组支持，
 * 			以及对正则表达式的多次匹配支持。
 */
public class PatternTest {

	public static void main(String[] args) {
		
		 Pattern p =Pattern.compile("a*b");

	     Matcher m =p.matcher("aaaaab");

	     boolean b = m.matches();
	     
	     System.out.println(b);
	}
}
