package com.kinnarastudio.kecakplugins.permissions;

import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.Permission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginManager;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author aristo
 * Execute 2 permission
 */
public class CompositePermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        final PluginManager pluginManager = (PluginManager) AppUtil.getApplicationContext().getBean("pluginManager");
        final boolean debug = isDebug();

        // validate using permission1
        Permission permission1 = pluginManager.getPlugin((Map<String, Object>) getProperty("permission1"));
        Permission permission2 = pluginManager.getPlugin((Map<String, Object>) getProperty("permission2"));

        final String condition = getCondition();

        if (debug) {
            LogUtil.info(getClassName(), "Condition [" + condition + "] "
                    + "validator 1 class [" + (permission1 == null ? "" : permission1.getClassName()) + "] result [" + isAuthorized(permission1) + "] "
                    + "validator 2 class [" + (permission2 == null ? "" : permission2.getClassName()) + "] result [" + isAuthorized(permission2) + "]");
        }

        if ("and".equalsIgnoreCase(getPropertyString("condition"))) {
            return isAuthorized(permission1) && isAuthorized(permission2);
        } else {
            return isAuthorized(permission1) || isAuthorized(permission2);
        }
    }

    protected boolean isAuthorized(Permission permission) {
        return permission == null || permission.isAuthorize();
    }

    @Override
    public String getName() {
        return AppPluginUtil.getMessage("compositePermission.title", getClassName(), "/messages/CompositePermission");
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
        return getName();
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClassName(), "/properties/CompositePermission.json", null, false, "/messages/CompositePermission");
    }

    protected boolean isDebug() {
        return "true".equalsIgnoreCase(getPropertyString("debug"));
    }

    protected String getCondition() {
        return getPropertyString("condition");
    }
}
