package com.kinnarastudio.kecakplugins.permissions;

import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginManager;
import org.kecak.apps.form.model.FormPermissionDefault;

import java.util.ResourceBundle;


/**
 * @author aristo
 * Negation of particular permission
 */
public class NotPermission extends FormPermissionDefault {
    @Override
    public boolean isAuthorize() {
        LogUtil.info(getClassName(), "isAuthorize element [" + getElement() + "]");

        UserviewPermission plugin = Utilities.getPermissionObject(this, "permission");
        if(plugin != null) {
            LogUtil.info(getClassName(), "isAuthorize plugin [" + plugin + "]");

            if(plugin instanceof FormPermission) {
                FormPermission formPermission = (FormPermission) plugin;
                formPermission.setElement(getElement());
                formPermission.setFormData(getFormData());
            }
            return !plugin.isAuthorize();
        }

        return true;
    }

    @Override
    public String getName() {
        return AppPluginUtil.getMessage("notPermission.title", getClassName(), "/messages/NotPermission");
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
        return AppUtil.readPluginResource(getClassName(), "/properties/NotPermission.json", null, true, "/messages/NotPermission");
    }
}
