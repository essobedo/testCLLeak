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
package org.example;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomClassLoader extends URLClassLoader {

    public CustomClassLoader() {
        super(urls(), null);
    }

    private static URL[] urls() {
        Function<URI, URL> toURL = uri -> {
            try {
                return uri.toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        };
        List<URL> l = Arrays.stream(Objects.requireNonNull(new File("./lib").listFiles()))
                .map(File::getAbsoluteFile)
                .map(File::toURI)
                .map(toURL).collect(Collectors.toList());
        l.add(toURL.apply(new File("./target/classes").getAbsoluteFile().toURI()));
        return l.toArray(URL[]::new);
    }

    @Override
    public void close() throws IOException {
        try {
            // Remain in the same CL to prevent filling again the Class#classValueMap
            loadClass("org.example.Main").getMethod("clean").invoke(null);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            super.close();
        }
    }
}
