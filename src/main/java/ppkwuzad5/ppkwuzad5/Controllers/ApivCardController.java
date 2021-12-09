package ppkwuzad5.ppkwuzad5.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;


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
                    element = list.select("li:nth-child("+ (j + 1) +") > div.row.border-bottom.company-top-content.pb-1 > div > h2 > a:first-child");
                    element2 = list.select("li:nth-child("+ (j + 1) +") > div.row.company-center-content > div > div > div:first-child");
                    String lines[] = element2.html().trim().split("</div>");
                    map.put(element.html(), lines[1]);
                }
            }

            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey()+" : "+entry.getValue());

            }

            return map.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error";
    }
}
