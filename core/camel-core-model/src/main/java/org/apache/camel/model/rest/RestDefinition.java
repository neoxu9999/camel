/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.model.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.CamelContext;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.model.OptionalIdentifiedDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.ToDefinition;
import org.apache.camel.model.ToDynamicDefinition;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.RestConfiguration;
import org.apache.camel.util.FileUtil;
import org.apache.camel.util.ObjectHelper;
import org.apache.camel.util.StringHelper;
import org.apache.camel.util.URISupport;

/**
 * Defines a rest service using the rest-dsl
 */
@Metadata(label = "rest")
@XmlRootElement(name = "rest")
@XmlAccessorType(XmlAccessType.FIELD)
public class RestDefinition extends OptionalIdentifiedDefinition<RestDefinition> {

    @XmlAttribute
    private String path;

    @XmlAttribute
    private String tag;

    @XmlAttribute
    private String consumes;

    @XmlAttribute
    private String produces;

    @XmlAttribute
    @Metadata(defaultValue = "auto")
    private String bindingMode;

    @XmlAttribute
    private String skipBindingOnErrorCode;

    @XmlAttribute
    private String clientRequestValidation;

    @XmlAttribute
    private String enableCORS;

    @XmlAttribute
    private String enableAutoDetect;

    @XmlAttribute
    private String apiDocs;

    @XmlElement(name = "securityDefinitions") // use the name Swagger/OpenAPI uses
    private RestSecuritiesDefinition securityDefinitions;

    @XmlElement(name = "securityRequirements") // use the name Swagger/OpenAPI uses
    private RestSecuritiesRequirement securityRequirements;

    @XmlElementRef
    private List<VerbDefinition> verbs = new ArrayList<>();

    @Override
    public String getShortName() {
        return "rest";
    }

    @Override
    public String getLabel() {
        return "rest";
    }

    public String getPath() {
        return path;
    }

    /**
     * Path of the rest service, such as "/foo"
     */
    public void setPath(String path) {
        this.path = path;
    }

    public String getTag() {
        return tag;
    }

    /**
     * To configure a special tag for the operations within this rest definition.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getConsumes() {
        return consumes;
    }

    /**
     * To define the content type what the REST service consumes (accept as input), such as application/xml or
     * application/json. This option will override what may be configured on a parent level
     */
    public void setConsumes(String consumes) {
        this.consumes = consumes;
    }

    public String getProduces() {
        return produces;
    }

    /**
     * To define the content type what the REST service produces (uses for output), such as application/xml or
     * application/json This option will override what may be configured on a parent level
     */
    public void setProduces(String produces) {
        this.produces = produces;
    }

    public String getBindingMode() {
        return bindingMode;
    }

    /**
     * Sets the binding mode to use. This option will override what may be configured on a parent level
     * <p/>
     * The default value is auto
     */
    public void setBindingMode(String bindingMode) {
        this.bindingMode = bindingMode;
    }

    public List<VerbDefinition> getVerbs() {
        return verbs;
    }

    public RestSecuritiesDefinition getSecurityDefinitions() {
        return securityDefinitions;
    }

    /**
     * Sets the security definitions such as Basic, OAuth2 etc.
     */
    public void setSecurityDefinitions(RestSecuritiesDefinition securityDefinitions) {
        this.securityDefinitions = securityDefinitions;
    }

    public RestSecuritiesRequirement getSecurityRequirements() {
        return securityRequirements;
    }

    /**
     * Sets the security requirement(s) for all endpoints.
     */
    public void setSecurityRequirements(RestSecuritiesRequirement securityRequirements) {
        this.securityRequirements = securityRequirements;
    }

    /**
     * The HTTP verbs this REST service accepts and uses
     */
    public void setVerbs(List<VerbDefinition> verbs) {
        this.verbs = verbs;
    }

    public String getSkipBindingOnErrorCode() {
        return skipBindingOnErrorCode;
    }

    /**
     * Whether to skip binding on output if there is a custom HTTP error code header. This allows to build custom error
     * messages that do not bind to json / xml etc, as success messages otherwise will do. This option will override
     * what may be configured on a parent level
     */
    public void setSkipBindingOnErrorCode(String skipBindingOnErrorCode) {
        this.skipBindingOnErrorCode = skipBindingOnErrorCode;
    }

