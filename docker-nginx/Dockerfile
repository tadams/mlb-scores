FROM nginx:alpine 

ARG GIT_DESCRIBE=SNAPSHOT
LABEL git-describe=$GIT_DESCRIBE

RUN rm -v /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx/
