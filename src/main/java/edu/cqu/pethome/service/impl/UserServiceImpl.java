package edu.cqu.pethome.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cqu.pethome.dto.LoginFormDTO;
import edu.cqu.pethome.dto.Result;
import edu.cqu.pethome.dto.UserDTO;
import edu.cqu.pethome.entities.User;
import edu.cqu.pethome.service.UserService;
import edu.cqu.pethome.utils.RegexUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<BaseMapper<User>,User> implements UserService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public Result sendCode(String phone, HttpSession httpSession) {
        // 检验手机号码是否正确
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 手机号错误，返回错误信息
            return Result.err("无效的手机号码");
        }
        // 手机号正确
        // 生成随机验证码
        String code = RandomUtil.randomNumbers(6);

        // 在redis存储验证码
        stringRedisTemplate.opsForValue().set(phone,code);

        // 在session中存储随机验证码
        httpSession.setAttribute("code",code);

        // 发送验证码
        log.info(phone+"的验证码为："+code);
        return Result.ok();
    }

    @Override
    public Result handleLogin(LoginFormDTO loginFormDTO, HttpSession httpSession) {
        // 检验手机号码是否正确
        if (RegexUtils.isPhoneInvalid(loginFormDTO.getPhone())||stringRedisTemplate.opsForValue().get(loginFormDTO.getPhone())==null) {
            // 手机号错误，返回错误信息
            return Result.err("无效的手机号码");
        }
        // 从redis拿到验证码

        String code = stringRedisTemplate.opsForValue().get(loginFormDTO.getPhone());
        // 检验验证码
        if (!code.equals(loginFormDTO.getCode()) || code==null) {
            // 错误直接返回
            return Result.err("验证码错误");
        }
        // 用户不存在，新建用户
        User user = query().eq("phone", loginFormDTO.getPhone()).one();
        if (user==null) {
            user = createNewUser(loginFormDTO.getPhone());
        }
        String token = UUID.randomUUID().toString(true);
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        // 将User对象转为HashMap存储
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(token,userMap);
        stringRedisTemplate.expire(token,30, TimeUnit.MINUTES);
        // 用户存在，返回登陆信息
        return Result.ok(token);
    }

    @Override
    public User createNewUser(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setNickName("新用户"+RandomUtil.randomNumbers(4));
        save(user);
        return user;
    }

}
