# pdf转成图片 拓胜ImageUtils工具类使用指南
## 1.在pom.xml引入jar包。
    <dependency>
      <groupId>com.toceansoft</groupId>
      <artifactId>toceansoft-common</artifactId>
      <version>0.0.1-RELEASE</version>
    </dependency>

## 2. API使用方法：
### 1）.系统需要将pdf文件转成图片    
    准备参数
        String pdfPath = "E:/aa/bb/temp.pdf"; // pdf文件路径
        String imageDir = "E:/cc/dd/";  // 图片存放文件夹路径     
        
    在项目中直接引入ImageUtils工具类调用方法返回图片路径集合："E:/cc/dd/temp-1.jpg"
            // 1、生成默认大小二维码
        List<String> list = ImageUtils.pdfToImages(pdfPath,imageDir); 
### 2）.系统需要将ppt文件转成图片    
    准备参数
        String pptPath = "E:/aa/bb/temp.ppt"; // ppt文件路径
        String imageDir = "E:/cc/dd/";  // 图片存放文件夹路径     
        
    在项目中直接引入ImageUtils工具类调用方法返回图片路径集合："E:/cc/dd/temp-1.jpg"
            // 1、生成默认大小二维码
        List<String> list = ImageUtils.pptToImages(pptPath,imageDir); 