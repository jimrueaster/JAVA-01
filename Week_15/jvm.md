# Section 01

> 重点：类加载器、内存模型

### 语言跨平台

* Java 可以做到二进制跨平台，只要有 JRE 就可以运行了
* C++ 只能做到源代码跨平台

### 字节码、类加载器、虚拟机关系

文件系统中，有**字节码**文件
虚拟机的**类加载器**加载**字节码**，变成**类**，再变成内存中的**对象实例**

## 字节码

* Java 需要编译，意味着 .java 无法直接运行。JVM 只认 .class 文件
* 单个字节组成的命令，每个 1B
* `javac HelloByteCode.java` 编译成字节码（就是二进制的东西，可以用 sublime text 打开）
* `javac -g HelloByteCode.java` 编译时增加方法的本地变量表(LocalVariableTable)
* `javap -c HelloByteCode.class` 查看助记符
* `javap -c -verbose HelloByteCode.class` 查看更多细节（常量池、行号、时间戳、校验和、版本号……）
    * 按前缀区分类型：a-address、i-int、d-double……
    * 算术操作：iadd、ladd……
    * 类型转换：i2i、i2d……
    * 逻辑控制：ifne
* 通过分析字节码发现语法糖的 JVM 原理
* 局部变量不能超过 256*256-1 个，由于槽位最多两个字节

#### 分成四大类

* 由于 JVM 基于栈操作，所以有栈操作指令
    * LOAD 负责把本地变量表的压到栈
    * STORE 把栈中的数据，写到本地变量表
* 程序流程指令，如 IF ELSE
* 对象操作指令，如方法调用
* 算术运算、类型转换指令

### 方法的本地变量表

* 变量的数目
* 变量名
* `Start` 指在哪个位置开始起作用
* `Length` 指范围
* `Code` 中的数字索引，是二进制字节码的位置

### 方法调用的指令（不用太 care）

* invokestatic 静态方法
* invokespecial 构造函数、private、可见超类
* invokevirtual public、protected、
* invokeinterface 通过接口调用
* invokedynamic lambda

## 类加载机制

### 步骤

> 口诀：家教准解初使卸

* 加载的过程
    * 加载：找 .class
    * 链接
        * 校验
        * 准备：常量池
        * 解析
    * 初始化
* 使用
* 卸载

#### 加载的时机

* 启动虚拟机
* new 类
* 调用静态方法
* 调用静态字段
* 子类初始化会触发父类初始化
* 接口 default 方法
* 反射
* MethodHandle 要初始化，否则调用不了

#### 只加载不初始化

* 子类用父类静态字段，子类不用初始化
* 定义对象数组
* 常量编译期间存入常量池，但只要未**直接引用定义常量的类**，就不会触发初始化
* 通过类名获取 Class 对象，只拿到了定义，但还没有实例对象   ，意味着还不能用
* 通过 Class.forName 加载指定类时，如果故意指定参数 `initialize` 为 false，那就不初始化（默认是初始化的）
* 通过 ClassLoader 的 loadClass 方法，也是只加载不初始化

### 三类加载器

* 双亲委托：其实是父辈委托，总之要“啃老”
* 负责依赖：或者叫全盘负责，意味着要负责某个类的所有依赖
* 缓存加载：只加载一次，之后从缓存加载

#### 添加引用类

* JDK lib/etc 或 -Djava.ext.dirs
* java -cp
* 自定义 ClassLoader
* 或者从 .class 文件中加载一个类：

> JDK 9

`Class.forName("xxxx", new URLClassloader("path or jar"))`

> JDK 8

当前执行类的 ClassLoader，反射调用 addUrl 方法

```
// via: https://stackoverflow.com/questions/46694600/java-9-compatability-issue-with-classloader-getsystemclassloader

private static int AddtoBuildPath(File f) {
    try {
        URI u = f.toURI();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(urlClassLoader, u.toURL());
    } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | MalformedURLException | IllegalAccessException ex) {
        return 1;
    }

    return 0;
}
```

## JVM 内存模型

### 内存结构

