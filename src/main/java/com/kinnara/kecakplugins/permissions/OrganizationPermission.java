package com.kinnara.kecakplugins.permissions;

import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;

/**
 * @author aristo
 *
 * Get permission for current organization
 */
public class OrganizationPermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        return false;
    }

    @Override
    public String getName() {
        return getLabel() + getVersion();
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
        return "Organization Permission";
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return null;
    }
}
