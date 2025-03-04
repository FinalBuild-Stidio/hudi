/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hudi.common.util;

import javax.annotation.Nullable;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Simple utility for operations on strings.
 */
public class StringUtils {

  public static final char[] HEX_CHAR = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  public static final String EMPTY_STRING = "";

  /**
   * <p>
   * Joins the elements of the provided array into a single String containing the provided list of elements.
   * </p>
   *
   * <p>
   * No separator is added to the joined String. Null objects or empty strings within the array are represented by empty
   * strings.
   * </p>
   *
   * <pre>
   * StringUtils.join(null)            = null
   * StringUtils.join([])              = ""
   * StringUtils.join([null])          = ""
   * StringUtils.join(["a", "b", "c"]) = "abc"
   * StringUtils.join([null, "", "a"]) = "a"
   * </pre>
   */
  public static <T> String join(final String... elements) {
    return join(elements, EMPTY_STRING);
  }

  public static <T> String joinUsingDelim(String delim, final String... elements) {
    return join(elements, delim);
  }

  public static String join(final String[] array, final String separator) {
    if (array == null) {
      return null;
    }
    return org.apache.hadoop.util.StringUtils.join(separator, array);
  }

  /**
   * Wrapper of {@link java.lang.String#join(CharSequence, Iterable)}.
   *
   * Allow return {@code null} when {@code Iterable} is {@code null}.
   */
  public static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
    if (elements == null) {
      return null;
    }
    return String.join(delimiter, elements);
  }

  public static String join(final List<String> list, final String separator) {
    if (list == null || list.size() == 0) {
      return null;
    }
    return org.apache.hadoop.util.StringUtils.join(separator, list.toArray(new String[0]));
  }

  public static String toHexString(byte[] bytes) {
    return new String(encodeHex(bytes));
  }

  public static char[] encodeHex(byte[] data) {
    int l = data.length;
    char[] out = new char[l << 1];
    int i = 0;

    for (int var4 = 0; i < l; ++i) {
      out[var4++] = HEX_CHAR[(240 & data[i]) >>> 4];
      out[var4++] = HEX_CHAR[15 & data[i]];
    }

    return out;
  }

  public static byte[] getUTF8Bytes(String str) {
    return str.getBytes(StandardCharsets.UTF_8);
  }

  public static boolean isNullOrEmpty(String str) {
    return str == null || str.length() == 0;
  }

  public static boolean nonEmpty(String str) {
    return !isNullOrEmpty(str);
  }

  /**
   * Returns the given string if it is non-null; the empty string otherwise.
   *
   * @param string the string to test and possibly return
   * @return {@code string} itself if it is non-null; {@code ""} if it is null
   */
  public static String nullToEmpty(@Nullable String string) {
    return string == null ? "" : string;
  }

  public static String objToString(@Nullable Object obj) {
    if (obj == null) {
      return null;
    }
    return obj instanceof ByteBuffer ? toHexString(((ByteBuffer) obj).array()) : obj.toString();
  }

  /**
   * Returns the given string if it is nonempty; {@code null} otherwise.
   *
   * @param string the string to test and possibly return
   * @return {@code string} itself if it is nonempty; {@code null} if it is empty or null
   */
  public static @Nullable String emptyToNull(@Nullable String string) {
    return stringIsNullOrEmpty(string) ? null : string;
  }

  private static boolean stringIsNullOrEmpty(@Nullable String string) {
    return string == null || string.isEmpty();
  }

  /**
   * Splits input string, delimited {@code delimiter} into a list of non-empty strings
   * (skipping any empty string produced during splitting)
   */
  public static List<String> split(@Nullable String input, String delimiter) {
    if (isNullOrEmpty(input)) {
      return Collections.emptyList();
    }
    return Stream.of(input.split(delimiter)).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
  }

  public static String getSuffixBy(String input, int ch) {
    int i = input.lastIndexOf(ch);
    if (i == -1) {
      return input;
    }
    return input.substring(i);
  }

  public static String removeSuffixBy(String input, int ch) {
    int i = input.lastIndexOf(ch);
    if (i == -1) {
      return input;
    }
    return input.substring(0, i);
  }
}
