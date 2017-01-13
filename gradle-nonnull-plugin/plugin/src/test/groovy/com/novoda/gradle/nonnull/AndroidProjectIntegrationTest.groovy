package com.novoda.gradle.nonnull

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.internal.DefaultGradleRunner
import org.junit.ClassRule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

import static com.google.common.truth.Truth.assertThat

public class AndroidProjectIntegrationTest {

    @ClassRule
    public static final ProjectRule PROJECT = new ProjectRule()

    @Test
    public void shouldGeneratePackageAnnotation() {
        PROJECT.generatedSrcDirs.each {
            def file = new File(it, 'package-info.java')
            assertThat(file.isFile()).isTrue()
        }
    }

    @Test
    public void shouldHaveAnnotationDefined() {
        PROJECT.generatedSrcDirs.each {
            def file = new File(it, 'package-info.java')
            assertThat(file.text).contains('@ParametersAreNonnullByDefault')
        }
    }

    static class ProjectRule implements TestRule {
        File projectDir
        BuildResult buildResult
        Set<File> generatedSrcDirs

        @Override
        Statement apply(Statement base, Description description) {
            projectDir = new File('../sample')

            buildResult = DefaultGradleRunner.create()
                    .withProjectDir(projectDir)
                    .withDebug(true)
                    .forwardStdOutput(new OutputStreamWriter(System.out))
                    .withArguments('clean', 'assembleCustomDebug', 'core:assemble')
                    .build()

            File generatedAppRoot = new File(projectDir, 'app/build/generated/source/nonNull/custom/debug')
            File generatedCoreRoot = new File(projectDir, 'core/build/generated/source/nonNull/main')

            generatedSrcDirs = [
                    new File(generatedAppRoot, 'com/novoda/gradle/nonnull'),
                    new File(generatedAppRoot, 'com/novoda/gradle/nonnull/custom'),
                    new File(generatedAppRoot, 'com/novoda/gradle/common'),
                    new File(generatedCoreRoot, 'com/novoda/gradle/nonnull')
            ]

            return base;
        }
    }

}