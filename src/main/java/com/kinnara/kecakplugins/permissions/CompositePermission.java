package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.commons.util.LogUtil;

/**
 * @author aristo
 * Execute 2 permission
 */
public class CompositePermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        boolean debug = "true".equalsIgnoreCase(getPropertyString("debug"));

        // validate using permission1
        UserviewPermission permission1 = Utilities.getPermissionObject(this, "permission1");
        UserviewPermission permission2 = Utilities.getPermissionObject(this, "permission2");

        if("and".equalsIgnoreCase(getPropertyString("condition"))) {
            return isAuthorized(permission1) && isAuthorized(permission2);
        } else {
            return isAuthorized(permission1) || isAuthorized(permission2);
        }
    }

    protected boolean isAuthorized(UserviewPermission permission) {
        return permission == null || permission.isAuthorize();
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
}
