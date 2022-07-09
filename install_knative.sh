#knative serving
kubectl apply -f https://github.com/knative/serving/releases/download/knative-v1.5.0/serving-crds.yaml
kubectl apply -f https://github.com/knative/serving/releases/download/knative-v1.5.0/serving-core.yaml

#kourier network layer
kubectl apply -f https://github.com/knative/net-kourier/releases/download/knative-v1.5.0/kourier.yaml
kubectl patch configmap/config-network \
  --namespace knative-serving \
  --type merge \
  --patch '{"data":{"ingress-class":"kourier.ingress.networking.knative.dev"}}'
kubectl --namespace kourier-system get service kourier

#Magic DNS
kubectl apply -f https://github.com/knative/serving/releases/download/knative-v1.5.0/serving-default-domain.yaml

#Knative eventing
kubectl apply -f https://github.com/knative/eventing/releases/download/knative-v1.5.1/eventing-crds.yaml
kubectl apply -f https://github.com/knative/eventing/releases/download/knative-v1.5.1/eventing-core.yaml


