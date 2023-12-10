package edu.cqu.pethome.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cqu.pethome.dto.Result;
import edu.cqu.pethome.entities.Hospital;
import edu.cqu.pethome.service.HospitalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static edu.cqu.pethome.utils.ConstantUtil.CACHE_HOSPITAL;
import static edu.cqu.pethome.utils.ConstantUtil.LOCK;

@Slf4j
@Service
public class HospitalServiceImpl extends ServiceImpl<BaseMapper<Hospital>,Hospital> implements HospitalService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    private static final ObjectMapper objectMapper=new ObjectMapper();
    private boolean tryLock(String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key,"1",10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unLock(String key){
        stringRedisTemplate.delete(key);
    }

    @Override
    public Result getHospitalByID(int id) throws JsonProcessingException, InterruptedException {
        //TODO 使用布隆过滤器，避免缓存击穿

        // 使用互斥锁，避免缓存穿透
        // 从缓存中查找医院信息
        String hospitalString = stringRedisTemplate.opsForValue().get(CACHE_HOSPITAL + id);
        // 如果找到了，直接返回
        if (StrUtil.isNotBlank(hospitalString)) {
            Hospital hospital = objectMapper.readValue(hospitalString, Hospital.class);
            return Result.ok(hospital);
        }
        if (tryLock(LOCK+id)) {
            log.info("-------- [MYSQL] ---------");
            //TODO 获取到锁，去数据库拿数据
            // 如果没找到，向数据库发送请求
            Hospital hospital = getById(id);
            if (hospital==null) {
                //返回空结果给redis，防止缓存穿透
                stringRedisTemplate.opsForValue().set(CACHE_HOSPITAL+id,null);
                // 如果不存在该医院信息，返回空结果
                return Result.err("好像出错了");
            }
            // 如果存在
            // 添加结果到redis
            String hospitalStr = objectMapper.writeValueAsString(hospital);
            stringRedisTemplate.opsForValue().set(CACHE_HOSPITAL+id,hospitalStr);
            stringRedisTemplate.expire(CACHE_HOSPITAL+id,30, TimeUnit.MINUTES);
            // 做完了，记得释放锁
            unLock(LOCK+id);

            //返回结果给前端
            return Result.ok(hospital);
        }
        //TODO 没获取锁，间隔一段时间，再获取一遍缓存
        Thread.sleep(100);
        return getHospitalByID(id);
    }

    @Override
    @Transactional
    public Result modifyHospital(Hospital hospital) {
        // 判断是否存在该条数据id
        if (hospital.getId()==null) {
            return Result.err("id不能为空");
        }
        // 存在则将数据存储到mysql中
        updateById(hospital);
        // 将redis的数据删除
        stringRedisTemplate.delete(CACHE_HOSPITAL+hospital.getId());
        return Result.ok();
    }
}
