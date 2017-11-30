package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;


public class SimpleHTMLCrawler {
    public boolean shouldVisit(Page referringPage, WebURL url) {
        return isInRootDomain(url) && isHTMLPage(url);
    }

    private boolean isHTMLPage(WebURL url) {
        return false;

    }

    private boolean isInRootDomain(WebURL url) {
        return false;
    }
}
