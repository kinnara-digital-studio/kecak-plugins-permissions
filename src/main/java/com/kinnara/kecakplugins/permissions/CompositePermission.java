package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginManager;

import javax.rmi.CORBA.Util;
import java.util.Map;

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

        if(debug) {
            LogUtil.info(getClassName(), "Condition ["+getPropertyString("condition")+"] "
                    + "validator 1 class [" + permission1.getClassName() + "] result [" + permission1.isAuthorize() + "] "
                    + "validator 2 class [" + permission2.getClassName() + "] result [" + permission2.isAuthorize() + "]");
        }

        if("and".equalsIgnoreCase(getPropertyString("condition"))) {
            return permission1.isAuthorize() && permission2.isAuthorize();
        } else {
            return permission1.isAuthorize() || permission2.isAuthorize();
        }
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
