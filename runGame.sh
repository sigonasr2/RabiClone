cd bin
if java -Djava.library.path="../lib" -Xms2G -Xmx2G -XX:+UseZGC -XX:+PrintCommandLineFlags -jar RabiClone.jar; then
    cd ..
    return
fi
if java -Djava.library.path="../lib" -Xms2G -Xmx2G -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -XX:+PrintCommandLineFlags -jar RabiClone.jar; then
    cd ..
    return
fi
if java -Djava.library.path="../lib" -Xms2G -Xmx2G -XX:+PrintCommandLineFlags -jar RabiClone.jar; then
    cd ..
    return
fi
cd ..