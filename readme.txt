How to open Alkitab in Netbeans*
- extract the source code into your desired location
- Open Netbeans 
- Go to Menu - File - Open Project
- Navigate to the folder where you extracted it
eg: ../alkitab-suite/alkitab-suite
- You are done

Now you can recompile, build, crete your own distribution, etc

note: alkitab-suite is the main project (aka the container project),
all other projects are sub-project (module) for this alkitab-suite.
eg:
alkitab-suite (Main Container Project)
|- alkitab-core
|- alkitab-branding
|- alkitab-localization
|- alkitab-user-guide
|- JSword
|- etc

The main source code is under alkitab-core

*Netbeans used is Netbeans 6.9.1
