package exp.libs.rpp.envm;

/**
 * <PRE>
 * 枚举类:构件依赖来源
 * </PRE>
 * <br/><B>PROJECT : </B> release-project-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-05-15
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.8
 */
public class DependMode {

	/**
	 * 生成引用maven目录优先的脚本。
	 * 即若依赖包原本是在maven仓库的，则在脚本直接引用。
	 * 若不在maven仓库的，先复制到./lib下，再在脚本引用。
	 */
	public final static DependMode MAVEN = new DependMode(1, "MAVEN");
	
	/**
	 * 生成引用lib目录优先的脚本。
	 * 即把所有依赖包都复制到./lib目录下，然后脚本引用./lib的包。
	 */
	public final static DependMode SELF = new DependMode(2, "SELF");
	
	private int id;
	
	private String mode;
	
	private DependMode(int id, String mode) {
		this.id = id;
		this.mode = mode;
	}
	
	public int ID() {
		return id;
	}
	
	public String MODE() {
		return mode;
	}
	
	public static DependMode toMode(String type) {
		DependMode dType = SELF;
		if(MAVEN.MODE().equalsIgnoreCase(type)) {
			dType = MAVEN;
		} else {
			dType = SELF;
		}
		return dType;
	}
	
}
