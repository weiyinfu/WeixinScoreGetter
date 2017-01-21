package agnomen;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 图片下载器，从网站上下载一堆图片
 */
public class ImageDownloader {
    static HttpClient client = HttpClients.createDefault();

    public static void main(String[] args) throws IOException {
        HttpGet get = new HttpGet("http://gsmis.graduate.buaa.edu.cn/gsmis/Image.do");
        Path folder = Paths.get("src/main/resources/checkcodes");
        if (Files.exists(folder) == false) {
            Files.createDirectory(folder);
        }
        for (int i = 0; i < 10; i++) {
            OutputStream cout = Files.newOutputStream(folder.resolve(i + ".jpg"));
            client.execute(get).getEntity().writeTo(cout);
            cout.close();
        }
    }
}