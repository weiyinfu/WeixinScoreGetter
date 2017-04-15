package spider;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyllabusGetter {
    //解析html,返回已经选了的课的列表
    List<CourseClass> parse(String html) {
        List<CourseClass> ans = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        for (Element i : doc.select("input[checked]")) {
            Element tr = i.parent().parent();
            Elements tds = tr.select("td");
            String timeAddresses[] = tds.get(1).text().split(" ");
            String name = tds.get(4).text();
            String score = tds.get(7).text();
            Course course = new Course();
            course.setName(name.substring(0, name.indexOf("--")));
            course.setScore(Double.parseDouble(score));
            List<CourseClass> list = new ArrayList<>();
            for (String timeAddress : timeAddresses) {
                if (timeAddress.length() == 0) continue;
                List<CourseClass> ll = parseTimeAddress(timeAddress);
                for (CourseClass cc : ll) {
                    cc.setCourse(course);
                    list.add(cc);
                }
            }
            course.setCourseClassList(list);
            courses.add(course);
        }
        for (Course i : courses) {
            for (CourseClass j : i.getCourseClassList())
                ans.add(j);
        }
        return ans;
    }

    //因为有些课占用好多节课,所以应该返回一个List<CourseClass>而不是CourseClass
    List<CourseClass> parseTimeAddress(String s) {
        List<CourseClass> ans = new ArrayList<>();
        String ss[] = s.split(",");
        int week = ss[0].charAt(1) - '0';
        Matcher m = Pattern.compile("\\d*~\\d*").matcher(ss[0]);
        m.find();
        String time[] = m.group().split("~");
        int start = Integer.parseInt(time[0]), end = Integer.parseInt(time[1]);
        String address = ss[1].substring(5, ss[1].length() - 1);
        CourseClass courseClass = new CourseClass();
        courseClass.setTime(start / 2 + 1);
        courseClass.setWeek(week);
        courseClass.setAddress(address);
        ans.add(courseClass);
        if (end - start == 3) {
            CourseClass courseClass1 = new CourseClass();
            courseClass1.setTime(start / 2 + 2);
            courseClass1.setWeek(week);
            courseClass1.setAddress(address);
            ans.add(courseClass1);
        }
        return ans;
    }

    void sortAndShow(List<CourseClass> courseClassList) {
        courseClassList.sort(new Comparator<CourseClass>() {
            @Override
            public int compare(CourseClass o1, CourseClass o2) {
                return o1.getWeek() * 10 + o1.getTime() - o2.getWeek() * 10 - o2.getTime();
            }
        });
        courseClassList.forEach(i -> {
            System.out.printf("周%d第%d节在%s上%s\n", i.getWeek(), i.getTime(), i.getAddress(), i.getCourse().getName());
        });
    }

    List<CourseClass> getSyllabus(String userId, String password, HttpClient client) throws IOException {
        Util.login(userId, password, client);
        Util.toModule(client);
        //定义一个courseClassList用于存放课程,下面访问多个页面下的课程
        List<CourseClass> courseClassList = new ArrayList<>();
        //访问必修课页面并用jsoup进行解析
        String bixiu = "http://gsmis.graduate.buaa.edu.cn/gsmis/py/pySelectCourses.do?do=xuanBiXiuKe";
        HttpEntity bixiuEntity = client.execute(new HttpGet(bixiu)).getEntity();
        String bixiuHtml = EntityUtils.toString(bixiuEntity);
        System.out.println(bixiuHtml + "================================");
        courseClassList.addAll(parse(bixiuHtml));
        //访问实验类和专题类课程页面并用jsoup解析
        String zhuanti = "http://gsmis.graduate.buaa.edu.cn/gsmis/py/pySylJsAction.do";
        //实验类和专题类课程有如下三种,分别发起一次post请求
        for (String zhuantiType : "001900 001700 000900".split(" ")) {
            List<NameValuePair> zhuantiForm = new ArrayList<>();
            zhuantiForm.add(new BasicNameValuePair("sydl", zhuantiType));
            HttpPost zhuantiPost = new HttpPost(zhuanti);
            zhuantiPost.setEntity(new UrlEncodedFormEntity(zhuantiForm));
            HttpEntity zhuantiEntity = client.execute(zhuantiPost).getEntity();
            String zhuantiHtml = EntityUtils.toString(zhuantiEntity);
            courseClassList.addAll(parse(zhuantiHtml));
        }
        sortAndShow(courseClassList);
        return courseClassList;
    }

    static SyllabusGetter syllabusGetter;

    public static List<CourseClass> get(String username, String password, HttpClient client) {
        if (syllabusGetter == null) syllabusGetter = new SyllabusGetter();
        try {
            return syllabusGetter.getSyllabus(username, password, client);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        String s = JSON.toJSONString(SyllabusGetter.get("xxxxx", "xxxx", HttpClients.createDefault()));
        System.out.println(s);
    }
}
