function search() {
    FILES2=$(ls -A $1)
    for g in $FILES2
    do
        if [ -d $1$g ];
        then
            echo "$1$g is a directory"
            search $1$g/
        else 
            echo "$1$g is a file"
            if [ $g != "md5" ]; then
                SUM=$(md5sum < $1$g)
                echo "$g:$SUM" >> $1md5
            else
                echo "  md5 file, ignoring..."
            fi
        fi
    done
}

function check() {
    echo "Check $1"
    FILES2=$(ls -A $1)
    if [ -f "$1/md5" ];
    then
        echo "   md5: https://raw.githubusercontent.com/sigonasr2/SigScript/main/$1md5"
        curl -H 'Cache-Control: no-cache, no-store' -s "https://raw.githubusercontent.com/sigonasr2/SigScript/main/$1md5/?$(date +%s)" --output /tmp/out
        cmp -s $1/md5 /tmp/out
        if [ "$?" -ne 0 ] 
        then
            echo " Differences detected!"
            cat /tmp/out 
            while IFS= read -r line
            do
                IFS=':' read -ra split <<< $line
                g="${split[0]}"
                echo "LINE -- $g" 
                if [ "$g" != "md5" ]; then
                    if [ -f $1$g ];
                    then
                        if [ "$g" != ".coauthors" ]; then
                            echo "++Redownload $1$g..."
                            if [ -f "$1$g" ]; then
                                curl -H 'Cache-Control: no-cache, no-store' "https://raw.githubusercontent.com/sigonasr2/SigScript/main/$1$g/?$(date +%s)" --output $1$g
                            else
                                echo "===Could not find directory, assuming regular scripts directory exists."
                                curl -H 'Cache-Control: no-cache, no-store' "https://raw.githubusercontent.com/sigonasr2/SigScript/main/$1$g/?$(date +%s)" --output $LANGUAGE/scripts/$g
                            fi
                        fi
                    else 
                        echo "++==Downloading $1$g..."
                        curl -H 'Cache-Control: no-cache, no-store' "https://raw.githubusercontent.com/sigonasr2/SigScript/main/$1$g/?$(date +%s)" --output $1$g
                    fi
                fi
            done < /tmp/out
        fi
    fi
    for g in $FILES2
    do
        if [ -d $1$g ];
        then
            echo "$1$g is a directory"
            check $1$g/
        fi
    done
}
