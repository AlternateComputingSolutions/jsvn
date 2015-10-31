package com.alternatecomputing.jsvn.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {

	/**
	 * transform a string in ISO-8859-1 encoding (that used by property files) to US_ASCII (that which is returned by subversion)
	 * @param text
	 * @return normalized string
	 */
	public static String normalizeFileEncoding(String text) {
		if (text != null) {
			try {
				String result = new String(text.getBytes("ISO-8859-1"), "US-ASCII");
				return result.replace('\\', '/');
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return text;
	}
}
