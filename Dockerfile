FROM openjdk:11
EXPOSE 9090
ADD target/sso-oauth-two-authentication-api.jar sso-oauth-two-authentication-api.jar
ENTRYPOINT ["java","-jar","/var/lib/jenkins/workspace/target/sso-oauth-two-authentication-api.jar"]
