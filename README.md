# JavaProjectTemplate
Provides a Java pipeline for building applications and deploying a JAR with ease. Providing a base graphical engine (fast pixel blitting) with support for input should be enough to get any project going.

The system will also use the SigScript updating system to keep files up-to-date.

## Usage:
**./sig** - Shows a menu with all scripts. Example output:
```
    gitpod /workspace/JavaProjectTemplate (main) $ ./sig

    Usage: ./sig <command> {args}

    ====    Current Configuration   =====================
            PROJECT_NAME    JavaProjectTemplate
            PROJECT_DIR                 src/sig
            MAIN_CLASS     sig.JavaProjectTemplate
            OUT_DIR                         bin
    =====================================================

    Command List:

            build          Builds and runs the project.                                     
            clean          Cleans up and removes unused files.                              
            commit         Adds a commit message and pushes project to github repository.   
            jar            Builds a runnable jar file using ${MAIN_CLASS} as an entry point and then runs the newly generated jar.
```
Configuration is modified at the top of the script file while the command list includes all included modules inside of `scripts`.
