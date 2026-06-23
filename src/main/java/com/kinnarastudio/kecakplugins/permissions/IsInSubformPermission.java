package com.kinnarastudio.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.service.FormUtil;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginManager;
import org.kecak.apps.form.model.FormPermissionDefault;

import java.util.Optional;
import java.util.ResourceBundle;

public class IsInSubformPermission extends FormPermissionDefault {
    public final static String LABEL = "Is In Subform Permission";

    @Override
    public boolean isAuthorize() {
        LogUtil.info(getClassName(), "isAuthorize element [" + getElement() + "]");
        return Optional.ofNullable(getElement())
                .map(FormUtil::findRootForm)
                .map(f -> {
                    LogUtil.info(getClassName(), "root form [" + f.getProperty("id") + "]");
                    return f;
                })
                .map(Element::getParent)
                .map(f -> {
                    LogUtil.info(getClassName(), "element [" + f.getProperty("id") + "]");
                    return f;
                })
                .isPresent();
    }

    @Override
    public String getName() {
        return LABEL;
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
        return LABEL;
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
