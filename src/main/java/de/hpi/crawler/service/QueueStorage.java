package de.hpi.crawler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.hpi.crawler.model.JsonConverter;
import de.hpi.crawler.service.model.data.CrawledPage;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public class QueueStorage implements StorageProvider {
    @Override
    public void store(Page page) throws Exception {
        String jsonOfPage = serializeToJSON(page);
        sendToQueue(jsonOfPage);
    }

    private String serializeToJSON(Page page) throws JsonProcessingException {
        CrawledPage pageToStore = new CrawledPage();
        pageToStore.setShopID(0); //TODO set the right ShopID
        pageToStore.setHtmlSource(((HtmlParseData) page.getParseData()).getHtml());
        pageToStore.setUrl(page.getWebURL().getURL());

        return serializeToJSON(pageToStore);
    }

    private String serializeToJSON (CrawledPage page) throws JsonProcessingException {
        return JsonConverter.getJsonStringForJavaObject(page);
    }

    private void sendToQueue(String json){
        //TODO: Implement
    }

    private void initializeConnectionToQueue(){
        //TODO: Implement
    }
}
