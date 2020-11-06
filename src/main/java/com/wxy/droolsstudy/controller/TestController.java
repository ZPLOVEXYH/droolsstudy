package com.wxy.droolsstudy.controller;

import com.wxy.droolsstudy.entity.Person;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private KieSession kieSession;

    @Resource
    @Qualifier("kieSessionByDB")
    private KieSession kieSessionByDB;

    @RequestMapping("/test")
    public String test(Integer age){
        Person person = new Person();
        person.setAge(age);
        person.setName("Test");

        Person person1 = new Person();
        person1.setAge(15);
        person1.setName("Test1");
        List list = new ArrayList<>();
        kieSession.setGlobal("myList",list);

        kieSession.insert(person);
        kieSession.insert(person1);
        kieSession.fireAllRules();
        System.out.println(list);
        return "Hello";
    }

    @RequestMapping("/test1")
    public String test1(){
        String myRule = "import com.asiainfo.bean.Person\n" +
                "\n" +
                "dialect  \"mvel\"\n" +
                "\n" +
                "rule \"age\"\n" +
                "    when\n" +
                "        $person : Person(age<16 || age>50)\n" +
                "    then\n" +
                "        System.out.println(\"这个人的年龄不符合要求！（基于动态加载）\");\n" +
                "end\n";
        KieHelper helper = new KieHelper();
        helper.addContent(myRule, ResourceType.DRL);

        KieSession ksession = helper.build().newKieSession();

        Person person = new Person();
        person.setAge(12);
        person.setName("Test");

        ksession.insert(person);
        ksession.fireAllRules();
        ksession.destroy();

        return "Hello";
    }

    @GetMapping("/test2")
    public String test2(Integer age,String name){
        Person person = new Person();
        person.setAge(age);
        person.setName("Test");

        Person person1 = new Person();
        person.setAge(age);
        person.setName(name);

        kieSessionByDB.insert(person);
        kieSessionByDB.insert(person1);
        kieSessionByDB.fireAllRules();

        return "DBtest"+age;
    }

}
