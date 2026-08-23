# 2048 游戏 Android 项目

一个经典的 2048 数字游戏的 Android 实现，使用 Java 开发。

## 功能特性

- 经典 2048 游戏玩法
- 滑动操作（上下左右）
- 分数统计
- 排行榜功能（本地数据库存储）
- 玩家 ID 输入

## 项目结构

```
app/src/main/java/com/example/myapplication/
├── MainActivity.java          # 主活动
├── GameBoard.java             # 游戏棋盘逻辑
├── GameView.java              # 游戏视图绘制
├── DatabaseHelper.java        # 数据库帮助类
├── ScoreRecord.java           # 分数记录模型
├── LeaderboardDialog.java     # 排行榜对话框
├── InputPlayerIdDialog.java   # 玩家ID输入对话框
├── Direction.java             # 方向枚举
└── utils/                     # 工具类
    ├── SortUtils.java         # 排序算法（冒泡、选择、快排、二分查找）
    ├── MathUtils.java         # 数学工具（质数、阶乘、斐波那契等）
    ├── DateUtils.java         # 日期工具（格式化、计算天数差等）
    └── StringUtils.java       # 字符串工具（反转、回文、脱敏等）
```

## 工具类 Demo

项目包含一些常用的 Java 工具类，可作为学习参考：

### SortUtils - 排序算法
- 冒泡排序 (bubbleSort)
- 选择排序 (selectionSort)
- 快速排序 (quickSort)
- 二分查找 (binarySearch)

### MathUtils - 数学工具
- 质数判断 (isPrime)
- 阶乘 (factorial)
- 斐波那契数列 (fibonacci)
- 最大公约数/最小公倍数 (gcd/lcm)
- 回文数判断 (isPalindrome)
- 数组求和、最大值、最小值、平均值

### DateUtils - 日期工具
- 日期格式化 (formatDate)
- 日期解析 (parseDate)
- 天数差计算 (daysBetween)
- 星期几获取 (getWeekDayCN)
- 是否今天判断 (isToday)

### StringUtils - 字符串工具
- 空判断 (isEmpty/isBlank)
- 字符串反转 (reverse)
- 回文字符串判断 (isPalindrome)
- 手机号/邮箱脱敏 (maskPhone/maskEmail)
- 数字转中文 (numberToChinese)

## 开发环境

- Android Studio
- 语言：Java
- 最低 SDK：根据项目配置
- 构建工具：Gradle

## 安装运行

1. 使用 Android Studio 打开项目
2. 等待 Gradle 同步完成
3. 连接 Android 设备或启动模拟器
4. 点击运行按钮

## 游戏玩法

1. 输入玩家 ID 开始游戏
2. 通过上下左右滑动移动方块
3. 相同数字的方块碰撞会合并
4. 目标是合成 2048
5. 游戏结束后可查看排行榜

## 提交记录

- 初始提交：完整的 2048 游戏项目
- 添加工具类：排序、数学、日期、字符串等常用工具方法

## License

个人学习项目，仅供参考学习。
