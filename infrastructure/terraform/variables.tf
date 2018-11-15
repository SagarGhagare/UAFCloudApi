variable "role_arn" {
  description = "The role to be assumed for deployment"
}

variable "role_session_name" {
  description = "The role session name defined"
}

variable "project" {
  description = "The parent project being deployed"
}

variable "subproject" {
  description = "The parent project being deployed"
}

variable "environment" {
  description = "The deployment environment"
}

variable "region" {
  description = "The region that resources will reside"
}

variable "artifact" {
  description = "The s3 directory that built artifacts will reside in"
}

