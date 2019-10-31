package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.plugin.base.PluginManager;

import java.util.Map;

public class Utilities {
    public static UserviewPermission getPermissionObject(UserviewPermission plugin, String permissionPropertyName) {
        PluginManager pluginManager = (PluginManager) AppUtil.getApplicationContext().getBean("pluginManager");
        Map<String, Object> propertyPermission = (Map<String, Object>)plugin.getProperty(permissionPropertyName);
        if(propertyPermission == null)
            return null;

        String className = (String)propertyPermission.get("className");
        UserviewPermission permission = (UserviewPermission) pluginManager.getPlugin(className);
        if(permission == null)
            return null;

        Map<String, Object> properties = (Map<String, Object>)propertyPermission.get("properties");
        if(properties != null)
            permission.setProperties(properties);

        permission.setFormData(plugin.getFormData());
        permission.setCurrentUser(plugin.getCurrentUser());

        return permission;
    }
}
