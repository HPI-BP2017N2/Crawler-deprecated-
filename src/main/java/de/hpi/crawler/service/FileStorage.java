package de.hpi.crawler.service;

import com.google.common.annotations.VisibleForTesting;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileStorage implements StorageProvider {

    //constants
    @Getter(AccessLevel.PRIVATE) private String folderName = "crawledPages/";
    @Getter(AccessLevel.PRIVATE) private long shopID;

    public FileStorage (String aFolderName, long aShopID) {
        folderName = aFolderName;
        shopID = aShopID;
    }

    public FileStorage(long aShopID){
        shopID = aShopID;
    }

    @Override
    public void store(Page page, long timestamp) throws IOException {
        String contentPage = ((HtmlParseData) page.getParseData()).getHtml();
        String fileName = Long.toString(shopID) + "-" + getDomainFileFriendly(page.getWebURL().getURL()) + "-" + Long.toString(timestamp) + ".html";
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

    @VisibleForTesting void saveStringToFile(String stringToWrite, String pathName) throws IOException {
        File file = new File(pathName);
        File folder = file.getParentFile();

        if(!folder.exists() && !folder.mkdirs()){
            throw new IOException("Couldn't create the storage folder: " + folder.getAbsolutePath() + " does it already exist ?");
        }

        try(  PrintWriter out = new PrintWriter( pathName )  ){
            out.println( stringToWrite);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
