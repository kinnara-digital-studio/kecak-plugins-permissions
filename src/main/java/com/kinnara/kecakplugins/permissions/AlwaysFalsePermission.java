package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.form.model.DefaultFormPermission;

public class AlwaysFalsePermission extends DefaultFormPermission {
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
