/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.html.ko4j;

import java.util.List;
import net.java.html.json.Models;
import org.netbeans.html.boot.spi.Fn;

final class MapObjs {
    private static Object onlyPresenter;
    private static boolean usePresenter;

    static {
        reset();
    }

    static void reset() {
        onlyPresenter = null;
        usePresenter = true;
    }

    private final List<Object> all;

    private MapObjs(Object... arr) {
        this.all = Models.asList(arr);
    }


    static Object put(Object now, Fn.Presenter key, Object js) {
        if (now instanceof MapObjs) {
            return ((MapObjs)now).put(key, js);
        } else {
            if (usePresenter) {
                if (onlyPresenter == null) {
                    onlyPresenter = key;
                    return js;
                } else if (onlyPresenter == key) {
                    return js;
                } else {
                    usePresenter = false;
                }
            }
            if (now == null) {
                return new MapObjs(key, js);
            } else {
                return new MapObjs(onlyPresenter, now, key, js);
            }
        }
    }

    static Object get(Object now, Fn.Presenter key) {
        if (now instanceof MapObjs) {
            return ((MapObjs)now).get(key);
        }
        return key == onlyPresenter ? now : null;
    }

    static Object[] remove(Object now, Fn.Presenter key) {
        if (now instanceof MapObjs) {
            return ((MapObjs)now).remove(key);
        }
        return new Object[] { now, null };
    }

    static Object[] toArray(Object now) {
        if (now instanceof MapObjs) {
            return ((MapObjs) now).all.toArray();
        }
        return new Object[] { onlyPresenter, now };
    }

    private Object put(Fn.Presenter key, Object js) {
        for (int i = 0; i < all.size(); i += 2) {
            if (all.get(i) == key) {
                all.set(i + 1, js);
                return this;
            }
        }
        all.add(key);
        all.add(js);
        return this;
    }

    private Object get(Fn.Presenter key) {
        for (int i = 0; i < all.size(); i += 2) {
            if (all.get(i) == key) {
                return all.get(i + 1);
            }
        }
        return null;
    }

    private Object[] remove(Fn.Presenter key) {
        for (int i = 0; i < all.size(); i += 2) {
            if (all.get(i) == key) {
                return new Object[] { all.get(i + 1), this };
            }
        }
        return new Object[] { null, this };
    }
}
