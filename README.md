# mojo-release-plugin
Maven项目发布插件

------


## 运行环境

　![](https://img.shields.io/badge/Maven-3.2.5%2B-brightgreen.svg)  ![](https://img.shields.io/badge/JDK-1.7%2B-brightgreen.svg)


## 软件介绍


- 一键快速发布一个可运行于生成环境的应用程序
- 支持混淆打包以避免被反编译
- 自动集成依赖构件、配置文件、部署文档、数据库脚本、版本说明、运行脚本等



> **注**：
<br/>　　本插件的主要作用其实就是生成运行脚本与组织应用程序的目录结构
<br/>　　混淆打包是依赖第三方 `proguard-maven-plugin` 插件实现的
<br/>　　应用程序的部署文件复制（如配置文件、部署文档、数据库脚本等）是依赖第三方 `maven-antrun-plugin` 插件实现的
<br/>　　版本说明是依赖 [`经验构件库 exp-libs`](https://github.com/lyy289065406/exp-libs) 实现的



## 使用说明

　本插件通过 `Maven install` 指令触发，在指定项目的 pom.xml 中添加本插件即可，完整参数配置如下：

```xml
<plugin>
    <groupId>exp.libs</groupId>
    <artifactId>mojo-release-plugin</artifactId>
    <version>${mrp.version}</version>	<!-- 根据实际情况设置版本号 -->
    <executions>
        <execution>
            <id>mrp</id>
            <phase>install</phase>
            <goals>
                <goal>install</goal>
            </goals>
            <configuration>
                <dependType>SELF</dependType>
                <jarLibDir>./lib</jarLibDir>
                <cmpPathMode>STAND</cmpPathMode>
                <mavenRepository>D:\mavenRepository</mavenRepository>
                <verClass>foo.bar.prj.Version</verClass>
                <mainClass>foo.bar.prj.Main</mainClass>
                <mainArgs></mainArgs>
                <charset>UTF-8</charset>
                <jdkPath>java</jdkPath>
                <xms>32m</xms>
                <xmx>64m</xmx>
                <jdkParams></jdkParams>
                <threadSuffix></threadSuffix>
                <noPrjVer>true</noPrjVer>
                <noVerJarRegex>exp-?libs-.*</noVerJarRegex>
                <proguard>false</proguard>
            </configuration>
        </execution>
    </executions>
</plugin>
```

　其中各个参数说明如下：

| 参数 | 必填 | 取值范围/约束 | 作用 |
|:----:|:--------:|:--------|:----|
| dependType | 否 | ○ SELF<br/>○ MAVEN | 影响所发布项目的运行脚本中`-cp *.jar`所依赖构件的指向位置：<br/><br/>**SELF（默认）** ： <br/>把所有依赖构件都复制到`./lib`目录下，运行脚本直接指向`./lib`。<br/>此方式所发布应用的体积较大，但是可放在任何环境中运行。<br/><br/>**MAVEN** ： <br/>对于通过pom依赖的构件，在运行脚本直接指向本地Maven仓库；<br/>对于不是通过pom依赖的构件，则复制到`./lib`目录后再进行指向。<br/>此方式所发布应用的体积较小，但是运行环境中需存在Maven仓库。 |
| jarLibDir | 否 | ./lib（默认） | 复制依赖构件到所发布应用下的目录位置<br/>（影响`dependType`的复制目录，**一般无需修改**） |
| cmpPathMode | 否 | ○ LEAST<br/>○ STAND<br/>○ MOST | 运行脚本中指向依赖构件路径的压缩模式。<br/>若运行脚本中`-cp *.jar`的依赖构件都是绝对路径，则会导致命令<br/>过长不易维护，因此本插件会提取相同前缀的路径并创建对应的路<br/>径前缀变量。此配置项的作用仅仅是影响这些变量的多寡而已，无<br/>需过于关注。<br/><br/>**默认为STAND，即标准模式，一般无需修改** |
| mavenRepository | 是 | 绝对路径 | 本地Maven仓库位置，必须与本地部署的`apache-maven`的配置文<br/>件`settings.xml`中的配置项`<localRepository>`取值一致。<br/><br/>win环境推荐值为`D:\mavenRepository`<br/>unix环境推荐值为`/var/loacl/mavenRepository` |
| verClass | 是 | main类路径 | 运行脚本打印应用版本信息的入口类路径 |
| mainClass | 是 | main类路径 | 运行脚本启动应用程序的入口类路径 |
| mainArgs | 否 | &nbsp; | 启动应用程序的main类参数表，按需填写即可 |
| charset | 否 | &nbsp; | 项目编码，**默认为UTF-8** |
| jdkPath | 否 | java（默认） | JRE路径，亦即`%JAVA_HOME%/bin/java`路径。<br/>由于配置了系统环境变量，一般无需修改。<br/>但若生产环境下存在多个JRE版本，则可按需修改。 |
| xms | 否 | 32m（默认） | 应用运行时的最小堆内存，按需填写即可 |
| xmx | 否 | 64m（默认） | 应用运行时的最大堆内存，按需填写即可 |
| jdkParams | 否 | &nbsp; | JVM参数表，按需填写即可 |
| threadSuffix | 否 | &nbsp; | 进程后缀名。<br/>所发布的应用默认用项目名作为进程名，若指定了后缀则会自动<br/>附加到进程名末尾，一般用于同一生产环境下部署多套应用时以<br/>作区分。（实际上在使用本插件发布应用后，也可通过直接修改<br/>`_threadname`文件达到同样目的） |
| noPrjVer | 否 | true（默认） | 所发布应用的自身jar文件是否去掉版本号。<br/>默认值为true（即去掉版本号），以便升级时无需调整运行脚本 |
| noVerJarRegex | 否 | &nbsp; | 命中**正则表达式**的\*.jar依赖构件去掉版本号。<br/>建议配置版本迭代较快的依赖构件，以便升级时无需调整运行脚本 |
| proguard | 否 | false（默认） | 是否启用混淆打包，可有效防止应用被反编译。<br/>需配置`proguard-maven-plugin`插件支持，但是proguard插件配置<br/>项过于复杂，推荐使用[`Maven项目规范骨架 mojo-archetype`](https://github.com/lyy289065406/mojo-archetype)创建项<br/>目，即可自动生成混淆配置。 |


## 附1：混淆打包插件 proguard-maven-plugin 的配置示例

```xml
<!-- 混淆打包插件 -->
<plugin>
    <groupId>com.github.wvengen</groupId>
    <artifactId>proguard-maven-plugin</artifactId>
    <version>2.0.7</version>
    <executions>
        <execution>
            <phase>package</phase>    <!-- 触发混淆打包的maven周期 -->
            <goals>
                <goal>proguard</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <!-- attach 的作用是在 install/deploy 时, 将生成的 pg 文件也安装/部署 -->
        <attach>false</attach>
        <attachArtifactClassifier>pg</attachArtifactClassifier>
        
        <!-- 指定混淆处理所需要的库文件 -->
        <libs>
            <lib>${java.home}/lib/rt.jar</lib> <!-- 运行时库rt是必须的 -->
        </libs>
        
        <!-- 指定要做混淆处理的 应用程序jar、war、ear，或目录 -->
        <injar></injar>
        
        <!-- 指定混淆处理完后要输出的jar、war、ear，及其目录名称 -->
        <outjar>${project.build.finalName}-pg</outjar>
        
        <!-- 混淆规则: 详细配置方式参考 ProGuard 官方文档 -->
        <options>
            <option>-ignorewarnings</option>         <!-- 忽略混淆警告 -->
            <!-- option>-dontobfuscate</option -->    <!-- 不混淆输入的类文件 -->
            <option>-dontshrink</option>               <!-- 不压缩输入的类文件 -->
            <option>-dontoptimize</option>             <!-- 不做代码优化 -->
            <option>-dontskipnonpubliclibraryclasses</option>        <!-- 不跳过私有依赖的类库 -->
            <option>-dontskipnonpubliclibraryclassmembers</option>    <!-- 不跳过私有依赖的类库成员 -->
            <!-- option>-overloadaggressively</option -->                <!-- 混淆时应用侵入式重载 -->
            <!-- option>-obfuscationdictionary {filename}</option -->    <!-- 使用给定文件中的关键字作为要混淆方法的名称 -->
            <!-- option>-applymapping {filename}</option -->            <!-- 重用映射增加混淆 -->
            <!-- option>-useuniqueclassmembernames</option -->            <!-- 确定统一的混淆类的成员名称来增加混淆 -->
            <!-- option>-dontusemixedcaseclassnames</option -->            <!-- 混淆时不会产生形形色色的类名 -->
            <!-- option>-renamesourcefileattribute {string}</option -->    <!-- 设置源文件中给定的字符串常量 -->
            <!-- option>-flattenpackagehierarchy {package_name}</option -->    <!-- 重新包装所有重命名的包并放在给定的单一包中 -->
            <!-- option>-repackageclass {package_name}</option -->            <!-- 重新包装所有重命名的类文件中放在给定的单一包中 -->


            <!--平行包结构（重构包层次），所有混淆的代码放在 pg 包下 -->
            <!-- 最好不要随便放, 若有多个项目混淆，不同jar的混淆类可能重名 -->
            <!-- 建议为{project.root.package}.pg （不存在此变量，此处仅为了说明） -->
            <option>-repackageclasses exp.libs.pojo.pg</option>

            <!-- 保留[源码] --><!-- 按实际项目切换 -->
            <!-- option>-keepattributes SourceFile</option -->
            
            <!-- 保留[行号] --><!-- 按实际项目切换 -->
            <option>-keepattributes LineNumberTable</option>
            
            <!-- 保留[注释] --><!-- 按实际项目切换 -->
            <!-- option>-keepattributes *Annotation*</option -->
            
            <!-- 保留[注解] --><!-- 按实际项目切换 -->
            <!-- option>-keepattributes Signature</option -->
            
            <!-- 保持[入口类]不变 -->
            <!-- 按实际项目修正 -->
            <option>-keep class 
                exp.libs.pojo.Version,
                exp.libs.pojo.Main
            </option>
            
            <!-- 保持[Bean类]不变（若框架对 Bean中的内容做了反射处理，则必须保持不变） -->
            <!-- 按实际项目修正 -->
            <option>-keep class exp.libs.pojo.bean.** { *;}</option>
            
            <!-- 保持[所有入口方法]不变 -->
            <!-- 固定不变 -->
            <option>-keepclasseswithmembers public class * { 
                        public static void main(java.lang.String[]);
                    }
            </option>
            
            <!-- 保持[对外API的类名和方法名]不变 -->
            <!-- 按实际项目修正 -->
            <option>-keep class exp.libs.pojo.api.** { *;}</option>
            
            <!-- 保持[所有本地化方法]不变 -->
            <!-- 固定不变 -->
            <option>-keepclasseswithmembernames class * {
                        native &lt;methods&gt;;
                    }
            </option>
            
            <!-- 保持[所有类成员变量]不变 -->
            <!-- 按实际项目修正 -->
            <!-- option>-keepclassmembers class * {
                        &lt;fields&gt;;
                    }
            </option -->
            
            <!-- 保持[所有枚举类必须的方法]不变 -->
            <!-- 固定不变 -->
            <option>-keepclassmembers class * extends java.lang.Enum {
                        public static **[] values();
                        public static ** valueOf(java.lang.String);
                    }
            </option>
            
            <!-- 保持[所有序列化接口]不变（若项目中不使用序列化，也可注释） -->
            <!-- 固定不变 -->
            <option>-keepclassmembers class * implements java.io.Serializable {
                        static final long serialVersionUID;
                        static final java.io.ObjectStreamField[] serialPersistentFields;
                        private void writeObject(java.io.ObjectOutputStream);
                        private void readObject(java.io.ObjectInputStream);
                        java.lang.Object writeReplace();
                        java.lang.Object readResolve();
                    }
            </option>
        </options>
    </configuration>
</plugin>
```

## 附2：部署文件复制插件 maven-antrun-plugin 的配置示例

```xml
<!-- Ant插件：项目部署文件复制 -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-antrun-plugin</artifactId>
    <version>1.7</version>
    <executions>
        <execution>
            <id>ant-build</id>
            <phase>install</phase>
            <goals>
                <goal>run</goal>
            </goals>
            <configuration>
                <target>
                    <echo>拷贝数据库脚本</echo>
                    <copy todir="${release.dir}/script">
                        <fileset dir="script"></fileset>
                    </copy>
                    <echo>拷贝项目配置文件</echo>
                    <copy todir="${release.dir}/conf">
                        <fileset dir="conf" />
                    </copy>
                    <echo>拷贝文档</echo>
                    <copy todir="${release.dir}/doc/04_维护文档">
                        <fileset dir="doc/04_维护文档" />
                    </copy>
                    <copy todir="${release.dir}/doc/06_使用文档">
                        <fileset dir="doc/06_使用文档" />
                    </copy>
                    <copy todir="${release.dir}/doc/07_演示文档">
                        <fileset dir="doc/07_演示文档" />
                    </copy>
                </target>
            </configuration>
        </execution>
    </executions>
</plugin>
```


## 版权声明

　[![Copyright (C) 2016-2018 By EXP](https://img.shields.io/badge/Copyright%20(C)-2006~2018%20By%20EXP-blue.svg)](http://exp-blog.com)　[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
  

- Site: [http://exp-blog.com](http://exp-blog.com) 
- Mail: <a href="mailto:289065406@qq.com?subject=[EXP's Github]%20Your%20Question%20（请写下您的疑问）&amp;body=What%20can%20I%20help%20you?%20（需要我提供什么帮助吗？）">289065406@qq.com</a>


------