    public String getClientRequestValidation() {
        return clientRequestValidation;
    }

    /**
     * Whether to enable validation of the client request to check:
     *
     * 1) Content-Type header matches what the Rest DSL consumes; returns HTTP Status 415 if validation error. 2) Accept
     * header matches what the Rest DSL produces; returns HTTP Status 406 if validation error. 3) Missing required data
     * (query parameters, HTTP headers, body); returns HTTP Status 400 if validation error. 4) Parsing error of the
     * message body (JSon, XML or Auto binding mode must be enabled); returns HTTP Status 400 if validation error.
     */
    public void setClientRequestValidation(String clientRequestValidation) {
        this.clientRequestValidation = clientRequestValidation;
    }

    public String getEnableCORS() {
        return enableCORS;
    }

    /**
     * Whether to enable CORS headers in the HTTP response. This option will override what may be configured on a parent
     * level
     * <p/>
     * The default value is false.
     */
    public void setEnableCORS(String enableCORS) {
        this.enableCORS = enableCORS;
    }

    public String getEnableAutoDetect() {
        return enableAutoDetect;
    }

    /**
     * Whether to enable auto-detect of the response body
     *
     * If a json response returned as empty String or null body with 200 OK, Camel auto-detect this and return empty
     * body and 204 instead.
     *
     * @param enableAutoDetect
     */
    public void setEnableAutoDetect(String enableAutoDetect) {
        this.enableAutoDetect = enableAutoDetect;
    }

    public String getApiDocs() {
        return apiDocs;
    }

    /**
     * Whether to include or exclude the VerbDefinition in API documentation. This option will override what may be
     * configured on a parent level
     * <p/>
     * The default value is true.
     */
    public void setApiDocs(String apiDocs) {
        this.apiDocs = apiDocs;
    }

    // Fluent API
    // -------------------------------------------------------------------------

    /**
     * To set the base path of this REST service
     */
    public RestDefinition path(String path) {
        setPath(path);
        return this;
    }

    /**
     * To set the tag to use of this REST service
     */
    public RestDefinition tag(String tag) {
        setTag(tag);
        return this;
    }

    public RestDefinition get() {
        return addVerb("get", null);
    }

    public RestDefinition get(String uri) {
        return addVerb("get", uri);
    }

    public RestDefinition post() {
        return addVerb("post", null);
    }

    public RestDefinition post(String uri) {
        return addVerb("post", uri);
    }

    public RestDefinition put() {
        return addVerb("put", null);
    }

    public RestDefinition put(String uri) {
        return addVerb("put", uri);
    }

    public RestDefinition patch() {
        return addVerb("patch", null);
    }

    public RestDefinition patch(String uri) {
        return addVerb("patch", uri);
    }

    public RestDefinition delete() {
        return addVerb("delete", null);
    }

    public RestDefinition delete(String uri) {
        return addVerb("delete", uri);
    }

    public RestDefinition head() {
        return addVerb("head", null);
    }

    public RestDefinition head(String uri) {
        return addVerb("head", uri);
    }

    public RestDefinition verb(String verb) {
        return addVerb(verb, null);
    }

    public RestDefinition verb(String verb, String uri) {
        return addVerb(verb, uri);
    }

    @Override
    public RestDefinition id(String id) {
        if (getVerbs().isEmpty()) {
            super.id(id);
        } else {
            // add on last verb as that is how the Java DSL works
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.id(id);
        }

        return this;
    }

    public RestDefinition deprecated() {
        if (!getVerbs().isEmpty()) {
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.deprecated();
        }

        return this;
    }

    @Override
    public RestDefinition description(String text) {
        if (getVerbs().isEmpty()) {
            super.description(text);
        } else {
            // add on last verb as that is how the Java DSL works
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.description(text);
        }

        return this;
    }

    @Override
    public RestDefinition description(String id, String text, String lang) {
        if (getVerbs().isEmpty()) {
            super.description(id, text, lang);
        } else {
            // add on last verb as that is how the Java DSL works
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.description(id, text, lang);
        }

        return this;
    }

