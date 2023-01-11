sudo useradd client
sudo passwd changeme
sudo chown client:client client-side-0.0.1-SNAPSHOT.jar
sudo chmod 500 client-side-0.0.1-SNAPSHOT.jar
sudo ln -s ./client-side-0.0.1-SNAPSHOT.jar /etc/init.d/client-side
sudo service client-side start