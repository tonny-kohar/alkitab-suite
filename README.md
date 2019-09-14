# Alkitab Bible Study

[Alkitab Bible Study](https://www.kiyut.com/products/alkitab/index.php) is an open source and free desktop Bible study software. It supports single/parallel view, commentaries, lexicons, dictionaries, glossaries, daily devotions, etc. It also comes with powerful search capability. It features rich and user friendly Bible software study tools.

This Bible software is build using [JSword](http://www.crosswire.org/jsword/) and is an alternative front end for [Bible Desktop](http://www.crosswire.org/bibledesktop/).

Alkitab Bible Study is released into [Public Domain](https://github.com/tonny-kohar/alkitab-suite/blob/master/alkitab-suite/legal/LICENSE.alkitab.txt).
Others third party Licenses please check [the following](https://github.com/tonny-kohar/alkitab-suite/tree/master/alkitab-suite/legal)

![Screenshot](https://www.kiyut.com/products/alkitab/alkitab.png)

## Features:

* View Single Book or Bible
* View Parallel Books or Bibles
* Cross Reference and Search
* Commentaries
* Strong's Concordance and Morphology
* Lexicons/Dictionaries/Glossaries
* Daily Devotions
* Plugins Support
* [Learn more ...](https://www.kiyut.com/products/alkitab/features.php)

# Development

The whole architecture is divided into 3 areas:
* Netbeans Platform RCP
* JSword CrossWire
* Alkitab Bible Study

## How To Build

* Clone this repo
* Open Netbeans 
* Go to Menu - File - Open Project
* Navigate to the folder where you clone it eg: ../alkitab-suite/alkitab-suite
* You are done

Now you can recompile, build, create your own distribution, etc

note: alkitab-suite is the main project (aka the container project),
all other projects are sub-project (module) for this alkitab-suite.
```
alkitab-suite (Main Container Project)
|- alkitab-core
|- alkitab-branding
|- alkitab-localization
|- alkitab-user-guide
|- JSword
|- etc
```

The main source code is under alkitab-core
