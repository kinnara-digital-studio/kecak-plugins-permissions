package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.model.PackageDefinition;
import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.DefaultFormPermission;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.Plugin;
import org.joget.plugin.base.PluginManager;
import org.joget.plugin.base.PluginWebSupport;
import org.joget.workflow.model.WorkflowActivity;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.model.WorkflowProcess;
import org.joget.workflow.model.service.WorkflowManager;
import org.joget.workflow.util.WorkflowUtil;
import org.json.JSONArray;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityPermission extends DefaultFormPermission implements PluginWebSupport {
    @Override
    public boolean isAuthorize() {
        final FormData formData = getFormData();
        boolean nonAssignment = "true".equalsIgnoreCase(getPropertyString("nonAssignment"));
        if (formData == null) {
            return nonAssignment;
        }

        WorkflowManager workflowManager = (WorkflowManager) AppUtil.getApplicationContext().getBean("workflowManager");
        WorkflowAssignment assignment = workflowManager.getAssignment(formData.getActivityId());
        if (assignment == null) {
            return nonAssignment;
        }

        LogUtil.info(getClassName(), "processId ["+assignment.getProcessDefId()+"] activityId ["+assignment.getActivityDefId()+"]");

        String processId = getPropertyString("processId");

        Matcher processIdMatcher = Pattern.compile("(?<=#)" + processId + "$").matcher(assignment.getProcessDefId());

        // processid matched and activities matched;
        return processIdMatcher.find() && Arrays.stream(getPropertyString("activities").split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .anyMatch(activityId -> activityId.equals(assignment.getActivityDefId()));
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
//        AppDefinition appDefinition = AppUtil.getCurrentAppDefinition();
//        JSONObject activitiesProperty = new JSONObject();
//        try {
//            activitiesProperty.put("name", "activities");
//            activitiesProperty.put("label", "@@activityPermission.activities@@");
//            if(appDefinition != null && isClassInstalled("com.kinnara.kecakplugins.workflowcomponentoptionsbinder.ActivityOptionsBinder")) {
//                String appId = appDefinition.getAppId();
//                String appVersion = appDefinition.getVersion().toString();
//                activitiesProperty.put("type", "multiselect");
//                activitiesProperty.put("options_ajax","[CONTEXT_PATH]/web/json/app[APP_PATH]/plugin/com.kinnara.kecakplugins.workflowcomponentoptionsbinder.ActivityOptionsBinder/service?appId="+appId + "&appVersion=" + appVersion + "&type=" + WorkflowActivity.TYPE_NORMAL);
//            } else {
//                activitiesProperty.put("type", "textfield");
//            }
//        } catch (JSONException ignored) { }
//
//        String[] args = { activitiesProperty.toString().replaceAll("\"", "'") };
//        return AppUtil.readPluginResource(getClassName(), "/properties/ActivityPermission.json", args, false, "/messages/ActivityPermission");
        Object[] arguments = new Object[]{getClassName(), getClassName()};
        return AppUtil.readPluginResource(getClassName(), "/properties/ActivityPermission.json", arguments, false, "/messages/ActivityPermission");
    }

    private boolean isClassInstalled(String className) {
        PluginManager pluginManager = (PluginManager) AppUtil.getApplicationContext().getBean("pluginManager");
        Plugin plugin = pluginManager.getPlugin(className);
        return plugin != null;
    }

    public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isAdmin = WorkflowUtil.isCurrentUserInRole((String) "ROLE_ADMIN");
        if (!isAdmin) {
            response.sendError(401);
            return;
        }
        String action = request.getParameter("action");
        String appId = request.getParameter("appId");
        String appVersion = request.getParameter("appVersion");
        ApplicationContext ac = AppUtil.getApplicationContext();
        AppService appService = (AppService) ac.getBean("appService");
        WorkflowManager workflowManager = (WorkflowManager) ac.getBean("workflowManager");
        AppDefinition appDef = appService.getAppDefinition(appId, appVersion);
        if ("getProcesses".equals(action)) {
            try {
                JSONArray jsonArray = new JSONArray();
                PackageDefinition packageDefinition = appDef.getPackageDefinition();
                Long packageVersion = packageDefinition != null ? packageDefinition.getVersion() : new Long(1);
                Collection<WorkflowProcess> processList = workflowManager.getProcessList(appId, packageVersion.toString());
                HashMap<String, String> empty = new HashMap<String, String>();
                empty.put("value", "");
                empty.put("label", "");
                jsonArray.put(empty);
                for (WorkflowProcess p : processList) {
                    HashMap<String, String> option = new HashMap<String, String>();
                    option.put("value", p.getIdWithoutVersion());
                    option.put("label", p.getName() + " (" + p.getIdWithoutVersion() + ")");
                    jsonArray.put(option);
                }
                jsonArray.write(response.getWriter());
            } catch (Exception ex) {
                LogUtil.error(getClassName(), ex, "Get Process options Error!");
            }
        } else if ("getActivities".equals(action)) {
            try {
                JSONArray jsonArray = new JSONArray();
                HashMap<String, String> empty = new HashMap<String, String>();
                empty.put("value", "");
                empty.put("label", "");
                jsonArray.put(empty);
                String processId = request.getParameter("processId");
                if (!"null".equalsIgnoreCase(processId) && !processId.isEmpty()) {
                    String processDefId = "";
                    if (appDef != null) {
                        WorkflowProcess process = appService.getWorkflowProcessForApp(appDef.getId(), appDef.getVersion().toString(), processId);
                        processDefId = process.getId();
                    }
                    Collection<WorkflowActivity> activityList = workflowManager.getProcessActivityDefinitionList(processDefId);
                    for (WorkflowActivity a : activityList) {
                        if (a.getType().equals("route") || a.getType().equals("tool")) continue;
                        HashMap<String, String> option = new HashMap<String, String>();
                        option.put("value", a.getActivityDefId());
                        option.put("label", a.getName() + " (" + a.getActivityDefId() + ")");
                        jsonArray.put(option);
                    }
                }
                jsonArray.write(response.getWriter());
            } catch (Exception ex) {
                LogUtil.error(getClass().getName(), ex, "Get activity options Error!");
            }
        } else {
            response.setStatus(204);
        }
    }
}
