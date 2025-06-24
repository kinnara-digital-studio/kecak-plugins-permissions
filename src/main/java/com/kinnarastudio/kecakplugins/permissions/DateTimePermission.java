package com.kinnarastudio.kecakplugins.permissions;

import com.kinnarastudio.commons.Try;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.Permission;
import org.joget.apps.userview.model.UserviewAccessPermission;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Date Time Permission
 */
public class DateTimePermission extends Permission implements FormPermission, UserviewAccessPermission {
    public final static String LABEL = "Date Time Permission";

    @Override
    public boolean isAuthorize() {
        final Date now = new Date();

        final String operator = getOperator();
        LogUtil.info(getClassName(), "operator [" + operator + "]");

        final Date[] values = getValues();
        LogUtil.info(getClassName(), "values [" + Arrays.stream(values).map(Date::toString).collect(Collectors.joining("; ")) + "]");

        switch (operator) {
            case "=":
                return Arrays.asList(values).contains(now);
            case "<>":
                return Arrays.stream(values).noneMatch(now::equals);
            case ">":
                return Arrays.stream(values).findFirst().map(now::after).orElse(false);
            case "<":
                return Arrays.stream(values).findFirst().map(now::before).orElse(false);
            default:
                return false;
        }
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
        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String[] args = new String[]{df.format(new Date())};
        return AppUtil.readPluginResource(getClassName(), "/properties/DateTimePermission.json", args, false, "/messages/DateTimePermission");
    }

    protected Date[] getValues() {
        final DateFormat df = getDateTimeFormat();
        return Optional.ofNullable(getPropertyString("values"))
                .map(s -> s.split(";"))
                .stream()
                .flatMap(Arrays::stream)
                .filter(Predicate.not(String::isEmpty))
                .map(Try.onFunction(df::parse))
                .toArray(Date[]::new);
    }

    protected DateFormat getDateTimeFormat() {
        return new SimpleDateFormat(getPropertyString("dateTimeFormat"));
    }

    protected String getOperator() {
        return getPropertyString("operator");
    }
}
