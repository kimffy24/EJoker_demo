#!/bin/bash

VERSION=1.2.0-snapshot

mvn deploy:deploy-file -Dfile=/server/data/ejoker/target/ejoker-${VERSION}.jar -DgroupId=pro.jiefzz -DartifactId=ejoker -Dversion=${VERSION} -Dpackaging=jar -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
