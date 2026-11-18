variable "db_password" {
  description = "PostgreSQL password"
  type        = string
  sensitive   = true
}

variable "db_username" {
  description = "PostgreSQL user"
  type        = string
  default     = "simple_crud_user"
}

variable "domain_name" {
  type = string
}

variable "initial_container_image" {
  description = "Image used for the first task definition. Later deploys are done by GitHub Actions."
  type        = string
  default     = "750686145863.dkr.ecr.eu-west-3.amazonaws.com/simple-crud:8472d2cf77def1468a7ebbc49b780246cb4d1119"
}

variable "github_repository" {
  description = "GitHub repository (owner/name) allowed to deploy through OIDC"
  type        = string
  default     = "jpedropsilva/simple-crud-aws"
}

variable "github_oidc_subject" {
  description = "Exact 'sub' claim of the GitHub OIDC token allowed to deploy"
  type        = string
  default     = "repo:jpedropsilva@20337098/simple-crud-aws@1425747056:ref:refs/heads/main"
}