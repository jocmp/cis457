```
gradle init --type java-library
```

----------------

In the `subprojects` closure, we have added the stuff that is common to each 
project i.e. applied the Java plugin, added the maven repository and added 
the common dependency on JUnit JAR for the testCompile configuration.

Much love to [ylww](https://github.com/ylww/gradleDemo) 
