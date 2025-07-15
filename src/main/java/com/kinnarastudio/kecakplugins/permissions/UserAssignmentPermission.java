package com.kinnarastudio.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormData;
import org.joget.plugin.base.PluginManager;
import org.joget.workflow.model.service.WorkflowManager;
import org.kecak.apps.form.model.FormPermissionDefault;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * User Assignment Permission
 *
 * Supported in form element, check if current form data is current user's pending assignment
 *
 */
public class UserAssignmentPermission extends FormPermissionDefault {
    public final static String LABEL = "User Assignment Permission";

    @Override
    public boolean isAuthorize() {
        final WorkflowManager workflowManager = (WorkflowManager) AppUtil.getApplicationContext().getBean("workflowManager");
        return Optional.ofNullable(getFormData())
                .map(FormData::getPrimaryKeyValue)
                .map(s -> workflowManager.getAssignmentByRecordId(s, null, null, null))
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
