package com.kinnarastudio.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginManager;

import java.util.ResourceBundle;

/**
 * @author aristo
 *
 * For testing and debugging purpose
 *
 */
public class DebugPermission extends UserviewPermission implements FormPermission {
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
        PluginManager pluginManager = (PluginManager) AppUtil.getApplicationContext().getBean("pluginManager");
        ResourceBundle resourceBundle = pluginManager.getPluginMessageBundle(getClassName(), "/messages/BuildNumber");
        String buildNumber = resourceBundle.getString("buildNumber");
        return buildNumber;
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
