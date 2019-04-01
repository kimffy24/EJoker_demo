EJoker_demo
======

展示一些基础案例，基本都是从 `https://github.com/tangxuehua/enode/tree/master/src/Samples` 这里翻译而来的。有疑问请在雪华那里提issue。

### Transfer测试

##### 基本功能展示

	env EJokerNodeAddr="Place your ip here" mvn -Dmaven.test.skip=true clean compile exec:exec -Dexec.executable="java" -Dexec.args="-server -Xms4g -Xmx8g -Xmn3g -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferApp"

##### 常规性多节点运行

+ 启动节点（可在多台机器上每个机器都启动1个节点）：
	
	env EJokerNodeAddr="Place your mechine ip here" mvn -Dmaven.test.skip=true clean compile exec:exec -Dexec.executable="java" -Dexec.args='-server -Xms4g -Xmx8g -Xmn3g -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferNAll'
	
也可以通过修改REPLY_PORT的值在1台机器上启动多个实例。

+ 启动入口

	env EJokerNodeAddr="Place your mechine ip here" mvn -Dmaven.test.skip=true clean compile exec:exec -Dexec.executable="java" -Dexec.args='-server -Xms4g -Xmx8g -Xmn3g -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferFrontend'
	
如果在同一台机器启动，请注意修改REPLY_PORT的值。