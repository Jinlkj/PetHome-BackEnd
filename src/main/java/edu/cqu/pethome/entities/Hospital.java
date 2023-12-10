package edu.cqu.pethome.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("table_hospital")
public class Hospital {
    @TableId(type = IdType.AUTO)
    Integer id;
    String name;
    String location;
    String description;
    String area;
    String imageMain;
}
