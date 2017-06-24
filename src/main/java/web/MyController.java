package web;

import com.alibaba.fastjson.JSON;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MyController {
Logger logger = Logger.getLogger(this.getClass());
@Resource
Config config;
@Resource
UserData userData;
@Resource
ScoreQuery scoreQuery;

@ResponseBody
@RequestMapping(value = "check", method = RequestMethod.GET)
String get(String signature, String timestamp, String nonce, String echostr) throws Exception {
   return signature.equals(
           Util.toHexString(
                   MessageDigest.getInstance("SHA1")
                           .digest(Stream.of(timestamp, nonce, config.token).sorted()
                                   .collect(Collectors.joining())
                                   .getBytes("utf8")))
   ) ? echostr : null;
}

@RequestMapping(value = "check", method = RequestMethod.POST)
String post(HttpServletRequest req, ModelMap model) throws Exception {
   Document doc = new SAXReader().read(req.getReader());
   String FromUserName = doc.selectSingleNode("//FromUserName").getText();
   String ToUserName = doc.selectSingleNode("//ToUserName").getText();
   String MsgType = doc.selectSingleNode("//MsgType").getText();
   model.addAttribute("ToUserName", FromUserName);
   model.addAttribute("FromUserName", ToUserName);
   model.addAttribute("CreateTime", System.currentTimeMillis());
   switch (MsgType) {
      case "text":
         model.addAttribute("MsgType", "text");
         String Content = doc.selectSingleNode("//Content").getText();
         model.addAttribute("Content", reply(Content, FromUserName));
         return "text";
      case "event": {
         model.addAttribute("MsgType", "text");
         model.addAttribute("Content", "欢迎使用北航查成绩，绑定学号密码就可以查成绩了！格式为‘sy1606604,123456’。\n本公众号目的在于服务大众，不会窃取用户信息。");
         return "text";
      }
      default:
         model.addAttribute("MsgType", "text");
         String content = "我只懂文字，不懂别的";
         model.addAttribute("Content", content);
         return "text";
   }
}

@RequestMapping(value = "test")
@ResponseBody
String test(String username, String password) {
   logger.info(username + " " + password);
   String ans = reply(username + " " + password, "weidiao");
   return ans;
}

@RequestMapping(value = "/wyf", produces = "text/json")
@ResponseBody
String manager(String password) {
   if (password.equals("wyf")) {
      return JSON.toJSONString(userData.users, true) + "===========\n" + userData.users.size();
   } else {
      return "do you have the password?";
   }
}

String reply(String q, String openid) {
   if (q.matches("[a-zA-Z]{2}[0-9]{7}[^\\w]\\w{3,15}")) {
      String username = q.substring(0, 9).toUpperCase();
      String password = q.substring(10);
      User u = new User(openid, username, password);
      if (scoreQuery.valid(username, password)) {
         userData.users.put(openid, u);
         userData.save();
         return getScore(openid, true);
      } else {
         return "您的用户名或者密码有误，请重新输入。输入格式为：“sy1606604 xxxxx”";
      }
   } else {
      if (userData.users.get(openid) == null)
         return "您还没有绑定学号密码，请输入类似“sy1606604 xxxxxx”格式的教务处学号密码进行绑定";
      else
         return getScore(openid, false);
   }
}

String getScore(String openid, boolean forceUpdate) {
   try {
      logger.info("getScore is running");
      String ans = scoreQuery.getScore(openid, forceUpdate);
      logger.info("getScore over");
      return ans;
   } catch (Exception e) {
      logger.error("", e);
   }
   return "查成绩失败，可能原因如下：\n* 你更改了密码\n* 现在外网无法访问教务处网站";
}
}
