package edu.cqu.pethome.controller;

import edu.cqu.pethome.dto.LoginFormDTO;
import edu.cqu.pethome.dto.Result;
import edu.cqu.pethome.dto.UserDTO;
import edu.cqu.pethome.service.UserService;
import edu.cqu.pethome.utils.UserHolder;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/code")
    public Result userCode(@RequestParam("phone") String phone, HttpSession httpSession){
        return userService.sendCode(phone,httpSession);
    }
    @PostMapping("/login")
    public Result userLogin(@RequestBody LoginFormDTO loginFormDTO, HttpSession httpSession){
        return userService.handleLogin(loginFormDTO,httpSession);
    }
    @GetMapping("/me")
    public Result me(){
        UserDTO user = UserHolder.getUser();
        return Result.ok(user);
    }
}
