package com.lecture.util;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lecture.localdata.DetailInfo;


public class ReadInfoUtil {

	public static DetailInfo getInfo(String filePath) throws Exception {
		DetailInfo detailInfo = new DetailInfo();
		try {
			File file = new File(filePath);
			Document doc = Jsoup.parse(file, "UTF-8");
			Elements infos = doc.getElementsByTag("p");
			// Elements
			// Elements
			detailInfo.setLec_aboutSpeaker("        " + infos.get(0).text());
			detailInfo.setLec_about("        " + infos.get(1).text());
		} catch (Exception e) {
			throw e;
		}
		return detailInfo;
	}

}
