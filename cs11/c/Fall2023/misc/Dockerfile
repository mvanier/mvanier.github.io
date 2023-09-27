FROM        ubuntu:hirsute
MAINTAINER  mvanier

# update and install dependencies
RUN         apt-get update \
                && apt-get install -y \
                    software-properties-common \
                    wget \
                && apt-get update \
                && apt-get install -y make nano vim gcc gdb

WORKDIR     /cs11_c
