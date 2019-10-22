#!/bin/bash

export EJokerNodeAddr=${EJokerNodeAddr:-"127.0.0.1"}
export EAmount=${EAmount:-5000}
export ELoop=${ELoop:-3}
export JVMM=${JVMM:-"4g"}
export JVMCM=${JVMCM:-"mixed"}

export JAVA_OPTS=${JAVA_OPTS:-"-Xms${JVMM} -Xmx${JVMM}"}

if [ "z" == "z${1}" ] ; then
  
  mvn -Dmaven.test.skip=true \
  clean \
  compile \
  exec:exec -Dexec.executable="java" -Dexec.args="-server -Xmn2g -X${JVMCM} \
    -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=64m \
    ${JAVA_OPTS} \
    -classpath %classpath \
    pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.TransferAppBatch"

else

  mvn -Dmaven.test.skip=true \
  clean \
  compile \
  org.apache.maven.plugins:maven-dependency-plugin:properties \
  exec:exec -Dexec.executable="java" -Dexec.args="-server -Xmn2g -X${JVMCM} \
    -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=64m \
    ${JAVA_OPTS} \
    -Dco.paralleluniverse.fibers.detectRunawayFibers=true \
    -Dco.paralleluniverse.fibers.verifyInstrumentation=true \
    -javaagent:\${co.paralleluniverse:quasar-core:jar} \
    -classpath %classpath \
    pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.quasar.TransferAppBatchQuasar"

fi
