package ppkwuzad5.ppkwuzad5.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ApivCardController {
    @GetMapping("test/")
    public String test() {
        return "test1";
    }

    @GetMapping("businessdata/{pageamount}/{businesstype}")
    public String getBusinessData(@PathVariable int pageamount, @PathVariable String businesstype) {
        String url;
        Document document;
        Elements list;
        Elements element;
        Elements element2;
        int size;
        Map<String, String> map = new HashMap<>();
        try {
            for (int i = 1; i <= pageamount; i++) {
                url = "https://panoramafirm.pl/" + businesstype + "/firmy," + i + ".html";
                document = Jsoup.connect(url).get();
                list = document.select("#company-list > li");
                size = list.size();

                for (int j = 0; j < size; j++) {
                    element = list.select("li:nth-child(" + (j + 1) + ") > div.row.border-bottom.company-top-content.pb-1 > div > h2 > a:first-child");
                    element2 = list.select("li:nth-child(" + (j + 1) + ") > div.row.company-center-content > div > div > div:first-child");
                    String lines[] = element2.html().trim().split("</div>");
                    map.put(element.html(), lines[1]);
                }
            }


            ArrayList<String> vcfList = new ArrayList<String>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                vcfList.add("BEGIN:VCARD\nVERSION:3.0\nNAME:" + entry.getKey() + "\nADD:" + entry.getValue() + "\nEND:VCARD");
            }
            for (int i = 0; i < vcfList.size(); i++) {
                System.out.println(vcfList.get(i));
            }

            String page = "";
            page += "<!DOCTYPE html> <html> <body> <h1>vCard</h1><ul>";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String tmp = "BEGIN:VCARDVERSION:3.0 NAME:" + entry.getKey() + " ADD:" + entry.getValue() + " END:VCARD";
                page += "<li>" + entry.getKey() + "<button type=\"button\" onclick=\"alert('" + tmp + "')\">wygeneruj vCard</button><br>";
            }
            page += "</ul></body></html>";

            return page;
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }


}
