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
package org.apache.camel.main;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.LoggingLevel;
import org.apache.camel.ManagementStatisticsLevel;
import org.apache.camel.StartupSummaryLevel;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.Tracer;
import org.apache.camel.support.PatternHelper;

/**
 * Common set of configuration options used by Camel Main, Camel Spring Boot and other runtimes.
 */
public abstract class DefaultConfigurationProperties<T> {

    private String name;
    @Metadata(defaultValue = "Default")
    private StartupSummaryLevel startupSummaryLevel;
    private int durationMaxSeconds;
    private int durationMaxIdleSeconds;
    private int durationMaxMessages;
    @Metadata(defaultValue = "shutdown", enums = "shutdown,stop")
    private String durationMaxAction = "shutdown";
    private int shutdownTimeout = 45;
    private boolean shutdownSuppressLoggingOnTimeout;
    private boolean shutdownNowOnTimeout = true;
    private boolean shutdownRoutesInReverseOrder = true;
    private boolean shutdownLogInflightExchangesOnTimeout = true;
    private boolean inflightRepositoryBrowseEnabled;
    private String fileConfigurations;
    private boolean jmxEnabled = true;
    @Metadata(enums = "classic,default,short,simple,off", defaultValue = "default")
    private String uuidGenerator = "default";
    private int producerTemplateCacheSize = 1000;
    private int consumerTemplateCacheSize = 1000;
    private boolean loadTypeConverters;
    private boolean loadHealthChecks;
    private boolean devConsoleEnabled;
    private boolean modeline;
    private int logDebugMaxChars;
    private boolean streamCachingEnabled;
    private String streamCachingSpoolDirectory;
    private String streamCachingSpoolCipher;
    private long streamCachingSpoolThreshold;
    private int streamCachingSpoolUsedHeapMemoryThreshold;
    private String streamCachingSpoolUsedHeapMemoryLimit;
    private boolean streamCachingAnySpoolRules;
    private int streamCachingBufferSize;
    private boolean streamCachingRemoveSpoolDirectoryWhenStopping = true;
    private boolean streamCachingStatisticsEnabled;
    private boolean debugging;
    private boolean backlogTracing;
    private boolean tracing;
    private boolean tracingStandby;
    private String tracingPattern;
    @Metadata(defaultValue = "%-4.4s [%-12.12s] [%-33.33s]")
    private String tracingLoggingFormat;
    private boolean sourceLocationEnabled;
    private boolean messageHistory;
    private boolean logMask;
    private boolean logExhaustedMessageBody;
    private boolean autoStartup = true;
    private boolean allowUseOriginalMessage;
    private boolean caseInsensitiveHeaders = true;
    private boolean autowiredEnabled = true;
    private boolean endpointRuntimeStatisticsEnabled;
    private boolean endpointLazyStartProducer;
    private boolean endpointBridgeErrorHandler;
    private boolean useDataType;
    private boolean useBreadcrumb;
    private boolean beanPostProcessorEnabled = true;
    @Metadata(defaultValue = "Default")
    private ManagementStatisticsLevel jmxManagementStatisticsLevel = ManagementStatisticsLevel.Default;
    private String jmxManagementNamePattern = "#name#";
    private boolean useMdcLogging;
    private String mdcLoggingKeysPattern;
    private String threadNamePattern;
    private String routeFilterIncludePattern;
    private String routeFilterExcludePattern;
    private boolean beanIntrospectionExtendedStatistics;
    private LoggingLevel beanIntrospectionLoggingLevel;
    private boolean routesCollectorEnabled = true;
    private String javaRoutesIncludePattern;
    private String javaRoutesExcludePattern;
    private String routesIncludePattern = "classpath:camel/*,classpath:camel-template/*,classpath:camel-rest/*";
    private String routesExcludePattern;
    private boolean routesReloadEnabled;
    @Metadata(defaultValue = "src/main/resources/camel")
    private String routesReloadDirectory = "src/main/resources/camel";
    private boolean routesReloadDirectoryRecursive;
    private String routesReloadPattern;
    @Metadata(defaultValue = "true")
    private boolean routesReloadRemoveAllRoutes = true;
    @Metadata(defaultValue = "true")
    private boolean routesReloadRestartDuration = true;
    private boolean lightweight;
    private boolean eagerClassloading;
    @Metadata(defaultValue = "default", enums = "default,prototype,pooled")
    private String exchangeFactory = "default";
    private int exchangeFactoryCapacity = 100;
    private boolean exchangeFactoryStatisticsEnabled;
    private boolean dumpRoutes;
    private Map<String, String> globalOptions;
    // route controller
    @Metadata(defaultValue = "DEBUG")
    @Deprecated
    private LoggingLevel routeControllerLoggingLevel;
    private boolean routeControllerSuperviseEnabled;
    private String routeControllerIncludeRoutes;
    private String routeControllerExcludeRoutes;
    private int routeControllerThreadPoolSize;
    private long routeControllerInitialDelay;
    private long routeControllerBackOffDelay;
    private long routeControllerBackOffMaxDelay;
    private long routeControllerBackOffMaxElapsedTime;
    private long routeControllerBackOffMaxAttempts;
    private double routeControllerBackOffMultiplier;
    private boolean routeControllerUnhealthyOnExhausted;
    // startup recorder
    @Metadata(enums = "false,off,java-flight-recorder,jfr,logging")
    private String startupRecorder;
    private int startupRecorderMaxDepth = -1;
    private boolean startupRecorderRecording;
    private String startupRecorderProfile = "default";
    private long startupRecorderDuration;
    private String startupRecorderDir;

    // getter and setters
    // --------------------------------------------------------------

    public String getName() {
        return name;
    }

    /**
     * Sets the name of the CamelContext.
     */
    public void setName(String name) {
        this.name = name;
    }

    public StartupSummaryLevel getStartupSummaryLevel() {
        return startupSummaryLevel;
    }

    /**
     * Controls the level of information logged during startup (and shutdown) of CamelContext.
     */
    public void setStartupSummaryLevel(StartupSummaryLevel startupSummaryLevel) {
        this.startupSummaryLevel = startupSummaryLevel;
    }

    public int getDurationMaxSeconds() {
        return durationMaxSeconds;
    }

    /**
     * To specify for how long time in seconds to keep running the JVM before automatic terminating the JVM. You can use
     * this to run Camel for a short while.
     */
    public void setDurationMaxSeconds(int durationMaxSeconds) {
        this.durationMaxSeconds = durationMaxSeconds;
    }

    public int getDurationMaxIdleSeconds() {
        return durationMaxIdleSeconds;
    }

    /**
     * To specify for how long time in seconds Camel can be idle before automatic terminating the JVM. You can use this
     * to run Camel for a short while.
     */
    public void setDurationMaxIdleSeconds(int durationMaxIdleSeconds) {
        this.durationMaxIdleSeconds = durationMaxIdleSeconds;
    }

    public int getDurationMaxMessages() {
        return durationMaxMessages;
    }

    /**
     * To specify how many messages to process by Camel before automatic terminating the JVM. You can use this to run
     * Camel for a short while.
     */
    public void setDurationMaxMessages(int durationMaxMessages) {
        this.durationMaxMessages = durationMaxMessages;
    }

    public String getDurationMaxAction() {
        return durationMaxAction;
    }

    /**
     * Controls whether the Camel application should shutdown the JVM, or stop all routes, when duration max is
     * triggered.
     */
    public void setDurationMaxAction(String durationMaxAction) {
        this.durationMaxAction = durationMaxAction;
    }

    public int getShutdownTimeout() {
        return shutdownTimeout;
    }

