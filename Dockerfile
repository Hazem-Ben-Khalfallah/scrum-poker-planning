# Start with a base image containing tomcat 7
FROM tomcat:7-jre8-alpine

# Add Maintainer Info
LABEL maintainer="benkhalfallahhazem@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG WAR_FILE=target/scrum_poker.war
ARG TOMCAT_WEBAPPS=/usr/local/tomcat/webapps

# Define active profile
ENV SPRING_PROFILES_ACTIVE=docker

# Empty webapps directory
RUN ["rm", "-rf", "/usr/local/tomcat/webapps"]

# Copy war file & set as root app
COPY ${WAR_FILE} ${TOMCAT_WEBAPPS}/ROOT.war

# Command to run
CMD ["catalina.sh", "run"]