FROM adoptopenjdk/openjdk11
MAINTAINER jackWu <627521884@qq.com>

RUN echo '本地使用mvn clean package'

ADD ./target/test_pull_image.jar test_pull_image.jar

ENTRYPOINT ["java","-jar","/test_pull_image.jar","&"]