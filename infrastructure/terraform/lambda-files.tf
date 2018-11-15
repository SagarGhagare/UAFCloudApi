# Files uploaded to S3 lambda deployment bucket as a dependency of lambda functions

resource "aws_s3_bucket_object" "fidouaf_api_jar" {
  bucket = "${local.deployment_bucket_name}"
  key    = "${local.fidouaf_api_jar_deployment_bucket_key}"
  source = "../../fidouaf-api.jar"
  acl    = "bucket-owner-full-control"
}