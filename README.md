### How to use
#### Git submodule

1. Add to the project
```cmd
git submodule add https://github.com/flsrg/llm-polling-client
```
2. Update after clone
```cmd
git submodule init
git submodule update
```
3. Configure gradle
```cmd
dependencies {
    implementation("dev.flsrg.llmpollingclient:1.0.0")
}
```
4. Update submodule to the latest commit
```cmd
git submodule update --remote
```