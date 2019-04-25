/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigExcludeController.java
 * 描述：
 * 修改人： liu
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.util;

import java.nio.ByteBuffer;
import java.util.Locale;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 对字符串进行防注入处理
 */
@Component
public class SqlProcessingUtil {

  /**
   * 判断是否 有注入式攻击
   * @param str 字符串
   * @return boolean
   */
  public boolean sqlInjectionProcessing(String str) {
   boolean isHasSQLInject = false;
      //字符串中的关键字更具需要添加
    String injStr = "'|and|exec|union|create|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|xp_|or|--|+";
    str = str.toLowerCase(Locale.US).trim();
    String[] injStrArray = injStr.split("\\|");
    for (String sql : injStrArray) {
      if (str.indexOf(sql) > -1) {
        isHasSQLInject = true;
        break;
      }
    }
    return isHasSQLInject;
  }

  /**
   *
   * @param str 字符串
   * @return  string
   */
  public String formatString(String str) {

    if (StringUtils.isEmpty(str)) {
     return "";
    } else {
      int stringLength = str.length();

        StringBuffer buf = new StringBuffer((int) (str.length() * 1.1));
        buf.append('\'');
        for (int i = 0; i < stringLength; ++i) {
          char c = str.charAt(i);
          switch (c) {
            case 0: /* Must be escaped for 'mysql' */
              buf.append('\\');
              buf.append('0');
              break;
            case '\n': /* Must be escaped for logs */
              buf.append('\\');
              buf.append('n');
              break;
            case '\r':
              buf.append('\\');
              buf.append('r');
              break;
            case '\\':
              buf.append('\\');
              buf.append('\\');
              break;
            case '\'':
              buf.append('\\');
              buf.append('\'');
              break;
            case '"': /* Better safe than sorry */
//              if (this.usingAnsiMode) {
                buf.append('\\');
//              }
              buf.append('"');
              break;
            case '\032': /* This gives problems on Win32 */
              buf.append('\\');
              buf.append('Z');

              break;

            case '\u00a5':
            case '\u20a9':
                ByteBuffer bbuf = ByteBuffer.allocate(1);
                if (bbuf.get(0) == '\\') {
                  buf.append('\\');
                }
//              }
              // fall through
            default:
              buf.append(c);
          }
        }
        buf.append('\'');
        str = buf.toString();
      return str;
      }
  }
}
