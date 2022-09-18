1.添加依赖
<!-- spring-boot-devtools -->
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-devtools</artifactId>
      <optional>true</optional> 
</dependency>

2.application.yml配置
spring.devtools.remote.restart.enabled=true
spring.devtools.restart.additional-paths=src/main/java

3.IDEA配置
1) File->Settings->Compiler->Build Project automatically 进行勾选
2) ctrl + shift + alt + / ,选择Registry,勾上 Compiler autoMake allow when app running