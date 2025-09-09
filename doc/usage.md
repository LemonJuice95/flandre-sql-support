# 使用方式

## 目录
 - [导入依赖](#导入依赖)
 - [执行SQL逻辑示例](#执行sql逻辑示例)
 - [配置文件](#配置文件)

## 导入依赖
在你的`build.gradle`中加入如下依赖配置：
```groovy
implementation 'io.github.lemonjuice95:flandre-sql-support:${替换为具体版本}'
```

## 执行SQL逻辑示例

```java
public void exampleExecute(Object param) {
    try (Connection connection = SQLCore.getInstance().startConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO some_chart(some_column) VALUES(?)")) {
        preparedStatement.setObject(1, param);
        preparedStatement.execute();
    } catch (SQLException e) {
        //处理异常逻辑
    }
}
```

## 配置文件

配置文件位于运行目录下的`config/mysql.properties`（不存在时会主动释放）

### 基础配置
- `bot.sql.host`
  - 运行MySQL服务的主机ip
- `bot.sql.port`
  - MySQL服务的端口
  - 默认值：`3306`
- `bot.sql.db_name`
  - 所要连接到的数据库名称
- `bot.sql.username`
  - MySQL的用户名
- `bot.sql.password`
  - MySQL的密码

### 连接配置
- `bot.sql.connection_pool.size`
  - 连接池中的最大连接数量
  - 默认值：`15`
- `bot.sql.connection.timeout_ms`
  - 连接超时时间（毫秒）
  - 默认值：`30000`（30秒）
- `bot.sql.connection.idle_timeout_ms`
  - 连接池闲置回收时间（毫秒）
  - 默认值：`1800000`（30分钟）
- `bot.sql.heartbeat.interval_ms`
  - 心跳检测连接存活状态的时间间隔（毫秒）
  - 默认值：`30000`（30秒）

### 危险区
- `bot.sql.strongly_need`
  - Bot是否对数据库具有**强依赖**
  - 默认值：`true`
  - 当设置为`true`时，心跳操作执行连续失败次数过多后会触发熔断机制，停止Bot的运行
- `bot.sql.max_failed_count`
  - 心跳操作执行失败的最大次数（在连续失败到达最大次数时立即停止）
  - 当`bot.sql.strongly_need`设置为`true`时，此配置生效
  - 默认值：`5`