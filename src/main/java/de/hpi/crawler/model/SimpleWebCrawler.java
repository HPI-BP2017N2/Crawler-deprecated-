package de.hpi.crawler.model;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleWebCrawler extends WebCrawler {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private StorageProvider storageProvider;
    @Getter(AccessLevel.PRIVATE) private final Logger logger = LoggerFactory.getLogger(SimpleWebCrawler.class);

    public SimpleWebCrawler(StorageProvider aStorageProvider) {
        storageProvider = aStorageProvider;
    }

    public SimpleWebCrawler(){}

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
            String url = page.getWebURL().getURL();
            getLogger().info("URL: {}", url);
            try {
                storageProvider.store(page, System.currentTimeMillis());
            } catch (Exception e) {
                logger.error("Storing failed", e);
            }
        }

        //getLogger().debug("Docid: {}", docid);
        //getLogger().info("URL: {}", url);
        //getLogger().debug("Docid of parent page: {}", parentDocid);

    }

    private boolean isMIMEfiltered(WebURL url) {
        return !url.getURL().matches(".*\\.(jpg|png|js|css|jpeg|txt|epub|fb2|docx|doc|xls|zip|rar|pdf|gif|gz|bin|dmg|iso|csv|log|xml|apk|exe|ttf|bmp|ico|svg|tif|tiff).*");
    }

    private boolean isInRootDomain(Page referringPage, WebURL url) {
        return referringPage.getWebURL().getDomain().equals(url.getDomain());
    }

    private boolean isPageContentHTML (Page page){
        return page.getParseData() instanceof HtmlParseData;
    }




}
