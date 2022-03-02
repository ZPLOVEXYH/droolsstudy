package com.wxy.droolsstudy.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.drools.core.base.RuleNameEndsWithAgendaFilter;
import org.drools.core.impl.InternalKnowledgeBase;
import org.kie.api.KieBase;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DroolsUtils {


    @Autowired
    private KieBase kieBase;

    /**
     * 获取交互会话 kiesession
     *
     * @return
     */
    public KieSession getKieSession() {
        return kieBase.newKieSession();
    }

    /**
     * 添加单个规则到规则库
     *
     * @param drl
     * @throws UnsupportedEncodingException
     */

    public void addRule(String drl) throws UnsupportedEncodingException {
        addRules(
                new ArrayList<String>() {{
                    add(drl);
                }}
        );
    }

    /**
     * 添加多个规则到规则库
     */
    public void addRules(List<String> drls) {
        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
        drls.forEach(e -> {
            try {
                kb.add(ResourceFactory.newByteArrayResource(e.getBytes("utf-8")), ResourceType.DRL);
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                unsupportedEncodingException.printStackTrace();
                log.info("unsupportedEncodingException={}", unsupportedEncodingException.getMessage());
            }
        });
        if (kb.hasErrors()) {
            String errorMessage = kb.getErrors().toString();
            log.info("规则语法异常={}", errorMessage);
        }
        InternalKnowledgeBase kBase = (InternalKnowledgeBase) kieBase;
        kBase.addPackages(kb.getKnowledgePackages());
    }

    /**
     * 添加到新的规则库
     *
     * @param drls
     */
    public void addRulesToNewRuleBase(List<String> drls) {
        KieHelper kieHelper = new KieHelper();
        drls.forEach(e -> {
            kieHelper.addContent(e, ResourceType.DRL);
        });
        KieBase newKieBase = kieHelper.build();
        this.kieBase = newKieBase;
    }

    /**
     * 执行单个对象
     *
     * @param obj      交互对象
     * @param ruleName 需要执行的规则名称
     */
    public void execute(Object obj, String ruleName) {
        executeMore(
                new ArrayList<Object>() {{
                    add(obj);
                }},
                ruleName
        );
    }

    /**
     * 执行多个对象
     *
     * @param list
     * @param ruleName 需要执行的规则名称
     */
    public void executeMore(List<Object> list, String ruleName) {
        KieSession kieSession = getKieSession();
        try {
            list.forEach(e -> {
                kieSession.insert(e);
            });
            if (StringUtils.isNoneBlank(ruleName)) {
                kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter(ruleName));
            } else {
                kieSession.fireAllRules();
            }
        } catch (Exception e) {
            log.info("执行异常list={},e={}", list, e);
        } finally {
            kieSession.dispose();
        }
    }

    /**
     * 执行自定义对象
     */
    public void executeCustomObj(Map<String, Object> objectMap, String packageName, String customObjName) throws IllegalAccessException, InstantiationException {
        FactType factType = kieBase.getFactType(packageName, customObjName);
        Object obj = factType.newInstance();
        factType.setFromMap(obj, objectMap);
        KieSession kieSession = getKieSession();
        kieSession.insert(factType);
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}
