package com.wxy.droolsstudy.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Margin {
    /**
     * 0 个人户 1企业户
     */
    private Integer type;
    /**
     * 单车低值
     */
    private Integer bicyclelow;

    private Integer num;

    private String result;
}
