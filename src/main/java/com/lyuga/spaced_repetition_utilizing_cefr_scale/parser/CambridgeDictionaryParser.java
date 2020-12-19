package com.lyuga.spaced_repetition_utilizing_cefr_scale.parser;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CambridgeDictionaryParser extends HTMLParser {
	public String fetchLowestCefr(String query) {
		ArrayList<String> cefrList = new ArrayList<String>();
		try {
			String urlString = "https://dictionary.cambridge.org/dictionary/english/"
					+ URLEncoder.encode(query, "UTF-8");
			URL url = new URL(urlString);
			createXPathObject(url);

			// CEFRレベルが含まれたNodeをすべて取得
			NodeList cefrNodeList = (NodeList) xPath.evaluate("//*[starts-with(@class, 'epp-xref dxref')]", document,
					XPathConstants.NODESET);

			int cefrNodeListLength = cefrNodeList.getLength();
			// CEFRレベルが定義されていない場合、"undefined"と返却する
			if (cefrNodeListLength == 0) {
				return "undefined";
			}

			// Nodeごとに文字列を取得し、可変長配列であるcefrListへ追加
			for (int i = 0; i < cefrNodeListLength; i++) {
				Node cefrNode = cefrNodeList.item(i);
				cefrList.add(cefrNode.getTextContent());
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// cefrListから、最も低いCEFRレベルを取り出して返却
		return Collections.min(cefrList);
	}
}