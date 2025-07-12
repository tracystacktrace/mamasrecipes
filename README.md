# Mama's Recipes

[![](https://jitpack.io/v/tracystacktrace/mamasrecipes.svg)](https://jitpack.io/#tracystacktrace/mamasrecipes) [![GitHub tag](https://img.shields.io/github/tag/tracystacktrace/mamasrecipes?include_prereleases=&sort=semver&color=yellow&label=Tag)](https://github.com/tracystacktrace/mamasrecipes/releases/)
[![License](https://img.shields.io/badge/License-GNU_LGPL_Version_2.1-yellow)](#license)

A small library designed to provide a non-binding flexible recipes API for many minecraft mods and modloaders.

## Documentation

In progress... for now, the [ReIndev implementation's docs](https://github.com/tracystacktrace/mamasrecipes-reindev) are in active writing, you can check them.

## Getting the library

The only method that's currently available is `jitpack.io`.

Simply add this repository in your project (if it wasn't added):
```groovy
// For Gradle projects
repositories {
	maven { url 'https://jitpack.io' }
}
```
```xml
<!-- For Maven projects -->
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```

And then add the library as dependency:

```groovy
// For Gradle projects
dependencies {
    implementation 'com.github.tracystacktrace:mamasrecipes:VERSION'
}
```
```xml
<!-- For Maven projects -->
<dependency>
    <groupId>com.github.tracystacktrace</groupId>
    <artifactId>mamasrecipes</artifactId>
    <version>VERSION</version>
</dependency>
```

## License

This library is licensed under [GNU LGPL Version 2.1](https://github.com/tracystacktrace/mamasrecipes/blob/master/LICENSE).

