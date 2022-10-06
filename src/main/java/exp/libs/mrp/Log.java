package exp.libs.mrp;

import exp.libs.log.Console;

/**
 * <PRE>
 * 控制台日志类.
 * </PRE>
 * <br/><B>PROJECT : </B> release-project-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-05-15
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.8
 */
public class Log {

	public static void info(String msg) {
		Console.out("[MRP] {}", msg);
	}
	
	public static void error(String msg) {
		Console.err("[MRP] {}", msg);
	}
	
	public static void error(String msg, Throwable e) {
		Console.err("[MRP] {}", msg, e);
	}
	
}
