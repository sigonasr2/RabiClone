cd bin
if java -Djava.library.path="../lib" -Xms2G -Xmx2G -XX:+UseZGC -XX:+PrintCommandLineFlags -jar RabiClone.jar; then
    cd ..
    exit
fi
if java -Djava.library.path="../lib" -Xms2G -Xmx2G -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -XX:+PrintCommandLineFlags -jar RabiClone.jar; then
    cd ..
    exit
fi
if java -Djava.library.path="../lib" -Xms2G -Xmx2G -XX:+PrintCommandLineFlags -jar RabiClone.jar; then
    cd ..
    exit
fi
cd ..