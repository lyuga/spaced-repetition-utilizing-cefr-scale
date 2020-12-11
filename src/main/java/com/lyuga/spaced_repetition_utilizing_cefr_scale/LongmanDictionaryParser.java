package com.lyuga.spaced_repetition_utilizing_cefr_scale;

import java.net.URL;
import java.net.URLEncoder;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LongmanDictionaryParser extends HTMLParser {
	public String fetchTranslation(String query) {
		StringBuilder sb = new StringBuilder();
		try {
			String urlString = "https://www.ldoceonline.com/jp/dictionary/english-japanese/"
					+ URLEncoder.encode(query, "UTF-8");
			URL url = new URL(urlString);
			createXPathObject(url);

			// 品詞Nodesを取得する
			NodeList partsOfSpeechNodeList = (NodeList) xPath.evaluate(
					"//h:div[contains(@class, 'lejEntry')]/h:span[@class='Wordclass']", document,
					XPathConstants.NODESET);

			for (int i = 0; i < partsOfSpeechNodeList.getLength(); i++) {
				Node itemNode = partsOfSpeechNodeList.item(i);
				NodeList translationNodeList;

				// 特定の品詞において、クラス属性が"FREQTRAN"であるspan要素を全て取得する
				NodeList freqtranNodeList = (NodeList) xPath.evaluate(
						"h:span[@class='Sense']//h:span[@class='Translation']/h:span[contains(@class, 'FREQTRAN')]",
						itemNode, XPathConstants.NODESET);

				// クラス属性が"POS"であるspan要素を品詞として格納する
				String partOfSpeech = xPath.evaluate("h:span[@class='POS']", itemNode) + "\t";
				sb.append(partOfSpeech);

				if (freqtranNodeList.getLength() > 0) {
					translationNodeList = freqtranNodeList;
				} else {
					// 特定の品詞において、クラス属性が"TRAN"であるspan要素を全て取得する
					translationNodeList = (NodeList) xPath.evaluate(
							"h:span[@class='Sense']//h:span[@class='Translation']/h:span[contains(@class, 'TRAN')]",
							itemNode, XPathConstants.NODESET);
				}

				for (int j = 0; j < translationNodeList.getLength(); j++) {
					Node meaningNode = translationNodeList.item(j);
					// 句読点, 空白とタブを""に置き換え、最後に", "を追加する
					String meaning = meaningNode.getTextContent().replaceAll("\\p{Punct}", "").replaceAll("\\p{Blank}",
							"") + ", ";
					sb.append(meaning);
				}

				// 末尾の", "を削除し、"\n"を追加する
				int startIndex = sb.length() - 2;
				sb.delete(startIndex, startIndex + 2).append("\n");
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}