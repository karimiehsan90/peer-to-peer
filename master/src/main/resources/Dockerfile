FROM ubuntu:16.04

RUN apt update && apt install -y openjdk-8-jdk && apt autoremove

COPY net /target

RUN chmod +x /target/bin/*

ENTRYPOINT ["/target/bin/start-master.sh"]
