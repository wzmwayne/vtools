# AGENTS.md — Scene (vtools)

## 项目概述

Android Root 系统调优应用，Kotlin + Java + C++ (JNI) + Shell 脚本。
原作者 helloklf，已闭源收费；当前 fork（`wzmwayne/vtools`），目标抹除原作者痕迹并独立维护。

## 构建与验证（重要）

⚠ **不要本地编译**。Gradle 6.5 / AGP 4.0.1 仅支持 JDK ≤ 14，本机只有 JDK 21+，本地无法构建。
验证一律通过 GitHub Actions 工作流编译（`gh workflow run` 或推送 master 触发）：

- `.github/workflows/debug.yml` — **debug 包 CI**，testkey 签名，含 NDK / Gradle / SDK 组件全量缓存，产出 `app/build/outputs/apk/debug/app-debug.apk`
- `.github/workflows/android.yml`、`Scene5.yml` — release 构建（沿用旧 helloklf deploy 逻辑，待清理）

## 测试 / Lint

- `./gradlew test` — 全为空桩，无实际覆盖
- 无 ktlint / detekt / spotless / editorconfig 配置

## 模块结构

| 模块 | 类型 | 职责 |
|------|------|------|
| `app` | application | 主应用 |
| `common` | library | Shell 执行、文件操作、公共 UI |
| `krscript` | library | KR-Script 脚本引擎（XML 配置 + Shell 执行） |

## 包名（已全量重命名）

- applicationId / Java 包根：`com.wzmwayne.scene`（原 `com.omarea.vtools`，**`vtools` 段已删除**，不要写成 `com.wzmwayne.scene.vtools`）
- 子包保持原有后缀：`com.wzmwayne.scene.activities`、`com.wzmwayne.scene.scene_mode`、`com.wzmwayne.scene.xposed` 等
- 模块命名空间：`com.wzmwayne.scene.common`、`com.wzmwayne.scene.krscript`
- Manifest `package` 与 `applicationId` 均为 `com.wzmwayne.scene`

## 关键入口

- Application: `app/.../com/wzmwayne/scene/Scene.kt`
- 启动页: `.../scene/activities/ActivityStartSplash.kt`
- 主界面: `.../scene/activities/ActivityMain.kt`
- 无障碍服务: `.../scene/AccessibilityScenceMode.kt`
- Xposed 模块: `.../scene/xposed/XposedInterface.java`（入口声明在 `assets/xposed_init`）
- 开机服务: `.../scene/services/BootService.kt`
- EventBus: `.../scene/data/EventBus.kt`（自实现，非第三方库）
- CPU 配置: `assets/powercfg/`（多 SoC：sdm845/kona/lahaina/mt6873 等）
- 打赏与作者联系方式已全部移除（AlipayDonate、FragmentDonate、paypal.me、vtools.omarea.com 链接、QQ 反馈链接、`Created by helloklf` 注释）

## 架构要点

- 无 DI 框架，无 ViewModel/LiveData/Repository 模式
- 自定义 EventBus；Data Binding 与 `kotlinx.android.synthetic` 并用
- Xposed 模块打包在同一 APK（`compileOnly` 依赖）
- Shell 命令通过 `common` 模块封装类执行（`KeepShell`/`KeepShellAsync`/`KeepShellPublic`），不要直接 `Runtime.exec`
- KR-Script 引擎读取 `assets/kr-script/` 下的 XML/Shell 配置

## 签名

- debug：`app/testkey.jks`（通用 testkey，alias `androiddebugkey`，storepass/keypass 均为 `android`）
- release / release_mini：仍用 `app/omoarea-test.jks`（alias `omarea.com`）— 待替换，属敏感材料

## 仍存的原作者痕迹（待处理）

- 外部 app 引用（**有意保留**，改名会破坏功能）：
  - `com.omarea.vaddin` — 预编译配套 app `others/xposed-addin.apk` 的包名，AIDL/ComponentName 绑定依赖它
  - `com.omarea.vboot` / `com.omarea.gesture` / `com.omarea.filter` — 外部 app 的白名单/启动逻辑
- CI release 工作流 deploy 到 `helloklf/vtools-dev-apks` 的逻辑
- `ActivityAddinOnline.kt` 中 addin 下载域名白名单（`vtools.omarea.com`、`vtools.oss-cn-beijing.aliyuncs.com`）— 功能依赖
- `docs/` 站点（`CNAME: vtools.omarea.com`）及打包 JS 内的作者路径
- 在线脚本 URL 已改指 `wzmwayne.github.io/vtools`，需实际部署生效
- `swap-controller/adb_process/steps.txt` 中的作者机器路径

## 代码风格

- 无格式化工具强制执行，遵循现有代码风格即可
- Shell 命令统一走 `common` 模块封装类，不要直接调 `Runtime.exec`