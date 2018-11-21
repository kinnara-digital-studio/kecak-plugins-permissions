package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.workflow.model.WorkflowProcess;
import org.joget.workflow.model.service.WorkflowManager;
import org.springframework.context.ApplicationContext;

public class ProcessCreatorPermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        if(getFormData() == null || getFormData().getPrimaryKeyValue() == null)
            return true;

        ApplicationContext appContext = AppUtil.getApplicationContext();
        WorkflowManager workflowManager = (WorkflowManager) appContext.getBean("workflowManager");

        WorkflowProcess process = workflowManager.getRunningProcessById(getFormData().getPrimaryKeyValue());
        return process.getRequesterId().equals(getCurrentUser().getUsername());
    }

    @Override
    public String getName() {
        return "Process Creator Permission";
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
        return "";
    }
}
