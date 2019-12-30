# black-white-ai
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

##### 待开发：
1. SafetyScorer评分类
2. 机器对弈，对比算法优劣

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
