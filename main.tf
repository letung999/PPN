resource "aws_ssm_parameter" "ssm" {
  name  = "/ppn/silo-5/url-db"
  type  = "String"
  value = "none"
}