    public RestDefinition consumes(String mediaType) {
        if (getVerbs().isEmpty()) {
            this.consumes = mediaType;
        } else {
            // add on last verb as that is how the Java DSL works
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.setConsumes(mediaType);
        }

        return this;
    }

    public RestOperationParamDefinition param() {
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }
        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        return param(verb);
    }

    public RestDefinition param(RestOperationParamDefinition param) {
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }
        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        verb.getParams().add(param);
        return this;
    }

    public RestDefinition params(List<RestOperationParamDefinition> params) {
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }
        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        verb.getParams().addAll(params);
        return this;
    }

    public RestOperationParamDefinition param(VerbDefinition verb) {
        return new RestOperationParamDefinition(verb);
    }

    public RestDefinition responseMessage(RestOperationResponseMsgDefinition msg) {
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }
        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        verb.getResponseMsgs().add(msg);
        return this;
    }

    public RestOperationResponseMsgDefinition responseMessage() {
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }
        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        return responseMessage(verb);
    }

    public RestOperationResponseMsgDefinition responseMessage(VerbDefinition verb) {
        return new RestOperationResponseMsgDefinition(verb);
    }

    public RestDefinition responseMessages(List<RestOperationResponseMsgDefinition> msgs) {
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }
        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        verb.getResponseMsgs().addAll(msgs);
        return this;
    }

    public RestDefinition responseMessage(int code, String message) {
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }
        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        RestOperationResponseMsgDefinition msg = responseMessage(verb);
        msg.setCode(String.valueOf(code));
        msg.setMessage(message);
        return this;
    }

    public RestDefinition responseMessage(String code, String message) {
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }
        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        RestOperationResponseMsgDefinition response = responseMessage(verb);
        response.setCode(code);
        response.setMessage(message);
        verb.getResponseMsgs().add(response);
        return this;
    }

    /**
     * To configure security definitions.
     */
    public RestSecuritiesDefinition securityDefinitions() {
        if (securityDefinitions == null) {
            securityDefinitions = new RestSecuritiesDefinition(this);
        }
        return securityDefinitions;
    }

    public RestDefinition produces(String mediaType) {
        if (getVerbs().isEmpty()) {
            this.produces = mediaType;
        } else {
            // add on last verb as that is how the Java DSL works
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.setProduces(mediaType);
        }

        return this;
    }

    public RestDefinition type(Class<?> classType) {
        // add to last verb
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }

        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        verb.setTypeClass(classType);
        verb.setType(asTypeName(classType));
        return this;
    }

    public RestDefinition outType(Class<?> classType) {
        // add to last verb
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }

        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        verb.setOutTypeClass(classType);
        verb.setOutType(asTypeName(classType));
        return this;
    }

    public RestDefinition bindingMode(RestBindingMode mode) {
        return bindingMode(mode.name());
    }

    public RestDefinition bindingMode(String mode) {
        if (getVerbs().isEmpty()) {
            this.bindingMode = mode.toLowerCase();
        } else {
            // add on last verb as that is how the Java DSL works
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.setBindingMode(mode.toLowerCase());
        }

        return this;
    }

    public RestDefinition skipBindingOnErrorCode(boolean skipBindingOnErrorCode) {
        if (getVerbs().isEmpty()) {
            this.skipBindingOnErrorCode = Boolean.toString(skipBindingOnErrorCode);
        } else {
            // add on last verb as that is how the Java DSL works
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.setSkipBindingOnErrorCode(Boolean.toString(skipBindingOnErrorCode));
        }

        return this;
    }

    public RestDefinition clientRequestValidation(boolean clientRequestValidation) {
        if (getVerbs().isEmpty()) {
            this.clientRequestValidation = Boolean.toString(clientRequestValidation);
        } else {
            // add on last verb as that is how the Java DSL works
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.setClientRequestValidation(Boolean.toString(clientRequestValidation));
        }

        return this;
    }

    public RestDefinition enableCORS(boolean enableCORS) {
        if (getVerbs().isEmpty()) {
            this.enableCORS = Boolean.toString(enableCORS);
        } else {
            // add on last verb as that is how the Java DSL works
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.setEnableCORS(Boolean.toString(enableCORS));
        }

        return this;
    }

    public RestDefinition enableAutoDetect(boolean enableAutoDetect) {
        if (getVerbs().isEmpty()) {
            this.enableAutoDetect = Boolean.toString(enableAutoDetect);
        } else {
            // add on last verb as that is how the Java DSL works
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.setEnableAutoDetect(Boolean.toString(enableAutoDetect));
        }

        return this;
    }

    /**
     * Include or exclude the current Rest Definition in API documentation.
     * <p/>
     * The default value is true.
     */
    public RestDefinition apiDocs(Boolean apiDocs) {
        if (getVerbs().isEmpty()) {
            this.apiDocs = apiDocs != null ? apiDocs.toString() : null;
        } else {
            // add on last verb as that is how the Java DSL works
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            verb.setApiDocs(apiDocs != null ? apiDocs.toString() : null);
        }

        return this;
    }

    /**
     * Sets the security setting for this verb.
     */
    public RestDefinition security(String key) {
        return security(key, null);
    }

    /**
     * Sets the security setting for this verb.
     */
    public RestDefinition security(String key, String scopes) {
        // add to last verb
        if (getVerbs().isEmpty()) {
            if (securityRequirements == null) {
                securityRequirements = new RestSecuritiesRequirement();
            }
            securityRequirements.securityRequirement(key, scopes);
        } else {
            VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
            SecurityDefinition sd = new SecurityDefinition();
            sd.setKey(key);
            sd.setScopes(scopes);
            verb.getSecurity().add(sd);
        }

        return this;
    }

    /**
     * Routes directly to the given static endpoint.
     * <p/>
     * If you need additional routing capabilities, then use {@link #route()} instead.
     *
     * @param  uri the uri of the endpoint
     * @return     this builder
     */
    public RestDefinition to(String uri) {
        // add to last verb
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }

        ToDefinition to = new ToDefinition(uri);

        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        verb.setTo(to);
        return this;
    }

    /**
     * Routes directly to the given dynamic endpoint.
     * <p/>
     * If you need additional routing capabilities, then use {@link #route()} instead.
     *
     * @param  uri the uri of the endpoint
     * @return     this builder
     */
    public RestDefinition toD(String uri) {
        // add to last verb
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }

        ToDynamicDefinition to = new ToDynamicDefinition(uri);

        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        verb.setToD(to);
        return this;
    }

    public RouteDefinition route() {
        // add to last verb
        if (getVerbs().isEmpty()) {
            throw new IllegalArgumentException("Must add verb first, such as get/post/delete");
        }

        // link them together so we can navigate using Java DSL
        RouteDefinition route = new RouteDefinition();
        route.setRestDefinition(this);
        VerbDefinition verb = getVerbs().get(getVerbs().size() - 1);
        verb.setRoute(route);
        return route;
    }

    /**
     * Build the from endpoint uri for the verb
     */
    public String buildFromUri(VerbDefinition verb) {
        return "rest:" + verb.asVerb() + ":" + buildUri(verb);
    }

    // Implementation
    // -------------------------------------------------------------------------

    private RestDefinition addVerb(String verb, String uri) {
        VerbDefinition answer;

        if ("get".equals(verb)) {
            answer = new GetVerbDefinition();
        } else if ("post".equals(verb)) {
            answer = new PostVerbDefinition();
        } else if ("delete".equals(verb)) {
            answer = new DeleteVerbDefinition();
        } else if ("head".equals(verb)) {
            answer = new HeadVerbDefinition();
        } else if ("put".equals(verb)) {
            answer = new PutVerbDefinition();
        } else if ("patch".equals(verb)) {
            answer = new PatchVerbDefinition();
        } else {
            answer = new VerbDefinition();
            answer.setMethod(verb);
        }
        getVerbs().add(answer);
        answer.setRest(this);
        answer.setUri(uri);
        return this;
    }

    /**
     * Transforms this REST definition into a list of {@link org.apache.camel.model.RouteDefinition} which Camel routing
     * engine can add and run. This allows us to define REST services using this REST DSL and turn those into regular
     * Camel routes.
     *
     * @param camelContext The Camel context
     */
    public List<RouteDefinition> asRouteDefinition(CamelContext camelContext) {
        ObjectHelper.notNull(camelContext, "CamelContext");

        // sanity check this rest definition do not have duplicates
        validateUniquePaths();

        List<RouteDefinition> answer = new ArrayList<>();

        RestConfiguration config = camelContext.getRestConfiguration();

        addRouteDefinition(camelContext, answer, config.getComponent(), config.getProducerComponent());

        return answer;
    }

    protected void validateUniquePaths() {
        Set<String> paths = new HashSet<>();
        for (VerbDefinition verb : verbs) {
            String path = verb.asVerb();
            if (verb.getUri() != null) {
                path += ":" + verb.getUri();
            }
            if (!paths.add(path)) {
                throw new IllegalArgumentException("Duplicate verb detected in rest-dsl: " + path);
            }
        }
    }

    protected String asTypeName(Class<?> classType) {
        // Workaround for https://issues.apache.org/jira/browse/CAMEL-15199
        //
        // The VerbDefinition::setType and VerbDefinition::setOutType require
        // the class to be expressed as canonical with an optional [] to mark
        // the type is an array but this i wrong as the canonical name can not
        // be dynamically be loaded by the classloader thus this workaround
        // that for nested classes generates a class name that does not respect
        // any JLS convention.
        //
        // TODO: this probably need to be revisited

        String type;

        if (!classType.isPrimitive()) {
            if (classType.isArray()) {
                type = StringHelper.between(classType.getName(), "[L", ";") + "[]";
            } else {
                type = classType.getName();
            }
        } else {
            type = classType.getCanonicalName();
        }

        return type;
    }

    /**
     * Transforms the rest api configuration into a {@link org.apache.camel.model.RouteDefinition} which Camel routing
     * engine uses to service the rest api docs.
     */
    public static RouteDefinition asRouteApiDefinition(CamelContext camelContext, RestConfiguration configuration) {
        RouteDefinition answer = new RouteDefinition();

        // create the from endpoint uri which is using the rest-api component
        String from = "rest-api:" + configuration.getApiContextPath();

        String routeId = configuration.getApiContextRouteId();
        if (routeId == null) {
            routeId = answer.idOrCreate(camelContext.adapt(ExtendedCamelContext.class).getNodeIdFactory());
        }

        // append options
        Map<String, Object> options = new HashMap<>();

        if (configuration.getComponent() != null && !configuration.getComponent().isEmpty()) {
            options.put("consumerComponentName", configuration.getComponent());
        }
        if (configuration.getApiContextIdPattern() != null) {
            options.put("contextIdPattern", configuration.getApiContextIdPattern());
        }

        if (!options.isEmpty()) {
            try {
                from = URISupport.appendParametersToURI(from, options);
            } catch (Exception e) {
                throw RuntimeCamelException.wrapRuntimeCamelException(e);
            }
        }

        // we use the same uri as the producer (so we have a little route for
        // the rest api)
        String to = from;
        answer.fromRest(from);
        answer.id(routeId);
        answer.to(to);

        return answer;
    }

    @SuppressWarnings("rawtypes")
    private void addRouteDefinition(
            CamelContext camelContext, List<RouteDefinition> answer, String component, String producerComponent) {
        for (VerbDefinition verb : getVerbs()) {
            // either the verb has a singular to or a embedded route
            RouteDefinition route = verb.getRoute();
            if (route == null) {
                // it was a singular to, so add a new route and add the singular
                // to as output to this route
                route = new RouteDefinition();
                ProcessorDefinition def = verb.getTo() != null ? verb.getTo() : verb.getToD();
                route.getOutputs().add(def);
            }

            // add the binding
            RestBindingDefinition binding = new RestBindingDefinition();
            binding.setComponent(component);
            binding.setType(verb.getType());
            binding.setTypeClass(verb.getTypeClass());
            binding.setOutType(verb.getOutType());
            binding.setOutTypeClass(verb.getOutTypeClass());
            // verb takes precedence over configuration on rest
            if (verb.getConsumes() != null) {
                binding.setConsumes(verb.getConsumes());
            } else {
                binding.setConsumes(getConsumes());
            }
            if (verb.getProduces() != null) {
                binding.setProduces(verb.getProduces());
            } else {
                binding.setProduces(getProduces());
            }
            if (verb.getBindingMode() != null) {
                binding.setBindingMode(verb.getBindingMode());
            } else {
                binding.setBindingMode(getBindingMode());
            }
            if (verb.getSkipBindingOnErrorCode() != null) {
                binding.setSkipBindingOnErrorCode(verb.getSkipBindingOnErrorCode());
            } else {
                binding.setSkipBindingOnErrorCode(getSkipBindingOnErrorCode());
            }
            if (verb.getClientRequestValidation() != null) {
                binding.setClientRequestValidation(verb.getClientRequestValidation());
            } else {
                binding.setClientRequestValidation(getClientRequestValidation());
            }
            if (verb.getEnableCORS() != null) {
                binding.setEnableCORS(verb.getEnableCORS());
            } else {
                binding.setEnableCORS(getEnableCORS());
            }
            if (verb.getEnableAutoDetect() != null) {
                binding.setEnableAutoDetect(verb.getEnableAutoDetect());
            } else {
                binding.setEnableAutoDetect(getEnableAutoDetect());
            }
            for (RestOperationParamDefinition param : verb.getParams()) {
                // register all the default values for the query and header parameters
                RestParamType type = param.getType();
                if ((RestParamType.query == type || RestParamType.header == type)
                        && ObjectHelper.isNotEmpty(param.getDefaultValue())) {
                    binding.addDefaultValue(param.getName(), param.getDefaultValue());
                }
                // register which parameters are required
                Boolean required = param.getRequired();
                if (required != null && required) {
                    if (RestParamType.query == type) {
                        binding.addRequiredQueryParameter(param.getName());
                    } else if (RestParamType.header == type) {
                        binding.addRequiredHeader(param.getName());
                    } else if (RestParamType.body == type) {
                        binding.setRequiredBody(true);
                    }
                }
            }

            route.setRestBindingDefinition(binding);

            // append options
            Map<String, Object> options = new HashMap<>();
            // verb takes precedence over configuration on rest
            if (verb.getConsumes() != null) {
                options.put("consumes", verb.getConsumes());
            } else if (getConsumes() != null) {
                options.put("consumes", getConsumes());
            }
            if (verb.getProduces() != null) {
                options.put("produces", verb.getProduces());
            } else if (getProduces() != null) {
                options.put("produces", getProduces());
            }

            // append optional type binding information
            String inType = binding.getType();
            if (inType != null) {
                options.put("inType", inType);
            }
            String outType = binding.getOutType();
            if (outType != null) {
                options.put("outType", outType);
            }

            if (component != null && !component.isEmpty()) {
                options.put("consumerComponentName", component);
            }
            if (producerComponent != null && !producerComponent.isEmpty()) {
                options.put("producerComponentName", producerComponent);
            }

            // include optional description, which we favor from 1) to/route
            // description 2) verb description 3) rest description
            // this allows end users to define general descriptions and override
            // then per to/route or verb
            String description = verb.getTo() != null ? verb.getTo().getDescriptionText() : route.getDescriptionText();
            if (description == null) {
                description = verb.getDescriptionText();
            }
            if (description == null) {
                description = getDescriptionText();
            }
            if (description != null) {
                options.put("description", description);
            }

            String path = getPath();
            String s1 = FileUtil.stripTrailingSeparator(path);
            String s2 = FileUtil.stripLeadingSeparator(verb.getUri());
            String allPath;
            if (s1 != null && s2 != null) {
                allPath = s1 + "/" + s2;
            } else if (path != null) {
                allPath = path;
            } else {
                allPath = verb.getUri();
            }

            // each {} is a parameter (url templating)
            Set<String> toRemove = null;
            if (allPath != null && allPath.contains("?")) {
                // special when having query parameters
                String path1 = StringHelper.before(allPath, "?");
                uriTemplating(camelContext, verb, path1, false);
                String path2 = StringHelper.after(allPath, "?");
                // there may be some query parameters that are templates which we then must remove
                toRemove = uriTemplating(camelContext, verb, path2, true);
            } else {
                // no query parameters
                uriTemplating(camelContext, verb, allPath, false);
            }

            if (verb.getType() != null) {
                String bodyType = verb.getType();
                if (bodyType.endsWith("[]")) {
                    bodyType = "List[" + bodyType.substring(0, bodyType.length() - 2) + "]";
                }
                RestOperationParamDefinition param = findParam(verb, RestParamType.body.name());
                if (param == null) {
                    // must be body type and set the model class as data type
                    param(verb).name(RestParamType.body.name()).type(RestParamType.body).dataType(bodyType).endParam();
                } else {
                    // must be body type and set the model class as data type
                    param.type(RestParamType.body).dataType(bodyType);
                }
            }

            // create the from endpoint uri which is using the rest component
            String from = buildFromUri(verb);

            // rebuild uri without these query parameters
            if (toRemove != null && !toRemove.isEmpty()) {
                try {
                    Map<String, Object> query = URISupport.parseQuery(URISupport.extractQuery(from));
                    // remove if the value matches, eg: auth={myAuth}
                    toRemove.forEach(v -> {
                        query.values().removeIf(qv -> qv.toString().equals(v));
                    });
                    from = URISupport.stripQuery(from);
                    if (!query.isEmpty()) {
                        String q = URISupport.createQueryString(query);
                        from = URISupport.stripQuery(from) + "?" + q;
                    }
                } catch (Exception e) {
                    throw RuntimeCamelException.wrapRuntimeCamelException(e);
                }
            }

            // append additional options
            if (!options.isEmpty()) {
                try {
                    from = URISupport.appendParametersToURI(from, options);
                } catch (Exception e) {
                    throw RuntimeCamelException.wrapRuntimeCamelException(e);
                }
            }

            // the route should be from this rest endpoint
            route.fromRest(from);
            route.setRestDefinition(this);
            answer.add(route);
        }
    }

    private Set<String> uriTemplating(
            CamelContext camelContext, VerbDefinition verb,
            String path, boolean query) {

        if (path == null) {
            return null;
        }

        Set<String> params = new HashSet<>();
        String[] arr = path.split("\\/");
        for (String a : arr) {
            // need to resolve property placeholders first
            try {
                a = camelContext.resolvePropertyPlaceholders(a);
            } catch (Exception e) {
                throw RuntimeCamelException.wrapRuntimeCamelException(e);
            }

            Matcher m = Pattern.compile("\\{(.*?)\\}").matcher(a);
            while (m.find()) {
                String key = m.group(1);
                params.add("{" + key + "}");
                //  merge if exists
                boolean found = false;
                for (RestOperationParamDefinition param : verb.getParams()) {
                    // name is mandatory
                    String name = param.getName();
                    StringHelper.notEmpty(name, "parameter name");
                    // need to resolve property placeholders first
                    try {
                        name = camelContext.resolvePropertyPlaceholders(name);
                    } catch (Exception e) {
                        throw RuntimeCamelException.wrapRuntimeCamelException(e);
                    }
                    if (name.equalsIgnoreCase(key)) {
                        param.type(query ? RestParamType.query : RestParamType.path);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    param(verb).name(key).type(query ? RestParamType.query : RestParamType.path).endParam();
                }
            }
        }
        return params;
    }

    private String buildUri(VerbDefinition verb) {
        if (path != null && verb.getUri() != null) {
            return path + ":" + verb.getUri();
        } else if (path != null) {
            return path;
        } else if (verb.getUri() != null) {
            return verb.getUri();
        } else {
            return "";
        }
    }

    private RestOperationParamDefinition findParam(VerbDefinition verb, String name) {
        for (RestOperationParamDefinition param : verb.getParams()) {
            if (name.equals(param.getName())) {
                return param;
            }
        }
        return null;
    }

}
