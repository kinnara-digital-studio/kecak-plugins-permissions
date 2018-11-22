package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.Plugin;
import org.joget.plugin.base.PluginManager;
import org.joget.workflow.model.WorkflowActivity;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.model.service.WorkflowManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ActivityPermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        final FormData formData = getFormData();
        boolean nonAssignment = "true".equalsIgnoreCase(getPropertyString("nonAssignment"));
        if(formData == null) {
            return nonAssignment;
        }

        WorkflowManager workflowManager = (WorkflowManager) AppUtil.getApplicationContext().getBean("workflowManager");
        WorkflowAssignment assignment = workflowManager.getAssignment(formData.getActivityId());
        if(assignment == null) {
            return nonAssignment;
        }

        return Arrays.stream(getPropertyString("activities").split(";"))
                .map(String::trim)
                .anyMatch(s -> s.equals(assignment.getActivityDefId()));
    }

    @Override
    public String getName() {
        return AppPluginUtil.getMessage("activityPermission.title", getClassName(), "/messages/ActivityPermission");
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
        AppDefinition appDefinition = AppUtil.getCurrentAppDefinition();
        JSONObject activitiesProperty = new JSONObject();
        try {
            activitiesProperty.put("name", "activities");
            activitiesProperty.put("label", "@@activityPermission.activities@@");
            if(appDefinition != null && isClassInstalled("com.kinnara.kecakplugins.workflowcomponentoptionsbinder.ActivityOptionsBinder")) {
                String appId = appDefinition.getAppId();
                String appVersion = appDefinition.getVersion().toString();
                activitiesProperty.put("type", "multiselect");
                activitiesProperty.put("options_ajax","[CONTEXT_PATH]/web/json/app[APP_PATH]/plugin/com.kinnara.kecakplugins.workflowcomponentoptionsbinder.ActivityOptionsBinder/service?appId="+appId + "&appVersion=" + appVersion + "&type=" + WorkflowActivity.TYPE_NORMAL);
            } else {
                activitiesProperty.put("type", "textfield");
            }
        } catch (JSONException ignored) { }

        String[] args = { activitiesProperty.toString().replaceAll("\"", "'") };
        return AppUtil.readPluginResource(getClassName(), "/properties/ActivityPermission.json", args, false, "/messages/ActivityPermission");
    }

    private boolean isClassInstalled(String className) {
        PluginManager pluginManager = (PluginManager) AppUtil.getApplicationContext().getBean("pluginManager");
        Plugin plugin = pluginManager.getPlugin(className);
        return plugin != null;
    }
}
