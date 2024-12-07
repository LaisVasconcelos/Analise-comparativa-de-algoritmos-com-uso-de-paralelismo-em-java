del .class\*.class
javac -d .class -cp ".;jocl-2.0.4.jar" Main.java SerialCPU.java ParallelCPU.java ParallelGPU.java
java -cp ".class;jocl-2.0.4.jar" Main