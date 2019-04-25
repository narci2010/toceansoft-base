# html转成pdf 拓胜PdfUtils工具类使用指南
## 1.在pom.xml引入jar包。
    <dependency>
      <groupId>com.toceansoft</groupId>
      <artifactId>toceansoft-common</artifactId>
      <version>0.0.1-RELEASE</version>
    </dependency>

## 2. API使用方法：
### 1).系统需要将html文件转成pdf    
    准备参数
        String htmlPath = "E:/aa/bb/temp.html"; // html文件路径
        String pdfDir = "E:/cc/dd/";  // pdf存放路径     
        
    在项目中直接引入ImageUtils工具类调用方法返回pdf文件路径："E:/cc/dd/1581554857855.pdf"
            // 1、生成默认大小二维码
        String pdfPath = PdfUtils.htmlToPdf(htmlPath,pdfDir); 
        
