package exp.libs.rpp.envm;

/**
 * <PRE>
 * 所生成的脚本名称定义。
 * 要求所有脚本文件名称都要先在此处定义(注意末尾还有一个所有脚本清单ALL_SCRIPTS)，以便管理。
 * 
 * </PRE>
 * <br/><B>PROJECT : </B> release-project-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-08-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.8
 */
public class ScriptNames {

	/**
	 * ant打包脚本:dos
	 */
	public final static String BUILD_BAT = "build.bat";
	
	/**
	 * ant配置脚本
	 */
	public final static String BUILD_XML = "build.xml";
	
	/**
	 * SQL自动化部署脚本:dos
	 */
	public final static String AUTODB_BAT = "autodb.bat";
	
	/**
	 * SQL自动化部署脚本:unix
	 */
	public final static String AUTODB_SH = "autodb.sh";
	
	/**
	 * 加密脚本:dos
	 */
	public final static String CRYPTO_BAT = "crypto.bat";
	
	/**
	 * 加密脚本:unix
	 */
	public final static String CRYPTO_SH = "crypto.sh";
	
	/**
	 * 接入环境检查脚本:dos
	 */
	public final static String STARTCHECK_BAT = "startcheck.bat";
	
	/**
	 * 接入环境检查脚本:unix
	 */
	public final static String STARTCHECK_SH = "startcheck.sh";
	
	/**
	 * 线程名声明脚本
	 */
	public final static String THREAD_NAME = "_threadname";
	
	/**
	 * 线程号查询脚本:unix
	 */
	public final static String ECHO_PID = "echo-pid.sh";
	
	/**
	 * 项目启动脚本（含gc日志）:dos
	 */
	public final static String START_GC_BAT = "start_gc.bat";
	
	/**
	 * 项目启动脚本（含gc日志）:unix
	 */
	public final static String START_GC_SH = "start_gc.sh";
	
	/**
	 * 项目启动脚本（无gc日志）:dos
	 */
	public final static String START_BAT = "start.bat";
	
	/**
	 * 项目启动脚本（无gc日志）:unix
	 */
	public final static String START_SH = "start.sh";
	
	/**
	 * 项目停止脚本:unix
	 */
	public final static String STOP_SH = "stop.sh";
	
	/**
	 * 项目版本打印脚本:dos
	 */
	public final static String VERSION_BAT = "version.bat";
	
	/**
	 * 项目版本打印脚本:unix
	 */
	public final static String VERSION_SH = "version.sh";
	
	/**
	 * 所有脚本名称清单
	 */
	public final static String[] ALL_SCRIPTS = {
		BUILD_BAT,
		BUILD_XML,
		AUTODB_BAT,
		AUTODB_SH,
		CRYPTO_BAT,
		CRYPTO_SH,
		STARTCHECK_BAT,
		STARTCHECK_SH,
		THREAD_NAME,
		ECHO_PID,
		START_GC_BAT,
		START_GC_SH,
		START_BAT,
		START_SH,
		STOP_SH,
		VERSION_BAT,
		VERSION_SH,
	};
	
	/**
	 * 禁止构造，避免误用
	 */
	private ScriptNames() {}
	
}
