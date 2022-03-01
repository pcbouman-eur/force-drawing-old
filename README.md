# Force Based drawing of Graphs

This is code I wrote as a final project for the course I followed at Utrecht University in 2007/2008.

It would use an implementation of a force-based drawing algorithm,  which is live animated in a kind of crude Swing GUI application. When generating the graph, you can choose whether to use 3D node coordinates or 2D nodes coordinates. In case of 3D, if you stop the animation you can drag with the right mouse button to rotate the graph.

The visualization unfortunately is not very calm, as no double buffering is used in the application. I don't think I would write this code this way anymore in 2022.

A short demonstration of a 3D force-based layout of a binary tree, starting out with random node coordinates, is presented below:

![Animated gif that shows the force based layout of a binary tree in 3D](/doc/force-drawing-v2.gif)

Typically, the force based local optimum of a binary tree in 2D is rather messy.

## Using it yourself

You can either download a Runnable JAR file under releases. If you have a Java VM, you can try running it directly, or alternatives using:
```
java -jar force-drawing-old-1.0.jar
```

Alternatively, you can use the following command on Linux/Mac OS to run it

```
./mvnw exec:java
```

or on Windows

```
mvnw exec:java
```

If you want to compile the project, you can do the following on Linux/Mac OS

```
./mvnw package
```

or on Windows

```
mvnw package
```

and find the compiled jar file in the `target/` directory.
