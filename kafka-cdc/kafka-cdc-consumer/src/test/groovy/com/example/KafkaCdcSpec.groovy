
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.REMOTE,
        repositoryRoot = "git://git@github.com:spring-cloud-samples/spring-cloud-contract-nodejs-contracts-git.git",
        ids = "com.example:artifact-id:0.0.1")
class KafkaCdcSpec {
}
