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
// parameters {
// stringParam('PROJECT_SCM_URL', "${PROJECT_SCM_URL}", 'The Bitbucket project name including the workspace.')
// }

    // because stash notifier will not work
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
                git("${PROJECT_SCM_URL}", "${BRANCH}") { node ->
                    node / 'userRemoteConfigs' / 'hudson.plugins.git.UserRemoteConfig' / 'extensions' << '' {
                        credentialsId("SSH_KEY_SECURITY")
                        url("${PROJECT_SCM_URL}")
                    }
                }
                scriptPath("Jenkinsfile")
            }
        }
}
}
