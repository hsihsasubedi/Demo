package com.example.demo.utils;

import java.io.File;
import java.util.UUID;

public class DemoConstants {

    public static final String UUID() {
        return UUID.randomUUID().toString();
    }

    public static final String[] USER_TYPE = {
            "ADMIN", "MANAGER", "OPERATOR"
    };

    public static String FILE_REPO = "D:\\Project_Docs\\NIIT-DEMO";

    public static String PRODUCT_REPO = FILE_REPO + File.separator + "product";

    public static String EMAIL_FROM="NIIT Nepal - Batch 2018";

    public static String APPLICATION_URL="http://localhost:8080/demo/";

    public static int DELIVERY_DAYS = 5;

    // CRON trigger Ref : https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html
    public static final String SCHEDULER_CRON = "0 0/5 * * * *";

}
