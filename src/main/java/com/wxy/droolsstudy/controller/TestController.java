package com.wxy.droolsstudy.controller;

import com.wxy.droolsstudy.entity.Person;
import com.wxy.droolsstudy.entity.PersonOrder;
import com.wxy.droolsstudy.utils.DroolsUtils;
import org.drools.core.impl.InternalKnowledgeBase;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private KieSession kieSession;

    @Resource
    @Qualifier("kieSessionByDB")
    private KieSession kieSessionByDB;

    @Autowired
    private DroolsUtils droolsUtils;

    @RequestMapping("/test")
    public String test(Integer age) {
        Person person = new Person();
        person.setAge(age);
        person.setName("Test");

//        Person person1 = new Person();
//        person1.setAge(15);
//        person1.setName("Test1");
//        List list = new ArrayList<>();
//        kieSession.setGlobal("myList",list);

        kieSession.insert(person);
//        kieSession.insert(person1);
        kieSession.fireAllRules();
//        System.out.println(list);
        return "Hello";
    }

    @RequestMapping("/test1")
    public String test1() {
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
    public String test2(Integer age, String name) {
        Person person = new Person();
        person.setAge(age);
        person.setName("Test");
//
//        Person person1 = new Person();
//        person.setAge(age);
//        person.setName(name);
//
//        kieSessionByDB.insert(person);
//        kieSessionByDB.insert(person1);
//        kieSessionByDB.fireAllRules();

        PersonOrder order = new PersonOrder();
        kieSession.insert(order);
        kieSession.fireAllRules();

        return "DBtest" + age;
    }

    @GetMapping("/test3")
    public String test3(Integer age, String name) {
        Person person = new Person();
        person.setAge(age);
        person.setName("Test");

        droolsUtils.execute(person, null);
        return "DBtest" + age;
    }

    @GetMapping("/test4")
    public String test4(Integer age) throws UnsupportedEncodingException {
        String drl = "package com.wxy.personrules\n" +
                "import com.wxy.droolsstudy.entity.Person\n" +
                "//global java.util.List myList\n" +
                "dialect  \"java\"\n" +
                "//function String myFun(Person p){\n" +
                "//    return p.getName();\n" +
                "//}\n" +
                "rule \"age1\"\n" +
                "no-loop true\n" +
                "\n" +
                "    when\n" +
                "        $p:Person(age > 10)\n" +
                "    then\n" +
                "        System.out.println($p.getAge());\n" +
                "end";


        Person person = new Person();
        person.setAge(age);
        person.setName("Test");
        droolsUtils.addRule(drl);
        droolsUtils.execute(person, null);
        return "DBtest" + age;
    }

    @GetMapping("/test5")
    public String test5(Integer age) {
        String drl = "package com.wxy.personrules\n" +
                "import com.wxy.droolsstudy.entity.Person\n" +
                "//global java.util.List myList\n" +
                "dialect  \"java\"\n" +
                "//function String myFun(Person p){\n" +
                "//    return p.getName();\n" +
                "//}\n" +
                "rule \"age3\"\n" +
                "no-loop true\n" +
                "\n" +
                "    when\n" +
                "        $p:Person(age < 10)\n" +
                "    then\n" +
                "        System.out.println($p.getAge());\n" +
                "end";
        droolsUtils.addRulesToNewRuleBase(
                new ArrayList<String>() {{
                    add(drl);
                }}
        );
        Person person = new Person();
        person.setAge(age);
        person.setName("Test");
        droolsUtils.execute(person, null);
        return "DBtest" + age;
    }
}
