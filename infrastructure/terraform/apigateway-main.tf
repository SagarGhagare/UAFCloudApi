# Gateway root
resource "aws_api_gateway_rest_api" "fidouaf_api" {
  name                     = "${local.environment}-fidouaf-api"
  description              = "API for Fingerprint registration and authentication"
  minimum_compression_size = "1000"

  endpoint_configuration {
    types = ["REGIONAL"]
  }
}

# creates a base path mapping to link the pre-registered API domain to this
# API for a specific stage under a specific path, e.g:
# api.dev.signin.nhs.uk/account
resource "aws_api_gateway_base_path_mapping" "fidouaf_api_base_path_mapping" {
  api_id      = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  stage_name  = "${aws_api_gateway_deployment.fidouaf_api_deployment.stage_name}"
  domain_name = "uaf.${local.domain}"
}

# Deployment
resource "aws_api_gateway_deployment" "fidouaf_api_deployment" {
  depends_on = [
    "module.fidouaf_api_reg_request_module",                     # Depend on every module to prevent premature deployment
    "module.fidouaf_api_reg_response_module",
    "module.fidouaf_api_auth_request_module",
    "module.fidouaf_api_auth_response_module",
    "module.fidouaf_api_dereg_request_module",
    "module.fidouaf_api_facets_get_module"
  ]

  rest_api_id = "${aws_api_gateway_rest_api.fidouaf_api.id}"

  stage_name = "default"

  variables {
    deployed_at = "${timestamp()}" ## change something every time to force the deployment
  }

  # Lifecycle rule required to fix problems with terraform apply in cases where
  # this deployment stage is linked to a API GW domain with base path mappings (which ours is)
  # see https://github.com/hashicorp/terraform/issues/10674
  lifecycle {
    create_before_destroy = true
  }
}
