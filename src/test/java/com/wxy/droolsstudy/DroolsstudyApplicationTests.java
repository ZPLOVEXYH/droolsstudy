package com.wxy.droolsstudy;

import com.wxy.droolsstudy.entity.Margin;
import com.wxy.droolsstudy.entity.People;
import com.wxy.droolsstudy.entity.Person;
import com.wxy.droolsstudy.entity.PersonOrder;
import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

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
        kieSession.setGlobal("myList", list);

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

        kieSession.insert(new ArrayList<String>() {{
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

        List<People> list = new ArrayList<People>() {{
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

        System.out.println("set" + people);
    }

    //使用全局变量 Drools规则文件中的全局变量（global variables）是规则文件代码与java代码之间相互交互的桥梁，
    //我们可以利用全局变量让规则文件中的程序使用java代码中的基本变量、缓存信息或接口服务等等。
    //全局变量可以是一个services或者一个对象，来方便drolls与java之间的数据传输
    @Test
    void global() {
        //声明全局变量
        List<People> list = new ArrayList<>();
        kieSession.setGlobal("myList", list);

        People people = new People();
        people.setSex(0);
        people.setDrlType("update");
        //传参
        kieSession.insert(people);
        //执行指定名称的规则
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("global"));
        System.out.println("获取全局变量执行结果:" + kieSession.getGlobal("myList"));
    }

    // 使用session.getQueryResults(查询名, 参数, 参数。。。) 来获取QueryResults匹配对象列表
    @Test
    public void query() {
        kieSession.insert(new People(1, "春", "query", 0));
        kieSession.insert(new People(2, "夏", "query", 0));
        kieSession.insert(new People(3, "秋", "query", 0));
        kieSession.insert(new People(4, "冬", "query", 0));
        kieSession.insert(new People(5, "达", "query", 0));
        QueryResults results = kieSession.getQueryResults("queryPeople", "达", 5);
        for (QueryResultsRow row : results) {
            People p = (People) row.get("$p");
            System.out.println(p);
        }
    }

    /**
     * 自定义fact对象
     *
     * @throws IllegalAccessException
     * @throws InstantiationException 通过 kieBase.getFactType(域名，实事名)的方式获取实事对象并实例
     *                                通过 factType.set(实例，属性名，属性值)的方式来赋值变量
     */
    @Test
    public void declare() throws IllegalAccessException, InstantiationException {

        FactType factType = kieBase.getFactType("com.wxy.droolsstudy", "Love");
        Object obj = factType.newInstance();
        factType.set(obj, "feel", "sad");
        factType.set(obj, "continued", "永远");
        kieSession.insert(obj);
        kieSession.fireAllRules();
    }

    /**
     * 在一个规则中如果条件满足就对Working Memory当中的某个Fact对象进行修改，比如使用update将其更新到当前的Working Memory当中，这时候引擎会再次检查所有的规则是否满足条件，如果满足会再执行，可能会出现死循环
     * <p>
     * 作用：用来控制已经执行过的规则条件再次满足时是否再次执行，默认是false，如果属性值是true，表示该规则只会被规则引擎检查一次，如果满足条件就执行规则的RHS部分
     * <p>
     * 注意：如果引擎内部因为对Fact更新引起引擎再次启动检查规则，那么它会忽略掉所有的no-loop属性设置为true的规则
     */
    @Test
    public void noloop() {
        kieSession.insert(new People(0, "张三", "no-loop", 11));
        kieSession.fireAllRules();
    }

    @Test
    public void insert() {
        kieSession.fireAllRules();
    }

    @Test
    public void count() {
        Margin margin = new Margin(0, 20, 1, null);
        kieSession.insert(margin);
        kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("20"));
        System.out.println(margin);
    }

    @Test
    public void Test10() {
        Person p = new Person();
        p.setAge(10);
        kieSession.insert(p);
        Map map = new HashMap();
        map.put("p1", "5");
        kieSession.insert(map);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test
    public void Test11() {
        Person p = new Person();
        p.setName("张三");
        p.setAge(10);

        List list = new ArrayList();
        list.add(1);
        list.add(2);
        p.setList(list);

        Set set = new HashSet();
        set.add(1);
        set.add(2);
        p.setSet(set);

        Map map = new HashMap();
        map.put("一班", 1);
        map.put("二班", 2);
        p.setMap(map);

        kieSession.insert(p);

        PersonOrder order = new PersonOrder();
        order.setPrice(11);
        order.setPerson(p);
        kieSession.insert(order);


//        Map map2 = new HashMap();
//        map2.put("一班2",1);
//        map2.put("二班2",2);
//        kieSession.insert(map);


        List list2 = new ArrayList();
        list2.add(1);
        list2.add(2);
        kieSession.insert(list2);

        List list3 = new ArrayList();
        list3.add(3);
        list3.add(4);
        kieSession.insert(list3);

        kieSession.fireAllRules();
        System.out.println("java 代码执行结果：" + p.getAge());
        kieSession.dispose();
    }

    @Test
    public void Test12() {
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("test01"));
        kieSession.dispose();
    }

    /**
     *orderType 更换类型  换卡1 换签2 换卡换签 5
     * guaranteeStatus 质保期 未获取0 未过期1 过期2
     * manMade 原因类型 true：人为 false：非人为
     * receiveMethod 领取方式 1:邮寄 2 网点自取
     * hasAfterSale 推荐人 true：是 false：否
     * obuSelect 回收选择  1:是,0:否
     * sendBack 回寄选择 1:是,0:否
     * etcChannel 卡片种类  徽通卡 1 苏通卡95折 2 苏通卡85折 3 八桂行卡 4 浙通卡 5 通渝卡 6 赣通卡 7 招商畅行卡 8
     * cardFee OBU服务费
     * cqObuFee 通渝设备费
     * cqMsgFee 通渝信息费
     * cqCardFee 通渝制卡费
     * cardBack 卡片回收
     * obuBack OBU回收
     * refundFee 退款金额
     */
    @Test
    public void Test13() throws IllegalAccessException, InstantiationException {
//        FactType factType = kieBase.getFactType("com.hly.drools.card", "CardChangeFee");
        FactType factType = kieBase.getFactType("com.hly.drools.store", "StoreRule");
        Object obj = factType.newInstance();
        factType.set(obj, "orderType", "5");
        factType.set(obj, "guaranteeStatus", 1);
        factType.set(obj, "manMade", false);
        factType.set(obj, "receiveMethod", 2);
        factType.set(obj, "hasAfterSale", false);
        factType.set(obj, "obuSelect", 0);
        factType.set(obj, "sendBack", 0);
        factType.set(obj, "etcChannel", "6");
        kieSession.insert(obj);
        int count = kieSession.fireAllRules();

        Map<String, Object> resMap = factType.getAsMap(obj);

        System.out.println("匹配了规则数量：" + count);
        System.out.println(resMap);
    }
    @Test
    public void Test14() throws IllegalAccessException, InstantiationException {
//        FactType factType = kieBase.getFactType("com.hly.drools.card", "CardChangeFee");
        FactType factType = kieBase.getFactType("com.hly.drools.card", "CardSignDepositRule");
        Object obj = factType.newInstance();
        factType.set(obj, "specialApproval", true);
        factType.set(obj, "cardCount", 100);
        kieSession.insert(obj);
        int count = kieSession.fireAllRules();

        Map<String, Object> resMap = factType.getAsMap(obj);

        System.out.println("匹配了规则数量：" + count);
        System.out.println(resMap);
    }

    @Test
    public void Test15() throws IllegalAccessException, InstantiationException {
//        FactType factType = kieBase.getFactType("com.hly.drools.card", "CardChangeFee");
        FactType factType = kieBase.getFactType("com.hly.drools.rate", "RechargeFee");
        Object obj = factType.newInstance();
        factType.set(obj, "rechargeAmount", 200);
        kieSession.insert(obj);
        int count = kieSession.fireAllRules();

        Map<String, Object> resMap = factType.getAsMap(obj);

        System.out.println("匹配了规则数量：" + count);
        System.out.println(resMap);
    }
}
