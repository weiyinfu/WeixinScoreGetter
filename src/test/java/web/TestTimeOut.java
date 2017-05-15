package web;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

public class TestTimeOut {
@Test
public void go() throws IOException {
   HttpClient client = MyClient.getClient();
   HttpGet get = new HttpGet("http://129.12.33.12");
   HttpResponse resp = client.execute(get);
   System.out.println(EntityUtils.toString(resp.getEntity()));
}
}
