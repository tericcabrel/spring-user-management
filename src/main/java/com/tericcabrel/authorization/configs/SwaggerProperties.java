package com.tericcabrel.authorization.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration("swaggerProperties")
public class SwaggerProperties {
    @Value("${api.version}")
    private String apiVersion;

    @Value("${swagger.enabled}")
    private String enabled = "false";

    @Value("${swagger.title}")
    private String title;

    @Value("${swagger.description}")
    private String description;

    @Value("${swagger.useDefaultResponseMessages}")
    private String useDefaultResponseMessages;

    @Value("${swagger.enableUrlTemplating}")
    private String enableUrlTemplating;

    @Value("${swagger.deepLinking}")
    private String deepLinking;

    @Value("${swagger.defaultModelsExpandDepth}")

    private String defaultModelsExpandDepth;

    @Value("${swagger.defaultModelExpandDepth}")
    private String defaultModelExpandDepth;

    @Value("${swagger.displayOperationId}")
    private String displayOperationId;

    @Value("${swagger.displayRequestDuration}")
    private String displayRequestDuration;

    @Value("${swagger.filter}")
    private String filter;

    @Value("${swagger.maxDisplayedTags}")
    private String maxDisplayedTags;

    @Value("${swagger.showExtensions}")
    private String showExtensions;

    public String getApiVersion() {
        return apiVersion;
    }

    public SwaggerProperties setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public String getEnabled() {
        return enabled;
    }

    public SwaggerProperties setEnabled(String enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SwaggerProperties setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SwaggerProperties setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getUseDefaultResponseMessages() {
        return useDefaultResponseMessages;
    }

    public SwaggerProperties setUseDefaultResponseMessages(String useDefaultResponseMessages) {
        this.useDefaultResponseMessages = useDefaultResponseMessages;
        return this;
    }

    public String getEnableUrlTemplating() {
        return enableUrlTemplating;
    }

    public SwaggerProperties setEnableUrlTemplating(String enableUrlTemplating) {
        this.enableUrlTemplating = enableUrlTemplating;
        return this;
    }

    public String getDeepLinking() {
        return deepLinking;
    }

    public SwaggerProperties setDeepLinking(String deepLinking) {
        this.deepLinking = deepLinking;
        return this;
    }

    public String getDefaultModelsExpandDepth() {
        return defaultModelsExpandDepth;
    }

    public SwaggerProperties setDefaultModelsExpandDepth(String defaultModelsExpandDepth) {
        this.defaultModelsExpandDepth = defaultModelsExpandDepth;
        return this;
    }

    public String getDefaultModelExpandDepth() {
        return defaultModelExpandDepth;
    }

    public SwaggerProperties setDefaultModelExpandDepth(String defaultModelExpandDepth) {
        this.defaultModelExpandDepth = defaultModelExpandDepth;
        return this;
    }

    public String getDisplayOperationId() {
        return displayOperationId;
    }

    public SwaggerProperties setDisplayOperationId(String displayOperationId) {
        this.displayOperationId = displayOperationId;
        return this;
    }

    public String getDisplayRequestDuration() {
        return displayRequestDuration;
    }

    public SwaggerProperties setDisplayRequestDuration(String displayRequestDuration) {
        this.displayRequestDuration = displayRequestDuration;
        return this;
    }

    public String getFilter() {
        return filter;
    }

    public SwaggerProperties setFilter(String filter) {
        this.filter = filter;
        return this;
    }

    public String getMaxDisplayedTags() {
        return maxDisplayedTags;
    }

    public SwaggerProperties setMaxDisplayedTags(String maxDisplayedTags) {
        this.maxDisplayedTags = maxDisplayedTags;
        return this;
    }

    public String getShowExtensions() {
        return showExtensions;
    }

    public SwaggerProperties setShowExtensions(String showExtensions) {
        this.showExtensions = showExtensions;
        return this;
    }
}