* 栈
    * 方法的局部变量表
    * 操作数栈
    * Class/Method 指针
    * 返回值
    * 除了对象，对象在堆
* 堆：又叫，共享堆，堆内
    * 年轻代
        * 新生儿（Eden Space）：new 出来的在这里
            * TLAB：给线程用的线程缓冲区
        * S0：新生儿满了，触发 GC，活下来的就到 就到 Survivor space，Eden Space 清空
        * S1：与 S0 之间总有一个是空的
    * 老年代
* 非堆
    * 其实还是堆
    * JConsole 里面就是叫 Non-Heap
    * 元数据区：对象本身的元数据
        * 方法区
            * 常量池
    * CCS（Compressed Class Space）：压缩类空间的一些指针
    * CC(Code Cache)：JIT 即时编译出来的一些代码
* 堆外(Direct Memory)
    * 不是 JMM 中定义的，JVM 管不到的
    * 直接向 OS 申请的，类似 C/C++ 的 malloc()
    * 可以给应用用，如 netty 新开辟出来的内存、JNI
* JVM 本身

### CPU 与内存行为

内存速度比 CPU 相比差几个数量级

CPU 的核，有 L1/L2 高速缓存区间，速度比内存高几个数量级

### JMM

一个规范，实现了 Java 并发程序真正的跨平台

### JVM 启动参数

* -D 系统参数，设置系统属性
* -X 非标准参数，不保证向后兼容
    * 可以用 `java -X` 查看有哪些可以用
* -XX 非稳定参数
* 运行模式
    * -server 启动慢
    * -client 启动快，运行时性能和内存管理效率不高，适合做客户端
    * -Xint 解释模式，强制使用解释，速度可能下跌为原来的 1/10
    * -Xcomp 强制编译所有字节码成本地代码，最快，但要注意预热
    * -Xmined 混合模式，也是默认的模式和推荐的模式，JVM 自己决定用解释还是编译

### 堆内存（最重要的参数）

* -Xmx 只限制 Heap 部分最大值
    * 堆内受 GC 管理，给业务代码用的
    * 堆外不受 GC 管理，不是给业务代码用的
    * **最佳实践：-Xmx 不超过 OS 可用的 60~80%**
* -Xms 堆内存初始大小
* -Xss 每个线程栈的 size，可以通过减少它增加线程数
* -Xmn NewSize 使用 G1GC 时不要设它
* -XX: MaxMetaspaceSize 默认不限制大小，一般不改它
* -XX: MaxDirectMemorySize 最大堆外内存
* -XX: UseAdaptiveSizePolicy 自动调整各区大小

# Section 02

> 重点：  
> * JDK 内置命令行、图形化工具
> * 串行、并行 GC
> * CMS 和 G1 GC

## JVM 命令行工具

### 核心的开发、部署、运行工具

* `java` run 起来
* `javac` 编译工具
* `javap` 反编译，通过助记符
* `javadoc` 根据代码和标准注释生成文档
* ...

### 辅助工具，用于运行期分析调优

> 最常用: 
> * `jstat` 
> * `jmap`
> * `jstack1`

* `jps` 和 `jinfo` 查看 java 进程
    * `jps` 很简陋
    * `jps -l` 看到类或 jar 包
    * `jps -mlv` 还可以看到 jvm 参数
        * 口诀：**买 lv**
    * `jinfo pid`
* `jstat` 查看 JVM 内部的 GC 相关信息，英文说明比较简陋
    * `jstat -gc` 查看
        * 表头中：
            * **XXC** 是容量 capacity
            * **XXU** 是使用量 used
            * 还有一些次数和时间
    * `jstat -gcutil` 查看百分比
    * `jstat -gcutil <interval> <count>` 查看百分比，按某个 interval，一共 count 次
* `jmap` 查看 heap 或类占用空间的情况，就是 `byte[], int[]` 那些
    * JDK 9 之后用 `jhsdb jmap`, JDK 8 用 `jmap`
    * `jmap -heap` 显示的是当前容量
    * *ratio 是最大容量（？）*
    * `jhsdb jmap --heap --pid pid` 看堆内存的状况，会有详细的英文说明
    * `jhsdb jmap --histo --pid pid`
