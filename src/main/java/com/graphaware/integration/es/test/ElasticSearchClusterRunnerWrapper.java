/*
 * Copyright (c) 2013-2016 GraphAware
 *
 * This file is part of the GraphAware Framework.
 *
 * GraphAware Framework is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of the
 * GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.graphaware.integration.es.test;

import org.codelibs.elasticsearch.runner.ElasticsearchClusterRunner;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * {@link ElasticSearchServerWrapper} for testing that uses {@link ElasticsearchClusterRunner} to run an embedded ES server.
 */
public class ElasticSearchClusterRunnerWrapper implements ElasticSearchServerWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchClusterRunnerWrapper.class);
    private ElasticsearchClusterRunner runner;

    @Override
    public void startEmbeddedServer() {
        final ClassLoader currentClassLoader = this.getClass().getClassLoader();
        final ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().setContextClassLoader(currentClassLoader);
                    runner = new ElasticsearchClusterRunner();
                    // create ES nodes
                    runner.onBuild(new ElasticsearchClusterRunner.Builder() {
                        @Override
                        public void build(int i, Settings.Builder builder) {
                            builder.put("http.cors.enabled", true);
                        }
                    }).build(ElasticsearchClusterRunner.newConfigs()
                            //.clusterName("es-cl-run-" + System.currentTimeMillis())
                            .numOfNode(1));

                    runner.ensureYellow();
                    LOG.info("Embedded ElasticSearch started ...");
                } catch (Exception e) {
                    LOG.error("Error while starting ElasticSearch embedded server!", e);
                }
            }
        });

        try {
            LOG.info("Waiting for ElasticSearch embedded server...");
            executor.shutdown();
            executor.awaitTermination(20, TimeUnit.SECONDS);
            LOG.info("Finished waiting.");
        } catch (InterruptedException ex) {
            LOG.error("Error while waiting!", ex);
        }
    }

    @Override
    public void createIndex(String index, Map<String, Object> properties) {
        final Settings.Builder builder = Settings.builder();

        for (Map.Entry entry : properties.entrySet()) {
            builder.put(entry.getKey(), entry.getValue());
        }

        CreateIndexResponse createIndexResponse = runner.createIndex(index, builder.build());

        if (!createIndexResponse.isAcknowledged()) {
            throw new IllegalStateException("Index create response now acknowledged!");
        }
    }

    @Override
    public void stopEmbeddedServer() {
        runner.close();
        runner.clean();
    }
}
