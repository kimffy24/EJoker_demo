#!/bin/bash

VERSION=2.2.0-rc

ArtifactPath=""
ArtifactId="ejoker"
#mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/target/${ArtifactId}-${VERSION}.jar -Dpackaging=jar \
#    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/pom.xml -Dpackaging=pom \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true -DgeneratePom=false

ArtifactPath="ejoker/"
ArtifactId="ejoker-common"
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/target/${ArtifactId}-${VERSION}.jar -Dpackaging=jar \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/pom.xml -Dpackaging=pom \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true -DgeneratePom=false

ArtifactPath="ejoker/"
ArtifactId="ejoker-core"
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/target/${ArtifactId}-${VERSION}.jar -Dpackaging=jar \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/pom.xml -Dpackaging=pom \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true -DgeneratePom=false

## --------------- support

ArtifactPath="ejoker/"
ArtifactId="ejoker-support"
#mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/target/${ArtifactId}-${VERSION}.jar -Dpackaging=jar \
#    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/pom.xml -Dpackaging=pom \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true -DgeneratePom=false

ArtifactPath="ejoker/ejoker-support/"
ArtifactId="ejoker-support-rpc-netty"
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/target/${ArtifactId}-${VERSION}.jar -Dpackaging=jar \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/pom.xml -Dpackaging=pom \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true -DgeneratePom=false

ArtifactPath="ejoker/ejoker-support/"
ArtifactId="ejoker-support-bootstrap"
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/target/${ArtifactId}-${VERSION}.jar -Dpackaging=jar \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/pom.xml -Dpackaging=pom \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true -DgeneratePom=false

ArtifactPath="ejoker/ejoker-support/"
ArtifactId="ejoker-support-mq-rocket-pull"
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/target/${ArtifactId}-${VERSION}.jar -Dpackaging=jar \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/pom.xml -Dpackaging=pom \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true -DgeneratePom=false


## --------------- mq

ArtifactPath="ejoker/"
ArtifactId="ejoker-mq"
#mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/target/${ArtifactId}-${VERSION}.jar -Dpackaging=jar \
#    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/pom.xml -Dpackaging=pom \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true -DgeneratePom=false

ArtifactPath="ejoker/ejoker-mq/"
ArtifactId="ejoker-mq-javaqueue"
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/target/${ArtifactId}-${VERSION}.jar -Dpackaging=jar \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/pom.xml -Dpackaging=pom \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true -DgeneratePom=false

ArtifactPath="ejoker/ejoker-mq/"
ArtifactId="ejoker-mq-rocketmq"
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/target/${ArtifactId}-${VERSION}.jar -Dpackaging=jar \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true
mvn deploy:deploy-file -Dfile=/server/data/${ArtifactPath}${ArtifactId}/pom.xml -Dpackaging=pom \
    -DgroupId=pro.jk -DartifactId=${ArtifactId} -Dversion=${VERSION} -Durl=file:./minimal-maven-repository/ -DrepositoryId=minimal-maven-repository -DupdateReleaseInfo=true -DgeneratePom=false
