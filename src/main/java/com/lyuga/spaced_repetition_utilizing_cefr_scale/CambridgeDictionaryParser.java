package com.lyuga.spaced_repetition_utilizing_cefr_scale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import nu.validator.htmlparser.dom.HtmlDocumentBuilder;

public class CambridgeDictionaryParser {
	private ArrayList<String> cefrList = new ArrayList<String>();

	public String fetchLowestCefr(String query) {
		try {
			String urlString = "https://dictionary.cambridge.org/dictionary/english/"
					+ URLEncoder.encode(query, "UTF-8");
			URL url = new URL(urlString);

			// 接続
			URLConnection connection = url.openConnection();
			connection.connect();

			InputStream inputStream = connection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader reader = new BufferedReader(inputStreamReader);

			// DOMツリーの構築
			HtmlDocumentBuilder builder = new HtmlDocumentBuilder();
			Document document = builder.parse(new InputSource(reader));

			XPath xPath = XPathFactory.newInstance().newXPath();
			// XPath式内で接頭辞 h がついている要素を HTML の要素として認識させる
			xPath.setNamespaceContext(new NamespaceContextHTML());

			// CEFRレベルが含まれたNodeをすべて取得
			NodeList cefrNodeList = (NodeList) xPath.evaluate("//*[starts-with(@class, 'epp-xref dxref')]", document,
					XPathConstants.NODESET);

			// Nodeごとに文字列を取得し、可変長配列であるcefrListへ追加
			for (int i = 0; i < cefrNodeList.getLength(); i++) {
				Node cefrNode = cefrNodeList.item(i);
				cefrList.add(cefrNode.getTextContent());
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// cefrListから、最も低いCEFRレベルを取り出して返却
		return Collections.min(cefrList);
	}
}