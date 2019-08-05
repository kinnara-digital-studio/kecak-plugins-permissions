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
        Map<String, Object> properties = (Map<String, Object>)propertyPermission.get("properties");

        UserviewPermission permission = (UserviewPermission) pluginManager.getPlugin(className);
        if(permission == null)
            return null;

        if(properties != null)
            permission.setProperties(properties);

        if(permission instanceof FormPermission && plugin instanceof FormPermission) {
            ((FormPermission)permission).setFormData(((FormPermission)plugin).getFormData());
            ((FormPermission)permission).setFormData(((FormPermission)plugin).getFormData());
        }


        permission.setCurrentUser(plugin.getCurrentUser());

        return permission;
    }
}
