FROM keadinzhou/ubuntu-dev
MAINTAINER KeadinZhou
USER root
RUN mkdir /root/judgehost/ \
    && mkdir /root/judgehost/core/ \
    && mkdir /root/judgehost/testcase/ \
    && mkdir /root/judgehost/test/
COPY ./zuccoj-judgehost.jar /root/judgehost/
COPY ./judgehost_run.sh /root/judgehost/
RUN chmod u+x /root/judgehost/judgehost_run.sh
COPY ./Compiler /root/judgehost/core/
COPY ./Core /root/judgehost/core/
CMD ["/bin/bash", "/root/judgehost/judgehost_run.sh"]
