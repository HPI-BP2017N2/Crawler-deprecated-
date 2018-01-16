package de.hpi.crawler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import de.hpi.crawler.model.JsonConverter;
import de.hpi.crawler.dto.CrawledPage;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import lombok.AccessLevel;
import lombok.Getter;

public class QueueStorage implements StorageProvider {

    @Getter(AccessLevel.PRIVATE) private long shopID = 1234;

    public QueueStorage(long aShopID){
        shopID = aShopID;
    }

    @Override
    public void store(Page page) throws Exception {
        String jsonOfPage = serializeToJSON(page, getShopID());
        sendToQueue(jsonOfPage);
    }

    private String serializeToJSON(Page page, long shopID) throws JsonProcessingException {
        CrawledPage pageToStore = new CrawledPage();
        pageToStore.setShopID(shopID);
        pageToStore.setHtmlSource(((HtmlParseData) page.getParseData()).getHtml());
        pageToStore.setUrl(page.getWebURL().getURL());

        return serializeToJSON(pageToStore);
    }

    private String serializeToJSON (CrawledPage page) throws JsonProcessingException {
        return JsonConverter.getJsonStringForJavaObject(page);
    }

    @VisibleForTesting void sendToQueue(String json){
        ConnectionService connection = new ConnectionService();
        connection.sendStringToQueue(json);
    }


}
