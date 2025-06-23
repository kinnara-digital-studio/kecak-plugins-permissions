package com.kinnarastudio.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.plugin.base.PluginManager;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author aristo
 *
 * Returns true when contains no data (form data or primary key is NULL)
 */
public class OnDataCreationPermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        return !Optional.ofNullable(getFormData())
                .map(FormData::getPrimaryKeyValue)
                .isPresent();
    }

    @Override
    public String getName() {
        return "On Data Creation Permission";
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
        return "";
    }
}
