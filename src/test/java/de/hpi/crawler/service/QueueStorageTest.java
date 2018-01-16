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

    }

    @Test
    public void storePage() {


        savedPage(shopID,"bla","http://www.google.de/", true );
        savedPage(shopID, "blub","https://www.google.in.co/123/test", true );
        savedPage(shopID,RandomStringUtils.randomAlphabetic(10),"https://www.google.in.co/123/test", true );
        savedPage(shopID,RandomStringUtils.randomAlphabetic(10),"https://google.in/123/test", true );

        savedPage(shopID, RandomStringUtils.randomAlphabetic(10),"https://www.:google.in.co/123/test", true );
    }

    private void savedPage(long shopID, String html, String url, Boolean valid ){
        try {
            TestTools testTools = new TestTools();
            this.getQueueStorage().store(testTools.constructTestPage(url, html));
            VerificationMode mode = valid ? times(1): never(); //TODO: see if this is still valid
            verify(this.getQueueStorage(), mode).sendToQueue(constructTestJSON(shopID, html, url));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String constructTestJSON (long shopID, String url, String html){
        String prototype = "{\n" +
                "  \"shopID\": \"{1}\",\n" +
                "  \"url\": \"{2}\",\n" +
                "  \"htmlSource\": \"{3}\"\n" +
                "}";
        String.format(prototype, shopID,url,html);
        return prototype;
    }

    //TODO: Fix errors ocurring at Test
}