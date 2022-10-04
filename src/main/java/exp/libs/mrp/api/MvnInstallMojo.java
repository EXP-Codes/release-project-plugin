package exp.libs.mrp.api;

import exp.libs.utils.file.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import exp.libs.mrp.Config;
import exp.libs.mrp.Log;
import exp.libs.mrp.cache.JarMgr;
import exp.libs.mrp.services.ScriptBuilder;

/**
 * <PRE>
 * 项目发布插件:Maven调用入口 - install功能部分。
 * 
 * 说明：根据脚本类型，自动生成J2SE项目的启动、停止、启动检查等脚本。
 * 
 * 		该插件在install生命周期后运行execute phase
 * 		运行插件前需要进行jar依赖分析requiresDependencyResolution
 * 
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> mojo-release-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-08-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 * 
 * @goal install
 * @requiresDependencyResolution runtime
 * @execute phase= "install"
 */
public class MvnInstallMojo extends org.apache.maven.plugin.AbstractMojo {

	/**
	 * Maven所发布的项目对象
	 * 
	 * @parameter default-value="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * <PRE>
	 * 依赖类型.
	 * 	MAVEN: 仓库依赖，所发布的项目脚本，直接依赖本地maven仓库的jar包
	 * 	SELF: 自身依赖，所发布的项目的依赖jar包会先复制到项目私有的lib目录，再在项目脚本中依赖lib下的jar包
	 * </PRE>
	 * 
	 * @parameter default-value="SELF"
	 * @required
	 */
	private String dependType;
	
	/**
	 * <PRE>
	 * 私有lib仓库的目录
	 * dependType = self 时有效
	 * </PRE>
	 * 
	 * @parameter default-value="./lib"
	 * @required
	 */
	private String jarLibDir;
	
	/**
	 * maven仓库路径
	 * 
	 * @parameter default-value="${settings.localRepository}"
	 * @required
	 */
	private String mavenRepository;
	
	/**
	 * 项目版本类路径
	 * 
	 * @parameter default-value="foo.bar.prj.Version"
	 * @required
	 */
	private String verClass;
	
	/**
	 * 项目启动类路径
	 * 
	 * @parameter default-value="foo.bar.prj.Main"
	 * @required
	 */
	private String mainClass;
	
	/**
	 * main方法入参
	 * 
	 * @parameter default-value=" "
	 * @required
	 */
	private String mainArgs;
	
	/**
	 * 项目编码
	 * 
	 * @parameter default-value="UTF-8"
	 * @required
	 */
	private String charset;

	/**
	 * JDK路径.
	 * 	默认为控制台模式的java（需环境变量支持）, 
	 * 	视实际运行需求，可为全路径，如 C:\Program Files\Java\jdk1.6.0_43\bin\java
	 *  或者应为UI时，可为 javaw
	 * 
	 * @parameter default-value="java"
	 * @required
	 */
	private String jdkPath;
	
	/**
	 * 默认分配JVM堆空间
	 * 
	 * @parameter default-value="64m"
	 * @required
	 */
	private String xms;
	
	/**
	 * 最大分配JVM堆空间
	 * 
	 * @parameter default-value="128m"
	 * @required
	 */
	private String xmx;

	/**
	 * jdk参数，若非空则会【附加】到所有脚本的JDK参数表中
	 * 
	 * @parameter default-value=" "
	 * @required
	 */
	private String jdkParams;
	
	/**
	 * 项目运行时的线程名称默认为 [项目名称].
	 * 	而 [线程后缀] 则附加在启动脚本/停止脚本的 [项目名称] 后面
	 * 
	 * @parameter default-value=" "
	 * @required
	 */
	private String threadSuffix;
	
	/**
	 * 所发布项目jar包是否带版本号（只影响启动脚本-cp列表中所发布项目的jar名称）。
	 * 对于maven项目, 主项目jar包是由第三方ant插件编译生成的，必定会带版本号。
	 * 
	 * 因此若此处值为true, 需要在ant插件配置中，拷贝所发布项目jar包时去掉版本号。
	 * 反之若此处值为false, 则需要在ant插件配置中，拷贝所发布项目jar包时加上版本号。
	 * 
	 * @parameter default-value="true"
	 * @required
	 */
	private String noPrjVer;
	
	/**
	 * 打包时需要去掉版本号的jars(使用正则匹配包名)
	 * 
	 * @parameter default-value=" "
	 * @required
	 */
	private String noVerJarRegex;
	
	/**
	 * 是否使用混淆包（需与混淆打包插件配合使用）
	 * 
	 * @parameter default-value="false"
	 * @required
	 */
	private String proguard;
	
	/**
	 * 路径压缩模式：
	 * LEAST：提取尽可能少的路径前缀：各路径中相同的节点至少出现2次以上才会被提取前缀，子前缀压缩。
	 * STAND：提取标准数量的路径前缀：路径中同层同名的节点至少出现2次以上才会被提取前缀，相同前缀压缩。
	 * MOST：提取尽可能多的路径前缀：所有路径都会被提取前缀，相同前缀压缩。
	 * 
	 * @parameter default-value="STAND"
	 * @required
	 */
	private String cmpPathMode;
	
	/**
	 * 构造函数
	 */
	public MvnInstallMojo() {}
	
	/**
	 * Maven插件调用入口
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Config.createInstn(this);
		Log.info("正在发布项目: ".concat(Config.getInstn().getReleaseName()));
		Log.info("项目发布参数: \r\n".concat(Config.getInstn().toString()));
		
		Log.info("正在清理上次发布缓存...");	// 由于Ant插件先于本插件运行，不能删除ReleaseDir
		FileUtils.delete(Config.getInstn().getCopyJarDir());
		FileUtils.createDir(Config.getInstn().getCopyJarDir());
		
		Log.info("正在定位项目依赖构件...");
		JarMgr.getInstn().loadJarPaths(project);
		Log.info("依赖构件清单(有序):\r\n".
				concat(JarMgr.getInstn().getJarSrcPathsInfo()));
		
		Log.info("正在拷贝项目依赖构件...");
		JarMgr.getInstn().copyJars();
		
		Log.info("正在生成项目脚本...");
		ScriptBuilder.exec();
		
		Log.info("项目发布完成, 发布目录: ".
				concat(Config.getInstn().getReleaseDir()));
	}
	
	public MavenProject getProject() {
		return project;
	}

	public String getDependType() {
		return dependType;
	}

	public String getJarLibDir() {
		return jarLibDir;
	}
	
	public String getMavenRepository() {
		return mavenRepository;
	}

	public String getVerClass() {
		return verClass;
	}
	
	public String getMainClass() {
		return mainClass;
	}

	public String getMainArgs() {
		return mainArgs;
	}

	public String getCharset() {
		return charset;
	}

	public String getJdkPath() {
		return jdkPath;
	}

	public String getXms() {
		return xms;
	}

	public String getXmx() {
		return xmx;
	}

	public String getJdkParams() {
		return jdkParams;
	}

	public String getThreadSuffix() {
		return threadSuffix;
	}

	public String getNoPrjVer() {
		return noPrjVer;
	}

	public String getNoVerJarRegex() {
		return noVerJarRegex;
	}

	public String getProguard() {
		return proguard;
	}

	public String getCmpPathMode() {
		return cmpPathMode;
	}

}
