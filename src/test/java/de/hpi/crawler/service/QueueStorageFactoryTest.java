package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class QueueStorageFactoryTest {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private SimpleWebCrawler crawler;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Page referringPage;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private WebURL url;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private QueueStorage queueStorage;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private long shopID = 1234;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private long timestamp;

    @Test
    public void store() {
        this.setQueueStorage(spy(new QueueStorage(shopID)));
        setTimestamp(System.currentTimeMillis());

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
            getQueueStorage().store(testPage,getTimestamp() );
            VerificationMode mode = times(1);
            verify(getQueueStorage(), mode).sendToQueue(constructTestJSON(shopID, getTimestamp(), url, html), "htmlPagesParser" );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String constructTestJSON(long shopID, long timestamp, String url, String html){
        String prototype = "{\n" +
                "  \"shopID\" : %1$s,\n" +
                "  \"timestamp\" : %2$s,\n" +
                "  \"url\" : \"%3$s\",\n" +
                "  \"htmlSource\" : \"%4$s\"\n" +
                "}";
        prototype = String.format(prototype, shopID, timestamp,url,html);
        return prototype;
    }
}