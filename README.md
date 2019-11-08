# black-white-ai
黑白棋AI

##### 思路：以“博弈算法”（“极大极小值算法”）为基础，“α-β算法”进行减枝优化

##### 启动：BlackWhite.java 运行main方法

##### 代码结构：
- BlackWhite.java 图形客户端
- BlackWhiteAI.java AI
- ChessBoard.java 棋盘
- PositionScorer.java 棋局评分（位置）
- SafetyScorer.java 棋局评分（安全性）【待开发】
- Config.java 全局配置类

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
- 机机对弈（TODO）
- 重构（TODO） 