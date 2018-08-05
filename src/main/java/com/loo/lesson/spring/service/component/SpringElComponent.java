package com.loo.lesson.spring.service.component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: base-core
 * @description:
 * @author: Tomax
 * @create: 2018-07-20 18:14
 **/
public class SpringElComponent {

    public boolean getExpResult(String expression, Map<String, Object> param) {
        boolean result = false;
        if (param != null) {
            Pattern pattern = Pattern.compile("@[\\w]+");
            Matcher matcher = pattern.matcher(expression);
            while (matcher.find()) {
                String rpparam = matcher.group();
                String paramName = rpparam.substring(1);
                expression = expression.replace(rpparam, param.get(paramName).toString());
            }
        }

        return result;
    }
}
