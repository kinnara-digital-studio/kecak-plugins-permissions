package com.kinnara.kecakplugins.permissions;

import org.joget.apps.app.dao.FormDefinitionDao;
import org.joget.apps.app.model.AppDefinition;
import org.joget.apps.app.model.FormDefinition;
import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.Form;
import org.joget.apps.form.model.FormPermission;
import org.joget.apps.form.service.FormService;
import org.joget.apps.form.service.FormUtil;
import org.joget.apps.userview.model.UserviewPermission;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.Plugin;
import org.joget.plugin.base.PluginManager;
import org.joget.plugin.property.model.PropertyEditable;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author aristo
 *
 * Set as readonly instead of hidden, always return true
 */
public class AsReadOnly extends UserviewPermission implements FormPermission {
    private static final WeakHashMap<String, Form> formCache = new WeakHashMap<>();

    @Override
    public boolean isAuthorize() {
        Map<String, Object> permission = (Map<String, Object>)getProperty("permission");
        String className = (String) permission.get("className");
        Map<String, Object> properties = permission == null ? null : (Map<String, Object>)permission.get("properties");

        PluginManager pluginManager = (PluginManager) AppUtil.getApplicationContext().getBean("pluginManager");
        Plugin plugin = pluginManager.getPlugin(className);
        if(properties != null)
            ((PropertyEditable)plugin).setProperties(properties);

        ((UserviewPermission)plugin).setCurrentUser(getCurrentUser());
        ((UserviewPermission)plugin).setFormData(getFormData());

        Form currentForm = generateForm(getPropertyString("formDefId"));

        if(currentForm == null) {
            LogUtil.warn(getClassName(), "Form ["+getPropertyString("formDefId")+"] cannot be defined");
            return true;
        }

        Element element = FormUtil.findElement(getPropertyString("elementId"), currentForm, getFormData(), true);

        if(element == null) {
            LogUtil.warn(getClassName(), "Element ["+getPropertyString("elementId")+"] in form ["+getPropertyString("formDefId")+"] cannot be found");
            return true;
        }

        // set as readonly if no permission
        FormUtil.setReadOnlyProperty(element, !((UserviewPermission)plugin).isAuthorize(), false);

        return true;
    }

    @Override
    public String getName() {
        return AppPluginUtil.getMessage("asReadonly.title", getClassName(), "/messages/AsReadOnly");
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
        return AppUtil.readPluginResource(getClassName(), "/properties/AsReadOnly.json", null, true, "/messages/AsReadOnly");
    }

    private Form generateForm(String formDefId) {
        if(formDefId == null || formDefId.isEmpty())
            return null;

        // check in cache
        if(formCache.containsKey(formDefId))
            return formCache.get(formDefId);

        // proceed without cache
        ApplicationContext appContext = AppUtil.getApplicationContext();
        FormService formService = (FormService) appContext.getBean("formService");

        AppDefinition appDef = AppUtil.getCurrentAppDefinition();
        if (appDef != null) {
            FormDefinitionDao formDefinitionDao =
                    (FormDefinitionDao) AppUtil.getApplicationContext().getBean("formDefinitionDao");

            FormDefinition formDef = formDefinitionDao.loadById(formDefId, appDef);
            if (formDef != null) {
                String json = formDef.getJson();
                Form form = (Form)formService.createElementFromJson(json);

                // put in cache if possible
                if(form != null)
                    formCache.put(formDefId, form);

                return form;
            }
        }
        return null;
    }
}
