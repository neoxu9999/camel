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
package org.apache.camel.component.metrics;

import java.util.HashMap;
import java.util.Map;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.apache.camel.test.spring.junit5.CamelSpringTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import static org.apache.camel.component.metrics.MetricsComponent.METRIC_REGISTRY_NAME;
import static org.apache.camel.component.metrics.MetricsConstants.HEADER_COUNTER_DECREMENT;
import static org.apache.camel.component.metrics.MetricsConstants.HEADER_COUNTER_INCREMENT;
import static org.apache.camel.component.metrics.MetricsConstants.HEADER_METRIC_NAME;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@CamelSpringTest
public class CounterRouteTest extends CamelSpringTestSupport {

    @EndpointInject("mock:out")
    private MockEndpoint endpoint;

    @Produce("direct:in-1")
    private ProducerTemplate producer1;

    @Produce("direct:in-2")
    private ProducerTemplate producer2;

    @Produce("direct:in-3")
    private ProducerTemplate producer3;

    @Produce("direct:in-4")
    private ProducerTemplate producer4;

    private MetricRegistry mockRegistry;

    private Counter mockCounter;

    private InOrder inOrder;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new AnnotationConfigApplicationContext();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        mockRegistry = Mockito.mock(MetricRegistry.class);
        context.getRegistry().bind(METRIC_REGISTRY_NAME, mockRegistry);
        mockCounter = mock(Counter.class);
        inOrder = Mockito.inOrder(mockRegistry, mockCounter);

        return context;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:in-1")
                        .to("metrics:counter:A?increment=5")
                        .to("mock:out");

                from("direct:in-2")
                        .to("metrics:counter:A?decrement=9")
                        .to("mock:out");

                from("direct:in-3")
                        .setHeader(HEADER_COUNTER_INCREMENT, constant(417L))
                        .to("metrics:counter:A")
                        .to("mock:out");

                from("direct:in-4")
                        .setHeader(HEADER_COUNTER_INCREMENT, simple("${body.length}"))
                        .to("metrics:counter:A")
                        .to("mock:out");
            }
        };
    }

    @AfterEach
    public void tearDown() {
        endpoint.reset();
        reset(mockRegistry, mockCounter);
    }

    @Test
    public void testOverrideMetricsName() throws Exception {
        when(mockRegistry.counter("B")).thenReturn(mockCounter);
        endpoint.expectedMessageCount(1);
        producer1.sendBodyAndHeader(new Object(), HEADER_METRIC_NAME, "B");
        endpoint.assertIsSatisfied();
        inOrder.verify(mockRegistry, times(1)).counter("B");
        inOrder.verify(mockCounter, times(1)).inc(5L);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testOverrideIncrement() throws Exception {
        when(mockRegistry.counter("A")).thenReturn(mockCounter);
        endpoint.expectedMessageCount(1);
        producer1.sendBodyAndHeader(new Object(), HEADER_COUNTER_INCREMENT, 14L);
        endpoint.assertIsSatisfied();
        inOrder.verify(mockRegistry, times(1)).counter("A");
        inOrder.verify(mockCounter, times(1)).inc(14L);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testOverrideIncrementAndDecrement() throws Exception {
        when(mockRegistry.counter("A")).thenReturn(mockCounter);
        endpoint.expectedMessageCount(1);
        Map<String, Object> headers = new HashMap<>();
        headers.put(HEADER_COUNTER_INCREMENT, 912L);
        headers.put(HEADER_COUNTER_DECREMENT, 43219L);
        producer1.sendBodyAndHeaders(new Object(), headers);
        endpoint.assertIsSatisfied();
        inOrder.verify(mockRegistry, times(1)).counter("A");
        inOrder.verify(mockCounter, times(1)).inc(912L);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testOverrideDecrement() throws Exception {
        when(mockRegistry.counter("A")).thenReturn(mockCounter);
        endpoint.expectedMessageCount(1);
        producer2.sendBodyAndHeader(new Object(), HEADER_COUNTER_DECREMENT, 7L);
        endpoint.assertIsSatisfied();
        inOrder.verify(mockRegistry, times(1)).counter("A");
        inOrder.verify(mockCounter, times(1)).dec(7L);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testOverrideUsingConstantValue() throws Exception {
        when(mockRegistry.counter("A")).thenReturn(mockCounter);
        endpoint.expectedMessageCount(1);
        producer3.sendBody(new Object());
        endpoint.assertIsSatisfied();
        inOrder.verify(mockRegistry, times(1)).counter("A");
        inOrder.verify(mockCounter, times(1)).inc(417L);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testOverrideUsingScriptEvaluation() throws Exception {
        when(mockRegistry.counter("A")).thenReturn(mockCounter);
        endpoint.expectedMessageCount(1);
        String message = "Hello from Camel Metrics!";
        producer4.sendBody(message);
        endpoint.assertIsSatisfied();
        inOrder.verify(mockRegistry, times(1)).counter("A");
        inOrder.verify(mockCounter, times(1)).inc(message.length());
        inOrder.verifyNoMoreInteractions();
    }
}
