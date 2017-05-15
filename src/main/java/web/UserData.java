package web;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
class UserData {
Logger logger = Logger.getLogger(this.getClass());

@Value("${userData}")
String userData;
ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

@PostConstruct
void load() {
   Path p = Paths.get(userData);
   if (Files.exists(p)) {
      try {
         JSON.parseArray(Files.readAllLines(p).stream().collect(Collectors.joining()), User.class).forEach((x) -> {
            users.put(x.getOpenid(), x);
         });
      } catch (IOException e) {
         logger.error("", e);
      }
   }
}

@PreDestroy
synchronized void save() {
   try (PrintWriter cout = new PrintWriter(
           Files.newBufferedWriter(Paths.get(userData)))
   ) {
      String json = JSON.toJSONString(users.values(), true);
      cout.write(json);
   } catch (IOException e) {
      logger.error("", e);
   }
}
}
