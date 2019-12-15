/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.binarycompatibility.rules;

import japicmp.model.JApiCompatibility;
import japicmp.model.JApiClass;
import japicmp.model.JApiField;
import japicmp.model.JApiHasAnnotations;
import japicmp.model.JApiMethod;
import me.champeau.gradle.japicmp.report.Violation;

import java.util.Map;

public class NewIncubatingAPIRule extends AbstractGradleViolationRule {

    public NewIncubatingAPIRule(Map<String, String> acceptedViolations) {
        super(acceptedViolations);
    }

    @Override
    public Violation maybeViolation(final JApiCompatibility member) {
        if (member instanceof JApiMethod || member instanceof JApiField || member instanceof JApiClass) {
            if (!isIncubatingOrDeprecated((JApiHasAnnotations) member)) {
                return null;
            }
            if (member instanceof JApiMethod && isOverride((JApiMethod) member)) {
                return null;
            }
            return Violation.info(member, "New public API in " + getCurrentVersion() + " (@Incubating)");
        }
        return null;
    }

}
