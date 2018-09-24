package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.plugin.base.PluginManager;

import java.util.Map;

/**
 * @author aristo
 * Execute 2 permission
 */
public class CompositePermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        // validate using validator1
        UserviewPermission validator1 = getPermissionObject("permission1");
        UserviewPermission validator2 = getPermissionObject("permission2");

        if("and".equalsIgnoreCase(getPropertyString("condition"))) {
            return validator1.isAuthorize() && validator2.isAuthorize();
        } else {
            return validator1.isAuthorize() || validator2.isAuthorize();
        }
    }

    @Override
    public String getName() {
        return AppPluginUtil.getMessage("compositePermission.title", getClassName(), "/messages/CompositePermission");
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

    private UserviewPermission getPermissionObject(String permissionPropertyName) {
        PluginManager pluginManager = (PluginManager)AppUtil.getApplicationContext().getBean("pluginManager");
        Map<String, Object> propertyPermission = (Map<String, Object>)getProperty(permissionPropertyName);

        String className = (String)propertyPermission.get("className");
        Map<String, Object> properties = (Map<String, Object>)propertyPermission.get("properties");

        UserviewPermission permission = (UserviewPermission) pluginManager.getPlugin(className);
        if(properties != null)
            permission.setProperties(properties);

        permission.setFormData(getFormData());
        permission.setCurrentUser(getCurrentUser());

        return permission;
    }
}
