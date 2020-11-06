package com.wxy.droolsstudy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class People {

    private int sex;
    private String name;
    private String drlType;
    private int age;
}
