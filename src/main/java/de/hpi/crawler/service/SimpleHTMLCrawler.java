package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;


public class SimpleHTMLCrawler {
    public boolean shouldVisit(Page referringPage, WebURL url) {
        url.setURL(url.getURL().toLowerCase());

        WebURL referringPageURL = referringPage.getWebURL();
        referringPageURL.setURL(referringPageURL.getURL().toLowerCase());
        referringPage.setWebURL(referringPageURL);

        return isInRootDomain( referringPage, url) && isHTMLPage(url);
    }


    boolean isHTMLPage(WebURL url) {
        return !url.getURL().matches(".*\\.(jpg|png|js|css|jpeg|txt|epub|fb2|docx|doc|xls|zip|rar|pdf|gif|gz|bin|dmg|iso|csv|log|xml|apk|exe|ttf|bmp|ico|svg|tif|tiff).*");
    }

    boolean isInRootDomain(Page referringPage, WebURL url) {
        return referringPage.getWebURL().getDomain().equals(url.getDomain());
    }


}
