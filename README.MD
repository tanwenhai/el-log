> Java Agent方式解析注解记录日志上下文信息(agent、apt、aop)，使用SPI提供el实现，依赖slf4j

# 目录结构
* ellog-agent java agent增强方式
* ellog-annotation 注解包
* ellog-core 核心代码实现
* ellog-spring spring el实现
* ellog-processor 编译时处理注解(非agent增强方式)
* ellog-aspect-aop aspectj aop编译时织入
* ellog-samples 样例