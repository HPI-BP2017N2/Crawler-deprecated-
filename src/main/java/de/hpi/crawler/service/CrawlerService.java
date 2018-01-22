package de.hpi.crawler.service;


import de.hpi.crawler.dto.FinishedShop;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class CrawlerService {

    //constants

    @Getter(AccessLevel.PRIVATE) private final int REQUEST_TIMEOUT_IN_MS = 100, MAX_LINK_LEVEL_DEPTH = 20, MAX_OVERALL_LINK_COUNT = 3, NUMBER_OF_CRAWLERS = 1;
    @Getter(AccessLevel.PRIVATE) private final String TMP_FILES_DIR = "./tmp",
            USER_AGENT = "Mozilla/5.0 (compatible; HPI-BPN2-2017/2.1; https://hpi.de/naumann/teaching/bachelorprojekte/inventory-management.html)";

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private ConnectionService connectionService;


    public CrawlerService(ConnectionService aConnectionService) {
        connectionService = aConnectionService;
    }

    public CrawlerService(){
        connectionService = new ConnectionService();
    }

    public void crawlDomain(long shopID) {
        try {
            String shopRootURL = getShopRootURL(shopID);
            CrawlController controller = createDefaultCrawlController(shopRootURL);
            controller.addSeed(shopRootURL);
            StorageCrawlerFactory factory = new StorageCrawlerFactory.QueueStorageFactory(shopID);

            new Thread(() -> {
                controller.start(factory,1);
                sendFinishedShopIDtoQueue(shopID, (QueueStorage)factory.getStorageProvider());
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendFinishedShopIDtoQueue(long shopID, QueueStorage queueStorage){
        FinishedShop finishedShop = new FinishedShop();
        finishedShop.setFinishedShopID(shopID);

        try {
            queueStorage.store(finishedShop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CrawlController createDefaultCrawlController (String startUrl) throws Exception {
        CrawlConfig defaultCrawlConfig = createDefaultCrawlConfig();
        PageFetcher defaultPageFetcher = new PageFetcher(defaultCrawlConfig);
        RobotstxtServer defaultRobotsTxtServer = createDefaultRobotsTxtServer(defaultPageFetcher, startUrl);
        return new CrawlController(defaultCrawlConfig,defaultPageFetcher, defaultRobotsTxtServer);
    }
    private CrawlConfig createDefaultCrawlConfig(){
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(getTMP_FILES_DIR());
        config.setPolitenessDelay(getREQUEST_TIMEOUT_IN_MS());
        config.setMaxDepthOfCrawling(getMAX_LINK_LEVEL_DEPTH());
        config.setMaxPagesToFetch(getMAX_OVERALL_LINK_COUNT());
        config.setIncludeBinaryContentInCrawling(false);
        config.setResumableCrawling(true);
        config.setIncludeHttpsPages(true);
        config.setUserAgentString(getUSER_AGENT());
        return config;
    }
    private RobotstxtServer createDefaultRobotsTxtServer(PageFetcher fetcher, String rootUrl){
        RobotstxtConfig robotsTxtConfig = new RobotstxtConfig();
        try {
            robotsTxtConfig.setEnabled(isRobotsTxtExisting(fetcher, new URL(rootUrl)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new RobotstxtServer(robotsTxtConfig, fetcher);
    }
    private boolean isRobotsTxtExisting(PageFetcher fetcher, URL url) {
        WebURL robotsTxtUrl = new WebURL();
        String host = url.getHost().toLowerCase();
        String port = ((url.getPort() == url.getDefaultPort()) || (url.getPort() == -1)) ? "" :
                (":" + url.getPort());
        String proto = url.getProtocol();
        robotsTxtUrl.setURL(proto + "://" + host + port + "/robots.txt");
        PageFetchResult fetchResult = null;
        try {
            fetchResult = fetcher.fetchPage(robotsTxtUrl);
        } catch (InterruptedException | IOException | PageBiggerThanMaxSizeException e) {
            e.printStackTrace();
        }
        int status = fetchResult.getStatusCode();
        return status != HttpStatus.SC_NOT_FOUND;
    }

    private String getShopRootURL(long shopID){
        return getConnectionService().getURLfromShopIDfromBridge(shopID);
    }
}
