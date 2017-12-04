package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SimpleHTMLCrawler extends WebCrawler {
    String folderName = "crawledPages/";


    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        url.setURL(url.getURL().toLowerCase());
        WebURL referringPageURL = referringPage.getWebURL();
        referringPageURL.setURL(referringPageURL.getURL().toLowerCase());
        referringPage.setWebURL(referringPageURL);
        return isInRootDomain( referringPage, url) && isHTMLPage(url);
    }

    @Override
    public void visit(Page page){
        if(isPageContentHTML(page)) {
            saveHTMLContentOfPage(page);
        }
    }

    public void saveHTMLContentOfPage(Page page) {
        String contentPage = ((HtmlParseData) page.getParseData()).getHtml();

        String fileName = convertDomainNameFileFriendly(page.getWebURL().getDomain()) +"-"+ new Date().getTime() + ".html";
        String pathName =  folderName + fileName;

        saveStringToFile(contentPage, pathName);
    }

    public String convertDomainNameFileFriendly(String domainName){
        Pattern pattern = Pattern.compile("^(?:https?:\\/\\/)?(?:[^@\\/\\n]+@)?(?:www\\.)?([^:\\/\\n]+)");
        Matcher matcher = pattern.matcher(domainName);
        return matcher.group(1);
    }
    void saveStringToFile(String stringToWrite, String pathName) {
        File file = new File(pathName);
        file.getParentFile().mkdirs();

        try(  PrintWriter out = new PrintWriter( pathName )  ){
            out.println( stringToWrite);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    boolean isHTMLPage(WebURL url) {
        return !url.getURL().matches(".*\\.(jpg|png|js|css|jpeg|txt|epub|fb2|docx|doc|xls|zip|rar|pdf|gif|gz|bin|dmg|iso|csv|log|xml|apk|exe|ttf|bmp|ico|svg|tif|tiff).*");
    }

    boolean isInRootDomain(Page referringPage, WebURL url) {
        return referringPage.getWebURL().getDomain().equals(url.getDomain());
    }

    boolean isPageContentHTML (Page page){
        return page.getParseData() instanceof HtmlParseData;
    }




}
