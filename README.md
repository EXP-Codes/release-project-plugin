# mojo-release-plugin
Maven项目发布插件

------


## 运行环境

　![](https://img.shields.io/badge/Maven-3.2.5%2B-brightgreen.svg)  ![](https://img.shields.io/badge/JDK-1.7%2B-brightgreen.svg)


## 软件介绍

- 自动化构建项目（告别重复劳动）
- 规范项目结构（只要创建，就是规范）
- 自动管理依赖构件（告别jar版本混乱）
- 一键发布项目/基线（告别各种脚本）


　快读发布一个可运行于生成环境的应用程序（继承运行环境、部署文档、版本说明、启动脚本等）

> 支持混淆打包（使用说明待补充）


## 使用说明

本插件通过Maven install指令触发，在指定项目的pom.xml中添加本插件即可，完整参数配置如下：

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

| 参数 | 是否必填 | 取值范围 | 作用 |
|:----:|:--------:|:--------:|:----:|
| dependType | 否 | SELF（默认） | 发布项目后，在启动脚本中\*.jar依赖构件的引用位置。<br/>除非机器有公共库，一般不用改，SELF表示把所有构件复制到项目内。 |
| jarLibDir | 否 | ./lib（默认） | 复制到\*.jar依赖构件到项目内的目录位置 |
| mavenRepository | 否 | D:\mavenRepository（默认） | 本地Maven仓库位置 |
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
