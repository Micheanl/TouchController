touchcontroller_fabric_libraries = {
    "@maven//:androidx_compose_runtime_runtime_saveable_desktop": "androidx_compose_runtime_runtime_saveable_desktop:1.10.2",
    "@maven//:androidx_savedstate_savedstate_desktop": "androidx_savedstate_savedstate_desktop:1.3.2",
    "@maven//:androidx_savedstate_savedstate_compose_desktop": "androidx_savedstate_savedstate_compose_desktop:1.3.2",
    "@maven//:androidx_lifecycle_lifecycle_common_jvm": "androidx_lifecycle_lifecycle_common_jvm:2.9.4",
    "@maven//:androidx_lifecycle_lifecycle_runtime_compose_desktop": "androidx_lifecycle_lifecycle_runtime_compose_desktop:2.9.4",
    "@maven//:org_jetbrains_kotlinx_kotlinx_collections_immutable_jvm": "org_jetbrains_kotlinx_kotlinx_collections_immutable_jvm:0.4.0",
    "@maven//:cafe_adriel_voyager_voyager_core_desktop": "cafe_adriel_voyager_voyager_core_desktop:1.1.0-beta03",
    "@maven//:cafe_adriel_voyager_voyager_navigator_desktop": "cafe_adriel_voyager_voyager_navigator_desktop:1.1.0-beta03",
    "@maven//:cafe_adriel_voyager_voyager_screenmodel_desktop": "cafe_adriel_voyager_voyager_screenmodel_desktop:1.1.0-beta03",
}

touchcontroller_unified_deps = {
    "androidx_compose_runtime_runtime_saveable_desktop": "@maven//:androidx_compose_runtime_runtime_saveable_desktop",
    "androidx_savedstate_savedstate_desktop": "@maven//:androidx_savedstate_savedstate_desktop",
    "androidx_savedstate_savedstate_compose_desktop": "@maven//:androidx_savedstate_savedstate_compose_desktop",
    "androidx_lifecycle_lifecycle_common_jvm": "@maven//:androidx_lifecycle_lifecycle_common_jvm",
    "androidx_lifecycle_lifecycle_runtime_compose_desktop": "@maven//:androidx_lifecycle_lifecycle_runtime_compose_desktop",
    "org_jetbrains_kotlinx_kotlinx_collections_immutable_jvm": "@maven//:org_jetbrains_kotlinx_kotlinx_collections_immutable_jvm",
    "cafe_adriel_voyager_voyager_core_desktop": "@maven//:cafe_adriel_voyager_voyager_core_desktop",
    "cafe_adriel_voyager_voyager_navigator_desktop": "@maven//:cafe_adriel_voyager_voyager_navigator_desktop",
    "cafe_adriel_voyager_voyager_screenmodel_desktop": "@maven//:cafe_adriel_voyager_voyager_screenmodel_desktop",
}

touchcontroller_unified_neoforge = {modid: ["common"] for modid in touchcontroller_unified_deps.keys()}

touchcontroller_unified_fabric = {
    "androidx_compose_runtime_runtime_saveable_desktop": "1.10.2",
    "androidx_savedstate_savedstate_desktop": "1.3.2",
    "androidx_savedstate_savedstate_compose_desktop": "1.3.2",
    "androidx_lifecycle_lifecycle_common_jvm": "2.9.4",
    "androidx_lifecycle_lifecycle_runtime_compose_desktop": "2.9.4",
    "org_jetbrains_kotlinx_kotlinx_collections_immutable_jvm": "0.4.0",
    "cafe_adriel_voyager_voyager_core_desktop": "1.1.0-beta03",
    "cafe_adriel_voyager_voyager_navigator_desktop": "1.1.0-beta03",
    "cafe_adriel_voyager_voyager_screenmodel_desktop": "1.1.0-beta03",
}