    /**
     * Timeout in seconds to graceful shutdown Camel.
     */
    public void setShutdownTimeout(int shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    public boolean isShutdownSuppressLoggingOnTimeout() {
        return shutdownSuppressLoggingOnTimeout;
    }

    /**
     * Whether Camel should try to suppress logging during shutdown and timeout was triggered, meaning forced shutdown
     * is happening. And during forced shutdown we want to avoid logging errors/warnings et all in the logs as a
     * side-effect of the forced timeout. Notice the suppress is a best effort as there may still be some logs coming
     * from 3rd party libraries and whatnot, which Camel cannot control. This option is default false.
     */
    public void setShutdownSuppressLoggingOnTimeout(boolean shutdownSuppressLoggingOnTimeout) {
        this.shutdownSuppressLoggingOnTimeout = shutdownSuppressLoggingOnTimeout;
    }

    public boolean isShutdownNowOnTimeout() {
        return shutdownNowOnTimeout;
    }

    /**
     * Sets whether to force shutdown of all consumers when a timeout occurred and thus not all consumers was shutdown
     * within that period.
     *
     * You should have good reasons to set this option to false as it means that the routes keep running and is halted
     * abruptly when CamelContext has been shutdown.
     */
    public void setShutdownNowOnTimeout(boolean shutdownNowOnTimeout) {
        this.shutdownNowOnTimeout = shutdownNowOnTimeout;
    }

    public boolean isShutdownRoutesInReverseOrder() {
        return shutdownRoutesInReverseOrder;
    }

    /**
     * Sets whether routes should be shutdown in reverse or the same order as they were started.
     */
    public void setShutdownRoutesInReverseOrder(boolean shutdownRoutesInReverseOrder) {
        this.shutdownRoutesInReverseOrder = shutdownRoutesInReverseOrder;
    }

    public boolean isShutdownLogInflightExchangesOnTimeout() {
        return shutdownLogInflightExchangesOnTimeout;
    }

    /**
     * Sets whether to log information about the inflight Exchanges which are still running during a shutdown which
     * didn't complete without the given timeout.
     *
     * This requires to enable the option inflightRepositoryBrowseEnabled.
     */
    public void setShutdownLogInflightExchangesOnTimeout(boolean shutdownLogInflightExchangesOnTimeout) {
        this.shutdownLogInflightExchangesOnTimeout = shutdownLogInflightExchangesOnTimeout;
    }

    public boolean isInflightRepositoryBrowseEnabled() {
        return inflightRepositoryBrowseEnabled;
    }

    /**
     * Sets whether the inflight repository should allow browsing each inflight exchange.
     *
     * This is by default disabled as there is a very slight performance overhead when enabled.
     */
    public void setInflightRepositoryBrowseEnabled(boolean inflightRepositoryBrowseEnabled) {
        this.inflightRepositoryBrowseEnabled = inflightRepositoryBrowseEnabled;
    }

    public String getFileConfigurations() {
        return fileConfigurations;
    }

    /**
     * Directory to load additional configuration files that contains configuration values that takes precedence over
     * any other configuration. This can be used to refer to files that may have secret configuration that has been
     * mounted on the file system for containers.
     *
     * You can specify a pattern to load from sub directories and a name pattern such as /var/app/secret/*.properties,
     * multiple directories can be separated by comma.
     */
    public void setFileConfigurations(String fileConfigurations) {
        this.fileConfigurations = fileConfigurations;
    }

    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

    /**
     * Enable JMX in your Camel application.
     */
    public void setJmxEnabled(boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
    }

    public String getUuidGenerator() {
        return uuidGenerator;
    }

    /**
     * UUID generator to use.
     *
     * default (32 bytes), short (16 bytes), classic (32 bytes or longer), simple (long incrementing counter), off
     * (turned off for exchanges - only intended for performance profiling)
     */
    public void setUuidGenerator(String uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    public int getProducerTemplateCacheSize() {
        return producerTemplateCacheSize;
    }

    /**
     * Producer template endpoints cache size.
     */
    public void setProducerTemplateCacheSize(int producerTemplateCacheSize) {
        this.producerTemplateCacheSize = producerTemplateCacheSize;
    }

    public int getConsumerTemplateCacheSize() {
        return consumerTemplateCacheSize;
    }

    /**
     * Consumer template endpoints cache size.
     */
    public void setConsumerTemplateCacheSize(int consumerTemplateCacheSize) {
        this.consumerTemplateCacheSize = consumerTemplateCacheSize;
    }

    public boolean isLoadTypeConverters() {
        return loadTypeConverters;
    }

    /**
     * Whether to load custom type converters by scanning classpath. This is used for backwards compatibility with Camel
     * 2.x. Its recommended to migrate to use fast type converter loading by setting <tt>@Converter(loader = true)</tt>
     * on your custom type converter classes.
     */
    public void setLoadTypeConverters(boolean loadTypeConverters) {
        this.loadTypeConverters = loadTypeConverters;
    }

    public boolean isLoadHealthChecks() {
        return loadHealthChecks;
    }

    /**
     * Whether to load custom health checks by scanning classpath.
     */
    public void setLoadHealthChecks(boolean loadHealthChecks) {
        this.loadHealthChecks = loadHealthChecks;
    }

    public boolean isDevConsoleEnabled() {
        return devConsoleEnabled;
    }

    /**
     * Whether to enable developer console (requires camel-console on classpath).
     *
     * The developer console is only for assisting during development. This is NOT for production usage.
     */
    public void setDevConsoleEnabled(boolean devConsoleEnabled) {
        this.devConsoleEnabled = devConsoleEnabled;
    }

    public boolean ismodeline() {
        return modeline;
    }

    /**
     * Whether camel-k style modeline is also enabled when not using camel-k. Enabling this allows to use a camel-k like
     * experience by being able to configure various settings using modeline directly in your route source code.
     */
    public void setmodeline(boolean modeline) {
        this.modeline = modeline;
    }

    public int getLogDebugMaxChars() {
        return logDebugMaxChars;
    }

    /**
     * Is used to limit the maximum length of the logging Camel message bodies. If the message body is longer than the
     * limit, the log message is clipped. Use -1 to have unlimited length. Use for example 1000 to log at most 1000
     * characters.
     */
    public void setLogDebugMaxChars(int logDebugMaxChars) {
        this.logDebugMaxChars = logDebugMaxChars;
    }

    public boolean isStreamCachingEnabled() {
        return streamCachingEnabled;
    }

    /**
     * Sets whether stream caching is enabled or not.
     *
     * Default is false.
     */
    public void setStreamCachingEnabled(boolean streamCachingEnabled) {
        this.streamCachingEnabled = streamCachingEnabled;
    }

    public String getStreamCachingSpoolDirectory() {
        return streamCachingSpoolDirectory;
    }

    /**
     * Sets the stream caching spool (temporary) directory to use for overflow and spooling to disk.
     *
     * If no spool directory has been explicit configured, then a temporary directory is created in the java.io.tmpdir
     * directory.
     */
    public void setStreamCachingSpoolDirectory(String streamCachingSpoolDirectory) {
        this.streamCachingSpoolDirectory = streamCachingSpoolDirectory;
    }

    public String getStreamCachingSpoolCipher() {
        return streamCachingSpoolCipher;
    }

    /**
     * Sets a stream caching cipher name to use when spooling to disk to write with encryption. By default the data is
     * not encrypted.
     */
    public void setStreamCachingSpoolCipher(String streamCachingSpoolCipher) {
        this.streamCachingSpoolCipher = streamCachingSpoolCipher;
    }

    public long getStreamCachingSpoolThreshold() {
        return streamCachingSpoolThreshold;
    }

    /**
     * Stream caching threshold in bytes when overflow to disk is activated. The default threshold is 128kb. Use -1 to
     * disable overflow to disk.
     */
    public void setStreamCachingSpoolThreshold(long streamCachingSpoolThreshold) {
        this.streamCachingSpoolThreshold = streamCachingSpoolThreshold;
    }

    public int getStreamCachingSpoolUsedHeapMemoryThreshold() {
        return streamCachingSpoolUsedHeapMemoryThreshold;
    }

    /**
     * Sets a percentage (1-99) of used heap memory threshold to activate stream caching spooling to disk.
     */
    public void setStreamCachingSpoolUsedHeapMemoryThreshold(int streamCachingSpoolUsedHeapMemoryThreshold) {
        this.streamCachingSpoolUsedHeapMemoryThreshold = streamCachingSpoolUsedHeapMemoryThreshold;
    }

    public String getStreamCachingSpoolUsedHeapMemoryLimit() {
        return streamCachingSpoolUsedHeapMemoryLimit;
    }

    /**
     * Sets what the upper bounds should be when streamCachingSpoolUsedHeapMemoryThreshold is in use.
     */
    public void setStreamCachingSpoolUsedHeapMemoryLimit(String streamCachingSpoolUsedHeapMemoryLimit) {
        this.streamCachingSpoolUsedHeapMemoryLimit = streamCachingSpoolUsedHeapMemoryLimit;
    }

    public boolean isStreamCachingAnySpoolRules() {
        return streamCachingAnySpoolRules;
    }

    /**
     * Sets whether if just any of the org.apache.camel.spi.StreamCachingStrategy.SpoolRule rules returns true then
     * shouldSpoolCache(long) returns true, to allow spooling to disk. If this option is false, then all the
     * org.apache.camel.spi.StreamCachingStrategy.SpoolRule must return true.
     *
     * The default value is false which means that all the rules must return true.
     */
    public void setStreamCachingAnySpoolRules(boolean streamCachingAnySpoolRules) {
        this.streamCachingAnySpoolRules = streamCachingAnySpoolRules;
    }

    public int getStreamCachingBufferSize() {
        return streamCachingBufferSize;
    }

    /**
     * Sets the stream caching buffer size to use when allocating in-memory buffers used for in-memory stream caches.
     *
     * The default size is 4096.
     */
    public void setStreamCachingBufferSize(int streamCachingBufferSize) {
        this.streamCachingBufferSize = streamCachingBufferSize;
    }

    public boolean isStreamCachingRemoveSpoolDirectoryWhenStopping() {
        return streamCachingRemoveSpoolDirectoryWhenStopping;
    }

    /**
     * Whether to remove stream caching temporary directory when stopping. This option is default true.
     */
    public void setStreamCachingRemoveSpoolDirectoryWhenStopping(boolean streamCachingRemoveSpoolDirectoryWhenStopping) {
        this.streamCachingRemoveSpoolDirectoryWhenStopping = streamCachingRemoveSpoolDirectoryWhenStopping;
    }

    public boolean isStreamCachingStatisticsEnabled() {
        return streamCachingStatisticsEnabled;
    }

    /**
     * Sets whether stream caching statistics is enabled.
     */
    public void setStreamCachingStatisticsEnabled(boolean streamCachingStatisticsEnabled) {
        this.streamCachingStatisticsEnabled = streamCachingStatisticsEnabled;
    }

    public boolean isTracing() {
        return tracing;
    }

    /**
     * Sets whether tracing is enabled or not.
     *
     * Default is false.
     */
    public void setTracing(boolean tracing) {
        this.tracing = tracing;
    }

    public boolean isTracingStandby() {
        return tracingStandby;
    }

    /**
     * Whether to set tracing on standby. If on standby then the tracer is installed and made available. Then the tracer
     * can be enabled later at runtime via JMX or via {@link Tracer#setEnabled(boolean)}.
     */
    public void setTracingStandby(boolean tracingStandby) {
        this.tracingStandby = tracingStandby;
    }

    public String getTracingPattern() {
        return tracingPattern;
    }

    /**
     * Tracing pattern to match which node EIPs to trace. For example to match all To EIP nodes, use to*. The pattern
     * matches by node and route id's Multiple patterns can be separated by comma.
     */
    public void setTracingPattern(String tracingPattern) {
        this.tracingPattern = tracingPattern;
    }

    public String getTracingLoggingFormat() {
        return tracingLoggingFormat;
    }

    /**
     * To use a custom tracing logging format.
     *
     * The default format (arrow, routeId, label) is: %-4.4s [%-12.12s] [%-33.33s]
     */
    public void setTracingLoggingFormat(String format) {
        tracingLoggingFormat = format;
    }

    public boolean isDebugging() {
        return debugging;
    }

    /**
     * Sets whether debugging is enabled or not.
     *
     * Default is false.
     */
    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
    }

    public boolean isBacklogTracing() {
        return backlogTracing;
    }

    /**
     * Sets whether backlog tracing is enabled or not.
     *
     * Default is false.
     */
    public void setBacklogTracing(boolean backlogTracing) {
        this.backlogTracing = backlogTracing;
    }

    public boolean isMessageHistory() {
        return messageHistory;
    }

    /**
     * Sets whether message history is enabled or not.
     *
     * Default is false.
     */
    public void setMessageHistory(boolean messageHistory) {
        this.messageHistory = messageHistory;
    }

    public boolean isSourceLocationEnabled() {
        return sourceLocationEnabled;
    }

    /**
     * Whether to capture precise source location:line-number for all EIPs in Camel routes.
     *
     * Enabling this will impact parsing Java based routes (also Groovy, Kotlin, etc.) on startup as this uses JDK
     * StackTraceElement to calculate the location from the Camel route, which comes with a performance cost. This only
     * impact startup, not the performance of the routes at runtime.
     */
    public void setSourceLocationEnabled(boolean sourceLocationEnabled) {
        this.sourceLocationEnabled = sourceLocationEnabled;
    }

    public boolean isLogMask() {
        return logMask;
    }

    /**
     * Sets whether log mask is enabled or not.
     *
     * Default is false.
     */
    public void setLogMask(boolean logMask) {
        this.logMask = logMask;
    }

    public boolean isLogExhaustedMessageBody() {
        return logExhaustedMessageBody;
    }

    /**
     * Sets whether to log exhausted message body with message history.
     *
     * Default is false.
     */
    public void setLogExhaustedMessageBody(boolean logExhaustedMessageBody) {
        this.logExhaustedMessageBody = logExhaustedMessageBody;
    }

    public boolean isAutoStartup() {
        return autoStartup;
    }

    /**
     * Sets whether the object should automatically start when Camel starts. Important: Currently only routes can be
     * disabled, as CamelContext's are always started. Note: When setting auto startup false on CamelContext then that
     * takes precedence and no routes are started. You would need to start CamelContext explicit using the
     * org.apache.camel.CamelContext.start() method, to start the context, and then you would need to start the routes
     * manually using CamelContext.getRouteController().startRoute(String).
     *
     * Default is true to always start up.
     */
    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    public boolean isAllowUseOriginalMessage() {
        return allowUseOriginalMessage;
    }

    /**
     * Sets whether to allow access to the original message from Camel's error handler, or from
     * org.apache.camel.spi.UnitOfWork.getOriginalInMessage(). Turning this off can optimize performance, as defensive
     * copy of the original message is not needed.
     *
     * Default is false.
     */
    public void setAllowUseOriginalMessage(boolean allowUseOriginalMessage) {
        this.allowUseOriginalMessage = allowUseOriginalMessage;
    }

    public boolean isCaseInsensitiveHeaders() {
        return caseInsensitiveHeaders;
    }

    /**
     * Whether to use case sensitive or insensitive headers.
     *
     * Important: When using case sensitive (this is set to false). Then the map is case sensitive which means headers
     * such as content-type and Content-Type are two different keys which can be a problem for some protocols such as
     * HTTP based, which rely on case insensitive headers. However case sensitive implementations can yield faster
     * performance. Therefore use case sensitive implementation with care.
     *
     * Default is true.
     */
    public void setCaseInsensitiveHeaders(boolean caseInsensitiveHeaders) {
        this.caseInsensitiveHeaders = caseInsensitiveHeaders;
    }

    public boolean isAutowiredEnabled() {
        return autowiredEnabled;
    }

    /**
     * Whether autowiring is enabled. This is used for automatic autowiring options (the option must be marked as
     * autowired) by looking up in the registry to find if there is a single instance of matching type, which then gets
     * configured on the component. This can be used for automatic configuring JDBC data sources, JMS connection
     * factories, AWS Clients, etc.
     *
     * Default is true.
     */
    public void setAutowiredEnabled(boolean autowiredEnabled) {
        this.autowiredEnabled = autowiredEnabled;
    }

    public boolean isEndpointRuntimeStatisticsEnabled() {
        return endpointRuntimeStatisticsEnabled;
    }

    /**
     * Sets whether endpoint runtime statistics is enabled (gathers runtime usage of each incoming and outgoing
     * endpoints).
     *
     * The default value is false.
     */
    public void setEndpointRuntimeStatisticsEnabled(boolean endpointRuntimeStatisticsEnabled) {
        this.endpointRuntimeStatisticsEnabled = endpointRuntimeStatisticsEnabled;
    }

    public boolean isEndpointLazyStartProducer() {
        return endpointLazyStartProducer;
    }

    /**
     * Whether the producer should be started lazy (on the first message). By starting lazy you can use this to allow
     * CamelContext and routes to startup in situations where a producer may otherwise fail during starting and cause
     * the route to fail being started. By deferring this startup to be lazy then the startup failure can be handled
     * during routing messages via Camel's routing error handlers. Beware that when the first message is processed then
     * creating and starting the producer may take a little time and prolong the total processing time of the
     * processing.
     *
     * The default value is false.
     */
    public void setEndpointLazyStartProducer(boolean endpointLazyStartProducer) {
        this.endpointLazyStartProducer = endpointLazyStartProducer;
    }

    public boolean isEndpointBridgeErrorHandler() {
        return endpointBridgeErrorHandler;
    }

    /**
     * Allows for bridging the consumer to the Camel routing Error Handler, which mean any exceptions occurred while the
     * consumer is trying to pickup incoming messages, or the likes, will now be processed as a message and handled by
     * the routing Error Handler.
     * <p/>
     * By default the consumer will use the org.apache.camel.spi.ExceptionHandler to deal with exceptions, that will be
     * logged at WARN/ERROR level and ignored.
     *
     * The default value is false.
     */
    public void setEndpointBridgeErrorHandler(boolean endpointBridgeErrorHandler) {
        this.endpointBridgeErrorHandler = endpointBridgeErrorHandler;
    }

    public boolean isUseDataType() {
        return useDataType;
    }

    /**
     * Whether to enable using data type on Camel messages.
     *
     * Data type are automatic turned on if one ore more routes has been explicit configured with input and output
     * types. Otherwise data type is default off.
     */
    public void setUseDataType(boolean useDataType) {
        this.useDataType = useDataType;
    }

    public boolean isUseBreadcrumb() {
        return useBreadcrumb;
    }

    /**
     * Set whether breadcrumb is enabled. The default value is false.
     */
    public void setUseBreadcrumb(boolean useBreadcrumb) {
        this.useBreadcrumb = useBreadcrumb;
    }

    public boolean isBeanPostProcessorEnabled() {
        return beanPostProcessorEnabled;
    }

    /**
     * Can be used to turn off bean post processing.
     *
     * Be careful to turn this off, as this means that beans that use Camel annotations such as
     * {@link org.apache.camel.EndpointInject}, {@link org.apache.camel.ProducerTemplate},
     * {@link org.apache.camel.Produce}, {@link org.apache.camel.Consume} etc will not be injected and in use.
     *
     * Turning this off should only be done if you are sure you do not use any of these Camel features.
     *
     * Not all runtimes allow turning this off (such as camel-blueprint or camel-cdi with XML).
     *
     * The default value is true (enabled).
     */
    public void setBeanPostProcessorEnabled(boolean beanPostProcessorEnabled) {
        this.beanPostProcessorEnabled = beanPostProcessorEnabled;
    }

    public ManagementStatisticsLevel getJmxManagementStatisticsLevel() {
        return jmxManagementStatisticsLevel;
    }

    /**
     * Sets the JMX statistics level, the level can be set to Extended to gather additional information
     *
     * The default value is Default.
     */
    public void setJmxManagementStatisticsLevel(ManagementStatisticsLevel jmxManagementStatisticsLevel) {
        this.jmxManagementStatisticsLevel = jmxManagementStatisticsLevel;
    }

    public String getJmxManagementNamePattern() {
        return jmxManagementNamePattern;
    }

    /**
     * The naming pattern for creating the CamelContext JMX management name.
     *
     * The default pattern is #name#
     */
    public void setJmxManagementNamePattern(String jmxManagementNamePattern) {
        this.jmxManagementNamePattern = jmxManagementNamePattern;
    }

    public boolean isUseMdcLogging() {
        return useMdcLogging;
    }

    /**
     * To turn on MDC logging
     */
    public void setUseMdcLogging(boolean useMdcLogging) {
        this.useMdcLogging = useMdcLogging;
    }

    public String getMdcLoggingKeysPattern() {
        return mdcLoggingKeysPattern;
    }

    /**
     * Sets the pattern used for determine which custom MDC keys to propagate during message routing when the routing
     * engine continues routing asynchronously for the given message. Setting this pattern to * will propagate all
     * custom keys. Or setting the pattern to foo*,bar* will propagate any keys starting with either foo or bar. Notice
     * that a set of standard Camel MDC keys are always propagated which starts with camel. as key name.
     *
     * The match rules are applied in this order (case insensitive):
     *
     * 1. exact match, returns true 2. wildcard match (pattern ends with a * and the name starts with the pattern),
     * returns true 3. regular expression match, returns true 4. otherwise returns false
     */
    public void setMdcLoggingKeysPattern(String mdcLoggingKeysPattern) {
        this.mdcLoggingKeysPattern = mdcLoggingKeysPattern;
    }

    public String getThreadNamePattern() {
        return threadNamePattern;
    }

    /**
     * Sets the thread name pattern used for creating the full thread name.
     *
     * The default pattern is: Camel (#camelId#) thread ##counter# - #name#
     *
     * Where #camelId# is the name of the CamelContext. and #counter# is a unique incrementing counter. and #name# is
     * the regular thread name.
     *
     * You can also use #longName# which is the long thread name which can includes endpoint parameters etc.
     */
    public void setThreadNamePattern(String threadNamePattern) {
        this.threadNamePattern = threadNamePattern;
    }

    public String getRouteFilterIncludePattern() {
        return routeFilterIncludePattern;
    }

    /**
     * Used for filtering routes matching the given pattern, which follows the following rules:
     *
     * - Match by route id - Match by route input endpoint uri
     *
     * The matching is using exact match, by wildcard and regular expression as documented by
     * {@link PatternHelper#matchPattern(String, String)}.
     *
     * For example to only include routes which starts with foo in their route id's, use: include=foo&#42; And to
     * exclude routes which starts from JMS endpoints, use: exclude=jms:&#42;
     *
     * Multiple patterns can be separated by comma, for example to exclude both foo and bar routes, use:
     * exclude=foo&#42;,bar&#42;
     *
     * Exclude takes precedence over include.
     */
    public void setRouteFilterIncludePattern(String include) {
        this.routeFilterIncludePattern = include;
    }

    public String getRouteFilterExcludePattern() {
        return routeFilterExcludePattern;
    }

    /**
     * Used for filtering routes routes matching the given pattern, which follows the following rules:
     *
     * - Match by route id - Match by route input endpoint uri
     *
     * The matching is using exact match, by wildcard and regular expression as documented by
     * {@link PatternHelper#matchPattern(String, String)}.
     *
     * For example to only include routes which starts with foo in their route id's, use: include=foo&#42; And to
     * exclude routes which starts from JMS endpoints, use: exclude=jms:&#42;
     *
     * Multiple patterns can be separated by comma, for example to exclude both foo and bar routes, use:
     * exclude=foo&#42;,bar&#42;
     *
     * Exclude takes precedence over include.
     */
    public void setRouteFilterExcludePattern(String exclude) {
        this.routeFilterExcludePattern = exclude;
    }

    public boolean isBeanIntrospectionExtendedStatistics() {
        return beanIntrospectionExtendedStatistics;
    }

    /**
     * Sets whether bean introspection uses extended statistics. The default is false.
     */
    public void setBeanIntrospectionExtendedStatistics(boolean beanIntrospectionExtendedStatistics) {
        this.beanIntrospectionExtendedStatistics = beanIntrospectionExtendedStatistics;
    }

    public LoggingLevel getBeanIntrospectionLoggingLevel() {
        return beanIntrospectionLoggingLevel;
    }

    /**
     * Sets the logging level used by bean introspection, logging activity of its usage. The default is TRACE.
     */
    public void setBeanIntrospectionLoggingLevel(LoggingLevel beanIntrospectionLoggingLevel) {
        this.beanIntrospectionLoggingLevel = beanIntrospectionLoggingLevel;
    }

    public boolean isRoutesCollectorEnabled() {
        return routesCollectorEnabled;
    }

    /**
     * Whether the routes collector is enabled or not.
     *
     * When enabled Camel will auto-discover routes (RouteBuilder instances from the registry and also load additional
     * routes from the file system).
     *
     * The routes collector is default enabled.
     */
    public void setRoutesCollectorEnabled(boolean routesCollectorEnabled) {
        this.routesCollectorEnabled = routesCollectorEnabled;
    }

    public String getJavaRoutesIncludePattern() {
        return javaRoutesIncludePattern;
    }

    /**
     * Used for inclusive filtering RouteBuilder classes which are collected from the registry or via classpath
     * scanning. The exclusive filtering takes precedence over inclusive filtering. The pattern is using Ant-path style
     * pattern. Multiple patterns can be specified separated by comma.
     *
     * Multiple patterns can be specified separated by comma. For example to include all classes starting with Foo use:
     * &#42;&#42;/Foo* To include all routes form a specific package use: com/mycompany/foo/&#42; To include all routes
     * form a specific package and its sub-packages use double wildcards: com/mycompany/foo/&#42;&#42; And to include
     * all routes from two specific packages use: com/mycompany/foo/&#42;,com/mycompany/stuff/&#42;
     */
    public void setJavaRoutesIncludePattern(String javaRoutesIncludePattern) {
        this.javaRoutesIncludePattern = javaRoutesIncludePattern;
    }

    public String getJavaRoutesExcludePattern() {
        return javaRoutesExcludePattern;
    }

    /**
     * Used for exclusive filtering RouteBuilder classes which are collected from the registry or via classpath
     * scanning. The exclusive filtering takes precedence over inclusive filtering. The pattern is using Ant-path style
     * pattern. Multiple patterns can be specified separated by comma.
     *
     * For example to exclude all classes starting with Bar use: &#42;&#42;/Bar&#42; To exclude all routes form a
     * specific package use: com/mycompany/bar/&#42; To exclude all routes form a specific package and its sub-packages
     * use double wildcards: com/mycompany/bar/&#42;&#42; And to exclude all routes from two specific packages use:
     * com/mycompany/bar/&#42;,com/mycompany/stuff/&#42;
     */
    public void setJavaRoutesExcludePattern(String javaRoutesExcludePattern) {
        this.javaRoutesExcludePattern = javaRoutesExcludePattern;
    }

    public String getRoutesIncludePattern() {
        return routesIncludePattern;
    }

    /**
     * Used for inclusive filtering of routes from directories. The exclusive filtering takes precedence over inclusive
     * filtering. The pattern is using Ant-path style pattern.
     *
     * Multiple patterns can be specified separated by comma, as example, to include all the routes from a directory
     * whose name contains foo use: &#42;&#42;/*foo*.
     */
    public void setRoutesIncludePattern(String routesIncludePattern) {
        this.routesIncludePattern = routesIncludePattern;
    }

    public String getRoutesExcludePattern() {
        return routesExcludePattern;
    }

    /**
     * Used for exclusive filtering of routes from directories. The exclusive filtering takes precedence over inclusive
     * filtering. The pattern is using Ant-path style pattern.
     *
     * Multiple patterns can be specified separated by comma, as example, to exclude all the routes from a directory
     * whose name contains foo use: &#42;&#42;/*foo*.
     */
    public void setRoutesExcludePattern(String routesExcludePattern) {
        this.routesExcludePattern = routesExcludePattern;
    }

    public boolean isRoutesReloadEnabled() {
        return routesReloadEnabled;
    }

    /**
     * Used for enabling automatic routes reloading. If enabled then Camel will watch for file changes in the given
     * reload directory, and trigger reloading routes if files are changed.
     */
    public void setRoutesReloadEnabled(boolean routesReloadEnabled) {
        this.routesReloadEnabled = routesReloadEnabled;
    }

    public String getRoutesReloadDirectory() {
        return routesReloadDirectory;
    }

    /**
     * Directory to scan for route changes. Camel cannot scan the classpath, so this must be configured to a file
     * directory. Development with Maven as build tool, you can configure the directory to be src/main/resources to scan
     * for Camel routes in XML or YAML files.
     */
    public void setRoutesReloadDirectory(String routesReloadDirectory) {
        this.routesReloadDirectory = routesReloadDirectory;
    }

    public boolean isRoutesReloadDirectoryRecursive() {
        return routesReloadDirectoryRecursive;
    }

    /**
     * Whether the directory to scan should include sub directories.
     *
     * Depending on the number of sub directories, then this can cause the JVM to startup slower as Camel uses the JDK
     * file-watch service to scan for file changes.
     */
    public void setRoutesReloadDirectoryRecursive(boolean routesReloadDirectoryRecursive) {
        this.routesReloadDirectoryRecursive = routesReloadDirectoryRecursive;
    }

    public String getRoutesReloadPattern() {
        return routesReloadPattern;
    }

    /**
     * Used for inclusive filtering of routes from directories.
     *
     * Typical used for specifying to accept routes in XML or YAML files, such as <tt>*.yaml,*.xml</tt>. Multiple
     * patterns can be specified separated by comma.
     */
    public void setRoutesReloadPattern(String routesReloadPattern) {
        this.routesReloadPattern = routesReloadPattern;
    }

    public boolean isRoutesReloadRemoveAllRoutes() {
        return routesReloadRemoveAllRoutes;
    }

    /**
     * When reloading routes should all existing routes be stopped and removed.
     *
     * By default, Camel will stop and remove all existing routes before reloading routes. This ensures that only the
     * reloaded routes will be active. If disabled then only routes with the same route id is updated, and any existing
     * routes are continued to run.
     */
    public void setRoutesReloadRemoveAllRoutes(boolean routesReloadRemoveAllRoutes) {
        this.routesReloadRemoveAllRoutes = routesReloadRemoveAllRoutes;
    }

    public boolean isRoutesReloadRestartDuration() {
        return routesReloadRestartDuration;
    }

    /**
     * Whether to restart max duration when routes are reloaded. For example if max duration is 60 seconds, and a route
     * is reloaded after 25 seconds, then this will restart the count and wait 60 seconds again.
     */
    public void setRoutesReloadRestartDuration(boolean routesReloadRestartDuration) {
        this.routesReloadRestartDuration = routesReloadRestartDuration;
    }

    public boolean isLightweight() {
        return lightweight;
    }

    /**
     * Configure the context to be lightweight. This will trigger some optimizations and memory reduction options.
     * Lightweight context have some limitations. At this moment, dynamic endpoint destinations are not supported.
     */
    public void setLightweight(boolean lightweight) {
        this.lightweight = lightweight;
    }

    public boolean isEagerClassloading() {
        return eagerClassloading;
    }

    /**
     * Whether to eager load a common set of Camel classes that would otherwise first be loaded on processing the first
     * message. By eager loading these classes then the JVM has already loaded the classes during build phase, which
     * allows Camel to process the first message faster.
     */
    public void setEagerClassloading(boolean eagerClassloading) {
        this.eagerClassloading = eagerClassloading;
    }

    public String getExchangeFactory() {
        return exchangeFactory;
    }

    /**
     * Controls whether to pool (reuse) exchanges or create new exchanges (prototype). Using pooled will reduce JVM
     * garbage collection overhead by avoiding to re-create Exchange instances per message each consumer receives. The
     * default is prototype mode.
     */
    public void setExchangeFactory(String exchangeFactory) {
        this.exchangeFactory = exchangeFactory;
    }

    /**
     * The capacity the pool (for each consumer) uses for storing exchanges. The default capacity is 100.
     */
    public int getExchangeFactoryCapacity() {
        return exchangeFactoryCapacity;
    }

    /**
     * The capacity the pool (for each consumer) uses for storing exchanges. The default capacity is 100.
     */
    public void setExchangeFactoryCapacity(int exchangeFactoryCapacity) {
        this.exchangeFactoryCapacity = exchangeFactoryCapacity;
    }

    public boolean isExchangeFactoryStatisticsEnabled() {
        return exchangeFactoryStatisticsEnabled;
    }

    /**
     * Configures whether statistics is enabled on exchange factory.
     */
    public void setExchangeFactoryStatisticsEnabled(boolean exchangeFactoryStatisticsEnabled) {
        this.exchangeFactoryStatisticsEnabled = exchangeFactoryStatisticsEnabled;
    }

    public boolean isDumpRoutes() {
        return dumpRoutes;
    }

    /**
     * If dumping is enabled then Camel will during startup dump all loaded routes (incl rests and route templates)
     * represented as XML DSL into the log. This is intended for trouble shooting or to assist during development.
     *
     * Sensitive information that may be configured in the route endpoints could potentially be included in the dump
     * output and is therefore not recommended to be used for production usage.
     *
     * This requires to have camel-xml-jaxb on the classpath to be able to dump the routes as XML.
     */
    public void setDumpRoutes(boolean dumpRoutes) {
        this.dumpRoutes = dumpRoutes;
    }

    public Map<String, String> getGlobalOptions() {
        return globalOptions;
    }

    /**
     * Sets global options that can be referenced in the camel context
     * <p/>
     * <b>Important:</b> This has nothing to do with property placeholders, and is just a plain set of key/value pairs
     * which are used to configure global options on CamelContext, such as a maximum debug logging length etc.
     */
    public void setGlobalOptions(Map<String, String> globalOptions) {
        this.globalOptions = globalOptions;
    }

    @Deprecated
    public LoggingLevel getRouteControllerLoggingLevel() {
        return routeControllerLoggingLevel;
    }

    /**
     * Sets the logging level used for logging route activity (such as starting and stopping routes). The default
     * logging level is DEBUG.
     */
    @Deprecated
    public void setRouteControllerLoggingLevel(LoggingLevel routeControllerLoggingLevel) {
        this.routeControllerLoggingLevel = routeControllerLoggingLevel;
    }

    public boolean isRouteControllerSuperviseEnabled() {
        return routeControllerSuperviseEnabled;
    }

    /**
     * To enable using supervising route controller which allows Camel to startup and then the controller takes care of
     * starting the routes in a safe manner.
     *
     * This can be used when you want to startup Camel despite a route may otherwise fail fast during startup and cause
     * Camel to fail to startup as well. By delegating the route startup to the supervising route controller then its
     * manages the startup using a background thread. The controller allows to be configured with various settings to
     * attempt to restart failing routes.
     */
    public void setRouteControllerSuperviseEnabled(boolean routeControllerSuperviseEnabled) {
        this.routeControllerSuperviseEnabled = routeControllerSuperviseEnabled;
    }

    public String getRouteControllerIncludeRoutes() {
        return routeControllerIncludeRoutes;
    }

    /**
     * Pattern for filtering routes to be included as supervised.
     *
     * The pattern is matching on route id, and endpoint uri for the route. Multiple patterns can be separated by comma.
     *
     * For example to include all kafka routes, you can say <tt>kafka:*</tt>. And to include routes with specific route
     * ids <tt>myRoute,myOtherRoute</tt>. The pattern supports wildcards and uses the matcher from
     * org.apache.camel.support.PatternHelper#matchPattern.
     */
    public void setRouteControllerIncludeRoutes(String routeControllerIncludeRoutes) {
        this.routeControllerIncludeRoutes = routeControllerIncludeRoutes;
    }

    public String getRouteControllerExcludeRoutes() {
        return routeControllerExcludeRoutes;
    }

    /**
     * Pattern for filtering routes to be excluded as supervised.
     *
     * The pattern is matching on route id, and endpoint uri for the route. Multiple patterns can be separated by comma.
     *
     * For example to exclude all JMS routes, you can say <tt>jms:*</tt>. And to exclude routes with specific route ids
     * <tt>mySpecialRoute,myOtherSpecialRoute</tt>. The pattern supports wildcards and uses the matcher from
     * org.apache.camel.support.PatternHelper#matchPattern.
     */
    public void setRouteControllerExcludeRoutes(String routeControllerExcludeRoutes) {
        this.routeControllerExcludeRoutes = routeControllerExcludeRoutes;
    }

    public int getRouteControllerThreadPoolSize() {
        return routeControllerThreadPoolSize;
    }

    /**
     * The number of threads used by the route controller scheduled thread pool that are used for restarting routes. The
     * pool uses 1 thread by default, but you can increase this to allow the controller to concurrently attempt to
     * restart multiple routes in case more than one route has problems starting.
     */
    public void setRouteControllerThreadPoolSize(int routeControllerThreadPoolSize) {
        this.routeControllerThreadPoolSize = routeControllerThreadPoolSize;
    }

    public long getRouteControllerInitialDelay() {
        return routeControllerInitialDelay;
    }

    /**
     * Initial delay in milli seconds before the route controller starts, after CamelContext has been started.
     */
    public void setRouteControllerInitialDelay(long routeControllerInitialDelay) {
        this.routeControllerInitialDelay = routeControllerInitialDelay;
    }

    public long getRouteControllerBackOffDelay() {
        return routeControllerBackOffDelay;
    }

    /**
     * Backoff delay in millis when restarting a route that failed to startup.
     */
    public void setRouteControllerBackOffDelay(long routeControllerBackOffDelay) {
        this.routeControllerBackOffDelay = routeControllerBackOffDelay;
    }

    public long getRouteControllerBackOffMaxDelay() {
        return routeControllerBackOffMaxDelay;
    }

    /**
     * Backoff maximum delay in millis when restarting a route that failed to startup.
     */
    public void setRouteControllerBackOffMaxDelay(long routeControllerBackOffMaxDelay) {
        this.routeControllerBackOffMaxDelay = routeControllerBackOffMaxDelay;
    }

    public long getRouteControllerBackOffMaxElapsedTime() {
        return routeControllerBackOffMaxElapsedTime;
    }

    /**
     * Backoff maximum elapsed time in millis, after which the backoff should be considered exhausted and no more
     * attempts should be made.
     */
    public void setRouteControllerBackOffMaxElapsedTime(long routeControllerBackOffMaxElapsedTime) {
        this.routeControllerBackOffMaxElapsedTime = routeControllerBackOffMaxElapsedTime;
    }

    public long getRouteControllerBackOffMaxAttempts() {
        return routeControllerBackOffMaxAttempts;
    }

    /**
     * Backoff maximum number of attempts to restart a route that failed to startup. When this threshold has been
     * exceeded then the controller will give up attempting to restart the route, and the route will remain as stopped.
     */
    public void setRouteControllerBackOffMaxAttempts(long routeControllerBackOffMaxAttempts) {
        this.routeControllerBackOffMaxAttempts = routeControllerBackOffMaxAttempts;
    }

    public double getRouteControllerBackOffMultiplier() {
        return routeControllerBackOffMultiplier;
    }

    /**
     * Backoff multiplier to use for exponential backoff. This is used to extend the delay between restart attempts.
     */
    public void setRouteControllerBackOffMultiplier(double routeControllerBackOffMultiplier) {
        this.routeControllerBackOffMultiplier = routeControllerBackOffMultiplier;
    }

    public boolean isRouteControllerUnhealthyOnExhausted() {
        return routeControllerUnhealthyOnExhausted;
    }

    /**
     * Whether to mark the route as unhealthy (down) when all restarting attempts (backoff) have failed and the route is
     * not successfully started and the route manager is giving up.
     *
     * Setting this to true allows health checks to know about this and can report the Camel application as DOWN.
     *
     * The default is false.
     */
    public void setRouteControllerUnhealthyOnExhausted(boolean routeControllerUnhealthyOnExhausted) {
        this.routeControllerUnhealthyOnExhausted = routeControllerUnhealthyOnExhausted;
    }

    public String getStartupRecorder() {
        return startupRecorder;
    }

    /**
     * To use startup recorder for capturing execution time during starting Camel. The recorder can be one of: false (or
     * off), logging, java-flight-recorder (or jfr).
     */
    public void setStartupRecorder(String startupRecorder) {
        this.startupRecorder = startupRecorder;
    }

    public int getStartupRecorderMaxDepth() {
        return startupRecorderMaxDepth;
    }

    /**
     * To filter our sub steps at a maximum depth.
     *
     * Use -1 for no maximum. Use 0 for no sub steps. Use 1 for max 1 sub step, and so forth.
     *
     * The default is -1.
     */
    public void setStartupRecorderMaxDepth(int startupRecorderMaxDepth) {
        this.startupRecorderMaxDepth = startupRecorderMaxDepth;
    }

    public boolean isStartupRecorderRecording() {
        return startupRecorderRecording;
    }

    /**
     * To enable Java Flight Recorder to start a recording and automatic dump the recording to disk after startup is
     * complete.
     *
     * This requires that camel-jfr is on the classpath, and to enable this option.
     */
    public void setStartupRecorderRecording(boolean startupRecorderRecording) {
        this.startupRecorderRecording = startupRecorderRecording;
    }

    public String getStartupRecorderProfile() {
        return startupRecorderProfile;
    }

    /**
     * To use a specific Java Flight Recorder profile configuration, such as default or profile.
     *
     * The default is default.
     */
    public void setStartupRecorderProfile(String startupRecorderProfile) {
        this.startupRecorderProfile = startupRecorderProfile;
    }

    public long getStartupRecorderDuration() {
        return startupRecorderDuration;
    }

    /**
     * How long time to run the startup recorder.
     *
     * Use 0 (default) to keep the recorder running until the JVM is exited. Use -1 to stop the recorder right after
     * Camel has been started (to only focus on potential Camel startup performance bottlenecks) Use a positive value to
     * keep recording for N seconds.
     *
     * When the recorder is stopped then the recording is auto saved to disk (note: save to disk can be disabled by
     * setting startupRecorderDir to false)
     */
    public void setStartupRecorderDuration(long startupRecorderDuration) {
        this.startupRecorderDuration = startupRecorderDuration;
    }

    public String getStartupRecorderDir() {
        return startupRecorderDir;
    }

    /**
     * Directory to store the recording. By default the current directory will be used. Use false to turn off saving
     * recording to disk.
     */
    public void setStartupRecorderDir(String startupRecorderDir) {
        this.startupRecorderDir = startupRecorderDir;
    }

    // fluent builders
    // --------------------------------------------------------------

    /**
     * Sets the name of the CamelContext.
     */
    public T withName(String name) {
        this.name = name;
        return (T) this;
    }

    /**
     * To specify for how long time in seconds to keep running the JVM before automatic terminating the JVM. You can use
     * this to run Camel for a short while.
     */
    public T withDurationMaxSeconds(int durationMaxSeconds) {
        this.durationMaxSeconds = durationMaxSeconds;
        return (T) this;
    }

    /**
     * To specify for how long time in seconds Camel can be idle before automatic terminating the JVM. You can use this
     * to run Camel for a short while.
     */
    public T withDurationMaxIdleSeconds(int durationMaxIdleSeconds) {
        this.durationMaxIdleSeconds = durationMaxIdleSeconds;
        return (T) this;
    }

    /**
     * To specify how many messages to process by Camel before automatic terminating the JVM. You can use this to run
     * Camel for a short while.
     */
    public T withDurationMaxMessages(int durationMaxMessages) {
        this.durationMaxMessages = durationMaxMessages;
        return (T) this;
    }

    /**
     * Controls whether the Camel application should shutdown the JVM, or stop all routes, when duration max is
     * triggered.
     */
    public T withDurationMaxAction(String durationMaxAction) {
        this.durationMaxAction = durationMaxAction;
        return (T) this;
    }

    /**
     * Timeout in seconds to graceful shutdown Camel.
     */
    public T withShutdownTimeout(int shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
        return (T) this;
    }

    /**
     * Whether Camel should try to suppress logging during shutdown and timeout was triggered, meaning forced shutdown
     * is happening. And during forced shutdown we want to avoid logging errors/warnings et all in the logs as a
     * side-effect of the forced timeout. Notice the suppress is a best effort as there may still be some logs coming
     * from 3rd party libraries and whatnot, which Camel cannot control. This option is default false.
     */
    public T withShutdownSuppressLoggingOnTimeout(boolean shutdownSuppressLoggingOnTimeout) {
        this.shutdownSuppressLoggingOnTimeout = shutdownSuppressLoggingOnTimeout;
        return (T) this;
    }

    /**
     * Sets whether to force shutdown of all consumers when a timeout occurred and thus not all consumers was shutdown
     * within that period.
     *
     * You should have good reasons to set this option to false as it means that the routes keep running and is halted
     * abruptly when CamelContext has been shutdown.
     */
    public T withShutdownNowOnTimeout(boolean shutdownNowOnTimeout) {
        this.shutdownNowOnTimeout = shutdownNowOnTimeout;
        return (T) this;
    }

    /**
     * Sets whether routes should be shutdown in reverse or the same order as they where started.
     */
    public T withShutdownRoutesInReverseOrder(boolean shutdownRoutesInReverseOrder) {
        this.shutdownRoutesInReverseOrder = shutdownRoutesInReverseOrder;
        return (T) this;
    }

    /**
     * Sets whether to log information about the inflight Exchanges which are still running during a shutdown which
     * didn't complete without the given timeout.
     *
     * This requires to enable the option inflightRepositoryExchangeEnabled.
     */
    public T withShutdownLogInflightExchangesOnTimeout(boolean shutdownLogInflightExchangesOnTimeout) {
        this.shutdownLogInflightExchangesOnTimeout = shutdownLogInflightExchangesOnTimeout;
        return (T) this;
    }

    /**
     * Sets whether the inflight repository should allow browsing each inflight exchange.
     *
     * This is by default disabled as there is a very slight performance overhead when enabled.
     */
    public T withInflightRepositoryBrowseEnabled(boolean inflightRepositoryBrowseEnabled) {
        this.inflightRepositoryBrowseEnabled = inflightRepositoryBrowseEnabled;
        return (T) this;
    }

    /**
     * Directory to load additional configuration files that contains configuration values that takes precedence over
     * any other configuration. This can be used to refer to files that may have secret configuration that has been
     * mounted on the file system for containers.
     *
     * You can specify a pattern to load from sub directories and a name pattern such as /var/app/secret/*.properties,
     * multiple directories can be separated by comma.
     */
    public T withFileConfigurations(String fileConfigurations) {
        this.fileConfigurations = fileConfigurations;
        return (T) this;
    }

    /**
     * Enable JMX in your Camel application.
     */
    public T withJmxEnabled(boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
        return (T) this;
    }

    /**
     * Producer template endpoints cache size.
     */
    public T withProducerTemplateCacheSize(int producerTemplateCacheSize) {
        this.producerTemplateCacheSize = producerTemplateCacheSize;
        return (T) this;
    }

    /**
     * Consumer template endpoints cache size.
     */
    public T withConsumerTemplateCacheSize(int consumerTemplateCacheSize) {
        this.consumerTemplateCacheSize = consumerTemplateCacheSize;
        return (T) this;
    }

    /**
     * Whether to load custom type converters by scanning classpath. This is used for backwards compatibility with Camel
     * 2.x. Its recommended to migrate to use fast type converter loading by setting <tt>@Converter(loader = true)</tt>
     * on your custom type converter classes.
     */
    public T withLoadTypeConverters(boolean loadTypeConverters) {
        this.loadTypeConverters = loadTypeConverters;
        return (T) this;
    }

    /**
     * Whether to load custom health checks by scanning classpath.
     */
    public T withLoadHealthChecks(boolean loadHealthChecks) {
        this.loadHealthChecks = loadHealthChecks;
        return (T) this;
    }

    /**
     * Whether camel-k style modeline is also enabled when not using camel-k. Enabling this allows to use a camel-k like
     * experience by being able to configure various settings using modeline directly in your route source code.
     */
    public T withModeline(boolean modeline) {
        this.modeline = modeline;
        return (T) this;
    }

    /**
     * Whether to enable developer console (requires camel-console on classpath).
     *
     * The developer console is only for assisting during development. This is NOT for production usage.
     */
    public T withDevConsoleEnabled(boolean devConsoleEnabled) {
        this.devConsoleEnabled = devConsoleEnabled;
        return (T) this;
    }

    /**
     * Is used to limit the maximum length of the logging Camel message bodies. If the message body is longer than the
     * limit, the log message is clipped. Use -1 to have unlimited length. Use for example 1000 to log at most 1000
     * characters.
     */
    public T withLogDebugMaxChars(int logDebugMaxChars) {
        this.logDebugMaxChars = logDebugMaxChars;
        return (T) this;
    }

    /**
     * Sets whether stream caching is enabled or not.
     *
     * Default is false.
     */
    public T withStreamCachingEnabled(boolean streamCachingEnabled) {
        this.streamCachingEnabled = streamCachingEnabled;
        return (T) this;
    }

    /**
     * Sets the stream caching spool (temporary) directory to use for overflow and spooling to disk.
     *
     * If no spool directory has been explicit configured, then a temporary directory is created in the java.io.tmpdir
     * directory.
     */
    public T withStreamCachingSpoolDirectory(String streamCachingSpoolDirectory) {
        this.streamCachingSpoolDirectory = streamCachingSpoolDirectory;
        return (T) this;
    }

    /**
     * Sets a stream caching cipher name to use when spooling to disk to write with encryption. By default the data is
     * not encrypted.
     */
    public T withStreamCachingSpoolCipher(String streamCachingSpoolCipher) {
        this.streamCachingSpoolCipher = streamCachingSpoolCipher;
        return (T) this;
    }

    /**
     * Stream caching threshold in bytes when overflow to disk is activated. The default threshold is 128kb. Use -1 to
     * disable overflow to disk.
     */
    public T withStreamCachingSpoolThreshold(long streamCachingSpoolThreshold) {
        this.streamCachingSpoolThreshold = streamCachingSpoolThreshold;
        return (T) this;
    }

    /**
     * Sets a percentage (1-99) of used heap memory threshold to activate stream caching spooling to disk.
     */
    public T withStreamCachingSpoolUsedHeapMemoryThreshold(int streamCachingSpoolUsedHeapMemoryThreshold) {
        this.streamCachingSpoolUsedHeapMemoryThreshold = streamCachingSpoolUsedHeapMemoryThreshold;
        return (T) this;
    }

    /**
     * Sets what the upper bounds should be when streamCachingSpoolUsedHeapMemoryThreshold is in use.
     */
    public T withStreamCachingSpoolUsedHeapMemoryLimit(String streamCachingSpoolUsedHeapMemoryLimit) {
        this.streamCachingSpoolUsedHeapMemoryLimit = streamCachingSpoolUsedHeapMemoryLimit;
        return (T) this;
    }

    /**
     * Sets whether if just any of the org.apache.camel.spi.StreamCachingStrategy.SpoolRule rules returns true then
     * shouldSpoolCache(long) returns true, to allow spooling to disk. If this option is false, then all the
     * org.apache.camel.spi.StreamCachingStrategy.SpoolRule must return true.
     *
     * The default value is false which means that all the rules must return true.
     */
    public T withStreamCachingAnySpoolRules(boolean streamCachingAnySpoolRules) {
        this.streamCachingAnySpoolRules = streamCachingAnySpoolRules;
        return (T) this;
    }

    /**
     * Sets the stream caching buffer size to use when allocating in-memory buffers used for in-memory stream caches.
     *
     * The default size is 4096.
     */
    public T withStreamCachingBufferSize(int streamCachingBufferSize) {
        this.streamCachingBufferSize = streamCachingBufferSize;
        return (T) this;
    }

    /**
     * Whether to remove stream caching temporary directory when stopping. This option is default true.
     */
    public T withStreamCachingRemoveSpoolDirectoryWhenStopping(boolean streamCachingRemoveSpoolDirectoryWhenStopping) {
        this.streamCachingRemoveSpoolDirectoryWhenStopping = streamCachingRemoveSpoolDirectoryWhenStopping;
        return (T) this;
    }

    /**
     * Sets whether stream caching statistics is enabled.
     */
    public T withStreamCachingStatisticsEnabled(boolean streamCachingStatisticsEnabled) {
        this.streamCachingStatisticsEnabled = streamCachingStatisticsEnabled;
        return (T) this;
    }

    /**
     * Sets whether tracing is enabled or not.
     *
     * Default is false.
     */
    public T withTracing(boolean tracing) {
        this.tracing = tracing;
        return (T) this;
    }

    /**
     * Whether to set tracing on standby. If on standby then the tracer is installed and made available. Then the tracer
     * can be enabled later at runtime via JMX or via {@link Tracer#setEnabled(boolean)}.
     *
     * Default is false.
     */
    public T withTracingStandby(boolean tracingStandby) {
        this.tracingStandby = tracingStandby;
        return (T) this;
    }

    /**
     * Sets whether backlog tracing is enabled or not.
     *
     * Default is false.
     */
    public T withBacklogTracing(boolean backlogTracing) {
        this.backlogTracing = backlogTracing;
        return (T) this;
    }

    /**
     * Sets whether message history is enabled or not.
     *
     * Default is false.
     */
    public T withMessageHistory(boolean messageHistory) {
        this.messageHistory = messageHistory;
        return (T) this;
    }

    /**
     * Whether to capture precise source location:line-number for all EIPs in Camel routes.
     *
     * Enabling this will impact parsing Java based routes (also Groovy, Kotlin, etc.) on startup as this uses JDK
     * StackTraceElement to calculate the location from the Camel route, which comes with a performance cost. This only
     * impact startup, not the performance of the routes at runtime.
     */
    public T withSourceLocationEnabled(boolean sourceLocationEnabled) {
        this.sourceLocationEnabled = sourceLocationEnabled;
        return (T) this;
    }

    /**
     * Sets whether log mask is enabled or not.
     *
     * Default is false.
     */
    public T withLogMask(boolean logMask) {
        this.logMask = logMask;
        return (T) this;
    }

    /**
     * Sets whether to log exhausted message body with message history.
     *
     * Default is false.
     */
    public T withLogExhaustedMessageBody(boolean logExhaustedMessageBody) {
        this.logExhaustedMessageBody = logExhaustedMessageBody;
        return (T) this;
    }

    /**
     * Sets whether the object should automatically start when Camel starts. Important: Currently only routes can be
     * disabled, as CamelContext's are always started. Note: When setting auto startup false on CamelContext then that
     * takes precedence and no routes is started. You would need to start CamelContext explicit using the
     * org.apache.camel.CamelContext.start() method, to start the context, and then you would need to start the routes
     * manually using CamelContext.getRouteController().startRoute(String).
     *
     * Default is true to always start up.
     */
    public T withAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
        return (T) this;
    }

