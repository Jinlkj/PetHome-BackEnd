package edu.cqu.pethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.cqu.pethome.dto.LoginFormDTO;
import edu.cqu.pethome.dto.Result;
import edu.cqu.pethome.entities.User;
import jakarta.servlet.http.HttpSession;

import java.util.Date;

public interface UserService extends IService<User> {
    Result sendCode(String phone, HttpSession httpSession);

    Result handleLogin(LoginFormDTO loginFormDTO, HttpSession httpSession);

    User createNewUser(String phone);
}
