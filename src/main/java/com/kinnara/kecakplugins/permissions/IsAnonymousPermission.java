package com.kinnara.kecakplugins.permissions;

import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.workflow.util.WorkflowUtil;

public class IsAnonymousPermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        return WorkflowUtil.isCurrentUserAnonymous();
    }

    @Override
    public String getName() {
        return "Is Anonymous";
    }

    @Override
    public String getVersion() {
        return getClass().getPackage().getImplementationVersion();
    }

    @Override
    public String getDescription() {
        return "Kecak Plugins; Artifact ID : " + getClass().getPackage().getImplementationTitle();
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
