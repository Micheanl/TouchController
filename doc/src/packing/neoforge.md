# NeoForge 打包

NeoForge 的 JarJar 和 Fabric 的 JiJ 机制不同：JarJar 并不存在一个静默跳过机制，它总是会被加载进游戏，因此 Fabric 的方案行不通。
不过我们可以用一些黑魔法：通过 FML 的扩展点 `IDependencyLocator`，自己实现一个 Jar-in-Jar 机制，从而变相实现这个功能。

## 最终 JAR 结构

```
touchcontroller-neoforge.jar
├── META-INF/neoforge.mods.toml                                   ← 供启动器检测
├── META-INF/services/
│   ├── net.neoforged.neoforgespi.locating.IDependencyLocator     ← Early Service SPI
│   └── net.neoforged.neoforgespi.locating.IModFileReader         ← Early Service SPI
├── META-INF/jars/
│   ├── touchcontroller-common.jar                                ← 公共代码（GAMELIBRARY）
│   ├── touchcontroller-1.21.1.jar                                ← MC 1.21.1 特定代码（MOD）
│   ├── touchcontroller-1.21.10.jar                               ← MC 1.21.10 特定代码（MOD）
│   ├── multijar-neoforge-manifest.json                           ← 加载器清单
│   └── ...
└── NeoV3Locator.class                                            ← 自举加载器
```

外层 JAR 实际上不是一个 NeoForge 模组：它的作用只是加载嵌套的 JAR 文件。
