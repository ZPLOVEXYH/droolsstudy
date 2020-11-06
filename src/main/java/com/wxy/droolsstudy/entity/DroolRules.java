package com.wxy.droolsstudy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("drool_rules")
public class DroolRules {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String ruleItem;

    private String isUse;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;

}
