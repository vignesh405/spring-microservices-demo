#Knative serving
kubectl delete -f https://storage.googleapis.com/knative-nightly/serving/latest/serving-core.yaml
kubectl delete -f https://storage.googleapis.com/knative-nightly/serving/latest/serving-crds.yaml

#Kourier
kubectl delete -f https://github.com/knative/net-kourier/releases/download/knative-v1.5.0/kourier.yaml

#Knative eventing
kubectl delete -f https://storage.googleapis.com/knative-nightly/eventing/latest/eventing-core.yaml
kubectl delete -f https://storage.googleapis.com/knative-nightly/eventing/latest/eventing-crds.yaml

