package exp.libs.mrp.api;

import exp.libs.utils.file.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;

import exp.libs.mrp.Config;
import exp.libs.mrp.Log;
import exp.libs.mrp.cache.JarMgr;
import exp.libs.mrp.services.ScriptBuilder;

/**
 * <PRE>
 * 项目发布插件: Maven 调用入口 - install。
 * 
 * 说明：根据脚本类型，自动生成 J2SE 项目的启动、停止、启动检查等脚本。
 * 
 * 		该插件在 install 生命周期后运行 execute phase
 * 		运行插件前需要进行 jar 依赖分析 requiresDependencyResolution
 * ------------------------
 * 参考文档： https://segmentfault.com/a/1190000041253195
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> mojo-release-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2022-10-04
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.8
 */
@Mojo(			// Mojo 插件基础信息
		name = "install", 					// 适用的目标名称，等价于旧版的 @goal 标签
		aggregator = false,					// ?
		configurator = "<configuration>",	// ?
		instantiationStrategy = InstantiationStrategy.SINGLETON,	// 实例化策略
		defaultPhase = LifecyclePhase.INSTALL,
		requiresDependencyResolution = ResolutionScope.RUNTIME,
		requiresDependencyCollection = ResolutionScope.RUNTIME,
		requiresOnline = false,				// 提示此Mojo不能在离线模式下运行
		requiresProject = true,				// 提示此Mojo必须在一个Maven项目内运行
		threadSafe = false					// 提示此Mojo是否线程安全，线程安全的Mojo支持在并行构建中被并发的调用
)
@Execute(		// Mojo 插件运行条件
		goal = "install",           		// 如果提供 goal，则在执行此 Mojo
		phase = LifecyclePhase.INSTALL, 	// 在此生命周期阶段自动执行此 Mojo
		lifecycle = "install"				// 在此生命周期中执行此Mojo
)
public class MvnInstallMojo extends AbstractMojo {

	/** Maven所发布的项目对象 */
	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	/**
	 * <PRE>
	 * 依赖模式.
	 *  SELF: 自身依赖，所发布的项目的依赖 jar 包会先复制到项目私有的 libs 目录，再在项目脚本中依赖 libs 下的 jar 包
	 * 	MAVEN: 仓库依赖，所发布的项目脚本，直接依赖本地 maven 仓库的 jar 包
	 * </PRE>
	 */
	@Parameter(name = "dependMode", alias = "dependType", defaultValue = "SELF")
	private String dependType;
	
	/**
	 * <PRE>
	 * 项目私有 libss 仓库的目录
	 * （仅当 dependType = SELF 时有效）
	 * </PRE>
	 */
	@Parameter(name = "jarLibDir", defaultValue = "./libs")
	private String jarLibDir;
	
	/**
	 * <PRE>
	 * maven 仓库路径
	 * （仅当 dependType = MAVEN 时有效）
	 * </PRE>
	 */
	@Parameter(name = "mavenRepository", alias = "mvnRepo", defaultValue = "${settings.localRepository}")
	private String mavenRepository;

	/** 项目启动类路径 */
	@Parameter(name = "mainClass", defaultValue = "foo.bar.prj.Main")
	private String mainClass;

	/** 项目版本类路径 */
	@Parameter(name = "versionClass", alias = "verClass", defaultValue = "foo.bar.prj.Version")
	private String verClass;
	
	/** main 方法入参 */
	@Parameter(name = "mainArgs", alias = "mainParams", defaultValue = "")
	private String mainArgs;
	
	/** 项目编码 */
	@Parameter(name = "charset", defaultValue = "UTF-8")
	private String charset;

	/**
	 * <PRE>
	 * JDK 路径.
	 * 	默认为控制台模式的 java（需环境变量支持）,
	 * 	当要启动 UI 时，可为 javaw
	 * 	视实际运行需求，可为全路径，如 C:\Program Files\Java\jdk1.6.0_43\bin\java
	 * </PRE>
	 */
	@Parameter(name = "jdkPath", defaultValue = "java")
	private String jdkPath;
	
	/** 默认分配JVM堆空间 */
	@Parameter(name = "xms", defaultValue = "64m")
	private String xms;
	
	/** 最大分配JVM堆空间 */
	@Parameter(name = "xmx", defaultValue = "128m")
	private String xmx;

	/** jdk参数，若非空则会【附加】到所有脚本的JDK参数表中 */
	@Parameter(name = "jdkArgs", alias = "jdkParams", defaultValue = "")
	private String jdkParams;
	
	/**
	 * <PRE>
	 * 项目运行时的线程名称默认为 [项目名称].
	 * 	而 [线程后缀] 则附加在启动脚本/停止脚本的 [项目名称] 后面
	 * </PRE>
	 */
	@Parameter(name = "threadSuffix", defaultValue = "")
	private String threadSuffix;
	
	/**
	 * <PRE>
	 * 所发布项目 jar 包是否带版本号（只影响启动脚本 -cp 列表中所发布项目的 jar 名称）。
	 * 对于 maven 项目, 主项目 jar 包默认会带版本号。
	 * 
	 * 因此若此处值为 true, 需要同时配置 &lt;build&gt;.&lt;finalName&gt; 去掉版本号。
	 * 反之若此处值为 false, 则无需做特殊配置。
	 * </PRE>
	 */
	@Parameter(name = "noPrjVer", defaultValue = "false")
	private String noPrjVer;
	
	/** 打包时需要去掉版本号的 jars 依赖 (使用正则匹配包名) */
	@Parameter(name = "noVerJarRegex", defaultValue = "")
	private String noVerJarRegex;
	
	/** 是否使用混淆包（需与混淆打包插件配合使用） */
	@Parameter(name = "proguard", defaultValue = "false")
	private String proguard;

	/**
	 * <PRE>
	 * 路径压缩模式：
	 *  LEAST：提取尽可能少的路径前缀：各路径中相同的节点至少出现 2 次以上才会被提取前缀，子前缀压缩。
	 *  STAND：提取标准数量的路径前缀：路径中同层同名的节点至少出现 2 次以上才会被提取前缀，相同前缀压缩。
	 *  MOST：提取尽可能多的路径前缀：所有路径都会被提取前缀，相同前缀压缩。
	 * </PRE>
	 */
	@Parameter(name = "cmpPathMode", defaultValue = "STAND")
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
