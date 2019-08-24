# Spring Boot 整合 Mybatis 原理

## Spring 基础

+ IoC容器
+ JavaConfig
+ 事件监听
+ SpringFactoriesLoader

## Spring Boot stater 基础

参考 《Spring源码深度解析（第2版）》第14章 Spring Boot 体系原理；  
理解这章内容对理解Spring Boot 加载所有 starter 组件非常重要。

#### @Conditional 条件注解 

ConditionalOnBean           仅在当前上下文中存在某个bean时，才会实例化这个Bean。
ConditionalOnClass          某个class位于类路径上，才会实例化这个Bean。
ConditionalOnCloudPlatform
ConditionalOnExpression     某个class位于类路径上，才会实例化这个Bean。
ConditionalOnJava
ConditionalOnJndi
ConditionalOnMissingBean    仅在当前上下文中不存在某个bean时，才会实例化这个Bean。
ConditionalOnMissingClass   仅在当前上下文中不存在某个bean时，才会实例化这个Bean。
ConditionalOnNotWebApplication  不是web应用时才会实例化这个Bean。
ConditionalOnProperty            
ConditionalOnResource
ConditionalOnSingleCandidate
ConditionalOnWebApplication
@AutoConfigureAfter         在某个bean完成自动配置后实例化这个bean。
@AutoConfigureBefore        在某个bean完成自动配置前实例化这个bean。

#### Spring boot autoconfigure 自动化配置加载 starter 原理  

@SpringBootApplication
```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration    //使能自动化配置
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication {
    //...
}
```

@EnableAutoConfiguration
```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import({AutoConfigurationImportSelector.class})    //加载 AutoConfigurationImportSelector.class
public @interface EnableAutoConfiguration {
    String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

    Class<?>[] exclude() default {};

    String[] excludeName() default {};
}
```

AutoConfigurationImportSelector.class 初始化

从源码方法名看，猜测三个比较重要的方法：

+ selectImports()

+ getAutoConfigurationEntry()
    
    加断点先进入到这个方法
    
    ```
    protected AutoConfigurationEntry getAutoConfigurationEntry(AutoConfigurationMetadata autoConfigurationMetadata,
        AnnotationMetadata annotationMetadata) {
        if (!isEnabled(annotationMetadata)) {
            return EMPTY_ENTRY;
        }
        AnnotationAttributes attributes = getAttributes(annotationMetadata);
        //所有包中需要自动加载的类列表，autoConfigurationMetadata是所有需要自动加载的类的列表以及其依赖类
        List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
        //去重
        configurations = removeDuplicates(configurations);
        //去除 exclusions 中指定的类
        Set<String> exclusions = getExclusions(annotationMetadata, attributes);
        checkExcludedClasses(configurations, exclusions);
        configurations.removeAll(exclusions);
        //TODO：
        configurations = filter(configurations, autoConfigurationMetadata);
        //TODO：派发事件
        fireAutoConfigurationImportEvents(configurations, exclusions);
        return new AutoConfigurationEntry(configurations, exclusions);
    }
    
    autoConfigurationMetadata数据来源：  
    从所有包的META-INF/spring-autoconfigure-metadata.properties中读取配置；
    这个项目只扫描到 spring-boot-autoconfigure-2.1.6.RELEASE.jar 和 mybatis-spring-boot-autoconfigure-2.0.1.jar中有这个文件；
    (TODO: spring-autoconfigure-metadata.properties 具体做什么的？从mybatis看是处理bean依赖的)
    [49. Creating Your Own Auto-configuration](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/htmlsingle/#boot-features-developing-auto-configuration)
    
    autoConfigurationMetadata 貌似是从所有包读取到的配置注解和对应要加载的类的Hashtable集合；
    里面包含 mybatis-spring-boot-autoconfigure 中的 
    org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration @ConditionalOnClass @AutoConfigureAfter
    
    idea的evaluate窗口搜索
    ```
    ((AutoConfigurationMetadataLoader.PropertiesAutoConfigurationMetadata) autoConfigurationMetadata).properties.getProperty("org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration.ConditionalOnClass")
    ```
    
    ```
    @org.springframework.context.annotation.Configuration
    @ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })   //要求类路径上存在这两个类
    @ConditionalOnSingleCandidate(DataSource.class)
    @EnableConfigurationProperties(MybatisProperties.class)
    @AutoConfigureAfter(DataSourceAutoConfiguration.class)      //要求DataSourceAutoConfiguration.class自动配置后才自动配置MybatisAutoConfiguration
    public class MybatisAutoConfiguration implements InitializingBean {
        ...
    }
    ```

    - getCandidateConfigurations()
       
       获取所有包 META-INF/spring.factories 中的自动配置类。

+ fireAutoConfigurationImportEvents

    ？？？

    ```
    private void fireAutoConfigurationImportEvents(List<String> configurations, Set<String> exclusions) {
        List<AutoConfigurationImportListener> listeners = getAutoConfigurationImportListeners();
        if (!listeners.isEmpty()) {
            AutoConfigurationImportEvent event = new AutoConfigurationImportEvent(this, configurations, exclusions);
            for (AutoConfigurationImportListener listener : listeners) {
                invokeAwareMethods(listener);
                listener.onAutoConfigurationImportEvent(event);
            }
        }
    }
    ```


总结：


#### 创建自己的 starter

[49.5 Creating Your Own Starter](https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/boot-features-developing-auto-configuration.html#boot-features-custom-starter)

## Spring Boot 应用整合 Mybatis 流程

Spring boot 整合 Mybatis 表面上非常简单，导入mybatis-spring-boot-starter
使用注解的方式，只是在 application.properties中添加spring.datasource的配置，
以及在Sql的Mapper接口添加 @Repository、@Mapper 、@Select 注解。
    
## Spring 整合 Mybatis 源码分析

但是对比Mybatis的命令行应用，Spring boot 背后自动做了什么操作？

翻了一下资料：发现《Spring源码深度解析（第2版）》第9章有讲spring通过xml整合Mybatis这部分内容。

书中介绍到两个重要的Bean：  

+ `SqlSessionFactoryBean`

    打开这个类的源文件，发现了和 Mybatis `XMLConfigBuilder`中很多相同的成员变量；
    有个重要的方法 `buildSqlSessionFactory()`;

    ```
    //Spring Bean 的加载是通过事件驱动的链式的加载
    public void onApplicationEvent(ApplicationEvent event) {...}
    //获取SqlSession
    public SqlSessionFactory getObject() throws Exception {...}
    //当Bean的所有属性被设置后执行，然后调用buildSqlbuildSqlSessionFactory方法
    public void afterPropertiesSet() throws Exception {...}
    //连接到Mybatis的API了
    protected SqlSessionFactory buildSqlSessionFactory() throws Exception {...}
    ```
    
+ `MapperFactoryBean`

Spring boot 导入 Mybatis 的包：
+ mybatis-spring-boot-starter
    - mybatis-spring-boot-autoconfigure
    - mybatis
    - mybatis-spring
    - spring-boot-starter-jdbc

在`SqlSessionFactoryBean`中加入断点，debug

+ DataSourceProperties

    spring.datasource 配置对应着 DataSourceProperties.java, 然后需要找出它是怎么
    被解析使用的。 
    
    ```
    @ConfigurationProperties(prefix = "spring.datasource")
    public class DataSourceProperties implements BeanClassLoaderAware, InitializingBean {...}
    ```
    
    `DataSourceInitializerInvoker`
    
    

+ @Repository
