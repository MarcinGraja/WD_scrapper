package com.graja.WD_scrapper.scrapper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class Scrapper {
    static WebClient webClient = new WebClient();
    static HtmlPage page;
    static HtmlForm form;
    static {
        try {
            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
            java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
            page = webClient.getPage("https://dziekanat.agh.edu.pl/PodzGodzinTok.aspx");
            form = page.getFormByName("aspnetForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void saveXmlToDisc(String name){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(name + ".xml"));
            writer.write(page.asXml());
            writer.close();
            page.save(new File("dude,work"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    static public List<HtmlElement> getRoomsList(){

        HtmlElement listOfRooms = page.getHtmlElementById("ctl00_ctl00_ContentPlaceHolder_RightContentPlaceHolder_ddlSala_DropDown");
        List<HtmlElement> rooms = page.getByXPath(listOfRooms.getCanonicalXPath() +"//li[@class='rcbItem ']");
        return rooms;
    }
    static public byte[] getCaptcha(){
        HtmlElement captchaParent = page.getHtmlElementById("ctl00_ctl00_ContentPlaceHolder_RightContentPlaceHolder_captcha");
        HtmlImage captcha = page.getFirstByXPath(captchaParent.getCanonicalXPath() + "//img");
        ImageReader reader;
        try {
            reader = captcha.getImageReader();
            BufferedImage img = ImageIO.read((ImageInputStream) reader.getInput());
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", bao);
            return bao.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    public static void postCaptcha(String captcha) {
        form.getInputByName("ctl00$ctl00$ContentPlaceHolder$RightContentPlaceHolder$captcha$ctl04").setValueAttribute(captcha);
    }
}
