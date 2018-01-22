package de.hpi.crawler.service;

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


    public static class FileStorageFactory extends StorageCrawlerFactory{
        public FileStorageFactory(String folderName, long shopID){
            super(new de.hpi.crawler.service.FileStorage(folderName, shopID));
        }
        public FileStorageFactory(long shopID){
            super(new de.hpi.crawler.service.FileStorage("crawledPages/", shopID));
        }
    }

     public static class QueueStorageFactory extends StorageCrawlerFactory{
        public QueueStorageFactory(long shopID){
            super(new de.hpi.crawler.service.QueueStorage(shopID));
        }
    }


}
