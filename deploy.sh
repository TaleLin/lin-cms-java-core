# deploy script
# deploy to sonatype and maven central
# remember to modify the version of all

export GPG_TTY=$(tty)

mvn clean deploy -projects core,lin-cms-spring-boot-autoconfigure,lin-cms-spring-boot-starter -Dmaven.test.skip=true
