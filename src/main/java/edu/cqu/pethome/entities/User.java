package edu.cqu.pethome.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("tb_user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    int id;
    String phone;
    String password;
    String nickName;
    String icon;
    Date createTime;
    Date updateTime;
}
