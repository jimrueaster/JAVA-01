# 学习笔记


## 命令

Java11:

弃用 -XX:+PrintGCDetails，改用 -Xlog:gc*

如 java -jar -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+UseParallelGC -Xlog:gc* gateway-server-0.0.1-SNAPSHOT.jar


弃用 jmap -heap, 改用：

jhsdb jmap --heap --pid PID


## 第二课作业

压测：并发 100，总请求数 10000

主机配置： 8GB内存 4核

 java -jar -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+UseSerialGC -Xlog:gc* gateway-server-0.0.1-SNAPSHOT.jar
 java -jar -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+UseConcMarkSweepGC -Xlog:gc* gateway-server-0.0.1-SNAPSHOT.jar
 java -jar -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+UseParallelGC -Xlog:gc* gateway-server-0.0.1-SNAPSHOT.jar
 java -jar -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+UseG1GC -Xlog:gc* gateway-server-0.0.1-SNAPSHOT.jar


|          | QPS | STW | YGC次数 | YGC耗时 | FGC次数 | FGC耗时 |
| -------- | --- | --- | ----- | ----- | ----- | ----- |
| Serial   | 714 | 337 | 3     | 197   | 2     | 140   |
| CMS      | 715 | 190 | 4     | 170   | 1     | 20    |
| Parallel | 709 | 190 | 6     | 110   | 2     | 80    |
| G1       | 667 | 153 | 10    | 153   | N/A   | N/A   |

在我的测试环境，Serial、CMS、Parallel 的 QPS 差不多，看来是 STW 对 QPS 的影响不大；
STW 的总耗时，也如理论所说的， Serial > CMS > Parallel > G1
但是 G1 的 YGC 次数之多出乎我的意料，以 `-Xmx1g -Xms1g` 重测 2 次之后依然相同，故改用更大的内存（2GB 和 4GB），YGC 次数均为 9 次，而 STW 有所下降（136ms 和 124ms），也算是符合预期

总结：
Serail: 单线程 GC
CMS: 多线程低延迟（将来会被 G1 取代）
Parallel: 多线程高吞吐
G1: 不用每次整理整个 Heap，而是每次只整理一部分的内存块，其中包括全部的 Young-Gen 和一部分 Old-Gen