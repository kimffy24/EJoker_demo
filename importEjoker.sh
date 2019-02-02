#!/bin/bash

VERSION=0.3.3-SNAPSHOT

mvn deploy:deploy-file -Dfile=/server/data/ejoker/target/ejoker-${VERSION}.jar -DgroupId=com.jiefzz -DartifactId=ejoker -Dversion=${VERSION} -Dpackaging=jar -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
