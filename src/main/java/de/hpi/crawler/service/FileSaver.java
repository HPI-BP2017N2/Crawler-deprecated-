package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSaver implements StorageProvider {

    //constants
    @Getter(AccessLevel.PRIVATE) private static final String folderName = "crawledPages/";

    @Override
    public void storePage(Page page) {
        String contentPage = ((HtmlParseData) page.getParseData()).getHtml();
        String fileName = getDomainFileFriendly(page.getWebURL().getURL()) + "-" + Long.toString(System.currentTimeMillis()) + ".html";
        String pathName =  folderName + fileName;
        saveStringToFile(contentPage, pathName);
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

    private void saveStringToFile(String stringToWrite, String pathName) {
        File file = new File(pathName);
        file.getParentFile().mkdirs();

        try(  PrintWriter out = new PrintWriter( pathName )  ){
            out.println( stringToWrite);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
