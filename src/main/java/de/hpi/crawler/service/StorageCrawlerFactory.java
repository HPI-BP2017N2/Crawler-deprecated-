package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


public class StorageCrawlerFactory implements CrawlController.WebCrawlerFactory{

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private StorageProvider storageProvider;

    public StorageCrawlerFactory(StorageProvider aStorageProvider){
        storageProvider = aStorageProvider;
    }

    @Override
    public WebCrawler newInstance(){
        return new SimpleWebCrawler(storageProvider);
    }


    public static class FileStorage extends StorageCrawlerFactory{
        public FileStorage(String folderName){
            super(new de.hpi.crawler.service.FileStorage(folderName));
        }
        public FileStorage(){
            super(new de.hpi.crawler.service.FileStorage("crawledPages/"));
        }
    }


}
