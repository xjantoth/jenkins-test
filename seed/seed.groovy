freeStyleJob("${SEED_PROJECT}-${SEED_BRANCH}-build") {
    description "Trigger the build of the ${BRANCH} pipeline."

    logRotator {
        numToKeep(5)
        artifactNumToKeep(1)
    }

    publishers {
        downstreamParameterized {
            trigger("${SEED_PROJECT}-${SEED_BRANCH}-builddeploy") {
                condition('ALWAYS')
                triggerWithNoParameters(true)
            }
        }
    }
}

pipelineJob("${SEED_PROJECT}-${SEED_BRANCH}-builddeploy") {
    description "Building and deploying the ${BRANCH} branch."
    // because stash notifier will not work
        parameters {
                stringParam("PROJECT_SPACE", "xjantoth", 'User space of the project e.g. ')
                    }

    triggers {
        scm('')
    }

    logRotator {
        numToKeep(5)
        artifactNumToKeep(1)
    }

    definition {
        cpsScm {
            scm {
                git{
                    remote {
                        github("${PROJECT_SPACE}/${PROJECT}", 'https')
                        credentials("SSH_KEY_SECURITY")
                    }
                    branches("${BRANCH}")
                    extensions { }

                }
            scriptPath('Jenkinsfile')


            }
        }
}
}
