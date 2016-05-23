/*
 * Copyright (c) 2016 AsyncHttpClient Project. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at
 *     http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.ning.http.client.async;

import static org.testng.Assert.fail;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.net.ssl.SSLException;

import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ConnectHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

public abstract class ClosingNonKeepAliveConnectionsMustNotThrowExceptionsTest extends AbstractBasicTest {

    @Override
    public AbstractHandler configureHandler() throws Exception {
        return new ConnectHandler();
    }

    @Test
    public void httpProxyToHttpTargetTest() throws IOException, InterruptedException, ExecutionException {
        try (AsyncHttpClient client = getAsyncHttpClient(new AsyncHttpClientConfig.Builder().build())) {
            Request request = new RequestBuilder("GET").setUrl(getTargetUrl2()).build();
            Future<Response> responseFuture = client.executeRequest(request);
            try {
                responseFuture.get();
                fail("It should thrown an exception... test is supposed to fail :P");
            } catch(ExecutionException e) {
                e.printStackTrace();
                Assert.assertEquals(SSLException.class, e.getCause().getClass());
            }
        }
    }
}
