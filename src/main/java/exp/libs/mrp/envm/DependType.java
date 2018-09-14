package exp.libs.mrp.envm;

/**
 * <PRE>
 * 枚举类:构件依赖来源
 * </PRE>
 * <br/><B>PROJECT : </B> mojo-release-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-05-15
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class DependType {

	/**
	 * 生成引用maven目录优先的脚本。
	 * 即若依赖包原本是在maven仓库的，则在脚本直接引用。
	 * 若不在maven仓库的，先复制到./lib下，再在脚本引用。
	 */
	public final static DependType MAVEN = new DependType(1, "MAVEN");
	
	/**
	 * 生成引用lib目录优先的脚本。
	 * 即把所有依赖包都复制到./lib目录下，然后脚本引用./lib的包。
	 */
	public final static DependType SELF = new DependType(2, "SELF");
	
	private int id;
	
	private String type;
	
	private DependType(int id, String type) {
		this.id = id;
		this.type = type;
	}
	
	public int ID() {
		return id;
	}
	
	public String TYPE() {
		return type;
	}
	
	public static DependType toType(String type) {
		DependType dType = SELF;
		if(MAVEN.TYPE().equalsIgnoreCase(type)) {
			dType = MAVEN;
		} else {
			dType = SELF;
		}
		return dType;
	}
	
}
