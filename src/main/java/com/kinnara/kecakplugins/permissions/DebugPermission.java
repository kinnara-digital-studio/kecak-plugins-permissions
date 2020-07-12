package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.DefaultFormPermission;
import org.joget.commons.util.LogUtil;

/**
 * @author aristo
 *
 * For testing and debugging purpose
 *
 */
public class DebugPermission extends DefaultFormPermission {
    @Override
    public boolean isAuthorize() {
        LogUtil.info(getClass().getName(), "isAuthorize [" + getPropertyParameter() + "]");
        return !"false".equalsIgnoreCase(getPropertyParameter());
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public String getVersion() {
        return getClass().getPackage().getImplementationVersion();
    }

    @Override
    public String getDescription() {
        return getClass().getPackage().getImplementationTitle();
    }

    @Override
    public String getLabel() {
        return "Debug Permission";
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClass().getName(), "/properties/DebugPermission.json", null, false, null);
    }

    private String getPropertyParameter() {
        return AppUtil.processHashVariable(getPropertyString("parameter"), null, null, null);
    }
}
