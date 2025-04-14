# aether
### 一、使用示例

#### 1.1 引入依赖

 最新版本：1.0.0.RELEASE

```
 <dependency>
     <groupId>com.ds.aether</groupId>
     <artifactId>aether-client</artifactId>
     <version>${lastversion}</version>
 </dependency>
```

### 1.2 使用示例

#### 1.2.1 客户端任务

```java
/**
 * @author ds
 * @date 2025/4/10
 * @description 示例任务
 *              使用示例
 *              1.添加@Job注解，指定任务名称
 *              2.继承AbstractJob，实现execute方法，执行任务逻辑
 */
@Slf4j
@Job(name = "job1")
public class ExampleJob extends AbstractJob {

    @Override
    public JobResult execute() throws Exception {
        log.info("执行任务...");
        return JobResult.success();
    }

}
```

