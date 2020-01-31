package com.kinnara.kecakplugins.permissions;

import org.joget.apps.form.model.DefaultFormPermission;
import org.joget.apps.form.service.FormUtil;
import org.joget.commons.util.LogUtil;

import java.util.Objects;
import java.util.Optional;

/**
 * @author aristo
 * Supposed to be used in form
 *
 * Return true if current form element is readonly
 *
 * Return true if placed in non form
 *
 */
public class IsReadOnlyPermission extends DefaultFormPermission {
    @Override
    public boolean isAuthorize() {
        return Optional.ofNullable(getElement())
                .filter(e -> Objects.nonNull(getFormData()))
                .map(e -> FormUtil.isReadonly(e, getFormData()))
                .orElse(true);
    }

    @Override
    public String getName() {
        return "Is Read-only Permission";
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
