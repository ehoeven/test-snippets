package fr.an.testspringbootfastcl.classloader;


public enum ClassLoaderPreloadMode {
    DEFAULT,
    RECORD_CLASSNAMES,
    RECORD_BINARY_CLASS_PRELOAD,
    PRELOAD_BY_NAME_PARALLEL,
    PRELOAD_BY_NAME_PARALLEL_BLOCK,
    PRELOAD_BINARY_CLASS_ASYNC,
    PRELOAD_BINARY_CLASS_BLOCK

}