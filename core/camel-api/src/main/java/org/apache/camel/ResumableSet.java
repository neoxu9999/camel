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

package org.apache.camel;

import java.util.Arrays;
import java.util.function.Predicate;

public interface ResumableSet<T> {

    /**
     * Iterates over the set of input checking if they should be resumed or not.
     *
     * @param  input
     * @param  resumableCheck
     * @return
     */
    default T[] resumeEach(T[] input, Predicate<T> resumableCheck) {

        T[] tmp = Arrays.copyOf(input, input.length);
        int count = 0;

        for (T entry : input) {
            if (resumableCheck.test(entry)) {
                tmp[count] = entry;
                count++;
            }
        }

        if (count != input.length) {
            return Arrays.copyOf(tmp, count);
        }

        return input;
    }

    /**
     * Iterates over the set of input checking if they should be resumed or not
     *
     * @param resumableCheck a checker method that returns true if a single entry of the input should be resumed or
     *                       false otherwise
     */
    void resumeEach(Predicate<T> resumableCheck);

    /**
     * Gets the input that should be resumed
     *
     * @return an array with the input that should be resumed
     */
    T[] resumed();

    /**
     * Whether there are resumable entries to process
     *
     * @return true if there are resumable entries or false otherwise
     */
    boolean hasResumables();
}
