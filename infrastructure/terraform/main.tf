provider "aws" {
  region = "${var.region}"

  assume_role {
    role_arn     = "${var.role_arn}"
    session_name = "${var.role_session_name}"
  }
}

# Backend must not be configured here for this to work as part of the pipeline

terraform {
  backend "s3"    {}
  region  = "${local.environment_region}"

  # key and bucket are specified by -backend-config flags when remote state is initialised by terraform task
}