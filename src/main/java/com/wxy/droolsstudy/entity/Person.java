package com.wxy.droolsstudy.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class Person {

    private String name;

    private Integer age;

    private Map map;

    private List list;

    private Set set;

}
