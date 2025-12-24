package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.plugin.base.PluginManager;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import java.util.function.Supplier;
import java.util.Map;
import java.util.*;
import org.joget.commons.util.LogUtil;



public class Utilities {
    public static UserviewPermission getPermissionObject(UserviewPermission plugin, String permissionPropertyName) {
        PluginManager pluginManager = (PluginManager) AppUtil.getApplicationContext().getBean("pluginManager");
        Map<String, Object> propertyPermission = (Map<String, Object>)plugin.getProperty(permissionPropertyName);
        if(propertyPermission == null)
            return null;

        String className = (String)propertyPermission.get("className");
        UserviewPermission permission = (UserviewPermission) pluginManager.getPlugin(className);
        if(permission == null)
            return null;

        Map<String, Object> properties = (Map<String, Object>)propertyPermission.get("properties");
        if(properties != null)
            permission.setProperties(properties);

        if(permission instanceof FormPermission && plugin instanceof FormPermission) {
            ((FormPermission)permission).setFormData(((FormPermission)plugin).getFormData());
        }

        permission.setCurrentUser(plugin.getCurrentUser());

        return permission;
    }

    public static <T> T getFromCache(String cacheKey, Supplier<T> ifNoCache) {

        final Cache cache = (Cache) AppUtil.getApplicationContext().getBean("fluCache");

        Element cached = cache.get(cacheKey);
        if (cached != null) {
            T value = (T) cached.getObjectValue();
            assert Objects.nonNull(value);

            LogUtil.info(Utilities.class.getName(), "Cache hit for key [" + cacheKey + "]");
            LogUtil.debug(Utilities.class.getName(), "Cache hit for key [" + cacheKey + "] value [" + value + "]");

            return value;
        }

        assert Objects.nonNull(ifNoCache);

        T value = Objects.requireNonNull(ifNoCache).get();

        if (value != null) {
            cache.put(new Element(cacheKey, value));
        }

        return value;
    }
}
