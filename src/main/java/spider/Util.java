package spider;

import agnomen.Decoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static void toModule(HttpClient client) throws IOException {
        //先访问toModule并且必须消耗掉这个页面,否则无法访问必修课页面
        String toModule = "http://gsmis.graduate.buaa.edu.cn/gsmis/toModule.do?prefix=/py&page=/pySelectCourses.do?do=xsXuanKe";
        HttpResponse toModuleResp = client.execute(new HttpGet(toModule));
        EntityUtils.consume(toModuleResp.getEntity());
    }

    public static boolean login(String userId, String password, HttpClient client) throws IOException {
        //访问登录页面并破解验证码
        String login = "http://gsmis.graduate.buaa.edu.cn/gsmis/main.do";
        String img = "http://gsmis.graduate.buaa.edu.cn/gsmis/Image.do";
        HttpEntity entity = client.execute(new HttpGet(login)).getEntity();
        EntityUtils.consume(entity);
        HttpEntity imgEntity = client.execute(new HttpGet(img)).getEntity();
        String checkCode = Decoder.parse(ImageIO.read(imgEntity.getContent()));
        EntityUtils.consume(imgEntity);
        //提交表单
        String form = "http://gsmis.graduate.buaa.edu.cn/gsmis/indexAction.do";
        HttpPost formPost = new HttpPost(form);
        List<NameValuePair> formList = new ArrayList<>();
        formList.add(new BasicNameValuePair("id", userId));
        formList.add(new BasicNameValuePair("password", password));
        formList.add(new BasicNameValuePair("checkcode", checkCode));
        formPost.setEntity(new UrlEncodedFormEntity(formList));
        HttpEntity indexEntity = client.execute(formPost).getEntity();
        String indexStr = EntityUtils.toString(indexEntity);
        //根据index页面内容判断是否登陆成功
        int pos = indexStr.indexOf("当前用户");
        return pos != -1;
    }
}
