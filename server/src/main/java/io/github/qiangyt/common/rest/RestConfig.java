package io.github.qiangyt.common.rest;

import java.util.List;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Import;

import io.github.qiangyt.common.json.Jackson;

import org.springframework.http.converter.HttpMessageConverter;

@Configuration
@lombok.Getter
@lombok.Setter
@EnableWebMvc
@Import({ ExceptionAdvise.class })
public class RestConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (int i = converters.size() - 1; i >= 0; i--) {
            var converter = converters.get(i);
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                converters.remove(i);
            }
        }

        var mapper = Jackson.buildDefaultMapper(false);
        var jacksonConverter = new MappingJackson2HttpMessageConverter(mapper);

        converters.add(0, jacksonConverter);
    }

}
