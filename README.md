# GenRN - 随机数生成器应用

[![Download APK](https://img.shields.io/badge/Download-Android_APK-brightgreen)](https://github.com/ff66ccff/GenRN/releases)
[![Platform](https://img.shields.io/badge/Platform-Android-blue)](https://www.android.com/)
[![Material Design](https://img.shields.io/badge/Design-Material-blueviolet)](https://material.io/)

GenRN是一款专为Android设计的随机数生成器应用，采用Material Design设计语言，专注于为课堂抽学号等场景提供便捷解决方案。

## 快速下载

点击下方按钮，前往最新版本的发布页面下载APK文件：

[**立即下载 GenRN APK**](https://github.com/ff66ccff/GenRN/releases)

## 安装说明

1. 从Releases页面下载最新版APK文件
2. 在Android设备上启用"未知来源"安装权限
3. 找到下载的APK文件并点击安装
4. 安装完成后即可打开应用

> **注意**：应用不需要特殊权限，安装后可直接使用

## 功能特性

### 核心功能

- **批量数字生成** - 支持同时生成多个不重复的随机数
- **智能重复检测** - 确保生成的数字不重复，符合超几何分布
- **范围自定义** - 自由设置随机数的最小值和最大值
- **数字预选功能** - 在数字池中标记特定数字作为必选项

### 使用场景

- 课堂随机点名
- 抽奖活动
- 随机分组
- 游戏随机选择
- 任何需要随机数的场景

### 应用亮点

- **实时统计** - 显示已生成、总数和剩余数字数量
- **历史记录** - 自动保存所有生成记录
- **数字池管理** - 可视化查看所有数字状态
- **深色模式** - 支持夜间使用的深色主题
- **响应式设计** - 适配各种屏幕尺寸

## 使用指南

### 基本使用
1. 设置最小值、最大值和生成数量
2. 点击"生成随机数"按钮
3. 查看生成结果和统计信息

### 高级功能
- **预选数字**：在数字池中点击未生成的数字将其标记为必选
- **重置数字池**：点击重置按钮清除所有生成记录
- **切换主题**：点击右上角图标切换深色/浅色模式

## 技术栈

- **开发环境**：Android Studio
- **核心语言**：Kotlin&HTML
- **设计语言**：Material Design 3
- **架构模式**：MVVM (Model-View-ViewModel)
- **存储**：本地数据持久化

## 特别鸣谢

在开发过程中，以下工具和服务为本项目提供了极大帮助：

- **ChatGPT** 和 **DeepSeek**：辅助编写了大量代码
- **Android Studio**：提供了强大的开发环境
- **Material Design**：提供了优秀的设计指南和组件
- **开源社区**：各种开源库和解决方案的支持

## 常见问题

**Q：为什么需要允许未知来源安装？**  
A：因为应用未上架Google Play商店，直接从APK安装需要此权限。

**Q：生成的随机数是否真正随机？**  
A：使用Android安全随机数生成器，符合密码学安全标准。

**Q：会收集用户数据吗？**  
A：不会，所有数据仅存储在本地设备上。

## 贡献与反馈

欢迎提交Issue或Pull Request：
- 报告问题：[Issues页面](https://github.com/ff66ccff/GenRN/issues)
- 贡献代码：Fork仓库并提交Pull Request
- 功能建议：通过Issues提出您的想法

