package com.example.easyquery;

import com.easy.query.core.common.bean.ReflectBean;
import com.easy.query.core.context.DefaultEasyQueryRuntimeContext;
import com.easy.query.core.util.EasyBeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints(EasyQueryRuntimeHintsRegistrar.class)
public class EasyQueryNativeDemoApplication {


//    DefaultEasyQueryRuntimeContext

    public static void main(String[] args) {
        EasyBeanUtil.FAST_BEAN_FUNCTION = ReflectBean::new;
        SpringApplication.run(EasyQueryNativeDemoApplication.class, args);
    }

}
