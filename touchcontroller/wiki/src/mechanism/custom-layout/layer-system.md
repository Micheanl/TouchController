# TouchController 的图层系统

> 参见：[自定义图层条件](custom-conditions.md)

## 介绍

<!-- ANCHOR: p1 -->
图层系统来自 [#123](https://github.com/TouchController/TouchController/discussions/123) 以及相关的各种 issue，其用途是在不同的情况下显示不同的控件，例如飞行、划船时。
<!-- ANCHOR_END: p1 -->

## 机制

<!-- ANCHOR: p2 -->
- 每个图层都可以配置显示条件，如果多个图层同时满足条件会叠加图层。
- TouchController 的控件必须在图层中创建。
<!-- ANCHOR_END: p2 -->

### 图层编辑

<!-- ANCHOR: p3 -->
是一个子页面，可以编辑图层名称和图层条件。可以在右侧的标签页中将图层条件添加到图层中，目前有三个标签页：

- 预置条件：TouchController v0.2.1-beta11 及以前仅可用这种预置条件，包含游泳中、飞行中、潜水中、允许飞行等等。
- 手持物品：包含类似[物品列表](../../gui/config-screen/sub-pages/item-list)的默认物品表及原版物品栏，点击一个物品可以添加手持此物品的条件。
- 乘坐实体：包含一个实体列表，几乎包含游戏中的所有实体类型。
- 选择实体：同上
- 自定义条件：参见[自定义图层条件](custom-conditions.md)。
<!-- ANCHOR_END: p3 -->

### 条件状态

<!-- ANCHOR: p4 -->
每个条件拥有以下几种状态：

- 从不：条件满足时不显示图层
- 要求：条件满足时显示图层
- 必须：所有“必要”条件满足时显示图层
<!-- ANCHOR_END: p4 -->

