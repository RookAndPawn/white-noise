# White Noise

This is a work in progress app that served two purposes

1. An excuse to learn how to use [j2objc](http://j2objc.org/) to write an ios app
2. A white-noise machine for my infant son

# Demo

You can download this app for [IOS devices](https://itunes.apple.com/WebObjects/MZStore.woa/wa/viewSoftware?id=1187739259&mt=8).  It is stupidly simple; white noise on <-> white noise off.  

# Building

To build this app to run it for your self or just to see how the j2objc generated components and the hand-written XCode components fit together, checkout the repo.  You will need to have a few things installed before you begin:

1. Java 8+
2. Maven 3+
3. j2objc 1.2+
4. XCode (Whatever's newest)

The first thing you need to do is configure where j2objc is unzipped.  This will need to be changed in a few places

- pom.xml - You'll need to change the properties `j2objc` and `j2objcc`
- white-noise-ios/Settings.conf - You'll need to change `J2OBJC_HOME`

Next you need to generate the objective c files.  This is done with Maven: 

```bash
mvn clean package
```
Now you should be able to open the associated XCode project and run it on any ios device.
