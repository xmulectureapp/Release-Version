package com.lecture.util;

import java.util.List;

public class ListToArrayUtil {

	public static String[] translate(List<String> list) {
		String[] array = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null) {
				array[i] = "Ä¬ÈÏÈÕÀú";
			} else {
				array[i] = list.get(i);
			}
		}
		return array;
	}
}