* `jstack` 很简单，就是用来看线程信息的
    * `jstack -l <pid>` 中的 `-l` 可以看锁
* `jcmd` JVM 命令的汇总，或者叫整合
    `jcmd <pid> help` 就会返回可用的参数


## JVM 图形工具

* jconsole
* jvisualvm
    * JDK 9 之后要另外安装
* idea 的插件：VisualGC
    * 要另外安装
* jmc 最强大
    * 支持“飞行记录”(flight record)，可以把抽样信息保存成 jfr 文件，让别人帮忙分析
    * 可视化比较好
    * 做了分析和运算，如单元时间内内存分配速率
    * 要另外安装

## GC

其实不只是**垃圾回收**，更准确应该叫**内存管理器**

### 背景和一般原理

#### 引用计数

每当有人使用，就加一；不再用就减一，引用数为零就回收掉

#### 循环依赖

> 你中有我，我中有你


```
graph LR
A-->B
B-->A
```

可以作为一个整体直接回收掉。但是如果简单粗暴用**引用计数**，那么就永远回收不了，导致内存泄漏

#### 标记清除法(Mark & Sweep)

标记可达对象，不可达的标记为**不可达**，两次后再回收

优势：

* 处理循环依赖比较容易
* 只扫描部分对象

劣势：

* 导致内存碎片化，需要压缩
* 需要 STW

实际例子：

* Parallel GC
* CMS

##### 细分的算法

* 标记清除法(Old 区一个区，先标记后清除)
* 标记复制法(Young -> Old)
* 标记清除整理法(额外加上压缩，避免内存碎片化)

#### 分代假设

假设大部分对象瞬时使用，用完就不再需要它；那么经过几次 GC 都没有被回收的内存，就认为它会一直存活

##### 做法

```
graph LR
Young-gen-->|一直没有被干掉|Old-gen
```

```
graph TD
A[Young-Gen]-->|8|B[Eden-Space]
A-->|1|C[S0]
A-->|1|D[S1]
B-->E[TLAB]
B-->F[TLAB]
```

* 绝大部分新生代会被回收，那么少部分会放在 Survivor 区，**Eden-Space 和 Survivor 区来回挪**
* Young GC 或者叫 Minor GC 频率会比 Old GC 大很多，**Young 区和 Old 区的 GC 算法也会不同**
* **标记阶段，暂停时间长短按存活对象数量来决定**，因为是深度遍历
* 可能因为对象太大，直接进入 Old 区

##### Eden 区没满，为什么也会发生 GC？

因为监测时，使用了“采样”，但采样就会导致失真

##### 为什么 Young 区只需要复制，Old 区要移动？

Young GC 时，Eden 区和 S0 不需要移动，直接认为它们都回收掉就可以了，节约时间和操作；
而对于 Old 区，就要保持内存连续，所以要移动

##### JVM 配置参数

* -XX: +MaxTenuringThreshold=15 多少次 Young GC 到老年代
* -XX: MaxPermSize=256m （1.8前）
* -XX: MaxMetaspaceSize=256m （1.8后） 默认无穷大，没有必要改它

##### 什么可以做根对象？

* 局部变量和输入参数
* 活动线程
* 所有类的静态字段
* JNI 引用（Java Native Interface），允许 Java 程序员“用其他语言来解决问题”

### 串行 GC

* 对年轻代：标记-复制
* 对老年代：标记-清除-整理
* 单线程，全线 STW
* 例子：办公室阿姨搞卫生，只有一个阿姨

### 并行(Parallel) GC

ParNewGC 搭配 CMS 使用

* +UseParallelGC 是针对老年代的。
* 例子：办公室阿姨搞卫生，有多个阿姨同时搞
* 吞吐量比较大，延迟比 CMS 要大
* Java8 默认 GC

### 并发(Concurrent) GC

CMS: **并发**标记清除

