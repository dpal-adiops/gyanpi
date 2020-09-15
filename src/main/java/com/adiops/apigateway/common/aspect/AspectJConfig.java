package com.adiops.apigateway.common.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@ComponentScan
@EnableSpringConfigured
@EnableLoadTimeWeaving(aspectjWeaving=AspectJWeaving.ENABLED)
public class AspectJConfig {

}
