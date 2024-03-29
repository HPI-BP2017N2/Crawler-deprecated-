package de.hpi.crawler.model;


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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Crawler {

    //constants

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private int REQUEST_TIMEOUT_IN_MS = 15000;
    @Getter(AccessLevel.PRIVATE)  private final int MAX_LINK_LEVEL_DEPTH = 20, MAX_OVERALL_LINK_COUNT = -1, NUMBER_OF_CRAWLERS = 1;
    @Getter(AccessLevel.PRIVATE) private final String TMP_FILES_DIR = "./tmp",
            USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36";
            //USER_AGENT = "Mozilla/5.0 (compatible; HPI-BPN2-2017/2.1; https://hpi.de/naumann/teaching/bachelorprojekte/inventory-management.html)";



    public Crawler(int timeoutInMS){
        setREQUEST_TIMEOUT_IN_MS(timeoutInMS);
    }

    public Crawler(){}

    public void startCrawling(String rootUrl, StorageProvider storageProvider) {
        try {
            CrawlController controller = createDefaultCrawlController(rootUrl);
            controller.addSeed(rootUrl);
            StorageCrawlerFactory factory = new StorageCrawlerFactory(storageProvider);

            new Thread(() -> {
                controller.start(factory,1);
                storageProvider.finishedCrawling();
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CrawlController createDefaultCrawlController (String startUrl) throws Exception {
        CrawlConfig defaultCrawlConfig = createDefaultCrawlConfig(startUrl);
        PageFetcher defaultPageFetcher = new PageFetcher(defaultCrawlConfig);
        RobotstxtServer defaultRobotsTxtServer = createDefaultRobotsTxtServer(defaultPageFetcher, startUrl);
        return new CrawlController(defaultCrawlConfig,defaultPageFetcher, defaultRobotsTxtServer);
    }
    private CrawlConfig createDefaultCrawlConfig(String startUrl){
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(getTMP_FILES_DIR() + "/" + getDomainFileFriendly(startUrl));
        config.setPolitenessDelay(getREQUEST_TIMEOUT_IN_MS());
        config.setMaxDepthOfCrawling(getMAX_LINK_LEVEL_DEPTH());
        config.setMaxPagesToFetch(getMAX_OVERALL_LINK_COUNT());
        config.setIncludeBinaryContentInCrawling(false);
        config.setResumableCrawling(false);
        config.setIncludeHttpsPages(true);
        config.setUserAgentString(getUSER_AGENT());
        return config;
    }

    private String getDomainFileFriendly(String url){

        Pattern pattern = Pattern.compile("^(?:https?://)?(?:[^@/\\n]+@)?(?:www\\.)?([^:/\\n]+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find())
        {
            return matcher.group(1).replaceAll("\\.","_");
        }
        else
            return "";
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

}