    /**
     * Sets whether to allow access to the original message from Camel's error handler, or from
     * org.apache.camel.spi.UnitOfWork.getOriginalInMessage(). Turning this off can optimize performance, as defensive
     * copy of the original message is not needed.
     *
     * Default is false.
     */
    public T withAllowUseOriginalMessage(boolean allowUseOriginalMessage) {
        this.allowUseOriginalMessage = allowUseOriginalMessage;
        return (T) this;
    }

    /**
     * Whether to use case sensitive or insensitive headers.
     *
     * Important: When using case sensitive (this is set to false). Then the map is case sensitive which means headers
     * such as content-type and Content-Type are two different keys which can be a problem for some protocols such as
     * HTTP based, which rely on case insensitive headers. However case sensitive implementations can yield faster
     * performance. Therefore use case sensitive implementation with care.
     *
     * Default is true.
     */
    public T withCaseInsensitiveHeaders(boolean caseInsensitiveHeaders) {
        this.caseInsensitiveHeaders = caseInsensitiveHeaders;
        return (T) this;
    }

    /**
     * Sets whether endpoint runtime statistics is enabled (gathers runtime usage of each incoming and outgoing
     * endpoints).
     *
     * The default value is false.
     */
    public T withEndpointRuntimeStatisticsEnabled(boolean endpointRuntimeStatisticsEnabled) {
        this.endpointRuntimeStatisticsEnabled = endpointRuntimeStatisticsEnabled;
        return (T) this;
    }

