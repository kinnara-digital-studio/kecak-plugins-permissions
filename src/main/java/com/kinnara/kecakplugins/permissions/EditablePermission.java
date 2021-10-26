package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.form.service.FormUtil;
import org.joget.apps.userview.model.UserviewPermission;
import org.kecak.apps.form.model.FormPermissionDefault;

/**
 * @author aristo
 *
 * Make objects readonly if don't have permission instead of invisible
 *
 */
public class EditablePermission extends UserviewPermission implements FormPermission {
    @Override
    public boolean isAuthorize() {
        UserviewPermission editabilityPermission = Utilities.getPermissionObject(this, "editabilityPermission");
        UserviewPermission visibilityPermission = Utilities.getPermissionObject(this, "visibilityPermission");

        final boolean isEditable = (editabilityPermission == null || editabilityPermission.isAuthorize());
        final boolean readonlyLabel = "true".equalsIgnoreCase(getPropertyString(FormUtil.PROPERTY_READONLY_LABEL));

        // process the element based on permission
        FormData formData = getFormData();
        Element element = getElement();

        if(formData != null && element != null) {
            element.getChildren(formData)
                    .forEach(e -> {
                        if (!isEditable) {
                            // if don't have write access, set as readonly
                            FormUtil.setReadOnlyProperty(e, true, readonlyLabel);
                        }
                    });
        }

        // is visible
        return (visibilityPermission == null || visibilityPermission.isAuthorize());
    }

    @Override
    public String getName() {
        return "Editable Permission";
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
        return AppUtil.readPluginResource(getClassName(), "/properties/EditablePermission.json", null, false, "/messages/EditablePermission");
    }
}
