package de.hpi.crawler.service;

import de.hpi.crawler.model.Crawler;
import de.hpi.crawler.model.QueueStorage;
import de.hpi.crawler.model.StorageProvider;
import de.hpi.rabbitmqProducer.RabbitProducer;
import de.hpi.restclient.clients.BPBridgeClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrawlerService {

    @Getter
    @Setter
    BPBridgeClient bpBridgeClient;

    @Getter
    @Setter
    Crawler shopCrawler;

    @Getter
    @Setter
    RabbitProducer matcherQueue;

    @Getter
    @Setter
    RabbitProducer pagesQueue;



    @Autowired
    public CrawlerService(BPBridgeClient bpBridgeClient) {
        setMatcherQueue(new RabbitProducer("crawler","DoneMessagesToMatch"));
        setPagesQueue(new RabbitProducer("crawler","HtmlPagesToParse"));
        setBpBridgeClient(bpBridgeClient);
        setShopCrawler(new Crawler());
    }

    public void crawlDomain(long shopID) {
        String shopRootURL = bpBridgeClient.shopIDToUrl(shopID).getShopUrl();
        //String shopRootURL = "https://www.saturn.de";
        //StorageProvider storageProvider = new FileStorage(shopID);
        StorageProvider storageProvider = new QueueStorage(shopID, getMatcherQueue(), getPagesQueue());
        getShopCrawler().startCrawling(shopRootURL, storageProvider);
    }
}
