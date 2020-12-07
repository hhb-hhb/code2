package com.heima.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com/heima/common/exception")
public class ExceptionCatchConfig {
}
