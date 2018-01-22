package de.hpi.crawler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.hpi.crawler.dto.CrawledPage;
import de.hpi.crawler.dto.FinishedShop;
import de.hpi.crawler.model.JsonConverter;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import lombok.AccessLevel;
import lombok.Getter;

public class QueueStorage implements StorageProvider {

    @Getter(AccessLevel.PRIVATE) private long shopID;

    public QueueStorage(long aShopID){
        shopID = aShopID;
    }

    @Override
    public void store(Page page, long timestamp) throws Exception {
        String jsonOfPage = serializeToJSON(page, getShopID(), timestamp);
        sendToQueue(jsonOfPage, "htmlPagesParser" );
    }

    public void store(FinishedShop finishedShop) throws Exception{
        sendToQueue(serializeToJSON(finishedShop), "finishedShopsMatcher" );
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

    @VisibleForTesting void sendToQueue(String json, String queueName){
        ConnectionService connection = new ConnectionService();
        connection.sendStringToQueue(json, queueName);
    }


}
