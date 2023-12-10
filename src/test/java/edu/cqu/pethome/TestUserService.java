package edu.cqu.pethome;

import edu.cqu.pethome.entities.User;
import edu.cqu.pethome.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
@Slf4j
public class TestUserService {
    @Autowired
    UserService userService;
    @Test
    public void testGetUser(){
        User user = userService.getById(1);
        Assertions.assertEquals("小鱼同学",user.getNickName());
        log.info(user.getNickName());
    }
}
