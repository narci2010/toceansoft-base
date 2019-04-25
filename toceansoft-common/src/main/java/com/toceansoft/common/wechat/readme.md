# WechatUtils 拓胜微信工具类使用指南
## 1.在pom.xml引入jar包。
    <dependency>
      <groupId>com.toceansoft</groupId>
      <artifactId>toceansoft-common</artifactId>
      <version>0.0.1-RELEASE</version>
    </dependency>

## 2. API使用方法：
### 1).微信公众号分享接口调用
    在项目根目录添加config.properties文件，配置如下信息
      wechat.offical.account.appid=xxxxx //公众号appid
      wechat.offical.account.appsecret=yyyyyy //公众号appsecret
    // url为String类型，前端传过来需要encodeURL
    Map<String,String> map =getResult(url)
    map的值返回给前端，用来设置wx.config的值
    //注意：在Spring环境下，支持通过spring.profiles.active设置不同环境采用不同的配置文件
    //e.g. spring.profiles.active=dev，则读取config-dev.properties  spring.profiles.active=docker，则读取config-docker.properties