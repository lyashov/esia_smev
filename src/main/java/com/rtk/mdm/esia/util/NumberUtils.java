package com.rtk.mdm.esia.util;

public class NumberUtils {
	public static Double parseDouble(String source) {
		if(source == null) {
			return null;
		}

		String replaced = source.replaceAll(",", ".");

		try {
			return Double.parseDouble(replaced);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Integer parseInteger(String source) {
		if(source == null) {
			return null;
		}

		try {
			return Integer.parseInt(source);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Long parseLong(String source) {
		if(source == null) {
			return null;
		}

		try {
			return Long.parseLong(source);
		} catch (NumberFormatException e) {
			return null;
		}

	}
}
