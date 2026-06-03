package top.fifthlight.fabazel.devlaunchwrapper;

public interface AttributeEnvironment {
    <K extends ContextAttributeKey<T>, T> T getAttribute(K key);

    <K extends ContextAttributeKey<T>, T> void putAttribute(K key, T value);
}
