### How to use
#### Git submodule
1. Add to the project
```declarative
git submodule add https://github.com/flsrg/LlmPollingClient libs/llm-client
```
2. Update after clone
```declarative
git submodule init
git submodule update
```
3. Configure gradle
```declarative
dependencies {
    implementation("com.llm:client:1.0.0")
}
```
4. Update submodule to the latest commit
```declarative
git submodule update --remote
```