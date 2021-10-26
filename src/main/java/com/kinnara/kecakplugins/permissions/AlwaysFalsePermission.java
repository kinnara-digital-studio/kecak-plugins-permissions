package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;

public class AlwaysFalsePermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        return false;
    }

    @Override
    public String getName() {
        return AppPluginUtil.getMessage("alwaysFalsePermission.title", getClassName(), "/messages/AlwaysFalsePermission");
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
        return "";
    }
}
