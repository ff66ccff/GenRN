
# GenRN - 基于 Kotlin 的随机数生成器

[![Download](https://img.shields.io/badge/Download-Latest%20Release-blue)](https://github.com/ff66ccff/GenRN/releases)
[Star History](https://api.star-history.com/svg?repos=ff66ccff/GenRN)

GenRN 是一个基于 Kotlin 和 Material Design 构建的随机数生成器，旨在为课堂抽学号提供简便的解决方案。

## 目录

- [快速下载](#快速下载)
- [项目介绍](#项目介绍)
- [功能特性](#功能特性)
- [安装与使用](#安装与使用)
- [贡献指南](#贡献指南)
- [特别鸣谢](#特别鸣谢)

---

## 快速下载

点击下方按钮，立即前往最新版本的发布页面：

[![立即下载 GenRN](https://img.shields.io/badge/Download-Latest%20Release-blue)](https://github.com/ff66ccff/GenRN/releases)

---

## 项目介绍

GenRN 的开发初衷是为课堂上的抽学号提供自动化和随机化工具。通过使用 Kotlin 语言和现代的 Material Design UI 设计，它提供了一种直观、简单的用户体验。

该项目的主要功能是生成多个随机数，特别适用于课堂上随机抽取学号。每个随机数的生成符合**超几何分布**，并在每个位置上独立检测重复性，确保公平性。

---

## 功能特性

- **批量随机数生成**  
  允许一次生成多个学号，满足需要同时抽取多名学生的需求。

- **位置独立的重复检测**  
  每个位置的数字生成相互独立，确保各个数字均满足超几何分布要求，避免重复。

---

## 安装与使用

### 环境要求

- **Kotlin 1.5+**
- **Android Studio 4.0+** 或者其他支持 Kotlin 的 IDE
- **Gradle 6.5+**

### 安装步骤

1. 克隆项目到本地：
   ```bash
   git clone https://github.com/ff66ccff/GenRN.git
   ```

2. 打开项目并通过 Gradle 进行构建：
   ```bash
   ./gradlew build
   ```

3. 运行应用或通过 Android Studio 部署到设备。

### 使用指南

1. 打开应用，选择要生成的随机数数量。
2. 点击“生成”，即可获得一组独立、符合超几何分布的学号。
3. 如需多次抽取，可以选择“重新生成”以避免重复。

---

## 贡献指南

欢迎任何形式的贡献！请按照以下步骤进行贡献：

1. Fork 本仓库。
2. 创建一个新的分支：
   ```bash
   git checkout -b feature-新功能
   ```
3. 提交你的更改：
   ```bash
   git commit -m "新增新功能"
   ```
4. Push 到你的分支：
   ```bash
   git push origin feature-新功能
   ```
5. 提交 Pull Request，并描述你的更改。

我们期待你的贡献！

---

## 特别鸣谢

在开发过程中，以下工具和服务为本项目提供了巨大的帮助：

- **ChatGPT** 和 **GitHub Copilot**：通过 AI 提供代码建议和编写支持。
- **Android Studio**：提供了稳定的开发环境。
- **Mihomo Party**：提供了网络支持。

感谢这些工具和服务的支持，帮助我们顺利完成该项目！

---

如果你觉得这个项目对你有帮助，请给我们一个⭐Star！感谢你的支持！
