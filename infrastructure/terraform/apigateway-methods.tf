module "fidouaf_api_reg_request_module" {
  # Same per module instance
  source                         = "./modules/api-lambda-method"
  account_name                   = "${local.account_name}"
  region                         = "${var.region}"
  api_gateway_id                 = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  function_bucket_name           = "${local.deployment_bucket_name}"
  function_environment_variables = "${local.lambda_function_env_vars}"
  function_role_arn              = "${aws_iam_role.fidouaf_lambda_role.arn}"
  api_gateway_execution_arn      = "${aws_api_gateway_rest_api.fidouaf_api.execution_arn}"
  subnet_ids                     = ["${local.function_subnet_ids}"]
  security_group_ids             = ["${local.function_security_group_ids}"]

  # Differ per module instance
  function_bucket_key     = "${aws_s3_bucket_object.fidouaf_api_jar.key}"
  api_gateway_resource_id = "${aws_api_gateway_resource.fidouaf_api_reg_request_resource.id}"
  function_name           = "${local.environment}-fidouaf-reg-request"
  function_handler        = "uk.nhs.digital.cid.fidouaf.handlers.RegistrationRequestHandler::handleRequest"
  http_method             = "GET"
  authorization           = "NONE"
  authorizer_id           = ""
  function_runtime        = "java8"
  tracing_config          = "Active"
}

module "fidouaf_api_reg_response_module" {
  # Same per module instance
  source                         = "./modules/api-lambda-method"
  account_name                   = "${local.account_name}"
  region                         = "${var.region}"
  api_gateway_id                 = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  function_bucket_name           = "${local.deployment_bucket_name}"
  function_environment_variables = "${local.lambda_function_env_vars}"
  function_role_arn              = "${aws_iam_role.fidouaf_lambda_role.arn}"
  api_gateway_execution_arn      = "${aws_api_gateway_rest_api.fidouaf_api.execution_arn}"
  subnet_ids                     = ["${local.function_subnet_ids}"]
  security_group_ids             = ["${local.function_security_group_ids}"]

  # Differ per module instance
  function_bucket_key     = "${aws_s3_bucket_object.fidouaf_api_jar.key}"
  api_gateway_resource_id = "${aws_api_gateway_resource.fidouaf_api_reg_response_resource.id}"
  function_name           = "${local.environment}-fidouaf-reg-response"
  function_handler        = "uk.nhs.digital.cid.fidouaf.handlers.RegistrationResponseHandler::handleRequest"
  http_method             = "POST"
  authorization           = "NONE"
  authorizer_id           = ""
  function_runtime        = "java8"
  tracing_config          = "Active"
}

module "fidouaf_api_auth_request_module" {
  # Same per module instance
  source                         = "./modules/api-lambda-method"
  account_name                   = "${local.account_name}"
  region                         = "${var.region}"
  api_gateway_id                 = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  function_bucket_name           = "${local.deployment_bucket_name}"
  function_environment_variables = "${local.lambda_function_env_vars}"
  function_role_arn              = "${aws_iam_role.fidouaf_lambda_role.arn}"
  api_gateway_execution_arn      = "${aws_api_gateway_rest_api.fidouaf_api.execution_arn}"
  subnet_ids                     = ["${local.function_subnet_ids}"]
  security_group_ids             = ["${local.function_security_group_ids}"]

  # Differ per module instance
  function_bucket_key     = "${aws_s3_bucket_object.fidouaf_api_jar.key}"
  api_gateway_resource_id = "${aws_api_gateway_resource.fidouaf_api_auth_request_resource.id}"
  function_name           = "${local.environment}-fidouaf-auth-request"
  function_handler        = "uk.nhs.digital.cid.fidouaf.handlers.AuthenticationRequestHandler::handleRequest"
  http_method             = "GET"
  authorization           = "NONE"
  authorizer_id           = ""
  function_runtime        = "java8"
  tracing_config          = "Active"
}

