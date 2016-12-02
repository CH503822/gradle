/*
 * Copyright 2012 the original author or authors.
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

package org.gradle.performance

import org.gradle.performance.categories.JavaPerformanceTest
import org.junit.experimental.categories.Category
import spock.lang.Unroll

@Category(JavaPerformanceTest)
class JavaUpToDateFullBuildPerformanceTest extends AbstractCrossVersionPerformanceTest {
    @Unroll("Up-to-date full build - #testProject")
    def "up-to-date full build Java build"() {
        given:
        runner.testId = "up-to-date full build Java build $testProject"
        runner.previousTestIds = ["up-to-date build $testProject"]
        runner.testProject = testProject
        runner.tasksToRun = ['build']
        runner.gradleOpts = ["-Xms${maxMemory}", "-Xmx${maxMemory}"]

        when:
        def result = runner.run()

        then:
        result.assertCurrentVersionHasNotRegressed()

        where:
        testProject       | maxMemory
        "small"           | '128m'
        "multi"           | '128m'
        "lotDependencies" | '256m'
    }

    @Unroll("Up-to-date full build (daemon) - #testProject")
    def "up-to-date full build Java build with daemon"() {
        given:
        runner.testId = "up-to-date full build Java build $testProject (daemon)"
        runner.testProject = testProject
        runner.tasksToRun = ['build']
        runner.gradleOpts = ["-Xms${maxMemory}", "-Xmx${maxMemory}"]
        runner.useDaemon = true
        when:
        def result = runner.run()

        then:
        result.assertCurrentVersionHasNotRegressed()
        where:
        testProject       |  maxMemory
        "bigOldJava"      | '768m'
        "lotDependencies" | '256m'
    }
}