* 年轻代是并行的**标记复制**
* 老年代是并发的**标记清除**（没有整理压缩的过程，会有碎片化问题）
* 如果使用并发，能让处理 GC 的线程与业务线程并存，避免长时间停顿
* 吞吐量比较小，但是延迟也比较少
* 例子：办公室阿姨，扫到员工脚下，再妨碍 30s

### G1 GC

可以看作 CMS 的升级版，执行步骤与 CMS 非常像

* -XX: G1NewSizePercent 初始年轻代占 Heap 的大小
* -XX: G1MaxNewSizePercent 最大年轻代占 Heap 的大小
* -XX: G1HeapRegionSize 2的整数次幂，太小的话，大对象就要拆开
* -XX: ConcGCThreads 并发线程数，太高会导致业务线程太少，太少会导致回收垃圾的县城不足
* -XX: InitiatingHeapOccupancyPercent （顶）简称 IHOP，仍然是小步快跑的思想，当老年代达到某个占比时，就开始回收，不再等到它满才回收
* -XX: G1HeapWastePercent （底）G1 停止回收的最小内存大小，不会回收到 0
* -XX: GCTimeRatio 计算花在 Java 应用线程上和 GC 线程上的时间比率
* -XX: MaxGCPauseMillis 预期每次执行 GC 操作暂停的时间，单位是毫秒，GC 会尽量保证

#### 注意事项

* 并发模式失败
    * G1 启动标记周期，但在 Mix GC 之前，老年代被塞满，此时 G1 会放弃标记周期
    * 解决方法：增加堆大小，或者调整周期如增加线程数
* 晋升失败
    * 没有足够的内存供活对象或晋升对象使用，由此触发 Full GC
    * 解决方法：
        * 增加 -XX: G1ReservePercent 增加预留内存量，还要记得增加 Heap 大小
        * 减少 -XX: InitiatingHeapOccupancyPercent 更容易触发老年代回收
        * 增加 -XX: ConcGCThreads 增加并行标记线程的数目
* 巨型对象分配失败
    * 找不到合适空间来分配时，会触发 Full GC
    * 解决方法：
        * 增加内存
        * 增加 -XX: G1HeapRegionSize 来存放大对象

### 常见 GC 组合

* Serial: Serial + Serial Old
* CMS: ParNew + CMS
    * 适合低延迟
* Parallel: Parallel Scavenge + Parallel Scavenge Old
    * 适合吞吐量大
* G1
    * 适合大内存，平均 GC 时间可控

### 新型 GC: ZGC / Shenandoah GC

都是 Pauseless GC

延迟极低，策略复杂

### GC 演进路线

1. 串行 -> 并行
2. 并行 -> 并发（尽量不影响业务线程）
3. CMS -> G1（尽量降低单次 GC 暂停的时间）
4. G1 -> ZGC（号称无停顿 GC）

### GC 总结

还是需要按照实际情况来选择 GC，可以在测试环境来实验


### 附笔记

你家存款有1024w（Xmx-单位m），你的CFO（GC）规定old老婆和young你占有比例2:1，所以你老婆占有的（最大值）是683w（old区最大值），你是341w（max new size），用jmap -heap去看的时候，就是你日常找你老婆申请零花钱，虽然你名义上有341w，但是你老婆看你一天也就抽包烟两顿饭，只给了你8k，这个8k就是young区的初始容量（远远不到新生代最大值341m）。注意，CFO一开始就可能给了你老婆100w。

作为一个节俭的young男人，你把约摸着，你所有的钱，计划分三份用途（eden理财、S0吃饭、S1穿衣服），花的比例应用是8:1:1。

这会儿你手上钱不多，，先得生存（survivor），估计得吃饭占大头，所以实际上你预计吃饭可能花多点1100(S0容量)，穿少点900(S1容量)，还有6000(eden容量)可以买理财，这时候你没法保证真正花的钱比例8:1:1，对吧，差不多6:1:1，但是你基本可以肯定，要是一下子把341w都给你，你就按这个比例来花。

实际上，你可能今天买理财钱花了一半，吃饭钱1100实际上只用了300，买衣服基金没动。
这样查看你的账户，
```
EC  6000
EU  3000
S0C 1100
S0U 300
S1C 900
S1   0
```
