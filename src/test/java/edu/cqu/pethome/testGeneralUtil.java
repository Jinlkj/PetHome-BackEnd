package edu.cqu.pethome;

import edu.cqu.pethome.entities.Hospital;
import edu.cqu.pethome.service.HospitalService;
import edu.cqu.pethome.utils.CacheClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static edu.cqu.pethome.utils.ConstantUtil.CACHE_HOSPITAL;

@Slf4j
@SpringBootTest
public class testGeneralUtil {
    @Autowired
    CacheClient cacheClient;
    @Autowired
    HospitalService hospitalService;
    @Test
    public void testCacheClient(){
        Hospital hospital = cacheClient.queryWithMutex(CACHE_HOSPITAL, 1, Hospital.class, hospitalService::getById, 30, TimeUnit.MINUTES);
        System.out.println(hospital);
    }
}
