package web;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MyControllerTests {
@Resource
MyController myController;

@Test
public void go() {
   String ans = myController.reply("sy1606604 20124003", "weidiao");
   System.out.println(ans);
}

@Autowired
UserData userData;

@Test
public void userDataNotNull() {
   System.out.println(userData.userData+"==");
}
}
