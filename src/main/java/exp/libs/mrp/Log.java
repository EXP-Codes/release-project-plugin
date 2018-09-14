package exp.libs.mrp;

/**
 * <PRE>
 * 控制台日志类.
 * </PRE>
 * <br/><B>PROJECT : </B> mojo-release-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-05-15
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Log {

	public static void debug(String msg) {
		System.out.println("[MRP] [DEBUG] ".concat(msg));
	}
	
	public static void info(String msg) {
		System.out.println("[MRP] [INFO] ".concat(msg));
	}
	
	public static void warn(String msg) {
		System.out.println("[MRP] [WARN] ".concat(msg));
	}

	public static void error(String msg) {
		System.err.println("[MRP] [ERROR] ".concat(msg));
	}
	
	public static void error(String msg, Throwable e) {
		error(msg);
		e.printStackTrace();
	}
	
}
