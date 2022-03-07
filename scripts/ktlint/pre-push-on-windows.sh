#!C:/Program\ Files/Git/usr/bin/sh.exe

echo "Running lint check..."

./gradlew ktlintCheck --daemon

# result state of upper command
status=$?

# return 1 exit code if ktlint check fails
[ $status -ne 0 ] && exit 1
exit 0