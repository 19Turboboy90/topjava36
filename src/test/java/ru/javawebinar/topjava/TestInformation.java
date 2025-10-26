package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestInformation implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(TestInformation.class);
    private static final Map<String, Long> collectInfo = new LinkedHashMap<>();

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long start = System.currentTimeMillis();
                statement.evaluate();
                long time = System.currentTimeMillis() - start;
                log.info("method - {} : time = {}ms", description.getMethodName(), time);
                collectInfo.put(description.getMethodName(), time);
            }
        };
    }

    public static void getInfo() {
        System.out.println("===============================Info about tests===============================");
        collectInfo.forEach((key, value) -> System.out.printf("method - %s : time = %dms\n", key, value));
        System.out.println("==============================================================================");
    }
}
