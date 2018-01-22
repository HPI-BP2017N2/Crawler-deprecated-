package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.mockito.verification.VerificationMode;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class QueueStorageTest {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private SimpleWebCrawler crawler;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Page referringPage;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private WebURL url;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private QueueStorage queueStorage;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private long shopID = 1234;

    @Test
    public void store() {
        this.setQueueStorage(spy(new QueueStorage(shopID)));

        savedPage(shopID,"bla","http://www.google.de/");
        savedPage(shopID, "blub","https://www.google.in.co/123/test");
        savedPage(shopID,RandomStringUtils.randomAlphabetic(10),"https://www.google.in.co/123/test");
        savedPage(shopID,RandomStringUtils.randomAlphabetic(10),"https://google.in/123/test");

        savedPage(shopID, RandomStringUtils.randomAlphabetic(10),"https://www.:google.in.co/123/test");
    }

    private void savedPage(long shopID, String html, String url){
        try {
            TestTools testTools = new TestTools();
            Page testPage = testTools.constructTestPage(url, html);
            getQueueStorage().store(testPage);
            VerificationMode mode = times(1);
            verify(getQueueStorage(), mode).sendToQueue(constructTestJSON(shopID, html, url));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String constructTestJSON (long shopID, String html, String url){
        String prototype = "{\n" +
                "  \"shopID\" : %1$s,\n" +
                "  \"url\" : \"%2$s\",\n" +
                "  \"htmlSource\" : \"%3$s\"\n" +
                "}";
        prototype = String.format(prototype, shopID,url,html);
        return prototype;
    }
}