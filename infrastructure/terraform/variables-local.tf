# Interpolated variables available within the scope of the top-level terraform module

locals {
  deployment_bucket_name                                           = "${local.artifact_account_name}-lambda-deploy"
  fidouaf_api_jar_deployment_bucket_key                            = "${var.artifact}/fidouaf-api.jar"

  lambda_function_env_vars = {
    ENVIRONMENT               = "${local.environment}"
    AWS_REGION_NAME           = "${local.environment_region}"
    UAF_BASE_URL              = "https://uaf.${local.domain}"
    FIDO_REGISTRATIONS_TABLE  = "${local.account_name}-${local.environment}-fidouaf-registrations"
    LOG_LEVEL                 = "INFO"
    SIGNATURES_TABLE_NAME     = "${local.account_name}-${local.environment}-fidouaf-signature"
	  FIDO_EXPIRY_MSECS         = "20"
    SECRET_KEY_NAME           = ""
  }
}