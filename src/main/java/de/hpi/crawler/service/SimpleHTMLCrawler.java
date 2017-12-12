package de.hpi.crawler.service;

import com.google.common.annotations.VisibleForTesting;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


public class SimpleHTMLCrawler extends WebCrawler {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private StorageProvider storageProvider;

    public SimpleHTMLCrawler(StorageProvider aStorageProvider) {
        storageProvider = aStorageProvider;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        url.setURL(url.getURL().toLowerCase());
        WebURL referringPageURL = referringPage.getWebURL();
        referringPageURL.setURL(referringPageURL.getURL().toLowerCase());
        referringPage.setWebURL(referringPageURL);
        return isInRootDomain( referringPage, url) && isMIMEfiltered(url);
    }

    @Override
    public void visit(Page page){
        if(isPageContentHTML(page)) {
            storageProvider.storePage(page);
        }
    }

    @VisibleForTesting boolean isMIMEfiltered(WebURL url) {
        return !url.getURL().matches(".*\\.(jpg|png|js|css|jpeg|txt|epub|fb2|docx|doc|xls|zip|rar|pdf|gif|gz|bin|dmg|iso|csv|log|xml|apk|exe|ttf|bmp|ico|svg|tif|tiff).*");
    }

    @VisibleForTesting boolean isInRootDomain(Page referringPage, WebURL url) {
        return referringPage.getWebURL().getDomain().equals(url.getDomain());
    }

    @VisibleForTesting boolean isPageContentHTML (Page page){
        return page.getParseData() instanceof HtmlParseData;
    }




}
