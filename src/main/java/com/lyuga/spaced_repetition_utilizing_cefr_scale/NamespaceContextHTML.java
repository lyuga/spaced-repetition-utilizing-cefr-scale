package com.lyuga.spaced_repetition_utilizing_cefr_scale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

// HTML用に名前空間接頭辞とURIを対応させる
public class NamespaceContextHTML implements NamespaceContext {
	private HashMap<String, String> prefixToURI;

	public NamespaceContextHTML() {
		prefixToURI = new HashMap<String, String>();
		prefixToURI.put("h", "http://www.w3.org/1999/xhtml");
	}

	// 接頭辞からURIを求める
	public String getNamespaceURI(String prefix) {
		return prefixToURI.get(prefix);
	}

	public Iterator<String> getPrefixes(String uri) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, String> entry : prefixToURI.entrySet()) {
			if (entry.getValue().equals(uri)) {
				list.add(entry.getKey());
			}
		}
		return list.iterator();
	}

	public String getPrefix(String uri) {
		return getPrefixes(uri).next();
	}
}