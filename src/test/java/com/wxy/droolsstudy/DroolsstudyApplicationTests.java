package com.wxy.droolsstudy;

import com.wxy.droolsstudy.entity.Margin;
import com.wxy.droolsstudy.entity.People;
import com.wxy.droolsstudy.entity.Person;
import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DroolsstudyApplicationTests {

    @Autowired
    private KieBase kieBase;

    @Autowired
    private KieSession kieSession;

    @Test
    void test() {
        Person person = new Person();
        person.setAge(10);
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
    }
    @Test
    void test1() {
        People people = new People();
        people.setSex(0);
        people.setName("张三");
        people.setDrlType("people");
        //传参
        kieSession.insert(people);
        //执行所有规则
//        kieSession.fireAllRules();
        //执行指定名称的规则
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("girl"));
    }

    // 穿入2个对象判断
    @Test
    void test2() {
        People people = new People();
        people.setSex(0);
        people.setName("小芳");
        people.setDrlType("people");

        Person person = new Person();
        person.setAge(16);
        person.setName("Test");

        //传参
        kieSession.insert(people);
        kieSession.insert(person);
        //执行指定名称的规则
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("girlAndPerson"));
    }
    //contains 用于判断对象的某个字段是否包含另外一个对象  not contains 不包含
    @Test
    void contains() {
        People people = new People();
        people.setSex(0);
        people.setName("小芳");
        people.setDrlType("people");
        kieSession.insert("小");
        kieSession.insert(people);
        //执行指定名称的规则
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("contains"));
    }
    //memberOf 用于判断对象的某个字段是否存在一个集合中
    @Test
    void memberOf() {
        People people = new People();
        people.setSex(0);
        people.setName("小芳");
        people.setDrlType("people");
        kieSession.insert("小");

        kieSession.insert(new ArrayList<String>(){{
            add("小白");
            add("小芳");
        }});

        kieSession.insert(people);
        //执行指定名称的规则
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("memberOf"));
    }
    //matches 匹配正则表达式的了
    @Test
    void matches() {
        People people = new People();
        people.setSex(0);
        people.setName("小芳");
        people.setDrlType("people");
        kieSession.insert(people);
        //执行指定名称的规则
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("matches"));
    }
    // from 从集合中匹配取对象
    @Test
    void from() {
        People people = new People();
        people.setSex(0);
        people.setName("小芳");
        people.setDrlType("people");

        People people1 = new People();
        people1.setSex(1);
        people1.setName("小黑");
        people1.setDrlType("people");

        People people2 = new People();
        people2.setSex(1);
        people2.setName("小黑黑");
        people2.setDrlType("people");

        List<People> list = new ArrayList<People>(){{
            add(people);
            add(people1);
            add(people2);
        }};
        //传参
        kieSession.insert(list);
        //执行指定名称的规则
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("from"));
    }

    // 从指定来源或从Drools引擎的工作内存中获取集合,可以使用Java集合（例如List，LinkedList和HashSet）
    @Test
    void collect() {
        People people = new People();
        people.setSex(0);
        people.setName("小芳");
        people.setDrlType("people");

        People people1 = new People();
        people1.setSex(1);
        people1.setName("小黑");
        people1.setDrlType("people");

        People people2 = new People();
        people2.setSex(1);
        people2.setName("小黑黑");
        people2.setDrlType("people");
        //传参
        kieSession.insert(people);
        kieSession.insert(people1);
        kieSession.insert(people2);
        //执行指定名称的规则
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("collect"));
    }
    // 用于遍历数据集对数据项执行自定义或预设动作并返回结果。
    @Test
    void accumulate() {
        People people = new People();
        people.setAge(1);

        People people1 = new People();
        people1.setAge(2);

        People people2 = new People();
        people2.setAge(3);
        //传参
        kieSession.insert(people);
        kieSession.insert(people1);
        kieSession.insert(people2);
        //执行指定名称的规则
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("accumulate"));
    }

    // 将改变通知drolls引擎 并通知引擎重新匹配该事实
    @Test
    void update() {
        People people = new People();
        people.setSex(0);
        people.setDrlType("update");
        //传参
        kieSession.insert(people);
        //执行指定名称的规则
        kieSession.fireAllRules();
//        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("update"));

        System.out.println("set"+ people);
    }

    //使用全局变量 Drools规则文件中的全局变量（global variables）是规则文件代码与java代码之间相互交互的桥梁，
    //我们可以利用全局变量让规则文件中的程序使用java代码中的基本变量、缓存信息或接口服务等等。
    //全局变量可以是一个services或者一个对象，来方便drolls与java之间的数据传输
    @Test
    void global() {
        //声明全局变量
        List<People> list = new ArrayList<>();
        kieSession.setGlobal("myList",list);

        People people = new People();
        people.setSex(0);
        people.setDrlType("update");
        //传参
        kieSession.insert(people);
        //执行指定名称的规则
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("global"));
        System.out.println("获取全局变量执行结果:"+ kieSession.getGlobal("myList"));
    }
    // 使用session.getQueryResults(查询名, 参数, 参数。。。) 来获取QueryResults匹配对象列表
    @Test
    public void query() {
        kieSession.insert(new People(1, "春", "query",0));
        kieSession.insert(new People(2, "夏", "query",0));
        kieSession.insert(new People(3, "秋", "query",0));
        kieSession.insert(new People(4, "冬", "query",0));
        kieSession.insert(new People(5, "达", "query",0));
        QueryResults results = kieSession.getQueryResults("queryPeople", "达", 5);
        for (QueryResultsRow row : results) {
            People p = (People) row.get("$p");
            System.out.println(p);
        }
    }

    /**
     * 自定义fact对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     * 通过 kieBase.getFactType(域名，实事名)的方式获取实事对象并实例
     * 通过 factType.set(实例，属性名，属性值)的方式来赋值变量
     */
    @Test
    public void declare() throws IllegalAccessException, InstantiationException {

        FactType factType = kieBase.getFactType("com.wxy.droolsstudy","Love");
        Object obj = factType.newInstance();
        factType.set(obj,"feel","sad");
        factType.set(obj,"continued","永远");
        kieSession.insert(obj);
        kieSession.fireAllRules();
    }

    /**
     *在一个规则中如果条件满足就对Working Memory当中的某个Fact对象进行修改，比如使用update将其更新到当前的Working Memory当中，这时候引擎会再次检查所有的规则是否满足条件，如果满足会再执行，可能会出现死循环
     *
     * 作用：用来控制已经执行过的规则条件再次满足时是否再次执行，默认是false，如果属性值是true，表示该规则只会被规则引擎检查一次，如果满足条件就执行规则的RHS部分
     *
     * 注意：如果引擎内部因为对Fact更新引起引擎再次启动检查规则，那么它会忽略掉所有的no-loop属性设置为true的规则
     */
    @Test
    public void noloop(){
        kieSession.insert(new People(0,"张三","no-loop",11));
        kieSession.fireAllRules();
    }

    @Test
    public void insert(){
        kieSession.fireAllRules();
    }

    @Test
    public void count(){
        Margin margin = new Margin(0, 20, 1, null);
        kieSession.insert(margin);
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("20"));
        System.out.println(margin);
    }
}
