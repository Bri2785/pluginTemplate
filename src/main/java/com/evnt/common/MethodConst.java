package com.evnt.common;

import com.unigrative.plugins.GenericPlugin;

enum MethodConstTest {

    UPDATE_SEED(Module.getModule("Seed"), "updateSeed"),
    RUN_DATA_EXPORT_QUERY(Module.DATA_EXPORT, "runQuery"),
    RUN_DATA_EXPORT(Module.DATA_EXPORT, "runDataExport");

    private Module module;
    private String methodName;

    private MethodConstTest(final Module module, final String methodName) {
        this.module = module;
        this.methodName = methodName;
    }

    public Module getModule() {
        return this.module;
    }

    public String getModuleName() {
        return this.module.getName();
    }

    public String getMethodName() {
        return this.methodName;
    }
}
