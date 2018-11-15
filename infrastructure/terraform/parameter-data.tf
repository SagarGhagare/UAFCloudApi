data "aws_ssm_parameter" "account_name" {
  name  = "/environment/${var.environment}/account_name"
}

data "aws_ssm_parameter" "artifact_account_name" {
  name  = "/environment/${var.environment}/artifact_account_name"
}

data "aws_ssm_parameter" "environment_region" {
  name  = "/environment/${var.environment}/environment_region"
}

data "aws_ssm_parameter" "environment" {
  name  = "/environment/${var.environment}/environment"
}

data "aws_ssm_parameter" "domain" {
  name  = "/environment/${var.environment}/domain"
}


locals {
  account_name = "${data.aws_ssm_parameter.account_name.value}"
  artifact_account_name = "${data.aws_ssm_parameter.artifact_account_name.value}"
  environment_region = "${data.aws_ssm_parameter.environment_region.value}"
  region = "${data.aws_ssm_parameter.environment_region.value}"
  environment = "${data.aws_ssm_parameter.environment.value}"
  domain = "${data.aws_ssm_parameter.domain.value}"
}