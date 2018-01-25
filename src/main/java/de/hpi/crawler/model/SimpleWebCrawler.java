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

import java.util.Set;


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
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        int parentDocid = page.getWebURL().getParentDocid();

        getLogger().debug("Docid: {}", docid);
        getLogger().info("URL: {}", url);
        getLogger().debug("Docid of parent page: {}", parentDocid);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            getLogger().debug("Text length: {}", text.length());
            getLogger().debug("Html length: {}", html.length());
            //getLogger().debug("Number of outgoing links: {}", links.size());

            try {
                storageProvider.store(page, System.currentTimeMillis());
            } catch (Exception e) {
                logger.error("Storing failed", e);
            }
        }

        getLogger().debug("=============");
       // if(isPageContentHTML(page)) {
      //      storageProvider.store(page);
      //  }
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
