package de.hpi.crawler.model;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


public class StorageCrawlerFactory implements CrawlController.WebCrawlerFactory{

    @Getter @Setter(AccessLevel.PRIVATE) private StorageProvider storageProvider;

    public StorageCrawlerFactory(StorageProvider aStorageProvider){
        storageProvider = aStorageProvider;
    }

    @Override
    public WebCrawler newInstance(){
        return new SimpleWebCrawler(storageProvider);
    }


}
