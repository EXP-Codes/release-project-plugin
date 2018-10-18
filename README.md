# mojo-release-plugin
Maven项目发布插件

------


## 运行环境

　![](https://img.shields.io/badge/Maven-3.2.5%2B-brightgreen.svg)  ![](https://img.shields.io/badge/JDK-1.7%2B-brightgreen.svg)


## 软件介绍


　快速发布一个可运行于生成环境的应用程序（集成运行环境、部署文档、版本说明、运行脚本等）

> 支持混淆打包（使用说明待补充）


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
                <cmpPathMode>STAND</cmpPathMode>
            </configuration>
        </execution>
    </executions>
</plugin>
```

其中，各个参数说明如下：

| 参数 | 必填 | 取值范围/约束 | 作用 |
|:----:|:--------:|:--------|:----|
| dependType | 否 | ○ SELF<br/>○ MAVEN | 影响所发布项目的运行脚本中 `-cp *.jar` 所依赖构件的指向位置：<br/><br/>**SELF（默认）** ： <br/>把所有依赖构件都复制到`./lib`目录下，运行脚本直接指向`./lib`。<br/>此方式所发布应用的体积较大，但是可放在任何环境中运行。<br/><br/>**MAVEN** ： <br/>对于通过pom依赖的构件，在运行脚本直接指向本地Maven仓库；<br/>对于不是通过pom依赖的构件，则复制到`./lib`目录后再进行指向。<br/>此方式所发布应用的体积较小，但是运行环境中需存在Maven仓库。 |
| jarLibDir | 否 | ./lib（默认） | 复制依赖构件到发布应用的目录<br/>（影响`dependType`的复制目录，一般无需改动） |
| mavenRepository | 否 | 绝对路径 | 本地Maven仓库位置，必须与本地部署的`apache-maven`的配置文<br/>件`settings.xml`中的配置项`&lt;localRepository&gt;`取值一致。<br/>win环境推荐值为`D:\mavenRepository`，unix环境推荐值为`/var/loacl/mavenRepository` |
| verClass | 是 | &nbsp; | 版本类路径（用于启动脚本打印项目版本信息） |
| mainClass | 是 | &nbsp; | 项目入口类路径（用于启动脚本启动程序） |
| mainArgs | 否 | &nbsp; | 入口类参数表 |
| charset | 否 | UTF-8（默认） | 项目编码 |
| jdkPath | 否 | java（默认） | JDK路径（若有配置系统环境变量则无需修改） |
| xms | 否 | 32m（默认） | 最小堆内存 |
| xmx | 否 | 64m（默认） | 最大堆内存 |
| jdkParams | 否 | &nbsp; | JDK参数表 |
| threadSuffix | 否 | &nbsp; | 进程后缀名。所发布的项目一般用项目名作为进程名，<br/>指定后缀后会自动附加到进程名末尾，一般用于同一台机器部署多套时区分 |
| noPrjVer | 否 | true（默认） | 所发布的项目jar文件是否带版本号 |
| noVerJarRegex | 否 | &nbsp; | 命中正则的\*.jar依赖构件去掉版本号 |
| proguard | 否 | false（默认） | 是否启用混淆打包（需配置proguard插件支持） |
| cmpPathMode | 否 | STAND（默认） | 启动脚本的压缩路径模式（一般无需修改） |


## 版权声明

　[![Copyright (C) 2016-2018 By EXP](https://img.shields.io/badge/Copyright%20(C)-2006~2018%20By%20EXP-blue.svg)](http://exp-blog.com)　[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
  

- Site: [http://exp-blog.com](http://exp-blog.com) 
- Mail: <a href="mailto:289065406@qq.com?subject=[EXP's Github]%20Your%20Question%20（请写下您的疑问）&amp;body=What%20can%20I%20help%20you?%20（需要我提供什么帮助吗？）">289065406@qq.com</a>


------
