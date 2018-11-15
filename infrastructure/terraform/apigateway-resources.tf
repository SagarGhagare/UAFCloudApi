/*
  Defines API gateway resources.
  Resources themselves define only a route in API gateway.
  Resources are refererenced by method definitions in order to associate a HTTP verb with a particular resource route.
*/

# /regRequest
resource "aws_api_gateway_resource" "fidouaf_api_reg_request_resource" {
  rest_api_id = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  parent_id   = "${aws_api_gateway_rest_api.fidouaf_api.root_resource_id}"
  path_part   = "regRequest"
}

# /regResponse
resource "aws_api_gateway_resource" "fidouaf_api_reg_response_resource" {
  rest_api_id = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  parent_id   = "${aws_api_gateway_rest_api.fidouaf_api.root_resource_id}"
  path_part   = "regResponse"
}

# /authRequest
resource "aws_api_gateway_resource" "fidouaf_api_auth_request_resource" {
  rest_api_id = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  parent_id   = "${aws_api_gateway_rest_api.fidouaf_api.root_resource_id}"
  path_part   = "authRequest"
}

# /authResponse
resource "aws_api_gateway_resource" "fidouaf_api_auth_response_resource" {
  rest_api_id = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  parent_id   = "${aws_api_gateway_rest_api.fidouaf_api.root_resource_id}"
  path_part   = "authResponse"
}

# /deregRequest
resource "aws_api_gateway_resource" "fidouaf_api_dereg_request_resource" {
  rest_api_id = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  parent_id   = "${aws_api_gateway_rest_api.fidouaf_api.root_resource_id}"
  path_part   = "deregRequest"
}

# /uaf
resource "aws_api_gateway_resource" "fidouaf_api_uaf_resource" {
  rest_api_id = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  parent_id   = "${aws_api_gateway_rest_api.fidouaf_api.root_resource_id}"
  path_part   = "uaf"
}

# /uaf/facets
resource "aws_api_gateway_resource" "fidouaf_api_facets_resource" {
  rest_api_id = "${aws_api_gateway_rest_api.fidouaf_api.id}"
  parent_id   = "${aws_api_gateway_resource.fidouaf_api_uaf_resource.id}"
  path_part   = "facets"
}