    /**
     * Whether the producer should be started lazy (on the first message). By starting lazy you can use this to allow
     * CamelContext and routes to startup in situations where a producer may otherwise fail during starting and cause
     * the route to fail being started. By deferring this startup to be lazy then the startup failure can be handled
     * during routing messages via Camel's routing error handlers. Beware that when the first message is processed then
     * creating and starting the producer may take a little time and prolong the total processing time of the
     * processing.
     *
     * The default value is false.
     */
    public T withEndpointLazyStartProducer(boolean endpointLazyStartProducer) {
        this.endpointLazyStartProducer = endpointLazyStartProducer;
        return (T) this;
    }

    /**
     * Allows for bridging the consumer to the Camel routing Error Handler, which mean any exceptions occurred while the
     * consumer is trying to pickup incoming messages, or the likes, will now be processed as a message and handled by
     * the routing Error Handler.
     * <p/>
     * By default the consumer will use the org.apache.camel.spi.ExceptionHandler to deal with exceptions, that will be
     * logged at WARN/ERROR level and ignored.
     *
     * The default value is false.
     */
    public T withEndpointBridgeErrorHandler(boolean endpointBridgeErrorHandler) {
        this.endpointBridgeErrorHandler = endpointBridgeErrorHandler;
        return (T) this;
    }

