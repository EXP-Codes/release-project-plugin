package exp.libs.mrp.envm;

/**
 * <PRE>
 * 枚举类:压缩路径前缀模式
 * </PRE>
 * <br/><B>PROJECT : </B> mojo-release-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-05-15
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.8
 */
public class CmpPathMode {

	/**
	 * 路径前缀压缩模式1：
	 * 	提取尽可能少的路径前缀：各路径中相同的节点至少出现2次以上才会被提取前缀，子前缀压缩。
	 */
	public final static CmpPathMode LEAST = new CmpPathMode(1, "LEAST");
	
	/**
	 * 路径前缀模式2：
	 * 	提取标准数量的路径前缀：路径中同层同名的节点至少出现2次以上才会被提取前缀，相同前缀压缩。
	 */
	public final static CmpPathMode STAND = new CmpPathMode(2, "STAND");
	
	/**
	 * 路径前缀压缩模式3：
	 * 	提取尽可能多的路径前缀：所有路径都会被提取前缀，相同前缀压缩。
	 */
	public final static CmpPathMode MOST = new CmpPathMode(3, "MOST");
	
	private int id;
	
	private String mode;
	
	private CmpPathMode(int id, String mode) {
		this.id = id;
		this.mode = mode;
	}
	
	public int ID() {
		return id;
	}
	
	public String MODE() {
		return mode;
	}
	
	public static CmpPathMode toMode(String mode) {
		CmpPathMode cpMode = STAND;
		if(LEAST.MODE().equalsIgnoreCase(mode)) {
			cpMode = LEAST;
		} else if(MOST.MODE().equalsIgnoreCase(mode)) {
			cpMode = MOST;
		}
		return cpMode;
	}
	
	
}
