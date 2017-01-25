package haha;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    //final static String appId = "wx1d9db42ae638e914";//测试号
    final static String appId = "wxa23c02eaac832dc4";
    //final static String appsecret = "7f832387735fb2fc78498c01741e2741";//测试号
    final static String appsecret = "ae19543d27cffba0d7b9239e18a24786";
    public final static String token = "20124003";

    final static Path userData = Paths.get(System.getProperty("user.home")).resolve("wxuser2.txt");

}
