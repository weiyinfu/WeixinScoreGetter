package web;

public class User {
String openid;
String username;
String password;
String result;


long lastUpdateTime;//上次更新数据时间
long lastAccessTime;//上次访问时间

public User() {

}

public User(String openid, String username, String password) {
   this.openid = openid;
   this.username = username;
   this.password = password;
}

public long getLastAccessTime() {
   return lastAccessTime;
}

public void setLastAccessTime(long lastAccessTime) {
   this.lastAccessTime = lastAccessTime;
}

public long getLastUpdateTime() {
   return lastUpdateTime;
}

public void setLastUpdateTime(long lastUpdateTime) {
   this.lastUpdateTime = lastUpdateTime;
}

public String getOpenid() {
   return openid;
}

public void setOpenid(String openid) {
   this.openid = openid;
}

public String getUsername() {
   return username;
}

public void setUsername(String username) {
   this.username = username;
}

public String getPassword() {
   return password;
}

public void setPassword(String password) {
   this.password = password;
}

public String getResult() {
   return result;
}

public void setResult(String result) {
   this.result = result;
}
}
