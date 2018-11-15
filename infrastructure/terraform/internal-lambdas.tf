# Authentication Lambda
resource "aws_lambda_function" "internal-auth-lambda" {
  s3_bucket = "${local.deployment_bucket_name}"
  s3_key = "${aws_s3_bucket_object.fidouaf_api_jar.key}"
  function_name = "${local.environment}-fidouaf-internal-auth-response"
  role = "${aws_iam_role.fidouaf_lambda_role.arn}"
  handler = "org.ebayopensource.fidouaf.res.FidoUafLambdaInternalHandler::handleRequest"
  runtime = "java8"
  memory_size = "512"
  publish = true
  timeout = 300

  environment {
    variables = "${local.lambda_function_env_vars}"
  }
}
