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

在指定项目的pom.xml中添加本插件即可，完整参数配置如下：

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
| dependType | 否 | SELF（默认） | &nbsp; |
| jarLibDir | 否 | ./lib（默认） | &nbsp; |
| mavenRepository | 否 | D:\mavenRepository（默认） | &nbsp; |
| verClass | 是 | &nbsp; | &nbsp; |
| mainClass | 是 | &nbsp; | &nbsp; |
| mainArgs | 否 | &nbsp; | &nbsp; |
| charset | 否 | UTF-8（默认） | &nbsp; |
| jdkPath | 否 | java（默认） | &nbsp; |
| xms | 否 | 32m（默认） | &nbsp; |
| xmx | 否 | 64m（默认） | &nbsp; |
| jdkParams | 否 | &nbsp; | &nbsp; |
| threadSuffix | 否 | &nbsp; | &nbsp; |
| noPrjVer | 否 | true（默认） | &nbsp; |
| noVerJarRegex | 否 | &nbsp; | &nbsp; |
| proguard | 否 | false（默认） | &nbsp; |
| cmpPathMode | 否 | STAND（默认） | &nbsp; |


## 版权声明

　[![Copyright (C) 2016-2018 By EXP](https://img.shields.io/badge/Copyright%20(C)-2006~2018%20By%20EXP-blue.svg)](http://exp-blog.com)　[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
  

- Site: [http://exp-blog.com](http://exp-blog.com) 
- Mail: <a href="mailto:289065406@qq.com?subject=[EXP's Github]%20Your%20Question%20（请写下您的疑问）&amp;body=What%20can%20I%20help%20you?%20（需要我提供什么帮助吗？）">289065406@qq.com</a>


------