    /**
     * Whether to enable using data type on Camel messages.
     *
     * Data type are automatic turned on if one ore more routes has been explicit configured with input and output
     * types. Otherwise data type is default off.
     */
    public T withUseDataType(boolean useDataType) {
        this.useDataType = useDataType;
        return (T) this;
    }

    /**
     * Set whether breadcrumb is enabled. The default value is false.
     */
    public T withUseBreadcrumb(boolean useBreadcrumb) {
        this.useBreadcrumb = useBreadcrumb;
        return (T) this;
    }

    /**
     * Can be used to turn off bean post processing.
     *
     * Be careful to turn this off, as this means that beans that use Camel annotations such as
     * {@link org.apache.camel.EndpointInject}, {@link org.apache.camel.ProducerTemplate},
     * {@link org.apache.camel.Produce}, {@link org.apache.camel.Consume} etc will not be injected and in use.
     *
     * Turning this off should only be done if you are sure you do not use any of these Camel features.
     *
     * Not all runtimes allow turning this off (such as camel-blueprint or camel-cdi with XML).
     *
     * The default value is true (enabled).
     */
    public T withBeanPostProcessorEnabled(boolean beanPostProcessorEnabled) {
        this.beanPostProcessorEnabled = beanPostProcessorEnabled;
        return (T) this;
    }

