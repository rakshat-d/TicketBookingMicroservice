package org.movieBooking.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@PropertySource("classpath:application.yml")
public class PropertyConfig {

    @Autowired
    private Environment env;

    public String getProperty(String key) {
        return env.getProperty(key);
    }

    public List<String> getPropertyList(String key) {
        return Arrays.stream(Objects.requireNonNull(env.getProperty(key)).split(",")).toList();
    }
}
