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
package org.apache.felix.converter.impl.yaml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.osgi.service.converter.Converter;
import org.osgi.service.converter.Encoding;

public class YamlEncodingImpl implements Encoding {
    private final Converter converter;
    private final Map<String, Object> configuration;
    private final Object object;
    private final int indentation = 2;

    public YamlEncodingImpl(Converter c, Map<String, Object> cfg, Object obj) {
        converter = c;
        configuration = cfg;
        object = obj;
    }

    @Override
    public Encoding pretty() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void to(OutputStream os, Charset charset) {
        try {
            os.write(encode(object).getBytes(charset));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Appendable to(Appendable out) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return encode(object);
    }

    private String encode(Object obj) {
        return encode(obj, 0);
    }

    @SuppressWarnings("rawtypes")
    private String encode(Object obj, int level) {
        if (obj == null)
            return "";

        if (obj instanceof Map) {
            return encodeMap((Map) obj, level);
        } else if (obj instanceof Number)  {
            return obj.toString();
        } else if (obj instanceof Boolean) {
            return obj.toString();
        }

        return "'" + converter.convert(obj).to(String.class) + "'";
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String encodeMap(Map m, int level) {
        StringBuilder sb = new StringBuilder();
        for (Entry entry : (Set<Entry>) m.entrySet()) {
            sb.append(getIdentPrefix(level));
            sb.append(entry.getKey().toString());
            sb.append(": ");
            sb.append(needsNewlineBefore(entry.getValue()));
            sb.append(encode(entry.getValue(), level + 1));
            sb.append("\n");
        }

        return sb.toString();
    }

    private Object needsNewlineBefore(Object value) {
        if (value instanceof Map)
            return "\n";

        return "";
    }

    private String getIdentPrefix(int level) {
        int numSpaces = indentation * level;
        StringBuilder sb = new StringBuilder(numSpaces);
        for (int i=0; i < numSpaces; i++)
            sb.append(' ');
        return sb.toString();
    }
}