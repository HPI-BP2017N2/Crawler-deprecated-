package de.hpi.crawler.controller;

import de.hpi.crawler.dto.CrawlerConfig;
import de.hpi.crawler.service.CrawlerService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CrawlerController {


    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private CrawlerService crawlerService;

    @Autowired
    public CrawlerController(CrawlerService crawlerService) {
        setCrawlerService(crawlerService);
    }

    @RequestMapping(value = "/crawler", method = RequestMethod.POST)
    public void startCrawling(@RequestBody CrawlerConfig crawlerConfig) {

        for (String url : crawlerConfig.getUrls()) {
            getCrawlerService().crawlPage(url);
        }
    }
}

