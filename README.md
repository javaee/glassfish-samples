# This project is migrated to Eclipse Foundation

Please use refer to [https://github.com/eclipse-ee4j/glassfish-samples](https://github.com/eclipse-ee4j/glassfish-samples)
This repository contains merge history prior to that migration.

# GlassFish Samples

This repository contains the source files for the
[GlassFish Samples](https://javaee.github.io/glassfish-samples) that are delivered with the 
Java EE SDK.

This software is provided to you under the terms described in
this [License](LICENSE). By using this software, you agree to accept
the terms, as described by this license.

If you encounter any issues, or wish to report bugs, please log into
GitHub and file an [Issue](https://github.com/javaee/glassfish-samples/issues).

# How-to Contribute to the GlassFish Samples

If you are interested in contributing to this project, read the following pages:

* [Contributing](CONTRIBUTING.md)
* [License](LICENSE)

# Building the Java EE 8 Samples

## Prerequisites

- Maven
- JDK8+
- GlassFish 5

## Build the Examples

To build the Java EE 8 GlassFish Samples:

* Clone this repository.
* From the `ws/javaee8` directory, run

```
mvn install
```

Each application module builds and the resulting WAR is written to each respective `target` subdirectory.

See the [README](ws/javaee8/README.md) file in the `/javaee8` directory for details on adding additional modules.
