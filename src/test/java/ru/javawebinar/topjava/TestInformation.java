package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestInformation implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(TestInformation.class);
    private static final Map<String, Long> collectInfo = new LinkedHashMap<>();
    private static final StringBuilder logInfo = new StringBuilder();

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long start = System.nanoTime();
                statement.evaluate();
                long time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                log.info("{} : time = {} ms", description.getMethodName(), time);
                collectInfo.put(description.getMethodName(), time);
            }
        };
    }

    public static void getInfo() {
        logInfo.append("\n===============================Info about tests===============================\n");
        collectInfo.forEach((key, value) ->
                logInfo.append(String.format("%s : %d ms\n", key, value)));
        logInfo.append("===============================================================================");

        log.info(logInfo.toString());
    }
}
