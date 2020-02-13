package com.graja.WD_scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.graja.WD_scrapper.scrapper.Scrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/rooms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getRooms(){
        List<HtmlElement> roomsAsHtml = Scrapper.getRoomsList();
        List<String> roomsAsString = new ArrayList<>(roomsAsHtml.size());
        for (HtmlElement room : roomsAsHtml){
            roomsAsString.add(room.getTextContent());
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(out, roomsAsString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/room/{roomNumber}/{startDate}/{endDate}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getRoomOccupancy(@PathVariable(name = "roomNumber") String roomNumber,
                                   @PathVariable(name = "startDate")LocalDate startDate,
                                   @PathVariable(name = "endDate") LocalDate endDate){
        return null;
    }
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/captcha", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getCaptcha(){
        return Scrapper.getCaptcha();
    }
    @PostMapping(value = "/captcha/{captcha}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody void postCaptcha(@PathVariable(name = "captcha") String captcha){
        Scrapper.postCaptcha(captcha);
    }
    @RequestMapping(value = "/save/{name}")
    public void saveState(@PathVariable(name = "name") String name){
        Scrapper.saveXmlToDisc(name);
    }
}
