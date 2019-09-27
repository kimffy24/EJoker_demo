#!/bin/bash

VERSION=1.2.0-snapshot

mvn deploy:deploy-file -Dfile=/server/data/ejoker/target/ejoker-${VERSION}.jar -Dpackaging=jar \
    -DgroupId=pro.jiefzz -DartifactId=ejoker -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
mvn deploy:deploy-file -Dfile=/server/data/ejoker/pom.xml -Dpackaging=pom \
    -DgroupId=pro.jiefzz -DartifactId=ejoker -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true -DgeneratePom=false