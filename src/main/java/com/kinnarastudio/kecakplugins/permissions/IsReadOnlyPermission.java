package com.kinnarastudio.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.form.service.FormUtil;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.plugin.base.PluginManager;
import org.kecak.apps.form.model.FormPermissionDefault;

import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author aristo
 * Supposed to be used in form
 *
 * Return true if current form element is readonly
 *
 * Return true if placed in non form
 *
 */
public class IsReadOnlyPermission extends FormPermissionDefault {
    @Override
    public boolean isAuthorize() {
        return Optional.ofNullable(getElement())
                .map(e -> FormUtil.isReadonly(e, getFormData()))
                .orElse(true);
    }

    @Override
    public String getName() {
        return "Is Read-only Permission";
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
