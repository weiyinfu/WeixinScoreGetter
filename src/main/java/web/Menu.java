package web;

import java.io.IOException;

public class Menu {
    static void createMenu() throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create";
        String json = Util.tos(Menu.class.getResourceAsStream("/menu.json"));
        MyClient.send(url, json);
    }

    static void getMenu() throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/get";
        MyClient.get(url);
    }

    static void deleteMenu() throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/delete";
        MyClient.get(url);
    }
}
