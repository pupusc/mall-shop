### 使用xxl-job的主要原因：
 * 开发人员不方便调试定时任务,无法方便的查看定时任务是否正确执行
 * 测试人员不方便测试定时任务,无法随时触发定时任务,需要让开发人员配合

### 开发步骤：
 * 1、参考如下demo编写JobHandler
```java
        @JobHandler(value="demoJobHandler")
        @Component
        public class DemoJobHandler extends IJobHandler {
        	@Override
        	public ReturnT<String> execute(String param) throws Exception {
        	    // 以下为定时任务业务逻辑
        		for (int i = 0; i < 5; i++) {
        		    // 使用XxlJobLogger进行日志打印,可在调度控制台查看日志信息
        			XxlJobLogger.log("beat at:" + i);
        			TimeUnit.SECONDS.sleep(2);
        		}
        		return SUCCESS;
        	}
        }
```

 * 2、登录对应环境的任务调度中心,在任务管理中添加任务
    >任务描述: 增加定时任务的可读性
    
    >路由策略: 故障转移(若执行器有多个实例,心跳检测后选择有效的执行器执行任务)
    
    >Cron: 根据实际情况填写
    
    >运行模式: BEAN
    
    >JobHandler: 填写@JobHandler(value="demoJobHandler")中的value值
    
    >阻塞处理策略: 单机串行
    
    >子任务ID: 通常为空
    
    >任务超时时间: 默认0
    
    >失败重试次数: 默认0
    
    >负责人: 开发人员姓名
    
    >报警邮件: 根据情况填写
    
    >任务参数: 默认为空
    
 * 3、任务点击启动
    支持单次执行
    支持查看任务调度与执行日志
    支持实时修改任务属性