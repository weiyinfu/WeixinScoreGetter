package haha;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
public class MyController {
    @ResponseBody
    @RequestMapping(value = "check", method = RequestMethod.GET)
    String get(String signature, String timestamp, String nonce, String echostr) throws Exception {
        String s = Arrays.asList(timestamp, nonce, MyClient.token).stream().sorted().collect(Collectors.joining());
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        String ans = new String(digest.digest(s.getBytes("utf8")), "utf8");
        return signature.equals(ans) ? echostr : null;
    }

    @RequestMapping(value = "check", method = RequestMethod.POST)
    String post(HttpServletRequest req, ModelMap model) throws Exception {
        Document doc = new SAXReader().read(req.getReader());
        String FromUserName = doc.selectSingleNode("//FromUserName").getText();
        String ToUserName = doc.selectSingleNode("//ToUserName").getText();
        String CreateTime = doc.selectSingleNode("//CreateTime").getText();
        String MsgType = doc.selectSingleNode("//MsgType").getText();
        String MsgId = doc.selectSingleNode("//MsgId").getText();
        model.addAttribute("ToUserName", FromUserName);
        model.addAttribute("FromUserName", ToUserName);
        model.addAttribute("CreateTime", System.currentTimeMillis());
        switch (MsgType) {
            case "text":
                model.addAttribute("MsgType", "text");
                String Content = doc.selectSingleNode("//Content").getText();
                model.addAttribute("Content", reply(Content, FromUserName));
                return "text";
        }
        return "";
    }

    String reply(String q, String openid) {
        if (q.matches("[a-zA-Z]{2}[0-9]{7}[,. +-，。 ].{3,15}")) {
            String username = q.substring(0, 9).toUpperCase();
            String password = q.substring(10);
            User u = new User();
            u.setUsername(username);
            u.setOpenid(openid);
            u.setPassword(password);
            if (MyClient.valid(username, password)) {
                MyClient.users.put(openid, u);
                UserData.save();
                return MyClient.getResult(openid);
            } else {
                return "您的用户名或者密码有误，请重新输入。输入格式为：“sy1606604,12345”";
            }
        } else {
            if (MyClient.users.get(openid) == null)
                return "您还没有绑定学号密码，请输入类似“sy1606604,xxxxxx”格式的教务处学号密码进行绑定";
            else
                return MyClient.getResult(openid);
        }
    }

    public static void main(String[] args) {
        System.out.println(new MyController().reply("sy1606604,xxxx", "weidiao"));
    }
}