    /**
     * Sets the JMX statistics level The level can be set to Extended to gather additional information
     *
     * The default value is Default.
     */
    public T withJmxManagementStatisticsLevel(ManagementStatisticsLevel jmxManagementStatisticsLevel) {
        this.jmxManagementStatisticsLevel = jmxManagementStatisticsLevel;
        return (T) this;
    }

    /**
     * The naming pattern for creating the CamelContext JMX management name.
     *
     * The default pattern is #name#
     */
    public T withJmxManagementNamePattern(String jmxManagementNamePattern) {
        this.jmxManagementNamePattern = jmxManagementNamePattern;
        return (T) this;
    }

    /**
     * To turn on MDC logging
     */
    public T withUseMdcLogging(boolean useMdcLogging) {
        this.useMdcLogging = useMdcLogging;
        return (T) this;
    }

    /**
     * Sets the thread name pattern used for creating the full thread name.
     *
     * The default pattern is: Camel (#camelId#) thread ##counter# - #name#
     *
     * Where #camelId# is the name of the CamelContext. and #counter# is a unique incrementing counter. and #name# is
     * the regular thread name.
     *
     * You can also use #longName# which is the long thread name which can includes endpoint parameters etc.
     */
    public T withThreadNamePattern(String threadNamePattern) {
        this.threadNamePattern = threadNamePattern;
        return (T) this;
    }

