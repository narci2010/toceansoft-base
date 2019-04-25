# 秒嘀短信 拓胜MobileMsgUtils工具类使用指南
## 1.在pom.xml引入jar包。
    <dependency>
      <groupId>com.toceansoft</groupId>
      <artifactId>toceansoft-common</artifactId>
      <version>0.0.1-RELEASE</version>
    </dependency>

## 2. API使用方法：
### 1）.系统需要发送短信    
    在项目中直接引入MobileMsgUtils工具类调用方法返回boolean值
        // 1、准备秒嘀参数
        String url = "https://api.miaodiyun.com/20150822/call/voiceCode"; // 秒嘀发送短信的url
        String accountSid = "dab8d1353f854ebaba9ca6399a6c574b";  // 秒嘀账户id
        String token = "45821a78bed44630900014c9d140cac4";  // 秒嘀账户token  
        // 2、准备短信参数
        String templateID = "45821a7";  // 秒嘀短信模板id 
        String mobile = "13533695842"; // 接收短信电话号码
        String param = "小明,3254"; // 短信模板内需要替换的参数，多个参数用英文逗号隔开
        // 将短信参数用放进map中传递
        Map<String, String> model = new HashMap<>();        
        model.put("templateid", templateID);
        model.put("to", mobile);
        model.put("param", param);
        // 3、调用工具类方法发送短信
        MiaoDiSmsUtils.sendCommonWithMiaodi(url, accountSid, token, model);
