package de.hpi.crawler.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.hpi.crawler.dto.CrawledPage;
import de.hpi.crawler.dto.FinishedShop;
import de.hpi.rabbitmqProducer.RabbitProducer;
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
        String jsonOfPage = serializeToJSON(page, getShopID(), timestamp);
        getPagesQueue().sendMessage(jsonOfPage);
    }

    @Override
    public void finishedCrawling() {
        FinishedShop finishedShop = new FinishedShop();

        finishedShop.setFinishedShopID(getShopID());
        getLogger().info("Finished Shop: {}", shopID);
        getMatcherQueue().sendMessage(Long.toString(shopID)); //TODO change to send object
    }

    private void sendFinishedShopIDtoQueue(long shopID, QueueStorage queueStorage){
        FinishedShop finishedShop = new FinishedShop();
        finishedShop.setFinishedShopID(shopID);
    }

    private String serializeToJSON(Page page, long shopID, long timestamp) throws JsonProcessingException {
        CrawledPage pageToStore = new CrawledPage();
        pageToStore.setShopID(shopID);
        pageToStore.setTimestamp(timestamp);
        pageToStore.setHtmlSource(((HtmlParseData) page.getParseData()).getHtml());
        pageToStore.setUrl(page.getWebURL().getURL());
        return serializeToJSON(pageToStore);
    }

    private String serializeToJSON (Object object) throws JsonProcessingException {
        return JsonConverter.getJsonStringForJavaObject(object);
    }


}