    /**
     * Used for filtering routes routes matching the given pattern, which follows the following rules:
     *
     * - Match by route id - Match by route input endpoint uri
     *
     * The matching is using exact match, by wildcard and regular expression as documented by
     * {@link PatternHelper#matchPattern(String, String)}.
     *
     * For example to only include routes which starts with foo in their route id's, use: include=foo&#42; And to
     * exclude routes which starts from JMS endpoints, use: exclude=jms:&#42;
     *
     * Multiple patterns can be separated by comma, for example to exclude both foo and bar routes, use:
     * exclude=foo&#42;,bar&#42;
     *
     * Exclude takes precedence over include.
     */
    public T withRouteFilterIncludePattern(String routeFilterIncludePattern) {
        this.routeFilterIncludePattern = routeFilterIncludePattern;
        return (T) this;
    }

    /**
     * Used for filtering routes routes matching the given pattern, which follows the following rules:
     *
     * - Match by route id - Match by route input endpoint uri
     *
     * The matching is using exact match, by wildcard and regular expression as documented by
     * {@link PatternHelper#matchPattern(String, String)}.
     *
     * For example to only include routes which starts with foo in their route id's, use: include=foo&#42; And to
     * exclude routes which starts from JMS endpoints, use: exclude=jms:&#42;
     *
     * Multiple patterns can be separated by comma, for example to exclude both foo and bar routes, use:
     * exclude=foo&#42;,bar&#42;
     *
     * Exclude takes precedence over include.
     */
    public T withRouteFilterExcludePattern(String routeFilterExcludePattern) {
        this.routeFilterExcludePattern = routeFilterExcludePattern;
        return (T) this;
    }

    /**
     * Sets whether bean introspection uses extended statistics. The default is false.
     */
    public T withBeanIntrospectionExtendedStatistics(boolean beanIntrospectionExtendedStatistics) {
        this.beanIntrospectionExtendedStatistics = beanIntrospectionExtendedStatistics;
        return (T) this;
    }

    /**
     * Sets the logging level used by bean introspection, logging activity of its usage. The default is TRACE.
     */
    public T withBeanIntrospectionLoggingLevel(LoggingLevel beanIntrospectionLoggingLevel) {
        this.beanIntrospectionLoggingLevel = beanIntrospectionLoggingLevel;
        return (T) this;
    }

    /**
     * Tracing pattern to match which node EIPs to trace. For example to match all To EIP nodes, use to*. The pattern
     * matches by node and route id's Multiple patterns can be separated by comma.
     */
    public T withTracingPattern(String tracingPattern) {
        this.tracingPattern = tracingPattern;
        return (T) this;
    }

    /**
     * To use a custom tracing logging format.
     *
     * The default format (arrow, routeId, label) is: %-4.4s [%-12.12s] [%-33.33s]
     */
    public T withTracingLoggingFormat(String format) {
        this.tracingLoggingFormat = format;
        return (T) this;
    }

    /**
     * Sets whether debugging is enabled or not.
     *
     * Default is false.
     */
    public T withDebugging(boolean debugging) {
        this.debugging = debugging;
        return (T) this;
    }

    /**
     * Sets the pattern used for determine which custom MDC keys to propagate during message routing when the routing
     * engine continues routing asynchronously for the given message. Setting this pattern to * will propagate all
     * custom keys. Or setting the pattern to foo*,bar* will propagate any keys starting with either foo or bar. Notice
     * that a set of standard Camel MDC keys are always propagated which starts with camel. as key name.
     *
     * The match rules are applied in this order (case insensitive):
     *
     * 1. exact match, returns true 2. wildcard match (pattern ends with a * and the name starts with the pattern),
     * returns true 3. regular expression match, returns true 4. otherwise returns false
     */
    public T withMdcLoggingKeysPattern(String mdcLoggingKeysPattern) {
        this.mdcLoggingKeysPattern = mdcLoggingKeysPattern;
        return (T) this;
    }

    /**
     * Whether the routes collector is enabled or not.
     *
     * When enabled Camel will auto-discover routes (RouteBuilder instances from the registry and also load additional
     * XML routes from the file system.
     *
     * The routes collector is default enabled.
     */
    public T withRoutesCollectorEnabled(boolean routesCollectorEnabled) {
        this.routesCollectorEnabled = routesCollectorEnabled;
        return (T) this;
    }

    /**
     * Used for inclusive filtering component scanning of RouteBuilder classes with @Component annotation. The exclusive
     * filtering takes precedence over inclusive filtering. The pattern is using Ant-path style pattern.
     *
     * Multiple patterns can be specified separated by comma. For example to include all classes starting with Foo use:
     * &#42;&#42;/Foo* To include all routes form a specific package use: com/mycompany/foo/&#42; To include all routes
     * form a specific package and its sub-packages use double wildcards: com/mycompany/foo/&#42;&#42; And to include
     * all routes from two specific packages use: com/mycompany/foo/&#42;,com/mycompany/stuff/&#42;
     */
    public T withJavaRoutesIncludePattern(String javaRoutesIncludePattern) {
        this.javaRoutesIncludePattern = javaRoutesIncludePattern;
        return (T) this;
    }

    /**
     * Used for exclusive filtering component scanning of RouteBuilder classes with @Component annotation. The exclusive
     * filtering takes precedence over inclusive filtering. The pattern is using Ant-path style pattern. Multiple
     * patterns can be specified separated by comma.
     *
     * For example to exclude all classes starting with Bar use: &#42;&#42;/Bar&#42; To exclude all routes form a
     * specific package use: com/mycompany/bar/&#42; To exclude all routes form a specific package and its sub-packages
     * use double wildcards: com/mycompany/bar/&#42;&#42; And to exclude all routes from two specific packages use:
     * com/mycompany/bar/&#42;,com/mycompany/stuff/&#42;
     */
    public T withJavaRoutesExcludePattern(String javaRoutesExcludePattern) {
        this.javaRoutesExcludePattern = javaRoutesExcludePattern;
        return (T) this;
    }

    /**
     * Used for inclusive filtering of routes from directories. The exclusive filtering takes precedence over inclusive
     * filtering. The pattern is using Ant-path style pattern.
     *
     * Multiple patterns can be specified separated by comma, as example, to include all the routes from a directory
     * whose name contains foo use: &#42;&#42;/*foo*.
     */
    public T withRoutesIncludePattern(String routesIncludePattern) {
        this.routesIncludePattern = routesIncludePattern;
        return (T) this;
    }

    /**
     * Used for exclusive filtering of routes from directories. The exclusive filtering takes precedence over inclusive
     * filtering. The pattern is using Ant-path style pattern.
     *
     * Multiple patterns can be specified separated by comma, as example, to exclude all the routes from a directory
     * whose name contains foo use: &#42;&#42;/*foo*.
     */
    public T withRoutesExcludePattern(String routesExcludePattern) {
        this.routesExcludePattern = routesExcludePattern;
        return (T) this;
    }

    /**
     * Used for enabling automatic routes reloading. If enabled then Camel will watch for file changes in the given
     * reload directory, and trigger reloading routes if files are changed.
     */
    public T withRoutesReloadEnabled(boolean routesReloadEnabled) {
        this.routesReloadEnabled = routesReloadEnabled;
        return (T) this;
    }

    /**
     * Directory to scan (incl subdirectories) for route changes. Camel cannot scan the classpath, so this must be
     * configured to a file directory. Development with Maven as build tool, you can configure the directory to be
     * src/main/resources to scan for Camel routes in XML or YAML files.
     */
    public T withRoutesReloadDirectory(String routesReloadDirectory) {
        this.routesReloadDirectory = routesReloadDirectory;
        return (T) this;
    }

    /**
     * Whether the directory to scan should include sub directories.
     *
     * Depending on the number of sub directories, then this can cause the JVM to startup slower as Camel uses the JDK
     * file-watch service to scan for file changes.
     */
    public T withRoutesReloadDirectoryRecursive(boolean routesReloadDirectoryRecursive) {
        this.routesReloadDirectoryRecursive = routesReloadDirectoryRecursive;
        return (T) this;
    }

    /**
     * Used for inclusive filtering of routes from directories.
     *
     * Typical used for specifying to accept routes in XML or YAML files. The default pattern is <tt>*.yaml,*.xml</tt>
     * Multiple patterns can be specified separated by comma.
     */
    public T withRoutesReloadPattern(String routesReloadPattern) {
        this.routesReloadPattern = routesReloadPattern;
        return (T) this;
    }

    /**
     * When reloading routes should all existing routes be stopped and removed.
     *
     * By default, Camel will stop and remove all existing routes before reloading routes. This ensures that only the
     * reloaded routes will be active. If disabled then only routes with the same route id is updated, and any existing
     * routes are continued to run.
     */
    public T withRoutesReloadRemoveAllRoutes(boolean routesReloadRemoveAllRoutes) {
        this.routesReloadRemoveAllRoutes = routesReloadRemoveAllRoutes;
        return (T) this;
    }

