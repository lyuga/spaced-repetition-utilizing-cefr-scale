package com.lyuga.spaced_repetition_utilizing_cefr_scale.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import nu.validator.htmlparser.dom.HtmlDocumentBuilder;

public class HTMLParser {
	protected XPath xPath;
	protected BufferedReader reader;
	protected Document document;

	protected void createXPathObject(URL url) throws IOException, SAXException {
		// 接続
		URLConnection connection = url.openConnection();
		connection.connect();

		InputStream inputStream = connection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
		reader = new BufferedReader(inputStreamReader);

		// DOMツリーの構築
		HtmlDocumentBuilder builder = new HtmlDocumentBuilder();
		document = builder.parse(new InputSource(reader));

		xPath = XPathFactory.newInstance().newXPath();
		// XPath式内で接頭辞 h がついている要素を HTML の要素として認識させる
		xPath.setNamespaceContext(new NamespaceContextHTML());
	}
}
