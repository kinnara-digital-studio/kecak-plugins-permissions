package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.plugin.base.Plugin;
import org.joget.plugin.base.PluginManager;
import org.joget.plugin.property.model.PropertyEditable;

import java.util.Map;


/**
 * @author aristo
 * Negation of particular permission
 */
public class NotPermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        Map<String, Object> permission = (Map<String, Object>)getProperty("permission");
        String className = (String) permission.get("className");
        Map<String, Object> properties = permission == null ? null : (Map<String, Object>)permission.get("properties");

        PluginManager pluginManager = (PluginManager) AppUtil.getApplicationContext().getBean("pluginManager");
        Plugin plugin = pluginManager.getPlugin(className);
        if(properties != null)
            ((PropertyEditable)plugin).setProperties(properties);

        ((UserviewPermission)plugin).setCurrentUser(getCurrentUser());
        ((UserviewPermission)plugin).setFormData(getFormData());

        return !((UserviewPermission)plugin).isAuthorize();
    }

    @Override
    public String getName() {
        return AppPluginUtil.getMessage("notPermission.title", getClassName(), "/messages/NotPermission");
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
        return AppUtil.readPluginResource(getClassName(), "/properties/NotPermission.json", null, true, "/messages/NotPermission");
    }
}
