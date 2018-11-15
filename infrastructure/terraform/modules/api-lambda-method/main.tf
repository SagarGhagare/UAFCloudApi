# Creates a lambda function with permissions
# Hooks up lambda with API gateway resource

# Lambda function (For Non-VPC Lambda)
resource "aws_lambda_function" "module_function" {
  count            = "${var.attach_vpc_config == "true" ? 0 : 1}"
  s3_bucket        = "${var.function_bucket_name}"
  s3_key           = "${var.function_bucket_key}"
  function_name    = "${var.function_name}"
  role             = "${var.function_role_arn}"
  handler          = "${var.function_handler}"
  runtime          = "${var.function_runtime}"
  memory_size      = "${var.function_memory}"
  publish          = true
  timeout          = 20

  environment {
    variables = "${var.function_environment_variables}"
  }

  tracing_config {
    mode = "${var.tracing_config}"
  }

  tags {
    "keepalive-concurrency" = "${var.keepalive_concurrency}"
  }

  lifecycle {
    # This tag is defaulted in terraform, but configured manually, so
    # ignore any changes
    ignore_changes = [
      "tags.keepalive-concurrency"
    ]
  }
}

# Permission for API gateway to invoke lambda function (For Non-VPC Lambda)
resource "aws_lambda_permission" "module_permission" {
  count         = "${var.attach_vpc_config == "true" ? 0 : 1}"
  action        = "lambda:InvokeFunction"
  function_name = "${aws_lambda_function.module_function.function_name}"
  principal     = "apigateway.amazonaws.com"

  # Allow any API gateway route to invoke
  source_arn = "${var.api_gateway_execution_arn}/*/*/*"
}

resource "aws_api_gateway_method" "module_method" {
  rest_api_id        = "${var.api_gateway_id}"
  resource_id        = "${var.api_gateway_resource_id}"
  http_method        = "${var.http_method}"
  authorization      = "${var.authorization}"
  authorizer_id      = "${var.authorizer_id}"
  request_parameters = "${var.request_parameters}"
}

# Integration between API gateway function and API method
resource "aws_api_gateway_integration" "module_integration" {
  rest_api_id             = "${var.api_gateway_id}"
  resource_id             = "${var.api_gateway_resource_id}"
  http_method             = "${var.http_method}"
  type                    = "AWS_PROXY"
  uri                     = "arn:aws:apigateway:${var.region}:lambda:path/2015-03-31/functions/arn:aws:lambda:${var.region}:${var.account_name}:function:${var.function_name}/invocations"
  integration_http_method = "POST"
  depends_on              = ["aws_api_gateway_method.module_method"]
}
