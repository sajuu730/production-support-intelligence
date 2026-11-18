package io.github.sajuu730.supportintelligence.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Clock;
@Configuration public class TimeConfig{@Bean Clock clock(){return Clock.systemUTC();}}