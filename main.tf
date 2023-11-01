resource "aws_ssm_parameter" "ssm" {
  name  = "/ppn/silo-5/url-db"
  type  = "String"
  value = "none"

  tags = {
    environment = "silo-5"
  }

  overwrite = true

  lifecycle {
    ignore_changes = [
      value,
      name,
      tags
    ]
  }
}