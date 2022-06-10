export AUTO_UPDATE=true

source utils/define.sh

define PROJECT_NAME "RabiClone"
define PROJECT_DIR "src/sig"
define MAIN_CLASS "sig.${PROJECT_NAME}"
define OUT_DIR "bin"
define LIBRARY_PATH "../lib"
define CLASS_PATH "${PROJECT_DIR}/..:lib/bin"
define CUSTOM_PARAMS "-Xmx2G -Xms2G -Dsun.java2d.opengl=true"
define LANGUAGE "Java"

source utils/main.sh