/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.kyligence.zenml.toolkit.config;

import io.kyligence.zenml.toolkit.exception.ErrorCode;
import io.kyligence.zenml.toolkit.exception.ToolkitException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class ToolkitConfig {
    private static final ToolkitConfig INSTANCE = new ToolkitConfig();
    private static final String PROPERTIES_FILE = "toolkit.properties";
    private static final String OVERRIDE_PROPERTIES_FILE = "toolkit.properties.override";

    public static ToolkitConfig getInstance() {
        return INSTANCE;
    }


    private Properties properties = new Properties();

    private ToolkitConfig() {
        loadConfig();
    }

    private void loadConfig() {
        var propFile = getPropertiesFile();
        if (!propFile.exists()) {
            log.error("fail to locate {}", PROPERTIES_FILE);
            throw new ToolkitException(ErrorCode.LOAD_CONFIG_ERROR, PROPERTIES_FILE);
        }
        var conf = new Properties();
        FileInputStream is = null;
        FileInputStream ois = null;
        try {
            is = new FileInputStream(propFile);
            conf.load(is);

            var overridePropFile = new File(propFile.getParentFile(), propFile.getName() + ".override");
            if (overridePropFile.exists()) {
                ois = new FileInputStream(overridePropFile);
                var overrideProp = new Properties();
                overrideProp.load(ois);
                conf.putAll(overrideProp);
            }
        } catch (IOException e) {
            throw new ToolkitException(e.getMessage());
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
            if (ois != null) {
                IOUtils.closeQuietly(ois);
            }
        }

        this.properties = conf;
    }

    private File getPropertiesFile() {
        String path = System.getProperty("PROPERTIES_PATH");
        if (StringUtils.isBlank(path)) {
            path = getPropertiesDirPath();
        }
        File overrideFile = new File(path, OVERRIDE_PROPERTIES_FILE);
        if (overrideFile.exists()) {
            return overrideFile;
        } else {
            return new File(path, PROPERTIES_FILE);
        }
    }

    public String getToolkitHome() {
        String home = System.getProperty("ZEN_HOME");
        if (StringUtils.isBlank(home)) {
            home = System.getenv("ZEN_HOME");
            if (StringUtils.isBlank(home)) {
                throw new ToolkitException(ErrorCode.ENV_NOT_FOUND);
            }
        }
        return home;
    }

    public String getOptional(String propertyKey, String defaultValue) {
        String property = System.getProperty(propertyKey);
        if (!StringUtils.isBlank(property)) {
            return property.trim();
        }
        property = properties.getProperty(propertyKey);
        if (StringUtils.isBlank(property)) {
            return defaultValue.trim();
        } else {
            return property.trim();
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getPropertiesDirPath() {
        return getToolkitHome() + File.separator + "conf";
    }

    public String getLocalTmpFolder() {
        return getOptional("zen.ml.toolkit.env.tmp-folder", getToolkitHome() + File.separator + "tmp");
    }

    public Double getFileSizeLimit() {
        return Double.parseDouble(getOptional("zen.ml.toolkit.resource.file-size-limit-kb", "200"));
    }

    public String getSecretKey() {
        return getOptional("zen.ml.toolkit.security.key", "6173646661736466e4bda0e8bf983161");
    }
}
