package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class TestTools {
    public Page constructTestPage(String url, String html) {
        WebURL urlOfPageToStore = new WebURL();
        urlOfPageToStore.setURL(url);
        Page pageToStore = new Page(urlOfPageToStore);
        HtmlParseData testParseData = new HtmlParseData();
        testParseData.setHtml(html);
        pageToStore.setParseData(testParseData);
        return pageToStore;
    }
}
