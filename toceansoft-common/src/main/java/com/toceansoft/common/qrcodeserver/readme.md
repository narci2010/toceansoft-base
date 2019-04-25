# QRCode 拓胜QRCodeUtils工具类使用指南
## 1.在pom.xml引入jar包。
    <dependency>
      <groupId>com.toceansoft</groupId>
      <artifactId>toceansoft-common</artifactId>
      <version>0.0.1-RELEASE</version>
    </dependency>

## 2. API使用方法：
### 1).系统需要生成二维码
    
    准备参数，其中生成二维码大小为默认大小240x240
        String logoPath = "E:/aa/bb/logo.jpg"; // logo图片地址
        String imagePath = "E:/cc/dd/qrcode.jpg";  // 生成二维码全路径
        String content = "www.baidu.com";  // 扫描二维码获得的信息或跳转的路径
        String text = "扫码有惊喜"; // 二维码下方显示的文字信息
        int size = 240; // 自定义二维码图片大小尺寸
        
    在项目中直接引入QRCodeUtils工具类调用方法即可
            // 1、生成默认大小二维码
        QRCodeUtils.createQRCode(imagePath,content); 
            // 2、生成默认大小二维码，带logo
        QRCodeUtils.createQRCode(logoPath,imagePath,content); 
            // 3、生成默认大小二维码，带logo，显示提示文字 
        QRCodeUtils.createQRCode(logoPath,imagePath,content,text); 
            // 4、生成自定义大小二维码，带logo
        QRCodeUtils.createQRCode(logoPath,imagePath,content,size);  
            // 5、生成自定义二维码，根据需求传参，如果不需要logo则 logoPath传null；
                如果不需要显示提示文字，则text传null；如果不需要自定义大小，则size传0；           
        QRCodeUtils.createQRCode(logoPath,imagePath,content,text,size);
