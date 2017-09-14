# GlassFish Samples

This repository contains the source files for the
[GlassFish Samples](https://javaee.github.io/glassfish-samples) that are delivered with the 
Java EE SDK.

This software is provided to you under the terms described in
this [License](LICENSE.txt). By using this software, you agree to accept
the terms, as described by this license.

If you encounter any issues, or wish to report bugs, please log into
GitHub and file an [Issue](https://github.com/javaee/glassfish-samples/issues).

# How-to contribute to GlassFish

If you are interested in contributing to this project, read the following pages:

* [How to Contribute to GlassFish and Java EE](https://javaee.github.io/glassfish/how-to-contribute)
* [Pull Request Acceptance Workflow](https://javaee.github.io/glassfish/pr_workflow)
* [License](https://javaee.github.io/glassfish/LICENSE) governing the GlassFish project

# Building the Java EE 8 Samples

## Pre requisites

- Maven
- JDK8+
- GlassFish 5

## Build the examples

To build the Java EE 8 GlassFish Samples:

* Clone this repository.
* From the ws/javaee8 directory, run

```
mvn install
```

See the [README](ws/javaee8/README.md) file in the `/javaee8` directory for details on adding additional samples.