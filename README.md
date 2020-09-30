# RobotController
TCP Server for controlling sota and commu.
ビルド済みのjarファイル: https://github.com/scelab/RobotController_bin

# Java version
JavaSE-1.8

# Usage
- Build a JAR file by using eclipse.
- Move the JAR file into a directory of Sota (CommU).
- Edit System.properties (e.g. IP settings, etc)
- Run the JAR file: `java -jar RobotController.jar`, then the server will run.
- Connect to the server by a client

# Build a JAR file
You need the following libraries.
- core-2.2.jar
- gson-2.8.5.jar
- javase-2.2.jar
- jna-4.1.0.jar
- json-20180813.jar
- sotalib.jar (You can get it from https://github.com/vstoneofficial/SotaSample)
