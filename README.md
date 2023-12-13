# min-max
黑白棋AI

##### 思路：以“博弈算法”（“极大极小值算法”）为基础，“α-β算法”进行减枝优化

##### 启动：BlackWhite.java 运行main方法

##### 代码结构：
- BlackWhite.java 图形客户端
- BlackWhiteAI.java AI
- ChessBoard.java 棋盘
- PositionScorer.java 棋局评分（位置）
- SafetyScorer.java 棋局评分（安全性）
- Config.java 全局配置类
- BWState.java 棋局状态枚举
- HumanPlayer.java 人类玩家
- HumanPlayerActionListener.java 人类玩家落子事件处理
- Piece.java 棋子类
- Player.java 玩家抽象类
- Point.java 坐标类

---

##### Release Notes
##### v0.5 
- 基础UI
- 人机对弈
- 博弈算法
- α-β优化算法
- “位置”评分算法
##### v1.0
- “安全性”评分算法
- 机器和机器对弈
- 重构
##### v2.0
- 历史记录 TODO
- 胜率分析并帮助决策 TODO

---

##### 学习笔记：
*下棋方式的选择：*
1. 分析ANALYSIS、策略STRATEGY、战术TACTICS -> 下一步棋MOVE
2. 如果那么规则 IF-THEN RULES
3. 向前看&评估 LOOK AHEAD & EVALUATE
4. 穷举 BRITISH MUSEUM
5. 尽量向前看 LOOCK AHEAD AS FAR AS POSSIBLE

*关于形式评估：*

通常棋局形式S可以用一个线性计分多项式来计算：

S = g(f1,f2,...fn) = c1f1 + c2f2 + ... + cnfn

*MINIMAX算法:*

MAX 极大者：希望评分对自己放尽量有利
MIN 极小者：作为极大者的对手，希望评分尽可能小，尽量对极大者不利，也就意味着对自己有利

<pre>
MAX       <2>
        /     \
       /       \
MIN   <2>      <1>
     /  \     /   \
    /    \   /     \
   2      7 1       8
</pre>
O = b(分支数) 的 d(深度) 次方

*α-β剪枝优化算法:*

<pre>
MAX      (>=2)
        /     \
       /       \
MIN   (2)     (<=1) 该分支可以减掉了
     /  \     /   \
    /    \   /     \
   2      7 1       
</pre>
O = 2 * [b(分支数) 的 d(深度)/2 次方]

*逐层深入PROGRESSIVE DEEPENING:*

有时我们并不确定计算n层需要花费多长时间，这个时间可能很长，用户无法等待。在这个情况下我们不妨先计算出n-1层，当n层（时间：b的n次方）计算时间过长时，返回n-1层（时间：b的n-1次方）的结果。n-1层计算所花费的时间为n层/b。假如分支数b为10，计算n-1层只需要n层的十分之一，显然这么做是值得的。
而当我们不确定能够计算到多少层时，可以每一层都计算一份保单。所花费时间为：(b的d次方 -1) / (b - 1)

DEEP BLUE(深蓝) = MINIMAX + α-β + PROGRESSIVE DEEPENING + 并行计算(PARALLEL COMPUTING) + 开局库(OPENING BOOK) + 残局算法(SPECIAL-PURPOSE STUFF FOR THE ENDGAME) + 非均树创建(UNEVEN TREE DEVELOPMENT)

> 参考：网易公开课 麻省理工学院公开课：人工智能 06 搜索：博弈、极小化极大、α-β http://open.163.com/newview/movie/free?pid=MCTMNN3UI&mid=MCTMO57MF
