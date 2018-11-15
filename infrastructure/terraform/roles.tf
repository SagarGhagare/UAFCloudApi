resource "aws_iam_role" "fidouaf_lambda_role" {
  name = "${local.environment}-fidouaf_lambda_role"

  assume_role_policy = "${file("./policies/fidouaf-lambda-role.json")}"
}



resource "aws_iam_role_policy" "fidouaf_lambda_role_policy" {
  name = "${local.environment}-fidouaf_lambda_role_policy"
  role = "${aws_iam_role.fidouaf_lambda_role.id}"

  policy = "${file("./policies/fidouaf-lambda-policy.json")}"
}