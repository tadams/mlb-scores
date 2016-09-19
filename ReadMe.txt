 ** Notes for CI / Docker Workshop ***

1) The user id / password for the VM is DevOps/none.
   - No password required on the login screen.

2) The image is Xunbuntu which a stripped down version
   of Ubuntu.

3) Items to verify before workshop
   - A Jenkins CI server should be running at http://localhost:9090
   - You should be able to build the Java application at the
     command line using the command: gradle clean build
   - Docker should be installed, verify by running docker --version

4) Virtual Box setup notes:
   - Allocate at least 2g of RAM to the VM
