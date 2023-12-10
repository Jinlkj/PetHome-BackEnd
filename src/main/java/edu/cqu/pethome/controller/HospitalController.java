package edu.cqu.pethome.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.cqu.pethome.dto.Result;
import edu.cqu.pethome.entities.Hospital;
import edu.cqu.pethome.service.HospitalService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static edu.cqu.pethome.utils.ConstantUtil.PAGE_MAX_NUM;

@RestController
@RequestMapping("/api/hospital")
public class HospitalController {
    @Autowired
    HospitalService hospitalService;
    @PostMapping("/info/{id}")
    public Result getHospitalById(@PathVariable int id){
        try {
            return hospitalService.getHospitalByID(id);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/modify")
    public Result modifyHospitalInfo(@RequestBody Hospital hospital){
        return hospitalService.modifyHospital(hospital);
    }
    @PutMapping("/insert")
    public Result insertHospital(@RequestBody Hospital hospital){
        boolean saved = hospitalService.save(hospital);
        if (saved) {
            return Result.ok("成功插入数据");
        }else{
            return Result.ok("插入数据失败，稍后再试");
        }
    }
    @GetMapping("/page/{page}")
    public Result getHospitalsByPage(@PathVariable int page){

        //TODO 分页暂未实现，获取到了全部信息，后面再看看
        // 根据类型分页查询
        Page<Hospital> resPage=hospitalService.query()
                .page(new Page<>(page,PAGE_MAX_NUM));
        // 返回数据
        return Result.ok(resPage.getRecords());
    }
}
