FROM area51/ubuntu-dev:18.04
MAINTAINER KeadinZhou
USER root
ADD jdk-8u251-linux-x64.tar.gz /root/
ENV JAVA_HOME=/root/jdk1.8.0_251
ENV JRE_HOME=${JAVA_HOME}/jre
ENV CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
ENV PATH=${JAVA_HOME}/bin:$PATH
CMD ["/bin/bash"]
