package web;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weixin")
public class Config {
String appId;
String appsecret;
String token;


public String getAppId() {
   return appId;
}

public void setAppId(String appId) {
   this.appId = appId;
}

public String getAppsecret() {
   return appsecret;
}

public void setAppsecret(String appsecret) {
   this.appsecret = appsecret;
}

public String getToken() {
   return token;
}

public void setToken(String token) {
   this.token = token;
}

}
