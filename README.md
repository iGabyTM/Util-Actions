# (Work in progress)
# Installation
> Replace `{version}` with the latest release [![](https://jitpack.io/v/iGabyTM/Util-Actions.svg)](https://jitpack.io/#iGabyTM/Util-Actions) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/44373db818ad47728a67a636f901a2ca)](https://www.codacy.com/manual/iGabyTM/Util-Actions?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=iGabyTM/Util-Actions&amp;utm_campaign=Badge_Grade)
### Gradle
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.iGabyTM:Util-Actions:{version}'
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.iGabyTM</groupId>
    <artifactId>Util-Actions</artifactId>
    <version>{version}</version>
</dependency>
```

# Usage
> Create a new instance of `ActionManager`
```java
// The constructor requires an instance of your main class ('plugin')
final ActionManager actionManager = new ActionManager(plugin);

actionManager.execute(player, "[chat] This message will be sent by the player :)");
```

# Actions
| ID | Alias | Description | Usage |
| --- | --- | --- | --- |
| `broadcast` | - | Send a message to all online players | `[broadcast] message` |
| `chat` | - | Send player a message in chat | `[message] message` |
