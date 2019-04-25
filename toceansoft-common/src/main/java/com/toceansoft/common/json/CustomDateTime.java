/**
 *   
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CustomDateTime.java
 * 描述：  
 * 修改人：Arber.Lee  
 * 修改时间：2017年11月24日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */    
package com.toceansoft.common.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**  
 * 自定义返回JSON 数据格中日期格式化处理  
 * @author Arber.Lee
 * @version 1.0.0   
 *  CustomDateTime  
 * @since 2017年11月24日  
 */

public class CustomDateTime extends JsonSerializer<Date> {

    @Override
    public void serialize(Date value,
                          JsonGenerator jsonGenerator,
                          SerializerProvider provider)
            throws IOException, JsonProcessingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonGenerator.writeString(sdf.format(value));
    }
}
