# Variables for lambda api integration module
variable "account_name" {
  description = "The environment the system will be built in"
}

variable "region" {
  description = "The region that resources will reside"
}

variable "function_bucket_name" {}

variable "function_bucket_key" {}

variable "function_name" {}

variable "function_handler" {}

variable "api_gateway_execution_arn" {}

variable "api_gateway_id" {}

variable "api_gateway_resource_id" {}

variable "request_parameters" {
  type    = "map"
  default = {}
}

variable "http_method" {
  description = "HTTP verb; GET, POST etc"
}

variable "function_environment_variables" {
  type    = "map"
  default = {}
}

variable "function_role_arn" {}

variable "function_runtime" {
  default = "python3.6"
}

variable "function_memory" {
  default = "512"
}

variable "function_publish" {
  default = true
}

variable function_timeout {
  default = 20
}

variable "security_group_ids" {
  type = "list"
}

variable "subnet_ids" {
  type = "list"
}

variable "attach_vpc_config" {
  default = "false"
}

variable "authorization" {}

variable "authorizer_id" {}

variable "tracing_config" {
  default = "PassThrough"
}

variable "keepalive_concurrency" {
  default = 0
}
