package kr.hhplus.be.server.config.common;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)// 엄격하게 매칭 (이름 완전 일치)
                .setFieldMatchingEnabled(true)// setter 없이도 필드 직접 매칭 허용
                .setFieldAccessLevel(AccessLevel.PRIVATE);  // private 필드에도 접근 허용

        return modelMapper;
    }

}