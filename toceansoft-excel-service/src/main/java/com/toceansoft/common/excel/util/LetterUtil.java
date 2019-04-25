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

/**
 * 字母工具类.
 *
 * @author asr
 */
public final class LetterUtil {

  /**
   * 数字转字母.
   *
   * @param number 数字
   * @return 字母
   */
  public static String numberToLetter(int number) {
    if (number <= 0) {
      return null;
    }
    String letter = "";
    number--;
    do {
      if (letter.length() > 0) {
        number--;
      }
      letter = ((char) (number % 26 + (int) 'A')) + letter;
      number = (int) ((number - number % 26) / 26);
    } while (number > 0);

    return letter;
  }
}
