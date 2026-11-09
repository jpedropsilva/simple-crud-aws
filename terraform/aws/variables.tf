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