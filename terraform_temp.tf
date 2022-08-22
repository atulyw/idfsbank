module "mf" {
  source         = "../../modules/cloudfront"
  for_each       = toset(var.dds_apps)
  s3_bucket_name = each.key
  env            = var.env
  web_acl_id     = var.web_acl_id
  tags           = var.tags
}
