#!/bin/bash

export EJokerNodeAddr=${EJokerNodeAddr:-"127.0.0.1"}
export EAmount=${EAmount:-9999}
export ELoop=${ELoop:-4}
export JVMM=${JVMM:-"4g"}
export JVMCM=${JVMCM:-"mixed"}

export JAVA_OPTS=${JAVA_OPTS:-"-Xms${JVMM} -Xmx${JVMM}"}

MAIN_CLASS="pro.jiefzz.demo.ejoker.transfer.boot.over_javaqueue.TransferAppBatchMain"

MVN_OPTS=""
QUASAR_ARGS=""

if [ "z" == "z${1}" ] ; then

    MVN_OPTS="-DuseQuasar=0"

else
  
    MVN_OPTS="-DuseQuasar=1"
    QUASAR_ARGS="-Dpro.jk.ejoker.bootstrap.useQuasar=1 \
	-Dco.paralleluniverse.fibers.detectRunawayFibers=false \
	-Dco.paralleluniverse.fibers.verifyInstrumentation=false \
	-javaagent:\${co.paralleluniverse:quasar-core:jar:jdk8}"

fi

PROM_ARGS="${QUASAR_ARGS} -classpath %classpath ${MAIN_CLASS}"
EXEC_ARGS="-server -Xmn2g -X${JVMCM} -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=64m ${JAVA_OPTS} ${PROM_ARGS}"

mvn -Dmaven.test.skip=true ${MVN_OPTS} \
  clean \
  compile \
  exec:exec -Dexec.executable="java" -Dexec.args="${EXEC_ARGS}"