module "fidouaf_api_auth_response_module" {
  # Same per module instance
  source                         = "./modules/api-lambda-method"
  account_name                   = "${local.account_name}"
  region                         = "${var.region}"
  api_gateway_id                 = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  function_bucket_name           = "${local.deployment_bucket_name}"
  function_environment_variables = "${local.lambda_function_env_vars}"
  function_role_arn              = "${aws_iam_role.fidouaf_lambda_role.arn}"
  api_gateway_execution_arn      = "${aws_api_gateway_rest_api.fidouaf_api.execution_arn}"
  subnet_ids                     = ["${local.function_subnet_ids}"]
  security_group_ids             = ["${local.function_security_group_ids}"]

  # Differ per module instance
  function_bucket_key     = "${aws_s3_bucket_object.fidouaf_api_jar.key}"
  api_gateway_resource_id = "${aws_api_gateway_resource.fidouaf_api_auth_response_resource.id}"
  function_name           = "${local.environment}-fidouaf-auth-response"
  function_handler        = "uk.nhs.digital.cid.fidouaf.handlers.AuthenticationResponseHandler::handleRequest"
  http_method             = "POST"
  authorization           = "NONE"
  authorizer_id           = ""
  function_runtime        = "java8"
  tracing_config          = "Active"
}

module "fidouaf_api_dereg_request_module" {
  # Same per module instance
  source                         = "./modules/api-lambda-method"
  account_name                   = "${local.account_name}"
  region                         = "${var.region}"
  api_gateway_id                 = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  function_bucket_name           = "${local.deployment_bucket_name}"
  function_environment_variables = "${local.lambda_function_env_vars}"
  function_role_arn              = "${aws_iam_role.fidouaf_lambda_role.arn}"
  api_gateway_execution_arn      = "${aws_api_gateway_rest_api.fidouaf_api.execution_arn}"
  subnet_ids                     = ["${local.function_subnet_ids}"]
  security_group_ids             = ["${local.function_security_group_ids}"]

  # Differ per module instance
  function_bucket_key     = "${aws_s3_bucket_object.fidouaf_api_jar.key}"
  api_gateway_resource_id = "${aws_api_gateway_resource.fidouaf_api_dereg_request_resource.id}"
  function_name           = "${local.environment}-fidouaf-dereq-request"
  function_handler        = "uk.nhs.digital.cid.fidouaf.handlers.DeRegistrationRequestHandler::handleRequest"
  http_method             = "POST"
  authorization           = "NONE"
  authorizer_id           = ""
  function_runtime        = "java8"
  tracing_config          = "Active"
}

module "fidouaf_api_facets_get_module" {
  # Same per module instance
  source                         = "./modules/api-lambda-method"
  account_name                   = "${local.account_name}"
  region                         = "${var.region}"
  api_gateway_id                 = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  function_bucket_name           = "${local.deployment_bucket_name}"
  function_environment_variables = "${local.lambda_function_env_vars}"
  function_role_arn              = "${aws_iam_role.fidouaf_lambda_role.arn}"
  api_gateway_execution_arn      = "${aws_api_gateway_rest_api.fidouaf_api.execution_arn}"
  subnet_ids                     = ["${local.function_subnet_ids}"]
  security_group_ids             = ["${local.function_security_group_ids}"]

  # Differ per module instance
  function_bucket_key     = "${aws_s3_bucket_object.fidouaf_api_jar.key}"
  api_gateway_resource_id = "${aws_api_gateway_resource.fidouaf_api_facets_resource.id}"
  function_name           = "${local.environment}-fidouaf-facets-get"
  function_handler        = "uk.nhs.digital.cid.fidouaf.handlers.UafFacetHandler::handleRequest"
  http_method             = "GET"
  authorization           = "NONE"
  authorizer_id           = ""
  function_runtime        = "java8"
  tracing_config          = "Active"
}