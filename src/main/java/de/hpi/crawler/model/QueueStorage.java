package de.hpi.crawler.model;

import de.hpi.rabbitmqProducer.RabbitProducer;
import de.hpi.restclient.dto.CrawledPage;
import de.hpi.restclient.dto.FinishedShop;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QueueStorage implements StorageProvider {

    @Getter(AccessLevel.PRIVATE) @Setter private long shopID;
    @Getter @Setter RabbitProducer matcherQueue;
    @Getter @Setter RabbitProducer pagesQueue;
    @Getter(AccessLevel.PRIVATE) private final Logger logger = LoggerFactory.getLogger(SimpleWebCrawler.class);

    public QueueStorage(long ShopID, RabbitProducer matcherQueue, RabbitProducer pagesQueue){
        setMatcherQueue(matcherQueue);
        setPagesQueue(pagesQueue);
        setShopID(ShopID);
    }

    @Override
    public void store(Page page, long timestamp) throws Exception {
        CrawledPage pageToStore = new CrawledPage(getShopID(), timestamp, page.getWebURL().getURL(),((HtmlParseData) page.getParseData()).getHtml());
        getPagesQueue().sendMessage(pageToStore);
    }

    @Override
    public void finishedCrawling() {
        FinishedShop finishedShop = new FinishedShop(shopID);
        getLogger().info("Finished Shop: {}", shopID);
        getMatcherQueue().sendMessage(finishedShop);
    }

    private void sendFinishedShopIDtoQueue(long shopID, QueueStorage queueStorage){
        FinishedShop finishedShop = new FinishedShop(shopID);
    }




}