    /**
     * Whether to restart max duration when routes are reloaded. For example if max duration is 60 seconds, and a route
     * is reloaded after 25 seconds, then this will restart the count and wait 60 seconds again.
     */
    public T withRoutesReloadRestartDuration(boolean routesReloadRestartDuration) {
        this.routesReloadRestartDuration = routesReloadRestartDuration;
        return (T) this;
    }

    /**
     * Configure the context to be lightweight. This will trigger some optimizations and memory reduction options.
     * <p/>
     * Lightweight context have some limitations. At the moment, dynamic endpoint destinations are not supported. Also,
     * this should only be done on a JVM with a single Camel application (microservice like camel-main, camel-quarkus,
     * camel-spring-boot). As this affects the entire JVM where Camel JARs are on the classpath.
     */
    public T withLightweight(boolean lightweight) {
        this.lightweight = lightweight;
        return (T) this;
    }

    /**
     * Whether to eager load a common set of Camel classes that would otherwise first be loaded on processing the first
     * message. By eager loading these classes then the JVM has already loaded the classes during build phase, which
     * allows Camel to process the first message faster.
     */
    public T withEagerClassloading(boolean eagerClassloading) {
        this.eagerClassloading = eagerClassloading;
        return (T) this;
    }

    /**
     * Controls whether to pool (reuse) exchanges or create new fresh exchanges (default). Using pooled will reduce JVM
     * garbage collection overhead by avoiding to re-create Exchange instances per message each consumer receives.
     */
    public T withExchangeFactory(String exchangeFactory) {
        this.exchangeFactory = exchangeFactory;
        return (T) this;
    }

    /**
     * The capacity the pool (for each consumer) uses for storing exchanges. The default capacity is 100.
     */
    public T withExchangeFactoryCapacity(int exchangeFactoryCapacity) {
        this.exchangeFactoryCapacity = exchangeFactoryCapacity;
        return (T) this;
    }

    /**
     * Configures whether statistics is enabled on exchange factory.
     */
    public T withExchangeFactoryStatisticsEnabled(boolean exchangeFactoryStatisticsEnabled) {
        this.exchangeFactoryStatisticsEnabled = exchangeFactoryStatisticsEnabled;
        return (T) this;
    }

    /**
     * If enable then Camel will during startup dump all loaded routes (incl rests and route templates) represented as
     * XML DSL into the log. This is intended for trouble shooting or to assist during development.
     *
     * Sensitive information that may be configured in the route endpoints could potentially be included in the dump
     * output and is therefore not recommended to be used for production usage.
     *
     * This requires to have camel-xml-jaxb on the classpath to be able to dump the routes as XML.
     */
    public T withDumpRoutes(boolean dumpRoutes) {
        this.dumpRoutes = dumpRoutes;
        return (T) this;
    }

    /**
     * Sets global options that can be referenced in the camel context
     * <p/>
     * <b>Important:</b> This has nothing to do with property placeholders, and is just a plain set of key/value pairs
     * which are used to configure global options on CamelContext, such as a maximum debug logging length etc.
     */
    public T withGlobalOptions(Map<String, String> globalOptions) {
        if (this.globalOptions == null) {
            this.globalOptions = new HashMap<>();
        }
        this.globalOptions.putAll(globalOptions);
        return (T) this;
    }

    /**
     * Sets global options that can be referenced in the camel context
     * <p/>
     * <b>Important:</b> This has nothing to do with property placeholders, and is just a plain set of key/value pairs
     * which are used to configure global options on CamelContext, such as a maximum debug logging length etc.
     */
    public T withGlobalOption(String key, String value) {
        if (this.globalOptions == null) {
            this.globalOptions = new HashMap<>();
        }
        this.globalOptions.put(key, value);
        return (T) this;
    }

    /**
     * Sets the logging level used for logging route activity (such as starting and stopping routes). The default
     * logging level is DEBUG.
     */
    public T withRouteControllerLoggingLevel(LoggingLevel routeControllerLoggingLevel) {
        this.routeControllerLoggingLevel = routeControllerLoggingLevel;
        return (T) this;
    }

    /**
     * To enable using supervising route controller which allows Camel to startup and then the controller takes care of
     * starting the routes in a safe manner.
     *
     * This can be used when you want to startup Camel despite a route may otherwise fail fast during startup and cause
     * Camel to fail to startup as well. By delegating the route startup to the supervising route controller then its
     * manages the startup using a background thread. The controller allows to be configured with various settings to
     * attempt to restart failing routes.
     */
    public T withRouteControllerSuperviseEnabled(boolean routeControllerSuperviseEnabled) {
        this.routeControllerSuperviseEnabled = routeControllerSuperviseEnabled;
        return (T) this;
    }

    /**
     * Initial delay in milli seconds before the route controller starts, after CamelContext has been started.
     */
    public T withRouteControllerInitialDelay(long routeControllerInitialDelay) {
        this.routeControllerInitialDelay = routeControllerInitialDelay;
        return (T) this;
    }

    /**
     * Backoff delay in millis when restarting a route that failed to startup.
     */
    public T withRouteControllerBackOffDelay(long routeControllerBackOffDelay) {
        this.routeControllerBackOffDelay = routeControllerBackOffDelay;
        return (T) this;
    }

    /**
     * Backoff maximum delay in millis when restarting a route that failed to startup.
     */
    public T withRouteControllerBackOffMaxDelay(long routeControllerBackOffMaxDelay) {
        this.routeControllerBackOffMaxDelay = routeControllerBackOffMaxDelay;
        return (T) this;
    }

    /**
     * Backoff maximum elapsed time in millis, after which the backoff should be considered exhausted and no more
     * attempts should be made.
     */
    public T withRouteControllerBackOffMaxElapsedTime(long routeControllerBackOffMaxElapsedTime) {
        this.routeControllerBackOffMaxElapsedTime = routeControllerBackOffMaxElapsedTime;
        return (T) this;
    }

    /**
     * Backoff maximum number of attempts to restart a route that failed to startup. When this threshold has been
     * exceeded then the controller will give up attempting to restart the route, and the route will remain as stopped.
     */
    public T withRouteControllerBackOffMaxAttempts(long routeControllerBackOffMaxAttempts) {
        this.routeControllerBackOffMaxAttempts = routeControllerBackOffMaxAttempts;
        return (T) this;
    }

    /**
     * Backoff multiplier to use for exponential backoff. This is used to extend the delay between restart attempts.
     */
    public T withRouteControllerBackOffMultiplier(double routeControllerBackOffMultiplier) {
        this.routeControllerBackOffMultiplier = routeControllerBackOffMultiplier;
        return (T) this;
    }

    /**
     * The number of threads used by the route controller scheduled thread pool that are used for restarting routes. The
     * pool uses 1 thread by default, but you can increase this to allow the controller to concurrently attempt to
     * restart multiple routes in case more than one route has problems starting.
     */
    public T withRouteControllerThreadPoolSize(int routeControllerThreadPoolSize) {
        this.routeControllerThreadPoolSize = routeControllerThreadPoolSize;
        return (T) this;
    }

    /**
     * Pattern for filtering routes to be included as supervised.
     *
     * The pattern is matching on route id, and endpoint uri for the route. Multiple patterns can be separated by comma.
     *
     * For example to include all kafka routes, you can say <tt>kafka:*</tt>. And to include routes with specific route
     * ids <tt>myRoute,myOtherRoute</tt>. The pattern supports wildcards and uses the matcher from
     * org.apache.camel.support.PatternHelper#matchPattern.
     */
    public T withRouteControllerIncludeRoutes(String routeControllerIncludeRoutes) {
        this.routeControllerIncludeRoutes = routeControllerIncludeRoutes;
        return (T) this;
    }

    /**
     * Pattern for filtering routes to be excluded as supervised.
     *
     * The pattern is matching on route id, and endpoint uri for the route. Multiple patterns can be separated by comma.
     *
     * For example to exclude all JMS routes, you can say <tt>jms:*</tt>. And to exclude routes with specific route ids
     * <tt>mySpecialRoute,myOtherSpecialRoute</tt>. The pattern supports wildcards and uses the matcher from
     * org.apache.camel.support.PatternHelper#matchPattern.
     */
    public T withRouteControllerExcludeRoutes(String routeControllerExcludeRoutes) {
        this.routeControllerExcludeRoutes = routeControllerExcludeRoutes;
        return (T) this;
    }

    /**
     * Whether to mark the route as unhealthy (down) when all restarting attempts (backoff) have failed and the route is
     * not successfully started and the route manager is giving up.
     *
     * Setting this to true allows health checks to know about this and can report the Camel application as DOWN.
     *
     * The default is false.
     */
    public T withRouteControllerUnhealthyOnExhausted(boolean unhealthyOnExhausted) {
        this.routeControllerUnhealthyOnExhausted = unhealthyOnExhausted;
        return (T) this;
    }

    /**
     * To use startup recorder for capturing execution time during starting Camel. The recorder can be one of: false (or
     * off), logging, java-flight-recorder (or jfr).
     *
     * The default is false.
     */
    public T withStartupRecorder(String startupRecorder) {
        this.startupRecorder = startupRecorder;
        return (T) this;
    }

    /**
     * To filter our sub steps at a maximum depth.
     *
     * Use -1 for no maximum. Use 0 for no sub steps. Use 1 for max 1 sub step, and so forth.
     *
     * The default is -1.
     */
    public T withStartupRecorderMaxDepth(int startupRecorderMaxDepth) {
        this.startupRecorderMaxDepth = startupRecorderMaxDepth;
        return (T) this;
    }

    /**
     * To enable Java Flight Recorder to start a recording and automatic dump the recording to disk after startup is
     * complete.
     *
     * This requires that camel-jfr is on the classpath, and to enable this option.
     */
    public T withStartupRecorderRecording(boolean startupRecorderRecording) {
        this.startupRecorderRecording = startupRecorderRecording;
        return (T) this;
    }

    /**
     * To use a specific Java Flight Recorder profile configuration, such as default or profile.
     *
     * The default is default.
     */
    public T withStartupRecorderProfile(String startupRecorderProfile) {
        this.startupRecorderProfile = startupRecorderProfile;
        return (T) this;
    }

    /**
     * How long time to run the startup recorder.
     *
     * Use 0 (default) to keep the recorder running until the JVM is exited. Use -1 to stop the recorder right after
     * Camel has been started (to only focus on potential Camel startup performance bottlenecks) Use a positive value to
     * keep recording for N seconds.
     *
     * When the recorder is stopped then the recording is auto saved to disk (note: save to disk can be disabled by
     * setting startupRecorderDir to false)
     */
    public T withStartupRecorderDuration(long startupRecorderDuration) {
        this.startupRecorderDuration = startupRecorderDuration;
        return (T) this;
    }

    /**
     * Directory to store the recording. By default the current directory will be used.
     */
    public T withStartupRecorderDir(String startupRecorderDir) {
        this.startupRecorderDir = startupRecorderDir;
        return (T) this;
    }

}
