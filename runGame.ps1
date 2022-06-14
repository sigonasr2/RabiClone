cd bin
java "-Djava.library.path=../lib" -Xms2G -Xmx2G -XX:+UseZGC -XX:+PrintCommandLineFlags -jar RabiClone.jar
if ($? -eq $True) {
    cd ..
    return
}
java "-Djava.library.path=../lib" -Xms2G -Xmx2G -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -XX:+PrintCommandLineFlags -jar RabiClone.jar
if ($? -eq $True) {
    cd ..
    return 
}
java "-Djava.library.path=../lib" -Xms2G -Xmx2G -XX:+PrintCommandLineFlags -jar RabiClone.jar
cd ..