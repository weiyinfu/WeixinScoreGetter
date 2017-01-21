package spider;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoreGetter {
    String trim(String s) {
        int front = s.indexOf("：");
        if (front != -1) s = s.substring(front + 1);
        int back = s.indexOf('-');
        if (back != -1) s = s.substring(0, back);
        return s.replace("<td>", "").replace("</td>", "").replace("&nbsp;", "").trim();
    }

    String getScore(String username, String password, HttpClient client) throws IOException {
        Util.login(username, password, client);
        Util.toModule(client);
        String url = "http://gsmis.graduate.buaa.edu.cn/gsmis/py/pyYiXuanKeCheng.do";
        HttpGet get = new HttpGet(url);
        HttpResponse resp = client.execute(get);
        String html = EntityUtils.toString(resp.getEntity());
        Document jsoup = Jsoup.parse(html);
        Elements elements = jsoup.select(".tablefont2");
        List<CourseScore> ans = new ArrayList<>();
        for (Element tr : elements) {
            Elements tds = tr.select("td");
            if (tds.size() < 10) continue;
            CourseScore courseScore = new CourseScore();
            courseScore.setName(trim(tds.get(2).toString()));
            courseScore.setScore(trim(tds.get(4).toString()));
            courseScore.setWeight(trim(tds.get(9).toString().trim()));
            ans.add(courseScore);
        }
        return ans.stream().map(x -> {
            return x.toString();
        }).reduce((x, y) -> {
            return x + "\n" + y;
        }).get();
    }

    static ScoreGetter scoreGetter = null;

    public static String get(String username, String password, HttpClient client) {
        if (scoreGetter == null) scoreGetter = new ScoreGetter();
        try {
            return scoreGetter.getScore(username, password, client);
        } catch (Exception e) {
            return "查成绩失败";
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(ScoreGetter.get("SY1606604", "20124003", HttpClients.createDefault()));
    }
}
