package exp.libs.mrp;

import exp.libs.mrp.api.MvnInstallMojo;
import exp.libs.mrp.envm.CmpPathMode;
import exp.libs.mrp.envm.DependType;
import exp.libs.utils.file.FileUtils;
import exp.libs.utils.other.BoolUtils;
import exp.libs.utils.other.PathUtils;
import exp.libs.utils.str.StrUtils;

/**
 * <PRE>
 * 配置类.
 * </PRE>
 * <br/><B>PROJECT : </B> mojo-release-plugin
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-05-15
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.8
 */
public class Config {

	public final static String TARGET_DIR = "./target";
	
	public final static String PROGUARD_SUFFIX = "-pg";
	
	private final static String DEFAULT_JAR_LIB = "./lib";
	
	private DependType dependType;
	
	private String jarLibDir;
	
	private String mavenRepository;
	
	/** 自动生成, 无需配置 */
	private String prjName;
	
	/** 自动生成, 无需配置 */
	private String prjVer;
	
	private boolean noPrjVer;
	
	private String noVerJarRegex;
	
	private String releaseName;
	
	private String releaseDir;
	
	private String verClass;
	
	private String mainClass;
	
	private String mainArgs;
	
	private String charset;
	
	private String jdkPath;
	
	private String xms;
	
	private String xmx;
	
	private String jdkParams;
	
	private String threadSuffix;
	
	private CmpPathMode cmpPathMode;
	
	private boolean proguard;
	
	private String proguardDir;
	
	private String copyJarDir;
	
	private static volatile Config instance;
	
	private Config() {}
	
	public static Config createInstn(MvnInstallMojo mvn) {
		if(mvn == null) {
			Log.error("初始化 mojo-release-plugin 失败");
			System.exit(1);
		}
		
		if(instance == null) {
			synchronized (Config.class) {
				if(instance == null) {
					instance = new Config();
					instance.init(mvn);
				}
			}
		}
		return instance;
	}
	
	/**
	 * 获取单例
	 * @return 单例
	 */
	public static Config getInstn() {
		if(instance == null) {
			synchronized (Config.class) {
				if(instance == null) {
					Log.error("mojo-release-plugin 尚未初始化");
					System.exit(1);
				}
			}
		}
		return instance;
	}
	
	private void init(MvnInstallMojo mvn) {
		try {
			this.dependType = DependType.toType(mvn.getDependType().trim());
			this.jarLibDir = mvn.getJarLibDir().trim();
			if(PathUtils.isFullPath(jarLibDir)) { jarLibDir = DEFAULT_JAR_LIB; }
			this.mavenRepository = mvn.getMavenRepository().trim();
			this.prjName = mvn.getProject().getArtifactId();
			this.prjVer = mvn.getProject().getVersion();
			this.noPrjVer = BoolUtils.toBool(mvn.getNoPrjVer().trim(), true);
			this.noVerJarRegex = mvn.getNoVerJarRegex().trim();
			this.releaseName = StrUtils.concat(prjName, "-", prjVer);
			this.releaseDir = StrUtils.concat(TARGET_DIR, "/", releaseName);
			this.verClass = mvn.getVerClass().trim();
			this.mainClass = mvn.getMainClass().trim();
			this.mainArgs = mvn.getMainArgs().trim();
			this.charset = mvn.getCharset().trim();
			this.jdkPath = mvn.getJdkPath().trim();
			this.xms = mvn.getXms().trim();
			this.xmx = mvn.getXmx().trim();
			this.jdkParams = mvn.getJdkParams().trim();
			this.threadSuffix = mvn.getThreadSuffix().trim().concat(" ");	// 线程后缀必须至少有一个空格, 便于sh脚本定位线程号
			this.cmpPathMode = CmpPathMode.toMode(mvn.getCmpPathMode().trim());
			this.proguardDir = StrUtils.concat(releaseDir, PROGUARD_SUFFIX);
			this.proguard = BoolUtils.toBool(mvn.getProguard().trim(), false);
			if(!FileUtils.exists(proguardDir)) { proguard = false; }
			this.copyJarDir = PathUtils.combine(releaseDir, jarLibDir);
			
		} catch(Exception e) {
			Log.error("加载 mojo-release-plugin 配置失败", e);
			System.exit(1);
		}
		
		if(FileUtils.createDir(copyJarDir) == null) {
			Log.error("创建构件目录失败: ".concat(copyJarDir));
			System.exit(1);
		}
	}
	
	public DependType getDependType() {
		return dependType;
	}
	
	public String getJarLibDir() {
		return jarLibDir;
	}

	public String getMavenRepository() {
		return mavenRepository;
	}

	public String getPrjName() {
		return prjName;
	}

	public String getPrjVer() {
		return prjVer;
	}

	public boolean isNoPrjVer() {
		return noPrjVer;
	}

	public String getNoVerJarRegex() {
		return noVerJarRegex;
	}

	public String getReleaseName() {
		return releaseName;
	}
	
	public String getReleaseDir() {
		return releaseDir;
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

	public CmpPathMode getCmpPathMode() {
		return cmpPathMode;
	}

	public boolean isProguard() {
		return proguard;
	}
	
	public String getProguardDir() {
		return proguardDir;
	}
	
	public String getCopyJarDir() {
		return copyJarDir;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("  dependType: ").append(getDependType().TYPE()).append("\r\n");
		sb.append("  jarLibDir: ").append(getJarLibDir()).append("\r\n");
		sb.append("  mavenRepository: ").append(getMavenRepository()).append("\r\n");
		sb.append("  prjName: ").append(getPrjName()).append("\r\n");
		sb.append("  prjVer: ").append(getPrjVer()).append("\r\n");
		sb.append("  noPrjVer: ").append(isNoPrjVer()).append("\r\n");
		sb.append("  noVerJarRegex: ").append(getNoVerJarRegex()).append("\r\n");
		sb.append("  releaseDir: ").append(getReleaseDir()).append("\r\n");
		sb.append("  verClass: ").append(getVerClass()).append("\r\n");
		sb.append("  mainClass: ").append(getMainClass()).append("\r\n");
		sb.append("  mainArgs: ").append(getMainArgs()).append("\r\n");
		sb.append("  charset: ").append(getCharset()).append("\r\n");
		sb.append("  jdkPath: ").append(getJdkPath()).append("\r\n");
		sb.append("  xms: ").append(getXms()).append("\r\n");
		sb.append("  xmx: ").append(getXmx()).append("\r\n");
		sb.append("  jdkParams: ").append(getJdkParams()).append("\r\n");
		sb.append("  threadSuffix: ").append(getThreadSuffix()).append("\r\n");
		sb.append("  cmpPathMode: ").append(getCmpPathMode().MODE()).append("\r\n");
		sb.append("  proguard: ").append(isProguard()).append("\r\n");
		sb.append("  proguardDir: ").append(getProguardDir()).append("\r\n");
		sb.append("  copyJarDir: ").append(getCopyJarDir()).append("\r\n");
		return sb.toString();
	}

}
