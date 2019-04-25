#  拓胜HtmlUtil工具类使用指南
## 1.在pom.xml引入jar包。
    <dependency>
      <groupId>com.toceansoft</groupId>
      <artifactId>toceansoft-common</artifactId>
      <version>0.0.1-RELEASE</version>
    </dependency>

## 2. API使用方法：
### 1）.在body里指定标签后面插入html片段    
    准备参数
        String input ="C:/Users/Administrator/Desktop/test.html";
        String output ="C:/Users/Administrator/Desktop/test2.html";
        String session = "<a href='www.baidu.com'>百度一下</a>";
        String tag = "p"; p标签、a标签等
        int times = 10;
    调用方法   
        原html文件中所有p标签后面插入html片段session
        HtmlUtil.insertNewHtmlSection(input,output,session,tag); 
        
        原html文件中所有p标签中前10个标签后面插入html片段session，times=0，全部插入
        HtmlUtil.insertNewHtmlSection(input,output,session,tag,times);
        
