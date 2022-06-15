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

Mizue (みずえ) 水恵
Turn off inline hints: (F1, search "Preferences: Open Settings (JSON)) "editor.inlayHints.enabled": false
Profont: 7x14 per tile.

Belly Flop -> Slide
Sprint leads into Belly Slide
Underwater / Water Dashing (Propelling motion)
        Air Dash (Water Dash-powerup)
Side Roll -> Slide
Wall Jumping

Movement Systems
Collectibles
Combat Systems
Storyboarding / Event Systems

https://docs.oracle.com/en/java/javase/14/jpackage/image-and-runtime-modifications.html#GUID-5B97E9DB-577A-427F-B275-97E8B27224C9


`all` to suppress all warnings
`boxing` to suppress warnings relative to boxing/unboxing operations
`cast` to suppress warnings relative to cast operations
`dep`-ann to suppress warnings relative to deprecated annotation
`deprecation` to suppress warnings relative to deprecation
`fallthrough` to suppress warnings relative to missing breaks in switch statements
`finally` to suppress warnings relative to finally block that don’t return
`hiding` to suppress warnings relative to locals that hide variable
`incomplete`-switch to suppress warnings relative to missing entries in a switch statement (enum case)
`nls` to suppress warnings relative to non-nls string literals
`null` to suppress warnings relative to null analysis
`restriction` to suppress warnings relative to usage of discouraged or forbidden references
`serial` to suppress warnings relative to missing serialVersionUID field for a serializable class
`static`-access to suppress warnings relative to incorrect static access
`synthetic`-access to suppress warnings relative to unoptimized access from inner classes
`unchecked` to suppress warnings relative to unchecked operations
`unqualified`-field-access to suppress warnings relative to field access unqualified
`unused` to suppress warnings relative to unused code

`javadoc` to suppress warnings relative to javadoc warnings
`rawtypes` to suppress warnings relative to usage of raw types
`static`-method to suppress warnings relative to methods that could be declared as static
`super` to suppress warnings relative to overriding a method without super invocations

`resource` to suppress warnings relative to usage of resources of type Closeable
`sync-override` to suppress warnings because of missing synchronize when overriding a synchronized method

http://www.magicandlove.com/blog/2012/05/01/opencl-particles-system-example-in-processing/