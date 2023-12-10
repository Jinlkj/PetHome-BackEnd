package edu.cqu.pethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.cqu.pethome.dto.Result;
import edu.cqu.pethome.entities.Hospital;

public interface HospitalService extends IService<Hospital> {
    Result getHospitalByID(int id) throws JsonProcessingException, InterruptedException;

    Result modifyHospital(Hospital hospital);
}
