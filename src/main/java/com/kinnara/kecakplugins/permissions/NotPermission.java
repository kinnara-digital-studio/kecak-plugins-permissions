package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;


/**
 * @author aristo
 * Negation of particular permission
 */
public class NotPermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        UserviewPermission plugin = Utilities.getPermissionObject(this, "permission");
        if(plugin != null)
            return !plugin.isAuthorize();

        return true;
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
