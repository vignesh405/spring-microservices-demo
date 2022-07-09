cd limits-microservices
kubectl apply -f deployment.yml
cd ..
cd currency-exchange-microservice
kubectl apply -f deployment.yml
cd ..
cd currency-conversion-microservice
kubectl apply -f deployment.yml
