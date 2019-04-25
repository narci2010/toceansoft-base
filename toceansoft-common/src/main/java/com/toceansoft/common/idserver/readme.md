# ToceanUuid 拓胜UUID工具类使用指南
## 1.在pom.xml引入jar包。
    <dependency>
      <groupId>com.toceansoft</groupId>
      <artifactId>toceansoft-common</artifactId>
      <version>0.0.1-RELEASE</version>
    </dependency>

## 2. API使用方法：
### 1).系统需要Long类型的UUID
    在项目application.yml文件中添加如下配置
    id: 
      workerid: 5
      datacenterid: 3
    // 两个参数取值都是0-31,如果不做配置，则两个参数默认值都为0
    // 返回值为Long类型的UUID
    Long uuid = ToceanUuid.getLongUuid();
    例子：445902383138996233（转成String，最长19个字符）
### 2).系统需要String类型的UUID
    String uuid=ToceanUuid.getStringUuid();
    例子:201805151056A5EC1AB7F6554F1A919DA595E0167DF3（长度44个字符）